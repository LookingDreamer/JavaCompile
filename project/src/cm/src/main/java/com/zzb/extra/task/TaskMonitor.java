package com.zzb.extra.task;


import net.sf.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Heweicheng on 2016/6/14.
 */
public class TaskMonitor {
    public static final Map<String,Object> attr = new HashMap<String,Object>();
    public static int counts = 0;
    public static void addValue(String key,Object value){
        try {
            attr.put(key, value);
            counts++;
            if (counts == 200) {
                counts = 0;
                clearMap();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public static void clearMap(){
        attr.clear();
    }
    public static String getValues(){
        return JSONObject.fromObject(attr).toString();
    }
}
