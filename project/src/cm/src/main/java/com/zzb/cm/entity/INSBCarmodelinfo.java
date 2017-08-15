package com.zzb.cm.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;


public class INSBCarmodelinfo extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

	/**
	 * 车辆人员主表id
	 */
	private String carinfoid;

	/**
	 * 品牌名称
	 */
	private String brandname;

	/**
	 * 车辆信息名称
	 */
	private String standardname;

	/**
	 * 车辆信息描述
	 */
	private String standardfullname;

	/**
	 * 汽车系列
	 */
	private String familyname;

	/**
	 * 价格新车购置价--不含税价
	 */
	private Double price;

	/**
	 * 税额含税价--含税价
	 */
	private Double taxprice;

	/**
	 * 类比价格--不含税类比价
	 */
	private Double analogyprice;

	/**
	 * 类比税额--含税类比价
	 */
	private Double analogytaxprice;

	/**
	 * 生产厂家
	 */
	private String factoryname;

	/**
	 * 商业类型
	 */
	private String commercetype;

	/**
	 * 车价选择
	 */
	private String carprice;

	/**
	 * 承载人数
	 */
	private Integer seat;

	/**
	 * 发动机排量
	 */
	private Double displacement;

	/**
	 * 别名
	 */
	private String aliasname;

	/**
	 * 变速箱
	 */
	private String gearbox;

	/**
	 * 载荷
	 */
	private Double loads;

	/**
	 * 核定载重量
	 */
	private Double unwrtweight;

	/**
	 * 整备质量
	 */
	private Double fullweight;

	/**
	 * 安全气袋编号
	 */
	private String airbagnum;

	/**
	 * abs标志位
	 */
	private String absflag;

	/**
	 * 
	 */
	private String listedyear;

	/**
	 * 
	 */
	private String cardeploy;

	/**
	 * 
	 */
	private Double policycarprice;

	/**
	 * 0 标准车型 1 非标准车型
	 */
	private String isstandardcar;
	
	/**
	 * 产地类型
	 */
	private Integer carVehicleOrigin;
	
	/**
	 * 人保车型名称
	 */
	private String rbCarModelName;
	
	/**
	 * 精友车型名称
	 */
	private String jyCarModelName;
	
	/**
	 * 内部车型code
	 */
	private String bwCode;
	
	/**
	 * 精友车型code
	 */
	private String jyCode;
	
	/**
	 * 人保车型code
	 */
	private String rbCode;
	
	/**
	 * 是否人保code匹配
	 */
	private String isRbMatch;
	
	/**
	 * 是否精友code匹配
	 */
	private String isJyMatch;
	
	/**
	 * 保险公司车型code
	 */
	private String insuranceCode;
	
	/**
	 * 玻璃类型
	 */
	private Integer glassType;
	
	/**
	 * 交管车辆类型
	 */
	private Integer jgVehicleType;
	
	/**
	 * 核定载客人数
	 */
	private Integer ratedPassengerCapacity;
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

	public String getCarinfoid() {
		return carinfoid;
	}

	public void setCarinfoid(String carinfoid) {
		this.carinfoid = carinfoid;
	}

	public String getBrandname() {
		return brandname;
	}

	public void setBrandname(String brandname) {
		this.brandname = brandname;
	}

	public String getStandardname() {
		return standardname;
	}

	public void setStandardname(String standardname) {
		this.standardname = standardname;
	}

	public String getStandardfullname() {
		return standardfullname;
	}

	public void setStandardfullname(String standardfullname) {
		this.standardfullname = standardfullname;
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

	public String getCommercetype() {
		return commercetype;
	}

	public void setCommercetype(String commercetype) {
		this.commercetype = commercetype;
	}

	public String getCarprice() {
		return carprice;
	}

	public void setCarprice(String carprice) {
		this.carprice = carprice;
	}

	public Integer getSeat() {
		return seat;
	}

	public void setSeat(Integer seat) {
		this.seat = seat;
	}

	public Double getDisplacement() {
		return displacement;
	}

	public void setDisplacement(Double displacement) {
		this.displacement = displacement;
	}

	public String getAliasname() {
		return aliasname;
	}

	public void setAliasname(String aliasname) {
		this.aliasname = aliasname;
	}

	public String getGearbox() {
		return gearbox;
	}

	public void setGearbox(String gearbox) {
		this.gearbox = gearbox;
	}

	public String getAirbagnum() {
		return airbagnum;
	}

	public void setAirbagnum(String airbagnum) {
		this.airbagnum = airbagnum;
	}

	public String getAbsflag() {
		return absflag;
	}

	public void setAbsflag(String absflag) {
		this.absflag = absflag;
	}

	public String getListedyear() {
		return listedyear;
	}

	public void setListedyear(String listedyear) {
		this.listedyear = listedyear;
	}

	public String getCardeploy() {
		return cardeploy;
	}

	public void setCardeploy(String cardeploy) {
		this.cardeploy = cardeploy;
	}

	public Double getPolicycarprice() {
		return policycarprice;
	}

	public void setPolicycarprice(Double policycarprice) {
		this.policycarprice = policycarprice;
	}

	public String getIsstandardcar() {
		return isstandardcar;
	}

	public void setIsstandardcar(String isstandardcar) {
		this.isstandardcar = isstandardcar;
	}

	public Integer getCarVehicleOrigin() {
		return carVehicleOrigin;
	}

	public void setCarVehicleOrigin(Integer carVehicleOrigin) {
		this.carVehicleOrigin = carVehicleOrigin;
	}

	public String getRbCarModelName() {
		return rbCarModelName;
	}

	public void setRbCarModelName(String rbCarModelName) {
		this.rbCarModelName = rbCarModelName;
	}

	public String getJyCarModelName() {
		return jyCarModelName;
	}

	public void setJyCarModelName(String jyCarModelName) {
		this.jyCarModelName = jyCarModelName;
	}

	public String getBwCode() {
		return bwCode;
	}

	public void setBwCode(String bwCode) {
		this.bwCode = bwCode;
	}

	public String getJyCode() {
		return jyCode;
	}

	public void setJyCode(String jyCode) {
		this.jyCode = jyCode;
	}

	public String getRbCode() {
		return rbCode;
	}

	public void setRbCode(String rbCode) {
		this.rbCode = rbCode;
	}

	public String getIsRbMatch() {
		return isRbMatch;
	}

	public void setIsRbMatch(String isRbMatch) {
		this.isRbMatch = isRbMatch;
	}

	public String getIsJyMatch() {
		return isJyMatch;
	}

	public void setIsJyMatch(String isJyMatch) {
		this.isJyMatch = isJyMatch;
	}

	public String getInsuranceCode() {
		return insuranceCode;
	}

	public void setInsuranceCode(String insuranceCode) {
		this.insuranceCode = insuranceCode;
	}

	public Integer getGlassType() {
		return glassType;
	}

	public void setGlassType(Integer glassType) {
		this.glassType = glassType;
	}

	public Integer getJgVehicleType() {
		return jgVehicleType;
	}

	public void setJgVehicleType(Integer jgVehicleType) {
		this.jgVehicleType = jgVehicleType;
	}

	public Integer getRatedPassengerCapacity() {
		return ratedPassengerCapacity;
	}

	public void setRatedPassengerCapacity(Integer ratedPassengerCapacity) {
		this.ratedPassengerCapacity = ratedPassengerCapacity;
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

	public Double getLoads() {
		return loads;
	}

	public void setLoads(Double loads) {
		this.loads = loads;
	}

	public Double getUnwrtweight() {
		return unwrtweight;
	}

	public void setUnwrtweight(Double unwrtweight) {
		this.unwrtweight = unwrtweight;
	}

	public Double getFullweight() {
		return fullweight;
	}

	public void setFullweight(Double fullweight) {
		this.fullweight = fullweight;
	}

	@Override
	public String toString() {
		return "INSBCarmodelinfo [carinfoid=" + carinfoid + ", brandname="
				+ brandname + ", standardname=" + standardname
				+ ", standardfullname=" + standardfullname + ", familyname="
				+ familyname + ", price=" + price + ", taxprice=" + taxprice
				+ ", analogyprice=" + analogyprice + ", analogytaxprice="
				+ analogytaxprice + ", factoryname=" + factoryname
				+ ", commercetype=" + commercetype + ", carprice=" + carprice
				+ ", seat=" + seat + ", displacement=" + displacement
				+ ", aliasname=" + aliasname + ", gearbox=" + gearbox
				+ ", loads=" + loads + ", unwrtweight=" + unwrtweight
				+ ", fullweight=" + fullweight + ", airbagnum=" + airbagnum
				+ ", absflag=" + absflag + ", listedyear=" + listedyear
				+ ", cardeploy=" + cardeploy
				+ ", policycarprice=" + policycarprice + ", isstandardcar="
				+ isstandardcar + "]";
	}

	
	
	
}