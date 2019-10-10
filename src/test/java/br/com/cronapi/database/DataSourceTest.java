package br.com.cronapi.database;

import app.entity.TestDataBaseType;
import com.google.gson.JsonObject;
import cronapi.database.DataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;

import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.TransactionRequiredException;

import static org.junit.jupiter.api.Assertions.*;

 class DataSourceTest {

    @Spy
    private DataSource dataSource;

    @InjectMocks
    private EntityManager manager;

    private DataSource getDataSourceLocal() {
        return new DataSource("app.entity.TestDataBaseType");
    }

    @BeforeEach
     void setUp() throws Exception {
        dataSource = Mockito.mock(DataSource.class);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void datasourceTest(){
        JsonObject json = new JsonObject();
        json.addProperty("entityFullName", "app.entity.TestDataBaseType");
        DataSource ds = new DataSource(json);
        assertEquals(ds.getSimpleEntity(),"TestDataBaseType");
        ds = new DataSource("app.entity.TestDataBaseType", Mockito.mock(EntityManager.class));
        assertEquals(ds.getSimpleEntity(),"TestDataBaseType");
        assertEquals(ds.getEntity(), "app.entity.TestDataBaseType");
    }

    @Test
    void getEntityManager() {
        EntityManager entityManager = dataSource.getEntityManager(TestDataBaseType.class);
        assertNull(entityManager);
        dataSource = getDataSourceLocal();
    }

    @Test
    void getDomainClass() throws NamingException {
        dataSource = getDataSourceLocal();
        assertEquals(dataSource.getDomainClass().getName(), "app.entity.TestDataBaseType");
    }

    @Test
    void getSimpleEntity() {
        dataSource = getDataSourceLocal();
        assertEquals(dataSource.getSimpleEntity(), "TestDataBaseType");
    }

    @Test
    void getId() {
        dataSource = Mockito.mock(DataSource.class);
        assertEquals(dataSource.getId(), null);
        dataSource =  new DataSource("app.entity.TestDataBaseType");
        assertEquals(dataSource.getId(), null);
        dataSource = getDataSourceLocal();
        dataSource.insert();
    }

    @Test
    void plainData() {
        dataSource =  new DataSource("app.entity.TestDataBaseType");
        dataSource.setPlainData(true);
        assertEquals(dataSource.isPlainData(), true);
    }

    @Test
    void urlParams() {
        dataSource =  new DataSource("app.entity.TestDataBaseType");
        dataSource.setUseUrlParams(true);
        assertEquals(dataSource.useUrlParams(), true);
    }

    @Test
    void flush() {
        dataSource =  new DataSource("app.entity.TestDataBaseType");
        assertThrows(TransactionRequiredException.class, () -> dataSource.flush());
    }
}