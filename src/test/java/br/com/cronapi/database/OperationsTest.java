package br.com.cronapi.database;

import cronapi.Var;
import cronapi.database.DataSource;
import cronapi.database.Operations;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

class OperationsTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void beginTransaction() {
        assertDoesNotThrow(() -> Operations.beginTransaction(Var.valueOf("app.entity.TestDataBaseType")));
        assertDoesNotThrow(() -> Operations.rollbackTransaction(Var.valueOf("app.entity.TestDataBaseType")));
        assertThrows(ClassCastException.class, () -> Operations.close(Var.valueOf("app.entity.TestDataBaseType")));
    }

    @Test
    void flushTransaction() {
        assertDoesNotThrow(() -> Operations.beginTransaction(Var.valueOf("app.entity.TestDataBaseType")));
        assertDoesNotThrow(() -> Operations.flushTransaction(Var.valueOf("app.entity.TestDataBaseType")));
    }

    @Test
    void beginAndCloseTransaction() throws Exception {
        assertDoesNotThrow(() -> Operations.beginTransaction(Var.valueOf("app.entity.TestDataBaseType")));
        Var item = cronapi.object.Operations.newObject(Var.valueOf("app.entity.TestDataBaseType"),Var.valueOf("typeString",Var.valueOf("teste string")),Var.valueOf("typeLogic",Var.VAR_TRUE),Var.valueOf("typeCaract",Var.valueOf("A")));
        assertThrows(ClassCastException.class, () -> Operations.close(item));
    }

    @Test
    void commitTransaction() throws Exception {
        assertDoesNotThrow(() -> Operations.beginTransaction(Var.valueOf("app.entity.TestDataBaseType")));
        assertDoesNotThrow(() -> Operations.commitTransaction(Var.valueOf("app.entity.TestDataBaseType")));
    }

    @Test
    void executeQuery(){
        assertDoesNotThrow(() -> Operations.beginTransaction(Var.valueOf("app.entity.TestDataBaseType")));
        AtomicReference<Var> item = new AtomicReference<>(Operations.query(Var.valueOf("app.entity.TestDataBaseType"), Var.valueOf("select t, t.id, t.typeBigDecinal, t.typeBigInteger, t.typeByte, t.typeByteArray, t.typeByteArrayBanco, t.typeCaract, t.typeDate, t.typeDateTime, t.typeIntero, t.typeLogic, t.typeLong, t.typeNumerico, t.typeShort, t.typeString, t.typeTime from TestDataBaseType t")));
        DataSource ds = (DataSource) item.get().getObject();
        assertEquals(ds.getPage().getTotalElements(), 0);
        assertDoesNotThrow(() -> {
            item.set(cronapi.object.Operations.newObject(Var.valueOf("app.entity.TestDataBaseType"), Var.valueOf("typeString", Var.valueOf("test")), Var.valueOf("typeLogic", Var.VAR_TRUE), Var.valueOf("typeCaract", Var.valueOf("A"))));
            Operations.insert(Var.valueOf("app.entity.TestDataBaseType"), item.get());
        });

        Var valueTest = Var.valueOf("test");
        item.set(Operations.query(Var.valueOf("app.entity.TestDataBaseType"), Var.valueOf("select t, t.id, t.typeBigDecinal, t.typeBigInteger, t.typeByte, t.typeByteArray, t.typeByteArrayBanco, t.typeCaract, t.typeDate, t.typeDateTime, t.typeIntero, t.typeLogic, t.typeLong, t.typeNumerico, t.typeShort, t.typeString, t.typeTime from TestDataBaseType t where t.typeString = :typeString"), Var.valueOf("typeString", valueTest)));
        assertEquals(ds.getPage().getTotalElements(), 0);
        assertDoesNotThrow(() -> Operations.rollbackTransaction(Var.valueOf("app.entity.TestDataBaseType")));
    }
}