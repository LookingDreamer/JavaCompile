package com.zzb.mobile.model;

public class LastInsuredRenewaByCarModel {
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
	 * 商业险配置是否与上一年一致 0不 一致 1一致
	 */
	private String flag;
    /**
     * 上年投保公司
     * @return
     */
    private String lastInsuredCom;
    /**
     * 上年投保公司编码
     * @return
     */
    private String lastInsuredComcode;
    /**
     * 代理人id
     */
    private String agentid;

	
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
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

    public String getLastInsuredCom() {
        return lastInsuredCom;
    }

    public void setLastInsuredCom(String lastInsuredCom) {
        this.lastInsuredCom = lastInsuredCom;
    }

    public String getLastInsuredComcode() {
        return lastInsuredComcode;
    }

    public void setLastInsuredComcode(String lastInsuredComcode) {
        this.lastInsuredComcode = lastInsuredComcode;
    }

    public String getAgentid() {
        return agentid;
    }

    public void setAgentid(String agentid) {
        this.agentid = agentid;
    }
}
