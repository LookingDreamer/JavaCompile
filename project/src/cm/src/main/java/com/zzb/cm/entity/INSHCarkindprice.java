package com.zzb.cm.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;


public class INSHCarkindprice extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

	/**
	 * 任务id
	 */
	private String taskid;

	/**
	 * 保险公司代码
	 */
	private String inscomcode;

	/**
	 * 险种编码
	 */
	private String riskcode;

	/**
	 * 险别编码
	 */
	private String inskindcode;

	/**
	 * 险别类型
	 */
	private String inskindtype;

	/**
	 * 保额
	 */
	private Double amount;

	/**
	 * 不计免赔
	 */
	private String notdeductible;

	/**
	 * 保费
	 */
	private Double amountprice;

	/**
	 * 险别要素已选项
	 */
	private String selecteditem;

	/**
	 * 前置险种
	 */
	private String preriskkind;

	/**
	 * 险种名称
	 */
	private String riskname;

	/**
	 * 保额描述
	 */
	private String amountDesc;

	/**
	 * 折后保费
	 */
	private Double discountCharge;

	/**
	 * 折扣率
	 */
	private Double discountRate;

	/**
	 * 不计免赔编码
	 */
	private String bjmpCode;

	/**
	 * 不计免赔原始保费
	 */
	private Double bjmpOrgCharge;

	/**
	 * 不计免赔折后保费
	 */
	private Double bjmpDiscountCharge;

	/**
	 * 不计免赔折扣率
	 */
	private Double bjmpDiscountRate;

	/**
	 * 选择的保险配置的key值
	 */
	private String plankey;

	/**
	 * 精灵或edi，robot-精灵，edi-EDI
	 */
	private String fairyoredi;

	/**
	 * 流程节点，A-报价，B-核保回写，D-承保回写，1-cm后台操作
	 */
	private String nodecode;
	
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

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getInscomcode() {
		return inscomcode;
	}

	public void setInscomcode(String inscomcode) {
		this.inscomcode = inscomcode;
	}

	public String getRiskcode() {
		return riskcode;
	}

	public void setRiskcode(String riskcode) {
		this.riskcode = riskcode;
	}

	public String getInskindcode() {
		return inskindcode;
	}

	public void setInskindcode(String inskindcode) {
		this.inskindcode = inskindcode;
	}

	public String getInskindtype() {
		return inskindtype;
	}

	public void setInskindtype(String inskindtype) {
		this.inskindtype = inskindtype;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getNotdeductible() {
		return notdeductible;
	}

	public void setNotdeductible(String notdeductible) {
		this.notdeductible = notdeductible;
	}

	public Double getAmountprice() {
		return amountprice;
	}

	public void setAmountprice(Double amountprice) {
		this.amountprice = amountprice;
	}

	public String getSelecteditem() {
		return selecteditem;
	}

	public void setSelecteditem(String selecteditem) {
		this.selecteditem = selecteditem;
	}

	public String getPreriskkind() {
		return preriskkind;
	}

	public void setPreriskkind(String preriskkind) {
		this.preriskkind = preriskkind;
	}

	public String getRiskname() {
		return riskname;
	}

	public void setRiskname(String riskname) {
		this.riskname = riskname;
	}

	public String getAmountDesc() {
		return amountDesc;
	}

	public void setAmountDesc(String amountDesc) {
		this.amountDesc = amountDesc;
	}

	public Double getDiscountCharge() {
		return discountCharge;
	}

	public void setDiscountCharge(Double discountCharge) {
		this.discountCharge = discountCharge;
	}

	public Double getDiscountRate() {
		return discountRate;
	}

	public void setDiscountRate(Double discountRate) {
		this.discountRate = discountRate;
	}

	public String getBjmpCode() {
		return bjmpCode;
	}

	public void setBjmpCode(String bjmpCode) {
		this.bjmpCode = bjmpCode;
	}

	public Double getBjmpOrgCharge() {
		return bjmpOrgCharge;
	}

	public void setBjmpOrgCharge(Double bjmpOrgCharge) {
		this.bjmpOrgCharge = bjmpOrgCharge;
	}

	public Double getBjmpDiscountCharge() {
		return bjmpDiscountCharge;
	}

	public void setBjmpDiscountCharge(Double bjmpDiscountCharge) {
		this.bjmpDiscountCharge = bjmpDiscountCharge;
	}

	public Double getBjmpDiscountRate() {
		return bjmpDiscountRate;
	}

	public void setBjmpDiscountRate(Double bjmpDiscountRate) {
		this.bjmpDiscountRate = bjmpDiscountRate;
	}

	public String getPlankey() {
		return plankey;
	}

	public void setPlankey(String plankey) {
		this.plankey = plankey;
	}

	public String getFairyoredi() {
		return fairyoredi;
	}

	public void setFairyoredi(String fairyoredi) {
		this.fairyoredi = fairyoredi;
	}

	public String getNodecode() {
		return nodecode;
	}

	public void setNodecode(String nodecode) {
		this.nodecode = nodecode;
	}

}