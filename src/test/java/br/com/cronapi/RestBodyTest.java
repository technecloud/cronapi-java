package br.com.cronapi;

import cronapi.RestBody;
import cronapi.Var;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class RestBodyTest {
    //Field inputs of type Var[] - was not mocked since Mockito doesn't mock arrays
    @Mock
    Map<String, Var> fields;
    @InjectMocks
    RestBody restBody;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetInputs() {
        Var[] result = restBody.getInputs();
        assertNull(result);
        restBody.setFields(new HashMap() {{
            put("String", Var.VAR_NULL);
        }});
        Map<?, ?> resultMap = restBody.getFields();
        assertEquals(1, resultMap.size());
        Var[] teste = new Var[1];
        teste[0] = Var.valueOf(resultMap);
        restBody.setInputs(teste);
        result = restBody.getInputs();
        assertTrue(result.length > 0);
        Var resultVar = restBody.getFirtsInput();
        assertTrue(resultVar.compareTo(teste[0]) == 0);
    }

    @Test
    void testGetFirtsInputNull() {
        Var result = restBody.getFirtsInput();
        assertNull(result);
    }

    @Test
    void testGetEntityData() {
        Map<?, ?> result = restBody.getEntityData();
        assertNull(result);
    }

    @Test
    void testParseBody() {
        RestBody result = RestBody.parseBody(new HashMap() {{
            put("String", "String");
        }});
        assertEquals("String", result.getInputs()[0].getObjectAsList().get(0).toString());
    }

    @Test
    void testParseBody2() {
        RestBody result = RestBody.parseBody(new HashMap() {{
            put("String", "String");
        }}, true);
        assertNull(result.getFirtsInput());
    }
}
