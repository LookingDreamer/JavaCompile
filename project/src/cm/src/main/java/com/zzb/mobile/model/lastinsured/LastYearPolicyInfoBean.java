package com.zzb.mobile.model.lastinsured;

import java.io.Serializable;

import net.sf.json.JSONArray;

public class LastYearPolicyInfoBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String status;
	String message;
	LastYearPersonBean carowner;//车主信息
	LastYearCarinfoBean lastYearCarinfoBean;//车辆信息
	JSONArray carModels;//车型信息
	LastYearSupplierBean lastYearSupplierBean;//保险公司名称
	LastYearPolicyBean lastYearPolicyBean;//保单信息
	JSONArray lastYearRiskinfos;//险种信息
	LastYearClaimBean lastYearClaimBean;//出险信息

	/**
	 * cif_find:0       0：没查到 1：自有数据查到 2:续保查询查到的 3：社会化查到的
	 cif_owncount：1  自有数据查询增加1次
	 cif_renewalcount：0   续保查询数据不增加
	 cif_socialcount：1   社会化接口查询增加1次
	 * @return
     */

	private int cif_find;
	private int cif_owncount;
	private int cif_renewalcount;
	private int cif_socialcount;

	public int getCif_find() {
		return cif_find;
	}

	public void setCif_find(int cif_find) {
		this.cif_find = cif_find;
	}

	public int getCif_owncount() {
		return cif_owncount;
	}

	public void setCif_owncount(int cif_owncount) {
		this.cif_owncount = cif_owncount;
	}

	public int getCif_renewalcount() {
		return cif_renewalcount;
	}

	public void setCif_renewalcount(int cif_renewalcount) {
		this.cif_renewalcount = cif_renewalcount;
	}

	public int getCif_socialcount() {
		return cif_socialcount;
	}

	public void setCif_socialcount(int cif_socialcount) {
		this.cif_socialcount = cif_socialcount;
	}

	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public LastYearPolicyBean getLastYearPolicyBean() {
		return lastYearPolicyBean;
	}
	public void setLastYearPolicyBean(LastYearPolicyBean lastYearPolicyBean) {
		this.lastYearPolicyBean = lastYearPolicyBean;
	}
	public LastYearPersonBean getCarowner() {
		return carowner;
	}
	public void setCarowner(LastYearPersonBean carowner) {
		this.carowner = carowner;
	}
	public LastYearCarinfoBean getLastYearCarinfoBean() {
		return lastYearCarinfoBean;
	}
	public void setLastYearCarinfoBean(LastYearCarinfoBean lastYearCarinfoBean) {
		this.lastYearCarinfoBean = lastYearCarinfoBean;
	}
	public JSONArray getCarModels() {
		return carModels;
	}
	public void setCarModels(JSONArray carModels) {
		this.carModels = carModels;
	}
	public void setLastYearSupplierBean(LastYearSupplierBean lastYearSupplierBean) {
		this.lastYearSupplierBean = lastYearSupplierBean;
	}
	public LastYearClaimBean getLastYearClaimBean() {
		return lastYearClaimBean;
	}
	public void setLastYearClaimBean(LastYearClaimBean lastYearClaimBean) {
		this.lastYearClaimBean = lastYearClaimBean;
	}
	public JSONArray getLastYearRiskinfos() {
		return lastYearRiskinfos;
	}
	public void setLastYearRiskinfos(JSONArray lastYearRiskinfos) {
		this.lastYearRiskinfos = lastYearRiskinfos;
	}
	public LastYearSupplierBean getLastYearSupplierBean() {
		return lastYearSupplierBean;
	}

	

}
