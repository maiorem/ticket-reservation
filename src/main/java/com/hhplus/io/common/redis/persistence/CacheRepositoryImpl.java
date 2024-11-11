package com.hhplus.io.common.redis.persistence;

import com.hhplus.io.common.redis.domain.repository.CacheRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import javax.cache.expiry.Duration;

@Repository
public class CacheRepositoryImpl implements CacheRepository {

    private final RedisTemplate<String, String> redisTemplate;

    public CacheRepositoryImpl(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void setValues(String key, String data) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        values.set(key, data);
    }

    @Override
    public void setValues(String key, String data, Duration duration) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        values.set(key, data, duration.getDurationAmount());
    }

    @Override
    public String getValues(String key) {
        ValueOperations<String, String> values = redisTemplate.opsForValue();
        return values.get(key);
    }

    @Override
    public void initCache(String key) {
        redisTemplate.delete(key);
    }
}
