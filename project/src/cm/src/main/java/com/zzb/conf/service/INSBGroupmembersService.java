package com.zzb.conf.service;

import com.cninsure.core.dao.BaseService;
import com.zzb.conf.entity.INSBGroupmembers;

public interface INSBGroupmembersService extends BaseService<INSBGroupmembers> {
	/**
	 * 查询用户是否存在于可执行的业管组里
	 * @param userId
	 * @param task
	 * @return
	 */
	public boolean queryUserGroupPrivileges(String userId,String task);
}