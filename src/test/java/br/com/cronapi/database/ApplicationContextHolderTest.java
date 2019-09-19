package br.com.cronapi.database;

import cronapi.database.ApplicationContextHolder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.support.AbstractApplicationContext;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class ApplicationContextHolderTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void setApplicationContext() throws NamingException {
        Context initContext = new InitialContext();
        ApplicationContextHolder applicationContextHolder= new ApplicationContextHolder();
        applicationContextHolder.setApplicationContext(new AbstractApplicationContext() {
            @Override
            protected void refreshBeanFactory() throws BeansException, IllegalStateException {

            }

            @Override
            protected void closeBeanFactory() {

            }

            @Override
            public ConfigurableListableBeanFactory getBeanFactory() throws IllegalStateException {
                return null;
            }
        });
        assertNotNull(applicationContextHolder);
    }

    @Test
    void getContext() {
        assertNotNull(ApplicationContextHolder.getContext());
    }
}