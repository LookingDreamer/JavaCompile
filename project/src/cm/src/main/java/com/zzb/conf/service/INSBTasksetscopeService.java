package com.zzb.conf.service;

import java.util.Map;

import com.cninsure.core.dao.BaseService;
import com.zzb.conf.entity.INSBTasksetscope;

public interface INSBTasksetscopeService extends BaseService<INSBTasksetscope> {
	/**
	 * 通过网点和任务组id得到关联的数量
	 * @param map
	 * @return
	 */
	public int selectScopListCountByDeptid(Map<String,Object> map);
}