package br.com.cronapi.map;


import com.google.gson.JsonObject;
import cronapi.Var;
import cronapi.map.Operations;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.util.LinkedHashMap;

public class MapTest {

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testCreateObjectMapWith() {
    }

    @Test
    public void testCreateObjectMap() throws Exception {
        Var retorno = Operations.createObjectMap();
        Assert.assertTrue(retorno.getObject() instanceof LinkedHashMap);
    }

    @Test
    public void testGetJsonOrMapField() throws Exception {
        String json = "{\n" +
                "   \"foo\" : \"foo\",\n" +
                "   \"bar\" : 10,\n" +
                "   \"baz\" : true\n" +
                "}";
        Var retorno = Operations.toList(Var.valueOf(json));
        retorno = Operations.getJsonOrMapField(retorno, Var.valueOf("$"));
        Assert.assertEquals(((JsonObject)retorno.getObject()).get("foo").getAsString(), "foo");
    }

   // @Test
    public void testGetMapField() throws Exception {
        String json = "{\n" +
                "   \"foo\" : \"foo\",\n" +
                "   \"bar\" : 10,\n" +
                "   \"baz\" : true\n" +
                "}";
        Var retorno = Operations.toList(Var.valueOf(json));
        retorno = Operations.getMapField(retorno, Var.valueOf("$"));
        Assert.assertEquals(((JsonObject)retorno.getObject()).get("foo").getAsString(), "foo");
    }

    @Test
    public void testSetMapField() {
    }

    @Test
    public void testSetMapFieldByKey() {
    }

  //  @Test
    public void testToJson() throws Exception {
        InputStream booksInput = getClass().getResourceAsStream("/books.json");
        Var booksJsonNovo = Operations.toJson(Var.valueOf(null));
        Assert.assertTrue(booksJsonNovo.getObject() instanceof JsonObject);
    }

    @Test
    public void testToList() {
    }

    @Test
    public void testToMap() throws Exception {
        String json = "{\n" +
                "   \"foo\" : \"foo\",\n" +
                "   \"bar\" : 10,\n" +
                "   \"baz\" : true\n" +
                "}";
        Var retorno = Operations.toMap(Var.valueOf(json));
        retorno = Operations.getJsonOrMapField(retorno, Var.valueOf("$"));
        Assert.assertEquals(((JsonObject)retorno.getObject()).get("foo").getAsString(), "foo");
    }

    public static class FooBarBaz {
        public String foo;
        public Long bar;
        public boolean baz;
    }
}