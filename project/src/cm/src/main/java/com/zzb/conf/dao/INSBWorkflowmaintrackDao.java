package com.zzb.conf.dao;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseDao;
import com.zzb.conf.entity.INSBWorkflowmain;
import com.zzb.conf.entity.INSBWorkflowmaintrack;

public interface INSBWorkflowmaintrackDao extends BaseDao<INSBWorkflowmaintrack> {

	/**
	 * 新增工作流节点轨迹
	 * 
	 * @param model
	 */
	public void insertByMainTable(INSBWorkflowmain model);
	
	
	/**
	 * 更新工作流轨迹表
	 * 
	 * @param model
	 */
	public void updateByMainTable(INSBWorkflowmain model);
	
	
	/**
	 * 根据工作流实例id查询所有轨迹信息
	 * 
	 * 按创建时间倒序排序记录轨迹
	 * 
	 * @param instanceId 工作流实例id
	 * @return
	 */
	public List<Map<String,Object>> selectAllTrack(String instanceId);

	/**
	 * 根据工作流实例id查询所有完成任务的轨迹信息
	 * 
	 * 按创建时间倒序排序记录轨迹
	 * 
	 * @param instanceId 工作流实例id
	 * @return
	 */
	public List<Map<String, Object>> selectAllComplateTrack(String taskid);	
	
	/**
	 * 通过实例id 节点名称判断当前信息
	 * String instanceid,String taskcode
	 * @param instanceid
	 * @return
	 */
	public String selectByInstanceIdTaskCode(Map<String,String> map);
	
	
	/**
	 * 更新轨迹表节点状态
	 * @param taskstate
	 */
	public void updateTaskStatusBylogicId(INSBWorkflowmaintrack model);


	/**
	 * 当日完成任务数
	 * @param map
	 * @return
	 */
	public long countDayTask(Map<String, Object> map);


	/**
	 * 当月完成任务数
	 * @param map
	 * @return
	 */
	public long countMonthTask(Map<String, Object> map);
	/**
	 * 流程图：业管
	 */
	public Map<String, Object> getUserInfo(String taskid);	
	
	/**
	 * 根据流程id得到前端流程状态码
	 * @param mianInstanceId
	 * @param subInstanceId
	 * @return
	 */
	public List<Map<String,String>> selectByInstanceId4h5(String instanceId);
	
	/**
	 * 
	 * @param param  时间限制   工作量限制   按工作量排序
	 * @return
	 */
	public List<Map<String,Object>> selectUserCodesByTimeAndTaskNum4Task(Map<String,Object> param);
	
	
	/**
	 * 所有业管当天处理的单子
	 * @param param
	 * @return
	 */
	public List<Map<String, Object>> selectUserCodesByTimeAndTaskNumAll4Task(Map<String, Object> param);
	
	 
	
	/**
	 * 查询我的历史任务
	 */
	public List<Map<String,Object>> queryMyHistorytask(Map<String,Object> map);
	
	/**
	 * 查询我的历史任务总条数
	 * 
	 */
	public long queryMyHistorytasknum(Map<String,Object> map);
	
	/**
	 * 查询保险inscomcode
	 */
	public Map<String,String> selectbyInstanceId(String instanceid);


    int count21CodeByTaskIdAndInscomcode(Map<String, String> paraMap);

}