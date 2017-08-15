package com.zzb.model;

import java.util.List;

public class SelectInsuredConfigModel {

	/**
	 * 商业险
	 */
	private List<SelectInsuredConfigBean> buessList;
	/**
	 * 不计免赔
	 */
	private List<SelectInsuredConfigBean> bjmpList;
	/**
	 * 交强险、车船税
	 */
	private List<SelectInsuredConfigBean> vehicleList;
	/**
	 * 商业险折扣率
	 */
	private Double discountRate;
	/**
	 * 交强险、车船税折扣率
	 */
	private Double vehicleDiscountRate;
	/**
	 * 商业险总保费
	 */
	private Double totalamountprice;
	/**
	 * 当前流程状态0为选择投保1报价中2为已投保3已核保
	 */
	private String processstatus;
	
	public Double getVehicleDiscountRate() {
		return vehicleDiscountRate;
	}
	public void setVehicleDiscountRate(Double vehicleDiscountRate) {
		this.vehicleDiscountRate = vehicleDiscountRate;
	}
	public List<SelectInsuredConfigBean> getBuessList() {
		return buessList;
	}
	public void setBuessList(List<SelectInsuredConfigBean> buessList) {
		this.buessList = buessList;
	}
	public List<SelectInsuredConfigBean> getBjmpList() {
		return bjmpList;
	}
	public void setBjmpList(List<SelectInsuredConfigBean> bjmpList) {
		this.bjmpList = bjmpList;
	}
	public List<SelectInsuredConfigBean> getVehicleList() {
		return vehicleList;
	}
	public void setVehicleList(List<SelectInsuredConfigBean> vehicleList) {
		this.vehicleList = vehicleList;
	}
	public Double getDiscountRate() {
		return discountRate;
	}
	public void setDiscountRate(Double discountRate) {
		this.discountRate = discountRate;
	}
	public Double getTotalamountprice() {
		return totalamountprice;
	}
	public void setTotalamountprice(Double totalamountprice) {
		this.totalamountprice = totalamountprice;
	}
	public String getProcessstatus() {
		return processstatus;
	}
	public void setProcessstatus(String processstatus) {
		this.processstatus = processstatus;
	}
	
}
