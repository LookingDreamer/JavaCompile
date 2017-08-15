package com.zzb.extra.model;

/**
 * Created by MagicYuan on 2016/5/30.
 */
public class AgentTaskModel {
    /**
     * 用户ID
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
     * 供应商协议id
     */
    private String agreementid;

    /**
     * 推荐人ID
     */
    private String referrerid;

    /**
     * 用户来源渠道
     */
    private String agentChannel;

    /**
     * 开始时间
     */
    private String startdate;

    /**
     * 结束时间
     */
    private String enddate;

    /**
     *  渠道id
     */
    private String channelid;

    private String commissionRatioId;

    /**
     *  佣金标记 报价、承保完成
     */
    private String commissionFlag = "quote";

    public String getChannelid() {
        return channelid;
    }

    public void setChannelid(String channelid) {
        this.channelid = channelid;
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

    public String getAgreementid() {
        return agreementid;
    }

    public void setAgreementid(String agreementid) {
        this.agreementid = agreementid;
    }

    public String getReferrerid() {
        return referrerid;
    }

    public void setReferrerid(String referrerid) {
        this.referrerid = referrerid;
    }

    public String getAgentChannel() {
        return agentChannel;
    }

    public void setAgentChannel(String agentChannel) {
        this.agentChannel = agentChannel;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }


    public String getCommissionFlag() {
        return commissionFlag;
    }

    public void setCommissionFlag(String commissionFlag) {
        this.commissionFlag = commissionFlag;
    }


    public String getCommissionRatioId() {
        return commissionRatioId;
    }

    public void setCommissionRatioId(String commissionRatioId) {
        this.commissionRatioId = commissionRatioId;
    }

}
