package com.zzb.cm.dao.impl;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.cm.dao.INSBUserremarkDao;
import com.zzb.cm.entity.INSBUserremark;

@Repository
public class INSBUserremarkDaoImpl extends BaseDaoImpl<INSBUserremark> implements
		INSBUserremarkDao {

	@Override
	public INSBUserremark selectByTaskId(String taskid) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectByTaskId"),taskid);
	}

}