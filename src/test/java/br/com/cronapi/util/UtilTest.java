package br.com.cronapi.util;

import cronapi.RestClient;
import cronapi.Utils;
import cronapi.Var;
import cronapi.util.Operations;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class UtilTest {

    private static final String MOCK_TESTE = "teste";

    @Test
    public void isUUID() {
        Var uuid = Operations.generateUUID();
        Assert.assertFalse(uuid.getObjectAsString().startsWith("\""));
    }

    @Test
    public void hasParamsPost() throws Exception {
        Var method = Var.valueOf("POST");
        Var contentType = Var.valueOf("application/json");
        Var address = Var.valueOf("https://reqres.in/api/users/2");
        Var params = Var.valueOf(cronapi.map.Operations.createObjectMapWith(Var.valueOf("name", Var.valueOf("morpheus"))));
        Var cookieContainer = Var.valueOf(Var.VAR_NULL);
        Var postData = Var.valueOf(cronapi.map.Operations.createObjectMapWith(Var.valueOf("job", Var.valueOf("zion resident"))));
        Var data = Operations.getURLFromOthers(method, contentType, address, params, cookieContainer, postData);
        Assert.assertFalse(data.isEmptyOrNull());
    }

    @Test
    public void hasParamsPut() throws Exception {
        Var method = Var.valueOf("PUT");
        Var contentType = Var.valueOf("application/json");
        Var address = Var.valueOf("https://reqres.in/api/users/2");
        Var params = Var.valueOf(cronapi.map.Operations.createObjectMapWith(Var.valueOf("name", Var.valueOf("morpheus"))));
        Var cookieContainer = Var.valueOf(Var.VAR_NULL);
        Var postData = Var.valueOf(cronapi.map.Operations.createObjectMapWith(Var.valueOf("job", Var.valueOf("zion resident"))));
        Var data = Operations.getURLFromOthers(method, contentType, address, params, cookieContainer, postData);
        Assert.assertFalse(data.isEmptyOrNull());
    }
  }

  @Test
  public void log() {
    final StringBuilder out = new StringBuilder();

    ConsoleHandler handler = new ConsoleHandler() {
      @Override
      public void publish(LogRecord record) {
        out.append(record.getMessage());
        if (record.getThrown() != null) {
          out.append(record.getThrown().getMessage());
        }
      }
    };

    Logger logger = Logger.getLogger("Tests");
    logger.setLevel(Level.ALL);
    handler.setLevel(Level.ALL);
    logger.setUseParentHandlers(false);
    logger.addHandler(handler);

    Operations.LOGGERS.put("Tests", logger);

    Operations.log(Var.valueOf("Tests"), Var.valueOf("INFO"), Var.valueOf("Log Message"), Var.VAR_NULL);
    Assert.assertTrue(out.indexOf("Log Message") != -1);

    out.delete(0, out.length());

    logger.setLevel(Level.INFO);
    handler.setLevel(Level.INFO);

    Operations.log(Var.valueOf("Tests"), Var.valueOf("SEVERE"), Var.valueOf("Log Error"), Var.valueOf(new Exception("Error Exception")));
    Assert.assertTrue(out.indexOf("Error Exception") != -1);

    out.delete(0, out.length());

    Operations.log(Var.valueOf("Tests"), Var.valueOf("FINE"), Var.valueOf("Log Message"), Var.VAR_NULL);
    Assert.assertFalse(out.indexOf("Log Message") != -1);

  }
    @Test
    public void hasParamsPatch() throws Exception {
        Var method = Var.valueOf("PATCH");
        Var contentType = Var.valueOf("application/json");
        Var address = Var.valueOf("https://reqres.in/api/users/2");
        Var params = Var.valueOf(cronapi.map.Operations.createObjectMapWith(Var.valueOf("name", Var.valueOf("morpheus"))));
        Var cookieContainer = Var.valueOf(Var.VAR_NULL);
        Var postData = Var.valueOf(cronapi.map.Operations.createObjectMapWith(Var.valueOf("job", Var.valueOf("zion resident"))));
        Var data = Operations.getURLFromOthers(method, contentType, address, params, cookieContainer, postData);
        Assert.assertFalse(data.isEmptyOrNull());
    }

    @Test
    public void hasParamsGet() throws Exception {
        Var method = Var.valueOf("GET");
        Var contentType = Var.valueOf("application/json");
        Var address = Var.valueOf("https://reqres.in/api/users/2");
        Var params = Var.valueOf(cronapi.map.Operations.createObjectMapWith(Var.valueOf("name", Var.valueOf("morpheus"))));
        Var cookieContainer = Var.valueOf(Var.VAR_NULL);
        Var postData = Var.valueOf(cronapi.map.Operations.createObjectMapWith(Var.valueOf("job", Var.valueOf("zion resident"))));
        Var data = Operations.getURLFromOthers(method, contentType, address, params, cookieContainer, postData);
        Assert.assertFalse(data.isEmptyOrNull());
    }

    @Test
    public void getCurrentUserName() throws Exception {
        Assert.assertNull(Operations.getCurrentUserName().getObject());
        RestClient.setRestClient(Mockito.mock(RestClient.class));
        RestClient.getRestClient().setUser(new User(MOCK_TESTE, MOCK_TESTE, new ArrayList<>()));
        Mockito.when(RestClient.getRestClient().getUser()).thenReturn(new User(MOCK_TESTE, MOCK_TESTE, new ArrayList<>()));
        Assert.assertEquals(Operations.getCurrentUserName().getObjectAsString(), MOCK_TESTE);
    }

    @Test
    public void random() throws Exception {
        Var data = Operations.random(Var.valueOf("10"));
        Assert.assertFalse(data.isEmptyOrNull());
    }

    @Test
    public void safeNameForMethodBlockly() {
        String data = Operations.safeNameForMethodBlockly(MOCK_TESTE);
        Assert.assertEquals(data, MOCK_TESTE);
        data = Operations.safeNameForMethodBlockly(null);
        Assert.assertEquals(data, "unnamed");
        data = Operations.safeNameForMethodBlockly("123456789");
        Assert.assertEquals(data, "my_123456789");
        String stringValue = "Ã§^aÃ£";//ç^aã -- ISO-8859-1
        String deserializedStringValue = new String(stringValue.getBytes(StandardCharsets.ISO_8859_1));
        data = Operations.safeNameForMethodBlockly(deserializedStringValue);
        Assert.assertEquals(data, "_C3_A7_5Ea_C3_A3");
    }

    @Test
    public void testPassword() throws Exception {
        BCryptPasswordEncoder a = new BCryptPasswordEncoder();
        Var data = Operations.matchesencryptPassword(Var.valueOf("cronapp"), Var.valueOf(a.encode("cronapp")));
        Assert.assertTrue(data.getObjectAsBoolean());
        data = Operations.encryptPassword(Var.valueOf("cronapp"));
        data = Operations.matchesencryptPassword(Var.valueOf("cronapp"), data);
        Assert.assertTrue(data.getObjectAsBoolean());
    }

    @Test
    public void encodeMD5() throws Exception {
        String md5 = Utils.encodeMD5("cronapp");
        Var retorno = Operations.encodeMD5(Var.valueOf("cronapp"));
        Assert.assertEquals(retorno.getObjectAsString(), md5);
    }

        Var data = Operations.getURLFromOthers(method, contentType, address, params, cookieContainer, postData);
        Assert.assertFalse(data.isEmptyOrNull());
    }

    @Test
    public void hasParamsPatch() throws Exception {
        Var method = Var.valueOf("PATCH");
        Var contentType = Var.valueOf("application/json");
        Var address = Var.valueOf("https://reqres.in/api/users/2");
        Var params = Var.valueOf(cronapi.map.Operations.createObjectMapWith(Var.valueOf("name", Var.valueOf("morpheus"))));
        Var cookieContainer = Var.valueOf(Var.VAR_NULL);
        Var postData = Var.valueOf(cronapi.map.Operations.createObjectMapWith(Var.valueOf("job", Var.valueOf("zion resident"))));
        Var data = Operations.getURLFromOthers(method, contentType, address, params, cookieContainer, postData);
        Assert.assertFalse(data.isEmptyOrNull());
    }

    @Test
    public void hasParamsGet() throws Exception {
        Var method = Var.valueOf("GET");
        Var contentType = Var.valueOf("application/json");
        Var address = Var.valueOf("https://reqres.in/api/users/2");
        Var params = Var.valueOf(cronapi.map.Operations.createObjectMapWith(Var.valueOf("name", Var.valueOf("morpheus"))));
        Var cookieContainer = Var.valueOf(Var.VAR_NULL);
        Var postData = Var.valueOf(cronapi.map.Operations.createObjectMapWith(Var.valueOf("job", Var.valueOf("zion resident"))));
        Var data = Operations.getURLFromOthers(method, contentType, address, params, cookieContainer, postData);
        Assert.assertFalse(data.isEmptyOrNull());
    }

    @Test
    public void getCurrentUserName() throws Exception {
        Assert.assertNull(Operations.getCurrentUserName().getObject());
        RestClient.setRestClient(Mockito.mock(RestClient.class));
        RestClient.getRestClient().setUser(new User(MOCK_TESTE, MOCK_TESTE, new ArrayList<>()));
        Mockito.when(RestClient.getRestClient().getUser()).thenReturn(new User(MOCK_TESTE, MOCK_TESTE, new ArrayList<>()));
        Assert.assertEquals(Operations.getCurrentUserName().getObjectAsString(), MOCK_TESTE);
    }

    @Test
    public void random() throws Exception {
        Var data = Operations.random(Var.valueOf("10"));
        Assert.assertFalse(data.isEmptyOrNull());
    }

    @Test
    public void safeNameForMethodBlockly() {
        String data = Operations.safeNameForMethodBlockly(MOCK_TESTE);
        Assert.assertEquals(data, MOCK_TESTE);
        data = Operations.safeNameForMethodBlockly(null);
        Assert.assertEquals(data, "unnamed");
        data = Operations.safeNameForMethodBlockly("123456789");
        Assert.assertEquals(data, "my_123456789");
        String stringValue = "Ã§^aÃ£";//ç^aã -- ISO-8859-1
        String deserializedStringValue = new String(stringValue.getBytes(StandardCharsets.ISO_8859_1));
        data = Operations.safeNameForMethodBlockly(deserializedStringValue);
        Assert.assertEquals(data, "_C3_A7_5Ea_C3_A3");
    }

    @Test
    public void testPassword() throws Exception {
        BCryptPasswordEncoder a = new BCryptPasswordEncoder();
        Var data = Operations.matchesencryptPassword(Var.valueOf("cronapp"), Var.valueOf(a.encode("cronapp")));
        Assert.assertTrue(data.getObjectAsBoolean());
        data = Operations.encryptPassword(Var.valueOf("cronapp"));
        data = Operations.matchesencryptPassword(Var.valueOf("cronapp"), data);
        Assert.assertTrue(data.getObjectAsBoolean());
    }

    @Test
    public void encodeMD5() throws Exception {
        String md5 = Utils.encodeMD5("cronapp");
        Var retorno = Operations.encodeMD5(Var.valueOf("cronapp"));
        Assert.assertEquals(retorno.getObjectAsString(), md5);
    }

}
