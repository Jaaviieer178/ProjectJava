package com.api.java.globalException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(ResponseStatusException e){
        ErrorResponse error = new ErrorResponse(e.getReason(), e.getStatusCode().value());
        return ResponseEntity.status(e.getStatusCode()).body(error);
    }

}
