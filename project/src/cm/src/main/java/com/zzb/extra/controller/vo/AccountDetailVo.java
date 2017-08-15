package com.zzb.extra.controller.vo;

import java.io.Serializable;

/**
 * Created by MagicYuan on 2016/5/12.
 */
public class AccountDetailVo implements Serializable {

    /**
     * 用户id
     */
    private String agentid;

    /**
     * 收支来源
     */
    private String fundsource;

    /**
     * 金额
     */
    private Double amount;

    /**
     * 备注
     */
    private String noti;

    public String getAgentid() {
        return agentid;
    }

    public void setAgentid(String agentid) {
        this.agentid = agentid;
    }

    public String getFundsource() {
        return fundsource;
    }

    public void setFundsource(String fundsource) {
        this.fundsource = fundsource;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getNoti() {
        return noti;
    }

    public void setNoti(String noti) {
        this.noti = noti;
    }
}
