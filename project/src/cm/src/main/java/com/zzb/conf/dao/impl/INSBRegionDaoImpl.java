package com.zzb.conf.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.conf.dao.INSBRegionDao;
import com.zzb.conf.entity.INSBRegion;

@Repository
public class INSBRegionDaoImpl extends BaseDaoImpl<INSBRegion> implements
		INSBRegionDao {

	@Override
	public List<Map<String, String>> initList(String parentcode) {
		
		return this.sqlSessionTemplate.selectList(this.getSqlName("parentcode"),parentcode);
	}
	@Override
	public List<Map<String, String>> initListfromSD(Map<String, String> para) {
		
		return this.sqlSessionTemplate.selectList(this.getSqlName("parentcodefromSD"),para);
	}
	@Override
	public List<Map<String, String>> getAllCity(String parentcode) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("getAllCity"),parentcode);
	}
	@Override
	public List<Map<String, String>> getAllCountry(String parentcode) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("getAllCountry"),parentcode);
	}
	@Override
	public List<Map<String, String>> getRegisterProvince() {
		return this.sqlSessionTemplate.selectList(this.getSqlName("getRegisterProvince"));
	}

	@Override
	public INSBRegion selectById(String id) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectById"),id);
	}

	@Override
	public List<INSBRegion> selectByIds(List<String> ids) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectByIds"), ids) ;
	}
}