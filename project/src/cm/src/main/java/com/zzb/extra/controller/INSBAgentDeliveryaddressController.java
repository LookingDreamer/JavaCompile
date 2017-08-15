package com.zzb.extra.controller;

import com.cninsure.core.controller.BaseController;
import com.cninsure.core.exception.ControllerException;
import com.cninsure.core.utils.BeanUtils;
import com.cninsure.core.utils.LogUtil;
import com.common.PagingParams;
import com.zzb.extra.entity.INSBAgentDeliveryaddress;
import com.zzb.extra.service.INSBAgentDeliveryaddressService;
import com.zzb.conf.controller.vo.BaseVo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/agentdeliveryaddress/*")
public class INSBAgentDeliveryaddressController extends BaseController{
	@Resource
	private INSBAgentDeliveryaddressService agentDeliveryaddressService;
	@Resource
	private HttpServletRequest request;

	/**
	 * 代理人配送地址列表
	 * @param para
	 * @param agentDeliveryaddress
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "initagentdeliveryaddresslist", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> initBankCardList(@ModelAttribute PagingParams para, @ModelAttribute INSBAgentDeliveryaddress agentDeliveryaddress) throws ControllerException{
		Map<String, Object> map = BeanUtils.toMap(agentDeliveryaddress,para);
		String agentid = (String)map.get("agentid");
		if(agentid == null || "".equals(agentid.trim())){
			LogUtil.debug("initBankCardList 错误：提交查询代理人为空");
			return null;
		}else{
			return agentDeliveryaddressService.initAgentDeliveryaddressList(map);
		}
	}


	/**
	 * 接口：
	 * 保存或修改用户配送地址信息，来源于微信的 MINI掌中保
	 * 必需项：openid、usersource
	 *
	 * @return
	 */
	@RequestMapping(value = "saveOrUpdateAgentDeliveryaddress", method = RequestMethod.POST)
	@ResponseBody
	public BaseVo saveOrUpdateAgentDeliveryaddress(HttpSession session,@ModelAttribute INSBAgentDeliveryaddress agentDeliveryaddress) {
		//INSCUser user = (INSCUser) session.getAttribute("insc_user");
		BaseVo bv = new BaseVo();
		try {
			if(agentDeliveryaddress!=null && agentDeliveryaddress.getAgentid()!=null && !"".equals(agentDeliveryaddress.getAgentid().trim())){
				agentDeliveryaddressService.saveOrUpdateAgentDeliveryaddress(agentDeliveryaddress);
				bv.setStatus("1");
				bv.setMessage("操作成功");
			}else{
				bv.setStatus("2");
				bv.setMessage("操作失败,提交配送地址所属用户id为空");
			}
		} catch (Exception e) {
			bv.setStatus("2");
			bv.setMessage("操作失败,发生异常");
			e.printStackTrace();
		}
		return bv;
	}


	/**
	 * 接口：
	 * 代理人配送地址列表
	 * @param agentid
	 * @throws ControllerException
	 */
	@RequestMapping(value = "queryAgentDeliveryAddressList", method = RequestMethod.GET)
	@ResponseBody
	public BaseVo queryAgentDeliveryAddressList(@RequestParam("agentid") String agentid) throws ControllerException{
		BaseVo bv = new BaseVo();
		try {
			if (agentid == null || "".equals(agentid.trim())) {
				LogUtil.debug("queryAgentDeliveryAddressList 错误：提交查询代理人为空");
				bv.setStatus("2");
				bv.setMessage("查询错误，提交查询代理人为空");
				return bv;
			} else {
				List<Map<Object, Object>> list = agentDeliveryaddressService.queryAgentDeliveryAddressList(agentid);
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
	 * 接口：
	 * 按id查询代理人具体一个配送地址
	 * @param id
	 * @throws ControllerException
	 */
	@RequestMapping(value = "queryAgentDeliveryAddressByid", method = RequestMethod.GET)
	@ResponseBody
	public BaseVo queryAgentDeliveryAddressByid(@RequestParam("id") String id) throws ControllerException{
		BaseVo bv = new BaseVo();
		try {
			if (id == null || "".equals(id.trim())) {
				LogUtil.debug("queryAgentDeliveryAddressByid 错误：提交配送地址id 为空");
				bv.setStatus("2");
				bv.setMessage("查询错误，提交配送地址id 为空");
				return bv;
			} else {
				bv.setStatus("1");
				bv.setMessage("查询成功");
				INSBAgentDeliveryaddress address = agentDeliveryaddressService.queryById(id);
				bv.setResult(address);
				return bv;
			}
		}catch(Exception ex){
			bv.setStatus("2");
			bv.setMessage("查询发生异常");
			ex.printStackTrace();
			return bv;
		}
	}

	/**
	 * 接口：
	 * 按id删除代理人具体一个配送地址
	 * @param id
	 * @throws ControllerException
	 */
	@RequestMapping(value = "deleteAgentDeliveryAddressByid", method = RequestMethod.GET)
	@ResponseBody
	public BaseVo deleteAgentDeliveryAddressByid(@RequestParam("id") String id) throws ControllerException{
		BaseVo bv = new BaseVo();
		if(id == null || "".equals(id.trim())){
			LogUtil.debug("deleteAgentDeliveryAddressByid 错误：提交配送地址id 为空");
			bv.setStatus("2");
			bv.setMessage("删除失败,提交配送地址id 为空");
		}else {
			agentDeliveryaddressService.deleteById(id);
			bv.setStatus("1");
			bv.setMessage("删除成功");
		}
		return bv;
	}

	/**
	 * 接口：
	 * 设置默认配送地址
	 * @param id
	 * @throws ControllerException
	 */
	@RequestMapping(value = "setDefaultAgentDeliveryAddress", method = RequestMethod.GET)
	@ResponseBody
	public BaseVo setDefaultAgentDeliveryAddress(@RequestParam("id") String id,@RequestParam("agentid") String agentid) throws ControllerException{
		BaseVo bv = new BaseVo();
		if(id == null ||agentid == null || "".equals(id.trim()) || "".equals(agentid.trim())){
			LogUtil.debug("setDefaultAgentDeliveryAddress 错误：提交配送地址id 或 代理人id 为空");
			bv.setStatus("2");
			bv.setMessage("设置失败,提交配送地址id 或 代理人id 为空");
		}else{
			agentDeliveryaddressService.setDefaultAgentDeliveryAddress(id, agentid);
			bv.setStatus("1");
			bv.setMessage("设置成功");
		}
		return bv;
	}


}