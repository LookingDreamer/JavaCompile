package com.zzb.extra.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.extra.dao.INSBActivityinfoDao;
import com.zzb.extra.entity.INSBActivityinfo;

@Repository
public class INSBActivityinfoDaoImpl extends BaseDaoImpl<INSBActivityinfo> implements
		INSBActivityinfoDao {

	@Override
	public  List<Map<Object, Object>> getList(Map map) {
		return this.sqlSessionTemplate.selectList("INSBActivityinfo_select", map);
	}
	@Override
	public Map findById(String id){
		return this.sqlSessionTemplate.selectOne("INSBActivityinfo_selectById", id);
	}
	
	@Override
	public void saveObject(INSBActivityinfo insbActivityinfo) {
		this.sqlSessionTemplate.insert("INSBActivityinfo_insert",insbActivityinfo);
	}

	@Override
	public void updateObject(INSBActivityinfo insbActivityinfo) {
		this.sqlSessionTemplate.update("INSBActivityinfo_updateById", insbActivityinfo);
	}

	@Override
	public void deleteObect(String id) {
		this.sqlSessionTemplate.delete("INSBActivityinfo_deleteById", id);
	}

}