package br.com.cronapi.json;

import com.google.gson.JsonObject;
import com.google.gson.internal.LinkedTreeMap;
import cronapi.Var;
import cronapi.database.DataSource;
import org.apache.commons.io.IOUtils;
import org.jdom2.Document;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

import static br.com.cronapi.mock.JsonMock.gerJsonToMap;
import static br.com.cronapi.mock.JsonMock.getJson;
import static cronapi.json.Operations.*;
import static org.junit.jupiter.api.Assertions.*;

 class JsonTest {

    private Var booksJson;
    private Var booksJsonConvert;

    @BeforeEach
     void setUp() throws Exception {
        try (InputStream booksInput = getClass().getResourceAsStream("/books.json")) {
            booksJson = toJson(Var.valueOf(IOUtils.toString(booksInput)));
        }
        try (InputStream booksInput = getClass().getResourceAsStream("/booksConvert.json")) {
            booksJsonConvert = toJson(Var.valueOf(IOUtils.toString(booksInput)));
        }
    }

    @AfterEach
     void tearDown() {
        booksJsonConvert = null;
        booksJson = null;
    }

    @Test
     void testCreateObjectJson() throws Exception {
        assertTrue(createObjectJson().getObject() instanceof JsonObject);
    }

    @Test
     void testDeleteObjectFromJson() throws Exception {
        deleteObjectFromJson(Var.valueOf(booksJson), Var.valueOf("store"));
        assertEquals(booksJson.getObjectAsJson().getAsJsonObject().get("expensive").toString(), "10");
    }

    @Test
     void testGetJsonOrMapField() throws Exception {
        Var retorno = getJsonOrMapField(Var.valueOf(booksJson),Var.valueOf("expensive"));
        assertEquals(retorno.getObjectAsString(), "10");
        retorno = getJsonOrMapField(Var.valueOf(booksJson),Var.valueOf("book.category"));
        assertNull(retorno.getObject());

        DataSource ds = Mockito.mock(DataSource.class);
        Mockito.when(ds.getObject()).thenReturn(Mockito.mock(DataSource.class));
        retorno = getJsonOrMapField(Var.valueOf(ds),Var.valueOf("book.category"));
        assertNull(retorno.getObject());

        String json = "{\n" +
                "   \"foo\" : \"foo\",\n" +
                "   \"bar\" : 10,\n" +
                "   \"baz\" : true\n" +
                "}";

        retorno = getJsonOrMapField(Var.valueOf(json), Var.valueOf("$"));
        assertEquals(((JsonObject)retorno.getObject()).get("foo").getAsString(), "foo");
    }

    @Test
     void testSetJsonOrMapField() throws Exception {
        Var retorno = getJsonOrMapField(Var.valueOf(booksJson),Var.valueOf("expensive"));
        assertEquals(retorno.getObjectAsString(), "10");
        setJsonOrMapField(Var.valueOf(booksJson),Var.valueOf("expensive"), Var.valueOf("11"));
        retorno = getJsonOrMapField(Var.valueOf(booksJson),Var.valueOf("expensive"));
        assertEquals(retorno.getObjectAsString(), "11");
        setJsonOrMapField(Var.valueOf(booksJson),Var.valueOf("store.bicycle.color"), Var.valueOf("black"));
        retorno = getJsonOrMapField(Var.valueOf(booksJson),Var.valueOf("store.bicycle.color"));
        assertEquals(retorno.getObjectAsString(), "black");
        retorno = getJsonOrMapField(Var.valueOf(getJson()), Var.valueOf("$"));
        assertEquals(((JsonObject)retorno.getObject()).get("foo").getAsString(), "foo");
        setJsonOrMapField(Var.valueOf(((JsonObject)retorno.getObject())),Var.valueOf("bar"), Var.valueOf("1"));
        retorno = getJsonOrMapField(retorno,Var.valueOf("bar"));
        assertEquals(retorno.getObjectAsString(), "1");
    }

    @Test()
     void testSetJsonOrMapFieldErro() throws Exception {
        DataSource ds = Mockito.mock(DataSource.class);
        Mockito.when(ds.getObject()).thenReturn(Mockito.mock(DataSource.class));
        assertThrows(ClassCastException.class, () -> { setJsonOrMapField(Var.valueOf(ds), Var.valueOf("store.bicycle.color"), Var.valueOf("2")); });
    }

    @Test
     void testToJson() throws Exception {
        InputStream booksInput = getClass().getResourceAsStream("/books.json");
        Var booksJsonNovo = toJson(Var.valueOf(IOUtils.toString(booksInput)));
        assertTrue(booksJsonNovo.getObject() instanceof JsonObject);
    }

    @Test
     void testToList() throws Exception {
        Var retorno = toList(Var.valueOf(getJson()));
        retorno = getJsonOrMapField(retorno, Var.valueOf("$"));
        assertEquals(((JsonObject)retorno.getObject()).get("foo").getAsString(), "foo");
    }

    @Test
     void testToMap() throws Exception {

        Var retorno = toMap(Var.valueOf(getJson()));
        retorno = getJsonOrMapField(retorno, Var.valueOf("$"));
        assertEquals(((JsonObject)retorno.getObject()).get("foo").getAsString(), "foo");
        retorno = toMap(Var.valueOf(gerJsonToMap()));
        assertTrue(retorno.getObject() instanceof List);
        InputStream fileInputStream = new FileInputStream(getClass().getResource("/books.json").getPath());
        Var booksJsonNovo = toMap(Var.valueOf(fileInputStream));
        assertTrue(booksJsonNovo.getObject() instanceof LinkedTreeMap);
    }

    @Test
     void testToXml() throws Exception {
        assertTrue(toXml(booksJsonConvert).getObject() instanceof Document);
    }

}