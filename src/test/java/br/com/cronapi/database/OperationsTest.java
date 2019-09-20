package br.com.cronapi.database;

import app.entity.TestDataBaseType;
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
        assertEquals(Operations.hasElement(item.get()).getObjectAsBoolean(), false);
        assertDoesNotThrow(() -> {
            item.set(cronapi.object.Operations.newObject(Var.valueOf("app.entity.TestDataBaseType"), Var.valueOf("typeString", Var.valueOf("test")), Var.valueOf("typeLogic", Var.VAR_TRUE), Var.valueOf("typeCaract", Var.valueOf("A"))));
            Var item2 = cronapi.object.Operations.newObject(Var.valueOf("app.entity.TestDataBaseType"), Var.valueOf("typeString", Var.valueOf("test")), Var.valueOf("typeLogic", Var.VAR_TRUE), Var.valueOf("typeCaract", Var.valueOf("A")));
            Operations.insert(Var.valueOf("app.entity.TestDataBaseType"), item.get());
            Operations.insert(Var.valueOf("app.entity.TestDataBaseType"), Var.valueOf("typeString", Var.valueOf("test2")), Var.valueOf("typeLogic", Var.VAR_TRUE));

            Var valueTest = Var.valueOf("test");
            item.set(Operations.query(Var.valueOf("app.entity.TestDataBaseType"), Var.valueOf("select t, t.id, t.typeBigDecinal, t.typeBigInteger, t.typeByte, t.typeByteArray, t.typeByteArrayBanco, t.typeCaract, t.typeDate, t.typeDateTime, t.typeIntero, t.typeLogic, t.typeLong, t.typeNumerico, t.typeShort, t.typeString, t.typeTime from TestDataBaseType t where t.typeString = :typeString"), Var.valueOf("typeString", valueTest)));
            DataSource dataSource = (DataSource) item.get().getObject();
            assertEquals(Operations.hasElement(item.get()).getObjectAsBoolean(), true);
            assertEquals(dataSource.getPage().getTotalElements(), 1);
            Operations.close(item.get());
            assertDoesNotThrow(() -> Operations.rollbackTransaction(Var.valueOf("app.entity.TestDataBaseType")));
        });


    }

    @Test
    void executeQueryNotQuery() {
        assertDoesNotThrow(() -> Operations.beginTransaction(Var.valueOf("app.entity.TestDataBaseType")));
        Var retorno = Operations.query(Var.valueOf("app.entity.TestDataBaseType"),Var.valueOf(Var.VAR_NULL));
        DataSource ds = (DataSource) retorno.getObject();
        assertEquals(ds.getPage().getTotalElements(), 0);
        assertDoesNotThrow(() -> Operations.rollbackTransaction(Var.valueOf("app.entity.TestDataBaseType")));
    }

    @Test
    void hasElement() {
        assertEquals(Operations.hasElement(Var.valueOf(Var.VAR_NULL)).getObjectAsBoolean(), false);
    }

    @Test
    void updateField() {
        assertDoesNotThrow(() -> Operations.updateField(Var.valueOf("app.entity.TestDataBaseType"), Var.valueOf("typeString"),  Var.valueOf("test2")));
    }

    @Test
    void getActiveData(){
        assertDoesNotThrow(() -> Operations.beginTransaction(Var.valueOf("app.entity.TestDataBaseType")));
        AtomicReference<Var> item = new AtomicReference<>(Operations.query(Var.valueOf("app.entity.TestDataBaseType"), Var.valueOf("select t, t.id, t.typeBigDecinal, t.typeBigInteger, t.typeByte, t.typeByteArray, t.typeByteArrayBanco, t.typeCaract, t.typeDate, t.typeDateTime, t.typeIntero, t.typeLogic, t.typeLong, t.typeNumerico, t.typeShort, t.typeString, t.typeTime from TestDataBaseType t")));
        DataSource ds = (DataSource) item.get().getObject();
        assertNull(Operations.getActiveData(Var.valueOf(item.get())).getObject());
        assertDoesNotThrow(() -> Operations.rollbackTransaction(Var.valueOf("app.entity.TestDataBaseType")));
    }

    @Test
    void update() throws Exception {
        assertDoesNotThrow(() -> Operations.beginTransaction(Var.valueOf("app.entity.TestDataBaseType")));
        Var item = cronapi.object.Operations.newObject(Var.valueOf("app.entity.TestDataBaseType"), Var.valueOf("typeString", Var.valueOf("test")), Var.valueOf("typeLogic", Var.VAR_TRUE), Var.valueOf("typeCaract", Var.valueOf("A")));
        Operations.insert(Var.valueOf("app.entity.TestDataBaseType"), item);
        String chave = ((TestDataBaseType) item.getObject()).getId();
        Var valueTest = Var.valueOf("test");
        item = Operations.query(Var.valueOf("app.entity.TestDataBaseType"), Var.valueOf("select t, t.id, t.typeBigDecinal, t.typeBigInteger, t.typeByte, t.typeByteArray, t.typeByteArrayBanco, t.typeCaract, t.typeDate, t.typeDateTime, t.typeIntero, t.typeLogic, t.typeLong, t.typeNumerico, t.typeShort, t.typeString, t.typeTime from TestDataBaseType t where t.typeString = :typeString"), Var.valueOf("typeString", valueTest));

        Var itemUpdate = cronapi.object.Operations.newObject(Var.valueOf("app.entity.TestDataBaseType"), Var.valueOf("typeString", Var.valueOf("test2")), Var.valueOf("typeLogic", Var.VAR_TRUE), Var.valueOf("typeCaract", Var.valueOf("A")), Var.valueOf("id", Var.valueOf(chave)));
        Operations.update(Var.valueOf("app.entity.TestDataBaseType"), itemUpdate);
        valueTest = Var.valueOf("test2");
        item = Operations.query(Var.valueOf("app.entity.TestDataBaseType"), Var.valueOf("select t, t.id, t.typeBigDecinal, t.typeBigInteger, t.typeByte, t.typeByteArray, t.typeByteArrayBanco, t.typeCaract, t.typeDate, t.typeDateTime, t.typeIntero, t.typeLogic, t.typeLong, t.typeNumerico, t.typeShort, t.typeString, t.typeTime from TestDataBaseType t where t.typeString = :typeString"), Var.valueOf("typeString", valueTest));
        DataSource dataSource = (DataSource) item.getObject();
        assertEquals(dataSource.getPage().getTotalElements(), 1);
        assertNull(Operations.getField(Var.valueOf(dataSource),  Var.valueOf("TestDataBaseType")).getObject());
        assertDoesNotThrow(() -> Operations.rollbackTransaction(Var.valueOf("app.entity.TestDataBaseType")));

    }

    @Test
    void delete() throws Exception {
        assertDoesNotThrow(() -> Operations.beginTransaction(Var.valueOf("app.entity.TestDataBaseType")));
        Var item = cronapi.object.Operations.newObject(Var.valueOf("app.entity.TestDataBaseType"), Var.valueOf("typeString", Var.valueOf("test")), Var.valueOf("typeLogic", Var.VAR_TRUE), Var.valueOf("typeCaract", Var.valueOf("A")));
        Operations.insert(Var.valueOf("app.entity.TestDataBaseType"), item);
        String chave = ((TestDataBaseType) item.getObject()).getId();
        Var valueTest = Var.valueOf("test");
        item = Operations.query(Var.valueOf("app.entity.TestDataBaseType"), Var.valueOf("select t, t.id, t.typeBigDecinal, t.typeBigInteger, t.typeByte, t.typeByteArray, t.typeByteArrayBanco, t.typeCaract, t.typeDate, t.typeDateTime, t.typeIntero, t.typeLogic, t.typeLong, t.typeNumerico, t.typeShort, t.typeString, t.typeTime from TestDataBaseType t where t.typeString = :typeString"), Var.valueOf("typeString", valueTest));

        Var itemUpdate = cronapi.object.Operations.newObject(Var.valueOf("app.entity.TestDataBaseType"), Var.valueOf("typeString", Var.valueOf("test2")), Var.valueOf("typeLogic", Var.VAR_TRUE), Var.valueOf("typeCaract", Var.valueOf("A")), Var.valueOf("id", Var.valueOf(chave)));
        Operations.remove(Var.valueOf("app.entity.TestDataBaseType"), itemUpdate);
        valueTest = Var.valueOf("test2");
        item = Operations.query(Var.valueOf("app.entity.TestDataBaseType"), Var.valueOf("select t, t.id, t.typeBigDecinal, t.typeBigInteger, t.typeByte, t.typeByteArray, t.typeByteArrayBanco, t.typeCaract, t.typeDate, t.typeDateTime, t.typeIntero, t.typeLogic, t.typeLong, t.typeNumerico, t.typeShort, t.typeString, t.typeTime from TestDataBaseType t where t.typeString = :typeString"), Var.valueOf("typeString", valueTest));
        DataSource dataSource = (DataSource) item.getObject();
        assertEquals(dataSource.getPage().getTotalElements(), 0);
        item = cronapi.object.Operations.newObject(Var.valueOf("app.entity.TestDataBaseType"), Var.valueOf("typeString", Var.valueOf("test")), Var.valueOf("typeLogic", Var.VAR_TRUE), Var.valueOf("typeCaract", Var.valueOf("A")));
        Operations.insert(Var.valueOf("app.entity.TestDataBaseType"), item);
    }

    @Test
    void remover() throws Exception {
        assertDoesNotThrow(() -> Operations.beginTransaction(Var.valueOf("app.entity.TestDataBaseType")));
        Var item = cronapi.object.Operations.newObject(Var.valueOf("app.entity.TestDataBaseType"), Var.valueOf("typeString", Var.valueOf("test")), Var.valueOf("typeLogic", Var.VAR_TRUE), Var.valueOf("typeCaract", Var.valueOf("A")));
        Operations.insert(Var.valueOf("app.entity.TestDataBaseType"), item);
        item = Operations.query(Var.valueOf("app.entity.TestDataBaseType"), Var.valueOf("select t, t.id, t.typeBigDecinal, t.typeBigInteger, t.typeByte, t.typeByteArray, t.typeByteArrayBanco, t.typeCaract, t.typeDate, t.typeDateTime, t.typeIntero, t.typeLogic, t.typeLong, t.typeNumerico, t.typeShort, t.typeString, t.typeTime from TestDataBaseType t "));
        //DataSource dataSource = (DataSource) item.getObject();
        DataSource ds = new DataSource("app.entity.TestDataBaseType");
        Var finalItem = item;
        assertThrows(RuntimeException.class, () ->  Operations.remove(finalItem));
        assertDoesNotThrow(() -> Operations.rollbackTransaction(Var.valueOf("app.entity.TestDataBaseType")));
    }

    @Test
    void getFieldFromDatasource(){
        assertNull(Operations.getFieldFromDatasource().getObject());
    }

}