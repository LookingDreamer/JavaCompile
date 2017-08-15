package com.zzb.cm.Interface.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import com.cninsure.core.utils.LogUtil;
import com.common.ConfigUtil;

public class IpUtil {
	private static Properties prop = new Properties();

	static {
		try {
			prop.load(ConfigUtil.class.getClassLoader().getResourceAsStream(
					"config/ips.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	};
	//对IP是否存在进行封装？
	public static Boolean isIpExist(HttpServletRequest req)
	{	 
		 String ipstr= prop.getProperty("ips");
		 String[] ips = ipstr.split(",");
		 List<String> iplist = Arrays.asList(ips); 
		 String ip = req.getHeader("x-forwarded-for");
	     if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {      
	         ip = req.getHeader("Proxy-Client-IP");      
	     }      
	     if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {      
	         ip = req.getHeader("WL-Proxy-Client-IP");      
	     }      
	     if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {      
	         ip = req.getRemoteAddr();      
	     }
	     if(iplist.contains(ip)){
	    	 return true;
	     }
	     return false;
	}

	
/**
 * 获取访问用户的客户端IP（适用于公网与局域网）.
 */
public static final String getIpAddr(final HttpServletRequest request)
        throws Exception {
    if (request == null) {
        throw (new Exception("getIpAddr method HttpServletRequest Object is null"));
    }
    String ipString = request.getHeader("x-forwarded-for");
    if (StringUtils.isBlank(ipString) || "unknown".equalsIgnoreCase(ipString)) {
        ipString = request.getHeader("Proxy-Client-IP");
    }
    if (StringUtils.isBlank(ipString) || "unknown".equalsIgnoreCase(ipString)) {
        ipString = request.getHeader("WL-Proxy-Client-IP");
    }
    if (StringUtils.isBlank(ipString) || "unknown".equalsIgnoreCase(ipString)) {
        ipString = request.getRemoteAddr();
    }
 
    // 多个路由时，取第一个非unknown的ip
    final String[] arr = ipString.split(",");
    for (final String str : arr) {
        if (!"unknown".equalsIgnoreCase(str)) {
            ipString = str;
            break;
        }
    }
 
    return ipString;
}
}
