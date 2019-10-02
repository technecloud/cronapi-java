package br.com.cronapi;

import cronapi.ClientCommand;
import cronapi.Var;
import org.apache.olingo.odata2.api.ClientCallback;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ClientCommandTest {
    @Mock
    Var function;
    @Mock
    List<Var> params;
    @InjectMocks
    ClientCommand clientCommand;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testAddParam() {
        clientCommand.addParam("values", "1", "2");
        assertEquals(3, clientCommand.getParams().size());
        List<Var> lts = clientCommand.getParams();
        lts.add(Var.valueOf("5"));
        clientCommand.setParams(lts);
        assertEquals(4, clientCommand.getParams().size());
    }

    @Test
    void testToClientCallback() {
        when(function.toString()).thenReturn("toStringResponse");

        ClientCallback result = clientCommand.toClientCallback();
        assertEquals("", result.getFunction());
    }
}