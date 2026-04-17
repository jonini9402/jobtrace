package com.jobtrace.global.exception;

import com.jobtrace.global.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handlerCustomException(CustomException e){

        ErrorCode errorCode = e.getErrorCode();
        return ResponseEntity
                .status(getHttpStatus(errorCode))
                .body(ErrorResponse.of(errorCode));
    }

    private HttpStatus getHttpStatus(ErrorCode errorCode) {
        return switch(errorCode) {
            case DUPLICATE_EMAIL -> HttpStatus.CONFLICT; //409
            case USER_NOT_FOUND, POST_NOT_FOUND -> HttpStatus.NOT_FOUND; //404
            case INVALID_PASSWORD -> HttpStatus.BAD_REQUEST; //400
            case TOKEN_NOT_FOUND,UNAUTHORIZED -> HttpStatus.UNAUTHORIZED; //401
        };
    }




}
