package com.zzb.cm.controller.vo;

import java.util.List;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;
import com.zzb.cm.entity.INSBInvoiceinfo;

public class OtherInformationVO extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;
	/*
	 * 商业险生效时间
	 */
	private String businessstartdate;
	/*
	 * 交强险生效时间
	 */
	private String compulsorystartdate;
	/*
	 * 商业险结束时间
	 */
	private String businessenddate;
	/*
	 * 交强险结束时间
	 */
	private String compulsoryenddate;
	/*
	 * 上年投保公司code
	 */
	private String preinscode;
	/*
	 * 是否指定驾驶人
	 */
	private String pointspecifydriver;
	/*
	 * 指定的驾驶人
	 */
	private String specifydriver;
	/*
	 * 车辆信息id
	 */
	private String carinfoId;
	/*
	 * 任务id
	 */
	private String taskid;
	/*
	 * 保险公司code
	 */
	private String inscomcode;
	/*
	 * 出单网点code
	 */
	private String deptCode;
	/*
	 * 01-续保,02-转保,03-新保,10-异地(客户忠诚度)
	 */
	private String customerinsurecode;
	// 无赔款不浮动原因
	private String loyaltyreasons;
	// 平台商业险出险次数
	private String commercialtimes;
	// 平台交强险出险次数
	private String compulsorytimes;
	// platform之值则表示平台信息在人工环节的修改请求
	private String infoType;

	public String getLoyaltyreasons() {
		return loyaltyreasons;
	}

	public void setLoyaltyreasons(String loyaltyreasons) {
		this.loyaltyreasons = loyaltyreasons;
	}

	public String getCommercialtimes() {
		return commercialtimes;
	}

	public void setCommercialtimes(String commercialtimes) {
		this.commercialtimes = commercialtimes;
	}

	public String getCompulsorytimes() {
		return compulsorytimes;
	}

	public void setCompulsorytimes(String compulsorytimes) {
		this.compulsorytimes = compulsorytimes;
	}

	public String getInfoType() {
		return infoType;
	}

	public void setInfoType(String infoType) {
		this.infoType = infoType;
	}

	/*
	 * 驾驶人信息
	 */
	private List<INSBPersonVO> driversInfo;

	public String getBusinessstartdate() {
		return businessstartdate;
	}

	public void setBusinessstartdate(String businessstartdate) {
		this.businessstartdate = businessstartdate;
	}

	public String getCompulsorystartdate() {
		return compulsorystartdate;
	}

	public void setCompulsorystartdate(String compulsorystartdate) {
		this.compulsorystartdate = compulsorystartdate;
	}

	public String getBusinessenddate() {
		return businessenddate;
	}

	public void setBusinessenddate(String businessenddate) {
		this.businessenddate = businessenddate;
	}

	public String getCompulsoryenddate() {
		return compulsoryenddate;
	}

	public void setCompulsoryenddate(String compulsoryenddate) {
		this.compulsoryenddate = compulsoryenddate;
	}

	public String getPreinscode() {
		return preinscode;
	}

	public void setPreinscode(String preinscode) {
		this.preinscode = preinscode;
	}

	public String getPointspecifydriver() {
		return pointspecifydriver;
	}

	public void setPointspecifydriver(String pointspecifydriver) {
		this.pointspecifydriver = pointspecifydriver;
	}

	public String getSpecifydriver() {
		return specifydriver;
	}

	public void setSpecifydriver(String specifydriver) {
		this.specifydriver = specifydriver;
	}

	public String getCarinfoId() {
		return carinfoId;
	}

	public void setCarinfoId(String carinfoId) {
		this.carinfoId = carinfoId;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public List<INSBPersonVO> getDriversInfo() {
		return driversInfo;
	}

	public void setDriversInfo(List<INSBPersonVO> driversInfo) {
		this.driversInfo = driversInfo;
	}

	public String getInscomcode() {
		return inscomcode;
	}

	public void setInscomcode(String inscomcode) {
		this.inscomcode = inscomcode;
	}

	public String getDeptCode() {
		return deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	public String getCustomerinsurecode() {
		return customerinsurecode;
	}

	public void setCustomerinsurecode(String customerinsurecode) {
		this.customerinsurecode = customerinsurecode;
	}

	public OtherInformationVO() {
		super();
	}

}
