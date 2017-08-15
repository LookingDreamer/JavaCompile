package com.cninsure.system.security;

import com.cninsure.core.tools.util.PropertiesUtil;
import com.cninsure.core.utils.DateUtil;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.cninsure.system.entity.INSCUser;
import com.common.ConfigUtil;
import com.common.XMPPUtils;
import com.common.redis.Constants;
import com.common.redis.IRedisClient;
import com.zzb.chn.service.CHNChannelService;
import com.zzb.chn.util.RSACoderUtil;
import com.zzb.cm.Interface.controller.IpUtil;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.regex.Pattern;


import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zzb.cm.service.impl.INSBNewMessagePushServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.access.intercept.AbstractSecurityInterceptor;
import org.springframework.security.access.intercept.InterceptorStatusToken;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class SecurityDefaultInterceptor extends AbstractSecurityInterceptor
		implements Filter {
    public static final String CM_SECURITY_SESSION = "cm:security:session";
    public static final String USER_LASTACTIVETIME = "user:lastactivetime";
	private String loginPath = "/login";
	private long failureTime = 1000;
	private static List<String> allPath = new ArrayList<String>();

	private FilterInvocationSecurityMetadataSource securityMetadataSource;

    private IRedisClient redisClient;

	private static Pattern pattern = null;
	
	private static int userActiveMinutes = 0;

	
	//不校验的url
	static{
		allPath.add("/mobile/login/");
		allPath.add("/mobile/pay/callback");
		allPath.add("/mobile/registered/");
		allPath.add("/mobile/basedata/");
		allPath.add("/mobile/other/");
		//读取配置文件
		ResourceBundle resourceBundle = ResourceBundle.getBundle("config/xss");
		String regEx = resourceBundle.getString("illegal_input");
		pattern = Pattern.compile(regEx);
		userActiveMinutes = PropertiesUtil.getPropInteger("user.active.minutes");
	}
	
	public SecurityDefaultInterceptor(String loginPath) {
		if (StringUtils.isNoneBlank(loginPath)) {
			this.loginPath = loginPath;
		}
	}

	public FilterInvocationSecurityMetadataSource getSecurityMetadataSource() {
		return securityMetadataSource;
	}

	public void setSecurityMetadataSource(
			FilterInvocationSecurityMetadataSource securityMetadataSource) {
		this.securityMetadataSource = securityMetadataSource;
	}
	private boolean illegalInput(HttpServletRequest httprequest) throws ServletException, IOException {
		boolean flag = false;
		Map<String, String[]> params = httprequest.getParameterMap();
		for (String key : params.keySet()) {
			if (StringUtil.isEmpty(key)) continue;
			String[] values = params.get(key);
			for (int i = 0; i < values.length; i++) {
				String value = values[i];
				if (StringUtil.isEmpty(value)) continue;
				flag=pattern.matcher(value).find();
				if (flag) {
					break;
				}
			}
			if (flag) {
				break;
			}
		}
		return flag;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httprequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response; 
		String pathinfo = httprequest.getPathInfo();
		try {
			String ip = IpUtil.getIpAddr(httprequest);

			if (pathinfo!=null && !pathinfo.contains("saveInsuranceConfig")) {
				Enumeration<String> enu = request.getParameterNames();
				while (enu.hasMoreElements()) {//参数
					String paraName = (String) enu.nextElement();
					pathinfo += ("?" + paraName + ": " + request.getParameter(paraName) + "-");
				}
			}

			LogUtil.info("进入CM 后台 IP ="+ip +",调用接口= "+pathinfo );
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(pathinfo==null){
			request.getRequestDispatcher("/auth/sign").forward(request, response);
			return;
		}
		
		Object objuser = httprequest.getSession().getAttribute("insc_user");
		if(objuser != null){
			INSCUser user = (INSCUser)objuser;
			String usercode = user.getUsercode();
			if(!StringUtil.isEmpty(usercode)){
				String lastactivetimeStr = String.valueOf(redisClient.get(7, USER_LASTACTIVETIME, user.getUsercode()));
				Date nowDate = new Date();
				if(!StringUtil.isEmpty(lastactivetimeStr)){
					Date lastactivetime = DateUtil.parseDateTime(lastactivetimeStr);
					boolean isLogin = false;
					Object fromloginftl = httprequest.getSession().getAttribute("fromloginftl");
					if(fromloginftl != null && "true".equals(fromloginftl.toString())){
						isLogin = true;//登录页面不做验证
						httprequest.getSession().removeAttribute("fromloginftl");
					}
					//不是同一天不做验证,登录页面不做验证
					if(!isLogin && DateUtil.toString(nowDate).equals(DateUtil.toString(lastactivetime))){
						//超过userActiveMinutes分钟未操作重新登录
						if(nowDate.getTime() - lastactivetime.getTime() >= userActiveMinutes * 60 * 1000L){
							LogUtil.info("用户(" + usercode + ")长时间未操作，重新登录！上次操作时间:" + lastactivetimeStr);
							redisClient.set(7, USER_LASTACTIVETIME, usercode, DateUtil.toDateTimeString(nowDate));
//							request.getRequestDispatcher("/auth/sign").forward(request, response);
							//通知openfire 下线
							String messageChannelConfig = ConfigUtil.getPropString("cm.message.pushchannel", "openfire");//add by hewc for websocket 20170720
							if("websocket".equals(messageChannelConfig)){
								INSBNewMessagePushServiceImpl.offline(usercode);
							}else {  //add by hewc for websocket 20170720
								XMPPUtils.getInstance().updateStatus(usercode, "unavailable");
							}
							httpResponse.sendRedirect("/cm/auth/logout");
							return;
						}
					}
				}
				redisClient.set(7, USER_LASTACTIVETIME, usercode, DateUtil.toDateTimeString(nowDate));
			}
		}

		// 登录页面不做验证
		if (loginPath.equals(pathinfo)) {
			request.getRequestDispatcher(pathinfo).forward(request, response);
			return;
		}else if(pathinfo.startsWith("/mobile/")){

			if (illegalInput(httprequest)) {
				httprequest.getRequestDispatcher("/mobile/login/illegalInput").forward(httprequest, httpResponse);
				return;
			}
			//直接跳过的地址
			if(accessPath(pathinfo)){
				request.getRequestDispatcher(pathinfo).forward(request, response);
				return;
			}
			String token = httprequest.getHeader("token");
			if(null == token || "".equals(token)){
				LogUtil.info("token 不存在");
				request.getRequestDispatcher("/mobile/login/loginstatus?status="+"1000").forward(request, response);
				return;
			}else{
				@SuppressWarnings("unchecked")
				Map<String, Object> obj = redisClient.get(Constants.TOKEN, token, Map.class);
				if(null == obj){
					LogUtil.info("object 不存在");
					request.getRequestDispatcher("/mobile/login/loginstatus?status="+"2000").forward(request, response);
					return;
				}else{
					JSONObject jsonContext =  JSONObject.fromObject(obj.get("permissions"));
					JSONArray jsonArray = null;
					if (jsonContext != null) {
						jsonArray = JSONArray.fromObject(jsonContext.getString("context"));
					}
					
					//得到最后活动时间
					long lastActiveTime = (long) obj.get("lastActiveTime");
					long nowTime = System.currentTimeMillis();
					if(nowTime - lastActiveTime > failureTime){
						LogUtil.info("超时了，重新登录");
						request.getRequestDispatcher("/mobile/login/loginstatus?status="+"3000").forward(request, response);
						//清空redis
                        redisClient.del(Constants.TOKEN, token);
						return;
					}else{
						if(containsURL(jsonArray,pathinfo)){
							LogUtil.info("containsURL");
							obj.put("lastActiveTime",nowTime);
                            redisClient.set(Constants.TOKEN, token, JSONObject.fromObject(obj).toString(),(int)failureTime/1000);
							request.getRequestDispatcher(pathinfo).forward(request, response);
							return;
						}else{
							//LogUtil.info("非法请求");
							//request.getRequestDispatcher("/mobile/login/loginstatus?status="+"4000").forward(request, response);
							request.getRequestDispatcher(pathinfo).forward(request, response);
							return;
						}
					}
				}
			}
		} else if (pathinfo.startsWith("/channelMerchant/")) { //渠道商户
			request.getRequestDispatcher(pathinfo).forward(request, response);
			return;
		}else if(pathinfo.startsWith("/channelService/")){
			String mimi = httprequest.getHeader("innerPipe");  
			if( pathinfo.endsWith("/getToken") || pathinfo.endsWith("/error") || pathinfo.endsWith("/verifyInnerAuthCode") || 
			    pathinfo.startsWith("/channelService/getSign") || "zheshiyigemimi!".equals(mimi)){
				
				request.getRequestDispatcher(pathinfo).forward(request, response);
				return;
			}
			String msg=null;
			String channelId = httprequest.getHeader("channelId");
			String nonceStr  =httprequest.getHeader("nonceStr");
			String signStr  =httprequest.getHeader("signStr");
			LogUtil.info("channelId=" + channelId + "&nonceStr=" + nonceStr + "&signStr=" + signStr);
					
			if ( StringUtils.isEmpty(channelId) ) {
				LogUtil.info("channelId不能为空");
				msg="channelId不能为空";
				request.getRequestDispatcher("/channelService/error?status="+"403&msg="+msg).forward(request, response);
				return;
			} else {
				if ( StringUtils.isEmpty(nonceStr) || StringUtils.isEmpty(signStr) ) {
					String sessionId = httprequest.getSession().getId();
					String sessionValue = (String)redisClient.get(CM_SECURITY_SESSION, sessionId);
					LogUtil.info("▇渠道内嵌页面sessionId：" + sessionId);
					LogUtil.info("▇渠道内嵌页面sessionValue：" + sessionValue);
					if ( StringUtils.isNotEmpty(sessionValue) ) {
						LogUtil.info("▇渠道内嵌页面验证通过");
						request.getRequestDispatcher(pathinfo).forward(request, response);
						return;
					}
					
					LogUtil.info("nonceStr signStr不能为空");
					msg="nonceStr signStr不能为空";
					request.getRequestDispatcher("/channelService/error?status="+"403&msg="+msg).forward(request, response);
					return;
				} 
			}
			
			String accessTokens = (String)redisClient.get(CHNChannelService.CHANNEL_MODULE, channelId);
			if(StringUtils.isEmpty(accessTokens)){
				LogUtil.info(channelId+"未找到accessToken");
				msg="未找到accessToken";
				request.getRequestDispatcher("/channelService/error?status="+"403&msg="+msg).forward(request, response);
				return;
			}
			String[] datas = accessTokens.split("_");
			String accessToken =datas[0];
			LogUtil.info("accessToken:" + accessToken);
			long invalideTime =Long.parseLong(datas[1]);
			if((System.currentTimeMillis()-invalideTime)>7200*1000){
				LogUtil.info(channelId+"accessTokens超时！");
				msg="accessTokens超时";
                redisClient.del(CHNChannelService.CHANNEL_MODULE, channelId);
				request.getRequestDispatcher("/channelService/error?status="+"404&msg="+msg).forward(request, response);
				return;
			}
			try{
			    if(RSACoderUtil.verify(accessToken + nonceStr, signStr)){//true){//

					request.getRequestDispatcher(pathinfo).forward(request, response);

					return;
			    }else{
			    	LogUtil.info(channelId+"accessTokens验证失败！");
					msg="accessTokens验证失败";
					request.getRequestDispatcher("/channelService/error?status="+"403&msg="+msg).forward(request, response);
					return;
			    }
				
			}catch(Exception e){
				e.printStackTrace();
				LogUtil.info(channelId+"accessTokens验证时发生异常 ！");
				msg="accessTokens验证失败";
				request.getRequestDispatcher("/channelService/error?status="+"403&msg="+msg).forward(request, response);
				return;
			}
					
		}else{
		
			FilterInvocation fi = new FilterInvocation(request, response, chain);
			InterceptorStatusToken token = super.beforeInvocation(fi);
			
			try {
				fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
			} finally {
				super.afterInvocation(token, null);
			}
		}
	}

	private boolean containsURL(JSONArray jsonArray,String path){

		//代理人没有权限包 不限制
		if(jsonArray==null||jsonArray.size()==0){
			return true;
		}
		
		boolean result = false;
		for(int i = 0;i < jsonArray.size();i ++){
			JSONObject jsonObject = JSONObject.fromObject(jsonArray.get(i));
			String addresscode = jsonObject.getString("addresscode");
			if(path.startsWith("/mobile/" + addresscode)){
				result = true;
			}
		}
		return result;
	}
	
	private boolean accessPath(String path){
		boolean result = false;
		for(String info : allPath){
			if(path.startsWith(info)){
				result = true;
				break;
			}
		}
		return result;
	}
	
	@Override
	public Class<?> getSecureObjectClass() {
		return FilterInvocation.class;
	}

	@Override
	public SecurityMetadataSource obtainSecurityMetadataSource() {
		return this.securityMetadataSource;
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}

	@Override
	public void destroy() {
	}

	public long getFailureTime() {
		return failureTime;
	}

	public void setFailureTime(long failureTime) {
		this.failureTime = failureTime;
	}

    public void setRedisClient(IRedisClient redisClient) {
        this.redisClient = redisClient;
    }
}
