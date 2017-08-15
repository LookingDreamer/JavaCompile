package com.zzb.conf.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.cninsure.core.utils.BeanUtils;
import com.zzb.conf.dao.INSBRiskitemDao;
import com.zzb.conf.entity.INSBRiskitem;

@Repository
public class INSBRiskitemDaoImpl extends BaseDaoImpl<INSBRiskitem> implements
		INSBRiskitemDao {
	
	@Override
	public Long queryCountVo(INSBRiskitem riskitem) {
		Map<String, Object> params = BeanUtils.toMap(riskitem);
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectVoCount"),params);

	}

	@Override
	public List<INSBRiskitem> queryListVo(INSBRiskitem riskitem) {
		Map<String, Object> params = BeanUtils.toMap(riskitem);
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectVo"),params);

	}
	
	@Override
	public List<INSBRiskitem> selectByModifyDate(String modifydate) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectByModifytime"), modifydate);
	}

	@Override
	public List<INSBRiskitem> selectAll() {
		return this.sqlSessionTemplate.selectList(this.getSqlName("select"));
	}

}