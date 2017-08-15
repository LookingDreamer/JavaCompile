package com.zzb.cm.Interface.entity.car.model; /**
 * 
 */

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.zzb.cm.Interface.entity.model.PayChannelType;
import com.zzb.cm.Interface.entity.model.PayStyle;

/**
 * 支付信息
 * @author austinChen
 * created at 2015年6月15日 下午3:12:14
 */
public class PayInfo implements Serializable {
	
	private String id;

	/**
	 * 序列化ID
	 */
	private static final long serialVersionUID = 7977188660505923386L;

	/*支付渠道ID*/
	private String payChannelId;
    /*支付渠道类型*/
	private PayChannelType payChannelType;
    /*支付方式*/
	private PayStyle payStyle;
    /*支付平台生成的交易跟踪号*/
	private String tradeNo;
    /*保险公司生成的支付跟踪号*/
	private String insureCoNo;
    /*杂项*/
	private String misc;


    /*支付链接*/
	private String payUrl;
	/*手机支付链接*/
	private String payWapUrl;
	/*电脑支付链接*/
	private String payPcUrl;
    /*支付金额*/
	private BigDecimal amount;
    /*支付时间*/
	private Date payTime;
    /*支付结果*/
	private Boolean payResult;

	//支付货币类型
	private String currencyCode;

	//收款商户号
	private String merchantId;
	//收款商户名称
	private String merchantName;

	//支付方式是否允许修改
	private Boolean allowChangePayStyle;

	//支持的支付渠道
	private List<PayChannel> payChannels;


    public String getPayWapUrl() {
		return payWapUrl;
	}

	public void setPayWapUrl(String payWapUrl) {
		this.payWapUrl = payWapUrl;
	}

	public String getPayPcUrl() {
		return payPcUrl;
	}

	public void setPayPcUrl(String payPcUrl) {
		this.payPcUrl = payPcUrl;
	}

	public Boolean getPayResult() {
		return payResult;
	}

	public Boolean isAllowChangePayStyle() {
        return allowChangePayStyle;
    }

    public void setAllowChangePayStyle(Boolean allowChangePayStyle) {
        this.allowChangePayStyle = allowChangePayStyle;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInsureCoNo() {
        return insureCoNo;
    }

    public void setInsureCoNo(String insureCoNo) {
        this.insureCoNo = insureCoNo;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getMisc() {
		return misc;
	}

	public void setMisc(String misc) {
		this.misc = misc;
	}

	public String getPayChannelId() {
        return payChannelId;
    }

    public void setPayChannelId(String payChannelId) {
        this.payChannelId = payChannelId;
    }

    public List<PayChannel> getPayChannels() {
        return payChannels;
    }

    public void setPayChannels(List<PayChannel> payChannels) {
        this.payChannels = payChannels;
    }

    public PayChannelType getPayChannelType() {
        return payChannelType;
    }

    public void setPayChannelType(PayChannelType payChannelType) {
        this.payChannelType = payChannelType;
    }

    public Boolean isPayResult() {
        return payResult;
    }

    public void setPayResult(Boolean payResult) {
        this.payResult = payResult;
    }

    public PayStyle getPayStyle() {
        return payStyle;
    }

    public void setPayStyle(PayStyle payStyle) {
        this.payStyle = payStyle;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public String getPayUrl() {
        return payUrl;
    }

    public void setPayUrl(String payUrl) {
        this.payUrl = payUrl;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }
}
