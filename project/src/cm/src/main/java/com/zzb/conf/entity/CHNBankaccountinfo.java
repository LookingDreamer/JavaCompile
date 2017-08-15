package com.zzb.conf.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;

/**
 * Created by HaiJuan.Lei on 2016/9/18.
 * 银行卡信息表
 */
public class CHNBankaccountinfo extends BaseEntity implements Identifiable {
    private static final long serialVersionUID = 1L;

    /**
     * 任务号
     */
    private String taskid;

    /**
     * 支付流水号

     * */
    private String payflowno;

    /**
     * 持卡人姓名
     * */
    private String accountname;
    /**
     * 银行卡号
     * */
    private String accountno;
    /**
     * 开户行
     * */
    private String bankname;
    /**
     * 开户行所在省|直辖市
     * */
    private String province;

    /**
     * 开户行所在市、县
     * */
    private String county;

    /**
     * 银行编码*/
    private String bankcode;

    public String getBankcode() { return bankcode;}

    public void setBankcode(String bankcode) { this.bankcode = bankcode;}
    public String getTaskid() {
        return taskid;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }

    public String getPayflowno() {
        return payflowno;
    }

    public void setPayflowno(String payflowno) {
        this.payflowno = payflowno;
    }

    public String getAccountname() {
        return accountname;
    }

    public void setAccountname(String accountname) {
        this.accountname = accountname;
    }

    public String getAccountno() {
        return accountno;
    }

    public void setAccountno(String accountno) {
        this.accountno = accountno;
    }

    public String getBankname() {
        return bankname;
    }

    public void setBankname(String bankname) {
        this.bankname = bankname;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

}
