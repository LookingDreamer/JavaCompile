
package com.common.redis;

import java.util.List;
import java.util.Set;

/**
 * Created by @Author Chen Haoming on 2016/6/20.
 */
public interface IRedisClient {

    boolean set(String module, String key, Object obj);

    boolean set(String module, String key, String value);
    
    boolean set(int dbindex,String module, String key, String value);

    boolean set(int dbindex,String module, String key, String value, int seconds);

    boolean set(int dbindex,String module, String key, Object obj, int seconds);

    boolean set(String module, String key, Object obj, int seconds);
    
    boolean set(String module, String key, String value, int seconds);

    boolean del(String module, String key);

    boolean del(int dbindex, String module, String key);

    Object get(int dbindex, String module, String key);

    <T> T get(int dbindex, String module, String key, Class<T> clazz);

    Object get(String module, String key);

    <T> T get(String module, String key, Class<T> clazz);

    <T> boolean setList(String module, String key, List<T> value);

    <T> boolean setList(String module, String key, List<T> value, int seconds);

    <T> List<T> getList(String module, String key, Class<T> clazz);

    int addOne(String module, String key);
    
    int minusOne(String module, String key);

    int atomicIncr(String module, String rdKey);

    void expire(String module, String rdKey, int second);

    String rename(String oldKey, String newKey);

    Set<String> zrevrange(String module, String key, long end);

    Set<String> zrevrange(String module, String key, long start, long end);

    Set<String> keys(int dbindex, String pattern);

    Set<String> keys(String pattern);

	Set<String> getKeys(String module, String rdKey);

    Long lpush(String module, String key, String... value);

    List<String> brpop(String module, String key, int timeout);

    Long lrem(String module, String key, Integer count, String value);

    List<String> lrange(String module, String key, long start, long end);

    Long llen(String module, String key);
}
