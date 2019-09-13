import br.com.cronapi.DatabaseTest;
import br.com.cronapi.RestClientTest;
import br.com.cronapi.VarTest;
import br.com.cronapi.dateTime.DateTimeTest;
import br.com.cronapi.email.EmailTest;
import br.com.cronapi.json.JsonTest;
import br.com.cronapi.list.ListTest;
import br.com.cronapi.logic.LogicTest;
import br.com.cronapi.map.MapTest;
import br.com.cronapi.math.MathTest;
import br.com.cronapi.odata.DatasourceExtensionTest;
import br.com.cronapi.rest.security.CronappSecurityTest;
import br.com.cronapi.util.SecurityUtilTest;
import br.com.cronapi.util.UtilTest;
import br.com.cronapi.xml.XmlTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        DatabaseTest.class, DateTimeTest.class, EmailTest.class, JsonTest.class,
        ListTest.class, LogicTest.class, MapTest.class, MathTest.class, //DatasourceExtensionTest.class,
        CronappSecurityTest.class, UtilTest.class, SecurityUtilTest.class,
        XmlTest.class, DatabaseTest.class,
        RestClientTest.class, VarTest.class
        })
public class AllTests {
}
