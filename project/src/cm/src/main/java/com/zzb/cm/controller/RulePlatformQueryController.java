package com.zzb.cm.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cninsure.core.controller.BaseController;
import com.cninsure.core.exception.ControllerException;
import com.cninsure.core.utils.LogUtil;
import com.zzb.cm.service.RulePlatformQueryService;
import com.zzb.mobile.model.CommonModel;

import net.sf.json.JSONObject;
/**
 * 规则平台查询调用 20160820版本 
 * @author hejie
 *
 */
@Controller
@RequestMapping("/workflow/*")
public class RulePlatformQueryController extends BaseController {
	
	@Resource
	private RulePlatformQueryService rulePlatformQueryService;
	/**
	 * 启动规则平台查询
	 * @param json {"taskid":"12121","inscomcode":"202744"}
	 * @return
	 */
	@RequestMapping(value = "startrulequery", method = RequestMethod.GET)
	@ResponseBody
	public CommonModel startRuleQuery(@RequestBody String json) {
		LogUtil.info("startrulequery" + json);
		return rulePlatformQueryService.startRuleQuery(json);
	}
	/**
	 * 保存规则平台查询返回数据
	 * @param json
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "saverulequeryinfo",produces="application/json;charset=UTF-8",method = RequestMethod.POST)
	@ResponseBody
	public String saveRuleQueryInfo(@RequestBody String json) throws ControllerException{
		//LogUtil.info("saveRuleQueryInfo" + json);
		
		JSONObject jsonObject = JSONObject.fromObject(json);
		if(jsonObject !=null && jsonObject.containsKey("callbacktype")){
			//1是江苏流程，0是默认之前流程
			String callbacktype = jsonObject.getString("callbacktype");
			if(!callbacktype.equals("") && callbacktype.equals("1")){
				return rulePlatformQueryService.saveRuleQueryInfoJiangSu(json);
			} 
		}
		
		return rulePlatformQueryService.saveRuleQueryInfo(json);
	}
}
