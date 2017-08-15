package com.zzb.extra.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.cninsure.core.controller.BaseController;
import com.cninsure.core.utils.BeanUtils;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.UUIDUtils;
import com.cninsure.system.entity.INSCUser;
import com.cninsure.system.service.INSCCodeService;
import com.common.PagingParams;
import com.zzb.conf.service.INSBAgentService;
import com.zzb.extra.dao.INSBMiniPermissionDao;
import com.zzb.extra.dao.INSBMiniRoleDao;
import com.zzb.extra.dao.INSBMiniUserRoleDao;
import com.zzb.extra.dao.INSBRolePermissionDao;
import com.zzb.extra.entity.INSBAgentUser;
import com.zzb.extra.entity.INSBMiniPermission;
import com.zzb.extra.entity.INSBMiniRole;
import com.zzb.extra.entity.INSBMiniUserRole;
import com.zzb.extra.entity.INSBRolePermission;
import com.zzb.extra.model.INSBAgentUserQueryModel;
import com.zzb.extra.model.RolePermissionModel;
import com.zzb.extra.service.INSBAgentUserService;
import com.zzb.extra.service.INSBMiniUserRoleService;
import com.zzb.extra.util.ParamUtils;

@Controller
@RequestMapping("/miniRolePer/*")
public class INSBMiniRoleController extends BaseController {

	@Resource
	private INSBMiniUserRoleService insbMiniUserRoleService;
	@Resource
    private INSBAgentUserService insbAgentUserService;
	@Resource
	private INSBMiniRoleDao insbMiniRoleDao;
	
	@Resource
	private INSCCodeService codeService;
	
	@Resource
	private INSBAgentService insbAgentService;
	
	@Resource
	private INSBMiniUserRoleDao insbMiniUserRoleDao;
	
	@Resource
	private INSBMiniPermissionDao insbMiniPermissionDao;
	
	@Resource
	private INSBRolePermissionDao insbRolePermissionDao;
	
	/**
	 * 返回视图
	 * @return
	 */
	@RequestMapping(value = "/userRolePermissionList", method = RequestMethod.GET)
	public ModelAndView confUserRolePermission() {
		ModelAndView mav = new ModelAndView("extra/userRolePerission");
		
		return mav;
	}
	/**
	 * 查询用户角色以及权限
	 * @param session
	 * @param agent
	 * @return
	 */
	@RequestMapping(value = "/selectUserAttr", method = RequestMethod.POST)
	@ResponseBody
	public String selectUserAttr(HttpSession session, @ModelAttribute INSBAgentUser agent) {
		String result = "";
		try {
			result = insbMiniUserRoleService.selectUserAttr(agent.getOpenid());
			return result;
		} catch (Exception e) {
			// TODO: handle exception
			LogUtil.info("权限管理错误信息"+e.getMessage());
			return ParamUtils.resultMap(false, "系统错误");
		}
	}
	/**
	 * 初始化数据的角色权限
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/initRolePermission", method = RequestMethod.GET)
	@ResponseBody
	public String initRolePermission(HttpSession session) {
		INSBMiniUserRole insbMiniUserRole = new INSBMiniUserRole();
		try {
			insbMiniUserRoleService.insert(insbMiniUserRole);
			
		} catch (Exception e) {
			// TODO: handle exception
			LogUtil.info("权限管理错误信息"+e.getMessage());
			return ParamUtils.resultMap(false, "系统错误");
		}
		return ParamUtils.resultMap(true, "操作成功");
	}
	/**
	 * 修改用户权限
	 * @param session
	 * @param agent
	 * @return
	 */
	@RequestMapping(value = "/updateUserRole", method = RequestMethod.GET)
	@ResponseBody
	public String updateUserRole(HttpSession session, @ModelAttribute INSBAgentUser agent) {
		try {
			Map<String, Object> map = BeanUtils.toMap(agent);
			String operator = ((INSCUser) session.getAttribute("insc_user")).getUsercode();
			INSBAgentUser agentUser = insbMiniUserRoleDao.selectByOpenid(map);
			INSBMiniUserRole insbMiniUserRole = insbMiniUserRoleDao.queryObjectByUserid(agentUser.getId());
			
			if(agentUser.getReferrerid() != null || !agentUser.getReferrerid().equals("")) {
				insbMiniUserRole.setOperator(operator);
				insbMiniUserRole.setRoleid("fd5befe0a94fd4cfaea89edee88024da");
				insbMiniUserRole.setModifytime(new Date());
			
				insbMiniUserRoleDao.updateObject(insbMiniUserRole);
			}else if(agentUser.getReferrerid() == null){
				Map<String, Object> param = new HashMap<>();
				param.put("referrerid", agent.getId());
				List<INSBAgentUser> tmpls = insbMiniUserRoleDao.selectAgentUser(param);
				if(tmpls.isEmpty() || tmpls.size()==0) {
					insbMiniUserRole.setRoleid("9c979c2acf80ccb6ebde0ae0907f77e6");
					insbMiniUserRole.setModifytime(new Date());
					insbMiniUserRole.setOperator(operator);
					insbMiniUserRoleDao.updateObject(insbMiniUserRole);
				}else {
					insbMiniUserRole.setRoleid("8f0b994200c987aebfe38fdfc313d660");
					insbMiniUserRole.setOperator(operator);
					insbMiniUserRole.setModifytime(new Date());
					insbMiniUserRoleDao.updateObject(insbMiniUserRole);
				}
			}
			
		} catch (Exception e) {
			LogUtil.info("权限管理错误信息"+e.getMessage());
			return ParamUtils.resultMap(false, "系统错误");
		}
		return ParamUtils.resultMap(true, "操作成功");
	}
	/**
	 * 修改权限呵呵
	 * @param session
	 * @param role
	 * @return
	 */
	@RequestMapping(value = "/updateRole", method = RequestMethod.GET)
	@ResponseBody
	public String updateRole(HttpSession session, @ModelAttribute INSBMiniRole role) {
		try {
			
				Map<String, Object> map = new HashMap<>();
				map.put("id", role.getId());
				INSBMiniRole insbMiniRole = insbMiniRoleDao.queryRoleByRolecode(map);
				if(insbMiniRole != null) {
					String operator = ((INSCUser) session.getAttribute("insc_user")).getUsercode();
					role.setModifytime(new Date());
					role.setOperator(operator);
					insbMiniRoleDao.updateObject(role);
				}
		} catch (Exception e) {
			LogUtil.info("权限管理错误信息"+e.getMessage());
			return "fail";
		}
		return "success";
	}
	/**
	 * 添加用户权限
	 * @param session
	 * @param role
	 * @return
	 */
	@RequestMapping(value = "/addUserRole", method = RequestMethod.GET)
	@ResponseBody
	public String addUserRole(HttpSession session, @ModelAttribute INSBMiniRole role) {
		try {
			role.setId(UUIDUtils.random());
			role.setCreatetime(new Date());
			role.setOperator(((INSCUser) session.getAttribute("insc_user")).getUsercode());
			insbMiniRoleDao.saveObject(role);
		} catch (Exception e) {
			LogUtil.info("权限管理错误信息"+e.getMessage());
			return "fail";
		}
		
		return "success";
	}
	/**
	 * 增加用户权限
	 * @param session
	 * @param permission
	 * @return
	 */
	@RequestMapping(value = "/addUserPermission", method = RequestMethod.GET)
	@ResponseBody
	public String addUserPermission(HttpSession session, @ModelAttribute INSBMiniPermission permission) {
		try {
			insbMiniPermissionDao.saveObject(permission);
			
		} catch (Exception e) {
			LogUtil.info("权限管理错误信息"+e.getMessage());
			return ParamUtils.resultMap(false, "系统错误");
		}
		return ParamUtils.resultMap(true, "操作成功");
	}
	/**
	 * 为某个角色添加权限
	 * @param session
	 * @param permission
	 * @return
	 */
	@RequestMapping(value = "/addRole2Permission", method = RequestMethod.GET)
	@ResponseBody
	public String addRolePermission(HttpSession session, String roleJosn, String permissionJosn) {
		INSBMiniRole role = null;
		INSBRolePermission rp = new INSBRolePermission();
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<INSBMiniRole> roles = JSONArray.parseArray(roleJosn, INSBMiniRole.class);
			List<INSBMiniPermission> permissions = JSONArray.parseArray(permissionJosn, INSBMiniPermission.class);
			for(INSBMiniRole roletmp : roles) {
				role = roletmp;
			}
			for(INSBMiniPermission per : permissions) {
				map.put("roleid", role.getId());
				map.put("permissionid", per.getId());
				List<INSBRolePermission> rptmp = insbRolePermissionDao.queryRolePerByPerid(map);
				
				if(rptmp != null && !rptmp.isEmpty()) continue;
				rp.setId(UUIDUtils.random());
				rp.setCreatetime(new Date());
				rp.setPermissionid(per.getId());
				rp.setRoleid(role.getId());
				rp.setOperator(((INSCUser)session.getAttribute("insc_user")).getUsercode());
				insbRolePermissionDao.saveObject(rp);
			}
			
		} catch (Exception e) {
			LogUtil.info("权限管理错误信息"+e.getMessage());
			return "fail";
		}
		return "success";
	}
	/**
	 * 推荐人添加
	 * @param miniAgentJson
	 * @param miniAgentedJson
	 * @return
	 */
	@RequestMapping(value = "/updateRefer", method = RequestMethod.GET)
    @ResponseBody
    public String updateRefer(HttpSession session, String miniAgentJson, String miniAgentedJson) {
		INSBAgentUser referrer = null;
		INSBAgentUser agentedUser = null;
		String referrerid = "";
    	try {
    		List<INSBAgentUserQueryModel> agents1 = JSONArray.parseArray(miniAgentJson, INSBAgentUserQueryModel.class);
    		List<INSBAgentUserQueryModel> agents2 = JSONArray.parseArray(miniAgentedJson, INSBAgentUserQueryModel.class);
    		for(INSBAgentUserQueryModel model : agents2) {
    			if(model.getRolecode() != null && model.getRolecode().equals("01") && model.getReferrerid() == null) {
    				return "fail";
    			}
    			Map<String, Object> map = BeanUtils.toMap(model);
    			agentedUser = insbMiniUserRoleDao.selectByOpenid(map);
    		}
    		
    		for(INSBAgentUserQueryModel model : agents1) {
    			Map<String, Object> map = BeanUtils.toMap(model);
    			referrer = insbMiniUserRoleDao.selectByOpenid(map);
    			referrerid = referrer.getId();
    		}
    		agentedUser.setReferrerid(referrerid);
    		agentedUser.setOperator(((INSCUser)session.getAttribute("insc_user")).getUsercode());
    		agentedUser.setModifytime(new Date());
    		insbMiniRoleDao.updateAgentReferrer(agentedUser);
    		INSBMiniUserRole insbMiniUserRole = insbMiniUserRoleDao.queryObjectByUserid(agentedUser.getId());
    		if(insbMiniUserRole != null) {
    			Map<String, Object> map = new HashMap<>();
    			map.put("rolecode", "02");
    			INSBMiniRole insbMiniRole = insbMiniRoleDao.queryRoleByRolecode(map);
    			if(insbMiniRole == null) return "fail";
    			insbMiniUserRole.setRoleid(insbMiniRole.getId());
    			insbMiniUserRole.setModifytime(new Date());
    			insbMiniUserRole.setOperator(((INSCUser)session.getAttribute("insc_user")).getUsercode());
    			insbMiniUserRoleDao.updateObject(insbMiniUserRole);
    		}else {
    			insbMiniUserRole = new INSBMiniUserRole();
    			insbMiniUserRole.setId(UUIDUtils.random());
    			insbMiniUserRole.setMiniuserid(agentedUser.getId());
    			Map<String, Object> map = new HashMap<>();
    			map.put("rolecode", "02");
    			INSBMiniRole insbMiniRole = insbMiniRoleDao.queryRoleByRolecode(map);
    			if(insbMiniRole == null) return "fail";
    			insbMiniUserRole.setRoleid(insbMiniRole.getId());
    			insbMiniUserRole.setCreatetime(new Date());
    			insbMiniUserRole.setOperator(((INSCUser)session.getAttribute("insc_user")).getUsercode());
    			insbMiniUserRoleDao.saveObject(insbMiniUserRole);
			}
			return "success";
		} catch (Exception e) {
			LogUtil.info("权限管理错误信息==>"+e.getMessage());
			return "fail";
		}
    }
	public Map<String, Object> queryUserByRole(HttpSession session,
            @ModelAttribute PagingParams para,
            @ModelAttribute INSBAgentUserQueryModel qm) {
		
		return null;
	}
	/**
	 * 查询角色信息
	 * @param session
	 * @param para
	 * @return
	 */
	@RequestMapping(value = "/queryRole", method = RequestMethod.GET)
    @ResponseBody
	public Map<String, Object> queryRole(HttpSession session,@ModelAttribute PagingParams para) {
		Map<String, Object> result = new HashMap<>();
		try {
			Map<String, Object> param = BeanUtils.toMap(para);
			List<INSBMiniRole> lists = insbMiniRoleDao.queryMiniRole(param);
			result.put("total", insbMiniRoleDao.queryCountRole(param));
			result.put("rows", lists);
		} catch (Exception e) {
			LogUtil.info("权限管理错误信息==>"+e.getMessage());
		}
		return result;
	}
	/**
	 * 删除角色，同时删除其他的所有的权限
	 * @param session
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/deleteRole", method = RequestMethod.GET)
    @ResponseBody
    public String deleteRole(HttpSession session, String id) {
		Map<String, Object> map = new HashMap<>();
		try {
			map.put("roleid", id);
			List<INSBRolePermission> permissions = insbRolePermissionDao.queryRperByRoleid(map);
			if(!permissions.isEmpty()) {
				for(INSBRolePermission per : permissions) {
					insbRolePermissionDao.deleteById(per.getId());
				}
			}
			List<INSBMiniUserRole> urs = insbMiniUserRoleDao.selectUserByRoleId(map);
			if(!urs.isEmpty()) {
				for(INSBMiniUserRole ur : urs) {
					insbMiniUserRoleDao.deleteObect(ur.getId());
				}
			}
			insbMiniRoleDao.deleteObect(id);
			return "success";
		} catch (Exception e) {
			LogUtil.info("权限管理错误信息==>"+e.getMessage());
			return "fail";
		}
	}
	/**
	 * 赋予用户角色能力
	 * @param session
	 * @param miniAgentJson
	 * @param miniRoleJson
	 * @return
	 */
	@RequestMapping(value = "/userAddRole", method = RequestMethod.GET)
	@ResponseBody
	public String userAddRole(HttpSession session, String miniAgentJson, String miniRoleJson) {
		
		String roleid = "";
		try {
			List<INSBAgentUser> users =  JSONArray.parseArray(miniAgentJson, INSBAgentUser.class);
			List<INSBMiniRole> roles = JSONArray.parseArray(miniRoleJson, INSBMiniRole.class);
			
			Map<String, Object> map = new HashMap<>();
			for(INSBMiniRole role1 : roles) {
				roleid = role1.getId();
				
			}
			for(INSBAgentUser user : users) {
				String userid = user.getId();
				map.put("miniuserid", userid);
				List<INSBMiniUserRole> urs = insbMiniUserRoleDao.selectUserByRoleId(map);
				/**
				 * 如果已经是有其他的角色了，删除记录再重新添加角色
				 */
				if(!urs.isEmpty()) {
					
					for(INSBMiniUserRole roleu : urs) {
						
						insbMiniUserRoleDao.deleteObect(roleu.getId());
					}
				}
				INSBMiniUserRole userrole = new INSBMiniUserRole();
				String miniuserid = user.getId();
				userrole.setId(UUIDUtils.random());
				userrole.setMiniuserid(miniuserid);
				userrole.setRoleid(roleid);
				userrole.setCreatetime(new Date());
				userrole.setOperator(((INSCUser)session.getAttribute("insc_user")).getUsercode());
				insbMiniUserRoleDao.saveObject(userrole);
			}
			return roleid;
		} catch (Exception e) {
			// TODO: handle exception
			LogUtil.info("mini权限管理错误信息==>"+e.getMessage());
			return "fail";
		}
	}
	/**
	 * 查询权限信息
	 * @param session
	 * @param model
	 * @param para
	 * @return
	 */
	@RequestMapping(value = "/queryPermissionList", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> queryPermissionList(HttpSession session,@ModelAttribute RolePermissionModel model,
			@ModelAttribute PagingParams para) {
		Map<String, Object> result = new HashMap<>();
		try {
			Map<String, Object> param = BeanUtils.toMap(para,model);
			List<Map<String, Object>> ls = insbMiniPermissionDao.queryPermissionList(param);
			result.put("rows", ls);
			result.put("total", insbMiniPermissionDao.queryCountPermission(param));
		} catch (Exception e) {
			LogUtil.info("mini权限管理错误信息==>"+e.getMessage());
		}
		return result;
	}
	/**
	 * 查询权限信息
	 * @param session
	 * @param model
	 * @param para
	 * @return
	 */
	@RequestMapping(value = "/queryPermissionListTr", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> queryPermissionListTr(HttpSession session,@ModelAttribute RolePermissionModel model,
			@ModelAttribute PagingParams para) {
		Map<String, Object> result = new HashMap<>();
		try {
			Map<String, Object> param = BeanUtils.toMap(para,model);
			List<INSBMiniPermission> ls = insbMiniPermissionDao.queryPermission(param);
			result.put("rows", ls);
			result.put("total", insbMiniPermissionDao.queryCountPermissionList(param));
		} catch (Exception e) {
			LogUtil.info("mini权限管理错误信息==>"+e.getMessage());
		}
		return result;
	}
	/**
	 * 初始化mini用户列表，角色使用
	 * @param session
	 * @param model
	 * @param para
	 * @return
	 */
	@RequestMapping(value = "/initMiniUserList", method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> initMiniUserList(HttpSession session,@ModelAttribute INSBAgentUser model,
			@ModelAttribute PagingParams para) {
		Map<String, Object> result = new HashMap<>();
		try {
			Map<String, Object> param = BeanUtils.toMap(para,model);
			List<INSBAgentUser> ls = insbMiniUserRoleDao.selectAgentUser(param);
			result.put("rows", ls);
			result.put("total", insbMiniUserRoleDao.selectCountUser(param));
		} catch (Exception e) {
			LogUtil.info("mini权限管理错误信息==>"+e.getMessage());
		}
		return result;
	}
}
