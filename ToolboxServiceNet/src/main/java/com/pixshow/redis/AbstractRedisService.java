package com.pixshow.redis;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;


public abstract class AbstractRedisService<K, V> {
    @Autowired
    public RedisTemplate<K, V> redisTemplate;

    protected RedisTemplate<K, V> getRedisTemplate() {
        return redisTemplate;
    }

    protected void setRedisTemplate(RedisTemplate<K, V> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    protected RedisSerializer<String> getRedisSerializer() {
        return redisTemplate.getStringSerializer();
    }

    public void set(final K key, final V value) {
        set(key, value, 0);
    }

    public void set(final K key, final V value, final long expiredTime) {
        BoundValueOperations<K, V> valueOper = redisTemplate.boundValueOps(key);
        if (expiredTime <= 0) {
            valueOper.set(value);
        } else {
            valueOper.set(value, expiredTime, TimeUnit.MILLISECONDS);
        }
    }

    public V get(final K key) {
        BoundValueOperations<K, V> valueOper = redisTemplate.boundValueOps(key);
        return valueOper.get();
    }
    
    public void del(K key) {
        if (redisTemplate.hasKey(key)) {
            redisTemplate.delete(key);
        }
    }

    public Boolean check(K key) {
        Boolean flag = false;
        if (redisTemplate.hasKey(key)) {
            flag = true;
        }
        return flag;
    }

    public Boolean check(K key, V value) {
        Boolean flag = false;
        if (redisTemplate.hasKey(key)) {
            if (value.equals(get(key))) {
                flag = true;
            }
        }
        return flag;
    }

}