package com.zzb.conf.dao;

import java.util.List;

import com.cninsure.core.dao.BaseDao;
import com.zzb.conf.entity.INSBRiskrenewalitem;

public interface INSBRiskrenewalitemDao extends BaseDao<INSBRiskrenewalitem> {

	public Long queryCountVo(INSBRiskrenewalitem riskrenewalitem);

	public List<INSBRiskrenewalitem> queryListVo(INSBRiskrenewalitem riskrenewalitem);

}