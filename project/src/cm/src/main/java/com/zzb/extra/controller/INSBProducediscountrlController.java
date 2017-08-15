package com.zzb.extra.controller;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.zzb.extra.entity.INSBProducediscountrl;
import com.zzb.extra.service.INSBProducediscountrlService;
/**
 * 供应商产品优惠关联设置
 * 
 * @author chenhx
 *
 */
@Controller
@RequestMapping("/producediscountrl/*")
public class INSBProducediscountrlController {
	@Resource
	private INSBProducediscountrlService INSBProducediscountrlService;
	
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public ModelAndView getList(Map map) {
		ModelAndView mav = new ModelAndView("extra/producediscount/listRl");
		return mav;
	}
	
	
	@RequestMapping(value = "getAllList", method = RequestMethod.GET)
	@ResponseBody
	public String getAllList(String type) {
		Map<String, Object> map = new HashMap();
		map.put("type",type);
		return INSBProducediscountrlService.getList(map);
	}
	

	@RequestMapping(value = "querylist", method = RequestMethod.GET)
	@ResponseBody
	public String querylist(String type) {
		Map<String, Object> map = new HashMap();
		map.put("type",type);
		return INSBProducediscountrlService.getList(map);
	}
	

	
	
	@RequestMapping(value = "save", method = RequestMethod.POST)
	@ResponseBody
	public String  saveObject(String  id,String companyno,String discountid) throws ParseException {
		INSBProducediscountrl INSBProducediscountrl=new INSBProducediscountrl();
		INSBProducediscountrl.setCompanyno(companyno);
		INSBProducediscountrl.setDiscountid(discountid);
		if(id.equals("")){
			INSBProducediscountrl.setId(String.valueOf(new Date().getTime()));
			INSBProducediscountrl.setCreatetime(new Date());
			INSBProducediscountrlService.saveObject(INSBProducediscountrl);
		}else{
			INSBProducediscountrl.setId(id);
			INSBProducediscountrlService.updateObject(INSBProducediscountrl);
		}
		return "0";
	}
	@RequestMapping(value = "delete", method = RequestMethod.POST)
	@ResponseBody
	public String deleteObect(String id) {
		INSBProducediscountrlService.deleteObect(id);
		return "0";
	}
}
