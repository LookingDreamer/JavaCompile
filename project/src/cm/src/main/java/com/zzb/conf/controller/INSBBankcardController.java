package com.zzb.conf.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
import com.cninsure.core.utils.StringUtil;
import com.cninsure.core.utils.UUIDUtils;
import com.cninsure.system.entity.INSCUser;
import com.common.PagingParams;
import com.zzb.conf.entity.INSBBankcard;
import com.zzb.conf.service.INSBBankcardService;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/bankcard/*")
public class INSBBankcardController extends BaseController{
	@Resource
	private INSBBankcardService bankcardService;
	
	/**
	 * 跳转到列表页面
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public ModelAndView list() throws ControllerException{
		ModelAndView mav = new ModelAndView("zzbconf/bankcardlist");
		List<Map<String, String>> banknamemaplist =  bankcardService.selectBanknamelist();
		mav.addObject("banknamelist", banknamemaplist);
		return mav;
	}
	
	/**
	 * 银行卡列表
	 * @param para
	 * @param bankcard
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "initbankcardlist", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> initBankCardList(@ModelAttribute PagingParams para, @ModelAttribute INSBBankcard bankcard) throws ControllerException{
		Map<String, Object> map = BeanUtils.toMap(bankcard,para);
		return bankcardService.initBankCardList(map);
	}
	
	/**
	 * 跳转新增页面
	 * @param id
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "addbankcard", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView addBankCard(@RequestParam(value="id",required=false) String id)throws ControllerException{
		ModelAndView model = new ModelAndView("zzbconf/bankcardsave");
		return model;
	}
	
	/**
	 * 添加或修改银行卡列表
	 * @param session
	 * @param bankcard
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "saveorupdate", method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView  saveorupdate(HttpSession session,@ModelAttribute INSBBankcard bankcard)throws ControllerException{
		ModelAndView model = new ModelAndView("zzbconf/bankcardlist");
		String operator = ((INSCUser)session.getAttribute("insc_user")).getUsername();
		Date date = new Date();
		if(StringUtil.isEmpty(bankcard.getId())){
			bankcard.setId(UUIDUtils.random());
			bankcard.setOperator(operator);
			bankcard.setCreatetime(date);
			bankcard.setModifytime(date);
			bankcardService.addBankCard(bankcard);
		}else{
			bankcard.setOperator(operator);
			bankcard.setModifytime(date);
			bankcardService.updateById(bankcard);
		}
		return model;
	}
	
	/**
	 * 查看并修改银行卡信息
	 * @param id
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "bankcardedit", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView  bankCardedit( @RequestParam("id") String id)throws ControllerException{
		ModelAndView model = new ModelAndView("zzbconf/bankcardedit");
		model.addObject("bankcard", bankcardService.queryById(id));
		return model;
	}
	
	
	/**
	 * 删除银行卡信息
	 * @param session
	 * @param id
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "deletebyid", method = RequestMethod.GET)
	@ResponseBody
	public String deleteById(HttpSession session, @RequestParam(value="id") String id) throws ControllerException{
		int count = bankcardService.deleteById(id);
		JSONObject jsonObject = new JSONObject();
		jsonObject.accumulate("count", count);
		return jsonObject.toString();
	}
	
	@RequestMapping(value = "queryByBanktoname", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> queryByBanktoname(@ModelAttribute PagingParams para,@RequestParam(value="banktoname") String banktoname) throws ControllerException{
		
		Map<String, Object> map = BeanUtils.toMap(para);
		map.put("banktoname", banktoname);
		return bankcardService.queryByBanktoname(map);
		
	}
	
}