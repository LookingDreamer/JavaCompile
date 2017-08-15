package com.zzb.mobile.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;

/**
 * Created by HaiJuan.Lei on 2016/10/8.
 * 推荐人抽奖码信息表
 */
public class INSBMarketingLottery extends BaseEntity implements Identifiable {
    private static final long serialVersionUID = 1L;
    /**
     * 推荐人
     * */
    private String referee;
    /**
     * 抽奖码
     * */
    private String  lotterycode;

    /**
     * 抽奖码状态（0：未开奖，1：未中奖，2：中奖）
     * */
    private String  lotterystatus;

    /**
     * 抽奖时间
     * */
    private String  lotterytime;

    /**
     * 营销活动名称
     * */
    private String  activityname;

    /**
     * 抽奖期数
     * */
    private int period;

    public String getReferee() {
        return referee;
    }

    public void setReferee(String referee) {
        this.referee = referee;
    }

    public String getLotterycode() {
        return lotterycode;
    }

    public void setLotterycode(String lotterycode) {
        this.lotterycode = lotterycode;
    }

    public String getLotterystatus() {
        return lotterystatus;
    }

    public void setLotterystatus(String lotterystatus) {
        this.lotterystatus = lotterystatus;
    }

    public String getLotterytime() {
        return lotterytime;
    }

    public void setLotterytime(String lotterytime) {
        this.lotterytime = lotterytime;
    }

    public String getActivityname() {
        return activityname;
    }

    public void setActivityname(String activityname) {
        this.activityname = activityname;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }
}
