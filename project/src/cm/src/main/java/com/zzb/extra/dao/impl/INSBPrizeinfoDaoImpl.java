package com.zzb.extra.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.extra.dao.INSBPrizeinfoDao;
import com.zzb.extra.entity.INSBPrizeinfo;

@Repository
public class INSBPrizeinfoDaoImpl extends BaseDaoImpl<INSBPrizeinfo> implements
		INSBPrizeinfoDao {
	
	@Override
	public List<Map<Object, Object>> getList(Map map) {
		return this.sqlSessionTemplate.selectList("INSBPrizeinfo_select", map);
	}

	@Override
	public void saveObject(INSBPrizeinfo insbPrizeinfo) {
		this.sqlSessionTemplate.insert("INSBPrizeinfo_insert", insbPrizeinfo);
	}

	
	public Map findById(String id){
		return this.sqlSessionTemplate.selectOne("INSBPrizeinfo_selectById", id);
	}
	@Override
	public void deleteObect(String id) {
		this.sqlSessionTemplate.delete("INSBPrizeinfo_deleteById", id);
	}

	@Override
	public void updateObject(INSBPrizeinfo insbPrizeinfo) {
		this.sqlSessionTemplate.update("INSBPrizeinfo_updateById", insbPrizeinfo);
	}

}