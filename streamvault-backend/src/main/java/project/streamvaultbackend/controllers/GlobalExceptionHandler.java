package project.streamvaultbackend.controllers;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.HttpStatus;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntime(RuntimeException ex) {
        // Log to console
        System.err.println("RuntimeException: " + ex.getMessage());
        //noinspection CallToPrintStackTrace
        ex.printStackTrace();
        String msg = ex.getMessage();
        HttpStatus status = HttpStatus.BAD_REQUEST; // Default

        // Custom error handling for known messages
        if ("Wrong password".equals(msg) || "Invalid credentials".equals(msg)) {
            status = HttpStatus.UNAUTHORIZED; // 401
        } else if ("User not found".equals(msg)) {
            status = HttpStatus.NOT_FOUND; // 404
        } else if ("Username already exists".equals(msg)) {
            status = HttpStatus.CONFLICT; // 409
        }  // 400


        return ResponseEntity.status(status).body(Map.of("error", msg));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArg(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
    }

    // Optional: handle all other exceptions
    @SuppressWarnings("CallToPrintStackTrace")
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleOther(Exception ex) {
        // Log to console
        System.err.println("Exception: " + ex.getMessage());
        ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Internal server error"));
    }
}
