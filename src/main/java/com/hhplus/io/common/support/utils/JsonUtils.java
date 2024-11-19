package com.hhplus.io.common.support.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JsonUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * JSON 문자열을 객체로 변환
     *
     * @param jsonString 변환할 JSON 문자열
     * @param clazz      변환할 객체의 클래스 타입
     * @param <T>        변환할 객체의 타입
     * @return JSON 문자열을 변환한 객체
     * @throws IOException JSON 처리를 위한 IO 예외
     */
    public static <T> T fromJsonString(String jsonString, Class<T> clazz)  {
        try {
            return objectMapper.readValue(jsonString, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 객체를 JSON 문자열로 변환
     *
     * @param object 변환할 객체
     * @param <T>    변환할 객체의 타입
     * @return 객체를 변환한 JSON 문자열
     * @throws JsonProcessingException JSON 처리 예외
     */
    public static <T> String toJsonString(T object)  {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Map을 JSONString으로 변환
     *
     * @param map 변환할 Map 객체
     * @return Map을 변환한 JSON 문자열
     * @throws JsonProcessingException JSON 처리 예외
     */
    public static String mapToJsonString(Map<String, Object> map){
        try {
            return objectMapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * List<Map>을 JSONString으로 변환
     *
     * @param list 변환할 List<Map> 객체
     * @return List<Map>을 변환한 JSON 문자열
     * @throws JsonProcessingException JSON 처리 예외
     */
    public static String listOfMapToJsonString(List<Map<String, Object>> list)
            throws JsonProcessingException {
        return objectMapper.writeValueAsString(list);
    }

    /**
     * JSON 문자열을 Map으로 변환
     *
     * @param jsonString 변환할 JSON 문자열
     * @return JSON 문자열을 변환한 Map 객체
     * @throws IOException JSON 처리를 위한 IO 예외
     */
    public static Map<String, Object> jsonStringToMap(String jsonString) {
        try {
            return objectMapper.readValue(jsonString, new TypeReference<Map<String, Object>>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * JSON 문자열을 List로 변환
     *
     * @param jsonString 변환할 JSON 문자열
     * @param clazz      변환할 객체의 클래스 타입
     * @param <T>        변환할 객체의 타입
     * @return JSON 문자열을 변환한 List<T> 객체
     * @throws IOException JSON 처리를 위한 IO 예외
     */
    public static <T> List<T> jsonStringToList(String jsonString, Class<T> clazz) {
        try {
            return objectMapper.readValue(
                    jsonString,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, clazz));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * JSONObject를 Map<String, String>으로 변환
     *
     * @param jsonObject 변환할 Map<String, Object> 객체
     * @return Map<String, Object>을 변환한 Map<String, String> 객체
     */
    public static Map<String, String> jsonObjectToMap(Map<String, Object> jsonObject) {
        return jsonObject.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey, entry -> entry.getValue().toString()
                ));
    }

    /**
     * JSON 배열을 List<Map<String, String>>으로 변환
     *
     * @param jsonArrayString 변환할 JSON 배열 문자열
     * @return JSON 배열 문자열을 변환한 List<Map<String, String>> 객체
     * @throws IOException JSON 처리를 위한 IO 예외
     */
    public static List<Map<String, String>> jsonArrayToListOfMap(String jsonArrayString)
            throws IOException {
        return objectMapper.readValue(
                jsonArrayString,
                TypeFactory.defaultInstance().constructCollectionType(List.class,
                        TypeFactory.defaultInstance().constructMapType(Map.class, String.class, String.class))
        );
    }
}
