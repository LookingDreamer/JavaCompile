package com.cninsure.jobpool;

import com.common.redis.CMRedisClient;
import com.common.redis.IRedisClient;

import java.util.List;

/**
 * author: wz
 * date: 2017/3/27.
 */
public class FairyInsureQueryQueue {
    private static final IRedisClient redisClient;
    private static final Integer MAX_CAPACITY = 5000;
    private static final Integer DEFAULT_TIMEOUT = 5;

    static {
        redisClient = CMRedisClient.getInstance();
    }

    private static final String MODULE = "com:zzb:cm:";
    private static final String KEY = "fairyinsurequerytaskqueue";

    public static long size() {
        return redisClient.llen(MODULE, KEY);
    }

    public static boolean isEmpty() {
        return redisClient.llen(MODULE, KEY) == 0;
    }

    public static long push(String... values) {
        if (size() >= MAX_CAPACITY) {
            throw new RuntimeException("InsureQueryQueue超出最大容量");
        }
        return redisClient.lpush(MODULE, KEY, values);
    }

    public static long remove(String value) {
        return redisClient.lrem(MODULE, KEY, 0, value);
    }

    public static void removeAll(String... values) {
        for (String value : values) {
            remove(value);
        }
    }

    public static void clear() {
        redisClient.del(MODULE, KEY);
    }

    public static String poll(int timeout) {
        List<String> list = redisClient.brpop(MODULE, KEY, timeout);
        if (list != null && list.size() > 0) {
            return list.get(1);
        }
        return null;
    }

    public static String poll() {
        return poll(DEFAULT_TIMEOUT);
    }
}
