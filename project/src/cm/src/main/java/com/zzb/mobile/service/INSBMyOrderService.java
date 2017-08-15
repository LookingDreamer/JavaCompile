package com.zzb.mobile.service;
import com.cninsure.core.dao.BaseService;
import com.zzb.cm.entity.INSBOrder;
import com.zzb.mobile.model.CommonModel;

public interface INSBMyOrderService extends BaseService<INSBOrder>{
	/**
	 * 获得我的订单信息的接口，继承INSBOrder这个实体类
	 * 输入参数较多，代理人编号，车牌号，被保人姓名，任务状态，每页显示数及起始位置。
	 * purchaserid 渠道用户id
	 * purchaserchannel 渠道id
	 * 完成
	 * */
	public String showMyOrder(String agentnum, String carlicenseno, String insuredname, 
			String taskStatus, int limit, long offset,String taskcreatetimeup,String taskcreatetimedown
			,String purchaserid,String purchaserchannel);
	/**
	 * 获得我的订单信息的接口，继承INSBOrder这个实体类
	 * 输入参数较多，代理人编号，车牌号，被保人姓名，任务状态，每页显示数及起始位置。
	 * 完成
	 * */
	public String showMyOrderForMinizzb(String channeluserid, String carlicenseno, String insuredname,
			String taskStatus, int limit, long offset,String taskcreatetimeup,String taskcreatetimedown);
	
	public CommonModel showMyOrderForChn(String agentnum, String carlicenseno, String insuredname, 
			String taskStatus, int limit, long offset,String taskcreatetimeup,String taskcreatetimedown, String chnId, String chnUserId, String taskId);

	public CommonModel showMyOrderForChnNew(String agentnum, String carlicenseno, String insuredname,
										 String taskStatus, int limit, long offset,String taskcreatetimeup,String taskcreatetimedown, String chnId, String chnUserId, String taskId);

	/*
	 * 出单网点显示
	 */
	public String showSingleSite(String processinstanceid,String inscomcode);

	/**
	 * 订单过期状态查询 true 过期不能支付
	 * @param taskid
	 * @param inscomcode
	 * @return
	 */
	public boolean removeOverTimeData(Object taskid, Object inscomcode);

	public boolean removeOverTimeData2(Object taskid, Object inscomcode);
}
