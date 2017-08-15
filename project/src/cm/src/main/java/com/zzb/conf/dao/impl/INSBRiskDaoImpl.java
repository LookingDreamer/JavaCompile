package com.zzb.conf.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.cninsure.core.utils.BeanUtils;
import com.zzb.conf.dao.INSBRiskDao;
import com.zzb.conf.entity.INSBRisk;

@Repository
public class INSBRiskDaoImpl extends BaseDaoImpl<INSBRisk> implements INSBRiskDao {
	@Override
	public List<INSBRisk> queryByRiskVo(INSBRisk insbRisk) {
		Map<String, Object> params = BeanUtils.toMap(insbRisk);
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectVo"),params);
	}
	@Override
	public List<INSBRisk> queryByRiskVopage(Map<String, Object> map) {
		//Map<String, Object> params = BeanUtils.toMap(insbRisk);
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectVorownum"),map);
	}
	@Override
//	public Long queryByRiskVoCount(INSBRisk insbRisk) {
//		Map<String, Object> params = BeanUtils.toMap(insbRisk);
//		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectVoCount"),params);
//	}
	public Long queryByRiskVoCount(Map<String, Object> map) {
		//Map<String, Object> params = BeanUtils.toMap(insbRisk);
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectVoCount"),map);
	}
	@Override
	public List<INSBRisk> selectByModifyDate(String c){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("params", params);
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectByModifyDate"),c);
	}
	@Override
	public List<Map<String,Object>> selectRiskList(String inscomcode) {
		//Map<String, Object> params = new HashMap<String, Object>();
		//params.put("params", params);
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectRiskListByMap"),inscomcode);
	}
	@Override
	public List<INSBRisk> selectAll() {
		return this.sqlSessionTemplate.selectList(this.getSqlName("select"));
	}
}