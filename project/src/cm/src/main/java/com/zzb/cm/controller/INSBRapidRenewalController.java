package com.zzb.cm.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cninsure.core.controller.BaseController;
import com.cninsure.core.exception.ControllerException;
import com.cninsure.system.entity.INSCUser;
import com.zzb.conf.entity.INSBAgent;
import com.zzb.conf.service.INSBAgentService;
import com.zzb.mobile.model.CallPlatformQueryModel;
import com.zzb.mobile.model.CommonModel;
import com.zzb.mobile.model.InsuredConfig;
import com.zzb.mobile.model.InsuredQuoteCreateModel;
import com.zzb.mobile.model.SearchProviderModel;
import com.zzb.mobile.service.AppInsuredQuoteService;
/**
 * CM系统 快速续保
 */ 
@Controller
@RequestMapping("/rapidrenewal/*")
public class INSBRapidRenewalController extends BaseController {
	@Resource
	private INSBAgentService insbAgentService;
	@Resource
	private AppInsuredQuoteService insuredQuoteService;

	/**
	 * 页面跳转  
	 * @param 
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "init", method = RequestMethod.GET)
	public ModelAndView modelinsurance(HttpSession session,@ModelAttribute InsuredConfig model,String id) throws ControllerException {
		ModelAndView mav = new ModelAndView("cm/modelinsurance/rapidrenewal");
		//获取代理人信息
		INSBAgent temp = new INSBAgent();
		temp.setId(id);
//		temp.setId("f4514660537e11e5d12d53043cee09e7");
		INSBAgent agent= insbAgentService.getAgentInfo(temp);
		Map<String, String>  quotearea =insuredQuoteService.getQuoteAreaByAgentid(id);
		
		//获取投保信息（证件/行驶区域）
//			Map<String, Object> insMap=insbAgentService.getInsuranceInfo();
		mav.addObject("INSBAgent", agent); 
		//所属区域
		mav.addObject("quotearea", quotearea);
//			mav.addObject("insuranceinfo", insMap);
		SearchProviderModel searchmodel = new SearchProviderModel();
		searchmodel.setAgentid(temp.getId());
		searchmodel.setChannel(temp.getChannelid());
		searchmodel.setCity(quotearea.get("cityCode"));
		searchmodel.setProvince(quotearea.get("provinceCode"));
		//获取保险公司列表
		mav.addObject("providers", insuredQuoteService.fastRenewProviders(searchmodel).getBody());
		return mav;
	}
	
	/**
	 * 1.调用工作流接口，获取实例id
	 * 2.根据代理人id获取代理人信息
	 * 3，保存车主信息，向车主信息表，人员表插入数据
	 * 4.向车辆信息表插入数据
	 * 5.报价信息总表插入投保地区
	 * 6.保存保单信息，插入一条数据默认为商业险
	 * @param createModel
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value="/create",method=RequestMethod.POST)
	@ResponseBody
	public CommonModel createInsuredInit(@ModelAttribute InsuredQuoteCreateModel createModel,HttpSession session) throws ControllerException{
		INSCUser inscUser = (INSCUser) session.getAttribute("insc_user");
		createModel.setUserid(inscUser.getId());
		return insuredQuoteService.createInsuredInit(createModel);
	}
	
	/**
	 * 快速续保，平台查询，获取上年保单信息，并入库
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/fastrenewcallplatformquery", method = RequestMethod.POST)
	@ResponseBody
	public CommonModel fastRenewCallPlatformQuery(@ModelAttribute CallPlatformQueryModel model){
		return insuredQuoteService.fastRenewCallPlatformQuery(model);
	}
}

