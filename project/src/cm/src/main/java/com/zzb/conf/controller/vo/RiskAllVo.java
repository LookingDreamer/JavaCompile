package com.zzb.conf.controller.vo;

import java.io.Serializable;
import java.util.List;

import com.zzb.conf.entity.INSBProvider;
import com.zzb.conf.entity.INSBRisk;
import com.zzb.conf.entity.INSBRiskitem;
import com.zzb.conf.entity.INSBRiskkind;

public class RiskAllVo implements Serializable {
	private static final long serialVersionUID = 1L;
	private INSBRisk risk;
	private INSBProvider provider;
	private List<INSBRiskitem> itemset;
	private List<INSBRiskkind> kindset;
	public INSBRisk getRisk() {
		return risk;
	}
	public void setRisk(INSBRisk risk) {
		this.risk = risk;
	}
	public List<INSBRiskitem> getItemset() {
		return itemset;
	}
	public void setItemset(List<INSBRiskitem> itemset) {
		this.itemset = itemset;
	}
	public List<INSBRiskkind> getKindset() {
		return kindset;
	}
	public void setKindset(List<INSBRiskkind> kindset) {
		this.kindset = kindset;
	}
	public INSBProvider getProvider() {
		return provider;
	}
	public void setProvider(INSBProvider provider) {
		this.provider = provider;
	}
}
