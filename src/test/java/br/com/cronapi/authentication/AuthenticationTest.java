package br.com.cronapi.authentication;

import cronapi.RestClient;
import cronapi.Var;
import cronapi.authentication.Operations;
import cronapi.util.SecurityUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import org.springframework.security.core.userdetails.User;
import org.springframework.util.ReflectionUtils;

class AuthenticationTest {

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    void testGetUserRolesEmpty() throws Exception {
        Var result = Operations.getUserRoles();
        Assertions.assertEquals(0, result.getObjectAsList().size());
    }

    @Test
    void testGetUserRoles() throws Exception {
        List<cronapi.util.SecurityUtil.SecurityGroup> list = new ArrayList<>();
        cronapi.util.SecurityUtil.SecurityGroup securityGroup = new SecurityUtil.SecurityGroup();
        securityGroup.id = "Administrators";
        securityGroup.name = "Administrators";
        list.add(securityGroup);
        RestClient.getRestClient().setUser(new User("teste", "teste", new ArrayList<>()));
        Var result = Operations.getUserRoles();
        Assertions.assertEquals(0, result.getObjectAsList().size());
    }


}
