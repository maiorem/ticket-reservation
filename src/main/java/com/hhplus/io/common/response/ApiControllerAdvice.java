package com.hhplus.io.common.response;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class ApiControllerAdvice{
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity serverExceptionInvoke(Exception exception) {
        return ResponseEntity.status(HttpStatusCode.valueOf(500)).body(ApiResponse.serverFail(exception));
    }

}
