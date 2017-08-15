package com.zzb.mobile.model.lastindanger;

import java.util.Date;
import java.util.List;

public class LastClaimBackInfo {
	String xubao;//续保标志  
	String personName;//车主姓名
	String insureClass;//单商业险值为字符串1，单交强险为字符串2，混保为字符串3
    String plateNum; //车牌号
    String year;//起保年度
    String vin; //车架号
    String engineNum;//发动机号
    String firstRegDate; //初登日期
    String carBrandName; //品牌名称
    Double price;//新车价
    Double taxPrice;//含税车价
    Double analogyPrice;//类比价
    Double analogyTaxPrice;//含税类比价
    String carModelDate;//上市年份
    int seatCnt;//核定载客
    String modelLoad;//核定载质量
    String fullLoad;//整备质量
    String displacement;//排气量
    List<BizClaims> bizClaims;//商业险理赔列表
    List<BizClaims> efcClaims;//交强险理赔列表
    List<LastPolicy> bizPolicies; //商业险上年投保记录
    List<LastPolicy> efcPolicies;//交强险上年投保记录
    String efcDiscount;//交强险折扣保费
    String noClaimDiscountCoefficient;//无赔款优待系数
    String noClaimDiscountCoefficientReasons;//无赔款折扣浮动原因
    int claimTimes;//平台商业险理赔次数
    String compulsoryClaimTimes;//平台交强险理赔次数
    double lastClaimSum;//平台商业险理赔金额
    Double loyalty;//平台客户忠诚度系数
    String loyaltyReasons;//客户忠诚度原因
    Double compulsoryClaimRate; //交强险理赔系数
    String compulsoryClaimRateReasons; //交强险浮动原因
    Double platformCarPrice;//平台车价
    String bwCommercialClaimTimes;//上年商业险理赔次数
    Double bwLastClaimSum; //上年商业险理赔金额
    String bwCommercialClaimRate;//上年商业险赔付率
    String firstInsureType;//投保类型
    String riskClass;//风险类别
    String efcFloat;//交强险违法浮动
    String pureESale; //是否通过纯电销投保
    String bwCompulsoryClaimTimes;//上年交强险理赔次数
    Double trafficOffenceDiscount;//交通违法系数
    String vehicleTaxOverdueFine;//车船税滞纳金
    String lwArrearsTax;//往年补缴税额
    String drunkDriving;//酒驾系数
    String taskSequence;//查询序列号
    String spiritName;//执行查询精灵名称（保险公司ID）
    String skillName;//执行查询精灵技能名称
    String threadId;//精灵使用的保险公司工号
    String updateTime;//数据更新时间
    String succeed;//精灵查询成功与否, success表示成功，failure表示失败，partialSuccess表示部分成功
    String queryState;//平台查询状态，created表示初始化中，executing表示执行中，completed表示完成
    String multiQuoteId;//多方报价单号
	Date currenttime;//当前时间
	String repeatInsurance;//重复投保提示 20160531
	//交强重复投保
    String jqrepeatInsurance;
    String cmCompanyCode;//保险公司code

	public String getCmCompanyCode() {
		return cmCompanyCode;
	}
	public void setCmCompanyCode(String cmCompanyCode) {
		this.cmCompanyCode = cmCompanyCode;
	}
	public String getJqrepeatInsurance() {
		return jqrepeatInsurance;
	}
	public void setJqrepeatInsurance(String jqrepeatInsurance) {
		this.jqrepeatInsurance = jqrepeatInsurance;
	}
	public String getRepeatInsurance() {
		return repeatInsurance;
	}
	public void setRepeatInsurance(String repeatInsurance) {
		this.repeatInsurance = repeatInsurance;
	}
	public String getXubao() {
		return xubao;
	}
	public void setXubao(String xubao) {
		this.xubao = xubao;
	}
	public Double getAnalogyPrice() {
		return analogyPrice;
	}
	public void setAnalogyPrice(Double analogyPrice) {
		this.analogyPrice = analogyPrice;
	}
	public String getCarBrandName() {
		return carBrandName;
	}
	public void setCarBrandName(String carBrandName) {
		this.carBrandName = carBrandName;
	}
	public String getSucceed() {
		return succeed;
	}
	public void setSucceed(String succeed) {
		this.succeed = succeed;
	}
	public Double getBwLastClaimSum() {
		return bwLastClaimSum;
	}
	public void setBwLastClaimSum(Double bwLastClaimSum) {
		this.bwLastClaimSum = bwLastClaimSum;
	}
	public List<BizClaims> getBizClaims() {
		return bizClaims;
	}
	public void setBizClaims(List<BizClaims> bizClaims) {
		this.bizClaims = bizClaims;
	}
	public Double getTaxPrice() {
		return taxPrice;
	}
	public void setTaxPrice(Double taxPrice) {
		this.taxPrice = taxPrice;
	}
	public List<BizClaims> getEfcClaims() {
		return efcClaims;
	}
	public void setEfcClaims(List<BizClaims> efcClaims) {
		this.efcClaims = efcClaims;
	}
	public String getNoClaimDiscountCoefficient() {
		return noClaimDiscountCoefficient;
	}
	public void setNoClaimDiscountCoefficient(String noClaimDiscountCoefficient) {
		this.noClaimDiscountCoefficient = noClaimDiscountCoefficient;
	}
	public int getSeatCnt() {
		return seatCnt;
	}
	public void setSeatCnt(int seatCnt) {
		this.seatCnt = seatCnt;
	}
	public String getBwCommercialClaimTimes() {
		return bwCommercialClaimTimes;
	}
	public void setBwCommercialClaimTimes(String bwCommercialClaimTimes) {
		this.bwCommercialClaimTimes = bwCommercialClaimTimes;
	}
	public String getLoyaltyReasons() {
		return loyaltyReasons;
	}
	public void setLoyaltyReasons(String loyaltyReasons) {
		this.loyaltyReasons = loyaltyReasons;
	}
	public Double getCompulsoryClaimRate() {
		return compulsoryClaimRate;
	}
	public void setCompulsoryClaimRate(Double compulsoryClaimRate) {
		this.compulsoryClaimRate = compulsoryClaimRate;
	}
	public String getCompulsoryClaimTimes() {
		return compulsoryClaimTimes;
	}
	public void setCompulsoryClaimTimes(String compulsoryClaimTimes) {
		this.compulsoryClaimTimes = compulsoryClaimTimes;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public String getBwCompulsoryClaimTimes() {
		return bwCompulsoryClaimTimes;
	}
	public void setBwCompulsoryClaimTimes(String bwCompulsoryClaimTimes) {
		this.bwCompulsoryClaimTimes = bwCompulsoryClaimTimes;
	}
	public String getFirstInsureType() {
		return firstInsureType;
	}
	public void setFirstInsureType(String firstInsureType) {
		this.firstInsureType = firstInsureType;
	}
	public Double getAnalogyTaxPrice() {
		return analogyTaxPrice;
	}
	public void setAnalogyTaxPrice(Double analogyTaxPrice) {
		this.analogyTaxPrice = analogyTaxPrice;
	}
	public String getDisplacement() {
		return displacement;
	}
	public void setDisplacement(String displacement) {
		this.displacement = displacement;
	}
	public double getLastClaimSum() {
		return lastClaimSum;
	}
	public void setLastClaimSum(double lastClaimSum) {
		this.lastClaimSum = lastClaimSum;
	}
	public Double getLoyalty() {
		return loyalty;
	}
	public void setLoyalty(Double loyalty) {
		this.loyalty = loyalty;
	}
	public String getCarModelDate() {
		return carModelDate;
	}
	public void setCarModelDate(String carModelDate) {
		this.carModelDate = carModelDate;
	}
	public Double getPlatformCarPrice() {
		return platformCarPrice;
	}
	public void setPlatformCarPrice(Double platformCarPrice) {
		this.platformCarPrice = platformCarPrice;
	}
	public List<LastPolicy> getEfcPolicies() {
		return efcPolicies;
	}
	public void setEfcPolicies(List<LastPolicy> efcPolicies) {
		this.efcPolicies = efcPolicies;
	}
	public int getClaimTimes() {
		return claimTimes;
	}
	public void setClaimTimes(int claimTimes) {
		this.claimTimes = claimTimes;
	}
	public String getSkillName() {
		return skillName;
	}
	public void setSkillName(String skillName) {
		this.skillName = skillName;
	}
	public String getSpiritName() {
		return spiritName;
	}
	public void setSpiritName(String spiritName) {
		this.spiritName = spiritName;
	}
	public String getVin() {
		return vin;
	}
	public void setVin(String vin) {
		this.vin = vin;
	}
	public String getQueryState() {
		return queryState;
	}
	public void setQueryState(String queryState) {
		this.queryState = queryState;
	}
	public String getTaskSequence() {
		return taskSequence;
	}
	public void setTaskSequence(String taskSequence) {
		this.taskSequence = taskSequence;
	}
	public String getPlateNum() {
		return plateNum;
	}
	public void setPlateNum(String plateNum) {
		this.plateNum = plateNum;
	}
	public List<LastPolicy> getBizPolicies() {
		return bizPolicies;
	}
	public void setBizPolicies(List<LastPolicy> bizPolicies) {
		this.bizPolicies = bizPolicies;
	}
	public String getVehicleTaxOverdueFine() {
		return vehicleTaxOverdueFine;
	}
	public void setVehicleTaxOverdueFine(String vehicleTaxOverdueFine) {
		this.vehicleTaxOverdueFine = vehicleTaxOverdueFine;
	}
	public String getPersonName() {
		return personName;
	}
	public void setPersonName(String personName) {
		this.personName = personName;
	}
	public String getInsureClass() {
		return insureClass;
	}
	public void setInsureClass(String insureClass) {
		this.insureClass = insureClass;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getFirstRegDate() {
		return firstRegDate;
	}
	public void setFirstRegDate(String firstRegDate) {
		this.firstRegDate = firstRegDate;
	}
	public String getEngineNum() {
		return engineNum;
	}
	public void setEngineNum(String engineNum) {
		this.engineNum = engineNum;
	}
	public String getCompulsoryClaimRateReasons() {
		return compulsoryClaimRateReasons;
	}
	public void setCompulsoryClaimRateReasons(String compulsoryClaimRateReasons) {
		this.compulsoryClaimRateReasons = compulsoryClaimRateReasons;
	}
	public String getLwArrearsTax() {
		return lwArrearsTax;
	}
	public void setLwArrearsTax(String lwArrearsTax) {
		this.lwArrearsTax = lwArrearsTax;
	}
	public String getMultiQuoteId() {
		return multiQuoteId;
	}
	public void setMultiQuoteId(String multiQuoteId) {
		this.multiQuoteId = multiQuoteId;
	}

	public String getDrunkDriving() {
		return drunkDriving;
	}
	public void setDrunkDriving(String drunkDriving) {
		this.drunkDriving = drunkDriving;
	}
	public String getModelLoad() {
		return modelLoad;
	}
	public void setModelLoad(String modelLoad) {
		this.modelLoad = modelLoad;
	}
	public String getFullLoad() {
		return fullLoad;
	}
	public void setFullLoad(String fullLoad) {
		this.fullLoad = fullLoad;
	}
	public String getEfcDiscount() {
		return efcDiscount;
	}
	public void setEfcDiscount(String efcDiscount) {
		this.efcDiscount = efcDiscount;
	}
	public String getNoClaimDiscountCoefficientReasons() {
		return noClaimDiscountCoefficientReasons;
	}
	public void setNoClaimDiscountCoefficientReasons(String noClaimDiscountCoefficientReasons) {
		this.noClaimDiscountCoefficientReasons = noClaimDiscountCoefficientReasons;
	}
	public String getBwCommercialClaimRate() {
		return bwCommercialClaimRate;
	}
	public void setBwCommercialClaimRate(String bwCommercialClaimRate) {
		this.bwCommercialClaimRate = bwCommercialClaimRate;
	}
	public String getRiskClass() {
		return riskClass;
	}
	public void setRiskClass(String riskClass) {
		this.riskClass = riskClass;
	}
	public String getEfcFloat() {
		return efcFloat;
	}
	public void setEfcFloat(String efcFloat) {
		this.efcFloat = efcFloat;
	}
	public String getPureESale() {
		return pureESale;
	}
	public void setPureESale(String pureESale) {
		this.pureESale = pureESale;
	}
	public Double getTrafficOffenceDiscount() {
		return trafficOffenceDiscount;
	}
	public void setTrafficOffenceDiscount(Double trafficOffenceDiscount) {
		this.trafficOffenceDiscount = trafficOffenceDiscount;
	}
	public String getThreadId() {
		return threadId;
	}
	public void setThreadId(String threadId) {
		this.threadId = threadId;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public Date getCurrenttime() {
		return currenttime;
	}
	public void setCurrenttime(Date currenttime) {
		this.currenttime = currenttime;
	}
}
