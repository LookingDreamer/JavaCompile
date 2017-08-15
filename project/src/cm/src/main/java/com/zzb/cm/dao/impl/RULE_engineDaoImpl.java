package com.zzb.cm.dao.impl;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.cninsure.core.dao.constants.SqlId;
import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.cninsure.core.exception.DaoException;
import com.zzb.cm.dao.RULE_engineDao;
import com.zzb.cm.entity.RULE_engine;

@Repository
public class RULE_engineDaoImpl extends BaseDaoImpl<RULE_engine> implements
		RULE_engineDao {
	@Override
	public void insert(RULE_engine ruleEngine) {
		sqlSessionTemplate.insert(getSqlName(SqlId.SQL_INSERT), ruleEngine);
	}
}