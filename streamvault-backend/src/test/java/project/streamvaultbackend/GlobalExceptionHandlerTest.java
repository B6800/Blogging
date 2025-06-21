package project.streamvaultbackend;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import project.streamvaultbackend.controllers.GlobalExceptionHandler;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void testHandleRuntime_WrongPassword() {
        ResponseEntity<Map<String, String>> resp = handler.handleRuntime(new RuntimeException("Wrong password"));
        assertEquals(HttpStatus.UNAUTHORIZED, resp.getStatusCode());
        assertNotNull(resp.getBody());
        assertEquals("Wrong password", resp.getBody().get("error"));
    }

    @Test
    void testHandleRuntime_InvalidCredentials() {
        ResponseEntity<Map<String, String>> resp = handler.handleRuntime(new RuntimeException("Invalid credentials"));
        assertEquals(HttpStatus.UNAUTHORIZED, resp.getStatusCode());
        assertNotNull(resp.getBody());
        assertEquals("Invalid credentials", resp.getBody().get("error"));
    }

    @Test
    void testHandleRuntime_UserNotFound() {
        ResponseEntity<Map<String, String>> resp = handler.handleRuntime(new RuntimeException("User not found"));
        assertEquals(HttpStatus.NOT_FOUND, resp.getStatusCode());
        assertNotNull(resp.getBody());
        assertEquals("User not found", resp.getBody().get("error"));
    }

    @Test
    void testHandleRuntime_UsernameAlreadyExists() {
        ResponseEntity<Map<String, String>> resp = handler.handleRuntime(new RuntimeException("Username already exists"));
        assertEquals(HttpStatus.CONFLICT, resp.getStatusCode());
        assertNotNull(resp.getBody());
        assertEquals("Username already exists", resp.getBody().get("error"));
    }

    @Test
    void testHandleRuntime_Other() {
        ResponseEntity<Map<String, String>> resp = handler.handleRuntime(new RuntimeException("Some other error"));
        assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
        assertNotNull(resp.getBody());
        assertEquals("Some other error", resp.getBody().get("error"));
    }

    @Test
    void testHandleIllegalArg() {
        ResponseEntity<Map<String, String>> resp = handler.handleIllegalArg(new IllegalArgumentException("Invalid input"));
        assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
        assertNotNull(resp.getBody());
        assertEquals("Invalid input", resp.getBody().get("error"));
    }

    @Test
    void testHandleOther() {
        ResponseEntity<Map<String, String>> resp = handler.handleOther(new Exception());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, resp.getStatusCode());
        assertNotNull(resp.getBody());
        assertEquals("Internal server error", resp.getBody().get("error"));
    }
}

