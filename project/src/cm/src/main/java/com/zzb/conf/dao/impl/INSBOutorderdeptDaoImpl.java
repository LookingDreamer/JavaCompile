package com.zzb.conf.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.cninsure.core.utils.BeanUtils;
import com.zzb.conf.dao.INSBOutorderdeptDao;
import com.zzb.conf.entity.INSBOutorderdept;

@Repository
public class INSBOutorderdeptDaoImpl extends BaseDaoImpl<INSBOutorderdept> implements
		INSBOutorderdeptDao {

	@Override
	public List<INSBOutorderdept> queryListVo(INSBOutorderdept query) {
		Map<String, Object> params = BeanUtils.toMap(query);
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectVo"),params);
	}

	@Override
	public List<String> selectDeptIdsByAgreementId(String agreementId) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectDeptIdsByAgreementId"), agreementId);
	}

}