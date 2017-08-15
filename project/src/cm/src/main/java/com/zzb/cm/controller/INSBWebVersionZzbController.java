package com.zzb.cm.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import com.cninsure.core.controller.BaseController;

/**
 * 网页版掌中宝
 */
@Controller
@RequestMapping("business/webversionzzb/*")
public class INSBWebVersionZzbController extends BaseController {
	
	//跳转到代客录单下的网页掌中宝页面
	@RequestMapping(value="show", method=RequestMethod.GET)
	public ModelAndView toCarTaskManageListPage(){
		ModelAndView mav = new ModelAndView("cm/webversionzzb/webVersionZzb");
		return mav;
	}
}
