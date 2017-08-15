package com.zzb.cm.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;


public class INSBRulequeryotherinfo extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

	/**
	 * 任务id
	 */
	private String taskid;

	/**
	 * 无赔款优待系数
	 */
	private Double noclaimdiscountcoefficient;

	/**
	 * 无赔款折扣浮动原因
	 */
	private String noclaimdiscountcoefficientreasons;

	/**
	 * 客户忠诚度原因
	 */
	private String loyaltyreasons;

	/**
	 * 交通违法系数
	 */
	private Double trafficoffencediscount;

	/**
	 * 交强险理赔系数
	 */
	private Double compulsoryclaimrate;

	/**
	 * 交强险浮动原因
	 */
	private String compulsoryclaimratereasons;

	/**
	 * 投保类型
	 */
	private String firstinsuretype;

	/**
	 * 上年商业险理赔次数
	 */
	private String bwcommercialclaimtimes;

	/**
	 * 上年商业险理赔金额
	 */
	private Double bwlastclaimsum;

	/**
	 * 上年交强险理赔次数
	 */
	private String bwcompulsoryclaimtimes;

	/**
	 * 上年交强险理赔金额
	 */
	private Double bwlastcompulsoryclaimsum;

	/**
	 * 平台商业险理赔次数
	 */
	private Integer claimtimes;

	/**
	 * 平台交强险理赔次数
	 */
	private Integer compulsoryclaimtimes;

	/**
	 * 平台商业险理赔金额
	 */
	private Double lastclaimsum;

	/**
	 * 商业险起保标识
	 */
	private String syendmark;

	/**
	 * 商业险起保提示语
	 */
	private String errormsgsy;

	/**
	 * 交强险起保标识
	 */
	private String jqendmark;

	/**
	 * 交强险起保提示语
	 */
	private String errormsgjq;

	/**
	 * 交强险折扣保费
	 */
	private String efcdiscount;

	/**
	 * 车船税滞纳金
	 */
	private String vehicletaxoverduefine;

	/**
	 * 风险类别
	 */
	private String riskclass;

	/**
	 * 是否通过纯电销投保
	 */
	private String pureesale;

	/**
	 * 上年商业险赔付率
	 */
	private String bwcommercialclaimrate;

	/**
	 * 往年补缴税额
	 */
	private String lwarrearstax;

	/**
	 * 平台车价
	 */
	private String platformcarprice;

	/**
	 * 酒驾系数
	 */
	private String drunkdrivingrate;

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public Double getNoclaimdiscountcoefficient() {
		return noclaimdiscountcoefficient;
	}

	public void setNoclaimdiscountcoefficient(Double noclaimdiscountcoefficient) {
		this.noclaimdiscountcoefficient = noclaimdiscountcoefficient;
	}

	public String getNoclaimdiscountcoefficientreasons() {
		return noclaimdiscountcoefficientreasons;
	}

	public void setNoclaimdiscountcoefficientreasons(String noclaimdiscountcoefficientreasons) {
		this.noclaimdiscountcoefficientreasons = noclaimdiscountcoefficientreasons;
	}

	public String getLoyaltyreasons() {
		return loyaltyreasons;
	}

	public void setLoyaltyreasons(String loyaltyreasons) {
		this.loyaltyreasons = loyaltyreasons;
	}

	public Double getTrafficoffencediscount() {
		return trafficoffencediscount;
	}

	public void setTrafficoffencediscount(Double trafficoffencediscount) {
		this.trafficoffencediscount = trafficoffencediscount;
	}

	public Double getCompulsoryclaimrate() {
		return compulsoryclaimrate;
	}

	public void setCompulsoryclaimrate(Double compulsoryclaimrate) {
		this.compulsoryclaimrate = compulsoryclaimrate;
	}

	public String getCompulsoryclaimratereasons() {
		return compulsoryclaimratereasons;
	}

	public void setCompulsoryclaimratereasons(String compulsoryclaimratereasons) {
		this.compulsoryclaimratereasons = compulsoryclaimratereasons;
	}

	public String getFirstinsuretype() {
		return firstinsuretype;
	}

	public void setFirstinsuretype(String firstinsuretype) {
		this.firstinsuretype = firstinsuretype;
	}

	public String getBwcommercialclaimtimes() {
		return bwcommercialclaimtimes;
	}

	public void setBwcommercialclaimtimes(String bwcommercialclaimtimes) {
		this.bwcommercialclaimtimes = bwcommercialclaimtimes;
	}

	public Double getBwlastclaimsum() {
		return bwlastclaimsum;
	}

	public void setBwlastclaimsum(Double bwlastclaimsum) {
		this.bwlastclaimsum = bwlastclaimsum;
	}

	public String getBwcompulsoryclaimtimes() {
		return bwcompulsoryclaimtimes;
	}

	public void setBwcompulsoryclaimtimes(String bwcompulsoryclaimtimes) {
		this.bwcompulsoryclaimtimes = bwcompulsoryclaimtimes;
	}

	public Double getBwlastcompulsoryclaimsum() {
		return bwlastcompulsoryclaimsum;
	}

	public void setBwlastcompulsoryclaimsum(Double bwlastcompulsoryclaimsum) {
		this.bwlastcompulsoryclaimsum = bwlastcompulsoryclaimsum;
	}

	public Integer getClaimtimes() {
		return claimtimes;
	}

	public void setClaimtimes(Integer claimtimes) {
		this.claimtimes = claimtimes;
	}

	public Integer getCompulsoryclaimtimes() {
		return compulsoryclaimtimes;
	}

	public void setCompulsoryclaimtimes(Integer compulsoryclaimtimes) {
		this.compulsoryclaimtimes = compulsoryclaimtimes;
	}

	public Double getLastclaimsum() {
		return lastclaimsum;
	}

	public void setLastclaimsum(Double lastclaimsum) {
		this.lastclaimsum = lastclaimsum;
	}

	public String getSyendmark() {
		return syendmark;
	}

	public void setSyendmark(String syendmark) {
		this.syendmark = syendmark;
	}

	public String getErrormsgsy() {
		return errormsgsy;
	}

	public void setErrormsgsy(String errormsgsy) {
		this.errormsgsy = errormsgsy;
	}

	public String getJqendmark() {
		return jqendmark;
	}

	public void setJqendmark(String jqendmark) {
		this.jqendmark = jqendmark;
	}

	public String getErrormsgjq() {
		return errormsgjq;
	}

	public void setErrormsgjq(String errormsgjq) {
		this.errormsgjq = errormsgjq;
	}

	public String getEfcdiscount() {
		return efcdiscount;
	}

	public void setEfcdiscount(String efcdiscount) {
		this.efcdiscount = efcdiscount;
	}

	public String getVehicletaxoverduefine() {
		return vehicletaxoverduefine;
	}

	public void setVehicletaxoverduefine(String vehicletaxoverduefine) {
		this.vehicletaxoverduefine = vehicletaxoverduefine;
	}

	public String getRiskclass() {
		return riskclass;
	}

	public void setRiskclass(String riskclass) {
		this.riskclass = riskclass;
	}

	public String getPureesale() {
		return pureesale;
	}

	public void setPureesale(String pureesale) {
		this.pureesale = pureesale;
	}

	public String getBwcommercialclaimrate() {
		return bwcommercialclaimrate;
	}

	public void setBwcommercialclaimrate(String bwcommercialclaimrate) {
		this.bwcommercialclaimrate = bwcommercialclaimrate;
	}

	public String getLwarrearstax() {
		return lwarrearstax;
	}

	public void setLwarrearstax(String lwarrearstax) {
		this.lwarrearstax = lwarrearstax;
	}

	public String getPlatformcarprice() {
		return platformcarprice;
	}

	public void setPlatformcarprice(String platformcarprice) {
		this.platformcarprice = platformcarprice;
	}

	public String getDrunkdrivingrate() {
		return drunkdrivingrate;
	}

	public void setDrunkdrivingrate(String drunkdrivingrate) {
		this.drunkdrivingrate = drunkdrivingrate;
	}

}