package com.zzb.extra.util;

import com.zzb.chn.util.JsonUtils;
import net.sf.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by MagicYuan on 2016/5/13.
 */
public class ParamUtils {

    /**
     * 返回操作提示结果
     *
     * @param success
     * @param msg
     * @return
     */
    public static String resultMap(Boolean success, String msg) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("success", success);
        resultMap.put("result", msg);
        JSONObject jsonObject = JSONObject.fromObject(resultMap);
        return jsonObject.toString();
    }

    /**
     * 返回自定义结果
     *
     * @param map
     * @return
     */
    public static String resultMap(Boolean success, Map<String, Object> map) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("success", success);
        resultMap.put("result", map);
        JSONObject jsonObject = JSONObject.fromObject(resultMap);
        return jsonObject.toString();
    }

    /**
     * 返回自定义结果
     *
     * @param map
     * @return
     */
    public static String resultMapByJackson(Boolean success, Map<String, Object> map) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("success", success);
        resultMap.put("result", map);
        return JsonUtils.serialize(resultMap);
    }

    /**
     * 返回列表查询结果
     *
     * @param total
     * @param rows
     * @return
     */
    public static String resultMap(long total, List<Map<Object, Object>> rows) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("success", true);
        resultMap.put("total", total);
        resultMap.put("rows", rows);
        JSONObject jsonObject = JSONObject.fromObject(resultMap);
        return jsonObject.toString();
    }

}
