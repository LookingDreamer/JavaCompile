package com.zzb.cm.dao.impl;

import com.zzb.cm.entity.INSBRulequeryclaims;
import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.cm.dao.INSBRulequeryrepeatinsuredDao;
import com.zzb.cm.entity.INSBRulequeryrepeatinsured;

import java.util.List;

@Repository
public class INSBRulequeryrepeatinsuredDaoImpl extends BaseDaoImpl<INSBRulequeryrepeatinsured> implements
		INSBRulequeryrepeatinsuredDao {

	@Override
	public List<INSBRulequeryrepeatinsured> selectPolicy(String taskid) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectPolicy"),taskid);

	}

}