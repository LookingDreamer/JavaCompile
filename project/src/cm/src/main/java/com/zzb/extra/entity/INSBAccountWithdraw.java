package com.zzb.extra.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;


public class INSBAccountWithdraw extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

	/**
	 * 账户id
	 */
	private String accountid;

	/**
	 * 提现金额
	 */
	private Double amount;

	/**
	 * 银行名称
	 */
	private String bankname;

    /**
     * 开户支行
     */
    private String branch;

	/**
	 * 银行卡号
	 */
	private String cardnumber;

	/**
	 * 状态
	 */
	private String status;

	public String getAccountid() {
		return accountid;
	}

	public void setAccountid(String accountid) {
		this.accountid = accountid;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getBankname() {
		return bankname;
	}

	public void setBankname(String bankname) {
		this.bankname = bankname;
	}

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

	public String getCardnumber() {
		return cardnumber;
	}

	public void setCardnumber(String cardnumber) {
		this.cardnumber = cardnumber;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}