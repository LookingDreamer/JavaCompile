package com.zzb.cm.Interface.entity.car.model;

import java.math.BigDecimal;
import java.util.List;

import com.zzb.cm.Interface.entity.model.PayChannelType;

/**
 * 支付渠道
 * Created by austinChen on 2015/10/9.
 */
public class PayChannel {
    /*支付渠道类型*/
    private PayChannelType payChannelType;
    private Integer weight;
    /*图标*/
    private String logo;
    /*服务金额*/
    private BigDecimal serviceCharges;
    /*储蓄卡银行*/
    private List<BankInfo> depositBanks;
    /*信用卡银行*/
    private List<BankInfo> credittBanks;

    public List<BankInfo> getCredittBanks() {
        return credittBanks;
    }

    public void setCredittBanks(List<BankInfo> credittBanks) {
        this.credittBanks = credittBanks;
    }

    public List<BankInfo> getDepositBanks() {
        return depositBanks;
    }

    public void setDepositBanks(List<BankInfo> depositBanks) {
        this.depositBanks = depositBanks;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public PayChannelType getPayChannelType() {
        return payChannelType;
    }

    public void setPayChannelType(PayChannelType payChannelType) {
        this.payChannelType = payChannelType;
    }

    public BigDecimal getServiceCharges() {
        return serviceCharges;
    }

    public void setServiceCharges(BigDecimal serviceCharges) {
        this.serviceCharges = serviceCharges;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}
