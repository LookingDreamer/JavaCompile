package com.zzb.mobile.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;
import java.util.Date;

/**
 * Created by HaiJuan.Lei on 2016/10/10.
 *
 */
public class INSBPersonalLotteryCodeInfo extends BaseEntity implements Identifiable {
    private static final long serialVersionUID = 1L;

    /**
     * 抽奖码
     * */
    private String lotterycode;

    /**
     *创建抽奖码时间
     * */
    private Date createcodetime;

    /**
     *使用抽奖码的时间
     * */
    private Date usetime;

    /**
     *代理人工号
     * */
    private String agentcode;

    /**
     *代理人姓名
     * */
    private String agentname;

    /**
     *代理人性别
     * */
    private int sex;

    /**
     *抽奖码状态（0：未开奖，1：未中奖，2：中奖）
     * */
    private int status;

    /**
     *期数
     * */
    private int period;

    public String getLotterycode() {
        return lotterycode;
    }

    public void setLotterycode(String lotterycode) {
        this.lotterycode = lotterycode;
    }

    public Date getCreatecodetime() {
        return createcodetime;
    }

    public void setCreatecodetime(Date createcodetime) {
        this.createcodetime = createcodetime;
    }

    public Date getUsetime() {
        return usetime;
    }

    public void setUsetime(Date usetime) {
        this.usetime = usetime;
    }

    public String getAgentcode() {
        return agentcode;
    }

    public void setAgentcode(String agentcode) {
        this.agentcode = agentcode;
    }

    public String getAgentname() {
        return agentname;
    }

    public void setAgentname(String agentname) {
        this.agentname = agentname;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }
}
