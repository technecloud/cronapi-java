package br.com.cronapi.database;

import app.dao.TestDataBaseTypeDAO;
import app.entity.TestDataBaseType;
import com.google.gson.JsonObject;
import cronapi.Var;
import cronapi.database.DataSource;
import cronapi.database.TransactionManager;
import org.junit.Assert;
import org.junit.jupiter.api.*;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.LocalEntityManagerFactoryBean;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.TransactionRequiredException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

public class DataSourceTest {

    @Spy
    private DataSource dataSource;

    @InjectMocks
    private EntityManager manager;

    private DataSource getDataSourceLocal() {
        return new DataSource("app.entity.TestDataBaseType");
    }

    @BeforeEach
    public void setUp() throws Exception {
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
    void getEntity() {
    }

    @Test
    void fetch() {
    }

    @Test
    void testFetch() {
    }

    @Test
    void getMetadata() {
    }

    @Test
    void insert() {
        dataSource = getDataSourceLocal();

    }

    @Test
    void toObject() {
    }

    @Test
    void testInsert() {
    }

    @Test
    void save() {
    }

    @Test
    void testSave() {
    }

    @Test
    void delete() {
    }

    @Test
    void testDelete() {
    }

    @Test
    void updateField() {
    }

    @Test
    void updateFields() {
    }

    @Test
    void filterByPk() {
    }

    @Test
    void filter() {
    }

    @Test
    void update() {
    }

    @Test
    void getObject() {
    }

    @Test
    void testGetObject() {
    }

    @Test
    void next() {
    }

    @Test
    void nextOnPage() {
    }

    @Test
    void hasNext() {
    }

    @Test
    void hasData() {
    }

    @Test
    void previous() {
    }

    @Test
    void setCurrent() {
    }

    @Test
    void getCurrent() {
    }

    @Test
    void getPage() {
    }

    @Test
    void setPageSize() {
    }

    @Test
    void testFilter() {
    }

    @Test
    void setDataSourceFilter() {
    }

    @Test
    void testFilter1() {
    }

    @Test
    void deleteRelation() {
    }

    @Test
    void insertRelation() {
    }

    @Test
    void resolveRelation() {
    }

    @Test
    void filterByRelation() {
    }

    @Test
    void clear() {
    }

    @Test
    void execute() {
    }

    @Test
    void getTotalElements() {
    }

    @Test
    void testToString() {
    }

    @Test
    void serialize() {
    }

    @Test
    void serializeWithType() {
    }

    @Test
    void checkRESTSecurity() {
    }

    @Test
    void testCheckRESTSecurity() {
    }

    @Test
    void getRelationEntity() {
    }

    @Test
    void disableMultiTenant() {
    }

    @Test
    void enableMultiTenant() {
    }

    @Test
    void getFilter() {
    }

    @Test
    void getIds() {
    }

    @Test
    void getId() {
        dataSource = Mockito.mock(DataSource.class);
        assertEquals(dataSource.getId(), null);
        dataSource =  new DataSource("app.entity.TestDataBaseType");
        assertEquals(dataSource.getId(), null);
        dataSource = getDataSourceLocal();
        dataSource.insert();
       // assertEquals(dataSource.getId(), null);
        //getEntityManager
    }

    @Test
    void getObjectWithId() {
    }

    @Test
    void validate() {
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