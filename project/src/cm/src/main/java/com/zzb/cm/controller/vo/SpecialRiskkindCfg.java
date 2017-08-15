package com.zzb.cm.controller.vo;

import com.cninsure.core.dao.domain.BaseEntity;
import com.cninsure.core.dao.domain.Identifiable;

public class SpecialRiskkindCfg  extends BaseEntity implements Identifiable {
	private static final long serialVersionUID = 1L;
	
	//特殊险别配置信息名称
	private String cfgKey;
	
	//特殊险别配置信息值
	private String cfgValue;

	public String getCfgKey() {
		return cfgKey;
	}

	public void setCfgKey(String cfgKey) {
		this.cfgKey = cfgKey;
	}

	public String getCfgValue() {
		return cfgValue;
	}

	public void setCfgValue(String cfgValue) {
		this.cfgValue = cfgValue;
	}

	public SpecialRiskkindCfg() {
		super();
	}
	
}
