package com.ming.util;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;
/**
 * 分布式锁
 * Created By Ranger on 2022/7/14.
 */
@Component
public class RedisLockUtil {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final byte[] SCRIPT_RELEASE_LOCK = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end".getBytes();


    /**
     * 分布式锁
     *
     * @param key        分布式锁key
     * @param expireTime 持有锁的最长时间 (redis过期时间) 秒为单位
     * @return 返回获取锁状态 成功失败 false 加锁成功获取到锁
     */
    public boolean tryLock(String key, int expireTime) {
        final JSONObject lock = new JSONObject();
        lock.put("id", key);
        // startTime
        lock.put("st", System.currentTimeMillis());
        // keepSeconds
        lock.put("ks", expireTime);
        return tryLock(key, "", expireTime);
    }

    /**
     * 释放锁
     * @param key
     */
    public void unLock(String key) {
        releaseLock(key, "");
    }


    /**
     * 尝试获取分布式锁
     *
     * @param key       键
     * @param requestId 请求ID
     * @param expire    锁的有效时间（秒）
     */

    public synchronized Boolean tryLock(String key, String requestId, long expire) {
        return redisTemplate.execute((RedisCallback<Boolean>) redisConnection -> redisConnection.set(key.getBytes(), requestId.getBytes(), Expiration.from(expire, TimeUnit.SECONDS), RedisStringCommands.SetOption.SET_IF_ABSENT));
    }

    /**
     * 释放分布式锁
     *
     * @param key       键
     * @param requestId 请求ID
     */
    public synchronized Boolean releaseLock(String key, String requestId) {
        return redisTemplate.execute((RedisCallback<Boolean>) redisConnection -> redisConnection.eval(SCRIPT_RELEASE_LOCK, ReturnType.BOOLEAN, 1, key.getBytes(), requestId.getBytes()));
    }
}
