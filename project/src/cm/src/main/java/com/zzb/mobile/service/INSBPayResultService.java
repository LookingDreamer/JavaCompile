package com.zzb.mobile.service;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseService;
import com.zzb.mobile.entity.INSBPayResult;


public interface INSBPayResultService extends BaseService<INSBPayResult> {
	/**
	 * 增加
	 */
	public void insert(INSBPayResult pay);
	/**
	 * 通过支付号查找
	 */
	public INSBPayResult selectPayBybizId(String bizId);

	/**
	 * 更新
	 */
	public void updatePayByBizId(INSBPayResult pay);

	
	
	
}
