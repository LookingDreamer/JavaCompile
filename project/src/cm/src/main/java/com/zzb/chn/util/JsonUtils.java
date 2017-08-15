package com.zzb.chn.util;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

/**
 * Created by Administrator on 2016/3/3.
 */
public class JsonUtils {
    private static final ObjectMapper mapper;

    static {
        mapper = new ObjectMapper();
        mapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(SerializationConfig.Feature.WRITE_NULL_PROPERTIES, false);
    }
/*
    public static JsonElement getElementByPath(JsonObject json, String path) {
        JsonElement current = json;
        for (String part : Splitter.on(".").split(path)) {
            if (!current.isJsonObject()) {
                throw new IllegalArgumentException("invalid path:" + path);
            }
            current = ((JsonObject) current).get(part);
        }
        return current;
    }*/

    public static ObjectMapper getObjectMapper() {
        return mapper;
    }

    public static String serialize(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T deserialize(String json, Class<T> cls) {
        try {
            return mapper.readValue(json, cls);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
