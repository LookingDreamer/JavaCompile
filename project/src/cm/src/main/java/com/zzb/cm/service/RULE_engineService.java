package com.zzb.cm.service;

import com.cninsure.core.dao.BaseService;
import com.zzb.cm.entity.RULE_engine;

public interface RULE_engineService extends BaseService<RULE_engine> {
	public void saveOrudpateRuleEngine(RULE_engine ruleEngine) throws Exception;

}