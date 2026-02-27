package controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;
    private WebRequest webRequest;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
        MockHttpServletRequest servletRequest = new MockHttpServletRequest();
        servletRequest.setRequestURI("/test/endpoint");
        webRequest = new ServletWebRequest(servletRequest);
    }

    @Test
    void testHandleIllegalArgumentException() {
        IllegalArgumentException ex = new IllegalArgumentException("Invalid input");

        ResponseEntity<GlobalExceptionHandler.ErrorResponse> response =
                handler.handleIllegalArgumentException(ex, webRequest);

        assertEquals(400, response.getStatusCodeValue());

        GlobalExceptionHandler.ErrorResponse body = response.getBody();
        assertNotNull(body);
        assertEquals(400, body.status());
        assertEquals("Bad Request", body.error());
        assertEquals("Invalid input", body.message());
        assertEquals("/test/endpoint", body.path());
        assertNotNull(body.timestamp());
    }

    @Test
    void testHandleNotFound() {
        NoSuchElementException ex = new NoSuchElementException("User not found");

        ResponseEntity<String> response = handler.handleNotFound(ex);

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("User not found", response.getBody());
    }
}