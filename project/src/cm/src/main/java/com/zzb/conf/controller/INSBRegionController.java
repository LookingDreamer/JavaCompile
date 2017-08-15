package com.zzb.conf.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import com.cninsure.core.exception.ControllerException;
import com.cninsure.core.utils.JsonUtil;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.system.entity.INSCUser;
import com.zzb.mobile.model.CommonModel;
import com.zzb.mobile.service.AppGeneralSettingService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cninsure.core.controller.BaseController;
import com.zzb.conf.entity.INSBRegion;
import com.zzb.conf.service.INSBRegionService;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping("/region/*")
public class INSBRegionController extends BaseController{
	@Resource
	private INSBRegionService regionservice;
	@Resource
	private AppGeneralSettingService generalSettingService;
	/**
	 * 根据 parentid 获取 下级区域
	 * @param parentid
	 * @return
	 */
	@RequestMapping(value = "/getregionsbyparentid", method = RequestMethod.GET)
	@ResponseBody
	public List<INSBRegion> getregionsbyparentid(String parentid) {
		INSBRegion regionvo = new INSBRegion();
		regionvo.setParentid(parentid);
		return regionservice.queryList(regionvo);
	}

	/**
	 * 初始化地区
	 * @param id
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "initRegion",method= RequestMethod.POST)
	@ResponseBody
	public List<Map<String, Object>> initRegion(@RequestParam(value="id", required=false) String id)throws ControllerException{
		LogUtil.info("initRegion:" + id);
		return regionservice.initRegion(id);
	}

	@RequestMapping(value = "regionMng" ,method =RequestMethod.GET )
	public ModelAndView regionMng()throws ControllerException{
		ModelAndView model = new ModelAndView("system/regionMng");
		return model;
	}

	@RequestMapping(value = "update" ,method =RequestMethod.POST )
	@ResponseBody
	public int update(INSBRegion insbRegion, HttpSession httpSession)throws ControllerException{
		INSCUser loginUser = (INSCUser) httpSession.getAttribute("insc_user");
		LogUtil.info("loginUser.getUsercode():" + loginUser.getUsercode());
		LogUtil.info("insbRegion:" + JsonUtil.getJsonString(insbRegion));
		return regionservice.updateRegion(insbRegion, loginUser.getUsercode());
	}
}
