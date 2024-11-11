package com.hhplus.io.common.interfaces;

import com.hhplus.io.common.support.domain.error.CoreException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Getter
public class ApiResponse<T> {

    private final ApiResponseHeader header;
    private final Map<String, T> body;

    public ApiResponse(ApiResponseHeader header, Map<String, T> body) {
        this.header = header;
        this.body = body;
    }

    private final static int SUCCESS = 200;
    private final static int FAILED = 500;
    private final static String SUCCESS_MESSAGE = "해당 요청이 정상적으로 완료되었습니다";
    private final static String FAILED_MESSAGE = "서버에서 오류가 발생하였습니다.";

    public static <T> ApiResponse<T> success(String name, T body) {
        Map<String, T> map = new HashMap<>();
        map.put(name, body);

        return new ApiResponse<>(new ApiResponseHeader(SUCCESS, SUCCESS_MESSAGE), map);
    }

    public static <T> ApiResponse<T> exceptionHandler(CoreException exception) {
        Map<String, String> map = new HashMap<>();
        log.error("에러 발생", exception);
        map.put("서버 오류 메세지", "서버 로그 요청 필요");
        return new ApiResponse(new ApiResponseHeader(FAILED, FAILED_MESSAGE), map);
    }

    public static <T> ApiResponse<T> serverFail(Exception exception) {
        Map<String, String> map = new HashMap<>();
        log.error("서버 에러 발생", exception);
        return new ApiResponse(new ApiResponseHeader(FAILED, FAILED_MESSAGE), map);
    }

}
