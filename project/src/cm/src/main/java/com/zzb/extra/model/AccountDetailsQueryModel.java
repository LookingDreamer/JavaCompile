package com.zzb.extra.model;

/**
 * Created by MagicYuan on 2016/5/4.
 */
public class AccountDetailsQueryModel {

    /**
     * 用户ID
     */
    private String agentid;

    /**
     * 用户姓名
     */
    private String agentname;

    /**
     * 收支类型
     */
    private String fundtype;

    /**
     * 收支来源
     */
    private String fundsource;

    /**
     * 记账开始时间
     */
    private String startdate;

    /**
     * 记账结束时间
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

    public String getFundtype() {
        return fundtype;
    }

    public void setFundtype(String fundtype) {
        this.fundtype = fundtype;
    }

    public String getFundsource() {
        return fundsource;
    }

    public void setFundsource(String fundsource) {
        this.fundsource = fundsource;
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
