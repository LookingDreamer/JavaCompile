package com.zzb.warranty.redis;

import com.common.JsonUtil;
import com.common.redis.CMRedisClient;

/**
 * Created by Administrator on 2017/2/10.
 */
public class WarrantyRedisClient extends CMRedisClient {
    private static final String MODULE = "com.zzb.warranty";

    public boolean set(String key, String value) {
        return super.set(MODULE, key, value);
    }

    @Override
    public boolean set(String module, String key, Object obj) {
        if (Number.class.isAssignableFrom(obj.getClass())) {
            return this.set(module, key, obj.toString());
        }
        return this.set(module, key, JsonUtil.serialize(obj));
    }

    @Override
    public Object get(String module, String key) {
        return super.get(module, key);
    }
}
