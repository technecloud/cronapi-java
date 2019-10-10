package br.com.cronapi.list;


import com.jayway.jsonpath.JsonPath;
import cronapi.Var;
import cronapi.list.Operations;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static cronapi.json.Operations.GSON_CONFIGURATION;
import static cronapi.json.Operations.toJson;
import static org.junit.jupiter.api.Assertions.*;

public class ListTest {

  private static Var BOOKS_JSON;

  @BeforeEach
  public void oneTimeSetUp() throws Exception {
    //
    try (InputStream booksInput = ListTest.class.getResourceAsStream("/books.json")) {
      String json = JsonPath.using(GSON_CONFIGURATION)
          .parse(IOUtils.toString(booksInput))
          .read("$.store.book")
          .toString();
      BOOKS_JSON = toJson(Var.valueOf(json));
    }
  }

  @Test
  public void newListShouldReturnEmptyList() {
    Var newListVar = Operations.newList();
    assertTrue(newListVar.getObject() instanceof List);
    List newList = (List) newListVar.getObject();
    assertEquals(newList.size(), 0);
  }

  @Test
  public void sizeFromListTest() {
    List<Object> list = new ArrayList<>();
    list.add(new Object());
    Var listVar = Var.valueOf(list);
    assertEquals(Operations.size(listVar).getObjectAsInt().intValue(), 1);
  }

  @Test
  public void sizeFromJsonTest() {
    Var listVar = Var.valueOf(BOOKS_JSON);
    assertEquals(Operations.size(listVar).getObjectAsInt().intValue(), 4);
  }

  @Test
  public void listFromTextTest() {
    Var listVar = Operations.getListFromText(Var.valueOf("Cronapp/Platform"), Var.valueOf("/"));
    assertTrue(listVar.getObject() instanceof List);
    assertEquals(Operations.size(listVar).getObjectAsInt().intValue(), 2);
  }

  @Test
  public void listFromTextUsingListAsParameterTest() {
    Var listParam = Var.valueOf(Arrays.asList("Cronapp", "/", "Platform"));
    Var listVar = Operations.getListFromText(listParam, Var.valueOf("/"));
    assertTrue(listVar.getObject() instanceof List);
    assertEquals(Operations.size(listVar).getObjectAsInt().intValue(), 2);
  }

  @Test
  public void listFromTextNullAsParameterTest() {
    Var listVar = Operations.getListFromText(Var.VAR_NULL, Var.VAR_NULL);
    assertTrue(listVar.getObject() instanceof List);
    assertEquals(Operations.size(listVar).getObjectAsInt().intValue(), 1);
  }

  @Test
  public void testSize() throws Exception {
    assertEquals(Operations.size(Var.valueOf(null)).getObjectAsLong(), Var.valueOf(0L).getObjectAsLong());
    assertEquals(Operations.size(Var.valueOf("teste")).getObjectAsLong(), Var.valueOf(1L).getObjectAsLong());
    // isEmpty
    assertFalse(Operations.isEmpty(Var.valueOf("teste")).getObjectAsBoolean());
    assertTrue(Operations.isEmpty(Var.valueOf(null)).getObjectAsBoolean());
  }

  @Test
  public void testNewList() throws Exception {
    Var list = Operations.newList(Var.valueOf("1"), Var.valueOf("2"), Var.valueOf("3"));
    assertEquals(list.getObjectAsList().size(), 3);
  }

  @Test
  public void testNewListRepeat() throws Exception {
    Var list = Operations.newListRepeat(Var.valueOf("1"), Var.valueOf("2"));
    assertEquals(list.getObjectAsList().size(), 2);
  }


}
