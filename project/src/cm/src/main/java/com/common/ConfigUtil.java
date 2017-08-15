package com.common;

import java.io.IOException;
import java.util.Properties;

public class ConfigUtil {
	private static Properties prop = new Properties();

	static {
		try {
			prop.load(ConfigUtil.class.getClassLoader().getResourceAsStream(
					"config/config.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getPropString(String key, String defaultValue) {
		String value = null;
		if (key != null && prop.containsKey(key)) {
			value = prop.getProperty(key);
		}
		return (value == null) ? defaultValue : value;
	}

	public static String getPropString(String key) {
		return prop.getProperty(key);
	}

	public static Integer getPropInteger(String key, Integer defaultValue) {
		String value = getPropString(key);
		return ((value == null || "".equals(value.trim())) ? defaultValue
				: Integer.valueOf(value));
	}

	public static Integer getPropInteger(String key) {
		String value = getPropString(key);
		if (value != null) {
			try {
				return Integer.valueOf(value);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return null;
	}
}
