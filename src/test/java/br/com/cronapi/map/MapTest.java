package br.com.cronapi.map;


import com.google.gson.JsonObject;
import cronapi.Var;
import cronapi.map.Operations;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MapTest {

    @Test
    void testCreateObjectMap() throws Exception {
        Var retorno = Operations.createObjectMap();
        assertTrue(retorno.getObject() instanceof LinkedHashMap);
    }

    @Test
    void testGetJsonOrMapField() throws Exception {
        String json = "{\n" +
                "   \"foo\" : \"foo\",\n" +
                "   \"bar\" : 10,\n" +
                "   \"baz\" : true\n" +
                "}";
        Var retorno = Operations.toList(Var.valueOf(json));
        retorno = Operations.getJsonOrMapField(retorno, Var.valueOf("$"));
        assertEquals("foo", ((JsonObject)retorno.getObject()).get("foo").getAsString());
    }

    @Test
    void testSetMapFieldByKey() throws Exception {
        Operations.setMapFieldByKey(new Var("id", "object"), new Var("id", "object"), new Var("id", "object"));
    }

    @Test
    void testToMap() throws Exception {
        String json = "{\n" +
                "   \"foo\" : \"foo\",\n" +
                "   \"bar\" : 10,\n" +
                "   \"baz\" : true\n" +
                "}";
        Var retorno = Operations.toMap(Var.valueOf(json));
        retorno = Operations.getJsonOrMapField(retorno, Var.valueOf("$"));
        assertEquals("foo", ((JsonObject)retorno.getObject()).get("foo").getAsString());
        Var result = Operations.toMap(Var.valueOf(getJsonToMap()));
        assertEquals(1, result.getObjectAsList().size());
    }

    @Test
    void testToJson() throws Exception {
        Var result = Operations.toJson(new Var("id", "object"));
        assertEquals(new Var("id", "object"), result);
    }

    @Test
    void testCreateObjectMapWith() throws Exception {
        Var result = Operations.createObjectMapWith(new Var("id", "object"));
        assertTrue(result.getObject() instanceof LinkedHashMap);
        assertEquals("object", Operations.getMapField(result, Var.valueOf("id")).getObjectAsString());
        Operations.setMapField(result, Var.valueOf("id1"), Var.valueOf("object1"));
        assertEquals("object1", Operations.getMapField(result, Var.valueOf("id1")).getObjectAsString());
        Operations.setMapFieldByKey(result, Var.valueOf("id2"), Var.valueOf("object2"));
        assertEquals("object2", Operations.getMapField(result, Var.valueOf("id2")).getObjectAsString());
        assertEquals("", Operations.getMapField(Var.valueOf(getJsonToMap()), Var.valueOf("foo")).getObjectAsString());
    }

    @Test
    void testToList() throws Exception {
        Var result = Operations.toList(new Var(getJson()));
        assertEquals("{\"foo\":\"foo\",\"bar\":10.0,\"baz\":true}", result.getObjectAsString());
    }

    public static class FooBarBaz {
        public String foo;
        public Long bar;
        public boolean baz;
    }

    static String getJson() {
        return "{\n" +
                "   \"foo\" : \"foo\",\n" +
                "   \"bar\" : 10,\n" +
                "   \"baz\" : true\n" +
                "}";
    }

    static String getJsonToMap(){
        return "[".concat(getJson()).concat("]");
    }
}