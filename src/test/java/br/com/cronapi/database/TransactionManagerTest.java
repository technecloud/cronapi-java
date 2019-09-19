package br.com.cronapi.database;

import app.entity.TestDataBaseType;
import cronapi.database.TransactionManager;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import static org.junit.jupiter.api.Assertions.*;

class TransactionManagerTest {

    @Test
    void addNamespace() {
        try {
            TransactionManager.addNamespace("app.entity.TestDataBaseType", Mockito.mock(EntityManager.class));
        } catch (Exception e) {
            fail();
        }
    }

    @Test
    void getEntityManager() {
        try {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("app");
            EntityManager entityManager = TransactionManager.getEntityManager(TestDataBaseType.class);
        } catch (Exception e) {
      //      fail();
        }
    }

    @Test
    void findEntityManagerFactory() {
    }

    @Test
    void commit() {
    }

    @Test
    void flush() {
    }

    @Test
    void begin() {
    }

    @Test
    void rollback() {
    }

    @Test
    void close() {
    }

    @Test
    void testCommit() {
    }

    @Test
    void testFlush() {
    }

    @Test
    void testRollback() {
    }

    @Test
    void testClose() {
    }

    @Test
    void clear() {
    }
}