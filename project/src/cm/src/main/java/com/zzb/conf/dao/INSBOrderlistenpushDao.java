package com.zzb.conf.dao;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseDao;
import com.zzb.conf.entity.INSBOrderlistenpush;

public interface INSBOrderlistenpushDao extends BaseDao<INSBOrderlistenpush> {
	
	/**
	 * 
	 * 查询当前流程是否属于蓝掌柜
	 * @param taskId
	 * @return
	 */
	public INSBOrderlistenpush selectDataByTaskId(String taskId);
	
	/**
	 * 
	 * 通过子实例查询主实例
	 * @param subTaskId
	 * @return
	 */
	public INSBOrderlistenpush selectDataBySubTaskId(String subTaskId);
	
	/**
	 * 获取订单推送表信息
	 * @param param
	 * @return
	 */
	public INSBOrderlistenpush queryOrderListen(Map<String, String> param);
	
	public List<INSBOrderlistenpush> queryListBytype(String type);

}