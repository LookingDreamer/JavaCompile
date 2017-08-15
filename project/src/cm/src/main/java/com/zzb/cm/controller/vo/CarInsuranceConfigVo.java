package com.zzb.cm.controller.vo;

import java.util.List;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;

public class CarInsuranceConfigVo  extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;
	
	//任务编号
	private String taskInstanceId;
	
	//所有报价保险公司编号
	private List<String> inscomcodeList;
	
	//编辑的保险公司编号
	private String thisInscomcode;
	
	//编辑的保险公司名称
	private String thisInscomname;
	
	//是否修改到所有单方
	private String edit2AllList;

	//保险配置是否与上年一致
	private String insureconfigsameaslastyear;
	
	//商业险配置信息
	List<CarInsConfigVo> busiKindprice;
	
	//不计免赔配置信息
	List<CarInsConfigVo> notdKindprice;
	
	//交强险车船税配置信息（必须）
	List<CarInsConfigVo> stroKindprice;
	
	//遍历下标
	private String carInsTaskInfoindex;

	//商业险折扣率
	private Double busdiscountrate;

	//交强险折扣率
	private Double strdiscountrate;
	
	//特殊险别标志
	private String specialRiskkindFlag;
	
	//特殊险别value值
	private String specialRiskkindValue;
	
	public String getTaskInstanceId() {
		return taskInstanceId;
	}
	public void setTaskInstanceId(String taskInstanceId) {
		this.taskInstanceId = taskInstanceId;
	}
	public String getThisInscomcode() {
		return thisInscomcode;
	}
	public void setThisInscomcode(String thisInscomcode) {
		this.thisInscomcode = thisInscomcode;
	}
	public String getEdit2AllList() {
		return edit2AllList;
	}
	public void setEdit2AllList(String edit2AllList) {
		this.edit2AllList = edit2AllList;
	}
	public List<String> getInscomcodeList() {
		return inscomcodeList;
	}
	public void setInscomcodeList(List<String> inscomcodeList) {
		this.inscomcodeList = inscomcodeList;
	}
	public String getCarInsTaskInfoindex() {
		return carInsTaskInfoindex;
	}
	public void setCarInsTaskInfoindex(String carInsTaskInfoindex) {
		this.carInsTaskInfoindex = carInsTaskInfoindex;
	}
	public List<CarInsConfigVo> getBusiKindprice() {
		return busiKindprice;
	}
	public void setBusiKindprice(List<CarInsConfigVo> busiKindprice) {
		this.busiKindprice = busiKindprice;
	}
	public List<CarInsConfigVo> getNotdKindprice() {
		return notdKindprice;
	}
	public void setNotdKindprice(List<CarInsConfigVo> notdKindprice) {
		this.notdKindprice = notdKindprice;
	}
	public List<CarInsConfigVo> getStroKindprice() {
		return stroKindprice;
	}
	public void setStroKindprice(List<CarInsConfigVo> stroKindprice) {
		this.stroKindprice = stroKindprice;
	}
	public Double getBusdiscountrate() {
		return busdiscountrate;
	}
	public void setBusdiscountrate(Double busdiscountrate) {
		this.busdiscountrate = busdiscountrate;
	}
	public Double getStrdiscountrate() {
		return strdiscountrate;
	}
	public void setStrdiscountrate(Double strdiscountrate) {
		this.strdiscountrate = strdiscountrate;
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
	public String getThisInscomname() {
		return thisInscomname;
	}
	public void setThisInscomname(String thisInscomname) {
		this.thisInscomname = thisInscomname;
	}
	public CarInsuranceConfigVo() {
		super();
	}

	public String getInsureconfigsameaslastyear() {
		return insureconfigsameaslastyear;
	}

	public void setInsureconfigsameaslastyear(String insureconfigsameaslastyear) {
		this.insureconfigsameaslastyear = insureconfigsameaslastyear;
	}
}
