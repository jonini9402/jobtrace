package com.jobtrace.global.exception;

import com.jobtrace.global.dto.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handlerCustomException(CustomException e){
        return ResponseEntity
                .badRequest()
                .body(ErrorResponse.of(e.getErrorCode()));
    }



}
