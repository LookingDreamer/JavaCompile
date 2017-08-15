package com.zzb.extra.controller;

import com.cninsure.core.controller.BaseController;
import com.cninsure.core.exception.ControllerException;
import com.cninsure.core.utils.BeanUtils;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.cninsure.core.utils.UUIDUtils;
import com.cninsure.system.entity.INSCUser;
import com.common.PagingParams;
import com.zzb.conf.controller.vo.BaseVo;
import com.zzb.extra.entity.INSBAgentBankcard;
import com.zzb.extra.service.INSBAgentBankcardService;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/agentbankcard/*")
public class INSBAgentBankcardController extends BaseController{
	@Resource
	private INSBAgentBankcardService agentbankcardService;
	
	/**
	 * 跳转到列表页面
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public ModelAndView list() throws ControllerException{
		ModelAndView mav = new ModelAndView("zzbconf/agentbankcardlist");
		List<Map<String, String>> banknamemaplist =  agentbankcardService.selectAgentBanknamelist();
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
	public Map<String, Object> initBankCardList(@ModelAttribute PagingParams para, @ModelAttribute INSBAgentBankcard bankcard) throws ControllerException{
		Map<String, Object> map = BeanUtils.toMap(bankcard,para);
		String cardagentid = (String)map.get("cardagentid");
		if(cardagentid == null || cardagentid == ""){
			LogUtil.debug("initbankcardlist 错误：提交查询代理人帐号为空");
			return null;
		}else {
			return agentbankcardService.initAgentBankCardList(map);
		}
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
		ModelAndView model = new ModelAndView("zzbconf/agentbankcardsave");
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
	public ModelAndView  saveorupdate(HttpSession session,@ModelAttribute INSBAgentBankcard bankcard)throws ControllerException{
		ModelAndView model = new ModelAndView("zzbconf/agentbankcardlist");
		String operator = ((INSCUser)session.getAttribute("insc_user")).getUsername();
		Date date = new Date();
		if(StringUtil.isEmpty(bankcard.getId())){
			bankcard.setId(UUIDUtils.random());
			bankcard.setOperator(operator);
			bankcard.setCreatetime(date);
			bankcard.setModifytime(date);
			agentbankcardService.addAgentBankCard(bankcard);
		}else{
			bankcard.setOperator(operator);
			bankcard.setModifytime(date);
			agentbankcardService.updateById(bankcard);
		}
		return model;
	}

	/**
	 * 接口:
	 * 添加或修改银行卡列表
	 * @param session
	 * @param bankcard
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "saveorupdatecard", method = RequestMethod.POST)
	@ResponseBody
	public BaseVo saveorupdatecard(HttpSession session,@ModelAttribute INSBAgentBankcard bankcard)throws ControllerException{
		BaseVo bv = new BaseVo();
		//String operator = ((INSCUser)session.getAttribute("insc_user")).getUsername();
		Date date = new Date();
		if (bankcard.getCardagentid() == null || "".equals(bankcard.getCardagentid().trim())) {
			bv.setStatus("2");
			bv.setMessage("操作失败,提交配银行卡所属用户id为空");
		} else {
			if (StringUtil.isEmpty(bankcard.getId())) {
				//bankcard.setOperator(operator);
				bankcard.setCreatetime(date);
				bankcard.setModifytime(date);
				agentbankcardService.addAgentBankCard(bankcard);
				bv.setStatus("1");
				bv.setMessage("新增成功");
			} else {
				//bankcard.setOperator(operator);
				bankcard.setModifytime(date);
				agentbankcardService.updateById(bankcard);
				bv.setStatus("1");
				bv.setMessage("修改成功");
			}
		}
		return bv;
	}


	/**
	 * 接口：
	 * 设置默认默认卡
	 * @param id 卡id
	 * @throws ControllerException
	 */
	@RequestMapping(value = "setDefaultCard", method = RequestMethod.GET)
	@ResponseBody
	public BaseVo setDefaultCard(@RequestParam("id") String id,@RequestParam("cardagentid") String cardagentid) throws ControllerException{
		BaseVo bv = new BaseVo();
		if(id == null ||cardagentid == null || "".equals(id.trim()) || "".equals(cardagentid.trim())){
			LogUtil.debug("setDefaultCard 错误：提交卡id 或 卡所属代理人id 为空");
			bv.setStatus("2");
			bv.setMessage("设置失败,提交卡id 或 卡所属代理人id 为空");
		}else{
			agentbankcardService.setDefaultCard(id, cardagentid);
			bv.setStatus("1");
			bv.setMessage("设置成功");
		}
		return bv;
	}

	/**
	 * 接口
	 * 银行卡列表
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "agentBankcardquerybyagentid", method = RequestMethod.GET)
	@ResponseBody
	public BaseVo agentBankcardquerybyagentid(@RequestParam("cardagentid") String cardagentid) throws ControllerException{
		BaseVo bv = new BaseVo();
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("cardagentid", cardagentid);
			if (cardagentid == null || cardagentid == "") {
				LogUtil.debug("initbankcardlist 错误：提交查询代理人帐号为空");
				bv.setStatus("2");
				bv.setMessage("查询失败，错误：提交查询代理人帐号为空");
				return bv;
			} else {
				List<Map<Object, Object>> list = agentbankcardService.agentBankcardquerybyagentid(map);
				bv.setStatus("1");
				bv.setMessage("查询成功");
				bv.setResult(list);
				return bv;
			}
		}catch(Exception ex){
			ex.printStackTrace();
			bv.setStatus("2");
			bv.setMessage("查询失败，发生异常");
			return bv;
		}
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
		ModelAndView model = new ModelAndView("zzbconf/agentbankcardedit");
		model.addObject("bankcard", agentbankcardService.queryById(id));
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
		int count = agentbankcardService.deleteById(id);
		JSONObject jsonObject = new JSONObject();
		jsonObject.accumulate("count", count);
		return jsonObject.toString();
	}

	@RequestMapping(value = "queryByBanktoname", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> queryByBanktoname(@ModelAttribute PagingParams para,@RequestParam(value="banktoname") String banktoname) throws ControllerException{

		Map<String, Object> map = BeanUtils.toMap(para);
		map.put("banktoname", banktoname);
		return agentbankcardService.queryByAgentBanktoname(map);

	}

}