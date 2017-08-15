package com.zzb.conf.controller;

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
import com.cninsure.system.service.INSCDeptService;
import com.common.PagingParams;
import com.zzb.conf.controller.vo.BaseVo;
import com.zzb.conf.service.INSBRulebseService;
import com.zzb.conf.service.INSBTasksetService;

/**
 * 
 * 任务规则管理
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/rulebase/*")
public class INSBRuleBaseController extends BaseController {

	@Resource
	private INSBRulebseService service;

	@Resource
	private INSCDeptService deptService;

	@Resource
	private INSBTasksetService tasksetService;

	/**
	 * 转到列表页面
	 * 
	 * 初始化机构数据
	 * 
	 * @return
	 */
	@RequestMapping(value = "menu2list", method = RequestMethod.GET)
	public ModelAndView menu2listBypage() {
		ModelAndView result = new ModelAndView("zzbconf/rulebaselist");
		Map<String, Object> tempResult = service.getParentDeptData();
		result.addObject("deptParentList", tempResult.get("deptParentList"));
		return result;
	}

	/**
	 * 初始化任务组所属机构
	 * 
	 * @param deptCode
	 * @return
	 */
	@RequestMapping(value = "main2addinitdept", method = RequestMethod.POST)
	@ResponseBody
	public List<Map<String, String>> main2AddInitDept(String deptCode) {
		return deptService.queryListByPcode4Group(deptCode);
	}

	/**
	 * 初始化任务列表
	 * 
	 * @param session
	 * @param para
	 * @param ruletype 调度规则/权重规则
	 * @param param2     规则名称/规则描述
	 * @param param3 value
	 * @param rulebaseStatus 规则状态 1:已关联 。2：未关联
	 * @param deptId 组织机构
	 * @param dept
	 * @param querybean
	 * @return
	 */
	@RequestMapping(value = "initrulebaselist", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> initRuleBaseList(
			HttpSession session,
			@ModelAttribute PagingParams para,
			@RequestParam(required = false, defaultValue = "0") int ruletype,
			@RequestParam(required = false, defaultValue = "0") int param2,
			@RequestParam(required = false, defaultValue = "") String param3,
			@RequestParam(required = false, defaultValue = "0") int rulebaseStatus,
			@RequestParam(required = false, defaultValue = "") String deptId,
			@ModelAttribute INSCDept dept, @ModelAttribute QueryBean querybean) {

		Map<String, Object> map = BeanUtils.toMap(dept, querybean, para);
		
		//查询条件
		Map<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("ruletype", ruletype);
		tempMap.put("param2", param2);
		tempMap.put("param3", param3);
		tempMap.put("rulebaseStatus", rulebaseStatus);
		tempMap.put("deptId", deptId);
		
		//规则模块查询方式选择 2
		return service.getListPageByParam(2, tempMap, map);
	}

	/**
	 * 批量修改任务组状态
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(value = "changestatus", method = RequestMethod.POST)
	@ResponseBody
	public BaseVo changeStatus(String ids) {
		BaseVo bv = new BaseVo();
		try {
			// service.updateStatusByIds(ids);
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
	 * 转到任务组列表页面
	 * 
	 * 初始化选中任务组
	 * 
	 * @return
	 */
	@RequestMapping(value = "mian2tasksetlist", method = RequestMethod.GET)
	public ModelAndView mian2tasksetList(
			@RequestParam(required = false) String id) {
		
		ModelAndView result = new ModelAndView("zzbconf/rulebasetasksetlist");
		
		Map<String, Object> tempMap = null;
		try {
			tempMap = service.initTasksetId(id);
			result.addObject("tasksetIds", tempMap.get("tasksetIds"));
		} catch (Exception e) {
			result.addObject("tasksetIds", "");
			e.printStackTrace();
		}
		result.addObject("id", id);
		
		
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
	public Map<String, Object> initTsakSetList(HttpSession session,
			@ModelAttribute PagingParams para,
			@RequestParam(required = false) String tasksetname,
			@RequestParam(required = false) String setstatus,
			@ModelAttribute INSCDept dept, @ModelAttribute QueryBean querybean) {
		Map<String, Object> map = BeanUtils.toMap(dept, querybean, para);
		
		
		return tasksetService
				.queryByParamPage("0", tasksetname, setstatus, map);
	}

	/**
	 * 
	 * 保存规则任务组信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "savetaskset2rule", method = RequestMethod.POST)
	@ResponseBody
	public BaseVo saveTaskset2rule(String ruleId, String tasksetId) {
		BaseVo bv = new BaseVo();
		return bv;

	}
}
