package com.zzb.cm.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cninsure.core.controller.BaseController;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.system.entity.INSCUser;
import com.zzb.cm.service.INSBOrderService;
import com.zzb.conf.service.INSBGroupmembersService;
import com.zzb.conf.service.INSBOrderdeliveryService;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/business/ordermanage/*")
public class MediumPaymentController extends BaseController {
	@Resource
	private INSBOrderService insbOrderService;
	@Resource
	private INSBOrderdeliveryService insbOrderdeliveryService;
	@Resource
	private INSBGroupmembersService  insbGroupmembersService;
	/*
	 * 二次支付成功接口
	 */
	@RequestMapping(value = "mediumPayment", method = RequestMethod.POST)
	@ResponseBody
	public String mediumPayment(@RequestBody Map<String,Object> param,HttpSession session){
		LogUtil.info("------------"+(String)param.get("processinstanceid"));
		INSCUser operator = (INSCUser) session.getAttribute("insc_user");
		try{
			//查询用户是否有二次支付流程的执行权限，如果没有则直接返回无权限，不继续以下操作
			if (!insbGroupmembersService.queryUserGroupPrivileges(operator.getId(), "a")) {
				return "noright";
			} else {
				return insbOrderService.getMediumPayment(
						param.get("processinstanceid").toString(),param.get("inscomcode").toString(),param.get("taskid").toString(),operator.getUsercode());
			}
			
		}catch(Exception e){
			e.printStackTrace();
			return "fail";
		}
	}
	
	/*
	 * 配送页面，保存配送编号
	@RequestMapping(value = "saveTracenumber", method = RequestMethod.POST)
	@ResponseBody
	public String saveTracenumber(@RequestBody Map<String,Object> param){
		System.out.println("saveTracenumber-----------"+param.get("processinstanceid")+param.get("tracenumber")+param.get("codevalue"));
		try{
			INSBOrderdelivery orderdelivery = new INSBOrderdelivery();
			orderdelivery.setTaskid((String)param.get("processinstanceid"));
			orderdelivery = insbOrderdeliveryService.queryOne(orderdelivery);
			orderdelivery.setTracenumber((String)param.get("tracenumber"));     //保存运单号
			orderdelivery.setLogisticscompany((String)param.get("codevalue"));  //保存物流公司id
			insbOrderdeliveryService.updateById(orderdelivery);
			return "success";
		}catch(Exception e){
			e.printStackTrace();
			return "fail";
		}
	}*/
}
