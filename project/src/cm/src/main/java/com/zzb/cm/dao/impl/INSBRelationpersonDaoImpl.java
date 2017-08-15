package com.zzb.cm.dao.impl;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.cm.dao.INSBRelationpersonDao;
import com.zzb.cm.entity.INSBRelationperson;

@Repository
public class INSBRelationpersonDaoImpl extends BaseDaoImpl<INSBRelationperson> implements
		INSBRelationpersonDao {

	@Override
	public INSBRelationperson selectByTaskID(String taskid) {
		return 	this.sqlSessionTemplate.selectOne(this.getSqlName("selectByTaskID"),taskid);
		
	}
	@Override
	public void updateByTaskId(INSBRelationperson iNSBRelationperson) {
		this.sqlSessionTemplate.update(this.getSqlName("updateByTaskId"),iNSBRelationperson);
	}

}