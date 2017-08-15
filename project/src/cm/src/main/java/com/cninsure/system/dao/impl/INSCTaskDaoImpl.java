package com.cninsure.system.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.cninsure.system.dao.INSCTaskDao;
import com.cninsure.system.entity.INSCTask;

@Repository
public class INSCTaskDaoImpl extends BaseDaoImpl<INSCTask> implements
		INSCTaskDao {

	@Override
	public List<INSCTask> selectAll() {
		return this.sqlSessionTemplate.selectList(this.getSqlName("select"));
	}
	@Override
	public List<INSCTask> selectAll(Map<String, Object> params) {
		return this.sqlSessionTemplate.selectList(getSqlName("selectPage"),params);
	}

}