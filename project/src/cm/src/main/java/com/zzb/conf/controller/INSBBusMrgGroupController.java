package com.zzb.conf.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import com.cninsure.core.exception.ControllerException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cninsure.core.controller.BaseController;
import com.cninsure.core.utils.BeanUtils;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.QueryBean;
import com.cninsure.core.utils.StringUtil;
import com.cninsure.system.entity.INSCDept;
import com.cninsure.system.entity.INSCUser;
import com.cninsure.system.service.INSCCodeService;
import com.cninsure.system.service.INSCDeptService;
import com.common.PagingParams;
import com.zzb.conf.controller.vo.BaseVo;
import com.zzb.conf.entity.INSBBusinessmanagegroup;
import com.zzb.conf.entity.INSBGroupprovide;
import com.zzb.conf.service.INSBAgentService;
import com.zzb.conf.service.INSBBusinessmanagegroupService;
import com.zzb.conf.service.INSBGroupdeptService;
import com.zzb.conf.service.INSBGroupprovideService;

/**
 * 
 * 业管群组管理
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/groupmrg/*")
public class INSBBusMrgGroupController extends BaseController {
	@Resource
	private INSBBusinessmanagegroupService service;

	@Resource
	private INSCDeptService deptService;

	@Resource
	private INSBAgentService agentService;

	@Resource
	private INSCCodeService codeService;
	
	@Resource
	private INSBGroupprovideService insbGroupprovideService;

	@Resource
	private INSBGroupdeptService insbGroupdeptService;
	/**
	 * 转到列表页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "menu2list", method = RequestMethod.GET)
	public ModelAndView menu2listBypage() {
		ModelAndView result = new ModelAndView("zzbconf/groupmrglist");
		return result;
	}

	@RequestMapping(value = "initgrouplist", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> initGroupList(HttpSession session,
			@ModelAttribute PagingParams para,
			@ModelAttribute INSBBusinessmanagegroup bmg,
			@ModelAttribute INSCDept dept, @ModelAttribute QueryBean querybean) {
		
		INSCUser user = (INSCUser) session.getAttribute("insc_user");
		Map<String, Object> map = BeanUtils.toMap(bmg, querybean, para);
		if(user!=null&&user.getDeptinnercode()!=null){
			map.put("userDept", user.getDeptinnercode());
		}else{
			map.put("userDept", null);
		}
		
		return service.queryByParamPage(map);
	}

	/**
	 * 转到新增页面
	 * 
	 * 初始化业管组 所属平台 权限信息
	 * 
	 * @return
	 */
	@RequestMapping(value = "mian2add", method = RequestMethod.GET)
	public ModelAndView mian2add() {
		ModelAndView result = new ModelAndView("zzbconf/groupmrg-detailedit");
		Map<String, Object> tempMap = service.main2add();
		
		result.addObject("comm", tempMap.get("comm"));
		result.addObject("taskType", tempMap.get("taskType"));
		result.addObject("groupType", tempMap.get("groupType"));
		result.addObject("parentGroup", tempMap.get("parentGroup"));
		return result;
	}

	/**
	 * 转到编辑页面
	 * 
	 * @param id
	 *            群组id
	 * @return
	 */
	@RequestMapping(value = "mian2edit", method = RequestMethod.GET)
	public ModelAndView mian2edit(String id) {
		ModelAndView result = new ModelAndView("zzbconf/groupmrg-detailedit");
		Map<String, Object> resultMap = null;
		try {
			resultMap = service.updateGruopData(id);

			result.addObject("comm", resultMap.get("comm"));
			result.addObject("data1", resultMap.get("data1"));
			result.addObject("data2", resultMap.get("data2"));
			result.addObject("data3", resultMap.get("data3"));
			result.addObject("providerNames", resultMap.get("providerNames"));
			result.addObject("deptNames", resultMap.get("deptNames"));
			result.addObject("providerIds", resultMap.get("providerIds"));
			result.addObject("deptIds", resultMap.get("deptIds"));
			result.addObject("deptParentList", resultMap.get("deptParentList"));
			result.addObject("deptList", resultMap.get("deptList"));
			result.addObject("groupOrgData", resultMap.get("groupOrgData"));
			result.addObject("groupType", resultMap.get("groupType"));
			result.addObject("taskType", resultMap.get("taskType"));
			result.addObject("oldtaskType", resultMap.get("oldtaskType"));
			result.addObject("groupDeptOrgName", resultMap.get("groupDeptOrgName"));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 初始化业管组所属机构
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
	 * 转到群组成员列表页面
	 * 
	 * @param groupId
	 * @return
	 */
	@RequestMapping(value = "mian2groupmember", method = RequestMethod.GET)
	public ModelAndView mian2groupMember(String groupId) {
		ModelAndView result = new ModelAndView("zzbconf/groupmrg-member-list");
		Map<String, Object> resultMap = null;
		try {
			resultMap = service.getGroupMenberData(groupId);
			result.addObject("comm", resultMap.get("comm"));
			result.addObject("pcomm", resultMap.get("pcomm"));
			result.addObject("data2", resultMap.get("data2"));
			result.addObject("providerNames", resultMap.get("providerNames"));
			result.addObject("deptNames", resultMap.get("deptNames"));
			result.addObject("providerIds", resultMap.get("providerIds"));
			result.addObject("deptIds", resultMap.get("deptIds"));
			result.addObject("groupDeptOrgName", resultMap.get("groupDeptOrgName"));
			result.addObject("groupDeptOrgParentName",
			resultMap.get("groupDeptOrgParentName"));

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	@RequestMapping(value = "initgroupproviderlist", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> initgroupproviderlist(HttpSession session,
			@ModelAttribute PagingParams para, @RequestParam String groupId,
			@ModelAttribute QueryBean querybean) {

		Map<String, Object> map = BeanUtils.toMap(querybean, para);
		Map<String, Object> result = null;
		try {
			map.put("groupid", groupId);
			result = service.getGroupProviderList(map,groupId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	@RequestMapping(value = "initgroupdeptidlist", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> initgroupdeptidlist(HttpSession session,
			@ModelAttribute PagingParams para, @RequestParam String groupId,
			@ModelAttribute QueryBean querybean) {

		Map<String, Object> map = BeanUtils.toMap(querybean, para);
		Map<String, Object> result = null;
		try {
			map.put("groupid", groupId);
			result = service.getGroupDeptidList(map,groupId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	@RequestMapping(value = "initgroupmemberlist", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> initgroupmemberlist(HttpSession session,
			@ModelAttribute PagingParams para, @RequestParam String groupId,
			@ModelAttribute QueryBean querybean) {

		Map<String, Object> map = BeanUtils.toMap(querybean, para);
		Map<String, Object> result = null;
		try {
			map.put("groupid", groupId);
			result = service.getGroupMemberList(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 通用编辑页面转到详细编辑页面
	 * 
	 * 初始化权限信息，只有也管组有组织机构（并且结构只能到平台）
	 * 
	 * 
	 * 新增页面
	 * 
	 * @param groupName
	 * @param pid
	 * @param groupKind
	 * @return
	 */
	@RequestMapping(value = "common2detail", method = RequestMethod.GET)
	public ModelAndView common2detail(String groupName, String pid,
			String groupKind) {
		Map<String, Object> resultMap = service.common2detail(groupName, pid,
				groupKind);

		ModelAndView result = new ModelAndView(resultMap.get("path").toString());

		result.addObject("comm", resultMap.get("comm"));
		result.addObject("data1", resultMap.get("data1"));
		result.addObject("data2", resultMap.get("data2"));
		result.addObject("data3", resultMap.get("data3"));
		result.addObject("taskType", resultMap.get("taskType"));
		result.addObject("deptParentList", resultMap.get("deptParentList"));
		return result;
	}

	/**
	 * 查询条件 机构树根据父群组过滤机构
	 * 
	 * @return
	 */
	@RequestMapping(value = "initdepttree11", method = RequestMethod.POST)
	@ResponseBody
	public List<Map<String, String>> initDeptTree(@RequestParam(value = "id", required = false) String parentcode) {
		return service.queryTreeByPcode(parentcode);
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
	public List<Map<String, String>> initProviderTree(
			@RequestParam(value = "id", required = false) String parentcode,
			@RequestParam(value = "checked", required = false) String checked,
			String providerIds) {
		return service.getProviderTreeList(parentcode, providerIds, checked);
	}
	
	
	/**
	 * 
	 * 保存群组基础数据
	 * @param session
	 * @param gropData
	 * @return
	 */
	@RequestMapping(value = "savebasegroupdata", method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView savebasegroupdata(HttpSession session,@ModelAttribute INSBBusinessmanagegroup gropData) {
		ModelAndView result = new ModelAndView("zzbconf/groupmrglist");
		INSCUser user = (INSCUser) session.getAttribute("insc_user");
		try {
			service.saveBaseGroupData(user, gropData);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return result;
		}
	}

	/**
	 * 保存群组信息
	 * 
	 * @param pcode
	 *            权限编码
	 * @param deptids
	 *            管理机构ids
	 * @return
	 */
	@RequestMapping(value = "savegroupdata", method = RequestMethod.POST)
	@ResponseBody
	public BaseVo saveGroupData(HttpSession session,
			@ModelAttribute INSBBusinessmanagegroup gropData, String[] pcode,
			String deptids, String providerIds,
			@RequestParam(required = false) String[] startworkdate,
			@RequestParam(required = false) String[] endworkdate,
			@RequestParam(required = false) String[] startworktime,
			@RequestParam(required = false) String[] endworktime) {
		BaseVo bv = new BaseVo();

		// 整理参数
		Map<String, Object> pramMap = new HashMap<String, Object>();
		INSCUser user = (INSCUser) session.getAttribute("insc_user");
		pramMap.put("startworkdate", startworkdate);
		pramMap.put("endworkdate", endworkdate);
		pramMap.put("startworktime", startworktime);
		pramMap.put("endworktime", endworktime);

		try {
			service.saveGroupData(user, gropData, pcode, deptids, providerIds,
					pramMap);
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
	 * 批量删除
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(value = "deletegroupbyid", method = RequestMethod.POST)
	@ResponseBody
	public BaseVo deleteGroupById(HttpSession session, String ids) {
		INSCUser operator = (INSCUser) session.getAttribute("insc_user");
		LogUtil.info("group批量删除ids为%s,操作人:%s", ids, operator.getUsercode());
		BaseVo bv = new BaseVo();
		try {
			service.deleteGroupBath(ids);
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
	 * 得到当前群组管理平台id 根据平台查询需要添加成员信息
	 * 
	 * 当前群组添加成员
	 * 
	 * @return
	 */
	@RequestMapping(value = "group2memberadd", method = RequestMethod.GET)
	public ModelAndView group2memberAdd(String gropId) {
		ModelAndView result = new ModelAndView("zzbconf/groupmrg-user-list");
		INSBBusinessmanagegroup bmg = service.queryById(gropId);
		if (bmg == null) throw new ControllerException("找不到业管组");
		result.addObject("groupId", gropId);
		return result;
	}

	/**
	 * 
	 * 根据当前的管理机构选择成员（未有当前群组id的用户）
	 * 
	 * @return
	 */
	@RequestMapping(value = "initgroupuserlist", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> initGroupUserList(HttpSession session,
			@ModelAttribute PagingParams para,
			@RequestParam(required = false) String groupId,
			@ModelAttribute QueryBean querybean, String usercode, String name, String deptid) {

		Map<String, Object> map = BeanUtils.toMap(groupId, querybean, para);
		return service.queryUsetListByGroupId(groupId, map, usercode, name,
				deptid);
	}
	
	/**
	 * TODO 根据当前平台查询供应商
	 * @param deptId 
	 * @return
	 */
	@RequestMapping(value = "initgroupprovider", method = RequestMethod.POST)
	@ResponseBody
	public List<Map<String, Object>> initGroupProvider(@RequestParam(required = false) String organizationid,@RequestParam(required = true)String id,String checked){
		return service.queryGroupProviderByDeptId(organizationid,id,checked);
	}

	/**
	 * 为群组添加成员
	 * 
	 * @param userIds
	 * @param groupId
	 * @return
	 */
	@RequestMapping(value = "savegroupusers", method = RequestMethod.POST)
	@ResponseBody
	public BaseVo saveGroupUsers(String userIds, String groupId) {
		BaseVo bv = new BaseVo();
		try {
			service.saveGroupUsers(userIds, groupId);
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
	 * 批量解除群组成员
	 * 
	 * @param groupId
	 * @param userIds
	 * @return
	 */
	@RequestMapping(value = "deletegroupmember", method = RequestMethod.POST)
	@ResponseBody
	public BaseVo deleteGroupMember(HttpSession session, String ids, String groupId) {
		INSCUser operator = (INSCUser) session.getAttribute("insc_user");
		LogUtil.info("批量解除群组成员ids为%s,操作人:%s", ids, operator.getUsercode());
		BaseVo bv = new BaseVo();
		try {
			service.removeGroupMember(ids, groupId);
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
	 * 批量解除群组供应商
	 * 
	 * @param groupId
	 * @param Ids
	 * @return
	 */
	@RequestMapping(value = "deletegroupprovide", method = RequestMethod.POST)
	@ResponseBody
	public BaseVo deleteGroupProvide(HttpSession session, String ids, String groupid) {
		INSCUser operator = (INSCUser) session.getAttribute("insc_user");
		LogUtil.info("批量解除群组供应商ids为%s,操作人:%s", ids, operator.getUsercode());
		BaseVo bv = new BaseVo();
		try {
			service.removeGroupProvide(ids, groupid);
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
	 * 批量解除群组网点
	 * 
	 * @param groupId
	 * @param Ids
	 * @return
	 */
	@RequestMapping(value = "deletegroupdept", method = RequestMethod.POST)
	@ResponseBody
	public BaseVo deleteGroupDept(HttpSession session, String ids, String groupid) {
		INSCUser operator = (INSCUser) session.getAttribute("insc_user");
		LogUtil.info("批量解除群组网点ids为%s,操作人:%s", ids, operator.getUsercode());
		BaseVo bv = new BaseVo();
		try {
			service.removeGroupDept(ids, groupid);
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
	 * 转到修改群组成员信息页面
	 * 
	 * @param groupId
	 * @return
	 */
	@RequestMapping(value = "group2privilegeupdate", method = RequestMethod.GET)
	public ModelAndView group2memberPrivilegeupdate(String groupId,
			String userId) {
		ModelAndView result = new ModelAndView(
				"zzbconf/groupmrg-memberprivilege-edit");
		
		Map<String,Object> tempData = service.queryGroupPrivilegeByGroupId(userId,groupId);
		result.addObject("user", tempData.get("user"));
		result.addObject("privalege", tempData.get("privalege"));
		result.addObject("groupId", groupId);

		return result;
	}

	/**
	 * 修改群组成员权限
	 * 
	 * @param id
	 * @param groupprivilege
	 * @return
	 */
	@RequestMapping(value = "updatgroupmemberprivilege", method = RequestMethod.POST)
	public ModelAndView updatGroupMemberPrivilege(String id, String groupId,
			String groupprivilege) {
		if(groupprivilege==null||StringUtil.isEmpty(groupprivilege)){
			groupprivilege = "1";
		}
		try {
			service.updatGroupMemberPrivilege(id, groupprivilege);
		} catch (Exception e) {
			e.printStackTrace();
		}
		ModelAndView result=this.mian2edit(groupId);
		return result;
//		ModelAndView result = new ModelAndView("zzbconf/groupmrg-member-list");
//		Map<String, Object> resultMap = null;
//		try {
//			resultMap = service.getGroupMenberData(groupId);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		result.addObject("comm", resultMap.get("comm"));
//		result.addObject("pcomm", resultMap.get("pcomm"));
//		result.addObject("data2", resultMap.get("data2"));
//		result.addObject("providerNames", resultMap.get("providerNames"));
//		result.addObject("deptNames", resultMap.get("deptNames"));
//		result.addObject("providerIds", resultMap.get("providerIds"));
//		result.addObject("deptIds", resultMap.get("deptIds"));
//		return result;
	}
	/**
	 * 群组添加供应商
	 * @param groupid
	 * @param provideid
	 * @return
	 */
	@RequestMapping(value = "savegroupprovidedata", method = RequestMethod.POST)
	@ResponseBody
	public BaseVo saveGroupProvidedata(HttpSession session,String groupid, String provideids) {
		int flag = 0;
		int i =0;
		BaseVo vo=new BaseVo();
		if(provideids==null||"".equals(provideids)){
			vo.setStatus("0");
			vo.setMessage("没有选中供应商！");
			return vo;
		};
		INSCUser user = (INSCUser) session.getAttribute("insc_user");
		String[] prvcode = provideids.split(",");
		try {
			for(i =0;i<prvcode.length;i++){
				INSBGroupprovide insbGroupprovide=new INSBGroupprovide();
				insbGroupprovide.setProvideid(prvcode[i]);
				insbGroupprovide.setGroupid(groupid);
				List<INSBGroupprovide> list=insbGroupprovideService.queryList(insbGroupprovide);
				//如果已存在该记录则跳过该记录的保存
				if (list.size()!=0) {
					flag+=1;
					continue;
				}
				insbGroupprovide.setOperator(user.getUsername());
				insbGroupprovide.setCreatetime(new Date());
				insbGroupprovideService.insert(insbGroupprovide);
				LogUtil.info("群组管理新增供应商"+insbGroupprovide);
			}
			if(flag!=0&&flag==i){
				vo.setStatus("0");
				vo.setMessage("保存"+i+"家, 有"+flag+"家供应商已关联, 操作失败");
			}else if(flag!=0&&flag!=i){
				vo.setStatus("1");
				vo.setMessage("保存"+i+"家, 有"+flag+"家供应商已关联, 有"+(i-flag)+"家操作成功");
			}else{
				vo.setStatus("1");
				vo.setMessage("操作成功！");
			}
			return vo;
		} catch (Exception e) {
			e.printStackTrace();
			vo.setStatus("0");
			vo.setMessage("操作失败！");
			return vo;
		}
	}
	/**
	 * 群组添加网点
	 * @param groupid
	 * @param provideid
	 * @return
	 */
	@RequestMapping(value = "savegroupwangdata", method = RequestMethod.POST)
	@ResponseBody
	public BaseVo saveGroupWangdata(HttpSession session,String groupid, String deptid,String grade) {
		BaseVo vo=new BaseVo();
		INSCUser user = (INSCUser) session.getAttribute("insc_user");
		
		try {
			insbGroupdeptService.saveGroupWangdata(user, groupid, deptid, grade);
			vo.setStatus("1");
			vo.setMessage("操作成功！");
		} catch (Exception e) {
			e.printStackTrace();
			vo.setStatus("0");
			vo.setMessage("操作失败！");
		}
		return vo;
	}
	/**
	 * 查询条件 机构树（异步）
	 * 
	 * @return
	 */
	@RequestMapping(value = "initmemberdepttree", method = RequestMethod.POST)
	@ResponseBody
	public List<Map<String, String>> initMemberDeptTree(@RequestParam(value = "id", required = false) String parentcode) {
		return deptService.queryTreeList(parentcode);
	}
}
