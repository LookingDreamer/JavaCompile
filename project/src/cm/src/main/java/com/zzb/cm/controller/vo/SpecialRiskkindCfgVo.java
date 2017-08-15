package com.zzb.cm.controller.vo;

import java.util.List;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;

public class SpecialRiskkindCfgVo  extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;
	
	//主任务id
	private String mInstanceid;
	
	//特殊险别配置信息的集合
	private List<SpecialRiskkindCfg> spcRiskkindCfg;
	
	//保险公司编号
	private String inscomcode;
	
	//险别编码
	private String riskkindcode;
	
	//是否修改到所有单方标志 Y表示是
	private String editToAll;
	
	//保险公司列表，用,分割
	private String inscomcodeList;

	public String getmInstanceid() {
		return mInstanceid;
	}

	public void setmInstanceid(String mInstanceid) {
		this.mInstanceid = mInstanceid;
	}

	public List<SpecialRiskkindCfg> getSpcRiskkindCfg() {
		return spcRiskkindCfg;
	}

	public void setSpcRiskkindCfg(List<SpecialRiskkindCfg> spcRiskkindCfg) {
		this.spcRiskkindCfg = spcRiskkindCfg;
	}

	public String getInscomcode() {
		return inscomcode;
	}

	public void setInscomcode(String inscomcode) {
		this.inscomcode = inscomcode;
	}

	public String getRiskkindcode() {
		return riskkindcode;
	}

	public void setRiskkindcode(String riskkindcode) {
		this.riskkindcode = riskkindcode;
	}

	public String getEditToAll() {
		return editToAll;
	}

	public void setEditToAll(String editToAll) {
		this.editToAll = editToAll;
	}

	public String getInscomcodeList() {
		return inscomcodeList;
	}

	public void setInscomcodeList(String inscomcodeList) {
		this.inscomcodeList = inscomcodeList;
	}

	public SpecialRiskkindCfgVo() {
		super();
	}
	
}
