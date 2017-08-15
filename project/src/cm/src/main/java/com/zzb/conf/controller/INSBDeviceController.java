package com.zzb.conf.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cninsure.core.controller.BaseController;
import com.cninsure.core.exception.ControllerException;
import com.cninsure.core.utils.BeanUtils;
import com.cninsure.system.service.INSCDeptService;
import com.common.PagingParams;
import com.zzb.conf.entity.INSBDevice;
import com.zzb.conf.service.INSBDeviceService;

@Controller
@RequestMapping("/device/*")
public class INSBDeviceController extends BaseController{
	@Resource
	private INSBDeviceService deviceService;
	
	@Resource
	private INSCDeptService service;
	/**
	 * 跳转到列表页面
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public ModelAndView list() throws ControllerException{
		ModelAndView mav = new ModelAndView("zzbconf/authcodemanager");
		return mav;
	}
	
	/**
	 * EDI列表
	 * @param para
	 * @param edi
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "initedilist", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> initUserList(@ModelAttribute PagingParams para, @ModelAttribute INSBDevice device) throws ControllerException{
		Map<String, Object> map = BeanUtils.toMap(device,para);
		return deviceService.initDeviceList(map);
	}
	
	@RequestMapping(value="querydepttree",method=RequestMethod.POST)
	@ResponseBody
	public List<Map<String, String>> queryDeptList(@RequestParam(value="id",required=false) String parentcode) throws ControllerException{
		return service.queryTreeList(parentcode);
	}
	
}