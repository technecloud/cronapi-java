package br.com.cronapi;

import cronapi.HttpGetWithBody;
import org.apache.http.HttpEntity;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.concurrent.Cancellable;
import org.apache.http.message.HeaderGroup;
import org.apache.http.params.HttpParams;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static org.mockito.Mockito.*;

class HttpGetWithBodyTest {
    @Mock
    HttpEntity entity;
    @Mock
    ProtocolVersion version;
    //Field uri of type URI - was not mocked since Mockito doesn't mock a Final class when 'mock-maker-inline' option is not set
    @Mock
    RequestConfig config;
    @Mock
    AtomicBoolean aborted;
    @Mock
    AtomicReference<Cancellable> cancellableRef;
    @Mock
    HeaderGroup headergroup;
    @Mock
    HttpParams params;
    @InjectMocks
    HttpGetWithBody httpGetWithBody;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }
}

