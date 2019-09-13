package br.com.cronapi;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import cronapi.Var;
import cronapi.VarSerializer;
import org.jboss.jandex.Type;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.Spy;

import static org.junit.Assert.*;

public class VarSerializerTest {

    @Spy
    private VarSerializer varSerializer;

    @Before
    public void before() throws Exception {
        varSerializer = new VarSerializer();
    }

    @Test
    public void serialize() {
        JsonElement jsonElement = varSerializer.serialize(Var.valueOf("teste"), String.class, Mockito.mock(JsonSerializationContext.class));
        Assert.assertEquals(jsonElement.getAsString(), "teste");
    }
}