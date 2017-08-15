package com.zzb.cm.Interface.entity.cif.model;


import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;


@SuppressWarnings("serial")
public class CarKind extends BaseEntity implements Identifiable{
	
	String vehicleno;//车牌号
	String kindcode;//险别编码
	String kindname; //险别名称
	double amount;   //保额
	double prem;     //保费
	double discount;  //折扣
	String supplierid;//保险公司
	String source;    //访问来源
	String policyno;  //保单号

	public String getVehicleno() {
		return vehicleno;
	}
	public void setVehicleno(String vehicleno) {
		this.vehicleno = vehicleno;
	}
	public String getKindcode() {
		return kindcode;
	}
	public void setKindcode(String kindcode) {
		this.kindcode = kindcode;
	}
	public String getKindname() {
		return kindname;
	}
	public void setKindname(String kindname) {
		this.kindname = kindname;
	}
	

	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public double getPrem() {
		return prem;
	}
	public void setPrem(double prem) {
		this.prem = prem;
	}
	public double getDiscount() {
		return discount;
	}
	public void setDiscount(double discount) {
		this.discount = discount;
	}
	public String getSupplierid() {
		return supplierid;
	}
	public void setSupplierid(String supplierid) {
		this.supplierid = supplierid;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public String getPolicyno() {
		return policyno;
	}
	public void setPolicyno(String policyno) {
		this.policyno = policyno;
	}
	@Override
	public String toString() {
		return "CarKind [vehicleno=" + vehicleno + ", kindcode=" + kindcode
				+ ", kindname=" + kindname + ", amount=" + amount + ", prem="
				+ prem + ", discount=" + discount + ", supplierid="
				+ supplierid + ", source=" + source + ", policyno=" + policyno
				+ "]";
	}
	
	
}
