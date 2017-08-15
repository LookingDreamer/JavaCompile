package com.zzb.cm.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
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
import com.zzb.cm.controller.vo.OrderListVo;
import com.zzb.cm.service.INSBQuotetotalinfoService;
import com.zzb.conf.service.INSBAgentService;
/**
 * CM系统 车型投保  订单列表
 */ 
@Controller
@RequestMapping("/orderlist/*")
public class INSBOrderListController extends BaseController {
	
	@Resource
	private INSBAgentService insbAgentService;
	
	@Resource
	private INSBQuotetotalinfoService insbQuotetotalinfoService;
	/**
	 * 页面跳转
	 * @param session
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "orderlist", method = RequestMethod.GET)
	@ResponseBody 
	public ModelAndView orderList(HttpSession session) throws ControllerException {
		ModelAndView mav = new ModelAndView("cm/valetcatalogue/orderList");
		return mav;
	}
	
	/**
	 * 初始化订单列表
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "initOrderList", method = RequestMethod.GET)
	@ResponseBody 
	public String initOrderList(HttpSession session,@ModelAttribute PagingParams para,@RequestParam String orderstatus ) throws ControllerException{
		INSCUser inscUser = (INSCUser) session.getAttribute("insc_user");
		String userid = inscUser.getId();
		Map<String, Object> map = BeanUtils.toMap(para);
		map.put("userid", userid);
		map.put("orderstatus", orderstatus);
		return insbQuotetotalinfoService.getQuotetotalinfoByUserid(map);
	}
	
	/**
	 * 初始化订单列表
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "searchOrderList", method = RequestMethod.GET)
	@ResponseBody 
	public String searchOrderList(HttpSession session,@ModelAttribute OrderListVo orderListVo,@ModelAttribute PagingParams para) throws Exception{
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		if (orderListVo.getFrom() != null && !"".equals(orderListVo.getFrom())) {
			System.out.println("------" + orderListVo.getFrom() + "-------------");
			orderListVo.setFromtime(sdf.parse(orderListVo.getFrom()));
		}
		if (orderListVo.getTo() != null && !"".equals(orderListVo.getTo())) {
			orderListVo.setTotime(sdf.parse(orderListVo.getTo()));
		}
		INSCUser inscUser = (INSCUser) session.getAttribute("insc_user");
		String userid = inscUser.getId();
		Map<String, Object> map = BeanUtils.toMap(orderListVo,para);
		map.put("userid", userid);
		return insbQuotetotalinfoService.getQuotetotalinfoByParams(map);
	}
}
