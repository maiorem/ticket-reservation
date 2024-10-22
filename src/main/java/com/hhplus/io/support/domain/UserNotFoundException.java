package com.hhplus.io.support.domain;

public class UserNotFoundException extends RuntimeException implements ExceptionIf {

    private String statusMessage;

    public UserNotFoundException(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    @Override
    public Integer getStatusCode() {
        return ErrorCode.NOT_FOUND.getStatusCode();
    }

    @Override
    public String getStatusMessage() {
        return statusMessage;
    }
}
