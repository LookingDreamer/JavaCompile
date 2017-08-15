package com.zzb.mobile.model;

import java.util.List;

/**
 * Created by Dai on 2016/7/2.
 */
public class RenewaltemSaveModel {

    private String processinstanceid;

    private String inscomcode;

    private String agreementid;

    private List<String> renewalitems;
    /**
     * 备注
     */
    private String remark;
    /**
     * 备注类型
     */
    private String remarkcode;

    public String getProcessinstanceid() {
        return processinstanceid;
    }

    public void setProcessinstanceid(String processinstanceid) {
        this.processinstanceid = processinstanceid;
    }

    public String getInscomcode() {
        return inscomcode;
    }

    public void setInscomcode(String inscomcode) {
        this.inscomcode = inscomcode;
    }

    public String getAgreementid() {
        return agreementid;
    }

    public void setAgreementid(String agreementid) {
        this.agreementid = agreementid;
    }

    public List<String> getRenewalitems() {
        return renewalitems;
    }

    public void setRenewalitems(List<String> renewalitems) {
        this.renewalitems = renewalitems;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemarkcode() {
        return remarkcode;
    }

    public void setRemarkcode(String remarkcode) {
        this.remarkcode = remarkcode;
    }
}
