package com.zzb.cm.service;

import java.util.Map;

import com.cninsure.core.dao.BaseService;
import com.cninsure.system.entity.INSCUser;
import com.common.RedisException;
import com.common.WorkFlowException;
import com.zzb.cm.controller.vo.MyTaskVo;


public interface INSBMyTaskService extends BaseService {

	/**
	 * 查询任务信息
	 */
	public Map<String, Object> queryTaskList(MyTaskVo myTaskVo);
	/**
	 *二次支付任务查看支付结果
	 */
	public String getPayResult(String taskid);
	
	/**
	 * 今日完成数量
	 */
	public long countDayTask(MyTaskVo taskVo);
	/**
	 * 当月完成数量
	 */
	public long countMonthTask(MyTaskVo taskVo);
	/**
	 *任务转发 
	 */
	public void transformTask(String instanceId, String providerid,int instanceType, INSCUser fromUser, String tousercode) throws WorkFlowException, RedisException;
	/**
	 * 检查任务是否属于改用户或者是否已经关闭
	 * @param instanceId
	 * @param providerid
	 * @param userCode
	 * @return
	 */
	public boolean checkCloseTask(String instanceId, String providerid, String userCode);
	
}


