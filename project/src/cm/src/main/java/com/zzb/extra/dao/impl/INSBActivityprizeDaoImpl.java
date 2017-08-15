package com.zzb.extra.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.extra.dao.INSBActivityprizeDao;
import com.zzb.extra.entity.INSBActivityprize;

@Repository
public class INSBActivityprizeDaoImpl extends BaseDaoImpl<INSBActivityprize> implements
		INSBActivityprizeDao {

	@Override
	public  List<Map<Object, Object>> getList(Map map) {
		return this.sqlSessionTemplate.selectList("INSBActivityprize_select", map);
	}
	@Override
	public Map findById(String id){
		return this.sqlSessionTemplate.selectOne("INSBActivityprize_selectById", id);
	}
	
	@Override
	public void saveObject(INSBActivityprize insbActivityprize) {
		this.sqlSessionTemplate.insert("INSBActivityprize_insert",insbActivityprize);
	}

	@Override
	public void updateObject(INSBActivityprize insbActivityprize) {
		this.sqlSessionTemplate.update("INSBActivityprize_updateById", insbActivityprize);
	}

	@Override
	public void deleteObect(String id) {
		this.sqlSessionTemplate.delete("INSBActivityprize_deleteById", id);
	}
	
	@Override
	public  List<Map<String, String>> getMap(String type){
		String value="";
		if(type.equals("A")){
			value="selectActivit";
		}
		if(type.equals("P")){
			value="selectPrize";
		}
		return this.sqlSessionTemplate.selectList(value);
	}
	
}