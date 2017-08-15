package com.zzb.extra.model;

/**
 * Created by MagicYuan on 2016/6/1.
 */
public class AgentPrizeModel {

    /**
     * 用户id
     */
    private String agentid;

    /**
     * 活动奖品id
     */
    private String activityprizeid;

    /**
     * 活动名称
     */
    private String activityname;

    /**
     * 活动类型
     */
    private String activitytype;

    /**
     * 奖品名称
     */
    private String prizename;

    /**
     * 奖品类型
     */
    private String prizetype;

    /**
     * 任务id
     */
    private String taskid;

    /**
     * 数额
     */
    private Double counts;

    /**
     * 状态
     */
    private String status;

    public String getAgentid() {
        return agentid;
    }

    public void setAgentid(String agentid) {
        this.agentid = agentid;
    }

    public String getActivityprizeid() {
        return activityprizeid;
    }

    public void setActivityprizeid(String activityprizeid) {
        this.activityprizeid = activityprizeid;
    }

    public String getActivityname() {
        return activityname;
    }

    public void setActivityname(String activityname) {
        this.activityname = activityname;
    }

    public String getActivitytype() {
        return activitytype;
    }

    public void setActivitytype(String activitytype) {
        this.activitytype = activitytype;
    }

    public String getPrizename() {
        return prizename;
    }

    public void setPrizename(String prizename) {
        this.prizename = prizename;
    }

    public String getPrizetype() {
        return prizetype;
    }

    public void setPrizetype(String prizetype) {
        this.prizetype = prizetype;
    }

    public String getTaskid() {
        return taskid;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }

    public Double getCounts() {
        return counts;
    }

    public void setCounts(Double counts) {
        this.counts = counts;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
