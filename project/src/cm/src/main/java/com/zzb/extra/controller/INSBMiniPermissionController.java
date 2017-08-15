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

import com.alibaba.fastjson.JSONArray;
import com.cninsure.core.controller.BaseController;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.cninsure.core.utils.UUIDUtils;
import com.cninsure.system.entity.INSCUser;
import com.zzb.conf.dao.INSBAgentDao;
import com.zzb.conf.entity.INSBAgent;
import com.zzb.extra.dao.INSBAgentUserDao;
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
import com.zzb.extra.service.INSBAgentUserService;
import com.zzb.extra.service.INSBMiniUserRoleService;
@Controller
@RequestMapping("/miniPermission/*")
/**
 * 
 try {
			return "success";
		} catch (Exception e) {
			LogUtil.info("权限管理错误信息"+e.getMessage());
			return "fail";
		}
	((INSCUser) session.getAttribute("insc_user")).getUsercode()	
 * @author shiguiwu
 *
 */
public class INSBMiniPermissionController extends BaseController {
	@Resource
	private INSBMiniPermissionDao permissionDao;
	
	@Resource
	private INSBMiniRoleDao roleDao;
	
	@Resource
	private INSBMiniUserRoleDao userRoleDao;
	
	@Resource
	private INSBRolePermissionDao rolePermissionDao;
	
	@Resource
	private INSBAgentUserDao agentDao;
	
	@Resource
	private INSBMiniUserRoleService insbMiniUserRoleService;
	
	@Resource
    private INSBAgentUserService insbAgentUserService;
	
	@Resource
	private INSBAgentDao insbAgentDao;
	/**
	 * 添加和更新权限
	 * @param permission
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/addOrUpdateMiniPermission", method = RequestMethod.GET)
	@ResponseBody
	public String addOrUpdateMiniPermission(@ModelAttribute INSBMiniPermission permission,HttpSession session) {
		try {
			Long tmpcode = permissionDao.selectMaxPercode(new HashMap<String, Object>());
			if(tmpcode == null) {
				tmpcode = 1L;
			} else {
				tmpcode++;
			}
			if(permission.getId() == null) {
				permission.setId(UUIDUtils.random());
				permission.setPerindex(tmpcode.intValue());
				permission.setPercode("qx"+StringUtil.leftPad(String.valueOf(tmpcode.intValue()), '0', 5));
				permission.setCreatetime(new Date());
				permission.setOperator(((INSCUser) session.getAttribute("insc_user")).getUsercode());
				permissionDao.saveObject(permission);
			} else {
				permission.setModifytime(new Date());
				permission.setOperator(((INSCUser) session.getAttribute("insc_user")).getUsercode());
				permissionDao.updateObject(permission);
			}
			return "success";
		} catch (Exception e) {
			LogUtil.info("mini权限管理错误信息==>"+e.getMessage());
			return "fail";
		}
	}
	/**
	 * 删除权限，同时删除中间表的记录
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/deletePermision", method = RequestMethod.GET)
	@ResponseBody
	public String deletePermision(String id) {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("permissionid", id);
			List<INSBRolePermission> rps = rolePermissionDao.queryRolePerByPerid(map);
			if( !rps.isEmpty() && rps != null) {
				for(INSBRolePermission rolePermission : rps) {
					rolePermissionDao.deleteById(rolePermission.getId());
				}
			}
			permissionDao.deleteObect(id);
			return "success";
		} catch (Exception e) {
			LogUtil.info("mini权限管理错误信息====>"+e.getMessage());
			return "fail";
		}
	}
	/**
	 * 接触角色和用户的关系
	 * @param uid
	 * @return
	 */
	@RequestMapping(value = "/relieveRoleToUser", method = RequestMethod.GET)
	@ResponseBody
	public String relieveRoleToUser(String uid, String rid) {
		 try {
			 Map<String, Object> map = new HashMap<>();
			 map.put("miniuserid", uid);
			 map.put("roleid", rid);
			 INSBMiniUserRole ur = userRoleDao.queryObjectByUidAndRoleid(map);
			 if(ur != null) userRoleDao.deleteObect(ur.getId());
			 Map<String, Object> mapUser = new HashMap<>();
			 mapUser.put("id", ur.getMiniuserid());
			 INSBAgentUser user = userRoleDao.selectByOpenid(mapUser);
			 if(user.getReferrerid() != null) user.setReferrerid(null);
			 insbAgentDao.updateById(user);
			 return "success";
			} catch (Exception e) {
				LogUtil.info("mini权限管理错误信息==>"+e.getMessage());
				return "fail";
			}
	}
	/**
	 * 接触角色和权限的关系的
	 * @param id
	 * @param roleJson
	 * @return
	 */
	@RequestMapping(value = "/relievePermissionToRole", method = RequestMethod.GET)
	@ResponseBody
	public String relievePermissionToRole(String id, String roleJson) {
		List<INSBMiniRole> roles = JSONArray.parseArray(roleJson, INSBMiniRole.class);
		String roleid = "";
		try {
			for(INSBMiniRole role : roles) {
				roleid = role.getId();
			}
			Map<String, Object> map = new HashMap<>();
			map.put("permissionid", id);
			map.put("roleid", roleid);
			List<INSBRolePermission> rps = rolePermissionDao.queryRolePerByPerid(map);
			if(!rps.isEmpty()) {
				for(INSBRolePermission rp : rps) {
					rolePermissionDao.deleteObect(rp.getId());
				}
			}
			return roleid;
		} catch (Exception e) {
			LogUtil.info("权限管理错误信息"+e.getMessage());
			return "fail";
		}
	}
	/**
	 * 初始化用户角色哈哈
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/initUserAtrrBt", method = RequestMethod.GET)
	@ResponseBody
	public String initUserAtrrBt(HttpSession session) {
		try {
			INSBMiniUserRole insbMiniUserRole = new INSBMiniUserRole();
			insbMiniUserRole.setOperator(((INSCUser) session.getAttribute("insc_user")).getUsercode());
			insbMiniUserRoleService.insert(insbMiniUserRole);
			return "success";
		} catch (Exception e) {
			LogUtil.info("mini权限管理错误信息==>"+e.getMessage());
			return "fail";
		}
	}
	@RequestMapping(value = "/updateMiniUser", method = RequestMethod.GET)
	@ResponseBody
	public String updateMiniUser(HttpSession session, INSBAgentUserQueryModel mode) {
		try {
			Map<String, Object> map = new HashMap<>();
			map.put("openid", mode.getOpenid());
			INSBAgent miniAgent = userRoleDao.selectByOpenid(map);
			if(miniAgent != null) {
				miniAgent.setPushChannel(mode.getPushChannel());
				miniAgent.setModifytime(new Date());//operator
				miniAgent.setOperator(((INSCUser) session.getAttribute("insc_user")).getUsercode());
				userRoleDao.updateMiniObject(miniAgent);
			}
			return "success";
		} catch (Exception e) {
			
			LogUtil.info("mini权限管理错误信息==>"+e.getMessage());
			return "fail";
		}
	}
	
	
}
