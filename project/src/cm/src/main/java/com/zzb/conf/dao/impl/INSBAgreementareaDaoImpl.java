package com.zzb.conf.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.conf.dao.INSBAgreementareaDao;
import com.zzb.conf.entity.INSBAgreementarea;

@Repository
public class INSBAgreementareaDaoImpl extends BaseDaoImpl<INSBAgreementarea> implements
		INSBAgreementareaDao {

	@Override
	public List<Map<String, Object>> getAgreeAreaByChninnercode(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectByChannelinnercode"), map);
	}

	public List<Map<String, Object>> getAgreeAreaAndJobNumByChninnercode(Map<String, Object> map){
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectJobNumByChannelinnercode"), map);
	}

}