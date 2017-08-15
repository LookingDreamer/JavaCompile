package com.zzb.mobile.model.lastinsured;

import java.io.Serializable;


public class CarModel implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String vehicleno; // varchar(20) //车牌号
	String supplierId;// varchar(30)// 保险公司
	String carmodelCode;// varchar(50)//车辆模型编码
	String vehiclecode; // varchar(20)//品牌型号
	String kindname;// varchar(30)//险别名称
	String vehiclename;// varchar(50)//车型全名
	String vehicletype;// varchar(50)//车辆种类
	String syvehicletype;// varchar(10)//商业险车辆类型
	String jqvehicletype;// varchar(10)//交强险车辆类型
	String manufacturer;// varchar(10)//产地来源
	String brandname;// varchar(100)//车辆品牌
	String familyid;// varchar(100)//车系代号
	String familyname;// varchar(100)//车系全名
	String factoryname;// varchar(100)//生产厂商
	String displacement;// varchar(10)//排气量
	String fullweight;// varchar(10)//整备质量
	String modelLoads;// varchar(10)//核定载质量
	String yearstyle;// varchar(10)//年款
	String gearbox;// varchar(10)//变速器
	String seat;// varchar(10)//座位数
	Double taxprice;// Double//新车购置价（含税）
	Double price;// Double//新车购置价（不含税）
	String gearboxtype;// varchar(10)//驱动形式
	String maketdate;// varchar(20)//上市年月
	String configname;// varchar(50)//配置名称
	Double analogyprice;// Double//类比价
	Double analogytaxprice;// Double//类比价含税
	String serchtimes;// varchar(10)//查询次数
	String carmodelsource;// varchar(10)//车辆模型来源
	String type;// varchar(10)//车辆模型类型
	
	String syvehicletypename;// varchar(20)//商业险车辆名称
	String jqvehicletypename;// varchar(20)//交强险车辆名称
	String vehicletypecode;//varchar(20)//车辆类型编码
	
	String vehicleId;//varchar(96)//精友车型ID
	public String getVehicleno() {
		return vehicleno;
	}
	public void setVehicleno(String vehicleno) {
		this.vehicleno = vehicleno;
	}
	public String getSupplierId() {
		return supplierId;
	}
	public void setSupplierId(String supplierId) {
		this.supplierId = supplierId;
	}
	public String getCarmodelCode() {
		return carmodelCode;
	}
	public void setCarmodelCode(String carmodelCode) {
		this.carmodelCode = carmodelCode;
	}
	public String getVehiclecode() {
		return vehiclecode;
	}
	public void setVehiclecode(String vehiclecode) {
		this.vehiclecode = vehiclecode;
	}
	public String getKindname() {
		return kindname;
	}
	public void setKindname(String kindname) {
		this.kindname = kindname;
	}
	public String getVehiclename() {
		return vehiclename;
	}
	public void setVehiclename(String vehiclename) {
		this.vehiclename = vehiclename;
	}
	public String getVehicletype() {
		return vehicletype;
	}
	public void setVehicletype(String vehicletype) {
		this.vehicletype = vehicletype;
	}
	public String getSyvehicletype() {
		return syvehicletype;
	}
	public void setSyvehicletype(String syvehicletype) {
		this.syvehicletype = syvehicletype;
	}
	public String getJqvehicletype() {
		return jqvehicletype;
	}
	public void setJqvehicletype(String jqvehicletype) {
		this.jqvehicletype = jqvehicletype;
	}
	public String getManufacturer() {
		return manufacturer;
	}
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
	public String getBrandname() {
		return brandname;
	}
	public void setBrandname(String brandname) {
		this.brandname = brandname;
	}
	public String getFamilyid() {
		return familyid;
	}
	public void setFamilyid(String familyid) {
		this.familyid = familyid;
	}
	public String getFamilyname() {
		return familyname;
	}
	public void setFamilyname(String familyname) {
		this.familyname = familyname;
	}
	public String getFactoryname() {
		return factoryname;
	}
	public void setFactoryname(String factoryname) {
		this.factoryname = factoryname;
	}
	public String getDisplacement() {
		return displacement;
	}
	public void setDisplacement(String displacement) {
		this.displacement = displacement;
	}
	public String getFullweight() {
		return fullweight;
	}
	public void setFullweight(String fullweight) {
		this.fullweight = fullweight;
	}
	public String getModelLoads() {
		return modelLoads;
	}
	public void setModelLoads(String modelLoads) {
		this.modelLoads = modelLoads;
	}
	public String getYearstyle() {
		return yearstyle;
	}
	public void setYearstyle(String yearstyle) {
		this.yearstyle = yearstyle;
	}
	public String getGearbox() {
		return gearbox;
	}
	public void setGearbox(String gearbox) {
		this.gearbox = gearbox;
	}
	public String getSeat() {
		return seat;
	}
	public void setSeat(String seat) {
		this.seat = seat;
	}
	public Double getTaxprice() {
		return taxprice;
	}
	public void setTaxprice(Double taxprice) {
		this.taxprice = taxprice;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public String getGearboxtype() {
		return gearboxtype;
	}
	public void setGearboxtype(String gearboxtype) {
		this.gearboxtype = gearboxtype;
	}
	public String getMaketdate() {
		return maketdate;
	}
	public void setMaketdate(String maketdate) {
		this.maketdate = maketdate;
	}
	public String getConfigname() {
		return configname;
	}
	public void setConfigname(String configname) {
		this.configname = configname;
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
	public String getSerchtimes() {
		return serchtimes;
	}
	public void setSerchtimes(String serchtimes) {
		this.serchtimes = serchtimes;
	}
	public String getCarmodelsource() {
		return carmodelsource;
	}
	public void setCarmodelsource(String carmodelsource) {
		this.carmodelsource = carmodelsource;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public String getSyvehicletypename() {
		return syvehicletypename;
	}
	public void setSyvehicletypename(String syvehicletypename) {
		this.syvehicletypename = syvehicletypename;
	}
	public String getJqvehicletypename() {
		return jqvehicletypename;
	}
	public void setJqvehicletypename(String jqvehicletypename) {
		this.jqvehicletypename = jqvehicletypename;
	}
	public String getVehicletypecode() {
		return vehicletypecode;
	}
	public void setVehicletypecode(String vehicletypecode) {
		this.vehicletypecode = vehicletypecode;
	}
	
	public String getVehicleId() {
		return vehicleId;
	}
	public void setVehicleId(String vehicleId) {
		this.vehicleId = vehicleId;
	}
	@Override
	public String toString() {
		return "CarModel [vehicleno=" + vehicleno + ", supplierId="
				+ supplierId + ", carmodelCode=" + carmodelCode
				+ ", vehiclecode=" + vehiclecode + ", kindname=" + kindname
				+ ", vehiclename=" + vehiclename + ", vehicletype="
				+ vehicletype + ", syvehicletype=" + syvehicletype
				+ ", jqvehicletype=" + jqvehicletype + ", manufacturer="
				+ manufacturer + ", brandname=" + brandname + ", familyid="
				+ familyid + ", familyname=" + familyname + ", factoryname="
				+ factoryname + ", displacement=" + displacement
				+ ", fullweight=" + fullweight + ", modelLoads=" + modelLoads
				+ ", yearstyle=" + yearstyle + ", gearbox=" + gearbox
				+ ", seat=" + seat + ", taxprice=" + taxprice + ", price="
				+ price + ", gearboxtype=" + gearboxtype + ", maketdate="
				+ maketdate + ", configname=" + configname + ", analogyprice="
				+ analogyprice + ", analogytaxprice=" + analogytaxprice
				+ ", serchtimes=" + serchtimes + ", carmodelsource="
				+ carmodelsource + ", type=" + type + "]";
	}


/*	public String getVehicletype() {
		return vehicletype;
	}

	public void setVehicletype(String vehicletype) {
		this.vehicletype = vehicletype;
	}

	public String getVehicleNo() {
		return vehicleNo;
	}

	public void setVehicleNo(String vehicleNo) {
		this.vehicleNo = vehicleNo;
	}

	public String getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(String supplierId) {
		this.supplierId = supplierId;
	}

	public String getCarmodelCode() {
		return carmodelCode;
	}

	public void setCarmodelCode(String carmodelCode) {
		this.carmodelCode = carmodelCode;
	}

	public String getVehicleCode() {
		return vehicleCode;
	}

	public void setVehicleCode(String vehicleCode) {
		this.vehicleCode = vehicleCode;
	}

	public String getVehicleName() {
		return vehicleName;
	}

	public void setVehicleName(String vehicleName) {
		this.vehicleName = vehicleName;
	}

	public String getSyvehicleType() {
		return syvehicleType;
	}

	public void setSyvehicleType(String syvehicleType) {
		this.syvehicleType = syvehicleType;
	}

	public String getJqvehicleType() {
		return jqvehicleType;
	}

	public void setJqvehicleType(String jqvehicleType) {
		this.jqvehicleType = jqvehicleType;
	}

	public String getManuFacturer() {
		return manuFacturer;
	}

	public void setManuFacturer(String manuFacturer) {
		this.manuFacturer = manuFacturer;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getFamilyId() {
		return familyId;
	}

	public void setFamilyId(String familyId) {
		this.familyId = familyId;
	}

	public String getFamilyName() {
		return familyName;
	}

	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}

	public String getFactoryName() {
		return factoryName;
	}

	public void setFactoryName(String factoryName) {
		this.factoryName = factoryName;
	}

	public String getDisplacement() {
		return displacement;
	}

	public void setDisplacement(String displacement) {
		this.displacement = displacement;
	}

	public String getFullWeight() {
		return fullWeight;
	}

	public void setFullWeight(String fullWeight) {
		this.fullWeight = fullWeight;
	}

	public String getModelLoads() {
		return modelLoads;
	}

	public void setModelLoads(String modelLoads) {
		this.modelLoads = modelLoads;
	}

	public String getYearStyle() {
		return yearStyle;
	}

	public void setYearStyle(String yearStyle) {
		this.yearStyle = yearStyle;
	}

	public String getGearBox() {
		return gearBox;
	}

	public void setGearBox(String gearBox) {
		this.gearBox = gearBox;
	}

	public String getSeat() {
		return seat;
	}

	public void setSeat(String seat) {
		this.seat = seat;
	}

	public Double getTaxPrice() {
		return taxPrice;
	}

	public void setTaxPrice(Double taxPrice) {
		this.taxPrice = taxPrice;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getGearBoxType() {
		return gearBoxType;
	}

	public void setGearBoxType(String gearBoxType) {
		this.gearBoxType = gearBoxType;
	}

	public String getMaketDate() {
		return maketDate;
	}

	public void setMaketDate(String maketDate) {
		this.maketDate = maketDate;
	}

	public String getConfigName() {
		return configName;
	}

	public void setConfigName(String configName) {
		this.configName = configName;
	}

	public Double getAnalogyPrice() {
		return analogyPrice;
	}

	public void setAnalogyPrice(Double analogyPrice) {
		this.analogyPrice = analogyPrice;
	}

	public Double getAnalogyTaxPrice() {
		return analogyTaxPrice;
	}

	public void setAnalogyTaxPrice(Double analogyTaxPrice) {
		this.analogyTaxPrice = analogyTaxPrice;
	}

	public String getCarmodelsource() {
		return carmodelsource;
	}

	public void setCarmodelsource(String carmodelsource) {
		this.carmodelsource = carmodelsource;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSerchtimes() {
		return serchtimes;
	}

	public void setSerchtimes(String serchtimes) {
		this.serchtimes = serchtimes;
	}*/
	
}
