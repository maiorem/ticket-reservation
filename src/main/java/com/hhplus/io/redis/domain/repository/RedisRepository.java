package com.hhplus.io.redis.domain.repository;

import org.springframework.stereotype.Repository;

import javax.cache.expiry.Duration;

@Repository
public interface RedisRepository {

    void setValues(String key, String data);
    void setValues(String key, String data, Duration duration);
    String getValues(String key);
    void initCache(String key);
}
