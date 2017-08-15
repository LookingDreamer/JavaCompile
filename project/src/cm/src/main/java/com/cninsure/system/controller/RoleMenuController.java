package com.cninsure.system.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cninsure.core.controller.BaseController;
import com.cninsure.system.service.INSCRoleMenuService;

@Controller
@RequestMapping("/rolemenu/*")
public class RoleMenuController extends BaseController {
	
	@Resource
	private INSCRoleMenuService inscRoleMenuService;
	
	/**
	 * 为角色绑定菜单
	 * @param session
	 * @param roleId
	 * @param menuIds
	 * @return
	 */
	@RequestMapping(value="bindingrolemenu" ,method=RequestMethod.POST)
	public String bindingRoleMenu( HttpSession session,String roleId, String menuIds){
		inscRoleMenuService.repairRoleMenu(session, roleId, menuIds);
		return "";
	}

}
