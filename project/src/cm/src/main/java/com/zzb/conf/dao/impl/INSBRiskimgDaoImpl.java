package com.zzb.conf.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.cninsure.core.utils.BeanUtils;
import com.cninsure.system.entity.INSCCode;
import com.zzb.conf.dao.INSBRiskimgDao;
import com.zzb.conf.entity.INSBRiskimg;

@Repository
public class INSBRiskimgDaoImpl extends BaseDaoImpl<INSBRiskimg> implements
		INSBRiskimgDao {

	@Override
	public Long queryCountVo(INSBRiskimg riskimg) {
		Map<String, Object> params = BeanUtils.toMap(riskimg);
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectCountVo"),params);
	}

	@Override
	public List<INSBRiskimg> queryListVo(INSBRiskimg riskimg) {
		Map<String, Object> params = BeanUtils.toMap(riskimg);
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectVo"),params);
	}

	@Override
	public void deleteByRiskId(String riskid) {
		this.sqlSessionTemplate.delete(this.getSqlName("deleteByRiskId"), riskid);
	}

	@Override
	public List<INSCCode> getDefaultRiskImg() {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectDefaultRiskImg"));
	}

	@Override
	public List<INSBRiskimg> queryListByVopage(Map<String, Object> initMap) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectVoBypage"),initMap);
	}

	@Override
	public List<INSBRiskimg> selectRiskimgByRiskid(String riskid) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectRiskimgByRiskid"), riskid);
	}

	@Override
	public List<String> selectRiskimgtypeByRiskid(String riskid) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectRiskimgtypeByRiskid"), riskid);
	}

	@Override
	public int deleteByRiskimgtypeRiskid(Map<String, String> urId) {
		return this.sqlSessionTemplate.delete(this.getSqlName("deleteByRiskimgtypeRiskid"), urId);	
	}

	@Override
	public List<INSCCode> selectNotSelectedRiskimgtypeByRiskid(String riskid) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectNotSelectedRiskimgtypeByRiskid"), riskid);
	}

}