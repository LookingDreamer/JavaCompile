package com.cninsure.system.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cninsure.core.controller.BaseController;
import com.cninsure.system.service.INSCUserRoleService;

@Controller
@RequestMapping("/userrole/*")
public class UserRoleController extends BaseController {
	
	@Resource
	private INSCUserRoleService inscUserRoleService;

}
