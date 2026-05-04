package DistributedTaskQueueSystem.demo.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.*;

import org.springframework.boot.micrometer.observation.autoconfigure.ObservationProperties.Http;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,Object>> handleValidErrors(MethodArgumentNotValidException ex){

        Map<String,String> fieldErrors = new HashMap<>();
        for(FieldError error: ex.getBindingResult().getFieldErrors()){
            fieldErrors.put(error.getField(),error.getDefaultMessage());

        }

        Map<String,Object> response = new HashMap<>();
        response.put("timeStamp", LocalDateTime.now());
        response.put("status", 400);
        response.put("error","validation failed");
        response.put("details", fieldErrors);

        return ResponseEntity.badRequest().body(response);
    }
     // Handles job not found and other runtime errors
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String,Object>> handleRuntimeException(RuntimeException ex){
        
        Map<String,Object> response = new HashMap<>();
        response.put("timeStamp", LocalDateTime.now());
        response.put("status", 500);
        response.put("error",ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

     // Catches anything else
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(
            Exception ex) {

        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", LocalDateTime.now());
        response.put("status", 500);
        response.put("error", "Internal server error");
        response.put("message", ex.getMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
