package com.cninsure.system.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import com.cninsure.core.utils.LogUtil;
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
import com.cninsure.core.utils.QueryBean;
import com.cninsure.system.entity.INSCDept;
import com.cninsure.system.entity.INSCRole;
import com.cninsure.system.entity.INSCUser;
import com.cninsure.system.service.INSCRoleMenuService;
import com.cninsure.system.service.INSCRoleService;
import com.cninsure.system.service.INSCUserRoleService;
import com.common.PagingParams;

@Controller
@RequestMapping("/role/*")
public class RoleController extends BaseController {

	@Resource
	private INSCRoleService inscRoleService;

	@Resource
	private INSCRoleMenuService inscRoleMenuService;

	@Resource
	private INSCUserRoleService inscUserRoleService;

	/**
	 * 菜单入口
	 * 
	 * @param session
	 * @param role
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "list", method = RequestMethod.GET)
	public ModelAndView list(HttpSession session, @ModelAttribute INSCRole role)
			throws ControllerException {
		List<INSCRole> roleList = new ArrayList<INSCRole>();
		ModelAndView mav = new ModelAndView("system/rolelist");
		try {
			if (BeanUtils.isNullForObject(role)) {
				roleList = inscRoleService.queryAll();
			} else {
				roleList.add(inscRoleService.queryOne(role));
			}
		} catch (Exception e) {
			System.out.println("BeanUtils.isNullForObject(role)   is  error");
			e.printStackTrace();
		}
		mav.addObject("roleList", roleList);
		mav.addObject("role", role);
		return mav;
	}

	/**
	 * 按条件查
	 * 
	 * @param session
	 * @param role
	 * @param dept
	 * @param querybean
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "showrolelist", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> showrolelist(HttpSession session,
			@ModelAttribute PagingParams para, @ModelAttribute INSCRole role,
			@ModelAttribute INSCDept dept, @ModelAttribute QueryBean querybean)
			throws ControllerException {
		Map<String, Object> map = BeanUtils.toMap(role, dept, querybean, para);
		return inscRoleService.showRoleList(map);
	}

	/**
	 * 删除角色信息
	 * 
	 * @param roldId
	 * @return
	 */
	@RequestMapping(value = "rmoverole", method = RequestMethod.GET)
	public ModelAndView rmoveRoleById(HttpSession session, String id) {
		INSCUser operator = (INSCUser) session.getAttribute("insc_user");
		LogUtil.info("删除角色信息id为%s,操作人:%s", id, operator.getUsercode());
		inscRoleService.deleteRoleById(id);
		ModelAndView mav = new ModelAndView("system/rolelist");
		return mav;
	}

	/**
	 * 转到新增页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "main2add", method = RequestMethod.GET)
	public ModelAndView main2addRole() {
		ModelAndView mav = new ModelAndView("system/roleedit");
		return mav;
	}

	/**
	 * 新增角色
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "addrole", method = RequestMethod.POST)
	public ModelAndView addRole(HttpSession session,
			@ModelAttribute INSCRole model) {
		INSCUser loginUser = (INSCUser) session.getAttribute("insc_user");
		model.setOperator(loginUser.getUsercode().toString());
		model.setCreatetime(new Date());
		inscRoleService.insert(model);
		ModelAndView mav = new ModelAndView("system/rolelist");
		return mav;
	}

	/**
	 * 转到修改页面
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "main2edit", method = RequestMethod.GET)
	public ModelAndView main2editRole(String id) {
		INSCRole result = inscRoleService.queryById(id);
		ModelAndView mav = new ModelAndView("system/roleedit");
		mav.addObject("role", result);
		return mav;
	}

	/**
	 * 新增或修改
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "updaterole", method = RequestMethod.POST)
	public ModelAndView modifyRole(HttpSession session,
			@ModelAttribute INSCRole model) {
		INSCUser loginUser = (INSCUser) session.getAttribute("insc_user");
		model.setOperator(loginUser.getUsercode().toString());
		if ("".equals(model.getId())) {
			model.setCreatetime(new Date());
			inscRoleService.insert(model);
		} else {
			model.setModifytime(new Date());
			inscRoleService.updateById(model);
		}

		ModelAndView mav = new ModelAndView("system/rolelist");
		return mav;
	}

	/**
	 * 初始化角色权限
	 * 
	 * @param roleId
	 * @return
	 */
	@RequestMapping(value = "initroletree", method = RequestMethod.POST)
	@ResponseBody
	public List<Map<String, String>> initRoleTree(String roleId) {
		return inscRoleService.initRoleTree(roleId);
	}

	/**
	 * 为角色绑定菜单
	 * 
	 * @param session
	 * @param roleId
	 * @param menuIds
	 * @return
	 */
	@RequestMapping(value = "bindingrolemenu", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, String> bindingRoleMenu(HttpSession session,
			String roleId, String menuIds) {
		Map<String, String> result = inscRoleMenuService.repairRoleMenu(
				session, roleId, menuIds);
		return result;
	}

	/**
	 * 批量删除角色信息
	 */
	@RequestMapping(value = "benchdeletebyids", method = RequestMethod.GET)
	public Map<String, String> benchDeleteByIds(HttpSession session,
			String roleIds) {
		INSCUser operator = (INSCUser) session.getAttribute("insc_user");
		LogUtil.info("批量删除角色信息ids为%s,操作人:%s", roleIds, operator.getUsercode());
		Map<String, String> result = inscRoleService.deleteRoleByIds(roleIds);
		return result;
	}

	/**
	 * 批量解绑角色用户关系
	 */
	@RequestMapping(value = "benchdeleteusersbyroleid", method = RequestMethod.POST)
	public Map<String, String> benchDeleteUsersbyRoleid(HttpSession session,
			String userIds, String roleId) {
		INSCUser operator = (INSCUser) session.getAttribute("insc_user");
		LogUtil.info("批量解绑角色用户关系ids为%s,roleId:%s,操作人:%s", userIds, roleId, operator.getUsercode());
		Map<String, String> result = inscRoleService.deleteUsersByRoleId(
				userIds, roleId);
		return result;
	}

	/**
	 * 转到展示角色用户信息页面
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "main2show", method = RequestMethod.POST)
	@ResponseBody
	public INSCRole main2showRole(String id) {
		INSCRole result = inscRoleService.queryById(id);
		return result;
	}

	/**
	 * 角色主页面转到用户列表页面
	 * 
	 * @return
	 */
	@RequestMapping(value = "main2userlist", method = RequestMethod.GET)
	public ModelAndView main2UserList(String roleId) {
		ModelAndView mav = new ModelAndView("system/roleuserlist");
		mav.addObject("roleId", roleId);
		return mav;
	}

	/**
	 * 根据角色id得到未绑定所有用户信息
	 * 
	 * @param roleId
	 * @return
	 */
	@RequestMapping(value = "loaduserlist", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> loadUserList(HttpSession session,
			@ModelAttribute PagingParams para,
			@ModelAttribute QueryBean querybean, String usercode, String name,
			String roleId) throws ControllerException {
		Map<String, Object> map = BeanUtils.toMap(querybean, para);
		map.put("usercode", usercode);
		map.put("name", name);
		map.put("roleId", roleId);
		return inscRoleService.findRmainUsersByIds(map);
	}

	/**
	 * 根据当前角色得到所有用户  TODO
	 * 
	 * @param roleId
	 * @return
	 */
	@RequestMapping(value = "getusersbyroleid", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getUsersByRoleId(HttpSession session,
			@ModelAttribute PagingParams para,
			@ModelAttribute QueryBean querybean,
			@RequestParam(required = false) String roleId) {
		Map<String, Object> map = BeanUtils.toMap(querybean, para);
		if(roleId!=null){
			map.put("roleId", roleId);
		}else{
			map.put("roleId", "");
		}
		return inscUserRoleService.getUsersByRoleid(map);
	}

	/**
	 * 为角色批量分配用户
	 * 
	 * @param userIds
	 */
	@RequestMapping(value = "saveroleusers", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> saveRoleUsers(HttpSession session,
			String userIds, String roleId) {
		return inscUserRoleService.saveUsersByRoleId(userIds, roleId);

	}
}
