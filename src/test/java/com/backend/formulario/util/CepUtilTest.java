package com.backend.formulario.util;

import com.backend.formulario.util.exceptions.CepFormatoInvalidoException;
import com.backend.formulario.util.exceptions.CepNotFoundException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.net.URI;
import java.net.http.HttpRequest;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class CepUtilTest {

    @Mock
    private HttpClient httpClient;

    @Mock
    private HttpResponse<String> httpResponse;

    @Mock
    private JsonNode jsonNode;

    private CepUtil cepUtil;

    @BeforeEach
    void setUp() {
        cepUtil = new CepUtil(httpClient);
    }

    @Test
    void testConsultarCep_statusCodeNao200_deveLancarIOException() throws IOException, InterruptedException {
        String cep = "12345678";

        when(httpResponse.statusCode()).thenReturn(500);
        when(httpClient.send(any(HttpRequest.class), eq(HttpResponse.BodyHandlers.ofString())))
            .thenReturn(httpResponse);
        
            IOException thrown = assertThrows(IOException.class, () -> cepUtil.consultarCep(cep));
            assertEquals("Erro ao consultar cep", thrown.getMessage());
    }


}
