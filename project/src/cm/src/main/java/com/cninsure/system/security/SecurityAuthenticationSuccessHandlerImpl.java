package com.cninsure.system.security;

import com.common.redis.CMRedisClient;
import com.common.redis.IRedisClient;
import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.cninsure.system.entity.INSCUser;
import com.cninsure.system.service.INSCUserService;

public class SecurityAuthenticationSuccessHandlerImpl implements
		AuthenticationSuccessHandler {

	private static final String MODULE = "cm:security:user";
	@Resource
	private INSCUserService inscUserService;

	@Resource
	private IRedisClient redisClient;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		String pws = request.getParameter("password")+"";
		String usercode = authentication.getName();
		try {
			redisClient.set(MODULE, usercode, StringUtil.md5Base64(pws), 24*60*60);
//			redisClient.set(MODULE, usercode, pws);
		} catch (Exception e) {
			LogUtil.error("密文密码存储redis错误");
			e.printStackTrace();
		}
		INSCUser user = null;
		if (StringUtils.isNotBlank(usercode)) {
			user = inscUserService.queryByUserCode(usercode);
		}

		request.getSession().setAttribute("insc_user", user);
		request.getSession().setAttribute("fromloginftl", "true");
		response.sendRedirect(request.getContextPath() + "/application");
	}

}
