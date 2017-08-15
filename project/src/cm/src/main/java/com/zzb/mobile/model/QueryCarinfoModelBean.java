package com.zzb.mobile.model;

public class QueryCarinfoModelBean {

	/**
	 * 品牌型号
	 */
	private String brandname;
	/**
	 * 发动机号
	 */
	private String engineno;
	/**
	 * 车辆识别代码
	 */
	private String vincode;
	/**
	 * 是否过户车
	 */
	private String isTransfercar;
	/**
	 * 过户日期
	 */
	private String transferdate;
	/**
	 * 车辆初登日期
	 */
	private String registdate;
	/**
	 * 排气量
	 */
	private Double displacement;
	/**
	 * 核定载人数
	 */
	private int seat;
	/**
	 * 车价选择
	 */
	private int carprice;
	/**
	 * 指定车价（投保车价）
	 */
	private int policycarprice;
	/**
	 * 所属性质
	 */
	private int property;
	/**
	 * 车辆性质
	 */
	private int carproperty;
	/**
	 * 核定载质量
	 */
	private int unwrtweight;
	/**
	 * 整备质量
	 */
	private int fullweight;
	
	public String getBrandname() {
		return brandname;
	}
	public void setBrandname(String brandname) {
		this.brandname = brandname;
	}
	public String getEngineno() {
		return engineno;
	}
	public void setEngineno(String engineno) {
		this.engineno = engineno;
	}
	public String getVincode() {
		return vincode;
	}
	public void setVincode(String vincode) {
		this.vincode = vincode;
	}
	public String getIsTransfercar() {
		return isTransfercar;
	}
	public void setIsTransfercar(String isTransfercar) {
		this.isTransfercar = isTransfercar;
	}
	public String getTransferdate() {
		return transferdate;
	}
	public void setTransferdate(String transferdate) {
		this.transferdate = transferdate;
	}
	public String getRegistdate() {
		return registdate;
	}
	public void setRegistdate(String registdate) {
		this.registdate = registdate;
	}
	public Double getDisplacement() {
		return displacement;
	}
	public void setDisplacement(Double displacement) {
		this.displacement = displacement;
	}
	public int getSeat() {
		return seat;
	}
	public void setSeat(int seat) {
		this.seat = seat;
	}
	public int getCarprice() {
		return carprice;
	}
	public void setCarprice(int carprice) {
		this.carprice = carprice;
	}
	public int getPolicycarprice() {
		return policycarprice;
	}
	public void setPolicycarprice(int policycarprice) {
		this.policycarprice = policycarprice;
	}
	public int getProperty() {
		return property;
	}
	public void setProperty(int property) {
		this.property = property;
	}
	public int getCarproperty() {
		return carproperty;
	}
	public void setCarproperty(int carproperty) {
		this.carproperty = carproperty;
	}
	public int getUnwrtweight() {
		return unwrtweight;
	}
	public void setUnwrtweight(int unwrtweight) {
		this.unwrtweight = unwrtweight;
	}
	public int getFullweight() {
		return fullweight;
	}
	public void setFullweight(int fullweight) {
		this.fullweight = fullweight;
	}
	
	
}
