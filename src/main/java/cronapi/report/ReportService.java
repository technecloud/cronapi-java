package cronapi.report;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.stimulsoft.base.exception.StiException;
import com.stimulsoft.base.serializing.StiDeserializationException;
import com.stimulsoft.report.StiOptions.Services;
import com.stimulsoft.report.StiReport;
import com.stimulsoft.report.StiSerializeManager;
import com.stimulsoft.report.enums.StiExportFormat;
import com.stimulsoft.report.export.service.StiExportService;
import cronapi.CronapiConfigurator;
import cronapi.QueryManager;
import cronapi.report.DataSourcesInBand.FieldParam;
import cronapi.report.DataSourcesInBand.ParamValue;
import cronapi.report.odata.StiODataDatabase;
import cronapi.report.odata.StiODataSource;
import cronapi.rest.DownloadREST;
import cronapp.reports.PrintDesign;
import cronapp.reports.ReportExport;
import cronapp.reports.ReportManager;
import cronapp.reports.commons.Functions;
import cronapp.reports.commons.Parameter;
import cronapp.reports.commons.ParameterType;
import cronapp.reports.commons.ReportFront;
import cronapp.reports.j4c.dataset.J4CDataset;
import cronapp.reports.j4c.dataset.J4CEntity;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import org.eclipse.persistence.config.PersistenceUnitProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.sql.DataSource;
import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ReportService {

  static {
    Services.getDataSource().add(StiODataSource.class);
    Services.getDataBases().add(StiODataDatabase.class);
  }

	private static final Logger log = LoggerFactory.getLogger(ReportService.class);

	private final ClassLoader loader;

	ReportService() {
		this.loader = Thread.currentThread().getContextClassLoader();
	}

	public ReportFront getReport(String reportName) {
		ReportFront reportResult = new ReportFront(reportName);
		try {
			if (reportName.contains("jrxml")) {
				log.info("Report in design mode, build the parameters...");
				InputStream inputStream = this.getInputStream(reportName);
				JasperDesign jasperDesign = JRXmlLoader.load(inputStream);
				Stream.of(jasperDesign.getParameters()).filter(jrParameter -> !jrParameter.isSystemDefined())
						.filter(jrParameter -> !jrParameter.getName().contains("image_"))
						.filter(jrParameter -> !jrParameter.getName().contains("sub_")).forEach(jrParameter -> {
					Parameter parameter = new Parameter();
					parameter.setName(jrParameter.getName());
					parameter.setType(ParameterType.toType(jrParameter.getValueClass()));
					parameter.setDescription(jrParameter.getDescription());
					JRExpression expression = jrParameter.getDefaultValueExpression();
					if (expression != null) {
            parameter.setValue(expression.getText());
          }
					reportResult.addParameter(parameter);
				});
			}
		} catch (JRException e) {
			log.error("Problems to make JasperDesign object.");
			throw new RuntimeException(e);
		}
		return reportResult;
	}

	public String getContentReport(String reportName) {
		try (InputStream inputStream = this.getInputStream(reportName)) {
			try (BufferedReader buffer = new BufferedReader(new InputStreamReader(inputStream, CronapiConfigurator.ENCODING))) {
				return buffer.lines().collect(Collectors.joining("\n"));
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public DataSourcesInBand getDataSourcesParams(DataSourcesInBand dataSourcesInBand) {
		dataSourcesInBand.getDatasources().forEach(dsp -> {
			JsonObject currentCustomQuery = null;
			try {
				currentCustomQuery = QueryManager.getQuery(dsp.getCustomId());
			} catch (Exception e) {
        log.error(e.getMessage());
			}
			List<ParamValue> dsParams = getDataSourceParamsFromCustomQuery(currentCustomQuery);
			dsParams.forEach(param -> {
				//Se nao existir nos parametros, adiciona
				Optional<FieldParam> exist = dsp.getFieldParams().stream().filter(fp -> param.getFieldName().equals(fp.getField())).findAny();
				if (!exist.isPresent()) {
          dsp.getFieldParams().add(new FieldParam(param.getFieldName(), param.getFieldName(), "String", ""));
        }
				dsp.getQueryParams().add(new ParamValue(param.getFieldName(), ":" + param.getFieldName()));
			});
			if (dsp.getFieldParams().size() > 0) {
        dataSourcesInBand.setHasParam(true);
      }
		});
		return dataSourcesInBand;
	}

	public String getPDFAsFile(ReportFront reportFront) {
		ReportExport result = this.getReportExport(reportFront);
    if (result == null) {
      return "";
    }
    result.exportReportToPdfFile();
		return DownloadREST.getDownloadUrl(new File(result.getTargetFile()));
	}

	public byte[] getPDF(ReportFront reportFront) {
		ReportExport result = this.getReportExport(reportFront);
		if (result == null) {
      return new byte[0];
    }
		return result.toPDF();
	}

	ReportExport getReportExport(ReportFront reportFront, File file) {
		ReportExport result = null;
		File pdf;
    try {
      if (file == null) {
        pdf = DownloadREST.getTempFile(UUID.randomUUID().toString() + ".pdf");
        if (pdf.createNewFile()) {
          log.info("Temporary report file created.");
        }
      } else {
        pdf = file;
      }
    } catch (IOException e) {
      log.error("Problems to make the temporary report file.");
      throw new RuntimeException(e);
    }

    InputStream inputStream = this.getInputStream(reportFront.getReportName());

    String reportName = reportFront.getReportName();
    if (reportName.contains("jrxml")) {
      ReportManager reportManager = ReportManager.newPrint(inputStream, pdf.getAbsolutePath());
      PrintDesign printDesign = reportManager.byDesign(reportFront.getParameters()).updateParameters()
          .updateImages().updateSubreports();

      J4CDataset dataset = printDesign.getCollectionDataset();
      if (dataset == null) {
        String datasource = printDesign.getDatasource();
        try (Connection connection = this.getConnection(datasource)) {
          result = printDesign.print(connection);
        } catch (SQLException e) {
          throw new RuntimeException(e);
        }
      } else {
        J4CEntity entity = dataset.getEntity();
        String jpql = entity.getJpql();
        if (Functions.isExists(jpql)) {
          String persistenceUnit = dataset.getPersistenceUnitName();
          EntityManager entityManager = this.getEntityManager(persistenceUnit);

          Map<String, Object> printParameters = printDesign.getPrintParameters();

          Query queryObject = entityManager.createQuery(jpql);

          Set<javax.persistence.Parameter<?>> objectParameters = queryObject.getParameters();
          Set<String> parameterNames = objectParameters.stream().map(javax.persistence.Parameter::getName)
              .collect(Collectors.toSet());

          Set<Map.Entry<String, Object>> entrySet = printParameters.entrySet();
          for (Map.Entry<String, Object> item : entrySet) {
            String name = item.getKey();
            if (parameterNames.contains(name)) {
              Object value = item.getValue();
              queryObject.setParameter(name, value);
            }
          }

          List resultList = Collections.emptyList();
          try {
            resultList = queryObject.getResultList();
          } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
          }
          result = printDesign.print(resultList);
        }
      }
    }
    return result;
	}

  /**
   * TODO adicionar mais um parametro: params, para passar para o datasource do report,
   * de acordo com o StimulsoftHelper
   */
	void exportStimulsoftReportToPdfFile(String reportName, File file) {
	  try {
      InputStream inputStream = this.getInputStream(reportName);

      StiReport stiReport = StiSerializeManager.deserializeReport(inputStream);
      stiReport.getReferencedAssemblies().clear();
      stiReport.setScript(null);
      stiReport.Render();

      StiExportFormat exportFormat = StiExportFormat.Pdf;
      StiExportService exportService = exportFormat.getExportService();

      if (file.createNewFile()) {
        log.info("Output report file created.");
      }

      exportService.export(stiReport, new FileOutputStream(file), exportFormat.getDefaultExportSettings());
    } catch (IOException | SAXException | StiDeserializationException | StiException e) {
      log.error("Problems exporting stimulsoft report to pdf file.");
      throw new RuntimeException(e);
	  }
  }

  private ReportExport getReportExport(ReportFront reportFront) {
    return this.getReportExport(reportFront, null);
  }

  private List<ParamValue> getDataSourceParamsFromCustomQuery(JsonObject customQuery) {
    List<ParamValue> params = new ArrayList<>();
    if (customQuery != null) {
      for (JsonElement queryParamsValues : customQuery.get("queryParamsValues").getAsJsonArray()) {
        JsonObject paramNameValue = queryParamsValues.getAsJsonObject();
        ParamValue paramValue = new ParamValue();
        paramValue.setFieldName(paramNameValue.get("fieldName").getAsString());
        params.add(paramValue);
      }
    }
    return params;
  }

  private EntityManager getEntityManager(String persistenceUnit) {
		HashMap<String, Object> properties = new HashMap<>();
		properties.put(PersistenceUnitProperties.JTA_DATASOURCE, persistenceUnit);
		EntityManagerFactory managerFactory = Persistence.createEntityManagerFactory(persistenceUnit, properties);
		return managerFactory.createEntityManager();
	}

	private Connection getConnection(String datasource) {
		if (datasource != null && !datasource.isEmpty() && !"null".equals(datasource)) {
			javax.naming.Context context = null;
			DataSource dataSource = null;
			try {
				context = (javax.naming.Context) new InitialContext().lookup("java:/comp/env");
				dataSource = (DataSource) context.lookup(datasource);
			} catch (NamingException e) {
				try {
					if (context != null) {
            dataSource = (DataSource) context.lookup(datasource.toLowerCase());
          }
				} catch (NamingException e1) {
					throw new RuntimeException(
							new Exception("Connection context not found.\nError: " + e.getMessage()));
				}
			}
			try {
				if (dataSource != null) {
          return dataSource.getConnection();
        }
			} catch (SQLException e) {
				throw new RuntimeException(
						new Exception("Trouble getting a connection from the context.\nError: " + e.getMessage()));
			}
		}
		return null;
	}

  private InputStream getInputStream(String reportName) {
    InputStream inputStream = loader.getResourceAsStream(reportName);
    if (inputStream == null) {
      throw new RuntimeException("File [" + reportName + "] not found.");
    }
    return inputStream;
  }
}