package com.zzb.warranty.form;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by Administrator on 2017/1/14.
 */
public class PayForm {
    @NotNull(message = "请提供订单号")
    private String orderNo;
    @NotNull(message = "支付通道不能为空")
    private String channelId;
    @NotNull(message = "支付类型不能为空")
    private String payType;
    @NotNull(message = "paySource不能为空")
    private String paySource;
    private String redirectUrl;

    private List<String> coupons;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getPaySource() {
        return paySource;
    }

    public void setPaySource(String paySource) {
        this.paySource = paySource;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public List<String> getCoupons() {
        return coupons;
    }

    public void setCoupons(List<String> coupons) {
        this.coupons = coupons;
    }

    @Override
    public String toString() {
        return "PayForm{" +
                "orderNo='" + orderNo + '\'' +
                ", channelId='" + channelId + '\'' +
                ", payType='" + payType + '\'' +
                ", paySource='" + paySource + '\'' +
                ", redirectUrl='" + redirectUrl + '\'' +
                '}';
    }
}
