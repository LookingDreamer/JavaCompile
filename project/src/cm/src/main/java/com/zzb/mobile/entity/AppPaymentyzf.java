package com.zzb.mobile.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;

public class AppPaymentyzf extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 姓名
	 */
	private String name;
	/**
	 * 联系地址
	 */
	private String relationaddress;
	/**
	 * 联系方式
	 */
	private String phone;
	
	private String jobNum;
	/**
	 * 证件类型
	 */
	private String idcradType;
	/**
	 * 证件号
	 */
	private String idcardno;
	/**
	 *  开户行省份code
	 */
	private String khprovincecode;
	/**
	 *  开户行市code
	 */
	private String khcitycode;
	/**
	 * 开户行名称
	 */
	private String khcomname;

	
	
	/**
	 * 校验码
	 */
    private String valiadatecode;
	/**
	 * 有效期年
	 */
	private String periodyear;
	/**
	 * 有效期月
	 */
	private String periodmonth;
	/**
	 * 发卡银行
	 */
	private String fkbankname;
	/**
	 * 发卡银行code
	 */
	private String fkbankcode;
		
	/**
	 * 银行卡种类
	 */
	private String bankcardType;
	/**
	 * 银行卡卡号
	 */
	private String bankcardno;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRelationaddress() {
		return relationaddress;
	}
	public void setRelationaddress(String relationaddress) {
		this.relationaddress = relationaddress;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getJobNum() {
		return jobNum;
	}
	public void setJobNum(String jobNum) {
		this.jobNum = jobNum;
	}
	public String getIdcradType() {
		return idcradType;
	}
	public void setIdcradType(String idcradType) {
		this.idcradType = idcradType;
	}
	public String getIdcardno() {
		return idcardno;
	}
	public void setIdcardno(String idcardno) {
		this.idcardno = idcardno;
	}
	public String getKhprovincecode() {
		return khprovincecode;
	}
	public void setKhprovincecode(String khprovincecode) {
		this.khprovincecode = khprovincecode;
	}
	public String getKhcitycode() {
		return khcitycode;
	}
	public void setKhcitycode(String khcitycode) {
		this.khcitycode = khcitycode;
	}
	public String getKhcomname() {
		return khcomname;
	}
	public void setKhcomname(String khcomname) {
		this.khcomname = khcomname;
	}
	public String getValiadatecode() {
		return valiadatecode;
	}
	public void setValiadatecode(String valiadatecode) {
		this.valiadatecode = valiadatecode;
	}
	public String getPeriodyear() {
		return periodyear;
	}
	public void setPeriodyear(String periodyear) {
		this.periodyear = periodyear;
	}
	public String getPeriodmonth() {
		return periodmonth;
	}
	public void setPeriodmonth(String periodmonth) {
		this.periodmonth = periodmonth;
	}

	

	public String getFkbankname() {
		return fkbankname;
	}
	public void setFkbankname(String fkbankname) {
		this.fkbankname = fkbankname;
	}
	public String getFkbankcode() {
		return fkbankcode;
	}
	public void setFkbankcode(String fkbankcode) {
		this.fkbankcode = fkbankcode;
	}
	public String getBankcardType() {
		return bankcardType;
	}
	public void setBankcardType(String bankcardType) {
		this.bankcardType = bankcardType;
	}
	public String getBankcardno() {
		return bankcardno;
	}
	public void setBankcardno(String bankcardno) {
		this.bankcardno = bankcardno;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}


	
	
	
}
