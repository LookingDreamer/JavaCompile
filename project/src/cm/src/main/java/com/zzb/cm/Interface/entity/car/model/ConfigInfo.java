package com.zzb.cm.Interface.entity.car.model;

import java.util.HashMap;
import java.util.Map;

public class ConfigInfo {

	Map<String,String> configMap = new HashMap<String,String>();

	public Map<String, String> getConfigMap() {
		return configMap;
	}

	public void setConfigMap(Map<String, String> configMap) {
		this.configMap = configMap;
	}
	
}
