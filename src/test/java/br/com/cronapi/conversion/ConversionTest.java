package br.com.cronapi.conversion;

import cronapi.Var;
import cronapi.conversion.Operations;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ConversionTest {

    private static final String VALUE_ASSERT = "Y3JvbmFwcA==";
    private static final String CRONAPP = "cronapp";
    private static TimeZone last;

    @BeforeClass
    public static void before() {
        last = TimeZone.getDefault();
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        Messages.set(new Locale("pt", "BR"));
    }

    @AfterClass
    public static void after() {
        TimeZone.setDefault(last);
    }

    private Calendar getTestDate() {
        return Var.valueOf("2012-05-12T04:05:24Z").getObjectAsDateTime();
    }

    @Test
    public void testConversion() {
        Assert.assertEquals(Operations.convert(Var.valueOf(1), Var.valueOf("STRING")).getObject(), "1");
        Assert.assertEquals(Operations.convert(Var.valueOf(true), Var.valueOf("STRING")).getObject(), "true");
        Assert.assertEquals(Operations.convert(Var.valueOf("true"), Var.valueOf("BOOLEAN")).getObject(), true);
        Assert.assertEquals(Operations.convert(Var.valueOf("false"), Var.valueOf("BOOLEAN")).getObject(), false);
        Assert.assertEquals(Operations.convert(Var.valueOf("2012-05-12T04:05:24Z"), Var.valueOf("DATETIME")).getObject(), getTestDate());

        Calendar cal = Calendar.getInstance();
        cal.setTime(getTestDate().getTime());
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        Assert.assertEquals(Operations.convert(Var.valueOf("2012-05-12T04:05:24Z"), Var.valueOf("DATE")).getObject(), cal);
        Assert.assertEquals(Operations.convert(Var.valueOf("2012-05-12T04:05:24Z"), Var.valueOf("TEXTTIME")).getObject(), "04:05:24");
        Assert.assertEquals(Operations.convert(Var.valueOf("2012-05-12T04:05:24Z"), Var.valueOf("TIME")).getObject(), Var.valueOf("1970-01-01T04:05:24Z").getObjectAsDateTime());
        Assert.assertEquals(Operations.convert(Var.valueOf("2012-05-12T04:05:24Z"), Var.valueOf("ISODATE")).getObject(), "2012-05-12T04:05:24Z");

        Assert.assertEquals(Operations.convert(Var.valueOf("1"), Var.valueOf("INTEGER")).getObject(), 1L);
        Assert.assertEquals(Operations.convert(Var.valueOf("1.1"), Var.valueOf("INTEGER")).getObject(), 1L);
        Assert.assertEquals(Operations.convert(Var.valueOf("1.1"), Var.valueOf("DOUBLE")).getObject(), 1.1D);
        Assert.assertEquals(Operations.convert(Var.valueOf("1"), Var.valueOf("DOUBLE")).getObject(), 1.0D);

        Map<String, String> map = new LinkedHashMap<>();
        map.put("Test", "Value");

        Assert.assertEquals(Operations.convert(Var.valueOf(map), Var.valueOf("MAP")).getObject(), map);

        List<String> list = new LinkedList<>();
        list.add("Test");

        Assert.assertEquals(((List) Operations.convert(Var.valueOf(list), Var.valueOf("LIST")).getObject()).get(0), list.get(0));

        Assert.assertEquals(((List) Operations.convert(Var.valueOf("[1,2]"), Var.valueOf("LIST")).getObject()).get(0), "1");
        Assert.assertEquals(((List) Operations.convert(Var.valueOf("1"), Var.valueOf("LIST")).getObject()).get(0), "1");

        byte[] test = "teste".getBytes();
        String b64 = new String(Base64.getEncoder().encode(test));

        Assert.assertEquals(Operations.convert(Var.valueOf(test), Var.valueOf("BYTEARRAY")).getObject(), test);
        Assert.assertEquals(new String((byte[]) Operations.convert(Var.valueOf(b64), Var.valueOf("BYTEARRAY")).getObject()), new String(test));
        Assert.assertEquals(new String((byte[]) Operations.convert(Var.valueOf("teste"), Var.valueOf("BYTEARRAY")).getObject()), new String(test));

    }

    @Test
    void testStringToBase64() throws Exception {
        Var result = Operations.StringToBase64(Var.valueOf(CRONAPP));
        assertEquals(VALUE_ASSERT, result.getObjectAsString());
    }

    @Test
    void testBase64ToString() throws Exception {
        Var result = Operations.base64ToString(Var.valueOf(VALUE_ASSERT));
        assertEquals(CRONAPP, result.getObjectAsString());
        result = Operations.base64ToString(Var.valueOf(VALUE_ASSERT.getBytes()));
        assertEquals(CRONAPP, result.getObjectAsString());
    }

    @Test
    void testAsciiToBinary() throws Exception {
        Var result = Operations.asciiToBinary(Var.valueOf(CRONAPP));
        assertEquals("01100011011100100110111101101110011000010111000001110000", result.getObjectAsString());
    }

    @Test
    void testBinaryToAscii() throws Exception {
        Var result = Operations.binaryToAscii(Var.valueOf("01100011011100100110111101101110011000010111000001110000"));
        assertEquals(CRONAPP, result.getObjectAsString());
    }

    @Test
    void testToBytes() throws Exception {
        Var result = Operations.toBytes(Var.valueOf(CRONAPP));
        assertEquals(CRONAPP.getBytes().length, result.getObjectAsByteArray().length);
    }

    @Test
    void testChrToAscii() throws Exception {
        Var result = Operations.chrToAscii(Var.valueOf("A"));
        assertEquals("65", result.getObjectAsString());
        result = Operations.chrToAscii(Var.VAR_NULL);
        assertEquals(0, result.getObjectAsLong());
    }

    @Test
    void testHexToInt() {
        Var result = Operations.hexToInt(Var.valueOf("A"));
        assertEquals("10", result.getObjectAsString());
    }

    @Test
    void testArrayToList() throws Exception {
        Var result = Operations.arrayToList(Var.valueOf(Arrays.asList(1,2,3)));
        assertEquals(1, result.getObjectAsList().size());
    }

    @Test
    void testBase64ToBinary() throws Exception {
        Var result = Operations.base64ToBinary(Var.valueOf(VALUE_ASSERT));
        assertEquals("01100011011100100110111101101110011000010111000001110000", result.getObjectAsString());
        result = Operations.base64ToBinary(Var.VAR_NULL);
        assertNull(result.getObject());
    }

    @Test
    void testStringToJs() throws Exception {
        Var result = Operations.stringToJs(new Var(null, "object"));
        assertEquals(new Var(null, "object"), result);
    }

    @Test
    void testStringToDate() throws Exception {
        Var result = Operations.stringToDate(Var.valueOf("01/01/1978"), Var.valueOf("DD/mm/YYYY"));
        assertEquals(78, result.getObjectAsDateTime().getTime().getYear());
    }

    @Test
    void testDecToHex() throws Exception {
        Var result = Operations.decToHex(Var.valueOf(BigDecimal.valueOf(10)));
        assertEquals("a", result.getObjectAsString());
    }

    @Test
    void testToLong() throws Exception {
        Var result = Operations.toLong(Var.valueOf("10"));
        assertEquals(10, result.getObjectAsLong());
    }

    @Test
    void testToString() throws Exception {
        Var result = Operations.toString(new Var(null, "object"));
        assertEquals(new Var(null, "object"), result);
    }

    @Test
    void testToDouble() throws Exception {
        Var result = Operations.toDouble(Var.valueOf("10.5"));
        assertEquals(Double.valueOf("10.5"), result.getObjectAsDouble());
    }

    @Test
    void testToBoolean() throws Exception {
        Var result = Operations.toBoolean(Var.valueOf("true"));
        assertEquals(true, result.getObjectAsBoolean());
    }

    @Test
    void testFormatDouble() throws Exception {
        Var result = Operations.formatDouble(Var.valueOf(41251.50000000012343), Var.valueOf("#.#####"));
        assertEquals("41251,5", result.getObjectAsString());
    }

    @Test
    void testConvertLongToDate() throws Exception {
        Var result = Operations.convertLongToDate(Var.valueOf("1355270400000"));
        assertEquals(112, result.getObjectAsDateTime().getTime().getYear());
    }
}
