package com.zzb.chn.bean.third;

import java.util.List;

public class ThirdCommissionBean {
	private String outCheckedId; 
	private String areaCode;
	private Integer checkState;
	private Integer prvId;
	private Integer payType;
	private BusinessCusBean businessCus;
	private VvTaxBean vvTax;
	private List<InsureInfoBean> insureInfo;
	private PolicyInfoBean policyInfo;
	private CarInfoBean carInfo;
	private List<PersonBean> persons;
	private String outPurchaseOrgId;
	private String outPurchaseOrgName;
	
	private String checkedId;
	private Integer checkedState;
	private Boolean ret;
	private Integer code;
	private String msg;
	private PolicySettleBean policySettle;
	
	public PolicySettleBean getPolicySettle() {
		return policySettle;
	}
	public void setPolicySettle(PolicySettleBean policySettle) {
		this.policySettle = policySettle;
	}
	public String getCheckedId() {
		return checkedId;
	}
	public void setCheckedId(String checkedId) {
		this.checkedId = checkedId;
	}
	public Integer getCheckedState() {
		return checkedState;
	}
	public void setCheckedState(Integer checkedState) {
		this.checkedState = checkedState;
	}
	public Boolean getRet() {
		return ret;
	}
	public void setRet(Boolean ret) {
		this.ret = ret;
	}
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getOutPurchaseOrgId() {
		return outPurchaseOrgId;
	}
	public void setOutPurchaseOrgId(String outPurchaseOrgId) {
		this.outPurchaseOrgId = outPurchaseOrgId;
	}
	public String getOutPurchaseOrgName() {
		return outPurchaseOrgName;
	}
	public void setOutPurchaseOrgName(String outPurchaseOrgName) {
		this.outPurchaseOrgName = outPurchaseOrgName;
	}
	public List<PersonBean> getPersons() {
		return persons;
	}
	public void setPersons(List<PersonBean> persons) {
		this.persons = persons;
	}
	public CarInfoBean getCarInfo() {
		return carInfo;
	}
	public void setCarInfo(CarInfoBean carInfo) {
		this.carInfo = carInfo;
	}
	public PolicyInfoBean getPolicyInfo() {
		return policyInfo;
	}
	public void setPolicyInfo(PolicyInfoBean policyInfo) {
		this.policyInfo = policyInfo;
	}
	public List<InsureInfoBean> getInsureInfo() {
		return insureInfo;
	}
	public void setInsureInfo(List<InsureInfoBean> insureInfo) {
		this.insureInfo = insureInfo;
	}
	public VvTaxBean getVvTax() {
		return vvTax;
	}
	public void setVvTax(VvTaxBean vvTax) {
		this.vvTax = vvTax;
	}
	public Integer getPrvId() {
		return prvId;
	}
	public void setPrvId(Integer prvId) {
		this.prvId = prvId;
	}
	public String getOutCheckedId() {
		return outCheckedId;
	}
	public void setOutCheckedId(String outCheckedId) {
		this.outCheckedId = outCheckedId;
	}
	public String getAreaCode() {
		return areaCode;
	}
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	public Integer getCheckState() {
		return checkState;
	}
	public void setCheckState(Integer checkState) {
		this.checkState = checkState;
	}
	public Integer getPayType() {
		return payType;
	}
	public void setPayType(Integer payType) {
		this.payType = payType;
	}
	public BusinessCusBean getBusinessCus() {
		return businessCus;
	}
	public void setBusinessCus(BusinessCusBean businessCus) {
		this.businessCus = businessCus;
	}
		
}
