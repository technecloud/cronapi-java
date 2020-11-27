package cronapi.report;

import com.sun.xml.bind.v2.TODO;
import cronapi.CronapiMetaData;
import cronapi.CronapiMetaData.CategoryType;
import cronapi.CronapiMetaData.ObjectType;
import cronapi.ParamMetaData;
import cronapi.Var;
import cronapp.reports.ReportExport;
import cronapp.reports.commons.Parameter;
import cronapp.reports.commons.ParameterType;
import cronapp.reports.commons.ReportFront;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Classe que representa ...
 *
 * @author Usuário de Teste
 * @version 1.0
 * @since 2017-12-04
 */

@CronapiMetaData(category = CategoryType.UTIL, categoryTags = {"Report", "Relatório"})
public class Operations {

  /**
   * Construtor
   **/
  public Operations() {

  }

  @CronapiMetaData(type = "function", name = "{{generateReport}}", nameTags = {"generateReport",
      "GerarRelatorio"}, description = "{{generateReportDescription}}", returnType = ObjectType.OBJECT, wizard = "procedures_generatereport_callreturn")
  public static final Var generateReport(@ParamMetaData(blockType = "util_report_list", type = ObjectType.STRING, description = "{{report}}") Var reportName,
                                         @ParamMetaData(type = ObjectType.STRING, description = "{{path}}") Var path) throws Exception {
    return generateReport(reportName, path, Var.VAR_NULL, Var.VAR_NULL, true);
  }

  @CronapiMetaData(type = "function", name = "{{generateReportWithParam}}", nameTags = {"generateReport",
      "GerarRelatorio"}, description = "{{generateReportDescription}}", returnType = ObjectType.OBJECT)
  public static final Var generateReportWithParam(@ParamMetaData(type = ObjectType.STRING, description = "{{report}}") Var reportName,
                                                  @ParamMetaData(type = ObjectType.STRING, description = "{{type}}", blockType = "util_dropdown", keys = {
                                                      "pdf", "html" }, values = {"PDF", "HTML"}) Var type,
                                                  @ParamMetaData(type = ObjectType.STRING, description = "{{path}}") Var path,
                                                  @ParamMetaData(type = ObjectType.MAP, description = "{{params}}") Var params) throws Exception {
    return generateReport(reportName, path, params, type,false);
  }

  @CronapiMetaData(type = "function", name = "{{generateReportWithJsonContent}}", nameTags = {"generateReport",
      "GerarRelatorio"}, description = "{{generateReportDescription}}", returnType = ObjectType.OBJECT)
  public static final Var generateReportWithJsonContent(@ParamMetaData(type = ObjectType.STRING, description = "{{reportContent}}") Var reportContent,
                                                        @ParamMetaData(type = ObjectType.STRING, description = "{{type}}", blockType = "util_dropdown", keys = {
                                                        "pdf", "html" }, values = {"PDF", "HTML"}) Var type,
                                                        @ParamMetaData(type = ObjectType.STRING, description = "{{path}}") Var path,
                                                        @ParamMetaData(type = ObjectType.MAP, description = "{{params}}") Var params) throws Exception {
    return generateStimulsoftReport(reportContent, path, params, type);
  }

  private static Map normalizeParameters(Var params) {

    Map<String, Object> parameters = new HashMap<>();

    if (params.getType() == Var.Type.LIST) {
      for (Object param : params.getObjectAsList()) {
        if (!Var.valueOf(param).isEmptyOrNull())
          parameters.put(Var.valueOf(param).getId(), Var.valueOf(param).getObjectAsString());
      }
    } else {
      for (Object  entry: params.getObjectAsMap().entrySet()) {
        Map.Entry<String, Var> m = (Map.Entry<String, Var>) entry;
        parameters.put(m.getKey(), m.getValue().getObject());
      }
    }

    return parameters;
  }

  public static final Var generateStimulsoftReport(Var reportContent, Var path, Var params, Var type) throws Exception {

    if (!reportContent.isEmptyOrNull() && !path.isEmptyOrNull()) {

      String typeVar = "pdf";
      if (!type.isEmptyOrNull()) typeVar = type.getObjectAsString();

      File file = new File(path.getObjectAsString());
      ReportService service = new ReportService();

      service.exportStimulsoftReportContentToFile(reportContent.getObjectAsString(), file, normalizeParameters(params), typeVar, false);
      return Var.valueOf(file);

    } else {
      throw new RuntimeException("Error without parameters/content");
    }

  }

  public static final Var generateReport(Var reportName, Var path, Var params, Var type, Boolean legacy) {
    File file;
    if (!reportName.isNull() || !path.isNull()) {
      ReportService service = new ReportService();
      if (reportName.getObjectAsString().endsWith(".report")) {
        file = new File(path.getObjectAsString());

        Map<String, String> parameters = normalizeParameters(params);

        if (legacy) {
          service.exportStimulsoftReportToPdfFile(reportName.getObjectAsString(), file, parameters);
        } else {
          service.exportStimulsoftReportToFile(reportName.getObjectAsString(), file, parameters, type.getObjectAsString(), false);
        }

      } else {
        ReportFront reportFront = service.getReport(reportName.getObjectAsString());
        if (params != Var.VAR_NULL && params.size() > 0) {
          for (Object param : params.getObjectAsList()) {
            Parameter parameter = new Parameter();
            parameter.setName(Var.valueOf(param).getId());
            parameter.setType(ParameterType.toType(Var.valueOf(param).getObject().getClass()));
            parameter.setValue(Var.valueOf(param).getObjectAsString());
            reportFront.addParameter(parameter);
          }
        }
        file = new File(path.getObjectAsString());
        ReportExport export = service.getReportExport(reportFront, file);
        if (export != null) {
          export.exportReportToPdfFile();
        } else {
          throw new RuntimeException("Error while exporting report [" + reportName.getObjectAsString() + "]");
        }
      }
    } else {
      throw new RuntimeException("Error without parameters");
    }
    return Var.valueOf(file);
  }
}