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
            case ERROR: log.error("");
            case WARN: log.warn("");
            default: log.info("");
        }
        switch (e.getErrorType().getCode()) {
            case DB_ERROR -> HttpStatus.INTERNAL_SERVER_ERROR.value();
            case NOT_FOUND -> HttpStatus.NOT_FOUND.value();
            case CLIENT_ERROR -> HttpStatus.BAD_REQUEST.value();
            case VALIDATION_ERROR -> HttpStatus.UNPROCESSABLE_ENTITY.value();
            default -> HttpStatus.OK.value();
        }
        return ResponseEntity.status(HttpStatusCode.valueOf(500)).body(ApiResponse.serverFail(e));
    }

}
