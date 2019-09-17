package br.com.cronapi;

import cronapi.Var;
import cronapi.database.Operations;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.fail;


public class DatabaseTest {

  @Test
  public void QAIBT_1436() {
    try {
      Operations.executeQuery(
          Var.valueOf("app.entity.User"),
          Var.valueOf("select u from User u"),
          Var.VAR_NULL
      );
      fail();
    } catch (Exception e) {
      assertFalse(e instanceof NullPointerException);
    }
  }
}
