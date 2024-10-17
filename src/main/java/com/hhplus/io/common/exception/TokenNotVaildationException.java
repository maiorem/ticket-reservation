package com.hhplus.io.common.exception;

import com.hhplus.io.common.response.ErrorCode;

public class TokenNotVaildationException  extends RuntimeException implements ExceptionIf{
    private final String statusMessage;

    public TokenNotVaildationException(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    @Override
    public Integer getStatusCode() {
        return ErrorCode.TOKEN_INVALID.getStatusCode();
    }

    @Override
    public String getStatusMessage() {
        return statusMessage;
    }
}
