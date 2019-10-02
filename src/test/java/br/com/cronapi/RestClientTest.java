package br.com.cronapi;

import cronapi.ClientCommand;
import cronapi.RestBody;
import cronapi.RestClient;
import cronapi.Var;
import cronapi.database.TenantService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class RestClientTest {

    @Test
    void checkGetRequest() {
        RestClient.getRestClient().getRequest();
    }

    @Test
    void checkGetResponse() {
        RestClient.getRestClient().getResponse();
    }

    @Mock
    ThreadLocal<RestClient> REST_CLIENT;
    @Mock
    LinkedList<ClientCommand> commands;
    @Mock
    HttpServletResponse response;
    @Mock
    HttpServletRequest request;
    @Mock
    HttpSession session;
    @Mock
    User user;
    //Field query of type JsonObject - was not mocked since Mockito doesn't mock a Final class when 'mock-maker-inline' option is not set
    //Field locale of type Locale - was not mocked since Mockito doesn't mock a Final class when 'mock-maker-inline' option is not set
    @Mock
    Map<String, String> parameters;
    @Mock
    List<GrantedAuthority> DEFAULT_AUTHORITIES;
    @Mock
    RestBody body;
    @Mock
    Var rawBody;
    @Mock
    TenantService tenantService;
    @InjectMocks
    RestClient restClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testClone() {
        when(tenantService.getContextIds()).thenReturn(new HashMap<String, Object>() {{
            put("String", "getContextIdsResponse");
        }});

        RestClient result = restClient.clone();
        assertTrue(result.getTenantService().getContextIds().containsValue("getContextIdsResponse"));
    }

    @Test
    void testGetContextRunnable() {
        when(tenantService.getContextIds()).thenReturn(new HashMap<String, Object>() {{
            put("String", "getContextIdsResponse");
        }});

        Runnable result = RestClient.getContextRunnable(null, true);
        assertThrows(ExceptionInInitializerError.class, () -> result.run());
    }

    @Test
    void testGetRestClient() {
        RestClient result = RestClient.getRestClient();
        assertEquals(0, result.getCommands().size());
    }

    @Test
    void testDownloadURL() {
        restClient.downloadURL("url");
    }

    @Test
    void testSetRestClient() {
        assertThrows(NoClassDefFoundError.class, () -> RestClient.setRestClient(new RestClient()));
    }

    @Test
    void testRemoveClient() {
        RestClient.removeClient();
    }

    @Test
    void testAddCommand() {
        ClientCommand clientCommand = getClientCommand();
        ClientCommand result = restClient.addCommand(clientCommand);
        assertEquals(clientCommand.getFunction().getObjectAsString(), result.getFunction().getObjectAsString());
    }

    @Test
    void testAddCommand2() {
        ClientCommand clientCommand = getClientCommand();
        ClientCommand result = restClient.addCommand("function");
        assertEquals(clientCommand.getFunction().getObjectAsString(), result.getFunction().getObjectAsString());
    }

    @Test
    void testGetBody() {
        RestBody result = restClient.getBody();
        assertNotNull(result);
        assertTrue(result instanceof RestBody);
    }

    @Test
    void testGetRequest() {
        HttpServletRequest result = restClient.getRequest();
        assertTrue(result instanceof HttpServletRequest);
    }

    @Test
    void testGetResponse() {
        HttpServletResponse result = restClient.getResponse();
        assertTrue(result instanceof HttpServletResponse);
    }

    @Test
    void testSetParameter() {
        restClient.setParameter("key", "value");
    }

    @Test
    void testSetParameters() {
        restClient.setParameters("parametersStr");
    }

    @Test
    void testGetParameter() {
        String result = restClient.getParameter("key");
        assertNull(result);
    }

    @Test
    void testHasParameter() {
        boolean result = restClient.hasParameter("key");
        assertFalse(result);
    }

    @Test
    void testGetParameter2() {
        String result = restClient.getParameter("key", "defaultValue");
        assertEquals("defaultValue", result);
    }

    @Test
    void testGetParameterAsInt() {
        int result = restClient.getParameterAsInt("key", 0);
        assertEquals(0, result);
    }

    @Test
    void testGetParameterAsBoolean() {
        boolean result = restClient.getParameterAsBoolean("key", true);
        assertEquals(true, result);
    }

    @Test
    void testGetMethod() {
        String result = restClient.getMethod();
        assertNull(result);
    }

    @Test
    void testGetUser() {
        restClient.setUser(new User("teste", "teste", new ArrayList<>()));
        User result = restClient.getUser();
        assertEquals("teste", result.getUsername());
    }

    @Test
    void testGetAuthorities() {
        Collection<GrantedAuthority> result = restClient.getAuthorities();
        assertEquals(0, result.size());
    }

    @Test
    void testGetSession() {
        HttpSession result = restClient.getSession();
        assertNotNull(result);
        assertTrue(result instanceof HttpSession);
    }

    @Test
    void testUpdateSessionValue() {
        restClient.updateSessionValue("name", "value");
    }

    @Test
    void testGetLocale() {
        Locale result = restClient.getLocale();
        assertEquals(null, result);
    }

    @Test
    void testGetSessionValue() {
        Object result = restClient.getSessionValue("name");
        assertEquals(null, result);
    }

    @Test
    void testGetUtcOffset() {
        Integer result = restClient.getUtcOffset();
        assertEquals(Integer.valueOf(-180), result);
    }

    @Test
    void testIsDefined() {
        boolean result = restClient.isDefined();
        assertEquals(true, result);
    }

    @Test
    void testGetFunction() {
        ClientCommand clientCommand = getClientCommand();
        Var command = Var.valueOf(clientCommand);
        clientCommand.setFunction(Var.valueOf("function1"));
        ClientCommand result = restClient.addCommand(clientCommand);
        assertEquals(clientCommand.getFunction().getObjectAsString(), result.getFunction().getObjectAsString());
    }

    private ClientCommand getClientCommand() {
        return new ClientCommand("function");
    }
}
