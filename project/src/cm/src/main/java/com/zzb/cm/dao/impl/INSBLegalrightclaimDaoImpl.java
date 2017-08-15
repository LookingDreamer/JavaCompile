package com.zzb.cm.dao.impl;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.cm.dao.INSBLegalrightclaimDao;
import com.zzb.cm.entity.INSBLegalrightclaim;

@Repository
public class INSBLegalrightclaimDaoImpl extends BaseDaoImpl<INSBLegalrightclaim> implements
		INSBLegalrightclaimDao {

	@Override
	public INSBLegalrightclaim selectByTaskID(String taskid) {
		//return 	this.sqlSessionTemplate.selectOne(this.getSqlName("selectByTaskID"),taskid);
		INSBLegalrightclaim info = new INSBLegalrightclaim();
		info.setTaskid(taskid);
		return this.selectOne(info);

	}

	@Override
	public void updateByTaskId(INSBLegalrightclaim iNSBLegalrightclaim) {
		this.sqlSessionTemplate.update(this.getSqlName("updateByTaskId"),iNSBLegalrightclaim);
		
	}

}