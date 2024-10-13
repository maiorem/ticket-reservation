package com.hhplus.io.common.response;

import lombok.Getter;

@Getter
public enum ErrorCode {

    NOT_FOUND(404, "해당 데이터를 찾을 수 없습니다."),
    DUPLICATE(700, "해당 데이터가 중복됩니다."),
    NOT_EXISTS(805, "데이터가 존재하지 않습니다. 업데이트 화면으로 이동합니다."),
    NOT_COINCIDE(900, "일치하지 않습니다."),
    FAILED(500, "서버에서 오류가 발생하였습니다.");

    private Integer statusCode;
    private String statusMessage;

    ErrorCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    ErrorCode(Integer statusCode, String statusMessage) {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
    }

}
