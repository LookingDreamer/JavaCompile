package com.cninsure.system.service;

import java.util.List;
import java.util.Map;

/**
 * @author hlj
 * @date 14:40 2015/6/12
 *
 */
public interface INSCResourceRoleService {
	/**
	 * 获得资源和角色的对应关系信息
	 * 
	 * @return
	 */
	public Map<String, List<String>> getResourceRolesMap();
}
