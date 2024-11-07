package com.hhplus.io.usertoken.persistence;

import com.hhplus.io.redis.domain.repository.RedisRepository;
import com.hhplus.io.usertoken.domain.repository.WaitingQueueRepository;
import com.hhplus.io.usertoken.domain.entity.WaitingQueueStatus;
import com.hhplus.io.usertoken.domain.entity.WaitingQueue;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.annotation.PostConstruct;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.hhplus.io.common.constants.Constants.CacheText.*;
import static com.hhplus.io.usertoken.domain.entity.QWaitingQueue.waitingQueue;

@Repository
public class WaitingQueueRepositoryImpl implements WaitingQueueRepository {

    private final WaitingQueueJpaRepository waitingQueueJpaRepository;
    private final JPAQueryFactory jpaQueryFactory;
    private final RedisRepository redisRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private ZSetOperations<String, String> zsetOperations;
    private SetOperations<String, String> setOperations;

    public WaitingQueueRepositoryImpl(WaitingQueueJpaRepository waitingQueueJpaRepository, JPAQueryFactory jpaQueryFactory, RedisRepository redisRepository, RedisTemplate<String, String> redisTemplate) {
        this.waitingQueueJpaRepository = waitingQueueJpaRepository;
        this.jpaQueryFactory = jpaQueryFactory;
        this.redisRepository = redisRepository;
        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    public void init() {
        zsetOperations = redisTemplate.opsForZSet();
        setOperations = redisTemplate.opsForSet();
    }

    @Override
    public WaitingQueue getQueueByUserAndStatus(Long userId, WaitingQueueStatus status) {
        Optional<WaitingQueue> optionalWaitingQueue = waitingQueueJpaRepository.findByUserIdAndStatus(userId, String.valueOf(status));
        return optionalWaitingQueue.orElse(null);
    }

    @Override
    public List<WaitingQueue> getAllQueueByStatus(WaitingQueueStatus status) {
        return waitingQueueJpaRepository.findAllByStatus(String.valueOf(status));
    }

    @Override
    public WaitingQueue generateQueue(WaitingQueue builder) {
        return waitingQueueJpaRepository.save(builder);
    }


    @Override
    public Long countByQueueStatus(WaitingQueueStatus status) {
        return waitingQueueJpaRepository.countByStatus(String.valueOf(status));
    }


    @Override
    public List<WaitingQueue> getAllQueueByStatusLimitUpdateCount(WaitingQueueStatus queueStatus, long updateProcess) {
        return jpaQueryFactory
                .selectFrom(waitingQueue)
                .where(waitingQueue.status.eq(String.valueOf(queueStatus)))
                .limit(updateProcess)
                .fetch();
    }

    @Override
    public String createWaitingQueue(Long userId) {
        long sequence = System.currentTimeMillis();
        String token = UUID.randomUUID().toString();
        Boolean isCreate = zsetOperations.add(WAITING_QUEUE_KEY_PREFIX + userId, token, sequence);
        return Boolean.TRUE.equals(isCreate) ? token : null;
    }


    @Override
    public String getWaitingQueue(Long userId) {
        return redisRepository.getValues(WAITING_QUEUE_KEY_PREFIX+userId);
    }

    @Override
    public Long getWatingQueueSequence(Long userId) {
        String token = redisRepository.getValues(WAITING_QUEUE_KEY_PREFIX + userId);
        return zsetOperations.rank(WAITING_QUEUE_KEY_PREFIX+userId, token);
    }


    @Override
    public List<String> getWaitingQueueList(Long range) {
        List<String> list = new ArrayList<>();
        Set<ZSetOperations.TypedTuple<String>> typedTuples = zsetOperations.popMin(Objects.requireNonNull(redisTemplate.keys(WAITING_QUEUE_KEY_PREFIX + "*")).toString(), range);
        if (typedTuples != null) {
            for (ZSetOperations.TypedTuple<String> typedTuple : typedTuples) {
                list.add(typedTuple.getValue());
            }
        }
        return list.isEmpty() ? null : list;
    }

    @Override
    public void deleteWaitingQueue(Long userId, String token) {
        String key = WAITING_QUEUE_KEY_PREFIX + userId;
        setOperations.remove(key, token);
    }

    @Override
    public void createActiveQueue(String token) {
        String key = ACTIVE_QUEUE_KEY_PREFIX + token;
        setOperations.add(key, token);
        setOperations.getOperations().expire(key, 30, TimeUnit.MINUTES);
    }

    @Override
    public Boolean isActive(String token){
        String key = ACTIVE_QUEUE_KEY_PREFIX + token;
        return Objects.requireNonNull(setOperations.members(key)).isEmpty();
    }

    public void refreshTimeout(String token) {
        String key = ACTIVE_QUEUE_KEY_PREFIX + token;
        setOperations.getOperations().expire(key, 5, TimeUnit.MINUTES);
    }

    @Override
    public void deleteActiveToken(String token) {
        String key = ACTIVE_QUEUE_KEY_PREFIX + token;
        setOperations.remove(key, token);
    }

    @Override
    public Long getQueueCount(String key) {
        return (long) Objects.requireNonNull(redisTemplate.keys(key + "*")).size();
    }

    @Override
    public void activateAll(List<String> tokenList) {
        redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            tokenList.forEach(token -> {
                String key = ACTIVE_QUEUE_KEY_PREFIX + token;
                connection.setCommands().sAdd(key.getBytes(), token.getBytes());
                connection.commands().expire(key.getBytes(), 300);
            });
            return null;
        });
    }

}
