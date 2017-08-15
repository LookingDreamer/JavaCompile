package com.zzb.mobile.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.ResourceBundle;
/**
 * 获取ldap的配置文件
 * @author yuzhang
 *
 */
public class ConfigUtil {
	public static void main(String[] args) {
		System.out.println(config);
		System.out.println(ConfigUtil.get("ldap.loginDN"));
		System.out.println(Integer.parseInt(" 123 ".trim()));

	}
	
	/**
	 * 配置文件
	 */
	private static ResourceBundle config = ResourceBundle.getBundle("config/ldap");
	
	
	/**
	 * 内存中的配置文件
	 */
	private static Properties properties = new Properties();
	
	
	public final static String get(String key, String defaultValue) {
		String value = config.getString(key);
		return null == value ? defaultValue : value;
	}
	
	public final static String get(String key) {
		return get(key, null);
	}	
}
