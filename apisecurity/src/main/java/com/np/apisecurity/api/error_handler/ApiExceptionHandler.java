package com.np.apisecurity.api.error_handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;

@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler {

    private static final ApiError GENERIC_API_ERROR = new ApiError("Sorry, an expected error occurred :-(");

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<ApiError> handleSQLException(SQLException e) {
        log.error("An exception occurred: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(GENERIC_API_ERROR);
    }
}
