package com.zzb.cm.Interface.entity.car.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 车船税投保信息
 * Created by austinChen on 2015/10/9.
 */
public class TaxSuiteInfo {
    /*车船税投保开始时间*/
    private Date start;
    /*车船税投保结束时间*/
    private Date end;
    /*车船税金额*/
    private BigDecimal charge;
    /*车船税滞纳金*/
    private BigDecimal delayCharge;
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

    public BigDecimal getCharge() {
		return charge;
	}

	public void setCharge(BigDecimal charge) {
		this.charge = charge;
	}

	public BigDecimal getDelayCharge() {
        return delayCharge;
    }

    public void setDelayCharge(BigDecimal delayCharge) {
        this.delayCharge = delayCharge;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }
}
