package com.zzb.cm.Interface.entity.car.model; /**
 *
 */

import java.math.BigDecimal;

import com.zzb.cm.Interface.entity.model.DeliveType;
import com.zzb.cm.Interface.entity.model.ReceiveDayType;
import com.zzb.cm.Interface.entity.model.ReceiveTimeType;

/**
 * 配送信息
 *
 * @author austinChen
 *         created at 2015年6月15日 下午1:55:40
 */
public class DeliverInfo extends PersonInfo {

    /**
     * 序列化ID
     */
    private static final long serialVersionUID = -2839412495057793506L;

    /*省份*/
    private String province;
    /*城市*/
    private String city;
    /*地区*/
    private String area;

    /*签收时间-工作日/休息日/任何时间*/
    private Integer receiveDayType;
    /*签收时间-早/中/晚/全天候*/
    private Integer receiveTimeType;

    /*是否需要发票*/
    private Boolean isInvoice;
    /*发票抬头*/
    private String invoiceTitle;
    /*备注*/
    private String remark;
    /*配送方式*/
    private Integer deliveType;
    /*是否保险公司配送*/
    private Boolean isInsureCoDelive;

    /**/
    private Boolean isFreightCollect;
    /*配送费*/
    private BigDecimal fee;
    /*运单号*/
    private String traceNumber;
    /*供应商id*/
    private String providerId;
    /*供应商名称*/
    private String providerName;


    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public Boolean isInvoice() {
        return isInvoice;
    }

    public void setInvoice(Boolean isInvoice) {
        this.isInvoice = isInvoice;
    }

    public String getInvoiceTitle() {
        return invoiceTitle;
    }

    public void setInvoiceTitle(String invoiceTitle) {
        this.invoiceTitle = invoiceTitle;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

	public String getTraceNumber() {
        return traceNumber;
    }

    public void setTraceNumber(String traceNumber) {
        this.traceNumber = traceNumber;
    }

    public Boolean isFreightCollect() {
        return isFreightCollect;
    }

    public void setFreightCollect(Boolean isFreightCollect) {
        this.isFreightCollect = isFreightCollect;
    }

    public Boolean isInsureCoDelive() {
        return isInsureCoDelive;
    }

    public void setInsureCoDelive(Boolean isInsureCoDelive) {
        this.isInsureCoDelive = isInsureCoDelive;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

	public Integer getReceiveDayType() {
		return receiveDayType;
	}

	public void setReceiveDayType(Integer receiveDayType) {
		this.receiveDayType = receiveDayType;
	}

	public Integer getReceiveTimeType() {
		return receiveTimeType;
	}

	public void setReceiveTimeType(Integer receiveTimeType) {
		this.receiveTimeType = receiveTimeType;
	}

	public Boolean getIsInvoice() {
		return isInvoice;
	}

	public void setIsInvoice(Boolean isInvoice) {
		this.isInvoice = isInvoice;
	}

	public Integer getDeliveType() {
		return deliveType;
	}

	public void setDeliveType(Integer deliveType) {
		this.deliveType = deliveType;
	}

	public Boolean getIsInsureCoDelive() {
		return isInsureCoDelive;
	}

	public void setIsInsureCoDelive(Boolean isInsureCoDelive) {
		this.isInsureCoDelive = isInsureCoDelive;
	}

	public Boolean getIsFreightCollect() {
		return isFreightCollect;
	}

	public void setIsFreightCollect(Boolean isFreightCollect) {
		this.isFreightCollect = isFreightCollect;
	}

	public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
