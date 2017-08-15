package com.zzb.mobile.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;

import java.util.Date;


/**
 * Created by HaiJuan.Lei on 2016/10/19.
 * 核心206-10-16号之前的代理人数据
 */
public class INSBCoreAgent extends BaseEntity implements Identifiable {

    private static final long serialVersionUID = 1L;

    /**
     * 核心代理人工号
     * */
    private String agentcode;
    /**
     * 代理人认证通过时间
     * */
    private Date authenticateddate;


    public Date getAuthenticateddate() {
        return authenticateddate;
    }

    public void setAuthenticateddate(Date authenticateddate) {
        this.authenticateddate = authenticateddate;
    }

    public String getAgentcode() {
        return agentcode;
    }

    public void setAgentcode(String agentcode) {
        this.agentcode = agentcode;
    }
}