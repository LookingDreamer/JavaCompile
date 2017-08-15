package com.zzb.cm.dao.impl;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.cm.dao.INSBLastyearinsurestatusDao;
import com.zzb.cm.entity.INSBLastyearinsurestatus;

@Repository
public class INSBLastyearinsurestatusDaoImpl extends BaseDaoImpl<INSBLastyearinsurestatus> implements
		INSBLastyearinsurestatusDao {

	@Override
	public INSBLastyearinsurestatus isHaveOneDate(String taskid) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("isHaveOneDate"), taskid);
	}

}