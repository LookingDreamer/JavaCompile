package com.zzb.extra.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.extra.dao.INSBUserprizeDao;
import com.zzb.extra.entity.INSBUserprize;

@Repository
public class INSBUserprizeDaoImpl extends BaseDaoImpl<INSBUserprize> implements
		INSBUserprizeDao {

	
	@Override
	public List<Map<Object, Object>> getList(Map map) {
		return this.sqlSessionTemplate.selectList("INSBUserprize_select", map);
	}
	
	@Override
	public INSBUserprize findById(String id){
		return this.sqlSessionTemplate.selectOne("INSBUserprize_selectById", id);
	}
	
	
	@Override
	public void saveObject(INSBUserprize insbUserprize) {
		this.sqlSessionTemplate.insert("INSBUserprize_insert",insbUserprize);
	}

	@Override
	public void updateObject(INSBUserprize insbUserprize) {
		this.sqlSessionTemplate.update("INSBUserprize_updateById", insbUserprize);
	}

	@Override
	public void deleteObect(String id) {
		this.sqlSessionTemplate.delete("INSBUserprize_deleteById", id);
	}

}