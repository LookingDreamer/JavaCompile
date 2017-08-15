package com.zzb.conf.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;

import java.util.Date;

public class INSBChannelcarinfo extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

	/**
	 * 地区
	 */
	private String drivingarea; 

	/**
	 * 车牌号码
	 */
	private String carlicenseno;

	@Override
	public String toString() {
		return  drivingarea
				+ "," + carlicenseno + ","
				+ customername + "," + address + ","
				+ phonenumber + "," + vehiclebrand
				+ "," + vehicletype + "," + registdate
				+ "," + engineno + "," + chassiso
				+ "," + batchcode + "," + channelcode
				+ ";\\r\\n";
	}

	/**
	 * 客户姓名
	 */
	private String customername;

	/**
	 * 地址
	 */
	private String address;

	/**
	 * 手机号码
	 */
	private String phonenumber;

	/**
	 * 车辆品牌
	 */
	private String vehiclebrand;

	/**
	 * 车辆型号
	 */
	private String vehicletype;

	/**
	 * 初次登记年月
	 */
	private Date registdate;

	/**
	 * 发动机号
	 */
	private String engineno;

	/**
	 * 车架号
	 */
	private String chassiso;
	
	/**
	 * 批次号
	 */
	private String batchcode;
	
	/**
	 * 渠道编码
	 */
	private String channelcode;

	public String getBatchcode() {
		return batchcode;
	}

	public void setBatchcode(String batchcode) {
		this.batchcode = batchcode;
	}

	public String getChannelcode() {
		return channelcode;
	}

	public void setChannelcode(String channelcode) {
		this.channelcode = channelcode;
	}

	public String getDrivingarea() {
		return drivingarea;
	}

	public void setDrivingarea(String drivingarea) {
		this.drivingarea = drivingarea;
	}

	public String getCarlicenseno() {
		return carlicenseno;
	}

	public void setCarlicenseno(String carlicenseno) {
		this.carlicenseno = carlicenseno;
	}

	public String getCustomername() {
		return customername;
	}

	public void setCustomername(String customername) {
		this.customername = customername;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhonenumber() {
		return phonenumber;
	}

	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}

	public String getVehiclebrand() {
		return vehiclebrand;
	}

	public void setVehiclebrand(String vehiclebrand) {
		this.vehiclebrand = vehiclebrand;
	}

	public String getVehicletype() {
		return vehicletype;
	}

	public void setVehicletype(String vehicletype) {
		this.vehicletype = vehicletype;
	}

	public Date getRegistdate() {
		return registdate;
	}

	public void setRegistdate(Date registdate) {
		this.registdate = registdate;
	}

	public String getEngineno() {
		return engineno;
	}

	public void setEngineno(String engineno) {
		this.engineno = engineno;
	}

	public String getChassiso() {
		return chassiso;
	}

	public void setChassiso(String chassiso) {
		this.chassiso = chassiso;
	}

}