package com.zzb.conf.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.cninsure.core.utils.BeanUtils;
import com.zzb.conf.dao.INSBRiskrenewalitemDao;
import com.zzb.conf.entity.INSBRiskrenewalitem;

@Repository
public class INSBRiskrenewalitemDaoImpl extends BaseDaoImpl<INSBRiskrenewalitem> implements
		INSBRiskrenewalitemDao {

	@Override
	public Long queryCountVo(INSBRiskrenewalitem riskrenewalitem) {
		Map<String, Object> params = BeanUtils.toMap(riskrenewalitem);
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectCountVo"),params);
	}

	@Override
	public List<INSBRiskrenewalitem> queryListVo(INSBRiskrenewalitem riskrenewalitem) {
		Map<String, Object> params = BeanUtils.toMap(riskrenewalitem);
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectVo"),params);
	}

}