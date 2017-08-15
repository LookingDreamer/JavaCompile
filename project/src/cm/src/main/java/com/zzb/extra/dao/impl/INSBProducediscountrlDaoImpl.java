package com.zzb.extra.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.extra.dao.INSBProducediscountrlDao;
import com.zzb.extra.entity.INSBProducediscountrl;

@Repository
public class INSBProducediscountrlDaoImpl extends BaseDaoImpl<INSBProducediscountrl> implements
		INSBProducediscountrlDao {
	@Override
	public  List<Map<Object, Object>> getList(Map map) {
		return this.sqlSessionTemplate.selectList("INSBProducediscountrl_select", map);
	}
	@Override
	public Map findById(String id){
		return this.sqlSessionTemplate.selectOne("INSBProducediscountrl_selectById", id);
	}
	
	@Override
	public void saveObject(INSBProducediscountrl iNSBProducediscountrl) {
		this.sqlSessionTemplate.insert("INSBProducediscountrl_insert",iNSBProducediscountrl);
	}

	@Override
	public void updateObject(INSBProducediscountrl iNSBProducediscountrl) {
		this.sqlSessionTemplate.update("INSBProducediscountrl_updateById", iNSBProducediscountrl);
	}

	@Override
	public void deleteObect(String id) {
		this.sqlSessionTemplate.delete("INSBProducediscountrl_deleteById", id);
	}
	
	@Override
	public  List<Map<String, String>> getMap(String type){
		return this.sqlSessionTemplate.selectList("selectAgreement");
	}
}