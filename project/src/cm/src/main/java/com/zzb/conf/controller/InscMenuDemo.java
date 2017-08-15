package com.zzb.conf.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.cninsure.core.controller.BaseController;
import com.zzb.conf.entity.Demo;
import com.zzb.conf.service.InscMenuDemoService;

@Controller
@RequestMapping("/demo/*")
public class InscMenuDemo extends BaseController{
	
	@Resource
	private InscMenuDemoService inscMenuDemoService;
	
	@RequestMapping(value = "logintest", method = RequestMethod.GET)
	public ModelAndView logintest(){
		return new ModelAndView("zzbconf/loginDemo");
	}
	
	
	@RequestMapping(value = "demotest", method = RequestMethod.POST)
	public ModelAndView demo(@ModelAttribute("demo") Demo demo){
		ModelAndView mav = new ModelAndView("zzbconf/demo");
		System.out.println(demo.getId()+"     "+demo.getName());
		List<Demo> data = inscMenuDemoService.getData();
		mav.addObject("data", data);
		return mav;
	}
}
