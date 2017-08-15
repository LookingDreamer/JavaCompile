package com.zzb.conf.service;

import com.cninsure.core.dao.BaseService;
import com.cninsure.system.entity.INSCUser;
import com.zzb.conf.entity.INSBGroupdept;

	public interface INSBGroupdeptService extends BaseService<INSBGroupdept> {
	
	/**
	 * 保存网点信息
	 * 
	 * @param groupid
	 * @param deptid
	 * @param grade
	 */
	public void saveGroupWangdata(INSCUser user,String groupid, String deptid,String grade);

}