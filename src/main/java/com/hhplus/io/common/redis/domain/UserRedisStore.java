package com.hhplus.io.common.redis.domain;


import com.hhplus.io.common.support.utils.JsonUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

public record UserRedisStore(String token, Long userId, Long concertId, Long concertDateId, List<Long> seatList) {
    public static UserRedisStore of(String token, Long userId, Long concertId, Long concertDateId, List<Long> seatList) {
        return new UserRedisStore(token, userId, concertId, concertDateId, seatList);
    }

    public static UserRedisStore converFromString(String values) {
        JSONObject jsonObject = new JSONObject(values);
        List<Long> seatList = null;
        try {
            seatList = JsonUtils.jsonStringToList((jsonObject.get("seatList")).toString(), Long.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new UserRedisStore(
                jsonObject.get("token").toString(),
                Long.parseLong(jsonObject.get("userId").toString()),
                Long.parseLong(jsonObject.get("concertId").toString()),
                Long.parseLong(jsonObject.get("concertDateId").toString()),
                seatList
        );
    }
}
