package com.hhplus.io.common.exception;

import com.hhplus.io.common.response.ErrorCode;

public class TokenExpireException extends RuntimeException implements ExceptionIf{
    private final String statusMessage;

    public TokenExpireException(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    @Override
    public Integer getStatusCode() {
        return ErrorCode.TOKEN_EXPIRED.getStatusCode();
    }

    @Override
    public String getStatusMessage() {
        return statusMessage;
    }
}
