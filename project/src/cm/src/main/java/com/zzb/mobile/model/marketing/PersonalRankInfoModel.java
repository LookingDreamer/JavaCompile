package com.zzb.mobile.model.marketing;

import com.cninsure.core.dao.domain.Identifiable;

/**
 * Created by HaiJuan.Lei on 2016/10/9.
 * 单条排名情况信息（用于返回给前端）
 */
public class PersonalRankInfoModel implements Identifiable {

    /**
     * 代理人工号
     * */
    private String agentCode;

    /**
     * 代理人姓名
     * */
    private String agentName;

    /**
     * 推荐人数排名次序（名次）
     * */
    private int rank;

    /**
     * 累计推荐人数
     * */
    private int referrals;

    /**
     * 网点编码
     * */
    private String orgCodeFive;

    /**
     * 网点所属二级机构名称
     * */
    private String orgName;


    /**
     * 累计获得红包（元）
     * */
    private int awardAmount;

    public String getAgentCode() {
        return agentCode;
    }

    public void setAgentCode(String agentCode) {
        this.agentCode = agentCode;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getReferrals() {
        return referrals;
    }

    public void setReferrals(int referrals) {
        this.referrals = referrals;
    }

    public int getAwardAmount() {
        return awardAmount;
    }

    public void setAwardAmount(int awardAmount) {
        this.awardAmount = awardAmount;
    }

    public String getOrgCodeFive() {
        return orgCodeFive;
    }

    public void setOrgCodeFive(String orgCodeFive) {
        this.orgCodeFive = orgCodeFive;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public void setId(String s) {

    }
}
