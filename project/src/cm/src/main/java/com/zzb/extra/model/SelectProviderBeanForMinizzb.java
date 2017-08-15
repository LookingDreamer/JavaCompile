package com.zzb.extra.model;

import com.zzb.mobile.model.SelectProviderBean;

/**
 * Created by liwucai on 2016/5/26 11:46.
 */
public class SelectProviderBeanForMinizzb extends SelectProviderBean {

    private String providername; //渠道供应商

    private String agentcode; //出单代理人工号

    private String chnagreementid;//渠道协议号

    public String getChnagreementid() {
        return chnagreementid;
    }

    public void setChnagreementid(String chnagreementid) {
        this.chnagreementid = chnagreementid;
    }

    public String getProvidername() {
        return providername;
    }

    public void setProvidername(String providername) {
        this.providername = providername;
    }

    public String getAgentcode() {
        return agentcode;
    }

    public void setAgentcode(String agentcode) {
        this.agentcode = agentcode;
    }

}
