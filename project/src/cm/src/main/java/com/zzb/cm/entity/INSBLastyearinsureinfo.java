package com.zzb.cm.entity;

import java.util.HashMap;
import java.util.Map;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;
import com.cninsure.core.utils.StringUtil;


public class INSBLastyearinsureinfo extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

	/**
	 * 任务id
	 */
	private String taskid;

	/**
	 * 上年商业险保单号
	 */
	private String sypolicyno;

	/**
	 * 上年交强险保单号
	 */
	private String jqpolicyno;

	/**
	 * 上年商业险赔付率
	 */
	private Double syclaimrate;

	/**
	 * 平台商业险理赔次数
	 */
	private Integer syclaimtimes;

	/**
	 * 平台交强险理赔次数
	 */
	private Integer jqclaimtimes;

	/**
	 * 上年交强险赔付率
	 */
	private Double jqclaimrate;

	/**
	 * 投保类型 非首次投保:0,新车首次投保:1,旧车首次投保:2
	 */
	private String firstinsuretype;

	/**
	 * 商业险理赔金额
	 */
	private Double sylastclaimsum;

	/**
	 * 交强理赔金额
	 */
	private Double jqlastclaimsum;

	/**
	 * 交通违规次数
	 */
	private Integer trafficoffence;

	/**
	 * 交通违规系数
	 */
	private Double trafficoffencediscount;

	/**
	 * 上年保险公司
	 */
	private String supplierid;

	/**
	 * 上年保险公司名称
	 */
	private String suppliername;

	/**
	 * 保险公司id
	 */
	private String inscomcode;

	/**
	 * 无赔款优待系数
	 */
	private String noclaimdiscountcoefficient;

	/**
	 * 交强险理赔系数
	 */
	private Double compulsoryclaimrate;

	/**
	 * 上年交强险理赔次数
	 */
	private String bwcompulsoryclaimtimes;

	/**
	 * 无赔款折扣浮动原因
	 */
	private String noclaimdiscountcoefficientreasons;

	/**
	 * 交强险事故浮动原因
	 */
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
	 * 上年商业险理赔次数
	 */
	private String bwcommercialclaimtimes;
	/**
	 * 更新状态位 1 更新 2 停止更新
	 */
	private String sflag;
	/**
	 * cif平台查询重复投保大于90天提示 商业重复投保
	 */
	private String repeatinsurance;
    /**
     * cif平台查询重复投保大于90天提示交强重复投保
     */
	private String jqrepeatinsurance;
	/**
     * 客户忠诚度原因
     */
	private String loyaltyreasons;
	
	public String getLoyaltyreasons() {
		return loyaltyreasons;
	}

	public void setLoyaltyreasons(String loyaltyreasons) {
		this.loyaltyreasons = loyaltyreasons;
	}
	
	public String getJqrepeatinsurance() {
		return jqrepeatinsurance;
	}

	public void setJqrepeatinsurance(String jqrepeatinsurance) {
		this.jqrepeatinsurance = jqrepeatinsurance;
	}

	public String getRepeatinsurance() {
		return repeatinsurance;
	}

	public void setRepeatinsurance(String repeatinsurance) {
		this.repeatinsurance = repeatinsurance;
	}

	public String getSflag() {
		return sflag;
	}

	public void setSflag(String sflag) {
		this.sflag = sflag;
	}

	public String getBwcommercialclaimtimes() {
		return bwcommercialclaimtimes;
	}

	public void setBwcommercialclaimtimes(String bwcommercialclaimtimes) {
		this.bwcommercialclaimtimes = bwcommercialclaimtimes;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
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

	public Double getJqclaimrate() {
		return jqclaimrate;
	}

	public void setJqclaimrate(Double jqclaimrate) {
		this.jqclaimrate = jqclaimrate;
	}

	public String getFirstinsuretype() {
		return firstinsuretype;
	}

	public void setFirstinsuretype(String firstinsuretype) {
		this.firstinsuretype = firstinsuretype;
	}

	public Double getSylastclaimsum() {
		return sylastclaimsum;
	}

	public void setSylastclaimsum(Double sylastclaimsum) {
		this.sylastclaimsum = sylastclaimsum;
	}

	public Double getJqlastclaimsum() {
		return jqlastclaimsum;
	}

	public void setJqlastclaimsum(Double jqlastclaimsum) {
		this.jqlastclaimsum = jqlastclaimsum;
	}

	public Integer getTrafficoffence() {
		return trafficoffence;
	}

	public void setTrafficoffence(Integer trafficoffence) {
		this.trafficoffence = trafficoffence;
	}

	public Double getTrafficoffencediscount() {
		return trafficoffencediscount;
	}

	public void setTrafficoffencediscount(Double trafficoffencediscount) {
		this.trafficoffencediscount = trafficoffencediscount;
	}

	public String getSupplierid() {
		return supplierid;
	}

	public void setSupplierid(String supplierid) {
		this.supplierid = supplierid;
	}

	public String getSuppliername() {
		return suppliername;
	}

	public void setSuppliername(String suppliername) {
		this.suppliername = suppliername;
	}

	public String getInscomcode() {
		return inscomcode;
	}

	public void setInscomcode(String inscomcode) {
		this.inscomcode = inscomcode;
	}

	public String getNoclaimdiscountcoefficient() {
		return noclaimdiscountcoefficient;
	}

	public void setNoclaimdiscountcoefficient(String noclaimdiscountcoefficient) {
		this.noclaimdiscountcoefficient = noclaimdiscountcoefficient;
	}

	public Double getCompulsoryclaimrate() {
		return compulsoryclaimrate;
	}

	public void setCompulsoryclaimrate(Double compulsoryclaimrate) {
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

	public void setNoclaimdiscountcoefficientreasons(String noclaimdiscountcoefficientreasons) {
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
	private static Map<String, String> map = new HashMap<String, String>();
	public static String convertFirstInsureTypeToCm(String firstInsureType){
		if(map.size() == 0){
			map.put("非首次投保", "0");
			map.put("新车首次投保", "1");
			map.put("旧车首次投保", "2");
		}
		return StringUtil.isEmpty(map.get(firstInsureType))?"":map.get(firstInsureType);
	}
}