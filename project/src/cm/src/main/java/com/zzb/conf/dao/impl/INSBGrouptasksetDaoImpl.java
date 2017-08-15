package com.zzb.conf.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.conf.dao.INSBGrouptasksetDao;
import com.zzb.conf.entity.INSBGrouptaskset;

@Repository
public class INSBGrouptasksetDaoImpl extends BaseDaoImpl<INSBGrouptaskset> implements
		INSBGrouptasksetDao {

	@Override
	public List<INSBGrouptaskset> selectByGroupId(String groupid) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectByGroupId"),groupid);
	}

	@Override
	public List<INSBGrouptaskset> selectPageByParam(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectPageList"), map);
	}

	@Override
	public long selectPageCountByParam(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectPageCount"), map);
	}

	@Override
	public List<INSBGrouptaskset> selectByTaskSetId(String tasksetid) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectByTaskSetId"), tasksetid);
	}

	@Override
	public void deleteByGroupId(String groupid) {
		this.sqlSessionTemplate.delete(this.getSqlName("deleteByGroupId"), groupid);
	}

	@Override
	public List<String> selectGroupIdsByTaskSetId4Task(List<String> ids) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectGroupIdsByTaskSetId4Task"), ids);
	}

}