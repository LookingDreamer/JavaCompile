package com.zzb.conf.entity;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;


public class INSBRiskrenewalitem extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;

	/**
	 * 险种ID
	 */
	private String riskid;

	/**
	 * 
	 */
	private String reitemname;

	/**
	 * 单买交强
	 */
	private String istraffic;

	/**
	 * 单买商业
	 */
	private String isbusiness;

	/**
	 * 商业+交强
	 */
	private String istrafficandbusiness;

	/**
	 * 提示信息
	 */
	private String presentation;

	public String getRiskid() {
		return riskid;
	}

	public void setRiskid(String riskid) {
		this.riskid = riskid;
	}

	public String getReitemname() {
		return reitemname;
	}

	public void setReitemname(String reitemname) {
		this.reitemname = reitemname;
	}

	public String getIstraffic() {
		return istraffic;
	}

	public void setIstraffic(String istraffic) {
		this.istraffic = istraffic;
	}

	public String getIsbusiness() {
		return isbusiness;
	}

	public void setIsbusiness(String isbusiness) {
		this.isbusiness = isbusiness;
	}

	public String getIstrafficandbusiness() {
		return istrafficandbusiness;
	}

	public void setIstrafficandbusiness(String istrafficandbusiness) {
		this.istrafficandbusiness = istrafficandbusiness;
	}

	public String getPresentation() {
		return presentation;
	}

	public void setPresentation(String presentation) {
		this.presentation = presentation;
	}

}