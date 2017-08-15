package com.zzb.extra.controller.vo;

import java.io.Serializable;

/**
 * Created by MagicYuan on 2016/5/12.
 */
public class AccountWithdrawVo implements Serializable {

    /**
     * 用户id
     */
    private String agentid;

    /**
     * 提现金额
     */
    private Double amount;

    /**
     * 银行名称
     */
    private String bankname;

    /**
     * 开户支行
     */
    private String branch;

    /**
     * 银行卡号
     */
    private String cardnumber;

    public String getAgentid() {
        return agentid;
    }

    public void setAgentid(String agentid) {
        this.agentid = agentid;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getBankname() {
        return bankname;
    }

    public void setBankname(String bankname) {
        this.bankname = bankname;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getCardnumber() {
        return cardnumber;
    }

    public void setCardnumber(String cardnumber) {
        this.cardnumber = cardnumber;
    }
}
