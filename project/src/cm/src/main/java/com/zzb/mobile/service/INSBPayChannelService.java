package com.zzb.mobile.service;

import com.cninsure.core.dao.BaseService;
import com.zzb.conf.entity.INSBPaychannel;
/*
 * 支付通道的接口
 * 显示支付通道列表
 * 进行选择支付通道
 */
public interface INSBPayChannelService extends BaseService<INSBPaychannel>{
	public String showPayChannel(String Deptcode,String prvid,String taskId, String subInstanceId, String clientType);

	public String showPayChannelForChn(String channelId,String prvid,String taskId, String subInstanceId, String clientType);
	/*
	 * 显示支付通道列表
	 */
	
	public String showPayChannelDetail(String id);
	/*
	 * 支付渠道详细信息
	 */
	
	public String choosePayChannel(String id,String agentnum,String processInstanceId,String inscomcode);
	/*
	 * 选择支付通道方式
	 */
	
	/**
	 * 根据机构id和供应商id查看  移动快刷      快钱的结算账户号和  快钱平台账户号
	 * @param deptid
	 * @param prvid
	 * @return
	 */
	public String queryPayChannelAccount(String deptid,String prvid);
	
	/**
	 * 查询Android最新版本号
	 * @return
	 */
	public String getNewVersion();

	public String choosePayChannel1(String id, String agentnum,
			String processInstanceId, String inscomcode);
}
