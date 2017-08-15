package com.zzb.conf.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.conf.dao.INSBInterfaceDao;
import com.zzb.conf.entity.INSBInterface;

@Repository
public class INSBInterfaceDaoImpl extends BaseDaoImpl<INSBInterface> implements
		INSBInterfaceDao {

	@Override
	public List<INSBInterface> selectAll() {
		// TODO Auto-generated method stub
		return this.sqlSessionTemplate.selectList(getSqlName("select"));
	}
	@Override
	public List<INSBInterface> selectAll(Map<String, Object> params) {
		return this.sqlSessionTemplate.selectList(getSqlName("selectPage"),params);
	}

}