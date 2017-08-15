package com.zzb.cm.Interface.entity.car.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 交强险投保信息
 * Created by austinChen on 2015/10/9.
 */
public class EfcSuiteInfo {
    /*交强险投保开始日期*/
    private Date start;
    /*交强险投保结束日期*/
    private Date end;
    /*交强险总保额*/
    private BigDecimal amount;
    /*交强险原始保费*/
    private BigDecimal orgCharge;
    /*交强险折后保费*/
    private BigDecimal discountCharge;
    /*交强险折扣率*/
    private BigDecimal discountRate;
    /**
	 * 不含税保费
	 */
	private Double noTaxPremium;
	
	/**
	 * 税
	 */
	private Double tax;
	
	public Double getNoTaxPremium() {
		return noTaxPremium;
	}

	public void setNoTaxPremium(Double noTaxPremium) {
		this.noTaxPremium = noTaxPremium;
	}

	public Double getTax() {
		return tax;
	}

	public void setTax(Double tax) {
		this.tax = tax;
	}

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getDiscountCharge() {
        return discountCharge;
    }

    public void setDiscountCharge(BigDecimal discountCharge) {
        this.discountCharge = discountCharge;
    }

    public BigDecimal getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(BigDecimal discountRate) {
        this.discountRate = discountRate;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public BigDecimal getOrgCharge() {
        return orgCharge;
    }

    public void setOrgCharge(BigDecimal orgCharge) {
        this.orgCharge = orgCharge;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }
}
