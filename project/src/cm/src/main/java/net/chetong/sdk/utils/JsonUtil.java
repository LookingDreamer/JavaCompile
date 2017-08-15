package net.chetong.sdk.utils;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;

public class JsonUtil {

	private static final SerializeConfig config;
	static {
		config = new SerializeConfig();
		config.put(java.util.Date.class, new SimpleDateFormatSerializer("yyyy/MM/dd HH:mm:ss"));
		config.put(java.sql.Date.class, new SimpleDateFormatSerializer("yyyy/MM/dd HH:mm:ss"));
		config.put(java.sql.Timestamp.class, new SimpleDateFormatSerializer("yyyy/MM/dd HH:mm:ss.SSS"));
		JSON.DEFFAULT_DATE_FORMAT = "yyyy/MM/dd HH:mm:ss";
		
        int features = 0;
        features |= Feature.AutoCloseSource.getMask();
		features |= Feature.InternFieldNames.getMask();
		features |= Feature.AllowUnQuotedFieldNames.getMask();
		features |= Feature.AllowSingleQuotes.getMask();
		features |= Feature.AllowArbitraryCommas.getMask();
		
        features |= Feature.DisableCircularReferenceDetect.getMask();
        features |= Feature.IgnoreNotMatch.getMask();
        JSON.DEFAULT_PARSER_FEATURE = features;
	}

	private static final SerializerFeature[] features = { 
			SerializerFeature.WriteMapNullValue, 
			SerializerFeature.WriteNullListAsEmpty, 
			SerializerFeature.WriteNullNumberAsZero,
			SerializerFeature.WriteNullBooleanAsFalse,
			SerializerFeature.WriteNullStringAsEmpty, 
			SerializerFeature.DisableCircularReferenceDetect
	};
	public static String toJSONString(Object object) {
		return JSON.toJSONString(object, config, features);
	}
	public static <T> List<T> toList(String s, Class<T> clz){
		return JSON.parseArray(s, clz);
	}
	public static <T> T toBean(String s, Class<T> clz){
		return JSON.parseObject(s, clz);
	}
	public static Object parse(String txt) {
		return JSON.parse(txt);
	}
	public static JSONObject parseObject(String txt) {
		return JSON.parseObject(txt);
	}
	public static JSONObject parseObject(Object obj){
		return JSON.parseObject(toJSONString(obj));
	}
	public static JSONArray parseArray(String txt) {
		return JSON.parseArray(txt);
	}

}
