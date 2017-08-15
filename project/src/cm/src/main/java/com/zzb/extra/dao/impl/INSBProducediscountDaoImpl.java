package com.zzb.extra.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.extra.dao.INSBProducediscountDao;
import com.zzb.extra.entity.INSBProducediscount;

@Repository
public class INSBProducediscountDaoImpl extends BaseDaoImpl<INSBProducediscount> implements
		INSBProducediscountDao {
	@Override
	public  List<Map<Object, Object>> getList(Map map) {
		return this.sqlSessionTemplate.selectList("INSBProducediscount_select", map);
	}
	@Override
	public Map findById(String id){
		return this.sqlSessionTemplate.selectOne("INSBProducediscount_selectById", id);
	}
	
	@Override
	public void saveObject(INSBProducediscount iNSBProducediscount) {
		this.sqlSessionTemplate.insert("INSBProducediscount_insert",iNSBProducediscount);
	}

	@Override
	public void updateObject(INSBProducediscount iNSBProducediscount) {
		this.sqlSessionTemplate.update("INSBProducediscount_updateById", iNSBProducediscount);
	}

	@Override
	public void deleteObect(String id) {
		this.sqlSessionTemplate.delete("INSBProducediscount_deleteById", id);
	}
	
	@Override
	public  List<Map<String, String>> getMap(String type){
		return this.sqlSessionTemplate.selectList("selectAgreement");
	}
}