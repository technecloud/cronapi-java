package br.com.cronapi;

import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import cronapi.Utils;
import cronapi.Var;
import cronapi.database.DataSource;
import cronapi.i18n.Messages;
import cronapi.map.Operations;
import org.jdom2.Document;
import org.jdom2.Element;
import org.joda.time.DateTime;
import org.junit.AfterClass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

import static br.com.cronapi.mock.JsonMock.*;
import static cronapi.Var.valueOf;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class VarTest {

    private static TimeZone last;

    @Spy
    private Var varSpy = new Var();

    @Mock
    private DataSource dataSource;


    @BeforeEach
    public void before() {
        MockitoAnnotations.initMocks(this);

        last = TimeZone.getDefault();
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        Messages.set(Locale.ENGLISH);
        dataSource = Mockito.mock(DataSource.class);

    }

    @AfterClass
    public static void after() {
        TimeZone.setDefault(last);
    }

    @Test
    public void nullVarShouldReturnEmptyString() {
        assertEquals(Var.VAR_NULL.getObjectAsString(), "");
        assertEquals(valueOf(null).getObjectAsString(), "");
    }

    @Test
    public void listConversion() {
        List list = valueOf("1,2").getObjectAsList();

        assertEquals(list.get(0), "1");
        assertEquals(list.get(1), "2");

        assertEquals(list.get(0).getClass(), String.class);
        assertEquals(list.get(1).getClass(), String.class);

        list = valueOf(1).getObjectAsList();
        assertEquals(list.get(0), 1L);
        assertEquals(list.get(0).getClass(), Long.class);
    }

    @Test
    public void generalConversion() {
        assertEquals((int) valueOf("1").getObjectAsInt(), 1);
        assertEquals((int) valueOf(1.0).getObjectAsInt(), 1);
        assertEquals((int) valueOf(1.1).getObjectAsInt(), 1);
        assertEquals((double) valueOf("1.0").getObjectAsDouble(), 1.0, 0.001);
        assertEquals((double) valueOf(1).getObjectAsDouble(), 1.0, 0.001);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        calendar.set(Calendar.MILLISECOND, 0);

        assertEquals(valueOf(valueOf(calendar.getTime()).toString()).getObjectAsDateTime().getTimeInMillis(), calendar.getTimeInMillis());

        ISO8601DateFormat format = new ISO8601DateFormat();
        String iso = format.format(calendar.getTime());

        assertEquals(valueOf(iso).getObjectAsDateTime().getTimeInMillis(), calendar.getTimeInMillis());

    }

    @Test
    public void dateConversion() {
        assertTrue(Var.deserialize("2019-04-10T00:00:00.000Z") instanceof Date);
        assertTrue(cronapi.conversion.Operations.convertLongToDate(valueOf("1554914251430")).getObjectAsDateTime() instanceof GregorianCalendar);
    }

    @Test
    public void stringToIntConversion() {
        assertTrue(valueOf("").getObjectAsInt() == 0);
        assertTrue(valueOf("").getObjectAsDouble() == 0.0);
        assertTrue(valueOf("").getObjectAsLong() == 0L);
    }

    private Var getTestDate() {
        return valueOf("2012-05-12T04:05:24Z");
    }

    private class AnyObject {
        private String test;

        public String getTest() {
            return test;
        }

        public void setTest(String test) {
            this.test = test;
        }
    }

    @Test
    public void getObjectAsStringConversion() {
        assertEquals(new Var("12").getObjectAsString(), "12");
        assertEquals(valueOf("12").getObjectAsString(), "12");
        assertEquals(valueOf("value").getObjectAsString(), "value");
        assertEquals(valueOf(12).getObjectAsString(), "12");
        assertEquals(valueOf(12.14556896).getObjectAsString(), "12.14556896");
        assertEquals(valueOf(12.1).getObjectAsString(), "12.1");
        assertEquals(valueOf(0.1).getObjectAsString(), "0.1");
        assertEquals(valueOf(true).getObjectAsString(), "true");
        assertEquals(valueOf(false).getObjectAsString(), "false");

        Date current = new Date();

        assertEquals(valueOf(current).getObjectAsString(), Utils.getISODateFormat().format(current));
        assertEquals(valueOf(getTestDate()).getObjectAsString(), "2012-05-12T04:05:24Z");
        assertEquals(valueOf(new File("/tmp/test.txt")).getObjectAsString(), "/tmp/test.txt");

        ByteArrayInputStream stream = new ByteArrayInputStream("test".getBytes());
        assertEquals(valueOf(stream).getObjectAsString(), "test");

        Document doc = new Document();
        Element newElement = new Element("test");
        newElement.setText("any");
        doc.addContent(newElement);

        assertEquals(valueOf(doc).getObjectAsString(), "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n<test>any</test>\r\n");

        assertEquals(valueOf(newElement).getObjectAsString(), "<test>any</test>");

        JsonObject json = new JsonObject();
        json.addProperty("test", "value");
        assertEquals(valueOf(json).getObjectAsString(), "{\"test\":\"value\"}");

        JsonElement jsonElement = new JsonPrimitive("test");
        assertEquals(valueOf(jsonElement).getObjectAsString(), "test");

        Var image = new Var("MinhaImagem".getBytes());
        assertEquals(image.getObjectAsString(), "TWluaGFJbWFnZW0=");

        AnyObject obj = new AnyObject();
        obj.setTest("value");

        assertEquals(valueOf(obj).getObjectAsString(), "{\"test\":\"value\"}");
    }

    @Test()
    public void getObjectAsString() throws IOException {
        InputStream booksInput = getClass().getResourceAsStream("/books.xml");
        assertTrue(valueOf(booksInput).getObjectAsString() instanceof String);
        // doReturn(null).when(org.apache.commons.io.IOUtils.toString(any(InputStream.class)));
        // when(org.apache.commons.io.IOUtils.toString(booksInput)).thenCallRealMethod();

       // assertTrue(Var.valueOf(booksInput).getObjectAsString() instanceof String);
    }

    @Test
    public void getObject() {
        assertNull(valueOf(null).getObject(Var.class));
        assertTrue(valueOf("new Var()").getObject(Var.class) instanceof Var);
        assertTrue(valueOf("new Var()").getObject(String.class) instanceof String);
        assertTrue(valueOf(new StringBuilder().append("teste")).getObject(StringBuilder.class) instanceof String);
        assertTrue(valueOf(new StringBuffer().append("teste")).getObject(StringBuffer.class) instanceof String);
        assertTrue(valueOf(Character.toUpperCase('a')).getObject(Character.class) instanceof String);
    }

    @Test
    public void getObjectBoolean() {
        assertTrue(valueOf(true).getObject(Boolean.class) instanceof Boolean);
        assertTrue(valueOf(0).getObject(Boolean.class) instanceof Boolean);
        assertTrue(valueOf("1").getObject(Boolean.class) instanceof Boolean);
        assertTrue(valueOf("true").getObject(Boolean.class) instanceof Boolean);
        assertTrue(valueOf("yes").getObject(Boolean.class) instanceof Boolean);
        assertTrue(valueOf("sim").getObject(Boolean.class) instanceof Boolean);
        assertTrue(valueOf("y").getObject(Boolean.class) instanceof Boolean);
        assertTrue(valueOf("s").getObject(Boolean.class) instanceof Boolean);
        assertTrue(valueOf("t").getObject(Boolean.class) instanceof Boolean);
        assertTrue(valueOf(new Double(0)).getObject(Boolean.class) instanceof Boolean);
        assertTrue(valueOf(new Date()).getObject(Boolean.class) instanceof Boolean);
        assertTrue(valueOf(new ArrayList<>()).getObject(Boolean.class) instanceof Boolean);
    }

    @Test
    public void getObjectAsDouble() {
        assertTrue(valueOf(true).getObject(Double.class) instanceof Double);
        assertTrue(valueOf(true).getObject(double.class) instanceof Double);
        assertTrue(valueOf(new Double(0)).getObject(Double.class) instanceof Double);
        assertTrue(valueOf(new Date()).getObject(Double.class) instanceof Double);
        assertTrue(valueOf(new ArrayList<>()).getObject(Double.class) instanceof Double);
        Var image = new Var("MinhaImagem".getBytes());
        assertTrue(valueOf(image).getObject(Double.class) instanceof Double);
        assertTrue(valueOf((float) 0).getObject(Float.class) instanceof Float);
    }

    @Test
    public void getObjectAsBigFloat() {
        assertTrue(valueOf(new BigDecimal(0)).getObject(BigDecimal.class) instanceof BigDecimal);
        assertTrue(valueOf(new BigInteger("0")).getObject(BigInteger.class) instanceof BigInteger);
    }

    @Test
    public void getObjectAsLong(){
        assertTrue(valueOf("0.5").getObject(Long.class) instanceof Long);
        assertTrue(valueOf(Calendar.getInstance()).getObject(Calendar.class) instanceof Calendar);
        assertTrue(valueOf(Integer.parseInt("0")).getObject(Integer.class) instanceof Integer);
        Var image = new Var("MinhaImagem".getBytes());
        assertTrue(valueOf(image).getObject(byte[].class) instanceof byte[]);

        assertTrue(valueOf(false).getObjectAsLong() instanceof Long);
        assertTrue(valueOf(Calendar.getInstance()).getObjectAsLong() instanceof Long);
        List<String> lista = new ArrayList();
        assertTrue(valueOf(lista).getObjectAsLong() instanceof Long);
        assertTrue(valueOf(lista).iterator() instanceof Iterator);
        lista.add("teste");
        assertEquals(valueOf(lista).size(), 1);
        assertTrue(valueOf((float) 0).getObjectAsLong() instanceof Long);
    }

    @Test
    public void getObjectAsInt(){
        assertTrue(valueOf(false).getObjectAsInt() instanceof Integer);
        assertTrue(valueOf(Calendar.getInstance()).getObjectAsInt() instanceof Integer);
        assertTrue(valueOf(new ArrayList<>()).getObjectAsInt() instanceof Integer);
        assertTrue(valueOf((float) 0).getObjectAsInt() instanceof Integer);
        assertTrue(valueOf("0.9").getObjectAsInt() instanceof Integer);
    }

    @Test
    public void getObjectAsDateTime(){
        assertTrue(valueOf(Integer.parseInt("5")).getObjectAsDateTime() instanceof Calendar);
        assertTrue(valueOf(new Double(0)).getObjectAsDateTime() instanceof Calendar);
        assertTrue(valueOf(DateTime.now()).getObjectAsDateTime() instanceof Calendar);
        assertTrue(valueOf(new ArrayList<>()).getObjectAsDateTime() instanceof Calendar);
        assertTrue(valueOf(false).getObjectAsDateTime() instanceof Calendar);
    }

    @Test
    public void cloneObject(){
        Var obj = valueOf(Calendar.getInstance());
        Object ob1 = obj.cloneObject();
        assertEquals(obj, ob1);
    }

    @Test
    public void testToString() throws Exception {
        assertTrue(valueOf(Integer.parseInt("0")).toString() instanceof String);
        assertTrue(valueOf(new Double(0)).toString() instanceof String);
        assertTrue(valueOf(DateTime.now()).toString() instanceof String);
        List lista = new LinkedList<>();
        assertEquals(valueOf(lista).toString() ,"[]");
        lista.add("teste");
        assertEquals(valueOf(lista).toString() ,"[teste]");
        assertTrue(valueOf(null).toString() instanceof String);
        assertTrue(valueOf(false).toString() instanceof String);
        assertTrue(valueOf("teste").toString() instanceof String);
        assertTrue(Operations.toMap(valueOf(getJson())).toString() instanceof String);
        assertTrue(Operations.toMap(valueOf(getJson())).getObjectAsList() instanceof List);
        assertTrue(Operations.toMap(valueOf(gerJsonToMap())).getObjectAsList() instanceof List);
        Mockito.when(dataSource.getPage()).thenReturn(Mockito.mock(Page.class));
        Mockito.when(dataSource.getPage().getContent()).thenReturn(Mockito.mock(List.class));
        Mockito.when(dataSource.getPage().getContent()).thenReturn(lista);
        assertTrue(valueOf(dataSource).getObjectAsList() instanceof List);
        assertTrue(valueOf(dataSource).getObjectAsDataSource() instanceof DataSource);
        JsonArray jsonArray = new JsonArray();
        jsonArray.add("teste");
        assertTrue(valueOf(jsonArray).getObjectAsList() instanceof List);
        assertTrue(valueOf(gerJsonToMap()).getObjectAsList() instanceof List);
        assertEquals(varSpy.valueOf(getJsonToMapEncode()).getObjectAsByteArray().toString().substring(0,1), "[");
        try {
            Var.valueOf(getJsonTempFile()).getObjectAsByteArray();
            fail();
        } catch (Exception e){
            assertEquals(e.getMessage(), "java.lang.RuntimeException: java.nio.file.NoSuchFileException: /tmp/CRONAPI_RECYCLE_FILES/books.json");
        }

    }

    @Test()
    public void getObjectAsByteArray(){
        ByteArrayInputStream stream = new ByteArrayInputStream("test".getBytes());
        assertEquals(valueOf(stream).getObjectAsString(), "test");
        assertTrue(valueOf(stream).getObjectAsByteArray() instanceof byte[]);
        try {
            assertTrue(valueOf(new File("/template.png")).getObjectAsByteArray() instanceof byte[]);
            fail();
        } catch (Exception e){
            assertTrue(e.getMessage().equals("java.io.FileNotFoundException: File '/template.png' does not exist"));
        }
    }

    @Test()
    public void getObjectAsByteArrayException() {
        try {
            assertTrue(valueOf(new File("/books.xml")).getObjectAsByteArray() instanceof byte[]);
            fail();
        } catch (Exception e){
            assertTrue(e.getMessage().equals("java.lang.RuntimeException: java.io.FileNotFoundException: File '/books.xml' does not exist"));
        }
    }

    @Test
    void negate(){
        assertTrue(varSpy.negate().getObjectAsBoolean());
        varSpy = valueOf(false);
        assertTrue(varSpy.negate().getObjectAsBoolean());
        varSpy = valueOf(true);
        assertEquals(varSpy.negate().getObjectAsBoolean(), false);
    }

    @Test
    void BooleanTest(){
        varSpy = valueOf("0");
        assertFalse(varSpy.getObjectAsBoolean());
    }

    @Test
    void evalTest(){
        assertNull(Var.eval("").getObject());
        assertNull(Var.eval(null).getObject());
        assertEquals(Var.eval("4*5").getObjectAsInt(), 20);
        assertNull(Var.eval("4*5-").getObject());
    }

    @Test
    void valueOfTest(){
        assertEquals(valueOf("1", Long.valueOf("0")).getObjectAsInt(), 0);
        assertEquals(valueOf("1", valueOf("1", 0)).getObjectAsInt(), 0);
    }

    @Test
    void newMapTest(){
        assertTrue(Var.newMap().getObject() instanceof LinkedHashMap);
        assertTrue(Var.newList().getObject() instanceof LinkedList);
        Var[] lista = new Var[0];
        assertTrue(Var.asObjectArray(lista) instanceof Object[]);
        lista = new Var[1];
        lista[0] = valueOf("0");
        assertTrue(Var.asObjectArray(lista) instanceof Object[]);
        varSpy = valueOf("0", "teste");
        varSpy.setId("1");
        assertEquals(varSpy.getId(), "1");
        assertNull(Var.deserialize(null));
    }

}
