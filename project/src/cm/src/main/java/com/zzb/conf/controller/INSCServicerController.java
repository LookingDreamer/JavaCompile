package com.zzb.conf.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.cninsure.core.controller.BaseController;
import com.cninsure.core.exception.ControllerException;
import com.zzb.conf.entity.baseData.INSCServicerModel;
import com.zzb.conf.service.INSCServicerService;

@Controller
@RequestMapping("/servicer/*")
public class INSCServicerController extends BaseController{
	@Resource
	private INSCServicerService inscServicerService;
	
	@RequestMapping(value="/list", method=RequestMethod.GET)
	public ModelAndView list() throws ControllerException{
		List<INSCServicerModel> result = inscServicerService.list();
		ModelAndView mav = new ModelAndView("zzbconf/servicers");
		mav.addObject("result", result);
		return mav;
	}
}
