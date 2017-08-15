package com.zzb.cm.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cninsure.core.exception.ControllerException;
import com.cninsure.core.utils.BeanUtils;
import com.cninsure.core.utils.StringUtil;
import com.cninsure.system.entity.INSCDept;
import com.cninsure.system.entity.INSCUser;
import com.cninsure.system.service.INSCDeptService;
import com.common.PagingParams;
import com.zzb.cm.entity.INSBFlowerror;
import com.zzb.cm.service.INSBFlowerrorService;
import com.zzb.conf.dao.INSBPolicyitemDao;
import com.zzb.conf.entity.INSBPolicyitem;

@Controller
@RequestMapping("/flowerror/*")
public class INSBFlowErrorControll {
	@Resource INSBFlowerrorService insbFlowerrorService;
	@Resource INSBPolicyitemDao insbPolicyitemDao;
	@Resource INSCDeptService inscDeptService;
	/**
	 * 跳转到列表页面
	 * 
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "errorinfo", method = RequestMethod.GET)
	public ModelAndView list() throws ControllerException {
		ModelAndView mav = new ModelAndView("system/errorinfo");
		return mav;
	}
	
	@RequestMapping(value = "showinfo", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> showinfo(HttpSession session,
			@RequestParam(value="taskid",required=false) String taskid,
			@RequestParam(value="inscomcode",required=false) String inscomcode,
			@RequestParam(value="firoredi",required=false) String firoredi,
			@RequestParam(value="flowcode",required=false) String flowcode,
			@RequestParam(value="taskcreatetimeup") String taskcreatetimeup,
			@RequestParam(value="taskcreatetimedown") String taskcreatetimedown,
			@ModelAttribute PagingParams para) throws Exception{
		INSCUser loginUser = (INSCUser) session.getAttribute("insc_user");
		INSCDept d = inscDeptService.getOrgDeptByDeptCode(loginUser.getUserorganization());
		Map<String, Object> map = new HashMap<String, Object>();
		INSBFlowerror query = new INSBFlowerror();
		if(!StringUtil.isEmpty(taskid)){
			query.setTaskid(taskid);
		}
		if(!StringUtil.isEmpty(inscomcode)){
			query.setInscomcode(inscomcode);
		}
		if(!StringUtil.isEmpty(firoredi)){
			query.setFiroredi(firoredi);
		}
		if(!StringUtil.isEmpty(flowcode)){
			query.setFlowcode(flowcode);
		}
		Map<String,Object> queryMap = new HashMap<>();
		queryMap = BeanUtils.toMap(query,para);
		if (!StringUtil.isEmpty(taskcreatetimeup)) {
			queryMap.put("taskcreatetimeup", taskcreatetimeup);
		}
		if (!StringUtil.isEmpty(taskcreatetimedown)) {
			queryMap.put("taskcreatetimedown", taskcreatetimedown);
		}
		if (!StringUtil.isEmpty(d.getDeptinnercode())) {
			queryMap.put("deptcode", d.getDeptinnercode());
		}
		map = insbFlowerrorService.initErrorList(queryMap);
		return map;
	}
	@RequestMapping(value = "showflowcode", method = RequestMethod.GET)
	@ResponseBody
	public List<INSBFlowerror> showflowcode(){
		return insbFlowerrorService.selectflowcode();
	}
	/**
	 * 跳转到列表页面
	 * 
	 * @return 
	 * @throws ControllerException
	 */
	@RequestMapping(value = "pushtocoreerror", method = RequestMethod.GET)
	public ModelAndView pushtocoreerrorlist() throws ControllerException {
		ModelAndView mav = new ModelAndView("system/pushtocoreerror");
		return mav;
	}

	@RequestMapping(value = "showpushtocoreinfo", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> showpushtocoreinfo(HttpSession session,
			@RequestParam(value="taskid",required=false) String taskid,
			@RequestParam(value="inscomcode",required=false) String inscomcode,
			@RequestParam(value="taskcreatetimeup") String taskcreatetimeup,
			@RequestParam(value="taskcreatetimedown") String taskcreatetimedown,
			@RequestParam(value="deptcode",required=false) String deptcode,
			@ModelAttribute PagingParams para) throws Exception{
		INSCUser loginUser = (INSCUser) session.getAttribute("insc_user");
		INSCDept d = inscDeptService.getOrgDeptByDeptCode(loginUser.getUserorganization());
		Map<String, Object> map = new HashMap<String, Object>();
		INSBFlowerror query = new INSBFlowerror();
		if(!StringUtil.isEmpty(taskid)){
			query.setTaskid(taskid);
		}
		if(!StringUtil.isEmpty(inscomcode)){
			query.setInscomcode(inscomcode);
		}
		query.setFiroredi("5");
		query.setFlowcode("-1");
		Map<String,Object> queryMap = new HashMap<>();
		queryMap = BeanUtils.toMap(query,para);
		if(!StringUtil.isEmpty(taskcreatetimeup))
			queryMap.put("taskcreatetimeup", taskcreatetimeup);//任务创建时间上限
		if(!StringUtil.isEmpty(taskcreatetimedown))
			queryMap.put("taskcreatetimedown", taskcreatetimedown);//任务创建时间下限树形结构code
		if(!StringUtil.isEmpty(deptcode)) {
			queryMap.put("deptcode", deptcode);//出单网点机构id
		} else {
			if(!StringUtil.isEmpty(d.getDeptinnercode()))
				queryMap.put("deptcode", d.getDeptinnercode());//树形结构code
		}

		map = insbFlowerrorService.initPushtocoreErrorList(queryMap);
		return map;
	}
	/**手动反向关闭
	 * 
	 * @return
	 * @throws ControllerException 
	 */
	@RequestMapping(value = "closstask", method = RequestMethod.GET)
	public ModelAndView closstask() throws ControllerException {
		ModelAndView mav = new ModelAndView("system/closstask");
		return mav;
	}

	@RequestMapping(value = "showclosstaskinfo", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> showclosstaskinfo(@RequestParam(value="taskid",required=false) String taskid,
			@RequestParam(value="inscomcode",required=false) String inscomcode,
			@RequestParam(value="taskcreatetimeup",required=false) String taskcreatetimeup,
			@RequestParam(value="taskcreatetimedown",required=false) String taskcreatetimedown,
			@ModelAttribute PagingParams para) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		INSBPolicyitem query = new INSBPolicyitem();
		if(!StringUtil.isEmpty(taskid)){
			query.setTaskid(taskid);
		}
		if(!StringUtil.isEmpty(inscomcode)){
			query.setInscomcode(inscomcode);
		}
		Map<String,Object> queryMap = new HashMap<>();
		queryMap = BeanUtils.toMap(query,para);
		queryMap.put("taskcreatetimeup", taskcreatetimeup);//任务创建时间上限
		queryMap.put("taskcreatetimedown", taskcreatetimedown);//任务创建时间下限
		map = insbFlowerrorService.initClosstaskList(queryMap);
		return map;
	}
}
