package com.zzb.cm.Interface.entity.car.model; /**
 * 
 */

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 单方对象,不存储投保记录，仅仅存储报价结果和投保记录结果
 * @author austinChen
 * created at 2015年6月12日 下午1:53:09
 */
public class SQ implements Serializable {

	/**
	 * 序列化ID
	 */
	private static final long serialVersionUID = 2539249503716692108L;
    /*单方报价ID*/
	private String sqId;
	/*险别配置*/
	private List<SuiteDef> suites;
    /*订单ID*/
	private String orderId;
    /*商业险暂存单ID*/
	private String bizTempId;
    /*交强险暂存单ID*/
	private String efcTempId;
    /*商业险投保单号*/
	private String bizProposeNum;
    /*交强险投保单号*/
	private String efcProposeNum;
    /*商业险保单号*/
	private String bizPolicyCode;
    /*交强险保单号*/
	private String efcPolicyCode;
    /*商业险折扣率*/
	private BigDecimal bussDiscountRate;
    /*交强险折扣率*/
	private BigDecimal trafficDiscountRate;
    /*商业险总保费*/
	private BigDecimal bizCharge;
    /*较强险总保费*/
	private BigDecimal efcCharge;
    /*车船税金额*/
	private BigDecimal taxCharge;
    /*总保费*/
	private BigDecimal totalCharge;

	private Date created;
	private Date updated;
	/*支付信息*/
	private PayInfo payInfo;
	private String msg;
	/*杂项*/
	private String misc;
	
	/**上年商业险保单号*/
	private String sypolicyno;
	/**上年交强险保单号*/
	private String jqpolicyno;
	/**上年商业险赔付率 */
	private Double syclaimrate;
	/**上年交强险赔付率*/
	private Double jqclaimrate;
	/**上年商业险理赔次数*/
	private Integer syclaimtimes;
	/**上年交强险理赔次数*/
	private Integer jqclaimtimes;
	
	/**无赔款优待系数*/
	private BigDecimal noclaimdiscountcoefficient;
	/**交强险理赔系数*/
	private BigDecimal compulsoryclaimrate;
	/**交强险理赔次数汉字*/
	private String bwcompulsoryclaimtimes;
	/**无赔款折扣浮动原因*/
	private String noclaimdiscountcoefficientreasons;
	/**交强险事故浮动原因*/
	private String compulsoryclaimratereasons;
	/**
	 * 商业险事故 JSONArray
	 */
	private String syclaims;

	/**
	 * 交强险事故 JSONArray
	 */
	private String jqclaims;

	/**
	 * 上年保单商业险开始时间
	 */
	private String systartdate;

	/**
	 * 上年保单商业险结束时间
	 */
	private String syenddate;

	/**
	 * 上年保单交强险开始时间
	 */
	private String jqstartdate;

	/**
	 * 上年保单交强险结束时间
	 */
	private String jqenddate;
	/**
	 * 商业险理赔次数汉子
	 */
	private String bwcommercialclaimtimes;
	/**
	 * 上年保险公司
	 */
	private String supplierid;
	
	/**
	 * 客户忠诚度
	 * @return
	 */
	private String loyaltyreasons;
	/**
	 * 从规则获取的折扣系数
	 */
	private Map<String, Double> discountRates;
	
	
	public String getLoyaltyreasons() {
		return loyaltyreasons;
	}
	public void setLoyaltyreasons(String loyaltyreasons) {
		this.loyaltyreasons = loyaltyreasons;
	}
	public String getSupplierid() {
		return supplierid;
	}
	public void setSupplierid(String supplierid) {
		this.supplierid = supplierid;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Timestamp created) {
		this.created = created;
	}
	public Date getUpdated() {
		return updated;
	} 
	public void setUpdated(Timestamp updated) {
		this.updated = updated;
	}
	public String getSqId() {
		return sqId;
	}
	public void setSqId(String sqId) {
		this.sqId = sqId;
	}
	public List<SuiteDef> getSuites() {
		return suites;
	}
	public void setSuites(List<SuiteDef> suites) {
		this.suites = suites;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getBizTempId() {
		return bizTempId;
	}

	public void setBizTempId(String bizTempId) {
		this.bizTempId = bizTempId;
	}

	public String getEfcTempId() {
		return efcTempId;
	}

	public void setEfcTempId(String efcTempId) {
		this.efcTempId = efcTempId;
	}

	public String getBizProposeNum() {
		return bizProposeNum;
	}
	public void setBizProposeNum(String bizProposeNum) {
		this.bizProposeNum = bizProposeNum;
	}
	public String getEfcProposeNum() {
		return efcProposeNum;
	}
	public void setEfcProposeNum(String efcProposeNum) {
		this.efcProposeNum = efcProposeNum;
	}
	public String getBizPolicyCode() {
		return bizPolicyCode;
	}
	public void setBizPolicyCode(String bizPolicyCode) {
		this.bizPolicyCode = bizPolicyCode;
	}
	public String getEfcPolicyCode() {
		return efcPolicyCode;
	}
	public void setEfcPolicyCode(String efcPolicyCode) {
		this.efcPolicyCode = efcPolicyCode;
	}
	public BigDecimal getBussDiscountRate() {
		return bussDiscountRate;
	}
	public void setBussDiscountRate(BigDecimal bussDiscountRate) {
		this.bussDiscountRate = bussDiscountRate;
	}
	public BigDecimal getTrafficDiscountRate() {
		return trafficDiscountRate;
	}
	public void setTrafficDiscountRate(BigDecimal trafficDiscountRate) {
		this.trafficDiscountRate = trafficDiscountRate;
	}
	public BigDecimal getBizCharge() {
		return bizCharge;
	}
	public void setBizCharge(BigDecimal bizCharge) {
		this.bizCharge = bizCharge;
	}
	public BigDecimal getEfcCharge() {
		return efcCharge;
	}
	public void setEfcCharge(BigDecimal efcCharge) {
		this.efcCharge = efcCharge;
	}
	public BigDecimal getTaxCharge() {
		return taxCharge;
	}
	public void setTaxCharge(BigDecimal taxCharge) {
		this.taxCharge = taxCharge;
	}
	public BigDecimal getTotalCharge() {
		return totalCharge;
	}
	public void setTotalCharge(BigDecimal totalCharge) {
		this.totalCharge = totalCharge;
	}
	public PayInfo getPayInfo() {
		return payInfo;
	}
	public void setPayInfo(PayInfo payInfo) {
		this.payInfo = payInfo;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getMisc() {
		return misc;
	}

	public void setMisc(String misc) {
		this.misc = misc;
	}
	public String getSypolicyno() {
		return sypolicyno;
	}
	public void setSypolicyno(String sypolicyno) {
		this.sypolicyno = sypolicyno;
	}
	public String getJqpolicyno() {
		return jqpolicyno;
	}
	public void setJqpolicyno(String jqpolicyno) {
		this.jqpolicyno = jqpolicyno;
	}
	public Double getSyclaimrate() {
		return syclaimrate;
	}
	public void setSyclaimrate(Double syclaimrate) {
		this.syclaimrate = syclaimrate;
	}
	public Double getJqclaimrate() {
		return jqclaimrate;
	}
	public void setJqclaimrate(Double jqclaimrate) {
		this.jqclaimrate = jqclaimrate;
	}
	public Integer getSyclaimtimes() {
		return syclaimtimes;
	}
	public void setSyclaimtimes(Integer syclaimtimes) {
		this.syclaimtimes = syclaimtimes;
	}
	public Integer getJqclaimtimes() {
		return jqclaimtimes;
	}
	public void setJqclaimtimes(Integer jqclaimtimes) {
		this.jqclaimtimes = jqclaimtimes;
	}
	public BigDecimal getNoclaimdiscountcoefficient() {
		return noclaimdiscountcoefficient;
	}
	public void setNoclaimdiscountcoefficient(BigDecimal noclaimdiscountcoefficient) {
		this.noclaimdiscountcoefficient = noclaimdiscountcoefficient;
	}
	public BigDecimal getCompulsoryclaimrate() {
		return compulsoryclaimrate;
	}
	public void setCompulsoryclaimrate(BigDecimal compulsoryclaimrate) {
		this.compulsoryclaimrate = compulsoryclaimrate;
	}
	public String getBwcompulsoryclaimtimes() {
		return bwcompulsoryclaimtimes;
	}
	public void setBwcompulsoryclaimtimes(String bwcompulsoryclaimtimes) {
		this.bwcompulsoryclaimtimes = bwcompulsoryclaimtimes;
	}
	public String getNoclaimdiscountcoefficientreasons() {
		return noclaimdiscountcoefficientreasons;
	}
	public void setNoclaimdiscountcoefficientreasons(
			String noclaimdiscountcoefficientreasons) {
		this.noclaimdiscountcoefficientreasons = noclaimdiscountcoefficientreasons;
	}
	public String getCompulsoryclaimratereasons() {
		return compulsoryclaimratereasons;
	}
	public void setCompulsoryclaimratereasons(String compulsoryclaimratereasons) {
		this.compulsoryclaimratereasons = compulsoryclaimratereasons;
	}
	public String getSyclaims() {
		return syclaims;
	}
	public void setSyclaims(String syclaims) {
		this.syclaims = syclaims;
	}
	public String getJqclaims() {
		return jqclaims;
	}
	public void setJqclaims(String jqclaims) {
		this.jqclaims = jqclaims;
	}
	public String getSystartdate() {
		return systartdate;
	}
	public void setSystartdate(String systartdate) {
		this.systartdate = systartdate;
	}
	public String getSyenddate() {
		return syenddate;
	}
	public void setSyenddate(String syenddate) {
		this.syenddate = syenddate;
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
	public String getBwcommercialclaimtimes() {
		return bwcommercialclaimtimes;
	}
	public void setBwcommercialclaimtimes(String bwcommercialclaimtimes) {
		this.bwcommercialclaimtimes = bwcommercialclaimtimes;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public void setUpdated(Date updated) {
		this.updated = updated;
	}

	public Map<String, Double> getDiscountRates() {
		return discountRates;
	}

	public void setDiscountRates(Map<String, Double> discountRates) {
		this.discountRates = discountRates;
	}
}
