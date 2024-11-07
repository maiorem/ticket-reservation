package com.hhplus.io.support.api;

import com.hhplus.io.common.interfaces.ApiResponse;
import com.hhplus.io.support.domain.error.CoreException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@Slf4j
@ControllerAdvice
public class ApiControllerAdvice{
    @ExceptionHandler(value = CoreException.class)
    public ResponseEntity<?> handle(CoreException e) {
        switch (e.getErrorType().getLogLevel()) {
            case ERROR: log.error("error :: {}", e.getMessage());
            case WARN: log.warn("warn :: {}", e.getMessage());
            default: log.info("info :: {}", e.getMessage());
        }
        HttpStatus status;
        switch (e.getErrorType().getCode()) {
            case DB_ERROR -> status = HttpStatus.INTERNAL_SERVER_ERROR;
            case NOT_FOUND -> status = HttpStatus.NOT_FOUND;
            case CLIENT_ERROR -> status = HttpStatus.BAD_REQUEST;
            case VALIDATION_ERROR -> status = HttpStatus.UNPROCESSABLE_ENTITY;
            case FORBIDDEN, EXPIRED ->  status = HttpStatus.FORBIDDEN;
            default -> status = HttpStatus.OK;
        }
        return ResponseEntity.status(status).body(ApiResponse.exceptionHandler(e));
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<?> handle(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.serverFail(e));
    }

}
