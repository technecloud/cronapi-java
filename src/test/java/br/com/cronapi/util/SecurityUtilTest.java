package br.com.cronapi.util;

import cronapi.Var;
import cronapi.util.Operations;
import cronapi.util.SecurityUtil;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.Assert.*;

public class SecurityUtilTest {

    @Test
    public void getRoles() {
        List<SecurityUtil.SecurityGroup> data = SecurityUtil.getRoles();
        Assert.assertTrue(Var.valueOf(data).isEmptyOrNull());
    }
}