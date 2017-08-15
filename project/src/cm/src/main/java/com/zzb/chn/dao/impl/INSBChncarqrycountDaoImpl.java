package com.zzb.chn.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.chn.dao.INSBChncarqrycountDao;
import com.zzb.chn.entity.INSBChncarqrycount;

@Repository
public class INSBChncarqrycountDaoImpl extends BaseDaoImpl<INSBChncarqrycount> implements INSBChncarqrycountDao {

	@Override
	public int addChncarqrycountDatas(INSBChncarqrycount chncarqrycount) {
		return this.sqlSessionTemplate.insert(this.getSqlName("insert"), chncarqrycount);
	}

	@Override
	public List<INSBChncarqrycount> selectCountByCidAndDay(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectCount"), map);
	}

	@Override
	public int addCount(INSBChncarqrycount chncarqrycount) {
		return this.sqlSessionTemplate.update(this.getSqlName("addCount"), chncarqrycount);
	}
	
	@Override
	public long countData(Map<String, Object> para) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("countData"), para);
	}
}