package com.hhplus.io.support.domain.error;

import lombok.Getter;
import org.springframework.boot.logging.LogLevel;

@Getter
public enum ErrorType {
    USER_NOT_FOUND(ErrorCode.NOT_FOUND, "사용자를 찾을 수 없습니다.", LogLevel.WARN),
    CONCERT_NOT_FOUND(ErrorCode.NOT_FOUND, "해당 공연을 찾을 수 없습니다.", LogLevel.WARN),
    SEAT_NOT_FOUND(ErrorCode.NOT_FOUND, "해당 좌석을 찾을 수 없습니다.", LogLevel.WARN),
    RESERVATION_NOT_FOUND(ErrorCode.NOT_FOUND, "예약을 찾을 수 없습니다.", LogLevel.WARN),
    DB_ERROR(ErrorCode.DB_ERROR, "DB ERROR", LogLevel.ERROR),
    FORBIDDEN(ErrorCode.FORBIDDEN, "해당 값을 사용할 수 없습니다.", LogLevel.WARN),
    EXPIRE_TEMP_RESERVATION(ErrorCode.EXPIRED, "예약시간이 만료되었습니다.", LogLevel.WARN),;

    private final ErrorCode code;
    private final String message;
    private final LogLevel logLevel;

    ErrorType(ErrorCode code, String message, LogLevel logLevel) {
        this.code = code;
        this.message = message;
        this.logLevel = logLevel;
    }
}
