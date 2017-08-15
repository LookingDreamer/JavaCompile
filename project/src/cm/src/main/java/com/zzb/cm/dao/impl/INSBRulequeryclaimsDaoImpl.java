package com.zzb.cm.dao.impl;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.cm.dao.INSBRulequeryclaimsDao;
import com.zzb.cm.entity.INSBRulequeryclaims;

import java.util.List;

@Repository
public class INSBRulequeryclaimsDaoImpl extends BaseDaoImpl<INSBRulequeryclaims> implements
		INSBRulequeryclaimsDao {

	@Override
	public List<INSBRulequeryclaims> selectPolicy(String taskid) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectPolicy"),taskid);
	}
}