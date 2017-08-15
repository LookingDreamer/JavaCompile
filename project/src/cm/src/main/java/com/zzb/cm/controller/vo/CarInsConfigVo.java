package com.zzb.cm.controller.vo;

import java.util.List;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;
import com.zzb.app.model.bean.AmountSelect;
import com.zzb.app.model.bean.SelectOption;

/**
 * @author liuchao
 * 读取保险配置给cm页面传递使用
 *
 */
public class CarInsConfigVo  extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 是否勾选中
	 */
	private String isChecked;
	
	/**
	 * 险种编码
	 */
	private String riskcode;
	
	/**
	 * 险种名称
	 */
	private String riskname;

	/**
	 * 险别编码
	 */
	private String inskindcode;
	
	/**
	 * 险别名称
	 */
	private String inskindname;

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
	private List<SelectOption> selecteditem;
	
	/**
	 * 险别要素已选项
	 */
	private List<String> selecteditemStrs;
	
	/**
	 * 前置险种
	 */
	private String preriskkind;
	
	/**
	 * 险别提供的要素选项
	 */
	private List<AmountSelect> amountSlecets;
	
	//特殊险别标志
	private String specialRiskkindFlag;
	
	//特殊险别value值
	private String specialRiskkindValue;
	
	//特殊险别key-value值
	private List<String> specialKVList;
	
	public String getIsChecked() {
		return isChecked;
	}


	public void setIsChecked(String isChecked) {
		this.isChecked = isChecked;
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


	public String getInskindname() {
		return inskindname;
	}


	public void setInskindname(String inskindname) {
		this.inskindname = inskindname;
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

	
	public List<SelectOption> getSelecteditem() {
		return selecteditem;
	}


	public void setSelecteditem(List<SelectOption> selecteditem) {
		this.selecteditem = selecteditem;
	}


	public List<AmountSelect> getAmountSlecets() {
		return amountSlecets;
	}


	public void setAmountSlecets(List<AmountSelect> amountSlecets) {
		this.amountSlecets = amountSlecets;
	}


	public List<String> getSelecteditemStrs() {
		return selecteditemStrs;
	}


	public void setSelecteditemStrs(List<String> selecteditemStrs) {
		this.selecteditemStrs = selecteditemStrs;
	}


	public String getRiskname() {
		return riskname;
	}


	public void setRiskname(String riskname) {
		this.riskname = riskname;
	}

	public String getPreriskkind() {
		return preriskkind;
	}


	public void setPreriskkind(String preriskkind) {
		this.preriskkind = preriskkind;
	}


	public String getSpecialRiskkindFlag() {
		return specialRiskkindFlag;
	}


	public void setSpecialRiskkindFlag(String specialRiskkindFlag) {
		this.specialRiskkindFlag = specialRiskkindFlag;
	}


	public String getSpecialRiskkindValue() {
		return specialRiskkindValue;
	}


	public void setSpecialRiskkindValue(String specialRiskkindValue) {
		this.specialRiskkindValue = specialRiskkindValue;
	}


	public List<String> getSpecialKVList() {
		return specialKVList;
	}


	public void setSpecialKVList(List<String> specialKVList) {
		this.specialKVList = specialKVList;
	}


	public CarInsConfigVo() {
		super();
	}
	
}
