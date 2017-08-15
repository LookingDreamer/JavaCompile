package com.zzb.conf.service;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseService;
import com.zzb.conf.entity.INSBOrderlistenpush;

public interface INSBOrderlistenpushService extends BaseService<INSBOrderlistenpush> {
	/**
	 * 定时轮询 更新添加订单失败状态的订单
	 */
	public void updateAddFail(INSBOrderlistenpush orderfail);
	
	/**
	 * 查询订单监听同送信息
	 * @param param
	 * @return
	 */
	public INSBOrderlistenpush queryOrderListen(Map<String, String> param);
	
	public List<INSBOrderlistenpush> queryListBytype(String type);
	
	
	/**
	 * cm更新状态失败 定时重新更新
	 * 
	 * @param mainTaskId
	 * @param subTaskId
	 * @param taskName
	 * @param status
	 */
	public void save4Again(String mainTaskId,String subTaskId,String taskName,String status);
}