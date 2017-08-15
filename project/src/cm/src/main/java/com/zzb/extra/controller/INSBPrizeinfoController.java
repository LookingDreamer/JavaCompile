package com.zzb.extra.controller;

import java.text.ParseException;
import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cninsure.core.controller.BaseController;
import com.cninsure.core.utils.BeanUtils;
import com.common.PagingParams;
import com.zzb.extra.entity.INSBPrizeinfo;
import com.zzb.extra.service.INSBPrizeinfoService;
/**
 * 奖品信息管理
 * 
 * @author chenhx
 *
 */
@Controller
@RequestMapping("/prizeinfo/*")
public class INSBPrizeinfoController extends BaseController {
	@Resource
	private INSBPrizeinfoService insbPrizeinfoService;

	@RequestMapping(value = "list", method = RequestMethod.GET)
	public ModelAndView getList(Map map) {
		ModelAndView mav = new ModelAndView("cm/market/list");
		return mav;
	}

	@RequestMapping(value = "querylist", method = RequestMethod.GET)
	@ResponseBody
	public String querylist(HttpSession session,@ModelAttribute PagingParams para,@ModelAttribute INSBPrizeinfo insbPrizeinfo) {
		Map<String, Object> map = BeanUtils.toMap(insbPrizeinfo,para);
		return insbPrizeinfoService.getList(map);
	}

	@RequestMapping(value = "edit", method = RequestMethod.GET)
	public ModelAndView editObject(String  id) {
		Map insbPrizeinfo=insbPrizeinfoService.findById(id);
		ModelAndView mav = new ModelAndView("cm/market/edit");
		mav.addObject("insbPrizeinfo", insbPrizeinfo);
		return mav;
	}

	@RequestMapping(value = "save", method = RequestMethod.POST)
	@ResponseBody
	public String saveObject(String id, String name,int type,int counts,String effectivetime,String invalidtime,int effectiveDates) throws ParseException {
		INSBPrizeinfo insbPrizeinfo=new INSBPrizeinfo();
		
		insbPrizeinfo.setName(name);
		insbPrizeinfo.setType(type);
		insbPrizeinfo.setCounts(counts);
		insbPrizeinfo.setEffectiveDates(effectiveDates);
		insbPrizeinfo.setInvalidtime(invalidtime);
		insbPrizeinfo.setEffectivetime(effectivetime);
		if(id.equals("")){
			insbPrizeinfo.setId(String.valueOf(new Date().getTime()));
			insbPrizeinfoService.saveObject(insbPrizeinfo);
		}else{
			insbPrizeinfo.setId(id);
			insbPrizeinfoService.updateObject(insbPrizeinfo);
		}
		
		return "0";
	}

	@RequestMapping(value = "delete", method = RequestMethod.POST)
	@ResponseBody
	public String deleteObect(String id) {
		insbPrizeinfoService.deleteObect(id);
		return "0";
	}

}
