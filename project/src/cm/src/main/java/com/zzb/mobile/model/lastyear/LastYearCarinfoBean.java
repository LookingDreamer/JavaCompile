package com.zzb.mobile.model.lastyear;

import java.io.Serializable;

public class LastYearCarinfoBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 车牌号
	 */
	private String vehicleno;
	/**
	 * 品牌型号
	 */
	private String vehiclecode;
	/**
	 * 排气量
	 */
	private String displacement;
	/**
	 * 核定载人数 (座位数)
	 */
	private String seatcount;
	/**
	 * 车价选择 （新车购置价）
	 */
	private String price; 
	/**
	 * 所属性质 车辆性质 (使用性质)
	 */
	private String usingnature; 
	/**
	 * 核定载质量
	 */
	private String modelLoads;
	/**
	 *  整备质量
	 */
	private String fullWeight;
	/**
	 * 发动机号
	 */
	private String engineno;
	/**
	 * 车辆识别代号 (车架号)
	 */
	private String vehicleframeno;
	/**
	 * 是否过户车 过户日期
	 */
	private String chgownerflag;
	/**
	 *  车辆初登日期
	 */
	private String registerdate;
	/**
	 * 行驶区域
	 */
	private String area;
	/**
	 * 商业险开始时间
	 */
	private String startdate;
	/**
	 * 商业险结束时间
	 */
	private String enddate;
	/**
	 * 交强险开始时间
	 */
	private String jqstartdate;
	/**
	 * 交强险结束时间
	 */
	private String jqenddate;

	public String getVehicleno() {
		return vehicleno;
	}

	public void setVehicleno(String vehicleno) {
		this.vehicleno = vehicleno;
	}

	public String getVehiclecode() {
		return vehiclecode;
	}

	public void setVehiclecode(String vehiclecode) {
		this.vehiclecode = vehiclecode;
	}

	public String getDisplacement() {
		return displacement;
	}

	public void setDisplacement(String displacement) {
		this.displacement = displacement;
	}

	public String getSeatcount() {
		return seatcount;
	}

	public void setSeatcount(String seatcount) {
		this.seatcount = seatcount;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getUsingnature() {
		return usingnature;
	}

	public void setUsingnature(String usingnature) {
		this.usingnature = usingnature;
	}

	public String getModelLoads() {
		return modelLoads;
	}

	public void setModelLoads(String modelLoads) {
		this.modelLoads = modelLoads;
	}

	public String getFullWeight() {
		return fullWeight;
	}

	public void setFullWeight(String fullWeight) {
		this.fullWeight = fullWeight;
	}

	public String getEngineno() {
		return engineno;
	}

	public void setEngineno(String engineno) {
		this.engineno = engineno;
	}

	public String getVehicleframeno() {
		return vehicleframeno;
	}

	public void setVehicleframeno(String vehicleframeno) {
		this.vehicleframeno = vehicleframeno;
	}

	public String getChgownerflag() {
		return chgownerflag;
	}

	public void setChgownerflag(String chgownerflag) {
		this.chgownerflag = chgownerflag;
	}

	public String getRegisterdate() {
		return registerdate;
	}

	public void setRegisterdate(String registerdate) {
		this.registerdate = registerdate;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getStartdate() {
		return startdate;
	}

	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}

	public String getEnddate() {
		return enddate;
	}

	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}

	public String getJqstartdate() {
		return jqstartdate;
	}

	public void setJqstartdate(String jqstartdate) {
		this.jqstartdate = jqstartdate;
	}

	public String getJqenddate() {
		return jqenddate;
	}

	public void setJqenddate(String jqenddate) {
		this.jqenddate = jqenddate;
	}
}
