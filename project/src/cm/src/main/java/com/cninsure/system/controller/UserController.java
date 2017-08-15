package com.cninsure.system.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import com.cninsure.core.utils.LogUtil;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cninsure.core.controller.BaseController;
import com.cninsure.core.exception.ControllerException;
import com.cninsure.core.utils.BeanUtils;
import com.cninsure.core.utils.StringUtil;
import com.cninsure.system.entity.INSCRole;
import com.cninsure.system.entity.INSCUser;
import com.cninsure.system.service.INSCDeptService;
import com.cninsure.system.service.INSCRoleService;
import com.cninsure.system.service.INSCUserRoleService;
import com.cninsure.system.service.INSCUserService;
import com.common.PagingParams;

@Controller
@RequestMapping("/user/*")
public class UserController extends BaseController {

	@Resource
	private INSCUserService inscUserService;
	@Resource
	private INSCRoleService inscRoleService;
	@Resource
	private INSCUserRoleService inscUserRoleService;
	@Resource
	private INSCDeptService deptService;

	/**
	 * 跳转到列表页面
	 * 
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "menu2list", method = RequestMethod.GET)
	public ModelAndView list() throws ControllerException {
		ModelAndView mav = new ModelAndView("system/userlist");
		return mav;
	}

	/**
	 * 初始化用户列表
	 * 
	 * @param para
	 * @param user
	 * @param dept
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "inituserlist", method = RequestMethod.GET)
	@ResponseBody
//	public Map<String, Object> initUserList(@ModelAttribute PagingParams para,
//			@ModelAttribute INSCUser user)
//			throws ControllerException {
//		Map<String, Object> map = BeanUtils.toMap(user, para);
//		return inscUserService.initUserList(map);
//	}
	public Map<String, Object> initUserList(@ModelAttribute PagingParams para,
			@ModelAttribute INSCUser user,@RequestParam(value = "roleid", required = false) String roleid)
			throws ControllerException {
		Map<String, Object> map = BeanUtils.toMap(user, para);
		map.put("roleid", roleid);
		return inscUserService.initUserList(map);
	}
	/**
	 * 查角色
	 * @param session
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "rolelist", method = RequestMethod.GET)
	@ResponseBody
	public List<INSCRole> rolelist(HttpSession session)
			throws ControllerException {
		List<INSCRole> rolelist = inscRoleService.queryAll();
		return rolelist;
	}
	/**
	 * 转到编辑页面
	 * 
	 * @param id
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "main2edit", method = RequestMethod.GET)
	public ModelAndView initEditeData(HttpSession session,
			@RequestParam(value = "id", required = false) String id)
			throws ControllerException {
		ModelAndView mav = new ModelAndView("system/usersave");
		INSCUser operator = (INSCUser) session.getAttribute("insc_user");
		Map<String, Object> map = inscUserService.getEditeData(id);

		Date date = new Date();		
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		mav.addObject("newDate",format.format(date));
		mav.addObject("user", map.get("user"));
		mav.addObject("roleIds", map.get("roleIds"));
		mav.addObject("roleList", map.get("roleList"));
		mav.addObject("addList", map.get("addList"));
		mav.addObject("usercode", operator.getUsercode());
		return mav;
	}

	/**
	 * 
	 * 保存用户信息
	 * 
	 * @param session
	 * @param user
	 * @param roleids
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "saveuser", method = RequestMethod.POST)
	public ModelAndView saveUser(HttpSession session,
			@ModelAttribute INSCUser user, String to) {
		ModelAndView mav = new ModelAndView("system/userlist");
		
		INSCUser operator = (INSCUser) session.getAttribute("insc_user");
		
		try {
			if(user.getMaturitydata().isEmpty()){
				user.setMaturitydata("2099-12-30");
			}
			inscUserService.saveOrUpdate(operator, user, to);
			mav.addObject("flag", "success");
		} catch (Exception e) {
			mav.addObject("flag", "false");
			e.printStackTrace();
			
		}

		
		return mav;
	}
	
	@RequestMapping(value = "checkpassword")
	@ResponseBody
	public String checkpassword(@RequestParam(value = "oldpwd") String oldpwd,
			@RequestParam(value = "newpwd") String newpwd){
		JSONObject jsonObject = new JSONObject();
		String resmgs = "success";
		boolean isreturn = false;
		if(!passwordRegExCheck(newpwd)){
			resmgs = "invalidnewpwd";//新密码格式错误
			isreturn = true;
		}

		//不是原密码，加密判断是否是旧密码
		if(!isreturn && !newpwd.equals(oldpwd)){
			String newpassword = StringUtil.md5Base64(newpwd);
			if(newpassword.equalsIgnoreCase(oldpwd)){
				resmgs = "samepwderr";
			}
		}
		
//		String id = null;
//		boolean isreturn = false;
//		
//		//修改用户，校验新密码是否和旧密码一致
//		if(!isreturn && user != null && !StringUtil.isEmpty(user.getId())){
//			id = user.getId();
//			INSCUser oldUser = inscUserService.queryById(id);
//			if(oldUser != null){
//				String oldpassword = oldUser.getPassword();
//				
//				//不是原密码，加密判断是否是旧密码
//				if(!oldpassword.equals(newpassword)){
//					newpassword = StringUtil.md5Base64(user.getPassword());
//					if(newpassword.equalsIgnoreCase(oldpassword)){
//						mav = new ModelAndView("system/usersave");
//						mav.addObject("samepwderr", true);
//						isreturn = true;
//					}
//				}
//			}
//		}
		
		jsonObject.accumulate("resmsg", resmgs);
		
		return jsonObject.toString();
	}

	/**
	 * 删除用户信息
	 * 
	 * @param session
	 * @param id
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "deletebyid", method = RequestMethod.GET)
	@ResponseBody
	public String deleteById(HttpSession session,
			@RequestParam(value = "id") String id) throws ControllerException {
		INSCUser operator = (INSCUser) session.getAttribute("insc_user");
		LogUtil.info("用户信息删除id为%s,操作人:%s", id, operator.getUsercode());
		int count = inscUserService.cascadDelete(id);
		JSONObject jsonObject = new JSONObject();
		jsonObject.accumulate("count", count);
		return jsonObject.toString();
	}

	/**
	 * 
	 * 批量删除用户信息
	 * 
	 * @param session
	 * @param arrayid
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "benchdeletebyids", method = RequestMethod.GET)
	@ResponseBody
	public String benchDeleteByIds(HttpSession session,
			@RequestParam(value = "arrayid" ,required = true) List<String> arrayid)
			throws ControllerException {
		INSCUser operator = (INSCUser) session.getAttribute("insc_user");
		LogUtil.info("批量删除用户信息ids为%s,操作人:%s", arrayid, operator.getUsercode());
		inscUserService.benchDeleteByIds(arrayid);
		JSONObject jsonObject = new JSONObject();
		int count = arrayid.size();
		jsonObject.accumulate("count", count);
		return jsonObject.toString();
	}

	/**
	 * 
	 * 跳转到修改页面
	 * 
	 * @param session
	 * @param id
	 * @param usercode
	 * @param username
	 * @param userorganization
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "updateuser", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView updateuser(
			HttpSession session,
			@RequestParam("id") String id,
			@RequestParam(value = "usercode", required = false) String usercode,
			@RequestParam(value = "username", required = false) String username,
			@RequestParam(value = "userorganization", required = false) String userorganization)
			throws ControllerException {
		INSCUser operator = (INSCUser) session.getAttribute("insc_user");
		ModelAndView mav = new ModelAndView("system/userupdate");
		INSCUser updateuser = new INSCUser();
		updateuser.setId(id);
		updateuser.setUsercode(usercode);
		updateuser.setUsername("username");
//		updateuser.setOperator("test");
		updateuser.setOperator(operator.getUsercode());
		updateuser.setModifytime(new Date());
//		updateuser.setPassword(StringUtil.md5Base64("123456"));
		updateuser.setPassword(StringUtil.md5Base64("password01!"));
		updateuser.setUserorganization(userorganization);
		inscUserService.updateById(updateuser);
		mav.addObject("updateuser", updateuser);
		mav.addObject("msg", "true");
		return mav;
	}

	@RequestMapping(value = "queryone", method = RequestMethod.GET)
	@ResponseBody
	public ModelAndView queryone(HttpSession session,
			@RequestParam("usercode") String usercode,
			@RequestParam("username") String username)
			throws ControllerException {
		ModelAndView mav = new ModelAndView("system/userlist");
		mav.addObject("usercode", usercode);
		List<INSCUser> userList = new ArrayList<INSCUser>();
		INSCUser user = new INSCUser();
		user.setUsercode(usercode);
		user.setUsername(username);
		user = inscUserService.queryOne(user);
		userList.add(user);
		mav.addObject("userList", userList);
		return mav;
	}

	@RequestMapping(value = "query", method = RequestMethod.GET)
	public ModelAndView query(HttpSession session,
			@RequestParam("user") INSCUser user) throws ControllerException {
		ModelAndView mav = new ModelAndView("system/userlist");
		mav.addObject("user", user);
		List<INSCUser> userList = new ArrayList<INSCUser>();
		userList = inscUserService.queryList(user);
		mav.addObject("userList", userList);
		return mav;
	}

	@RequestMapping(value = "usercodecheck", method = RequestMethod.POST)
	@ResponseBody
	public String userCodeCheck(
			@RequestParam(value = "usercode") String usercode,
			@RequestParam(value = "id") String id) throws ControllerException {
		INSCUser user = inscUserService.queryById(id);
		if (!StringUtil.isEmpty(user) && user.getUsercode().equals(usercode)) {
			msg = true + "";
		} else {
			msg = inscUserService.userCodeCheck(usercode) + "";
		}
		return msg;
	}

	@RequestMapping(value = "updatepassword", method = RequestMethod.GET)
	public ModelAndView updatepassword(HttpSession session)
			throws ControllerException {
		String usercode = "1";
		ModelAndView mav = new ModelAndView("system/userupdatepassword");
		INSCUser user = inscUserService.queryByUserCode(usercode);
		mav.addObject("user", user);
		return mav;
	}
	
	@RequestMapping(value = "updatetasks", method = RequestMethod.GET)
	public ModelAndView updatetasks(HttpSession session)
			throws ControllerException {
		ModelAndView mav = new ModelAndView("system/userupdatetaskpool");
		INSCUser loginUser = (INSCUser) session.getAttribute("insc_user");
		int t = inscUserService.getByUsercode(loginUser.getUsercode()).getTaskpool();
		mav.addObject("taskpool",t>0?t:20);
		return mav;
	}
	
	@RequestMapping(value = "changetaskpool", method = RequestMethod.POST)
	@ResponseBody
	public String changetaskpool( @RequestParam(value = "usercode") String usercode, @RequestParam(value = "taskpool") String taskpool )
			throws ControllerException {
		boolean falg = inscUserService.changePooltasks(usercode, taskpool);
		//ALTER TABLE `inscuser`
		//ADD COLUMN `taskpool`  int(3) NULL AFTER `onlinestatus`;
		JSONObject jsonObject = new JSONObject();
		msg = falg + "";
		jsonObject.accumulate("msg", msg);
		return jsonObject.toString();
	}
	@RequestMapping(value = "changepassword", method = RequestMethod.POST)
	@ResponseBody
	public String changePassWord(
			@RequestParam(value = "usercode") String usercode,
			@RequestParam(value = "oldpwd") String oldpwd,
			@RequestParam(value = "newpwd") String newpwd)
			throws ControllerException {
		
		JSONObject jsonObject = new JSONObject();
		if(!passwordRegExCheck(newpwd)){
			jsonObject.accumulate("msg", "invalidpwd");
			return jsonObject.toString();
		}
		
		boolean falg = inscUserService.changePassword(usercode, oldpwd, newpwd);
		msg = falg + "";
		jsonObject.accumulate("msg", msg);
		return jsonObject.toString();
	}
	@RequestMapping(value = "introduction/{usercode}", method = RequestMethod.GET)
	public ModelAndView introduction(
			@PathVariable(value = "usercode") String usercode) {
		ModelAndView mav = new ModelAndView("system/userintroduction");
		INSCUser user = new INSCUser();
		mav.addObject("user", user);
		return mav;
	}

	@RequestMapping(value = "message/{usercode}", method = RequestMethod.GET)
	public ModelAndView message(
			@PathVariable(value = "usercode") String usercode) {
		ModelAndView mav = new ModelAndView("system/usermessage");
		INSCUser user = new INSCUser();
		mav.addObject("user", user);
		return mav;
	}

	/**
	 * 批量重置密码
	 * 
	 * @param userIds
	 * @return
	 */
	@RequestMapping(value = "resetpwd", method = RequestMethod.GET)
	public Map<String, String> resetPwd(String userIds) {
		return inscUserService.updateResetPwd(userIds);
	}

	/**
	 * 批量重用户状态停用
	 * 
	 * @param userIds
	 * @return
	 */
	@RequestMapping(value = "resetusersataus", method = RequestMethod.GET)
	public Map<String, String> resetUserSataus(String userIds,int type) {
		return inscUserService.updateResetUserSataus(userIds,type);
	}

	/**
	 * 查询条件 机构树（异步）
	 * 
	 * @return
	 */
	@RequestMapping(value = "initdepttree", method = RequestMethod.POST)
	@ResponseBody
	public List<Map<String, String>> initDeptTree(
			@RequestParam(value = "id", required = false) String parentcode) {
		return deptService.queryTreeList(parentcode);
	}
	
	/**
	 * 密码正则表达式校验,6-16位包含数字、大写字母、小写字母、特殊符号4种中的任意三种
	 * @param password
	 * @return
	 */
	public boolean passwordRegExCheck(String password){
		if(password == null) return false;
		String regEx = "^(?![0-9a-z]+$)(?![0-9A-Z]+$)(?![0-9\\W]+$)(?![a-z\\W]+$)(?![a-zA-Z]+$)(?![A-Z\\W]+$)[a-zA-Z0-9\\W_]{6,16}$";
		return Pattern.matches(regEx, password);
	}
}
