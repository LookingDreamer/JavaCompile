package com.zzb.ads.util;

import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.common.HttpClientUtil;
import net.sf.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by Administrator on 2017/5/16.
 */
public class AdsUtil {

    /**
     * 根据协议id取出相应的广告信息
     * @param agreements agreements=agreementid,agreementid002
     * @return
     * {"result":{"agreementid":[{"createTime":"2017-05-11 19:14:19","description":"测试1desctiption","endTime":"2017-06-10 19:14:19","id":"a001","name":"测试1",
     * "startTime":"2017-05-11 19:14:19","status":"1","title":"测试1title"},{"createTime":"2017-05-11 19:14:19","description":"测试1desctiption",
     * "endTime":"2017-06-10 19:14:19","id":"a001","name":"测试1","startTime":"2017-05-11 19:14:19","status":"1","title":"测试1title"}],
     * "agreementid002":[]},"message":"111","status":"1"}
     */
    public static JSONObject getAgreementAds (String agreements) {
        JSONObject result = null;
        try {
            ResourceBundle resourceBundle = ResourceBundle.getBundle("config/config");
            String coreAddress= resourceBundle.getString("ads.url");
            if (StringUtil.isEmpty(coreAddress)) {
                return null;
            }
            //coreAddress = "http://10.68.14.139:8080/ads";
            //http://10.68.14.139:8080/ads/sales/getsales?agreements=agreementid,agreementid002
            String path = coreAddress + "/sales/getsales";
            Map<String,String> map = new HashMap<String, String>();
            map.put("agreements", agreements);//代理人工号
            LogUtil.info("请求广告系统 path:" + path + " map :" + JSONObject.fromObject(map));
            String jsonString = HttpClientUtil.doGetAsyncClient(path,map,"utf-8");
            LogUtil.info("请求广告系统 result:" +jsonString);
            JSONObject jsonObject = JSONObject.fromObject(jsonString);
            result = jsonObject.getJSONObject("result");
        } catch (Exception e) {
            result = null;
            LogUtil.error("请求广告系统异常：" + e);
        }
        return result;
    }
}
