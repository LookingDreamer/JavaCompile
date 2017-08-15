package com.zzb.conf.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cninsure.core.controller.BaseController;
import com.cninsure.core.exception.ControllerException;
import com.cninsure.core.utils.BeanUtils;
import com.cninsure.core.utils.StringUtil;
import com.cninsure.system.entity.INSCUser;
import com.common.PagingParams;
import com.zzb.conf.service.INSBChannelagreementService;

/**
 * 
 * 协议管理
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/channelagreement/*") 
public class INSBChannelAgreementController extends BaseController {
	@Resource
	private INSBChannelagreementService insbChannelagreementService;
	
	/**
	 * 页面加载(liuchao)
	 */
	@RequestMapping(value = "showchannelagreement", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result = new ModelAndView("zzbconf/newchannelagreement");
		return result;
	}
	
	@RequestMapping(value = "agreementChn", method = RequestMethod.GET)
	public ModelAndView agreementChn() {
		ModelAndView result = new ModelAndView("zzbconf/agreementchn");
		return result;
	}
	
	@RequestMapping(value="/aglist", method=RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> aglist(@ModelAttribute PagingParams para, String agname, String agcode, String deptid, String providerid) throws ControllerException{
		Map<String, Object> map = BeanUtils.toMap(para);
		if (StringUtil.isNotEmpty(agname)) map.put("agreementname", agname);
		if (StringUtil.isNotEmpty(agcode)) map.put("agreementcode", agcode);
		if (StringUtil.isNotEmpty(deptid)) map.put("deptid", deptid);
		if (StringUtil.isNotEmpty(providerid)) map.put("providerid", providerid);
		
		return insbChannelagreementService.aglist(map);
	}
	
	@RequestMapping(value="/agchnlist", method=RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> agchnlist(String agreeid) throws ControllerException{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("agreeid", agreeid);
		return insbChannelagreementService.agchnlist(map);
	}
	
	@RequestMapping(value="/delchn", method=RequestMethod.POST)
	@ResponseBody
	public String delchn(String ids) throws ControllerException{
		return insbChannelagreementService.delchn(ids);
	}
	
	@RequestMapping(value="/addchn", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	public String addchn(HttpSession session, String channelIds, String agreeId, String deptId, String payIds) throws ControllerException{
		if ( StringUtil.isEmpty(channelIds) || StringUtil.isEmpty(agreeId) || 
			 StringUtil.isEmpty(deptId) || StringUtil.isEmpty(payIds) ) {
			return "请检查入参";
		}
		INSCUser operator = (INSCUser)session.getAttribute("insc_user");
		return insbChannelagreementService.addchn(operator.getName(), channelIds, agreeId, deptId, payIds);
	}
	
	@RequestMapping(value="/swagchn", method=RequestMethod.POST, produces="application/json;charset=UTF-8")
	@ResponseBody
	public String swagchn(HttpSession session, String channelIds, String agreeId, 
			String toAgreeId, String payIds, String deptId) throws ControllerException{
		if (StringUtil.isEmpty(channelIds) || 
			StringUtil.isEmpty(agreeId) || 
			StringUtil.isEmpty(toAgreeId) ) {
			return "请检查入参";
		}
		
		INSCUser operator = (INSCUser)session.getAttribute("insc_user");
		return insbChannelagreementService.swagchn(operator.getName(), channelIds, agreeId, toAgreeId, payIds, deptId);
	}
	
	@RequestMapping(value="/agdeptlist", method=RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> agdeptlist(String agreeid) throws ControllerException{
		if (StringUtil.isEmpty(agreeid)) {
			throw new ControllerException("请检查入参");
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("agreeid", agreeid);
		return insbChannelagreementService.agdeptlist(map);
	}
	
}
