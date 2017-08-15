package com.zzb.cm.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cninsure.core.exception.ControllerException;
import com.cninsure.core.utils.BeanUtils;
import com.cninsure.core.utils.JsonUtil;
import com.zzb.conf.entity.INSBAgent;
import com.zzb.conf.service.INSBAgentService;
import com.zzb.mobile.model.CommonModel;
import com.zzb.mobile.model.FileUploadBase64Param;
import com.zzb.mobile.model.SearchProviderModel;
import com.zzb.mobile.service.AppInsuredQuoteService;
import com.zzb.mobile.service.AppRegisteredService;

@Controller
@RequestMapping("/valetcatalogue/insured/quote/*")
public class INSBInsuredQuoteController {
	
	@Resource
	private AppRegisteredService registeredService;
	
	@Resource
	private AppInsuredQuoteService insuredQuoteService;
	
	@Resource
    private HttpServletRequest request;
	
	@Resource
	private INSBAgentService agentService;
	
	/**
	 * 获取代理人信息
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "/getAgentInfo", method = RequestMethod.GET)
	@ResponseBody
	public String getAgentInfo(@RequestParam(value="id") String id)throws ControllerException{
		Map<String, Object> map = new HashMap<String, Object>();
		INSBAgent temp = new INSBAgent();
		temp.setId(id);
		map.put("body",BeanUtils.toMap(agentService.getAgentInfo(temp)));
		map.put("status", "success");
		map.put("message", "操作成功");
		return 	JsonUtil.getJsonString(map);
	}
	
	/**
	 * 获取保险配置方案列表
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "/schemelist", method = RequestMethod.GET)
	@ResponseBody
	public CommonModel schemeList(@RequestParam(value="agentnotitype", required=false) String agentnotitype)throws ControllerException{
		return insuredQuoteService.schemeList(null, agentnotitype);
	}
	
	/**
	 * 查询保险配置信息
	 * @param plankey
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "/insurancescheme", method = RequestMethod.GET)
	@ResponseBody
	public CommonModel insuranceScheme(@RequestParam(value="plankey") String plankey)throws ControllerException{
		String processinstanceid = null;
		return insuredQuoteService.insuranceScheme(plankey,processinstanceid);
	}
	
	/**
	 * 
	 * 接口描述：文件上传
	 * 请求地址	
	 * @param 
	 */
	@RequestMapping(value = "/fileUpLoadBase64", method = RequestMethod.POST)
	@ResponseBody
	public CommonModel fileUpLoadBase64(@RequestBody FileUploadBase64Param param)
			throws ControllerException {
		return registeredService.fileUpLoadBase64(request,param.getFile(),param.getFileName(),param.getFileType(),param.getFileDescribes(),param.getJobNum(),param.getTaskId());
	}
	
	/**
	 * 
	 * 上年投保信息
	 * @param 
	 */
	@RequestMapping(value = "/getLastYearPolicyInfo", method = RequestMethod.GET)
	@ResponseBody
	public CommonModel getLastYearPolicyInfo()
			throws ControllerException {
		return null;
	}
	
	/**
	 * 
	 * 获取车型信息
	 * @param 
	 */
	@RequestMapping(value = "/getCarInfo", method = RequestMethod.GET)
	@ResponseBody
	public CommonModel getCarInfo(@RequestParam(value="modelname") String modelName,@RequestParam(value="pagesize") String pageSize,@RequestParam(value="currentpage") String currentPage)
			throws ControllerException {
		return insuredQuoteService.searchCarModel(modelName,pageSize,currentPage);
	}
	/**
	 * 
	 * 获取保险公司信息
	 * @param 
	 */
	@RequestMapping(value = "/getInsurance", method = RequestMethod.GET)
	@ResponseBody
	public String getInsurance()
			throws ControllerException {
		SearchProviderModel model = new SearchProviderModel();
		model.setProcessinstanceid("*");
		model.setChannel("");//渠道代码
		model.setAgentid("");//代理人ID
		model.setCity("");//市代码
		model.setProvince("");//省代码
		return insuredQuoteService.searchProvider(model).toString();
	}
}
