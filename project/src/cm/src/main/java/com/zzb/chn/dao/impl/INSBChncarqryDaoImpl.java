package com.zzb.chn.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.chn.dao.INSBChncarqryDao;
import com.zzb.chn.entity.INSBChncarqry;

@Repository
public class INSBChncarqryDaoImpl extends BaseDaoImpl<INSBChncarqry> implements INSBChncarqryDao {
	
	@Override
	public int addChncarqryDatas(INSBChncarqry chncarqry) {
		return this.sqlSessionTemplate.insert(this.getSqlName("insert"), chncarqry);
	}

	@Override
	public int createSubTable(INSBChncarqry chncarqry) {
		return this.sqlSessionTemplate.update(this.getSqlName("createSubTable"), chncarqry);
	}

	@Override
	public List<Map<String, Object>> queryDetail(Map<String, Object> para) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("queryDetail"), para);
	}
	@Override
	public long queryDetailSize(Map<String, Object> para) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("queryDetailSize"), para);
	}

	@Override
	public long queryClnGroupCount(Map<String, Object> para) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("queryClnGroupCount"), para);
	}

	@Override
	public List<Map<String, Object>> queryGroupRecCount(Map<String, Object> para) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("queryGroupRecCount"), para);
	}
	@Override
	public long queryGroupRecCountSize(Map<String, Object> para) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("queryGroupRecCountSize"), para);
	}

	@Override
	public List<Map<String, Object>> querySummaryCount(Map<String, Object> para) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("querySummaryCount"), para);
	}
	@Override
	public long queryCount(Map<String, Object> para) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("queryCount"), para);
	}
	@Override
	public Map<String, Object> queryMaxTimeRec(Map<String, Object> para) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("queryMaxTimeRec"), para);
	} 

}