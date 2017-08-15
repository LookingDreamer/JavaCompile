package com.zzb.cm.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;


public class INSBInvoiceinfo extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

	/**
	 * 任务id
	 */
	private String taskid;

	/**
	 * 保险公司代码
	 */
	private String inscomcode;

	/**
	 * 发票信息 0普通发票 1增值税发票 2普通发票(需资料)
	 */
	private Integer invoicetype;

	/**
	 * 开户银行名称
	 */
	private String bankname;

	/**
	 * 银行账号
	 */
	private String accountnumber;

	/**
	 * 纳税人识别号/统一社会信用代码
	 */
	private String identifynumber;

	/**
	 * 纳税登记电话
	 */
	private String registerphone;

	/**
	 * 纳税登记地址
	 */
	private String registeraddress;

	/**
	 * 电子邮箱
	 */
	private String email;

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getInscomcode() {
		return inscomcode;
	}

	public void setInscomcode(String inscomcode) {
		this.inscomcode = inscomcode;
	}

	public Integer getInvoicetype() {
		return invoicetype;
	}

	public void setInvoicetype(Integer invoicetype) {
		this.invoicetype = invoicetype;
	}

	public String getBankname() {
		return bankname;
	}

	public void setBankname(String bankname) {
		this.bankname = bankname;
	}

	public String getAccountnumber() {
		return accountnumber;
	}

	public void setAccountnumber(String accountnumber) {
		this.accountnumber = accountnumber;
	}

	public String getIdentifynumber() {
		return identifynumber;
	}

	public void setIdentifynumber(String identifynumber) {
		this.identifynumber = identifynumber;
	}

	public String getRegisterphone() {
		return registerphone;
	}

	public void setRegisterphone(String registerphone) {
		this.registerphone = registerphone;
	}

	public String getRegisteraddress() {
		return registeraddress;
	}

	public void setRegisteraddress(String registeraddress) {
		this.registeraddress = registeraddress;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	} 	
}