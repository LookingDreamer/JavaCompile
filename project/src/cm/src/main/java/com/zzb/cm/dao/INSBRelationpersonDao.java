package com.zzb.cm.dao;

import com.cninsure.core.dao.BaseDao;
import com.zzb.cm.entity.INSBApplicant;
import com.zzb.cm.entity.INSBRelationperson;

public interface INSBRelationpersonDao extends BaseDao<INSBRelationperson> {
/**
 * 依据联系人的taskID查询联系人的信息	
 * @param taskid
 * @return
 */
	public INSBRelationperson selectByTaskID(String taskid);
/**
 * 依据taskID改变投保人personID
 */
	public void updateByTaskId(INSBRelationperson  iNSBRelationperson);
}