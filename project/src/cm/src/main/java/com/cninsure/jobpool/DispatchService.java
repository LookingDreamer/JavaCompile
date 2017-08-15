package com.cninsure.jobpool;

import com.cninsure.system.entity.INSCUser;
import com.common.RedisException;
import com.common.WorkFlowException;



/**
 * 任务部分对外接口
 * 
 * @author Administrator
 *
 */
public interface DispatchService {
	
	 
	/**
	 * 出池
	 * @param task
	 */
	public void removTaskFromPool(Task task);
	
	
	/**
	 * 
	 * 回收
	 * @param task
	 * @param recycleUser
	 * @return
	 */
	public Task recycle(Task task, INSCUser recycleUser);
	
	/**
	 * 群补分配
	 * @param task
	 * @throws WorkFlowException
	 */
	public  void  dispatchAll(Task task)throws WorkFlowException;

	 /* 
	 * 重启调度
	 * 需要回收
	 * 
	 * @param instanceId 实例id
	 * @param providerId 供应商
	 * @param userCode 操作人
	 * @param instanceType 实例类型
	 */
	public void restartTask(String instanceId, String providerId,String userCode, int instanceType,String fromOperator)throws WorkFlowException, RedisException;
	
	
	/**
	 * 
	 * 暂停调度
	 * 
	 * @param instanceId
	 * @param providerId
	 * @param instanceType  1:主 2：子
	 */
	public  void pauseTask(String instanceId, String providerId, int instanceType,String fromOperator)throws RedisException;
	
	

	/**
	 * 领取任务
	 * 
	 * 需要回收
	 * 
	 * TODO  更新工作流状态表
	 * 
	 * @param instanceId
	 * @param providerId
	 * @param instanceType
	 * @param fromUser 操作人
	 * @param toUser 目标人
	 * @throws RedisException 
	 */
	public String getTask(String instanceId, String providerId,int instanceType, INSCUser fromUser, INSCUser toUser)throws WorkFlowException, RedisException;
	
	
	/**
	 * 改派任务
	 * 
	 * 需要回收
	 * 
	 * TODO  更新工作流状态表
	 * 
	 * @param instanceId
	 * @param providerId
	 * @param instanceType
	 * @param fromUser 操作人
	 * @param toUser 目标人
	 */
	public void reassignment(String instanceId, String providerId,int instanceType, INSCUser fromUser, INSCUser toUser)throws WorkFlowException, RedisException;
	
	
	
	
	/**
	 * 改派未入池任务
	 * 
	 * @param instanceId
	 * @param providerId
	 * @param instanceType
	 * @param fromUser
	 * @param toUser
	 * @throws WorkFlowException
	 */
	public void reassignment4NoRedis(String instanceId, String providerId,int instanceType, INSCUser fromUser, INSCUser toUser)throws WorkFlowException;
	
	
	/**
	 * 拒绝任务   
	 * 
	 * 需要回收
	 * 
	 * TODO 更新工作流状态表
	 * 
	 * @param instanceId
	 * @param instanceType 1:主 2：子
	 * @param user
	 */
	public void refuseTask(String instanceId,String providerId,int instanceType,INSCUser user)throws WorkFlowException , RedisException;

	
	
	/**
	 * 
	 * 核心反向关闭订单
	 * @param mainInstanceId 主流程
	 * @param instanceid 子流程
	 */
	public void completedAllTaskByCore(String mainInstanceId,String instanceid);
	
	
	/**
	 * 
	 * 主流程关闭 关闭所有子流程
	 * @param subinstanceid
	 * @param status
	 */
	public  void closeSubTask(String subinstanceid,String status);

	/**
	 * 当redis为空时查询数据库
	 *
	 * @param mainId 主流程
	 * @param subId 子流程
	 * @param prv 供应商
	 */
	public Task getDataFromDb(String mainId,String subId, String prv);
}
