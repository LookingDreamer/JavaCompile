package com.zzb.extra.controller;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.zzb.extra.entity.INSBProducediscount;
import com.zzb.extra.service.INSBProducediscountService;
/**
 * 供应商产品优惠管理
 * @author chenhx
 *
 */
@Controller
@RequestMapping("/producediscount/*")
public class INSBProducediscountController {
	@Resource
	private INSBProducediscountService INSBProducediscountService;
	
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public ModelAndView getList(Map map) {
		ModelAndView mav = new ModelAndView("extra/producediscount/list");
		return mav;
	}
	
	
	@RequestMapping(value = "getAllList", method = RequestMethod.GET)
	@ResponseBody
	public String getAllList(String type) {
		Map<String, Object> map = new HashMap();
		map.put("type",type);
		return INSBProducediscountService.getList(map);
	}
	

	@RequestMapping(value = "querylist", method = RequestMethod.GET)
	@ResponseBody
	public String querylist(String type) {
		Map<String, Object> map = new HashMap();
		map.put("type",type);
		return INSBProducediscountService.getList(map);
	}
	
	
	@RequestMapping(value = "save", method = RequestMethod.POST)
	@ResponseBody
	public String  saveObject(String  id,String name,String note,String type) throws ParseException {
		INSBProducediscount INSBProducediscount=new INSBProducediscount();
		INSBProducediscount.setName(name);
		INSBProducediscount.setType(type);
		INSBProducediscount.setNote(note);
		if(id.equals("")){
			INSBProducediscount.setId(String.valueOf(new Date().getTime()));
			INSBProducediscount.setCreatetime(new Date());
			INSBProducediscountService.saveObject(INSBProducediscount);
		}else{
			INSBProducediscount.setId(id);
			INSBProducediscountService.updateObject(INSBProducediscount);
		}
		return "0";
	}
	@RequestMapping(value = "delete", method = RequestMethod.POST)
	@ResponseBody
	public String deleteObect(String id) {
		INSBProducediscountService.deleteObect(id);
		return "0";
	}
}
