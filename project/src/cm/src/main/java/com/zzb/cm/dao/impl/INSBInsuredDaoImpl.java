package com.zzb.cm.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.cm.dao.INSBInsuredDao;
import com.zzb.cm.entity.INSBInsured;

@Repository
public class INSBInsuredDaoImpl extends BaseDaoImpl<INSBInsured> implements INSBInsuredDao {

	@Override
	public List<INSBInsured> selectInsuredList(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("select"), map);
	}

	@Override
	public INSBInsured selectInsuredByTaskId(String taskid) {
		//return this.sqlSessionTemplate.selectOne(this.getSqlName("selectByTaskId"), taskid);
		INSBInsured info = new INSBInsured();
		info.setTaskid(taskid);
		return this.selectOne(info);
	}

	@Override
	public void deletebyID(String id) {
		this.sqlSessionTemplate.delete(this.getSqlName("deletebyID"), id);
	}

}