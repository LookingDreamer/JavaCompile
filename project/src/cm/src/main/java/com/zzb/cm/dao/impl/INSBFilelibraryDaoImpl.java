package com.zzb.cm.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.cm.dao.INSBFilelibraryDao;
import com.zzb.cm.entity.INSBFilelibrary;

@Repository
public class INSBFilelibraryDaoImpl extends BaseDaoImpl<INSBFilelibrary> implements
		INSBFilelibraryDao {

	@Override
	public List<INSBFilelibrary> selectByFilebusinessCode(String code) {
		return 	this.sqlSessionTemplate.selectList(this.getSqlName("selectByFilebusinessCode"),code);
	}

	@Override
	public List<INSBFilelibrary> selectListByMap(Map<String,Object> map) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectListBymap"),map);
	}

	@Override
	public List<INSBFilelibrary> selectAll() {
		return this.sqlSessionTemplate.selectList(this.getSqlName("select"));
	}
	
	@Override
	public List<INSBFilelibrary> selectAll(Map<String, Object> Params) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectPage"),Params);
	}

	@Override
	public int deleteIn(List<String> list){
		return this.sqlSessionTemplate.delete(this.getSqlName("deleteInIds"), list);
	}

}