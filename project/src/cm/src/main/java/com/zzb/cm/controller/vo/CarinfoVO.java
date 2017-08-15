package com.zzb.cm.controller.vo;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;

public class CarinfoVO extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

	/**
	 * 任务id
	 */
	private String taskid;
	
	/**
	 * 保险公司code
	 */
	private String inscomcode;

	/**
	 * 车辆识别代号
	 */
	private String vincode;

	/**
	 * 
	 */
	private String engineno;

	/**
	 * 车辆初始登记日期
	 */
	private String registdate;


	/**
	 * 过户日期
	 */
	private String transferdate;

	/**
	 * 过户车
	 */
	private String isTransfercar;

	/**
	 * 行驶区域
	 */
	private String drivingarea;

	/**
	 * 平均行驶里程
	 */
	private String mileage;

	/**
	 * 所属性质
	 */
	private String property;

	/**
	 * 车辆性质
	 */
	private String carproperty;

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

	public String getVincode() {
		return vincode;
	}

	public void setVincode(String vincode) {
		this.vincode = vincode;
	}

	public String getEngineno() {
		return engineno;
	}

	public void setEngineno(String engineno) {
		this.engineno = engineno;
	}

	public String getRegistdate() {
		return registdate;
	}

	public void setRegistdate(String registdate) {
		this.registdate = registdate;
	}

	public String getTransferdate() {
		return transferdate;
	}

	public void setTransferdate(String transferdate) {
		this.transferdate = transferdate;
	}

	public String getIsTransfercar() {
		return isTransfercar;
	}

	public void setIsTransfercar(String isTransfercar) {
		this.isTransfercar = isTransfercar;
	}

	public String getDrivingarea() {
		return drivingarea;
	}

	public void setDrivingarea(String drivingarea) {
		this.drivingarea = drivingarea;
	}

	public String getMileage() {
		return mileage;
	}

	public void setMileage(String mileage) {
		this.mileage = mileage;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public String getCarproperty() {
		return carproperty;
	}

	public void setCarproperty(String carproperty) {
		this.carproperty = carproperty;
	}

	public CarinfoVO() {
		super();
	}


}