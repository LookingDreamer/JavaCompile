package com.zzb.mobile.controller;

import javax.annotation.Resource;
import javax.validation.Valid;

import com.cninsure.core.utils.LogUtil;
import com.common.JsonUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.cninsure.core.controller.BaseController;
import com.zzb.mobile.model.PayChannelParamVo;
import com.zzb.mobile.service.INSBPayChannelService;

@Controller
@RequestMapping("/mobile/*")
public class INSBPayChannelController extends BaseController{
	@Resource
	private INSBPayChannelService insbPayChannelService;
	/*
	 * showPayChannel，显示支付渠道
	 */
	@RequestMapping(value = "basedata/myTask/showPayChannel",produces = "application/json;charset=utf-8", method = RequestMethod.POST)
	@ResponseBody
	public String showPayChannel(@RequestBody @Valid PayChannelParamVo payChannelParamVo){
		LogUtil.info("获取支付方式：" + JsonUtil.serialize(payChannelParamVo));
		//入参 Deptcode , prvid;
		return insbPayChannelService.showPayChannel(
				payChannelParamVo.getDeptcode(),
				payChannelParamVo.getPrvid(),
				payChannelParamVo.getTaskid(),
				payChannelParamVo.getSubInstanceId(),
				payChannelParamVo.getClienttype());
	}

	/*
	 * showPayChannelForChn，显示渠道配置的支付渠道
	 */
	@RequestMapping(value = "basedata/myTask/showPayChannelForChn",produces = "application/json;charset=utf-8", method = RequestMethod.POST)
	@ResponseBody
	public String showPayChannelForChn(@RequestBody @Valid PayChannelParamVo payChannelParamVo){
		LogUtil.info("获取支付方式(渠道)：" + JsonUtil.serialize(payChannelParamVo));
		//入参 channelId , prvid;
		return insbPayChannelService.showPayChannelForChn(
				payChannelParamVo.getChannelId(),
				payChannelParamVo.getPrvid(),
				payChannelParamVo.getTaskid(),
				payChannelParamVo.getSubInstanceId(),
				payChannelParamVo.getClienttype());
	}
	
	/*
	 * choosePayChannel，选择保存支付方式
	 */
	@RequestMapping(value = "basedata/myTask/choosePayChannel", method = RequestMethod.POST)
	@ResponseBody
	public String choosePayChannel(@RequestBody PayChannelParamVo payChannelParamVo){
        LogUtil.info("保存支付方式：" + JsonUtil.serialize(payChannelParamVo));
		return insbPayChannelService.choosePayChannel(
				payChannelParamVo.getPaychannelid(),
				payChannelParamVo.getOperator(),
				payChannelParamVo.getTaskid(),
				payChannelParamVo.getInscomcode());
	}
	
	/*
	 * showPayChannelDetail，选中的某种支付渠道的详细
	 */
	@RequestMapping(value = "basedata/myTask/showPayChannelDetail", method = RequestMethod.POST)
	@ResponseBody
	public String showPayChannelDetail(@RequestBody String id){
		return insbPayChannelService.showPayChannelDetail(id);
	}
	
	/**
	 * 根据机构id和供应商id查看  移动快刷      快钱的结算账户号和  快钱平台账户号
	 * @param payChannelParamVo
	 * @return
	 */
	@RequestMapping(value = "basedata/myTask/queryPayChannelAccount", method = RequestMethod.POST)
	@ResponseBody
	public String queryPayChannelAccount(@RequestBody PayChannelParamVo payChannelParamVo){
		return insbPayChannelService.queryPayChannelAccount(payChannelParamVo.getDeptcode(),payChannelParamVo.getPrvid());
	}
	/**
	 * 查询code表信息并返回当前最新版本号接口
	 * @return
	 */
	@RequestMapping(value = "basedata/myTask/getNewVersion", method = RequestMethod.POST)
	@ResponseBody
	public String getNewVersion(){
		return insbPayChannelService.getNewVersion();
	}
}
