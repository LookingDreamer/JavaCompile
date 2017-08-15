package com.cninsure.jobpool;

import com.cninsure.core.utils.LogUtil;
import com.common.redis.CMRedisClient;

/**
 * 池对象 完成对工作池 遍历 排序 获取 添加  
 * 
 * @author hxx
 *
 */
public class Pool {

	/**
	 * 任务池 名称前缀
	 */
	public static final String MODULE = "cm:job_pool:flow";
	private static int RedisTaskExpire = 7*24*3600;

	/**
	 * 新增修改订单任务
	 * 
	 * @param task 任务
	 */
	public static boolean addOrUpdate(Task task) {
		LogUtil.info("===新增任务---开始---task="+task);
		if(task.getSonProInstanceId()!=null&&!"".equals(task.getSonProInstanceId())){
			CMRedisClient.getInstance().set(MODULE, task.getSonProInstanceId(), task, RedisTaskExpire);
			LogUtil.info("===新增任务---子流程---成功---maintaskid="+task.getProInstanceId()+"---prv="+task.getPrvcode()+"---task="+task);
			return true;
		}else if(null!=task.getProInstanceId()){
			CMRedisClient.getInstance().set(MODULE, task.getProInstanceId(), task, RedisTaskExpire);
			LogUtil.info("===新增任务---主流程---成功---maintaskid="+task.getProInstanceId()+"---prv="+task.getPrvcode()+"---task="+task);
			return true;
		}else{
			LogUtil.info("===新增任务---主流程为null---失败---task="+task);
			return false;
		}
	}
	/**
	 * 删除订单任务
	 * 
	 * @param proInstanceId 主流程id
	 * @param sonProInstanceId 子流程id
	 * @param prvcode
	 */
	public static boolean del(String proInstanceId,String sonProInstanceId ,String prvcode) {
		LogUtil.info("===删除任务---开始---proInstanceId="+proInstanceId+"---sonProInstanceId="+sonProInstanceId);
		if(null!=sonProInstanceId){
			CMRedisClient.getInstance().del(MODULE, sonProInstanceId);
			LogUtil.info("===删除任务---子流程任务---成功---maintaskid="+proInstanceId+"---sonProInstanceId="+sonProInstanceId);
			return true;
		}else if(proInstanceId!=null){
			CMRedisClient.getInstance().del(MODULE, proInstanceId);
			LogUtil.info("===删除任务---主流程任务---成功---maintaskid="+proInstanceId+"---sonProInstanceId="+sonProInstanceId);
			return true;
		}else{
			LogUtil.info("===删除任务---主流程为null---成功---proInstanceId="+proInstanceId+"---sonProInstanceId="+sonProInstanceId);
			return false;
		}
	}
	
	/**
	 * 查询订单任务
	 * 
	 * @param proInstanceId
	 * @param sonProInstanceId
	 * @param prvcode
	 * @return
	 */
	public  static Task get(String proInstanceId,String sonProInstanceId,String prvcode) {
		LogUtil.info("===查询任务---开始---proInstanceId="+proInstanceId+"---sonProInstanceId="+sonProInstanceId);
		Task task = null;
		if(null!=sonProInstanceId){
			task =  CMRedisClient.getInstance().get(MODULE, sonProInstanceId, Task.class);
			LogUtil.info("===查询任务---子流程任务---task="+task);
			return task;
		}else if(null!=proInstanceId){
			task =  CMRedisClient.getInstance().get(MODULE, proInstanceId, Task.class);
			LogUtil.info("===查询任务---主流程任务---task="+task);
			return task;
		}else{
			return null;
		}
	}
}
