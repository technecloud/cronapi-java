package br.com.cronapi.conversion;

import cronapi.Var;
import cronapi.conversion.Operations;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class OperationsTest {

    private static final String VALUE_ASSERT = "Y3JvbmFwcA==";
    private static final String CRONAPP = "cronapp";

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
        assertEquals(new Var("id", "object"), result);
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
        Var result = Operations.stringToDate(new Var(null, "object"), new Var(null, "object"));
        assertEquals(new Var(null, "object"), result);
    }

    @Test
    void testDecToHex() throws Exception {
        Var result = Operations.decToHex(new Var(null, "object"));
        assertEquals(new Var(null, "object"), result);
    }

    @Test
    void testToLong() throws Exception {
        Var result = Operations.toLong(new Var(null, "object"));
        assertEquals(new Var(null, "object"), result);
    }

    @Test
    void testToString() throws Exception {
        Var result = Operations.toString(new Var(null, "object"));
        assertEquals(new Var(null, "object"), result);
    }

    @Test
    void testToDouble() throws Exception {
        Var result = Operations.toDouble(new Var(null, "object"));
        assertEquals(new Var(null, "object"), result);
    }

    @Test
    void testToBoolean() throws Exception {
        Var result = Operations.toBoolean(new Var(null, "object"));
        assertEquals(new Var(null, "object"), result);
    }

    @Test
    void testFormatDouble() throws Exception {
        Var result = Operations.formatDouble(new Var("id", "object"), new Var("id", "object"));
        assertEquals(new Var("id", "object"), result);
    }

    @Test
    void testConvertLongToDate() throws Exception {
        Var result = Operations.convertLongToDate(new Var("id", "object"));
        assertEquals(new Var("id", "object"), result);
    }
}
