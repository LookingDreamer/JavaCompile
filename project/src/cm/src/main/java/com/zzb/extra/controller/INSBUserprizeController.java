package com.zzb.extra.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.zzb.extra.entity.INSBUserprize;
import com.zzb.extra.service.INSBActivityprizeService;
import com.zzb.extra.service.INSBUserprizeService;
/*
 * 
 * 用户活动获取奖品维护
 */
@Controller
@RequestMapping("/userprize/*")
public class INSBUserprizeController {
	@Resource
	private INSBUserprizeService insbUserprizeService;
	
	@Resource
	private INSBActivityprizeService insbActivityprizeService;
	
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public ModelAndView getList(Map map) {
		ModelAndView mav = new ModelAndView("cm/market/userprize/list");
		return mav;
	}
	
	@RequestMapping(value = "querylist", method = RequestMethod.GET)
	@ResponseBody
	public String querylist(String uid,String type) {
		Map<String, Object> map = new HashMap();
		map.put("uid",uid);
		map.put("type",type);
		return insbUserprizeService.getList(map);
	}
	
	@RequestMapping(value = "setting", method = RequestMethod.GET)
	public String setting(String  hid,String jid) {
		return null;
	}
	
	
	
	
	
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public String saveObject(String activityid, String uid,String prizeid) {
		INSBUserprize insbUserprize =new INSBUserprize();
		insbUserprize.setActivityid(activityid);
		insbUserprize.setPrizeid(prizeid);
		insbUserprize.setCounts(1);
		
		SimpleDateFormat sb=new SimpleDateFormat("YYYY-MM-DD");
		
		insbUserprize.setId(String.valueOf(new Date().getTime()));
		insbUserprize.setGettime(sb.format(new Date()));
		insbUserprize.setUid(uid);
		insbUserprize.setStatus('0');
		insbUserprizeService.saveObject(insbUserprize);
		return "0";
	}
	@RequestMapping(value = "delete", method = RequestMethod.POST)
	@ResponseBody
	public String deleteObect(String id) {
		insbUserprizeService.deleteObect(id);
		return "0";
	}
	@RequestMapping(value = "update", method = RequestMethod.POST)
	@ResponseBody
	public String updateObject(@RequestBody String id) {
		JSONObject ob=JSONObject.parseObject(id);
		String aid=ob.getString("id");
		INSBUserprize insbUserprize =insbUserprizeService.findById(aid);
		if(insbUserprize==null){
			return "1";
		}
         SimpleDateFormat sb=new SimpleDateFormat("YYYY-MM-DD");
		insbUserprize.setUsetime(sb.format(new Date()));
		insbUserprizeService.updateObject(insbUserprize);
		return "0";
	}
}
