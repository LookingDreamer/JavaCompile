package com.zzb.cm.Interface.service;

import com.common.redis.IRedisClient;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.cninsure.core.tools.util.ValidateUtil;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.system.service.OnlineService;
import com.common.XMPPUtils;

public class OpenfireOnlineUsersListener implements ServletContextListener{

	private static final String url_dat = "http://" + ValidateUtil.getConfigValue("fairy.host") + ":" + ValidateUtil.getConfigValue("fairy.backgr.port") + "/plugins/online/status";
	public static final String ONLINE = "cm:zzb:online_user";

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		WebApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(arg0.getServletContext()); 
		OnlineService onlineService  = (OnlineService) context.getBean("onlineService"); 
		String url = url_dat;
		URL realUrl;
		StringBuffer sb = new StringBuffer();
		try {
			realUrl = new URL(url);
			HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection(); 
			connection.connect();
			InputStream is = connection.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String s = "";
			while ((s = br.readLine()) != null) {
				sb.append(s);
			}
			br.close();
			isr.close();
			is.close();
			connection.disconnect();
			s = sb.toString();
			if("null".equals(s)||s==null){
				
			}else{
				String [] onlinUsers = s.split(",");
				LogUtil.info("init onlineUser from openfire server:");
				for(int i=0;i<onlinUsers.length;i++){
					String userCode = onlinUsers[i].split("@")[0];
					try {
						IRedisClient redisClient = context.getBean(IRedisClient.class);
						redisClient.set(ONLINE, userCode, onlinUsers[i]);
					} catch (Exception e) {
						LogUtil.info("启动时获取当前在线列表出错。" + LocalDateTime.now());
						e.printStackTrace();
					}
					LogUtil.info("----" + userCode + "----");
				}
				onlineService.changeAllOnlinestatus(onlinUsers, 1);
			}
			XMPPUtils.getInstance();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
