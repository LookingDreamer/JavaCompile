package com.zzb.cm.dao;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseDao;
import com.zzb.cm.entity.INSBApplicant;
import com.zzb.cm.entity.INSBCarinfo;

public interface INSBApplicantDao extends BaseDao<INSBApplicant> {
	/**
	 * 依据taskID改变投保人personID
	 */
	public void updateByTaskId(INSBApplicant iNSBApplicant);

	/**
	 * 依据taskID查询投保人的信息
	 * 
	 * @param taskid
	 * @return
	 */
	public INSBApplicant selectByTaskID(String taskid);
	/**
	 * 获取数据记录数，用于前端获取供应商报价前，检查数据是否完整
	 * @param taskID 任务号
	 * @return
	 */
	public List<Map<Object, Object>> getCheckData(String taskID);

}