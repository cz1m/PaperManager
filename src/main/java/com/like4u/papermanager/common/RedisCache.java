package com.like4u.papermanager.common;


import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author Zhang Min
 * @version 1.0
 * @date 2023/7/5 11:10
 */
@SuppressWarnings(value = { "unchecked", "rawtypes" })
@Component
public class RedisCache {

public final RedisTemplate redisTemplate;

    public RedisCache(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /***
     * 向redis输入对象或者基本数据类型
     * @param key 键值
     * @param value 对象
     * @param <T> 返回值类型
     */
    public <T> void setCacheObject(final String key, final T value)
    {
        redisTemplate.opsForValue().set(key, value);
    }

    /***
     * 从redis中获取值
     * @param key 键
     * @return 获取到的value
     * @param <T> value的类型
     */
    public <T> T  getCacheObject(final String key){
        ValueOperations<String,T> valueOperations = redisTemplate.opsForValue();
        return valueOperations.get(key);
    }

    /***
     *
     * @param key 要获取的数据的key
     * @param value 获取的数据
     * @param time 过期时间
     * @param timeUnit 时间单位
     * @param <T> 获取的数据类型
     */

    public <T> void setCacheObjectAndTTL(String key, T value, Integer time, TimeUnit timeUnit){
        redisTemplate.opsForValue().set(key,value,time,timeUnit);
    }


    public void deleteObject(String userKey) {

        redisTemplate.delete(userKey);
    }
}
