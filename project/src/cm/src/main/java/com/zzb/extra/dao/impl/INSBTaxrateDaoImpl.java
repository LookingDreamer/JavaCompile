package com.zzb.extra.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.extra.dao.INSBTaxrateDao;
import com.zzb.extra.entity.INSBChnTaxrate;

@Repository
public class INSBTaxrateDaoImpl extends BaseDaoImpl<INSBChnTaxrate> implements
		INSBTaxrateDao {

	@Override
	public List<INSBChnTaxrate> queryTaxrateList(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.sqlSessionTemplate.selectList(this.getSqlName("select"), map);
	}

	@Override
	public Long queryTaxrate(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectCount"), map);
	}

	@Override
	public void updateStatusById(INSBChnTaxrate tax) {
		// TODO Auto-generated method stub
		this.sqlSessionTemplate.update(this.getSqlName("updateStatusById"), tax);
	}

	@Override
	public List<Map<Object, Object>> queryRateLsitVo(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.sqlSessionTemplate.selectList(this.getSqlName("queryRateLsitVo"), map);
	}

	@Override
	public Long queryRateLsitVoCount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.sqlSessionTemplate.selectOne(this.getSqlName("queryRateLsitVoCount"), map);
	}

	@Override
	public Integer updateStatusById(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.sqlSessionTemplate.update(this.getSqlName("updateStatusById"),map);
	}

	@Override
	public INSBChnTaxrate queryTaxrateByChannelid(String channelid) {
		// TODO Auto-generated method stub
		return this.sqlSessionTemplate.selectOne(this.getSqlName("queryTaxrateByChannelid"), channelid);
	}

	@Override
	public void cleanTaxrateStatus(String channelid) {
		// TODO Auto-generated method stub
		this.sqlSessionTemplate.update(this.getSqlName("cleanTaxrateStatus"),channelid);
	}

}