package com.zzb.extra.controller;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cninsure.core.utils.BeanUtils;
import com.common.PagingParams;
import com.zzb.extra.entity.INSBActivityprize;
import com.zzb.extra.service.INSBActivityprizeService;
/**
 * 
 * @author chenhx
 *活动与奖金关联管理
 */
@Controller
@RequestMapping("/activityprize/*")
public class INSBActivityprizeController {
	@Resource
	private INSBActivityprizeService insbActivityprizeService;
	
	
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public ModelAndView getList(Map map) {
		ModelAndView mav = new ModelAndView("cm/market/activityprize/list");
		List<Map<String,String>> alist=insbActivityprizeService.getMap("A");
		List<Map<String,String>> plist=insbActivityprizeService.getMap("P");
		mav.addObject("alist", alist);
		mav.addObject("plist", plist);
		return mav;
	}
	
	
	@RequestMapping(value = "querylist", method = RequestMethod.GET)
	@ResponseBody
	public String querylist(HttpSession session,@ModelAttribute PagingParams para,@ModelAttribute INSBActivityprize insbActivityprize) {
		Map<String, Object> map = BeanUtils.toMap(insbActivityprize,para);
		return insbActivityprizeService.getList(map);
	}

	@RequestMapping(value = "edit", method = RequestMethod.GET)
	public ModelAndView editObject(String  id) {
		Map insbActivityinfo=insbActivityprizeService.findById(id);
		ModelAndView mav = new ModelAndView("cm/market/activityprize/edit");
		mav.addObject("insbActivityinfo", insbActivityinfo);
		return mav;
	}
	
	
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public String  saveObject(String  id,String activityid,String prizeid) throws ParseException {
		INSBActivityprize insbActivityprize=new INSBActivityprize();
		insbActivityprize.setActivityid(activityid);
		insbActivityprize.setPrizeid(prizeid);
		if(id.equals("")){
			insbActivityprize.setId(String.valueOf(new Date().getTime()));
			insbActivityprizeService.saveObject(insbActivityprize);
		}else{
			insbActivityprize.setId(id);
			insbActivityprizeService.updateObject(insbActivityprize);
		}
		return "0";
	}
	@RequestMapping(value = "delete", method = RequestMethod.POST)
	@ResponseBody
	public String deleteObect(String id) {
		insbActivityprizeService.deleteObect(id);
		return "0";
	}

}
