package br.com.cronapi.util;

import cronapi.Var;
import cronapi.util.Operations;
import org.junit.Assert;
import org.junit.Test;

public class UtilTest {

  @Test
  public void isUUID() {
    Var uuid = Operations.generateUUID();
    Assert.assertFalse(uuid.getObjectAsString().startsWith("\""));
  }

  @Test
  public void hasParams() {
    Var method = Var.valueOf("POST");
    //Var contentType = Var.valueOf("application/json");
    Var contentType = Var.valueOf("application/json");
    Var address = Var.valueOf("https://reqres.in/api/users/2");
    try {
      Var params = Var.valueOf(cronapi.map.Operations.createObjectMapWith(Var.valueOf("name", Var.valueOf("morpheus"))));
      //Var params = Var.VAR_NULL;
      Var cookieContainer = Var.valueOf(Var.VAR_NULL);
      Var postData = Var.valueOf(cronapi.map.Operations.createObjectMapWith(Var.valueOf("job", Var.valueOf("zion resident"))));
      //Var postData = Var.VAR_NULL;
      //Var postData = Var.valueOf("{\"LOGUSR\":\"ADMIN\",\"PSSUSER\":\"ADMIN\",\"CODUSR\":\"000197\",\"USRTKN\":\"\",\"CLIENT\":\"\",\"CAMPHA\":\"\",\"DTRANG\":\"\",\"STATUS\":\"\"}");
      Var data = Operations.getURLFromOthers(method, contentType, address, params, cookieContainer, postData);
      Assert.assertFalse(data.isEmptyOrNull());
    } catch (Exception e) {
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
}
