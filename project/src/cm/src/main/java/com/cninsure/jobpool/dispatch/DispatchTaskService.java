package com.cninsure.jobpool.dispatch;

import com.cninsure.jobpool.Task;
import com.cninsure.system.entity.INSCUser;
import com.common.WorkFlowException;

public interface DispatchTaskService {
	/**
	 * 获取任务所属的业管组信息通知任务调度增加该任务进组
	 * @param task
	 * @throws WorkFlowException
	 */
	public  void  dispatchTask(Task task)throws WorkFlowException;
	
	/**
	 * 已完成任务,或者cm改派任务，通知调度删除该任务，不再对此类任务进行调度分配
	 * @param task
	 * @throws WorkFlowException
	 */
	public  void  completeTask(Task task, String endTask)throws WorkFlowException;
	
	/**
	 * 通知调度删除该任务
	 * @param task
	 * @throws WorkFlowException
	 */
	public void deleteTask(Task task)throws WorkFlowException;   
	
	/**
	 * 组成员上线通知调度分配组内未处理任务
	 * @param worker
	 * @throws WorkFlowException
	 */
	public  void  userLoginForTask(String worker)throws WorkFlowException;
	/**
	 * 初始化成员在线通知调度分配组内未处理任务
	 * @param worker
	 * @throws WorkFlowException
	 */
	public void usersLoginForTask(String[] userCodes);
	/**
	 * 组成员下线通知调度回该业管手头未处理任务
	 * @param worker
	 * @throws WorkFlowException
	 */
	public  void  userLogoutForTask(String worker)throws WorkFlowException;
	/**
	 * 组成员忙通知调度中心暂停分配任务
	 * @param worker
	 * @throws WorkFlowException
	 */
	public void userBusyForTask(String usercode);
	/**
	 * 任务已经分配到一个业管身上（cm提供给dispatch调用接口）
	 * @param task
	 * return int 
	 */
	public  int  taskDispatched(Task task);
	
	/**
	 * 回收已经分配到一个业管身上的任务（cm提供给dispatch调用接口）
	 * @param task
	 * return int 
	 */
	public  int  reclyDispatched(Task task);
	/**
	 * 回收已经被业管打回的任务
	 * @param task
	 */
	public  void  userRefuseTask(Task task);
	
	public void sendXmppMessage4User(INSCUser fromUser, INSCUser toUser,String msgType);
}
