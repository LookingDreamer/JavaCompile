package com.zzb.mobile.model;

public class RenewalQuoteinfoModel {

	/**
	 * 实例id
	 */
	private String processinstanceid;
	/**
	 * 车牌号
	 */
	private String carNumber;
	/**
	 * 车主姓名
	 */
	private String carOwer;
	/**
	 * 投保公司编码
	 */
	private String inscomcode;
	/**
	 * 协议ID
	 */
	private String agreementid;

    /**
     * 出单网点id
     */
    private String singlesite;

    /**
     * 01传统02网销03电销
     */
    private String buybusitype;

    /**
     * 投保地区省份编码
     */
    private String insuredprovince;

    /**
     * 投保地区地市编码
     */
    private String insuredcity;

    public String getProcessinstanceid() {
        return processinstanceid;
    }

    public void setProcessinstanceid(String processinstanceid) {
        this.processinstanceid = processinstanceid;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public String getCarOwer() {
        return carOwer;
    }

    public void setCarOwer(String carOwer) {
        this.carOwer = carOwer;
    }

    public String getInscomcode() {
        return inscomcode;
    }

    public void setInscomcode(String inscomcode) {
        this.inscomcode = inscomcode;
    }

    public String getAgreementid() {
        return agreementid;
    }

    public void setAgreementid(String agreementid) {
        this.agreementid = agreementid;
    }

    public String getSinglesite() {
        return singlesite;
    }

    public void setSinglesite(String singlesite) {
        this.singlesite = singlesite;
    }

    public String getBuybusitype() {
        return buybusitype;
    }

    public void setBuybusitype(String buybusitype) {
        this.buybusitype = buybusitype;
    }

    public String getInsuredprovince() {
        return insuredprovince;
    }

    public void setInsuredprovince(String insuredprovince) {
        this.insuredprovince = insuredprovince;
    }

    public String getInsuredcity() {
        return insuredcity;
    }

    public void setInsuredcity(String insuredcity) {
        this.insuredcity = insuredcity;
    }
}
