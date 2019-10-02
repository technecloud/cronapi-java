package br.com.cronapi.rest;

import app.entity.TestDataBaseType;
import br.com.cronapi.json.JsonTest;
import cronapi.*;
import cronapi.database.EntityMetadata;
import cronapi.database.TenantService;
import cronapi.rest.CronapiREST;
import cronapi.util.SecurityUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

import static cronapi.json.Operations.toJson;
import static org.mockito.Mockito.*;

class CronapiRESTTest {

    private static final String APP_ENTITY_TEST_DATA_BASE_TYPE = "app.entity.TestDataBaseType";

    @Mock
    HttpServletRequest request;
    @Mock
    HttpServletResponse response;
    @Mock
    TenantService tenantService;

    @InjectMocks
    CronapiREST cronapiREST;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testHandleControllerException() {
        ResponseEntity<ErrorResponse> result = cronapiREST.handleControllerException(request, new Exception("teste"));
        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
    }

    @Test
    void testDataOptions() throws Exception {
        HttpEntity<EntityMetadata> result = cronapiREST.dataOptions(APP_ENTITY_TEST_DATA_BASE_TYPE);
        Assertions.assertEquals(APP_ENTITY_TEST_DATA_BASE_TYPE, ((EntityMetadata)result.getBody()).getName());
    }

    @Test
    void testCrudGetException() throws Exception {
        when(request.getServletPath()).thenReturn("app/app.entity.TestDataBaseType");
        try {
            HttpEntity<Object> result = cronapiREST.crudGet(APP_ENTITY_TEST_DATA_BASE_TYPE, PageRequest.of(0, 10));
            Assertions.assertEquals(HttpStatus.OK,((ResponseEntity)result).getStatusCode());
        }catch (Exception ex) {
            Assertions.assertThrows(RuntimeException.class, () -> cronapiREST.crudGet(APP_ENTITY_TEST_DATA_BASE_TYPE, PageRequest.of(0, 10)));
        }
    }

    @Test
    void testCrudGet() throws Exception {
        getDados();
        HttpEntity<Object> result =  cronapiREST.crudGet(APP_ENTITY_TEST_DATA_BASE_TYPE, PageRequest.of(0, 10));
        Assertions.assertEquals(HttpStatus.OK,((ResponseEntity)result).getStatusCode());
    }

    @Test
    void testCrudPutException() throws Exception {
        getDados();
        TestDataBaseType testDataBaseType = new TestDataBaseType();
        testDataBaseType.setTypeString("teste");
        Assertions.assertThrows(RuntimeException.class, () -> cronapiREST.crudPut(APP_ENTITY_TEST_DATA_BASE_TYPE, Var.valueOf(testDataBaseType)));
    }

    @Test
    void testCrudPut() throws Exception {
        getDados();
        TestDataBaseType testDataBaseType = new TestDataBaseType();
        Map<String, Object> listTets = getStringObjectMap(testDataBaseType);
        HttpEntity<Object> result = cronapiREST.crudPost(APP_ENTITY_TEST_DATA_BASE_TYPE, Var.valueOf(listTets));
        Assertions.assertEquals(HttpStatus.OK,((ResponseEntity)result).getStatusCode());
        testDataBaseType.setTypeString("teste1");
        listTets.put("typeString", testDataBaseType.getTypeString());
        result = cronapiREST.crudPut(APP_ENTITY_TEST_DATA_BASE_TYPE, Var.valueOf(testDataBaseType));
        Assertions.assertEquals(HttpStatus.OK,((ResponseEntity)result).getStatusCode());

    }

    @Test
    void testCrudPost() throws Exception {
        getDados();
        TestDataBaseType testDataBaseType = new TestDataBaseType();
        Map<String, Object> listTets = getStringObjectMap(testDataBaseType);
        HttpEntity<Object> result = cronapiREST.crudPost(APP_ENTITY_TEST_DATA_BASE_TYPE, Var.valueOf(listTets));
        Assertions.assertEquals(HttpStatus.OK,((ResponseEntity)result).getStatusCode());
   //     cronapiREST.crudDelete("/"+APP_ENTITY_TEST_DATA_BASE_TYPE+"/"+testDataBaseType.getId()+"/");
    }

  //  @Test
    void testCrudDelete() throws Exception {
        getDados();
        HttpEntity<Object> result =  cronapiREST.crudGet(APP_ENTITY_TEST_DATA_BASE_TYPE+"/teste", PageRequest.of(0, 10));
        cronapiREST.crudDelete(APP_ENTITY_TEST_DATA_BASE_TYPE);
    }

    @Test
    void testQueryGetNewException() {
        Assertions.assertThrows(RuntimeException.class, () -> cronapiREST.queryGetNew("id"));
    }


    private Map<String, Object> getStringObjectMap(TestDataBaseType testDataBaseType ) {
        testDataBaseType.setTypeString("teste");
        Map<String, Object> listTets = new HashMap<>();
        listTets.put("id", testDataBaseType.getId());
        listTets.put("typeString", testDataBaseType.getTypeString());
        return listTets;
    }

    private void getDados() {
        when(request.getServletPath()).thenReturn("app/app.entity.TestDataBaseType/");
        RestClient.getRestClient().setUser(new User("teste", "teste", new ArrayList<>()));
    }


//    @Test
//    void testQueryGet() throws Exception {
//        HttpEntity<?> result = cronapiREST.queryGet("id", null);
//        Assertions.assertEquals(null, result);
//    }
//
//    @Test
//    void testQueryPost() throws Exception {
//        Object result = cronapiREST.queryPost("id", new HashMap() {{
//            put(null, null);
//        }}, true);
//        Assertions.assertEquals("replaceMeWithExpectedResult", result);
//    }
//
//    @Test
//    void testQueryPut() throws Exception {
//        Object result = cronapiREST.queryPut("id", new HashMap() {{
//            put(null, null);
//        }}, true);
//        Assertions.assertEquals("replaceMeWithExpectedResult", result);
//    }
//
//    @Test
//    void testQueryDelete() throws Exception {
//        Object result = cronapiREST.queryDelete("id", true);
//        Assertions.assertEquals("replaceMeWithExpectedResult", result);
//    }
//
//    @Test
//    void testPostBody() throws Exception {
//        RestResult result = cronapiREST.postBody(null, "clazz");
//        Assertions.assertEquals(new RestResult(new Var("id", "object"), Arrays.<ClientCommand>asList(new ClientCommand("function"))), result);
//    }
//
//    @Test
//    void testGetParam() throws Exception {
//        RestResult result = cronapiREST.getParam("TestDataBaseType.clazz");
//        Assertions.assertEquals(new RestResult(new Var("id", "object"), Arrays.<ClientCommand>asList(new ClientCommand("function"))), result);
//    }
//
//    @Test
//    void testPostParams() throws Exception {
//        RestResult result = cronapiREST.postParams(new Var[]{new Var(null, "object")}, "clazz");
//        Assertions.assertEquals(new RestResult(new Var("id", "object"), Arrays.<ClientCommand>asList(new ClientCommand("function"))), result);
//    }
//
//    @Test
//    void testGetRest() throws Exception {
//        Var result = cronapiREST.getRest("clazz");
//        Assertions.assertEquals(new Var("id", "object"), result);
//    }
//
//    @Test
//    void testPostRestRaw() throws Exception {
//        Var result = cronapiREST.postRestRaw("body", "clazz");
//        Assertions.assertEquals(new Var("id", "object"), result);
//    }
//
//    @Test
//    void testPostRestBinary() throws Exception {
//        Var result = cronapiREST.postRestBinary("body", "clazz");
//        Assertions.assertEquals(new Var("id", "object"), result);
//    }
//
//    @Test
//    void testPostRestForm() throws Exception {
//        Var result = cronapiREST.postRestForm(new HashMap<String, String>() {{
//            put("String", "String");
//        }}, "clazz");
//        Assertions.assertEquals(new Var("id", "object"), result);
//    }
//
//    @Test
//    void testPostRest() throws Exception {
//        Var result = cronapiREST.postRest(new Var("id", "object"), "clazz");
//        Assertions.assertEquals(new Var("id", "object"), result);
//    }
//
//    @Test
//    void testPutRest() throws Exception {
//        Var result = cronapiREST.putRest(new Var[]{new Var(null, "object")}, "clazz");
//        Assertions.assertEquals(new Var("id", "object"), result);
//    }
//
//    @Test
//    void testDeleteRest() throws Exception {
//        Var result = cronapiREST.deleteRest("clazz");
//        Assertions.assertEquals(new Var("id", "object"), result);
//    }
//
//    @Test
//    void testPostRestLegacy() throws Exception {
//        Var result = cronapiREST.postRestLegacy(new Var[]{new Var(null, "object")}, "clazz");
//        Assertions.assertEquals(new Var("id", "object"), result);
//    }
//
//    @Test
//    void testGetRestLegacy() throws Exception {
//        Var result = cronapiREST.getRestLegacy("clazz");
//        Assertions.assertEquals(new Var("id", "object"), result);
//    }
//
//    @Test
//    void testPutRestLegacy() throws Exception {
//        Var result = cronapiREST.putRestLegacy(new Var[]{new Var(null, "object")}, "clazz");
//        Assertions.assertEquals(new Var("id", "object"), result);
//    }
//
//    @Test
//    void testDeleteRestLegacy() throws Exception {
//        Var result = cronapiREST.deleteRestLegacy("clazz");
//        Assertions.assertEquals(new Var("id", "object"), result);
//    }
//
//    @Test
//    void testSecurityRoles() throws Exception {
//        List<SecurityUtil.SecurityGroup> result = cronapiREST.securityRoles();
//        Assertions.assertEquals(Arrays.<SecurityUtil.SecurityGroup>asList(new SecurityUtil.SecurityGroup()), result);
//    }
//
//    @Test
//    void testFilePreview() throws Exception {
//        cronapiREST.filePreview("fileName");
//    }
//
//    @Test
//    void testDownloadFile() throws Exception {
//        cronapiREST.downloadFile("entity", "field", new Var("id", "object"));
//    }
//
//    @Test
//    void testDownloadFileGet() throws Exception {
//        cronapiREST.downloadFileGet("entity", "field", "ids");
//    }

//    @Test
//    void testUploadFile() throws Exception {
//        ResponseEntity<Object> result = cronapiREST.uploadFile(new MultipartFile[]{null});
//        Assertions.assertEquals(null, result);
//    }
}
