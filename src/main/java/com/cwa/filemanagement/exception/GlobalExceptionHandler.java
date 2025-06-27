package com.cwa.filemanagement.exception;

import com.cwa.filemanagement.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.net.MalformedURLException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(FileStorageException.class)
    public ResponseEntity<ApiResponse> handleFileStorageException(FileStorageException ex) {
        return ResponseEntity.badRequest().body(new ApiResponse(false, ex.getMessage()));
    }

    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<ApiResponse> handleFileNotFoundException(FileNotFoundException ex) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(MalformedURLException.class)
    public ResponseEntity<ApiResponse> handleMalformedURLException(MalformedURLException ex) {
        return ResponseEntity.badRequest().body(new ApiResponse(false, "File is too large!"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleException(Exception ex) {
        return ResponseEntity.internalServerError().body(new ApiResponse(false, "Unknown error occurred!"));
    }
}
