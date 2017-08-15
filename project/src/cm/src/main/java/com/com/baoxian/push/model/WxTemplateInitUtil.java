package com.com.baoxian.push.model;

import com.alibaba.fastjson.JSON;
import com.cninsure.core.utils.LogUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lq on 2016/12/15.
 */
public class WxTemplateInitUtil {

    //推送状态-模板实例
    public static Map<String, Object> templateParams;

    public static Map<String, Object> getTemplateParams() {
        return templateParams;
    }

    public static void setTemplateParams(Map<String, Object> templateParams) {
        WxTemplateInitUtil.templateParams = templateParams;
    }

    static class WxTemplate {
        public Map<String, String> template;

        public Map<String, String> getTemplate() {
            return template;
        }

        public void setTemplate(Map<String, String> template) {
            this.template = template;
        }
    }

    /**
     *
     * @param keyWordKey       关键字名称
     * @return
     * @throws Exception
     */
    public static String getKeyValue(WxTemplate wxTemplate, String keyWordKey, Map wxParaM) throws Exception {
        String value = wxTemplate.getTemplate().get(keyWordKey);
        if (!value.contains("#") && !value.contains("$")) {
            return value;
        } else {
            while (value.contains("#")) {
                String paramKey = StringUtils.substringBetween(value, "#", "#");
                String paramKeyValue = getParam(paramKey, wxParaM);
                value = value.replace("#" + paramKey + "#", paramKeyValue);
            }
        }

        return value;
    }

    private static String getParam(String key, Map wxParaM) throws  Exception{
        if(wxParaM != null && wxParaM.containsKey(key)){
            return (String) wxParaM.get(key);


        }else{
            throw new Exception("[微信消息组包]消息对象：" + wxParaM.get("taskId") + "_" + wxParaM.get("insComName") + "的param中缺少" + key + "参数");

        }

    }

    /**
     * 获取templateId
     * @param templateNamekey
     * @return
     */
    public static String getTemplateId(String templateNamekey) throws Exception {
        String templateId = null;
        WxTemplate wxTemplate = (WxTemplate) WxTemplateInitUtil.templateParams.get(templateNamekey);
        for(String key : wxTemplate.getTemplate().keySet()){
            if(key.equals("templateId")){
                templateId = wxTemplate.getTemplate().get(key);
            }
        }

        if(templateId == null){
            throw new Exception("[微信消息组包]没有对应：" + templateNamekey + "的templateId！");
        }else{
            return templateId;
        }
    }

    /**
     * 微信data组包
     * @param templateNamekey
     * @return
     */
    public static Map packWxData(String templateNamekey, Map wxParaM) throws Exception {
        LogUtil.info("[微信消息组包]消息对象：" + wxParaM.get("taskId") + "_" + wxParaM.get("insComName") + "开始组包");
        Map<String, Object> result = new HashMap<>();
        WxTemplate wxTemplate = (WxTemplate) WxTemplateInitUtil.templateParams.get(templateNamekey);
        for(String key : wxTemplate.getTemplate().keySet()){
            if(key.equals("templateId")){
                continue;
            }
            Map<String, String> keyWordMap = new HashMap<>();
            keyWordMap.put("value", getKeyValue(wxTemplate, key, wxParaM));
            keyWordMap.put("color", "#173177");
            result.put(key, keyWordMap);
        }

        if(result.size() <= 0){
            throw new Exception("[微信消息组包]消息对象：" + wxParaM.get("taskId") + "_" + wxParaM.get("insComName") + "无法组成信息包！");
        }else{
            LogUtil.info("[微信消息组包]消息对象：" + wxParaM.get("taskId") + "_" + wxParaM.get("insComName") + "组包完成：" + JSON.toJSONString(result));
            return result;
        }
    }

}