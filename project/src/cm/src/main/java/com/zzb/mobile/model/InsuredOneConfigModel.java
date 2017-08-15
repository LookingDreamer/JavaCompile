package com.zzb.mobile.model;

import java.util.List;

public class InsuredOneConfigModel {
	/**
	 * 实例id
	 */
	private String processinstanceid;
	/**
	 * 商业险
	 */
	private List<BusinessRisks> businessRisks;
	/**
	 * 交强险
	 */
	private List<StrongRisk> strongRisk;
	/**
	 * 备注
	 */
	private String remark;
	/**
     * 备注类型
     */
    private String remarkcode;
	/**
	 * 投保车价
	 */
	private Double policycarprice;
	/**
	 * 选择的保险配置的key值
	 */
	private String plankey;
	/**
	 * 供应商id
	 */
	private String pid;
	/**
	 * 操作人
	 */
	private String operator;
	/**
	 *  新增设备险  新增设备
	 */
	private List<NewEquipmentInsBean> equipmentInsBeans;
	/**
	 * 修理期间费用补偿险 天数
	 */
	private String compensationdays;
    /**
     * 保险配置是否与上年一致(1：是，其他值：否)
     */
	private String sameaslastyear;
	
	public List<NewEquipmentInsBean> getEquipmentInsBeans() {
		return equipmentInsBeans;
	}
	public void setEquipmentInsBeans(List<NewEquipmentInsBean> equipmentInsBeans) {
		this.equipmentInsBeans = equipmentInsBeans;
	}
	public String getCompensationdays() {
		return compensationdays;
	}
	public void setCompensationdays(String compensationdays) {
		this.compensationdays = compensationdays;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getProcessinstanceid() {
		return processinstanceid;
	}
	public void setProcessinstanceid(String processinstanceid) {
		this.processinstanceid = processinstanceid;
	}
	public List<BusinessRisks> getBusinessRisks() {
		return businessRisks;
	}
	public void setBusinessRisks(List<BusinessRisks> businessRisks) {
		this.businessRisks = businessRisks;
	}
	public List<StrongRisk> getStrongRisk() {
		return strongRisk;
	}
	public void setStrongRisk(List<StrongRisk> strongRisk) {
		this.strongRisk = strongRisk;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}

    public String getRemarkcode() {
        return remarkcode;
    }

    public void setRemarkcode(String remarkcode) {
        this.remarkcode = remarkcode;
    }

	public Double getPolicycarprice() {
		return policycarprice;
	}
	public void setPolicycarprice(Double policycarprice) {
		this.policycarprice = policycarprice;
	}
	public String getPlankey() {
		return plankey;
	}
	public void setPlankey(String plankey) {
		this.plankey = plankey;
	}
	public String getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = pid;
	}

    public String getSameaslastyear() {
        return sameaslastyear;
    }

    public void setSameaslastyear(String sameaslastyear) {
        this.sameaslastyear = sameaslastyear;
    }
}
