package com.zzb.mobile.dao.impl;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.mobile.dao.INSBPayResultDao;
import com.zzb.mobile.entity.INSBPayResult;

@Repository
public class INSBPayResultDaoImpl extends BaseDaoImpl<INSBPayResult> implements INSBPayResultDao {

	/**
	 * 更新
	 */
	@Override
	public void updatePayByBizId(INSBPayResult pay) {
		this.sqlSessionTemplate.update(this.getSqlName("updateByBizId"),pay);

	}

	@Override
	public INSBPayResult selectPayByBizId(String bizId) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectByBidId"),bizId);
	}


}
