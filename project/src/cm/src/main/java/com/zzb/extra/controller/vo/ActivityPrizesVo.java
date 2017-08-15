package com.zzb.extra.controller.vo;

import java.io.Serializable;

/**
 * Created by MagicYuan on 2016/5/19.
 */
public class ActivityPrizesVo implements Serializable {
    /**
     * 用户id
     */
    private String agentid;

    /**
     * 任务id
     */
    private String taskid;

    /**
     * 供应商code
     */
    private String providercode;

    /**
     * 活动类型
     */

    private String activitytype;

    /**
     * 是否获取已有奖品分类总额
     */

    private boolean gettotal;

    /**
     * 操作类型
     */

    private String operate;

    /**
     * 用户奖品ID
     */

    private String agentprizeid;

    /**
     * 推荐人
     */

    private String referrerid;

    /**
     * 当前用户
     */

    private String jobnum;

    /**
     *  奖品状态
     */

    private String status;

    /**
     *  付款状态
     */

    private String paid;

    /**
     *  营销通道
     */
    private String agentchannel;

    /**
     *  奖品类别
     */
    private String prizetype;

    public String getAgentchannel() {
        return agentchannel;
    }

    public void setAgentchannel(String agentchannel) {
        this.agentchannel = agentchannel;
    }

    public String getAgentid() {
        return agentid;
    }

    public void setAgentid(String agentid) {
        this.agentid = agentid;
    }

    public String getTaskid() {
        return taskid;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }

    public String getProvidercode() {
        return providercode;
    }

    public void setProvidercode(String providercode) {
        this.providercode = providercode;
    }


    public String getActivitytype() {
        return activitytype;
    }

    public void setActivitytype(String activitytype) {
        this.activitytype = activitytype;
    }


    public boolean isGettotal() {
        return gettotal;
    }

    public void setGettotal(boolean gettotal) {
        this.gettotal = gettotal;
    }

    public String getOperate() {
        return operate;
    }

    public void setOperate(String operate) {
        this.operate = operate;
    }


    public String getAgentprizeid() {
        return agentprizeid;
    }

    public void setAgentprizeid(String agentprizeid) {
        this.agentprizeid = agentprizeid;
    }


    public String getReferrerid() {
        return referrerid;
    }

    public void setReferrerid(String referrerid) {
        this.referrerid = referrerid;
    }

    public String getJobnum() {
        return jobnum;
    }

    public void setJobnum(String jobnum) {
        this.jobnum = jobnum;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaid() {
        return paid;
    }

    public void setPaid(String paid) {
        this.paid = paid;
    }

    public String getPrizetype() {
        return prizetype;
    }

    public void setPrizetype(String prizetype) {
        this.prizetype = prizetype;
    }


}
