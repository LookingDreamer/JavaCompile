package com.zzb.cm.controller.vo;


public class SupplementInfoVO{

	/**
	 * 投保类型(非首次投保:0,新车首次投保:1,旧车首次投保:2)
	 */
	private Integer firstInsureType;
	
	/**
	 * 上年交强理赔次数
	 * 连续三年没有理赔:-2,连续两年没有理赔:-1,上年没有理赔:0,新保或上年发生一次有责任不涉及死亡理赔:1,上年有两次及以上理赔:2,上年有涉及死亡理赔:3
	 */
	private Integer compulsoryClaimTimes;

	/**
	 * 商业险理赔金额
	 */
	private Double lastCommercialClaimSum;

	/**
	 * 上年商业保险费
	 */
	private Double lastCommercialPremium;

	/**
	 * 上年商业赔付率
	 */
	private Double commercialClaimRate;

	/**
	 * 特种车类型
	 * 特种车型一:1,特种车型二:2,特种车型三:3,特种车型四:4
	 */
	private Long specialVehicleType;

	/**
	 * 上年商业理赔次数
	 * 连续五年没有理赔:-4,连续四年没有理赔:-3,连续三年没有理赔:-2,连续两年没有理赔:-1,去年没有理赔:0,去年发生一次理赔:1,
	 * 去年发生两次理赔:2,去年发生三次理赔:3,去年发生四次理赔:4,去年发生五次理赔:5,去年发生六次理赔:6,去年发生七次理赔:7,
	 * 去年发生八次理赔:8,去年发生九次理赔:9,去年发生十次理赔:10,去年发生十次以上理赔:11
	 */
	private Integer commercialClaimTimes;

	/**
	 * 车辆归属区域
	 * 本地:0,省内异地:1,省外异地:2,港澳地区:3
	 */
	private Integer registerArea;

	/**
	 * 自定义折扣
	 */
	private Double customDiscount;

	/**
	 * 使用车队优惠
	 */
	private Boolean useMotorcadeMode;

	/**
	 * 车辆用途
	 * 默认:0,挂车:1,自卸货车:2,泵车:3,搅拌车:4,起重机:5,跑车:6,轿跑车:7,牵引车:8,半挂牵引车:9,教练车:10,
	 * 警车:11,武警车:12,越野车:13,农用车:14,皮卡车:15,水泥车:16,清障车:17,挖掘车:18,冷藏车:19,罐车:20,
	 * 拖拉机:21,货车:22,三轮汽车:23,清洗车:24,厢式车:25,吊车:26,载货机动车:27,客货两用车:28,清扫车:29,
	 * 清洁车:30,装卸车:31,升降车:32,推土车:33,保温车:34,监测车:35,消防车:36,运钞车:37,医疗车:38,电视转播车:39,
	 * 救护车:40,洒水车:41,拖车:42,工程车:43,检修车:44,运输车:45,作业车:46,自卸低速货车:47
	 */
	private Integer vehicularApplications;
	
	/**
	 * 自重计算标准
	 * 默认:默认,整吨计量:整吨计量,半吨计量:半吨计量,实际质量:实际质量
	 */
	private String fullLoadCalculationType;


	public Integer getFirstInsureType() {
		return firstInsureType;
	}


	public void setFirstInsureType(Integer firstInsureType) {
		this.firstInsureType = firstInsureType;
	}


	public Integer getCompulsoryClaimTimes() {
		return compulsoryClaimTimes;
	}


	public void setCompulsoryClaimTimes(Integer compulsoryClaimTimes) {
		this.compulsoryClaimTimes = compulsoryClaimTimes;
	}


	public Double getLastCommercialClaimSum() {
		return lastCommercialClaimSum;
	}


	public void setLastCommercialClaimSum(Double lastCommercialClaimSum) {
		this.lastCommercialClaimSum = lastCommercialClaimSum;
	}


	public Double getLastCommercialPremium() {
		return lastCommercialPremium;
	}


	public void setLastCommercialPremium(Double lastCommercialPremium) {
		this.lastCommercialPremium = lastCommercialPremium;
	}


	public Double getCommercialClaimRate() {
		return commercialClaimRate;
	}


	public void setCommercialClaimRate(Double commercialClaimRate) {
		this.commercialClaimRate = commercialClaimRate;
	}


	public Long getSpecialVehicleType() {
		return specialVehicleType;
	}


	public void setSpecialVehicleType(Long specialVehicleType) {
		this.specialVehicleType = specialVehicleType;
	}


	public Integer getCommercialClaimTimes() {
		return commercialClaimTimes;
	}


	public void setCommercialClaimTimes(Integer commercialClaimTimes) {
		this.commercialClaimTimes = commercialClaimTimes;
	}


	public Integer getRegisterArea() {
		return registerArea;
	}


	public void setRegisterArea(Integer registerArea) {
		this.registerArea = registerArea;
	}


	public Double getCustomDiscount() {
		return customDiscount;
	}


	public void setCustomDiscount(Double customDiscount) {
		this.customDiscount = customDiscount;
	}


	public Boolean getUseMotorcadeMode() {
		return useMotorcadeMode;
	}


	public void setUseMotorcadeMode(Boolean useMotorcadeMode) {
		this.useMotorcadeMode = useMotorcadeMode;
	}


	public Integer getVehicularApplications() {
		return vehicularApplications;
	}


	public void setVehicularApplications(Integer vehicularApplications) {
		this.vehicularApplications = vehicularApplications;
	}


	public String getFullLoadCalculationType() {
		return fullLoadCalculationType;
	}


	public void setFullLoadCalculationType(String fullLoadCalculationType) {
		this.fullLoadCalculationType = fullLoadCalculationType;
	}


	public SupplementInfoVO() {
		super();
	}


}