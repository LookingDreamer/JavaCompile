package com.zzb.extra.util.wxmsgtemplate;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by Heweicheng on 2016/6/29.
 * 微信消息模板MODEL
 */
public class WxTemplate implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 模板消息id
     */
    private String template_id;
    /**
     * 用户openId
     */
    private String touser;
    /**
     * URL置空，则在发送后，点击模板消息会进入一个空白页面（ios），或无法点击（android）
     */
    private String url;
    /**
     * 模板顶端颜色
     */
    private String topcolor;
    /**
     * 详细内容
     */
    private Map<String, TemplateData> data;

    public String getTemplate_id() {
        return template_id;
    }
    public void setTemplate_id(String template_id) {
        this.template_id = template_id;
    }
    public String getTouser() {
        return touser;
    }
    public void setTouser(String touser) {
        this.touser = touser;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public Map<String, TemplateData> getData() {
        return data;
    }
    public void setData(Map<String, TemplateData> data) {
        this.data = data;
    }

    public String getTopcolor() {
        return topcolor;
    }

    public void setTopcolor(String topcolor) {
        this.topcolor = topcolor;
    }
}
