package com.zzb.cm.dao;

import com.cninsure.core.dao.BaseDao;
import com.zzb.cm.entity.INSBApplicant;
import com.zzb.cm.entity.INSBLegalrightclaim;

public interface INSBLegalrightclaimDao extends BaseDao<INSBLegalrightclaim> {

	/**
	 * 依据taskID查询权益索赔人的信息
	 * @param taskid
	 * @return
	 */
	public INSBLegalrightclaim selectByTaskID(String taskid);
	/**
	 * 依据taskID改变权益索赔人personID
	 */
	public void updateByTaskId(INSBLegalrightclaim  iNSBLegalrightclaim);
	
}