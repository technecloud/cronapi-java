package br.com.cronapi.json;

import com.google.gson.JsonObject;
import com.google.gson.internal.LinkedTreeMap;
import cronapi.Var;
import cronapi.database.DataSource;
import cronapi.json.Operations;
import org.apache.commons.io.IOUtils;
import org.jdom2.Document;
import org.jdom2.input.JDOMParseException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

import static cronapi.json.Operations.toJson;
import static cronapi.json.Operations.toXml;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

public class JsonTest {

    private Var booksJson;
    private Var booksJsonConvert;

    @BeforeEach
    public void setUp() throws Exception {
        try (InputStream booksInput = getClass().getResourceAsStream("/books.json")) {
            booksJson = toJson(Var.valueOf(IOUtils.toString(booksInput)));
        }
        try (InputStream booksInput = getClass().getResourceAsStream("/booksConvert.json")) {
            booksJsonConvert = toJson(Var.valueOf(IOUtils.toString(booksInput)));
        }
    }

    @AfterEach
    public void tearDown() {
        booksJsonConvert = null;
        booksJson = null;
    }

    @Test
    public void testCreateObjectJson() throws Exception {
        assertTrue(Operations.createObjectJson().getObject() instanceof JsonObject);
    }

    @Test
    public void testDeleteObjectFromJson() throws Exception {
        Operations.deleteObjectFromJson(Var.valueOf(booksJson), Var.valueOf("store"));
        assertEquals(booksJson.getObjectAsJson().getAsJsonObject().get("expensive").toString(), "10");
    }

    @Test
    public void testGetJsonOrMapField() throws Exception {
        Var retorno = Operations.getJsonOrMapField(Var.valueOf(booksJson),Var.valueOf("expensive"));
        assertEquals(retorno.getObjectAsString(), "10");
        retorno = Operations.getJsonOrMapField(Var.valueOf(booksJson),Var.valueOf("book.category"));
        assertNull(retorno.getObject());

        DataSource ds = Mockito.mock(DataSource.class);
        Mockito.when(ds.getObject()).thenReturn(Mockito.mock(DataSource.class));
        retorno = Operations.getJsonOrMapField(Var.valueOf(ds),Var.valueOf("book.category"));
        assertNull(retorno.getObject());

        String json = "{\n" +
                "   \"foo\" : \"foo\",\n" +
                "   \"bar\" : 10,\n" +
                "   \"baz\" : true\n" +
                "}";

        retorno = Operations.getJsonOrMapField(Var.valueOf(json), Var.valueOf("$"));
        assertEquals(((JsonObject)retorno.getObject()).get("foo").getAsString(), "foo");
    }

    @Test
    public void testSetJsonOrMapField() throws Exception {
        Var retorno = Operations.getJsonOrMapField(Var.valueOf(booksJson),Var.valueOf("expensive"));
        assertEquals(retorno.getObjectAsString(), "10");
        Operations.setJsonOrMapField(Var.valueOf(booksJson),Var.valueOf("expensive"), Var.valueOf("11"));
        retorno = Operations.getJsonOrMapField(Var.valueOf(booksJson),Var.valueOf("expensive"));
        assertEquals(retorno.getObjectAsString(), "11");
        Operations.setJsonOrMapField(Var.valueOf(booksJson),Var.valueOf("store.bicycle.color"), Var.valueOf("black"));
        retorno = Operations.getJsonOrMapField(Var.valueOf(booksJson),Var.valueOf("store.bicycle.color"));
        assertEquals(retorno.getObjectAsString(), "black");
        String json = "{\n" +
                "   \"foo\" : \"foo\",\n" +
                "   \"bar\" : 10,\n" +
                "   \"baz\" : true\n" +
                "}";

        retorno = Operations.getJsonOrMapField(Var.valueOf(json), Var.valueOf("$"));
        assertEquals(((JsonObject)retorno.getObject()).get("foo").getAsString(), "foo");
        Operations.setJsonOrMapField(Var.valueOf(((JsonObject)retorno.getObject())),Var.valueOf("bar"), Var.valueOf("1"));
        retorno = Operations.getJsonOrMapField(retorno,Var.valueOf("bar"));
        assertEquals(retorno.getObjectAsString(), "1");
    }

    @Test()
    public void testSetJsonOrMapFieldErro() throws Exception {
        DataSource ds = Mockito.mock(DataSource.class);
        Mockito.when(ds.getObject()).thenReturn(Mockito.mock(DataSource.class));
        assertThrows(ClassCastException.class, () -> { Operations.setJsonOrMapField(Var.valueOf(ds), Var.valueOf("store.bicycle.color"), Var.valueOf("2")); });
    }

    @Test
    public void testToJson() throws Exception {
        InputStream booksInput = getClass().getResourceAsStream("/books.json");
        Var booksJsonNovo = toJson(Var.valueOf(IOUtils.toString(booksInput)));
        assertTrue(booksJsonNovo.getObject() instanceof JsonObject);
    }

    @Test
    public void testToList() throws Exception {
        String json = "{\n" +
                "   \"foo\" : \"foo\",\n" +
                "   \"bar\" : 10,\n" +
                "   \"baz\" : true\n" +
                "}";
        Var retorno = Operations.toList(Var.valueOf(json));
        retorno = Operations.getJsonOrMapField(retorno, Var.valueOf("$"));
        assertEquals(((JsonObject)retorno.getObject()).get("foo").getAsString(), "foo");
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
        assertEquals(((JsonObject)retorno.getObject()).get("foo").getAsString(), "foo");
        json = "[".concat(json).concat("]");
        retorno = Operations.toMap(Var.valueOf(json));
        assertTrue(retorno.getObject() instanceof List);
        InputStream fileInputStream = new FileInputStream(getClass().getResource("/books.json").getPath());
        Var booksJsonNovo = Operations.toMap(Var.valueOf(fileInputStream));
        assertTrue(booksJsonNovo.getObject() instanceof LinkedTreeMap);
    }

//    @Test(expected = JDOMParseException.class)
//    public void testToXmlException() throws Exception {
//        assertTrue(toXml(booksJson).getObject() instanceof Document);
//    }

    @Test
    public void testToXml() throws Exception {
        assertTrue(toXml(booksJsonConvert).getObject() instanceof Document);
    }

    public static class FooBarBaz {
        public String foo;
        public Long bar;
        public boolean baz;
    }
}