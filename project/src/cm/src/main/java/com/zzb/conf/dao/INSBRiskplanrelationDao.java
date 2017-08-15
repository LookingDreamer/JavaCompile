package com.zzb.conf.dao;

import java.util.List;

import com.cninsure.core.dao.BaseDao;
import com.zzb.conf.entity.INSBRiskplanrelation;

public interface INSBRiskplanrelationDao extends BaseDao<INSBRiskplanrelation> {

	List<INSBRiskplanrelation> selectAllPlankey();

}