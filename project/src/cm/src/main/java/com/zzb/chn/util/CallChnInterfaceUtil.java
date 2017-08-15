package com.zzb.chn.util;

import com.common.HttpSender;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/3/24.
 */
public class CallChnInterfaceUtil {

    public static String callInterface(String preInterface,String interfaceName,String json,String channelId) throws Exception{
        Map<String, String> httpHead = new HashMap<>();
        httpHead.put("innerPipe", "zheshiyigemimi!");
        httpHead.put("Content-Type", "application/json;charset=utf-8");
        httpHead.put("channelId", channelId);
        String sign = HttpSender.doPost(preInterface + "/channel/getSignForInner", json, httpHead, "UTF-8");
        httpHead.put("sign", sign);
        String res  = HttpSender.doPost(preInterface + "/channel/"+ interfaceName, json, httpHead, "UTF-8");
        return res;
    }

}
