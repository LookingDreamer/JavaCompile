package com.zzb.extra.entity;

import com.cninsure.core.dao.domain.Identifiable;

public class INSBAgentRecommend extends INSBAgentUser implements Identifiable {
    private static final long serialVersionUID = 1L;

    private String referreropenid; // 推荐人openid
    private String presenteeopenid; // 被推荐人openid
    private String recommendtype; //推荐方式

    public String getReferreropenid() {
        return referreropenid;
    }

    public void setReferreropenid(String referreropenid) {
        this.referreropenid = referreropenid;
    }

    public String getPresenteeopenid() {
        return presenteeopenid;
    }

    public void setPresenteeopenid(String presenteeopenid) {
        this.presenteeopenid = presenteeopenid;
    }

    @Override
    public String getRecommendtype() {
        return recommendtype;
    }

    @Override
    public void setRecommendtype(String recommendtype) {
        this.recommendtype = recommendtype;
    }
}
