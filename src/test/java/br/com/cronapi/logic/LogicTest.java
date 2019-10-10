package br.com.cronapi.logic;

import cronapi.Var;
import cronapi.logic.Operations;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LogicTest {

  @Test
  public void checkMethodIsNull() {
    assertFalse(Operations.isNull(Var.VAR_EMPTY).getObjectAsBoolean());
    assertTrue(Operations.isNull(Var.valueOf(null)).getObjectAsBoolean());
    assertTrue(Operations.isNull(Var.VAR_NULL).getObjectAsBoolean());
    assertFalse(Operations.isNull(Var.valueOf(new ArrayList<>())).getObjectAsBoolean());
    assertFalse(Operations.isNull(Var.valueOf(new HashMap<>())).getObjectAsBoolean());
  }

  @Test
  public void checkMethodIsNullOrEmpty() {

    assertTrue(Operations.isNullOrEmpty(Var.valueOf(new ArrayList<>())).getObjectAsBoolean());

    List<Object> list = new ArrayList<>();
    list.add(new Object());
    Var listVar = Var.valueOf(list);
    assertFalse(Operations.isNullOrEmpty(listVar).getObjectAsBoolean());

    assertTrue(Operations.isNullOrEmpty(Var.VAR_EMPTY).getObjectAsBoolean());
    assertTrue(Operations.isNullOrEmpty(Var.valueOf(null)).getObjectAsBoolean());
    assertFalse(Operations.isNullOrEmpty(Var.VAR_ZERO).getObjectAsBoolean());

    assertTrue(Operations.isNullOrEmpty(Var.valueOf(new HashMap<>())).getObjectAsBoolean());

    Map map = new HashMap();
    map.put("chave", new Object());
    Var mapVar = Var.valueOf(list);

    assertFalse(Operations.isNullOrEmpty(mapVar).getObjectAsBoolean());
  }

  @Test
  public void checkMethodIsEmpty() {
    assertTrue(Operations.isEmpty(Var.valueOf(new ArrayList<>())).getObjectAsBoolean());

    List<Object> list = new ArrayList<>();
    list.add(new Object());
    Var listVar = Var.valueOf(list);
    assertFalse(Operations.isEmpty(listVar).getObjectAsBoolean());
    assertTrue(Operations.isEmpty(Var.VAR_EMPTY).getObjectAsBoolean());
    assertFalse(Operations.isEmpty(null).getObjectAsBoolean());
    assertFalse(Operations.isEmpty(Var.VAR_ZERO).getObjectAsBoolean());

    assertTrue(Operations.isEmpty(Var.valueOf(" ")).getObjectAsBoolean());

    assertTrue(Operations.isEmpty(Var.valueOf(new HashMap<>())).getObjectAsBoolean());

    Map map = new HashMap();
    map.put("chave", new Object());
    Var mapVar = Var.valueOf(list);

    assertFalse(Operations.isEmpty(mapVar).getObjectAsBoolean());
  }

}
