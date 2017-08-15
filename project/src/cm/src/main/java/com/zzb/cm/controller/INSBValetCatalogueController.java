package com.zzb.cm.controller;


import java.util.Map;

import javax.annotation.Resource;




import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cninsure.core.controller.BaseController;
import com.cninsure.core.exception.ControllerException;
import com.cninsure.core.utils.BeanUtils;
import com.cninsure.system.entity.INSCUser;
import com.common.PagingParams;
import com.zzb.conf.entity.INSBAgent;
import com.zzb.conf.service.INSBAgentService;
import com.zzb.mobile.model.CommonModel;
import com.zzb.mobile.service.AppLoginService;

/**
 * CM系统 代客录单
 */ 
@Controller
@RequestMapping("/business/valetcatalogue/*")
public class INSBValetCatalogueController extends BaseController {
	
	@Resource
	private INSBAgentService insbAgentService;
	@Resource
	private AppLoginService appLoginService;

	/**
	 * 页面跳转  
	 * @param taskid 
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "showaletcataloguelist", method = RequestMethod.GET)
	public ModelAndView list(HttpSession session,String agentid) throws ControllerException {
		ModelAndView mav = new ModelAndView("cm/valetcatalogue/valetCatalogue");
		INSCUser loginUser = (INSCUser) session.getAttribute("insc_user");
		mav.addObject("loginUser",loginUser);
		return mav;
	}
	/**
	 * 查询代理人信息
	 * @param para 
	 * @param agent 
	 * @return
	 * @throws ControllerException
	 */
	
	@RequestMapping(value = "showagentmessage", method = RequestMethod.GET)
	@ResponseBody
	public String getAgentMessage(@ModelAttribute PagingParams para, INSBAgent agent,HttpSession session) throws ControllerException {
		INSCUser operator = (INSCUser) session.getAttribute("insc_user");
		if("1".equals(agent.getRownum())||para.getOffset()>=0){
			Map<String, Object> map = BeanUtils.toMap(agent,para);
			map.put("deptinnercode",operator.getDeptinnercode());
			return insbAgentService.getAgentInfo(map);
		}else{
			return null;
		}
		
	}
	/**
	 * 根据工号获取唯一标识
	 * @param jobnum
	 */

	@RequestMapping(value = "/gettokenbyjobnum", method = RequestMethod.POST)
	@ResponseBody
	public CommonModel getTokenbyJobnum(@RequestParam(value="jobnum") String jobnum, HttpSession session)throws ControllerException{
		INSCUser operator = (INSCUser) session.getAttribute("insc_user");
		return appLoginService.userLoginGetKey(jobnum, operator.getId());
	}
}
