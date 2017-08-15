package com.zzb.conf.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;


public class INSBCertification extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

	/**
	 * 代理人工号
	 */
	private String agentnum;
	
	/**
	 * 身份证正面照
	 */
	private String idcardpositive;
	
	/**
	 * 身份证反面照
	 */
	private String idcardopposite;
	
	/**
	 * 主营业务
	 */
	private String mainbiz;
	
	/**
	 * 银行名称
	 */
	private String bankname;
	/**
	 * 开户行
	 */
	private String depositbank;
	/**
	 * 开户名
	 */
	private String accountname;
	/**
	 * 账号
	 */
	private String accountnum;
	
	/**
	 * 银行卡正面照
	 */
	private String bankcardpositive;
	
	/**
	 * 资格证照片页
	 */
	private String qualificationpositive;
	
	/**
	 * 资格证照片页
	 */
	private String qualificationpage;
	
	/**
	 * 所属机构id
	 */
	private String deptid;
	/**
	 * 是否同意条款 1同意/0不同意
	 */
	private Integer agree;
	/**
	 * 审核状态   2审核不通过/1审核通过/0初始值
	 */
	private Integer status;
	/**
	 * 指定操作员
	 * ALTER TABLE `insbcertification`
	 * ADD COLUMN `designatedgroup`  varchar(20) NULL AFTER `designatedoperator`;
	 */
	private String designatedoperator;
	
	/**
	 * 指定处理组
	 */
	private String designatedgroup;

	private String rank;

	/**
	 * 银行卡号
	 */
	private String bankcard;

	/**
	 * 银行卡号所属银行
	 */
	private String belongs2bank;

	public String getBankcard() {
		return bankcard;
	}

	public void setBankcard(String bankcard) {
		this.bankcard = bankcard;
	}

	public String getBelongs2bank() {
		return belongs2bank;
	}

	public void setBelongs2bank(String belongs2bank) {
		this.belongs2bank = belongs2bank;
	}

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	public String getDesignatedgroup() {
		return designatedgroup;
	}

	public void setDesignatedgroup(String designatedgroup) {
		this.designatedgroup = designatedgroup;
	}

	public String getDesignatedoperator() {
		return designatedoperator;
	}

	public void setDesignatedoperator(String designatedoperator) {
		this.designatedoperator = designatedoperator;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}


	public String getAgentnum() {
		return agentnum;
	}

	public void setAgentnum(String agentnum) {
		this.agentnum = agentnum;
	}

	public String getIdcardpositive() {
		return idcardpositive;
	}

	public String getDeptid() {
		return deptid;
	}

	public void setDeptid(String deptid) {
		this.deptid = deptid;
	}

	public void setIdcardpositive(String idcardpositive) {
		this.idcardpositive = idcardpositive;
	}

	public String getIdcardopposite() {
		return idcardopposite;
	}

	public void setIdcardopposite(String idcardopposite) {
		this.idcardopposite = idcardopposite;
	}

	public String getMainbiz() {
		return mainbiz;
	}

	public void setMainbiz(String mainbiz) {
		this.mainbiz = mainbiz;
	}

	public String getBankname() {
		return bankname;
	}

	public void setBankname(String bankname) {
		this.bankname = bankname;
	}

	public String getDepositbank() {
		return depositbank;
	}

	public void setDepositbank(String depositbank) {
		this.depositbank = depositbank;
	}

	public String getAccountname() {
		return accountname;
	}

	public void setAccountname(String accountname) {
		this.accountname = accountname;
	}

	public String getAccountnum() {
		return accountnum;
	}

	public void setAccountnum(String accountnum) {
		this.accountnum = accountnum;
	}

	public String getBankcardpositive() {
		return bankcardpositive;
	}

	public void setBankcardpositive(String bankcardpositive) {
		this.bankcardpositive = bankcardpositive;
	}

	public String getQualificationpositive() {
		return qualificationpositive;
	}

	public void setQualificationpositive(String qualificationpositive) {
		this.qualificationpositive = qualificationpositive;
	}

	public String getQualificationpage() {
		return qualificationpage;
	}

	public void setQualificationpage(String qualificationpage) {
		this.qualificationpage = qualificationpage;
	}

	public Integer getAgree() {
		return agree;
	}

	public void setAgree(Integer agree) {
		this.agree = agree;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "INSBCertification [agentnum=" + agentnum + ", idcardpositive="
				+ idcardpositive + ", idcardopposite=" + idcardopposite
				+ ", mainbiz=" + mainbiz + ", bankname=" + bankname
				+ ", depositbank=" + depositbank + ", accountname="
				+ accountname + ", accountnum=" + accountnum
				+ ", bankcardpositive=" + bankcardpositive
				+ ", qualificationpositive=" + qualificationpositive
				+ ", qualificationpage=" + qualificationpage + ", deptid="
				+ deptid + ", agree=" + agree + ", status=" + status
				+ ", designatedoperator=" + designatedoperator + "]";
	}



}