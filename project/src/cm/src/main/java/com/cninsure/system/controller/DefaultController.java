package com.cninsure.system.controller;

import com.cninsure.core.utils.StringUtil;
import com.common.redis.Constants;
import com.common.redis.IRedisClient;

import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.Date;
import java.util.ResourceBundle;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.cninsure.core.controller.BaseController;
import com.cninsure.core.exception.ControllerException;
import com.cninsure.core.utils.DateUtil;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.system.entity.INSCDept;
import com.cninsure.system.entity.INSCUser;
import com.cninsure.system.service.INSCDeptService;
import com.cninsure.system.service.INSCMenuService;

@Controller
@RequestMapping("/*")
@SessionAttributes("insc_user")
public class DefaultController extends BaseController {

	private static String serviceName = ""; 
	private static String resource = "";
	private static String host = "";
	private static String websocketServer = "";//add by hewc for websocket 20170720
	private static String messagePushChannel = "";//add by hewc for websocket 20170720
	static {
		ResourceBundle resourceBundle = ResourceBundle.getBundle("config/config");
		serviceName = resourceBundle.getString("fairy.serviceName");
		resource = resourceBundle.getString("fairy.resource");
		host = resourceBundle.getString("fairy.host");
		websocketServer = resourceBundle.getString("cm.websocket.socketserver");//add by hewc for websocket 20170720
		messagePushChannel = resourceBundle.getString("cm.message.pushchannel");//add by hewc for websocket 20170720
	}
	@Resource
	private INSCMenuService inscMenuService;
	@Resource
	private INSCDeptService inscDeptService;
    @Resource
	private IRedisClient redisClient;

	@RequestMapping(value = "application", method = RequestMethod.GET)
	public ModelAndView application(@ModelAttribute("insc_user") INSCUser user, HttpSession session)
			throws ControllerException {
		ModelAndView mav = new ModelAndView("application");
		if (user != null) {
			Date pwdmdytime = user.getPwsmodifytime();
			// 密码超过90天未修改，修改密码
			if ((pwdmdytime != null) && (new Date().getTime() - pwdmdytime.getTime() > 90 * 86400000L)) {
				mav = new ModelAndView("login");
				mav.addObject("ismodifypwd", true);
				session.setAttribute("SPRING_SECURITY_LAST_EXCEPTION", "");
				return mav;
			}
		}
		
		INSCDept inscDept = inscDeptService.getOrgDept(user.getUserorganization());
		/*try {
			KafkaClientHelper.collect("inscDept",JSONObject.fromObject(inscDept).toString());
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		mav.addObject("org", inscDept.getComname());

//		taskthreadPool4workflow.execute(new Runnable() {
//			@Override
//			public void run() { 
//				try {
//					LogUtil.debug("检索当前登录人所属组未分配任务开始");
//					//inscMenuService.loginUserDispatchWork(user.getUsercode());
//					//用户上线，通知调度中心可以分配任务给该用户，不直接获取调度任务
//					dispatchTaskService.userLoginForTask(user.getUsercode());
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//
//			}
//		});

		String menuContent = inscMenuService.queryMenusFtl(user.getUsercode(), "0");
		mav.addObject("menu", menuContent);
		// String openpwd = (String) RedisClient.get(user.getUsercode());
		Object openpwd = redisClient.get(Constants.CM_SECURITY_USER, user.getUsercode());
		// String ppp = LdapMd5.Md5Encode(openpwd.toString());
		// System.out.println("加密前"+openpwd.toString()+"加密后"+ppp);
		mav.addObject("ppp", openpwd);
		mav.addObject("username", user.getUsercode());
		mav.addObject("postfix", "@" + serviceName + "/cm");
		mav.addObject("host", "http://" + host + ":7070/http-bind/");

		mav.addObject("messagePushChannel",messagePushChannel);//add by hewc for websocket 20170720
		mav.addObject("websocketServer",websocketServer);//add by hewc for websocket 20170720

		mav.addObject("myMenu", inscMenuService.getTaskManageDataByUserCode(user.getUsercode()));

		mav.addObject("showQuickLoopMenu", StringUtil.isNotEmpty(menuContent) ? (menuContent.contains("loopunderwritinglist") ? "true" : "false") : "false");

		LogUtil.debug("login username ：" + user.getUsercode() + openpwd);
		return mav;
	}

	@RequestMapping(value = "user/logout", method = RequestMethod.GET)
	public void logout(HttpServletRequest request,HttpServletResponse response) throws ControllerException, IOException {
		response.sendRedirect(request.getContextPath() + "/auth/logout");
	}

	@RequestMapping(value = "auth/sign")
	public ModelAndView login(HttpServletRequest re) throws ControllerException {
		return new ModelAndView("login");
	}

	@RequestMapping(value = "auth/denied", method = RequestMethod.GET)
	public ModelAndView denied() throws ControllerException {
		return new ModelAndView("denied");
	}

	@RequestMapping(value = "auth/same", method = RequestMethod.GET)
	public ModelAndView same() throws ControllerException {
		return new ModelAndView("same");
	}

	@RequestMapping(value = "error", method = RequestMethod.GET)
	public ModelAndView error() throws ControllerException {
		return new ModelAndView("error");
	}

}
