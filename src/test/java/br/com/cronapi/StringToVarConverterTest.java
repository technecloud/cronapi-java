package br.com.cronapi;

import cronapi.StringToVarConverter;
import cronapi.Var;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class StringToVarConverterTest {
    StringToVarConverter stringToVarConverter = new StringToVarConverter();

    @Test
    void testConvert() {
        Var result = stringToVarConverter.convert("source");
        Assertions.assertEquals("source", result.toString());
    }
}