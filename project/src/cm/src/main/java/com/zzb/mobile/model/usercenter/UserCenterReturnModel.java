package com.zzb.mobile.model.usercenter;

/**
 * Created by Hwc on 2017/5/22.
 */
public class UserCenterReturnModel {
    /**
     * 回调URL
     */
    private String backurl;
    /**
     * 响应信息
     */
    private String message;
    /**
     * 返回状态信息fail success
     */
    private String status;
    /**
     * 登录cookie,对应一个用户，两个小时过期
     */
    private String usercookie;

    public String getBackurl() {
        return backurl;
    }

    public void setBackurl(String backurl) {
        this.backurl = backurl;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUsercookie() {
        return usercookie;
    }

    public void setUsercookie(String usercookie) {
        this.usercookie = usercookie;
    }
}
