package com.lzgapi.order.model;

/**
 * Created by Administrator on 2017/5/23.
 */
public class LzgCheckTokenModel {
    /**
     * 请求结果标记，OK-成功，fail
     */
    private String flag;
    /**
     * 错误提示
     */
    private String message;
    /**
     * 错误编号
     */
    private String errorCode;
    /**
     * 代理人信息
     */
    private LzgUserInfoModel userinfo;

    /**
     * 代理人工号
     */
    private String agentcode;
    /**
     * 手机号
     */
    private String mobile;

    /**
     * email
     */
    private String email;

    /**
     * 登录同一用户中心的usercookie
     */
    private String usercookie;

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public LzgUserInfoModel getUserinfo() {
        return userinfo;
    }

    public void setUserinfo(LzgUserInfoModel userinfo) {
        this.userinfo = userinfo;
    }

    public String getAgentcode() {
        return agentcode;
    }

    public void setAgentcode(String agentcode) {
        this.agentcode = agentcode;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsercookie() {
        return usercookie;
    }

    public void setUsercookie(String usercookie) {
        this.usercookie = usercookie;
    }
}
