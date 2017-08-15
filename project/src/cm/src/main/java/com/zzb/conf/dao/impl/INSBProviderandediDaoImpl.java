package com.zzb.conf.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.conf.dao.INSBProviderandediDao;
import com.zzb.conf.entity.INSBProviderandedi;

@Repository
public class INSBProviderandediDaoImpl extends BaseDaoImpl<INSBProviderandedi> implements
		INSBProviderandediDao {

	@Override
	public int addproandedi(INSBProviderandedi bean) {
		return this.sqlSessionTemplate.insert(this.getSqlName("insert"),bean);
	}

	@Override
	public int deleteRelation(String ediid) {
		return this.sqlSessionTemplate.delete(this.getSqlName("deleterelation"),ediid);
	}
	
	@Override
	public List<Map<String,String>> selectByPid(String providerid) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectByPid"),providerid);
	}

}