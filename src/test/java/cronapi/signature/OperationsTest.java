package cronapi.signature;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import cronapi.Var;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.security.*;
import java.util.Map;


public class OperationsTest {

    private KeyPair keys;
    private File file = File.createTempFile("temp", "file");
    public OperationsTest() throws IOException {
    }

    @BeforeTest
    public void setup() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
        Map<String, Object> object = mapper.readValue("{\"someKey1\": \"someValue1\", \"someKey2\": [{ \"someInnerKey1\": \"someInnerValue1\" },{ \"someInnerKey2\": \"someInnerValue2\"}]}", new TypeReference<Map<String, Object>>() {
        });
        writer.writeValue(this.file, object);
        this.keys = Operations.generateKeyPair(file.getPath(), file.getName(), "RSA", 2048);
    }

    @AfterTest
    public void remove(){
        this.file.deleteOnExit();
    }

    @Test
    public void generateKeyPairTest() throws Exception {
        Assert.assertThrows(InvalidParameterException.class , () -> Operations.generateKeyPair(file.getPath(), file.getName(), "RSA", 0));
        Assert.assertThrows(NoSuchAlgorithmException.class , () -> Operations.generateKeyPair(file.getPath(), file.getName(), "fakeAlgorithm", 0));
        Assert.assertNotNull(Operations.generateKeyPair("", null, "RSA", 2048));
    }

    @Test
    public void testSignObject() throws Exception {
        SignedObject signedObject = Operations.signObject(Var.valueOf(this.file), this.keys.getPrivate(), "SHA256withRSA");
        Assert.assertNotNull(signedObject);
    }
    @Test
    public void testVerify() throws Exception {
        SignedObject signedObject = Operations.signObject(Var.valueOf(this.file), this.keys.getPrivate(), "SHA256withRSA");
        KeyPair keys = Operations.generateKeyPair(file.getPath(), file.getName(), "RSA", 2048);
        Assert.assertTrue(Operations.verify(signedObject, this.keys.getPublic(), "SHA256withRSA"));
        Assert.assertFalse(Operations.verify(signedObject, keys.getPublic(), "SHA256withRSA"));
    }



}