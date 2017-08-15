package com.lzgapi.order.service;


import java.util.Map;

import com.zzb.conf.entity.INSBOrderlistenpush;
import com.zzb.mobile.model.CommonModel;

/**
 * 
 * 向蓝掌柜 同步订单信息
 * @author Administrator
 *
 */
public interface LzgOrderService {
	
	/**
	 * 订单正常状态更新   
	 * 
	 * 普通更新/完成
	 * 
	 * @param mainTaskId 主实例id
	 * @param subTaskId 子实例id
	 * @param taskName 当前结点名称
	 * @param status 当前结点状态
	 */
	public void updateOrderNormalData(String mainTaskId, String subTaskId,String taskName);
	
	
	/**
	 * 掌中宝取消订单
	 * 
	 * 
	 * @param mainTaskId
	 * @param subTaskId
	 * @param taskName
	 */
	public void cancleOrderFromCM(String mainTaskId,String subTaskId);
	

	/**
	 * 蓝掌柜主动取消订单
	 * @param data
	 * @return
	 */
	public Map<String,String> cancleOrderFromLZG(String data);
	
	
	public void save4Again(String mainTaskId,String subTaskId,String taskName,String status);
	/**
	 * 懒掌柜   订单保存接口
	 * @param order
	 * @param productcode
	 * @param agentnum 代理人工号
	 */
	public void addOrderData(INSBOrderlistenpush order,String productcode,String agentnum);
	
	/**
	 * 产品进入接口
	 * @param token
	 * @param lzgOtherUserId
	 * @param lzgManagerId
	 * @param lzgRequirementId
	 * @param lzgProductCode
	 * @return
	 */
	public CommonModel getProduct(String token,String lzgOtherUserId,String lzgManagerId,
			String lzgRequirementId,String lzgProductCode);
	
	/**
	 * 订单继续处理接口
	 * @param token
	 * @param lzgOtherUserId
	 * @param lzgRequirementId
	 * @param lzgOtherOrderNo
	 * @return
	 */
	public CommonModel orderContinue(String token,String lzgOtherUserId,String lzgRequirementId,
			String lzgOtherOrderNo);
	
	/**
	 * 保存订单推送表信息
	 * @param taskid
	 * @param lzgid
	 */
	public  void addorderListerpust(String taskid,String lzgid);
}
