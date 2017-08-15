package com.zzb.extra.controller;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.zzb.extra.entity.INSBActivityinfo;
import com.zzb.extra.service.INSBActivityinfoService;
/**
 * 
 * @author chenhx
 *活动信息管理
 */
@Controller
@RequestMapping("/activityinfo/*")
public class INSBActivityinfoController {
	@Resource
	private INSBActivityinfoService insbActivityinfoService;
	
	/*
	 * 
	 * 查询页面跳转和页面初始化
	 */
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public ModelAndView getList(Map map) {
		ModelAndView mav = new ModelAndView("cm/market/activityinfo/list");
		return mav;
	}
	
	/**
	 * 
	 * @param type 根据活动类型查询对应的活动信息
	 * @return
	 */
	@RequestMapping(value = "getAllList", method = RequestMethod.GET)
	@ResponseBody
	public String getAllList(String type) {
		Map<String, Object> map = new HashMap();
		map.put("type",type);
		return insbActivityinfoService.getList(map);
	}
	

	@RequestMapping(value = "querylist", method = RequestMethod.GET)
	@ResponseBody
	public String querylist(String type) {
		Map<String, Object> map = new HashMap();
		map.put("type",type);
		return insbActivityinfoService.getList(map);
	}
	

	@RequestMapping(value = "edit", method = RequestMethod.GET)
	public ModelAndView editObject(String  id) {
		Map insbActivityinfo=insbActivityinfoService.findById(id);
		ModelAndView mav = new ModelAndView("cm/market/activityinfo/edit");
		mav.addObject("insbActivityinfo", insbActivityinfo);
		return mav;
	}
	
	
	@RequestMapping(value = "save", method = RequestMethod.POST)
	@ResponseBody
	public String  saveObject(String  id,String name,String type,String limited,String effectivetime,String endtime ,String status,String conditions) throws ParseException {
		INSBActivityinfo insbActivityinfo=new INSBActivityinfo();
		insbActivityinfo.setName(name);
		insbActivityinfo.setType(type);
		insbActivityinfo.setEndtime(endtime);
		insbActivityinfo.setConditions(conditions);
		insbActivityinfo.setStatus(status);
		insbActivityinfo.setLimited(limited);
		insbActivityinfo.setEffectivetime(effectivetime);
		Calendar ca=Calendar.getInstance();
		if(id.equals("")){
			insbActivityinfo.setId(String.valueOf(new Date().getTime()));
			insbActivityinfo.setCreatetime(ca.getTime());
			insbActivityinfoService.saveObject(insbActivityinfo);
		}else{
			insbActivityinfo.setId(id);
			insbActivityinfoService.updateObject(insbActivityinfo);
		}
		return "0";
	}
	@RequestMapping(value = "delete", method = RequestMethod.POST)
	@ResponseBody
	public String deleteObect(String id) {
		insbActivityinfoService.deleteObect(id);
		return "0";
	}
}
