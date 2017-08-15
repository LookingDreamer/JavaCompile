package com.zzb.cm.Interface.entity.car.model; /**
 *
 */

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 险种配置
 *
 * @author austinChen
 *         created at 2015年6月15日 下午1:46:36
 */
public class SuiteDef implements Serializable {

    /**
     * 险种配置
     */
    private static final long serialVersionUID = -7606853479541143768L;

    /*险种代码*/
    private String code;
    /*险种名称*/
    private String name;
    /*保额描述*/
    private String amountDesc;
    /*保额*/
    private BigDecimal amount;
    /*原始保费*/
    private BigDecimal orgCharge;
    /*折后保费*/
    private BigDecimal discountCharge;
    /*折扣率*/
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

    public String getAmountDesc() {
        return amountDesc;
    }

    public void setAmountDesc(String amountDesc) {
        this.amountDesc = amountDesc;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getOrgCharge() {
        return orgCharge;
    }

    public void setOrgCharge(BigDecimal orgCharge) {
        this.orgCharge = orgCharge;
    }
}
