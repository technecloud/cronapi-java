package cronapi.odata.server;

import org.eclipse.persistence.internal.jpa.deployment.PersistenceUnitProcessor;
import org.eclipse.persistence.internal.jpa.deployment.SEPersistenceUnitInfo;
import org.eclipse.persistence.jpa.Archive;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.persistence.Persistence;
import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;
import java.util.List;
import java.util.Set;

@Configuration
@DependsOn("cronappEntityManagerFactory")
@ComponentScan(basePackages = {
    "cronapi.odata.server",
})
public class ODataConfiguration implements ServletContextInitializer {

  public static final String SERVICE_URL = "/api/cronapi/odata/v2/";

  @Override
  public void onStartup(ServletContext servletContext) {
    registerServlet(servletContext);
  }

  private void registerServlet(ServletContext servletContext) {

    Set<Archive> archives = PersistenceUnitProcessor.findPersistenceArchives();

    int i = 0;
    for (Archive archive : archives) {

      List<SEPersistenceUnitInfo> persistenceUnitInfos = PersistenceUnitProcessor.getPersistenceUnits(archive, Thread.currentThread().getContextClassLoader());

      persistenceUnitInfos.sort((p1, p2) -> {
        if (p1.getPersistenceUnitName().equals("app")) {
          return -1;
        }
        if (p2.getPersistenceUnitName().equals("app")) {
          return 1;
        }

        return p1.getPersistenceUnitName().compareTo(p2.getPersistenceUnitName());
      });

      for (SEPersistenceUnitInfo pui : persistenceUnitInfos) {

        String namespace = pui.getPersistenceUnitName();

        ODataServletV2 servlet = new ODataServletV2(Persistence.createEntityManagerFactory(namespace), namespace, i);

        ServletRegistration.Dynamic serviceServlet = servletContext.addServlet("ServiceOData" + namespace, servlet);

        serviceServlet.addMapping(SERVICE_URL + namespace + "/*");
        serviceServlet.setAsyncSupported(true);
        serviceServlet.setLoadOnStartup(2);

        i++;
      }
    }
  }
}
