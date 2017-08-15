package com.zzb.conf.service;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseService;
import com.zzb.conf.entity.INSBWorkflowsubtrack;

public interface INSBWorkflowsubtrackService extends BaseService<INSBWorkflowsubtrack> {

	public Map<String,Object> getMyTaskLastNode(String maininstanceid,String instanceid);

	/**
	 * 通过主流程实例id和子流程id和任务状态查询子任务轨迹
	 * */
	public INSBWorkflowsubtrack getSubTrack(String maininstanceid, String subinstanceid, String taskcode);
	
	/**
	 * 通过主流程实例id和保险公司code查询子任务轨迹
	 * */
	public INSBWorkflowsubtrack getSubTrackByInscomcode(String maininstanceid, String inscomcode);
	/**
	 *流程图：报价 
	 */
	public List<Map<String, Object>> getQuoteInfo(String taskid); 
	
	/**
	 * 打开任务，记录打开任务轨迹
	 * @param param
	 * @throws Exception
	 */
	public void addTrackdetail(INSBWorkflowsubtrack param, String operator);
	
	/**
	 * 区分主流程子流程
	 * 主流程批量关闭
	 * 子流程只关闭自己
	 * 
	 * @param param mainTaskId	subTaskId taskCode
	 * @return
	 */
	public boolean addTracks4EndOrClose(Map<String,String> param);
	
	/**
	 * 查询含有taskcodelist 轨迹信息
	 * @param map
	 * @return
	 */
	public List<INSBWorkflowsubtrack> selectByTaskcodeList(Map<String, Object> map);
}