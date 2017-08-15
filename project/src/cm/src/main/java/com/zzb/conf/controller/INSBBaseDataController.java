package com.zzb.conf.controller;


import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cninsure.core.controller.BaseController;
import com.zzb.conf.entity.INSBBaseData;
import com.zzb.conf.service.INSBBaseDataService;

@Controller
@RequestMapping("/baseData")
public class INSBBaseDataController extends BaseController  {

	@Resource
	private INSBBaseDataService service;
	
	/**
	 * 初始化页面
	 * @return
	 */
	@RequestMapping(value="/list",method=RequestMethod.POST)
	@ResponseBody
	public String getBaseDataList(int page,int rows ){
		return service.queryBaseDataList(page,rows);
		
	}
	/**
	 * 
	 * 按条件查询
	 * @param model
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping(value="/query",method=RequestMethod.GET)
	@ResponseBody
	public ModelAndView getBaseDataListByModel(@ModelAttribute INSBBaseData model,int page, int rows ){
		List<INSBBaseData> result = service.queryBaseDataListByModel(model, page, rows);
		ModelAndView mav = new ModelAndView("zzbconf/baseDatalist");
		mav.addObject("baseDataList", result);
		return mav;
		
	}
	/**
	 * 跳转到列表页面
	 * @return
	 */
	@RequestMapping(value="/tolist",method=RequestMethod.GET)
	public ModelAndView baseData2List(){
		ModelAndView mav = new ModelAndView("zzbconf/baseDataList");
		return mav;
		
	}
	/**
	 * 跳转到编辑页面/新增页面
	 * @return
	 */
	@RequestMapping(value="/toedite",method=RequestMethod.GET)
	public ModelAndView baseData2Edite(){
		ModelAndView mav = new ModelAndView("zzbconf/baseDataEdit");
		return mav;
	}
	
	/**
	 * 新增
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/add", method=RequestMethod.POST)
	public ModelAndView addBaseData(@ModelAttribute INSBBaseData model){
		int result = service.addBaseData(model);
		ModelAndView mav = new ModelAndView("zzbconf/baseDataEdit");
		mav.addObject("result",result);
		return mav;
	}
	
	@RequestMapping(value="/delete",method=RequestMethod.POST)
	public String deleteBaseData(String id){
	    service.removeBaseData(id);
		return "zzbconf/baseDataEdit";
	}
	
	@RequestMapping(value="/update",method=RequestMethod.PUT)
	public String updateBaseData(@ModelAttribute INSBBaseData baseDataModel){
		service.modifyBaseData(baseDataModel);
		return "zzbconf/baseDataEdit";
	}
	
	
}
