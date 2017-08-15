package com.zzb.cm.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;


public class INSBRulequerycarinfo extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

	/**
	 * 任务id
	 */
	private String taskid;

	/**
	 * 车牌
	 */
	private String licenseno;

	/**
	 * 发动机
	 */
	private String engineno;

	/**
	 * vin码
	 */
	private String vinno;

	/**
	 * 初登日期
	 */
	private String enrolldate;

	/**
	 * 品牌名称
	 */
	private String carbrandname;

	/**
	 * 新车购置价
	 */
	private Double price;

	/**
	 * 新车购置价(含税)
	 */
	private Double taxprice;

	/**
	 * 类比价
	 */
	private Double analogyprice;

	/**
	 * 类比价(含税)
	 */
	private Double analogytaxprice;

	/**
	 * 上市年份
	 */
	private String carmodeldate;

	/**
	 * 核定载客
	 */
	private Integer seatcnt;

	/**
	 * 核定载质量
	 */
	private Double modelload;

	/**
	 * 整备质量
	 */
	private Double fullload;

	/**
	 * 排气量
	 */
	private Double displacement;

	/**
	 * 行业车型编码
	 */
	private String trademodelcode;

	/**
	 * 自主核保系数
	 */
	private Double selfinsurerate;

	/**
	 * 自主渠道系数
	 */
	private Double selfchannelrate;

	/**
	 * NCD系数
	 */
	private Double ncdrate;

	/**
	 * 车辆种类
	 */
	private String vehicletype;

	/**
	 * 机动车车损纯风险基准保费
	 */
	private Double basicriskpremium;

	/**
	 * 查询公司名称
	 */
	private String insureco;

	/**
	 * 车型编码
	 */
	private String modelcode;
	
	/**
	 * 精灵进行交管信息查询的公司
	 *
	 */
	private String supplierid;

	public String getSupplierid() {
		return supplierid;
	}

	public void setSupplierid(String supplierid) {
		this.supplierid = supplierid;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getLicenseno() {
		return licenseno;
	}

	public void setLicenseno(String licenseno) {
		this.licenseno = licenseno;
	}

	public String getEngineno() {
		return engineno;
	}

	public void setEngineno(String engineno) {
		this.engineno = engineno;
	}

	public String getVinno() {
		return vinno;
	}

	public void setVinno(String vinno) {
		this.vinno = vinno;
	}

	public String getEnrolldate() {
		return enrolldate;
	}

	public void setEnrolldate(String enrolldate) {
		this.enrolldate = enrolldate;
	}

	public String getCarbrandname() {
		return carbrandname;
	}

	public void setCarbrandname(String carbrandname) {
		this.carbrandname = carbrandname;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Double getTaxprice() {
		return taxprice;
	}

	public void setTaxprice(Double taxprice) {
		this.taxprice = taxprice;
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

	public String getCarmodeldate() {
		return carmodeldate;
	}

	public void setCarmodeldate(String carmodeldate) {
		this.carmodeldate = carmodeldate;
	}

	public Integer getSeatcnt() {
		return seatcnt;
	}

	public void setSeatcnt(Integer seatcnt) {
		this.seatcnt = seatcnt;
	}

	public Double getModelload() {
		return modelload;
	}

	public void setModelload(Double modelload) {
		this.modelload = modelload;
	}

	public Double getFullload() {
		return fullload;
	}

	public void setFullload(Double fullload) {
		this.fullload = fullload;
	}

	public Double getDisplacement() {
		return displacement;
	}

	public void setDisplacement(Double displacement) {
		this.displacement = displacement;
	}

	public String getTrademodelcode() {
		return trademodelcode;
	}

	public void setTrademodelcode(String trademodelcode) {
		this.trademodelcode = trademodelcode;
	}

	public Double getSelfinsurerate() {
		return selfinsurerate;
	}

	public void setSelfinsurerate(Double selfinsurerate) {
		this.selfinsurerate = selfinsurerate;
	}

	public Double getSelfchannelrate() {
		return selfchannelrate;
	}

	public void setSelfchannelrate(Double selfchannelrate) {
		this.selfchannelrate = selfchannelrate;
	}

	public Double getNcdrate() {
		return ncdrate;
	}

	public void setNcdrate(Double ncdrate) {
		this.ncdrate = ncdrate;
	}

	public String getVehicletype() {
		return vehicletype;
	}

	public void setVehicletype(String vehicletype) {
		this.vehicletype = vehicletype;
	}

	public Double getBasicriskpremium() {
		return basicriskpremium;
	}

	public void setBasicriskpremium(Double basicriskpremium) {
		this.basicriskpremium = basicriskpremium;
	}

	public String getInsureco() {
		return insureco;
	}

	public void setInsureco(String insureco) {
		this.insureco = insureco;
	}

	public String getModelcode() {
		return modelcode;
	}

	public void setModelcode(String modelcode) {
		this.modelcode = modelcode;
	}

}