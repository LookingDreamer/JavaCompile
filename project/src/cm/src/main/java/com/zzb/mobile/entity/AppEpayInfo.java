package com.zzb.mobile.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;

public class AppEpayInfo extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 姓名
	 */
	private String name;
	/**
	 * 证件地址
	 */
	private String idcardaddress;
	/**
	 * 银行预留电话
	 */
	private String phone;
	/**
	 * 工号
	 */
	private String jobNum;

	/**
	 * 证件号
	 */
	private String idcardno;
	/**
	 *  开户行省份
	 */
	private String khprovince;
	/**
	 *  开户行市
	 */
	private String khcity;

	/**
	 * 银行卡卡号
	 */
	private String bankcardno;
	
	/**
	 * 总支付金额
	 */
	private BigDecimal totalamount;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIdcardaddress() {
		return idcardaddress;
	}

	public void setIdcardaddress(String idcardaddress) {
		this.idcardaddress = idcardaddress;
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


	public String getIdcardno() {
		return idcardno;
	}

	public void setIdcardno(String idcardno) {
		this.idcardno = idcardno;
	}

	public String getKhprovince() {
		return khprovince;
	}

	public void setKhprovince(String khprovince) {
		this.khprovince = khprovince;
	}

	public String getKhcity() {
		return khcity;
	}

	public void setKhcity(String khcity) {
		this.khcity = khcity;
	}

	public String getBankcardno() {
		return bankcardno;
	}

	public void setBankcardno(String bankcardno) {
		this.bankcardno = bankcardno;
	}

	public BigDecimal getTotalamount() {
		return totalamount;
	}

	public void setTotalamount(BigDecimal totalamount) {
		this.totalamount = totalamount;
	}






	

}
