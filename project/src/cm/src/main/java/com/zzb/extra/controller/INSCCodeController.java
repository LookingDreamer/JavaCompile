package com.zzb.extra.controller;

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
import com.cninsure.system.entity.INSCCode;
import com.cninsure.system.entity.INSCUser;
import com.cninsure.system.service.INSCCodeService;
import com.common.PagingParams;

@Controller
@RequestMapping("/code/*")
public class INSCCodeController extends BaseController {
	@Resource
	private INSCCodeService inscCodeService;
	
	@RequestMapping(value="codemanage",method=RequestMethod.GET)
	public ModelAndView toCodeManageListPage(HttpSession session){
		ModelAndView mav = new ModelAndView("extra/codemanage");
		return mav;
	}
	
	@RequestMapping(value="querycodelist",method=RequestMethod.GET, produces = "text/json;charset=UTF-8")
	@ResponseBody
	public String selectAllErrorInfo(HttpSession session, @ModelAttribute PagingParams para){
		Map<String, Object> map = BeanUtils.toMap(para);
		String res = inscCodeService.selectAllErrorCodeInfo(map);
		return res;
	}
	
	@RequestMapping(value="saveeditcode",method=RequestMethod.POST, produces = "text/json;charset=UTF-8")
	@ResponseBody
	public String saveEditCode(HttpSession session, @ModelAttribute INSCCode para) throws ControllerException{
		INSCUser user = (INSCUser) session.getAttribute("insc_user");
		para.setOperator(user.getUsercode());
		return inscCodeService.saveEditCode(para);
	}
}
