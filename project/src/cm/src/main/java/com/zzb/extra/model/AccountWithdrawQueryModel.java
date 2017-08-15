package com.zzb.extra.model;

/**
 * Created by MagicYuan on 2016/5/4.
 */
public class AccountWithdrawQueryModel {

    /**
     * 用户ID
     */
    private String agentid;

    /**
     * 用户姓名
     */
    private String agentname;

    /**
     * 转入银行账号
     */
    private String cardnumber;

    /**
     * 提现状态
     */
    private String status;

    /**
     * 提现申请开始时间
     */
    private String startdate;

    /**
     * 提现申请结束时间
     */
    private String enddate;

    public String getAgentid() {
        return agentid;
    }

    public void setAgentid(String agentid) {
        this.agentid = agentid;
    }

    public String getAgentname() {
        return agentname;
    }

    public void setAgentname(String agentname) {
        this.agentname = agentname;
    }

    public String getCardnumber() {
        return cardnumber;
    }

    public void setCardnumber(String cardnumber) {
        this.cardnumber = cardnumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

}
