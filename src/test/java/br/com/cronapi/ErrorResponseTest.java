package br.com.cronapi;

import cronapi.ErrorResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ErrorResponseTest {
    @Mock
    HashSet<String> IGNORED;
    @InjectMocks
    ErrorResponse errorResponse;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testCreateException() {
        RuntimeException runtimeException = ErrorResponse.createException(null, "method");
        assertNotNull(runtimeException);
    }

    @Test
    void testGetExceptionMessage() {
        String result = ErrorResponse.getExceptionMessage(new Exception("replaceMeWithExpectedResult"), "method");
        assertEquals("replaceMeWithExpectedResult", result);
    }
}

