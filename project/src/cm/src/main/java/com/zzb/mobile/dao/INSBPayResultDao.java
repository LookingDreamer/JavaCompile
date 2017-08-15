package com.zzb.mobile.dao;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseDao;
import com.zzb.mobile.entity.INSBPayResult;


public interface INSBPayResultDao extends BaseDao<INSBPayResult> {

	public void insert(INSBPayResult pay);
	
	public INSBPayResult selectPayByBizId(String bizId);
	
	
	public void updatePayByBizId(INSBPayResult pay);	
	
}
