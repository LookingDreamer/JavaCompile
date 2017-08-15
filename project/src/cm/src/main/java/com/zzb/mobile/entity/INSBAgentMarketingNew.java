package com.zzb.mobile.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;

import java.util.Date;

/**
 * Created by HaiJuan.Lei on 2016/10/8.
 * 营销活动新增的代理人信息
 */
public class INSBAgentMarketingNew  extends BaseEntity implements Identifiable {
    private static final long serialVersionUID = 1L;

    /**
     * 对应insbagent表的id
     * */
    private String insbagentid;

    /**
     * 代理人openid
     * */
    private String openid;

    /**
     * 代理人姓名
     * */
    private String name;

    /**
     * 代理人性别
     * */
    private String sex;

    /**
     * 代理人工号
     * */
    private String jobnum;

    /**
     * 代理人联系电话号码
     * */
    private String phone;

    /**
     * 代理人所属机构网点编码
     * */
    private String deptid;

    /**
     * 代理人编码（=代理人工号）
     * */
    private  String agentcode;
    /**
     *
     * 代理人的推荐人工号
     * */
    private String referee;

    /**
     * 代理人推荐人openid
     * */
    private String refereeopenid;

    /**
     * 注册成功时间
     * */
    private Date registertime;

    /**
     * 代理人证件号
     * */
    private String idno;

    /**
     * 代理人证件类型
     * */
    private String idnotype;

    /**
     * 是否已将微信红包成功发送给推荐人
     * */
    private String issentredpacket;

    /**
     * 红包发放情况描述（失败：（1：无openid，2、未出单），成功：0）
     * */
    private String redpacketStatus;


    public String getRedpacketStatus() {
        return redpacketStatus;
    }

    public void setRedpacketStatus(String redpacketStatus) {
        this.redpacketStatus = redpacketStatus;
    }

    /**
     * 营销活动名称
     * */
    private  String activityname;

    public String getInsbagentid() {
        return insbagentid;
    }

    public void setInsbagentid(String insbagentid) {
        this.insbagentid = insbagentid;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getJobnum() {
        return jobnum;
    }

    public void setJobnum(String jobnum) {
        this.jobnum = jobnum;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDeptid() {
        return deptid;
    }

    public void setDeptid(String deptid) {
        this.deptid = deptid;
    }

    public String getAgentcode() {
        return agentcode;
    }

    public void setAgentcode(String agentcode) {
        this.agentcode = agentcode;
    }

    public String getReferee() {
        return referee;
    }

    public void setReferee(String referee) {
        this.referee = referee;
    }

    public String getRefereeopenid() {
        return refereeopenid;
    }

    public void setRefereeopenid(String refereeopenid) {
        this.refereeopenid = refereeopenid;
    }

    public Date getRegistertime() {
        return registertime;
    }

    public void setRegistertime(Date registertime) {
        this.registertime = registertime;
    }

    public String getIdno() {
        return idno;
    }

    public void setIdno(String idno) {
        this.idno = idno;
    }

    public String getIdnotype() {
        return idnotype;
    }

    public void setIdnotype(String idnotype) {
        this.idnotype = idnotype;
    }

    public String getIssentredpacket() {
        return issentredpacket;
    }

    public void setIssentredpacket(String issentredpacket) {
        this.issentredpacket = issentredpacket;
    }

    public String getActivityname() {
        return activityname;
    }

    public void setActivityname(String activityname) {
        this.activityname = activityname;
    }
}
