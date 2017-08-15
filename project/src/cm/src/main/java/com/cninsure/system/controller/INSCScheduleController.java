package com.cninsure.system.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.cninsure.core.controller.BaseController;
import com.cninsure.core.exception.ControllerException;
import com.cninsure.core.utils.BeanUtils;
import com.cninsure.system.entity.INSCSchedule;
import com.cninsure.system.entity.INSCUser;
import com.cninsure.system.service.INSCCodeService;
import com.cninsure.system.service.INSCScheduleService;
import com.cninsure.system.service.INSCTaskService;
import com.common.ConstUtil;
import com.common.ModelUtil;
import com.common.PagingParams;

@Controller
@RequestMapping(value = "/schedule/*")
@SessionAttributes("insc_user")
public class INSCScheduleController extends BaseController {
	@Resource
	private INSCScheduleService inscScheduleService;

	@Resource
	private INSCCodeService inscCodeService;

	@Resource
	private INSCTaskService inscTaskService;

	@RequestMapping(value = "list", method = RequestMethod.GET)
	public ModelAndView getScheduleList() throws ControllerException {
		return new ModelAndView("cninsure/system/scheduleList");
	}

	@RequestMapping(value = "listData", method = RequestMethod.GET)
	@ResponseBody
	public String getScheduleListData(@ModelAttribute PagingParams para, @ModelAttribute INSCSchedule schedule) throws ControllerException {
		Map<String, Object> map = BeanUtils.toMap(schedule,para);
		return inscScheduleService.getScheduleList(map);
	}

	@RequestMapping(value = "scheduleEdit", method = RequestMethod.GET)
	public ModelAndView getScheduleEdit() throws ControllerException {
		return new ModelAndView("cninsure/system/scheduleEdit");
	}

	@RequestMapping(value = "gettaskbyTasktypeid", method = RequestMethod.GET)
	@ResponseBody
	public String getTaskByTasktypeid(@RequestParam(value = "id") String id)
			throws ControllerException {
		return inscTaskService.getTaskByTasktypeid(id);
	}

	@RequestMapping(value = "saveSchedule", method = RequestMethod.POST)
	@ResponseBody
	public String saveOrUpdateSchedule(
			@ModelAttribute(value = "insc_user") INSCUser user,
			@ModelAttribute INSCSchedule schedule) throws ControllerException {
		return inscScheduleService.saveOrUpdateSchedule(user, schedule);
	}

	@RequestMapping(value = "getschedule", method = RequestMethod.GET)
	@ResponseBody
	public String getSchedule(@RequestParam(value = "id") String id) {
		return inscScheduleService.getSchedule(id);
	}

	@RequestMapping(value = "deletebyids", method = RequestMethod.GET)
	public String deleteByIds(@RequestParam(value = "ids") List<String> ids)
			throws ControllerException {
		return inscScheduleService.deleteByIds(ids);
	}

	@RequestMapping(value = "scheeditpage", method = RequestMethod.GET)
	public ModelAndView scheEditPage(
			@RequestParam(value = "scheid", required = false, defaultValue = "") String id,
			@RequestParam(value="offset",required=false) Integer offset,
            @RequestParam(value="limit",required=false) Integer limit) {
		ModelAndView mav = new ModelAndView("cninsure/system/scheduleEdit");
		if(ModelUtil.isVoluation(offset, limit)){
            offset=ConstUtil.OFFSET;
            limit=ConstUtil.LIMIT;
        }
		Map<String, Object> map = inscScheduleService.getEditInfo(id,offset,limit);
		mav.addObject("taskTypeList", map.get("taskTypeList"));
		mav.addObject("taskList", map.get("taskList"));
		mav.addObject("scheEntity", map.get("scheEntity"));
		return mav;
	}

	@RequestMapping(value = "", method = RequestMethod.GET)
	@ResponseBody
	public String executebyids(@RequestParam(value = "ids") List<String> ids) {
		return inscScheduleService.executebyids(ids);
	}
}
