package com.zzb.extra.controller.vo;

/**
 * 临时工号影射出单工号
 * Created by StevenLi on 2016/5/23 11:37.
 */
public class TempjobnumMap2Jobnum {
    private String channeluserid; //渠道用户uuid
    private String tempjobnum; //渠道临时工号
    private String token; //渠道临时工号生成的token
    private String city; //投保地区
    private String usersource; //用户来源：minizzb
    private String openid; //渠道用户openid
    private String jobnum; //渠道出单工号
    private String agentId; //渠道出单工号的uuid

    public String getChanneluserid() {
        return channeluserid;
    }

    public void setChanneluserid(String channeluserid) {
        this.channeluserid = channeluserid;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public String getTempjobnum() {
        return tempjobnum;
    }

    public void setTempjobnum(String tempjobnum) {
        this.tempjobnum = tempjobnum;
    }

    public String getJobnum() {
        return jobnum;
    }

    public void setJobnum(String jobnum) {
        this.jobnum = jobnum;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getUsersource() {
        return usersource;
    }

    public void setUsersource(String usersource) {
        this.usersource = usersource;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }
}
