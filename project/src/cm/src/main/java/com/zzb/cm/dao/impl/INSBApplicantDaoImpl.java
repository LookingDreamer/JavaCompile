package com.zzb.cm.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.cm.dao.INSBApplicantDao;
import com.zzb.cm.entity.INSBApplicant;

@Repository
public class INSBApplicantDaoImpl extends BaseDaoImpl<INSBApplicant> implements
		INSBApplicantDao {

	@Override
	public void updateByTaskId(INSBApplicant iNSBApplicant) {
		this.sqlSessionTemplate.update(this.getSqlName("updateByTaskId"),iNSBApplicant);
		
	}

	@Override
	public INSBApplicant selectByTaskID(String taskid) {
		//return 	this.sqlSessionTemplate.selectOne(this.getSqlName("selectByTaskID"),taskid);
		INSBApplicant iNSBApplicant = new INSBApplicant();
		iNSBApplicant.setTaskid(taskid);
		return this.selectOne(iNSBApplicant);

	}

	@Override
	public List<Map<Object, Object>> getCheckData(String taskID) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("getcheckdata"),taskID);
	}

}