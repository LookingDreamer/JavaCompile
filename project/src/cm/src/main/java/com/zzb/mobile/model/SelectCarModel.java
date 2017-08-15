package com.zzb.mobile.model;

public class SelectCarModel {
	
	/**
	 * 实例id
	 */
	private String processinstanceid;
	/**
	 * 新车购置价
	 */
	private Double taxPrice;
	/**
	 * 别名
	 */
	private String aliasname;
	/**
	 * 不含税类比价
	 */
	private Double analogyprice;
	/**
	 * 含税类比价
	 */
	private Double analogytaxprice;
	/**
	 * 车型品牌名称
	 */
	private String brandname;
	/**
	 * 排气量
	 */
	private Double displacement;
	/**
	 * 车型系列名称
	 */
	private String familyname;
	/**
	 * 整备质量
	 */
	private Double fullweight;
	/**
	 * 变速箱 自动档  手动挡
	 */
	private String gearbox;
	/**
	 * 载荷
	 */
	private Double loads;
	/**
	 * 上市年份
	 */
	private String listedyear;
	/**
	 * 不含税价
	 */
	private Double price;
	/**
	 * 座位数
	 */
	private Integer seat;
	/**
	 * 车型品牌名称
	 */
	private String standardname;
	/**
	 * 车辆产地类型  国产:0,进口:1,合资:2
	 */
	private String carVehicleOrigin;
	/**
	 * 交管车辆类型
	 */
	private String jgVehicleType;
	/**
	 * 精友车型id
	 */
	private String vehicleid;
	/**
	 * 商业险车辆名称
	 */
	private String syvehicletypename;
	/**
	 * 机动车编码
	 */
	private String syvehicletypecode;
	
	public String getSyvehicletypecode() {
		return syvehicletypecode;
	}

	public void setSyvehicletypecode(String syvehicletypecode) {
		this.syvehicletypecode = syvehicletypecode;
	}

	public String getSyvehicletypename() {
		return syvehicletypename;
	}

	public void setSyvehicletypename(String syvehicletypename) {
		this.syvehicletypename = syvehicletypename;
	}
	public String getVehicleid() {
		return vehicleid;
	}
	public void setVehicleid(String vehicleid) {
		this.vehicleid = vehicleid;
	}
	public String getProcessinstanceid() {
		return processinstanceid;
	}
	public void setProcessinstanceid(String processinstanceid) {
		this.processinstanceid = processinstanceid;
	}
	public Double getTaxPrice() {
		return taxPrice;
	}
	public void setTaxPrice(Double taxPrice) {
		this.taxPrice = taxPrice;
	}
	public String getAliasname() {
		return aliasname;
	}
	public void setAliasname(String aliasname) {
		this.aliasname = aliasname;
	}
	public Double getAnalogyprice() {
		return analogyprice;
	}
	public void setAnalogyprice(Double analogyprice) {
		this.analogyprice = analogyprice;
	}
	public Double getAnalogytaxprice() {
		return analogytaxprice;
	}
	public void setAnalogytaxprice(Double analogytaxprice) {
		this.analogytaxprice = analogytaxprice;
	}
	public String getBrandname() {
		return brandname;
	}
	public void setBrandname(String brandname) {
		this.brandname = brandname;
	}
	public Double getDisplacement() {
		return displacement;
	}
	public void setDisplacement(Double displacement) {
		this.displacement = displacement;
	}
	public String getFamilyname() {
		return familyname;
	}
	public void setFamilyname(String familyname) {
		this.familyname = familyname;
	}
	public Double getFullweight() {
		return fullweight;
	}
	public void setFullweight(Double fullweight) {
		this.fullweight = fullweight;
	}
	public String getGearbox() {
		return gearbox;
	}
	public void setGearbox(String gearbox) {
		this.gearbox = gearbox;
	}
	public Double getLoads() {
		return loads;
	}
	public void setLoads(Double loads) {
		this.loads = loads;
	}
	public String getListedyear() {
		return listedyear;
	}
	public void setListedyear(String listedyear) {
		this.listedyear = listedyear;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public Integer getSeat() {
		return seat;
	}
	public void setSeat(Integer seat) {
		this.seat = seat;
	}
	public String getStandardname() {
		return standardname;
	}
	public void setStandardname(String standardname) {
		this.standardname = standardname;
	}
	public String getCarVehicleOrigin() {
		return carVehicleOrigin;
	}
	public void setCarVehicleOrigin(String carVehicleOrigin) {
		this.carVehicleOrigin = carVehicleOrigin;
	}
	public String getJgVehicleType() {
		return jgVehicleType;
	}
	public void setJgVehicleType(String jgVehicleType) {
		this.jgVehicleType = jgVehicleType;
	}
	
}
