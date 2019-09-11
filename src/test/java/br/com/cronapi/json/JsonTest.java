package br.com.cronapi.json;

import com.google.gson.JsonObject;
import com.jayway.jsonpath.JsonPath;
import cronapi.Var;
import cronapi.database.DataSource;
import cronapi.json.Operations;
import org.apache.commons.io.IOUtils;
import org.jdom2.Document;
import org.jdom2.input.JDOMParseException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.InputStream;

import static cronapi.json.Operations.toJson;
import static cronapi.json.Operations.toXml;

public class JsonTest {

    private Var booksJson;
    private Var booksJsonConvert;

    @Before
    public void setUp() throws Exception {
        try (InputStream booksInput = getClass().getResourceAsStream("/books.json")) {
            booksJson = toJson(Var.valueOf(IOUtils.toString(booksInput)));
        }
        try (InputStream booksInput = getClass().getResourceAsStream("/booksConvert.json")) {
            booksJsonConvert = toJson(Var.valueOf(IOUtils.toString(booksInput)));
        }
    }

    @After
    public void tearDown() {
        booksJsonConvert = null;
        booksJson = null;
    }

    @Test
    public void testCreateObjectJson() throws Exception {
        Assert.assertTrue(Operations.createObjectJson().getObject() instanceof JsonObject);
    }

    @Test
    public void testDeleteObjectFromJson() throws Exception {
        Operations.deleteObjectFromJson(Var.valueOf(booksJson), Var.valueOf("store"));
        Assert.assertEquals(booksJson.getObjectAsJson().getAsJsonObject().get("expensive").toString(), "10");
    }

    @Test
    public void testGetJsonOrMapField() throws Exception {
        Var retorno = Operations.getJsonOrMapField(Var.valueOf(booksJson),Var.valueOf("expensive"));
        Assert.assertEquals(retorno.getObjectAsString(), "10");
        retorno = Operations.getJsonOrMapField(Var.valueOf(booksJson),Var.valueOf("book.category"));
        Assert.assertNull(retorno.getObject());

        DataSource ds = Mockito.mock(DataSource.class);
        Mockito.when(ds.getObject()).thenReturn(Mockito.mock(DataSource.class));
        retorno = Operations.getJsonOrMapField(Var.valueOf(ds),Var.valueOf("book.category"));
        Assert.assertNull(retorno.getObject());

        String json = "{\n" +
                "   \"foo\" : \"foo\",\n" +
                "   \"bar\" : 10,\n" +
                "   \"baz\" : true\n" +
                "}";

        retorno = Operations.getJsonOrMapField(Var.valueOf(json), Var.valueOf("$"));
        Assert.assertEquals(((JsonObject)retorno.getObject()).get("foo").getAsString(), "foo");
    }

    @Test
    public void testSetJsonOrMapField() throws Exception {
        Var retorno = Operations.getJsonOrMapField(Var.valueOf(booksJson),Var.valueOf("expensive"));
        Assert.assertEquals(retorno.getObjectAsString(), "10");
        Operations.setJsonOrMapField(Var.valueOf(booksJson),Var.valueOf("expensive"), Var.valueOf("11"));
        retorno = Operations.getJsonOrMapField(Var.valueOf(booksJson),Var.valueOf("expensive"));
        Assert.assertEquals(retorno.getObjectAsString(), "11");
        Operations.setJsonOrMapField(Var.valueOf(booksJson),Var.valueOf("store.book.category[0]"), Var.valueOf("reference 1"));
        //Assert.assertNull(retorno.getObject());
    }

    @Test
    public void testToJson() {
    }

    @Test
    public void testToList() {
    }

    @Test
    public void testToMap() {
    }

    @Test(expected = JDOMParseException.class)
    public void testToXmlException() throws Exception {
        Assert.assertTrue(toXml(booksJson).getObject() instanceof Document);
    }
    @Test
    public void testToXml() throws Exception {
        Assert.assertTrue(toXml(booksJsonConvert).getObject() instanceof Document);
    }

    public static class FooBarBaz {
        public String foo;
        public Long bar;
        public boolean baz;
    }
}