package com.zzb.conf.entity;

import java.util.List;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;


public class INSBPaychannelmanager extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

	/**
	 * 支付通道ID
	 */
	private String paychannelid;

	/**
	 * 协议ID
	 */
	private String agreementid;
	/**
	 * 协议IDs
	 */
	private List<String> agreementids;

	/**
	 *  收款方 字典 type : tradingobject 
	 */
	private String collectiontype;

	/**
	 * 收款单位名称
	 */
	private String collectionname;

	/**
	 * 机构所在行政区编码
	 */
	private String deptcode;

	/**
	 * 结算商户号
	 */
	private String settlementno;

	/**
	 * 平台商户号
	 */
	private String terraceno;

	/**
	 * 字典 type : tradingobject 
	 */
	private String paytarget;

	/**
	 * 支付通道ID
	 */
	private String transferdesc;

	/**
	 * 排序权重
	 */
	private String sort;

	/**
	 * 费率优惠描述
	 */
	private String favorabledescribe;
	
	/**
	 * 供应商id
	 */
	private String providerid;

	/**
	 * 机构id
	 */
	private String deptid;
	/**
	 * 结算商户号名称
	 */
	private String settlementnoname;

	/**
	 * 平台商户号名称
	 */
	private String terracenoname;

	private String userDept;

	public String getUserDept() {
		return userDept;
	}

	public void setUserDept(String userDept) {
		this.userDept = userDept;
	}

	public String getPaychannelid() {
		return paychannelid;
	}

	public void setPaychannelid(String paychannelid) {
		this.paychannelid = paychannelid;
	}

	public String getAgreementid() {
		return agreementid;
	}

	public void setAgreementid(String agreementid) {
		this.agreementid = agreementid;
	}

	public String getCollectiontype() {
		return collectiontype;
	}

	public void setCollectiontype(String collectiontype) {
		this.collectiontype = collectiontype;
	}

	public String getCollectionname() {
		return collectionname;
	}

	public void setCollectionname(String collectionname) {
		this.collectionname = collectionname;
	}

	public String getDeptcode() {
		return deptcode;
	}

	public void setDeptcode(String deptcode) {
		this.deptcode = deptcode;
	}

	public String getSettlementno() {
		return settlementno;
	}

	public void setSettlementno(String settlementno) {
		this.settlementno = settlementno;
	}

	public String getTerraceno() {
		return terraceno;
	}

	public void setTerraceno(String terraceno) {
		this.terraceno = terraceno;
	}

	public String getPaytarget() {
		return paytarget;
	}

	public void setPaytarget(String paytarget) {
		this.paytarget = paytarget;
	}

	public String getTransferdesc() {
		return transferdesc;
	}

	public void setTransferdesc(String transferdesc) {
		this.transferdesc = transferdesc;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getFavorabledescribe() {
		return favorabledescribe;
	}

	public void setFavorabledescribe(String favorabledescribe) {
		this.favorabledescribe = favorabledescribe;
	}

	public String getProviderid() {
		return providerid;
	}

	public void setProviderid(String providerid) {
		this.providerid = providerid;
	}

	public String getDeptid() {
		return deptid;
	}

	public void setDeptid(String deptid) {
		this.deptid = deptid;
	}

	public String getSettlementnoname() {
		return settlementnoname;
	}

	public void setSettlementnoname(String settlementnoname) {
		this.settlementnoname = settlementnoname;
	}

	public String getTerracenoname() {
		return terracenoname;
	}

	public void setTerracenoname(String terracenoname) {
		this.terracenoname = terracenoname;
	}

	@Override
	public String toString() {
		return "INSBPaychannelmanager [paychannelid=" + paychannelid + ", agreementid=" + agreementid
				+ ", collectiontype=" + collectiontype + ", collectionname=" + collectionname + ", deptcode=" + deptcode
				+ ", settlementno=" + settlementno + ", terraceno=" + terraceno + ", paytarget=" + paytarget
				+ ", transferdesc=" + transferdesc + ", sort=" + sort + ", favorabledescribe=" + favorabledescribe
				+ ", providerid=" + providerid + ", deptid=" + deptid + ", settlementnoname=" + settlementnoname
				+ ", terracenoname=" + terracenoname + "]";
	}

	public List<String> getAgreementids() {
		return agreementids;
	}

	public void setAgreementids(List<String> agreementids) {
		this.agreementids = agreementids;
	}

	
	
	
}