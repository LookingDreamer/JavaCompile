package com.zzb.conf.dao;

import java.util.List;

import com.cninsure.core.dao.BaseDao;
import com.zzb.conf.entity.INSBOutorderdept;

public interface INSBOutorderdeptDao extends BaseDao<INSBOutorderdept> {

	public List<INSBOutorderdept> queryListVo(INSBOutorderdept query);
	
	public List<String> selectDeptIdsByAgreementId(String agreementId);

}