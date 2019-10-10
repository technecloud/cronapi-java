package br.com.cronapi.util;

import cronapi.Var;
import cronapi.util.SecurityUtil;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class SecurityUtilTest {

    @Test
    public void getRoles() {
        List<SecurityUtil.SecurityGroup> data = SecurityUtil.getRoles();
        assertTrue(Var.valueOf(data).isEmptyOrNull());
    }
}