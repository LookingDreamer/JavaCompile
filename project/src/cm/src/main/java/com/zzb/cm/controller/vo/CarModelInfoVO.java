package com.zzb.cm.controller.vo;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;

public class CarModelInfoVO extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

	/**
	 * 任务id
	 */
	private String instanceId;
	
	/**
	 * 保险公司code
	 */
	private String inscomcode;

	/**
	 * 车辆性质
	 */
	private String carproperty;

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
	 * 价格
	 */
	private Double price;

	/**
	 * 税额
	 */
	private Double taxprice;

	/**
	 * 类比价格
	 */
	private Double analogyprice;

	/**
	 * 类比税额
	 */
	private Double analogytaxprice;

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
	 * 上市年份
	 */
	private String listedyear;

	/**
	 * 车型配置
	 */
	private String cardeploy;

	/**
	 * 投保车价
	 */
	private Double policycarprice;


	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	public String getInscomcode() {
		return inscomcode;
	}

	public void setInscomcode(String inscomcode) {
		this.inscomcode = inscomcode;
	}

	public String getCarproperty() {
		return carproperty;
	}

	public void setCarproperty(String carproperty) {
		this.carproperty = carproperty;
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

	public CarModelInfoVO() {
		super();
	}


}