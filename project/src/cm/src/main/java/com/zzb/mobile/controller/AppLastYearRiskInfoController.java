package com.zzb.mobile.controller;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cninsure.core.exception.ControllerException;
import com.zzb.cm.service.INSBCarinfoService;
import com.zzb.mobile.entity.LastYearRiskInfo;
import com.zzb.mobile.model.CommonModel;
import com.zzb.mobile.service.AppInsuredQuoteService;
import com.zzb.mobile.service.AppLastYearRiskInfoService;

@Controller
@RequestMapping("/mobile/basedata/myTask/LastYearRiskInfo/*")
public class AppLastYearRiskInfoController {

	@Resource
	private AppLastYearRiskInfoService appLastYearRiskInfoService;
	@Resource
	private AppInsuredQuoteService appInsuredQuoteService;
	@Resource
	private INSBCarinfoService carinfoService;


	/**
	 * 多方报价 查看上年险种
	 * 接口描述：通过任务id去查询此车的上年投保的保险配置和险种保费信息
	 * 请求方式 get
	 * 请求地址 /mobile/basedata/myTask/LastYearRiskInfo/getLastYearRiskInfo
	 * @param processInstanceId 流程实例id
	 */
	@RequestMapping(value = "/getLastYearRiskInfo", method = RequestMethod.POST)
	@ResponseBody
	public CommonModel getLastYearRiskInfo(@RequestBody LastYearRiskInfo lastYearRiskInfo)
			throws ControllerException {
//		JSONObject jsonObject = JSONObject.fromObject(params);
//		Map<String, Object> map = carinfoService.getCarInfoByTaskId(lastYearRiskInfo.getProcessinstanceid());
//		jsonObject.
//		appInsuredQuoteService.queryLastInsuredByNumOrCar(model)
		return appLastYearRiskInfoService.getLastYearRiskInfo(lastYearRiskInfo);
	}
	
	
	
}
