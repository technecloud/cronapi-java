package br.com.cronapi.database;

import cronapi.Var;
import cronapi.database.Operations;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
}