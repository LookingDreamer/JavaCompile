package com.zzb.conf.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.cninsure.core.controller.BaseController;
import com.cninsure.core.exception.ControllerException;
import com.zzb.conf.entity.INSBAutoconfigshow;
import com.zzb.conf.service.INSBAutoconfigshowService;

@Controller
@RequestMapping("/autoconfigshow/*")
public class INSBAutoConfigShowController extends BaseController {
	@Resource
	private INSBAutoconfigshowService autoconfigshowService;
	
	/**
	 * 返回平台deptId下拥有“平台(标识05)”能力的所有保险公司
	 * @param deptId
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value="deptabilitycompany", method = RequestMethod.GET)
	public ModelAndView getOneAbilityByDeptId(@RequestParam("deptId") String deptId) throws ControllerException{
		ModelAndView modelView = new ModelAndView();
		List<INSBAutoconfigshow> resList = autoconfigshowService.getOneAbilityByDeptId(deptId, "05");//05 代表平台能力
		modelView.addObject("deptcompanylist", resList);
		return modelView;
	}
}
