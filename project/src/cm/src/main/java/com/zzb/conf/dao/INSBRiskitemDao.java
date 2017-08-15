package com.zzb.conf.dao;

import java.util.List;

import com.cninsure.core.dao.BaseDao;
import com.zzb.conf.entity.INSBRiskitem;

public interface INSBRiskitemDao extends BaseDao<INSBRiskitem> {

	public Long queryCountVo(INSBRiskitem riskitem);

	public List<INSBRiskitem> queryListVo(INSBRiskitem riskitem);
	
	public List<INSBRiskitem> selectByModifyDate(String modifydate);
	@Deprecated
	public List<INSBRiskitem> selectAll();

}