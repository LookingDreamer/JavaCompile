package com.zzb.mobile.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;

public class Insbpaymenttransaction extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 日期
	 */
	private String date;
	/**
	 * 前缀
	 */
	private String prfix;
	/**
	 * 流水号
	 */
	private int transaction;

	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getPrfix() {
		return prfix;
	}
	public void setPrfix(String prfix) {
		this.prfix = prfix;
	}
	public int getTransaction() {
		return transaction;
	}
	public void setTransaction(int transaction) {
		this.transaction = transaction;
	}
	@Override
	public String toString() {
		return "Insbpaymenttransaction [date=" + date + ", prfix=" + prfix
				+ ", transaction=" + transaction + "]";
	}





}
