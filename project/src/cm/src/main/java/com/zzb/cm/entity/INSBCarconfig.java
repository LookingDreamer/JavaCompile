package com.zzb.cm.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;


public class INSBCarconfig extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

	/**
	 * 任务id
	 */
	private String taskid;

	/**
	 * 险种代码
	 */
	private String riskcode;

	/**
	 * 险别类型
	 */
	private String inskindtype;

	/**
	 * 险别编码
	 */
	private String inskindcode;

	/**
	 * 保额
	 */
	private String amount;

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
	 * 选择的保险配置的key值
	 */
	private String plankey;
	
	public String getPlankey() {
		return plankey;
	}

	public void setPlankey(String plankey) {
		this.plankey = plankey;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getRiskcode() {
		return riskcode;
	}

	public void setRiskcode(String riskcode) {
		this.riskcode = riskcode;
	}

	public String getInskindtype() {
		return inskindtype;
	}

	public void setInskindtype(String inskindtype) {
		this.inskindtype = inskindtype;
	}

	public String getInskindcode() {
		return inskindcode;
	}

	public void setInskindcode(String inskindcode) {
		this.inskindcode = inskindcode;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
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

}