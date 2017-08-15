package com.zzb.conf.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.conf.dao.INSBOrderlistenpushDao;
import com.zzb.conf.entity.INSBOrderlistenpush;

@Repository
public class INSBOrderlistenpushDaoImpl extends BaseDaoImpl<INSBOrderlistenpush> implements
		INSBOrderlistenpushDao {

	@Override
	public INSBOrderlistenpush selectDataByTaskId(String taskId) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectCountByTaskId"), taskId);
	}

	@Override
	public INSBOrderlistenpush selectDataBySubTaskId(String subTaskId) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectDataBySubTaskId"), subTaskId);
	}

	@Override
	public INSBOrderlistenpush queryOrderListen(Map<String, String> param) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("queryOrderListen"), param);
	}

	@Override
	public List<INSBOrderlistenpush> queryListBytype(String type) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("queryListBytype"), type);
	}

}