package com.zzb.conf.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.conf.dao.INSCServicerDao;
import com.zzb.conf.entity.baseData.INSCServicerModel;

@Repository
public class INSCServicerDaoImpl extends BaseDaoImpl<INSCServicerModel> implements INSCServicerDao {

	@Override
	public List<INSCServicerModel> list() {
		return this.sqlSessionTemplate.selectList("com.zzb.conf.entity.INSCBaseData_select");
	}

}
