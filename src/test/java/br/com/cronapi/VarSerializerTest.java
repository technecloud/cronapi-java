package br.com.cronapi;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import cronapi.Var;
import cronapi.VarSerializer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.Spy;

import static org.junit.Assert.*;

public class VarSerializerTest {

    @Spy
    private VarSerializer varSerializer;

    @BeforeEach
    public void before() throws Exception {
        varSerializer = new VarSerializer();
    }

    @Test
    public void serialize() {
        JsonElement jsonElement = varSerializer.serialize(Var.valueOf("teste"), String.class, Mockito.mock(JsonSerializationContext.class));
        assertEquals(jsonElement.getAsString(), "teste");
    }
}