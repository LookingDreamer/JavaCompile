package com.zzb.conf.controller;

import java.util.ArrayList;
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

import com.cninsure.core.controller.BaseController;
import com.cninsure.core.utils.BeanUtils;
import com.cninsure.core.utils.QueryBean;
import com.cninsure.system.entity.INSCDept;
import com.cninsure.system.entity.INSCUser;
import com.cninsure.system.service.INSCCodeService;
import com.cninsure.system.service.INSCDeptService;
import com.common.PagingParams;
import com.zzb.conf.controller.vo.BaseVo;
import com.zzb.conf.entity.INSBBusinessmanagegroup;
import com.zzb.conf.entity.INSBTaskset;
import com.zzb.conf.service.INSBBusinessmanagegroupService;
import com.zzb.conf.service.INSBRulebseService;
import com.zzb.conf.service.INSBTasksetService;
import com.zzb.conf.service.INSBTasksetscopeService;

/**
 * 
 * 任务组管理
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/taskset/*")
public class INSBTaskSetController extends BaseController {
	@Resource
	private INSBTasksetService service;
	@Resource
	private INSBTasksetscopeService taskesetscopeService;
	@Resource
	private INSCDeptService deptService;
	@Resource
	private INSCCodeService codeService;
	@Resource
	private INSBBusinessmanagegroupService businessmanagegroupService;
	@Resource
	private INSBRulebseService rulebseService;

	/**
	 * 转到列表页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "menu2list", method = RequestMethod.GET)
	public ModelAndView menu2listBypage() {
		ModelAndView result = new ModelAndView("zzbconf/tasksetlist");
		return result;
	}

	/**
	 * @param session
	 * @param para
	 * @param tasksetkind
	 *            类别
	 * @param tasksetname
	 *            类别内容
	 * @param setstatus
	 *            权限是否生效
	 * @param dept
	 * @param querybean
	 * @return
	 */
	@RequestMapping(value = "inittasksetlist", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> initTsakSetList(
			HttpSession session,
			@ModelAttribute PagingParams para,
			@RequestParam(required = false, defaultValue = "0") String tasksetkind,
			@RequestParam(required = false) String tasksetname,
			@RequestParam(required = false) String setstatus,
			@ModelAttribute INSCDept dept, @ModelAttribute QueryBean querybean) {
		Map<String, Object> map = BeanUtils.toMap(dept, querybean, para);
		return service.queryByParamPage(tasksetkind, tasksetname, setstatus,
				map);
	}

	/**
	 * 转到新增页面
	 * 
	 * 初始化组织机构下拉框
	 * 
	 * @return
	 */
	@RequestMapping(value = "mian2add", method = RequestMethod.GET)
	public ModelAndView mian2add() {
		ModelAndView result = new ModelAndView("zzbconf/tasksetedit");
		try {
			Map<String,Object> tempMap =  service.initMian2addData();
			result.addObject("deptParentList", tempMap.get("deptParentList"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * 初始化任务组所属机构
	 * 
	 * @param deptCode
	 * @return
	 */
	@RequestMapping(value="main2addinitdept",method=RequestMethod.POST)
	@ResponseBody
	public List<Map<String, String>> main2AddInitDept(String deptCode){
		return deptService.queryListByPcode4Group(deptCode);
	}

	/**
	 * 新增任务组
	 * 
	 * @param set
	 * @return
	 */
	@RequestMapping(value = "saveorupdatetasksetdata", method = RequestMethod.POST)
	public ModelAndView saveOrUpdateTaskSetData(HttpSession session,@ModelAttribute INSBTaskset set) {
		ModelAndView result = new ModelAndView("zzbconf/tasksetlist");
		INSCUser user = (INSCUser) session.getAttribute("insc_user");
		service.saveOrUpdate(user,set);
		return result;
	}

	/**
	 * 转到修改页面
	 * 
	 * 初始化数据
	 * 
	 * @return
	 */
	@RequestMapping(value = "main2edit", method = RequestMethod.GET)
	public ModelAndView main2edit(String id) {
		ModelAndView result = new ModelAndView("zzbconf/tasksetedit");
		try {
			Map<String,Object> tempResult = service.initMain2editData(id);
			result.addObject("setModel", tempResult.get("setModel"));
			result.addObject("deptParentList", tempResult.get("deptParentList"));
			result.addObject("deptList", tempResult.get("deptList"));
			result.addObject("deptModel", tempResult.get("deptModel"));
			result.addObject("taskType", tempResult.get("taskType"));
			result.addObject("oldtaskType", tempResult.get("oldtaskType"));
			result.addObject("providerName", tempResult.get("providerName"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@RequestMapping(value = "deletebyids", method = RequestMethod.POST)
	@ResponseBody
	public BaseVo deleteByIds(String ids) {
		BaseVo bv = new BaseVo();
		try {
			service.deleteByIds(ids);
			bv.setStatus("1");
			bv.setMessage("操作成功！");
		} catch (Exception e) {
			bv.setStatus("2");
			bv.setMessage("操作失败！");
			e.printStackTrace();
		}
		return bv;
	}
	
	/**
	 * 批量修改任务组状态
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(value="changestatus",method=RequestMethod.POST)
	@ResponseBody
	public BaseVo changeStatus(String ids){
		BaseVo bv = new BaseVo();
		try {
			service.updateStatusByIds(ids);
			bv.setStatus("1");
			bv.setMessage("操作成功！");
		} catch (Exception e) {
			bv.setStatus("2");
			bv.setMessage("操作失败！");
			e.printStackTrace();
		}
		return bv;
	}

	/**
	 * 转到群组列表页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "mian2grouplist", method = RequestMethod.GET)
	public ModelAndView main2groupList(String id) {
		ModelAndView result = new ModelAndView("zzbconf/tasksetgrouplist");
		Map<String,Object> tempData  = service.queryData2groupList(id);
		result.addObject("model", tempData.get("model"));
		result.addObject("groupIds", tempData.get("groupIds"));
		return result;
	}

	/**
	 * 转到规则列表
	 * 
	 * @param id 任务组id 初始化选中数据
	 * @return
	 */
	@RequestMapping(value="mian2rulelist", method=RequestMethod.GET)
	public ModelAndView mian2ruleList(String id){
		ModelAndView result = new ModelAndView("zzbconf/tasksetrulebaselist");
		Map<String,Object> tempData  = service.queryData2ruleList(id);
		result.addObject("model", tempData.get("model"));
		result.addObject("rulebaseIds", tempData.get("rulebaseIds"));
		return result;
	}
	
	/**
	 * 初始化规则列表
	 * 
	 * @param session
	 * @param para
	 * @param ruleName 规则名称
	 * @param rulePostil 规则描述
	 * @param bmg
	 * @param dept
	 * @param querybean
	 * @return
	 */
	@RequestMapping(value = "inittasksetrulelist", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> initTaskSetRuleList(HttpSession session,
			@ModelAttribute PagingParams para,
			@RequestParam(required=false) String ruleName,
			@RequestParam(required=false) String rulePostil,
			@ModelAttribute INSBBusinessmanagegroup bmg,
			@ModelAttribute INSCDept dept, @ModelAttribute QueryBean querybean) {
		Map<String, Object> map = BeanUtils.toMap(bmg, dept, querybean, para);
		Map<String, Object> temMap = new HashMap<String,Object>();
		temMap.put("rulePostil", rulePostil);
		temMap.put("ruleName", ruleName);
		Map<String, Object> result = rulebseService.getListPageByParam(1,temMap,map);
		return result;
	}
	
	/**
	 * 任务组绑定规则
	 * 
	 * @param ruleIds
	 * @param tasksetid
	 * @return
	 */
	@RequestMapping(value = "saverulebase2taskset",method=RequestMethod.POST)
	@ResponseBody
	public BaseVo saveRulebBase2taskset(HttpSession session, String ruleIds,String tasksetid) {
		BaseVo bv = new BaseVo();
		INSCUser user = (INSCUser) session.getAttribute("insc_user");
		try {
			service.saveTasksetRuleBase(user,ruleIds,tasksetid);
			bv.setStatus("1");
			bv.setMessage("操作成功！");
		} catch (Exception e) {
			bv.setStatus("2");
			bv.setMessage("操作失败！");
			e.printStackTrace();
		}
		return bv;

	}
	
	/**
	 * 初始化业管组信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "inittasksetgrouplist", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> initTaskSetGroupList(HttpSession session,
			@ModelAttribute PagingParams para,@RequestParam(required=false) String groupname,
			@ModelAttribute INSBBusinessmanagegroup bmg,
			@ModelAttribute INSCDept dept, @ModelAttribute QueryBean querybean) {
		Map<String, Object> map = BeanUtils.toMap(bmg, dept, querybean, para);
		map.put("groupkind", "4000000");
		map.put("groupname", groupname);
		Map<String, Object> result = businessmanagegroupService
				.queryByParamPage(map);
		return result;
	}

	/**
	 * 保存任务组 也管组关系
	 * 
	 * @param groupIds
	 * @param tasksetid
	 * @return
	 */
	@RequestMapping(value = "savegroups2taskset")
	@ResponseBody
	public BaseVo savegroups2taskset(HttpSession session,String groupIds,String tasksetid) {
		BaseVo bv = new BaseVo();
		INSCUser user = (INSCUser) session.getAttribute("insc_user");
		try {
			service.saveTasksetGroup(user,groupIds,tasksetid);
			bv.setStatus("1");
			bv.setMessage("操作成功！");
		} catch (Exception e) {
			bv.setStatus("2");
			bv.setMessage("操作失败！");
			e.printStackTrace();
		}
		return bv;
	}
	/**
	 * 初始化供应商树
	 * 
	 * @param parentcode
	 * @param checked
	 * @return
	 */
	@RequestMapping(value = "initprovidertree", method = RequestMethod.POST)
	@ResponseBody
	public List<Map<String, String>> initProviderTree(String taskSetId,
			@RequestParam(value="id",required = false) String parentcode,
			@RequestParam( required = false) String checked,
			@RequestParam(required=false) String providerId) {
		return service.getProviderTreeList(taskSetId,parentcode, providerId, checked);
	}
	
	/**
	 * 
	 * 编辑页面网点选择联动
	 * @param pid
	 * @return
	 */
	@RequestMapping(value="getdeptbypid",method=RequestMethod.POST)
	@ResponseBody
	public List<Map<String,String>> getdeptbypid(String parentId){
		List<Map<String,String>> result = new ArrayList<Map<String,String>>();
		try {
			result = service.getdeptbypid(parentId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 任务组关联网点
	 * 
	 * @param session
	 * @param tasksetId
	 * @param deptId
	 * @return
	 */
	@RequestMapping(value="savetasksetscop",method=RequestMethod.POST)
	@ResponseBody
	public BaseVo saveTasksetDeptId(HttpSession session,String tasksetId,String deptId){
		BaseVo bv = new BaseVo();
		
		INSCUser user = (INSCUser) session.getAttribute("insc_user");
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("deptid", deptId);
		map.put("taksetid", tasksetId);
		int count = taskesetscopeService.selectScopListCountByDeptid(map);
		if(count == 0){
			try {				
				service.saveTaskSetScop(user, tasksetId, deptId);
				bv.setStatus("1");
				bv.setMessage("操作成功！");
			} catch (Exception e) {
				e.printStackTrace();
				bv.setStatus("2");
				bv.setMessage("操作失败！");
			}
		}else{
			//bv.setStatus("2");
			bv.setMessage("该网点已关联，不可重复关联");
		}		
		return bv;
	}
	
	
	/**
	 * 编辑页面初始化任务组 区域下拉列表
	 * 
	 * @param tasksetId
	 * @return
	 */
	@RequestMapping(value="inittasksetscop",method=RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> initTasksetScop(@ModelAttribute PagingParams para,String tasksetId){
		
		Map<String, Object> map = BeanUtils.toMap(para);
		Map<String, Object> result = null;
		try {
			result = service.getScopListByTaskSetId( map,tasksetId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	@RequestMapping(value="removetetasksetscop",method=RequestMethod.POST)
	@ResponseBody
	public BaseVo removeTetasksetScop(String deptIds){
		BaseVo bv = new BaseVo();
		
		try {
			service.removeTaskSetScop(deptIds);
			bv.setStatus("1");
			bv.setMessage("操作成功！");
		} catch (Exception e) {
			e.printStackTrace();
			bv.setStatus("2");
			bv.setMessage("操作失败！");
		}
		return bv;
	}
	 
	
}
