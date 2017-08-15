package com.zzb.extra.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;


public class INSBAccountDetails extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

	/**
	 * 账户id
	 */
	private String accountid;

	/**
	 * 收支类型
	 */
	private String fundtype;

	/**
	 * 收支来源
	 */
	private String fundsource;

	/**
	 * 金额
	 */
	private Double amount;

	/**
	 * 余额
	 */
	private Double balance;

	/**
	 * 任务号
	 */
	private String taskid;

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}

	public String getAccountid() {
		return accountid;
	}

	public void setAccountid(String accountid) {
		this.accountid = accountid;
	}

	public String getFundtype() {
		return fundtype;
	}

	public void setFundtype(String fundtype) {
		this.fundtype = fundtype;
	}

	public String getFundsource() {
		return fundsource;
	}

	public void setFundsource(String fundsource) {
		this.fundsource = fundsource;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}
}