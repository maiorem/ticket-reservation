package com.hhplus.io.common.cache.domain;


import com.hhplus.io.common.support.utils.JsonUtils;
import org.json.JSONObject;

import java.util.List;

public record UserRedisStore(String token, Long userId, Long concertId, Long concertDateId, List<Long> seatList) {
    public static UserRedisStore of(String token, Long userId, Long concertId, Long concertDateId, List<Long> seatList) {
        return new UserRedisStore(token, userId, concertId, concertDateId, seatList);
    }

    public static UserRedisStore converFromString(String values) {
        JSONObject jsonObject = new JSONObject(values);
        List<Long> seatList = JsonUtils.jsonStringToList((jsonObject.get("seatList")).toString(), Long.class);
        return new UserRedisStore(
                jsonObject.get("token").toString(),
                Long.parseLong(jsonObject.get("userId").toString()),
                Long.parseLong(jsonObject.get("concertId").toString()),
                Long.parseLong(jsonObject.get("concertDateId").toString()),
                seatList
        );
    }
}
