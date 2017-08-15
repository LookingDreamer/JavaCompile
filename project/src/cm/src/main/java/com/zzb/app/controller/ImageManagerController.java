package com.zzb.app.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cninsure.core.controller.BaseController;
import com.cninsure.core.exception.ControllerException;
import com.zzb.app.model.ImageManagerModel;
import com.zzb.conf.service.INSBPolicyitemService;

@Controller
@RequestMapping("/imageApi/imageAPI/*")
public class ImageManagerController extends BaseController{
	@Resource
	private INSBPolicyitemService policyitemService;
	
	
	/**
	 * 根据代理人查询影像list
	 * 请求方式：POST
	 * 请求地址	/imageApi/imageAPI/tqSearchListByAgentId
	 * @param agentid
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value="/tqSearchListByAgentId",method=RequestMethod.POST)
	@ResponseBody
	public String tqSearchListByAgentId(@RequestParam(value="agentid") String agentid) throws ControllerException{
		return policyitemService.getImageInfoList(agentid);
	}
	/**
	 * 查询影像详细信息
	 * 请求方式	POST
	 * 请求地址	/imageApi/imageAPI/tqSearchAlbumById
	 * @param policyitemid  投保单id
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value="/tqSearchAlbumById",method=RequestMethod.POST)
	@ResponseBody
	public String tqSearchAlbumById(@RequestParam(value="policyitemid") String policyitemid	) throws ControllerException{
		return policyitemService.getImageInfo(policyitemid);
	}
	
	
	/**
	 * 添加影像
	 * 请求方式：post
	 * 请求地址：/imageApi/imageAPI/COS
	 * @param model
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value="/cos",method=RequestMethod.POST)
	@ResponseBody
	public String cos(@ModelAttribute ImageManagerModel model)throws ControllerException{
		return policyitemService.cos(model);
	}
	
	
	
	
}
