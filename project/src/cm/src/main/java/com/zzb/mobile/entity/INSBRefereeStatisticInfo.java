package com.zzb.mobile.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;

/**
 * Created by HaiJuan.Lei on 2016/10/19.
 * 推荐人的推荐信息
 */
public class INSBRefereeStatisticInfo  extends BaseEntity implements Identifiable {
    private static final long serialVersionUID = 1L;

    /**
     * 推荐人姓名
     * */
    private String name;

    /**
     * 推荐人工号
     * */
    private  String agentcode;

    /**

     * 推荐人数*/
    private  int referrals;

    public int getReferrals() {
        return referrals;
    }




    public void setReferrals(int referrals) {
        this.referrals = referrals;
    }

    public String getAgentcode() {
        return agentcode;
    }

    public void setAgentcode(String agentcode) {
        this.agentcode = agentcode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
