package com.zzb.extra.util;

import com.cninsure.core.utils.StringUtil;
import com.common.HttpClientUtil;
import com.zzb.extra.util.wxmsgtemplate.WxTemplate;
import net.sf.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Heweicheng on 2016/6/29.
 */
public class WxMsgTemplateUtils {
    /**
     * 微信发送模板消息API
     */
    public static String sendMsg(WxTemplate wxTemplate,String access_token){
        if(StringUtil.isEmpty(wxTemplate.getTouser())||StringUtil.isEmpty(wxTemplate.getTemplate_id())||StringUtil.isEmpty(access_token)){
            return ParamUtils.resultMap(false, "参数错误，touser、template_id和access_token不能为空!");
        }
        String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="+access_token;
        String jsonString = JSONObject.fromObject(wxTemplate).toString();
        return HttpClientUtil.doPostJsonString(url,jsonString);
    }
    /**
     * 微信发送模板消息API
     */
    public static String sendMsg(String jsonString,String access_token){
        JSONObject jsonObj = JSONObject.fromObject(jsonString);
        String touser = "";
        if (jsonObj.has("touser")) {
             touser = jsonObj.getString("touser");
        }
        String template_id = "";
        if (jsonObj.has("template_id")) {
            template_id = jsonObj.getString("template_id");
        }
        if(StringUtil.isEmpty(touser)||StringUtil.isEmpty(template_id)||StringUtil.isEmpty(access_token)){
            return ParamUtils.resultMap(false, "参数错误，touser、template_id和access_token不能为空!");
        }
        String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="+access_token;
        return HttpClientUtil.doPostJsonString(url,jsonString);
    }
    /**
     * 获取所有模板信息
     */
    public static String queryTemplateList(String access_token){
        if(StringUtil.isEmpty(access_token)){
            return ParamUtils.resultMap(false, "参数错误，access_token不能为空！");
        }
        String url = "https://api.weixin.qq.com/cgi-bin/template/get_all_private_template?access_token="+access_token;
        Map<String,String> param = new HashMap<String,String>();
        return HttpClientUtil.doGet(url,param);
    }
    /**
     * 根据微信模板库的ID获取模板ID
     */
    public static String queryTemplateID(String template_id_short,String access_token){
        if(StringUtil.isEmpty(access_token)||StringUtil.isEmpty(template_id_short)){
            return ParamUtils.resultMap(false, "参数错误，template_id_short和access_token不能为空！");
        }
        String url = "https://api.weixin.qq.com/cgi-bin/template/api_add_template?access_token="+access_token;
        Map<String,String> param = new HashMap<String,String>();
        param.put("template_id_short",template_id_short);
        return HttpClientUtil.doPostJsonString(url, JSONObject.fromObject(param).toString());
    }

}
