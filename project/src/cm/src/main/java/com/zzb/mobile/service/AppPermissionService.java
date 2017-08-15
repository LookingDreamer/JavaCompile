package com.zzb.mobile.service;

import com.cninsure.core.dao.BaseService;
import com.zzb.cm.entity.INSBOrder;
import com.zzb.conf.controller.vo.AgentRelatePermissionVo;
import com.zzb.conf.entity.INSBPermission;
import com.zzb.mobile.model.CommonModel;
import com.zzb.mobile.model.lastinsured.LastYearPolicyInfoBean;

import java.util.List;
import java.util.Map;

public interface AppPermissionService {
	/**
	 *
	 * @param jobnum
	 * @param taskid
	 * @param permission
	 * @return  权限结果  0 有权限，1 预警，2 无权限，-1 错误
	 */
	Map<String,Object> checkPermission(String jobnum, String taskid, String permission);
	/**
	 *
	 * @param jobnum
	 * @param taskid
	 * @param permission
	 * @return  权限结果  0 有权限，1 预警，2 无权限，-1 错误
	 */
	Map<String,Object> findPermission(String jobnum, String taskid, String permission);
	boolean resetPermission(String jobnum);

	/**
	 * 根据大数据返回结果更新权限使用次数
	 * @param lastYearPolicyInfoBean
	 * @return
     */
	void updatePermission(LastYearPolicyInfoBean lastYearPolicyInfoBean, List<AgentRelatePermissionVo> permissionVoList, String taskid);

}
