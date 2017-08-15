package com.zzb.cm.dao.impl;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.cm.dao.INSBSupplementDao;
import com.zzb.cm.entity.INSBSupplement;

@Repository
public class INSBSupplementDaoImpl extends BaseDaoImpl<INSBSupplement> implements
		INSBSupplementDao {

	@Override
	public void updateBykeyidandproviderValue(INSBSupplement model) {
		this.sqlSessionTemplate.update(this.getSqlName("updateBykeyidandproviderValue"), model); 
	}

}