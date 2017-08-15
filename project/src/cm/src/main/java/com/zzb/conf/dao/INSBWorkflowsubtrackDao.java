package com.zzb.conf.dao;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseDao;
import com.zzb.conf.entity.INSBWorkflowsub;
import com.zzb.conf.entity.INSBWorkflowsubtrack;
 
public interface INSBWorkflowsubtrackDao extends BaseDao<INSBWorkflowsubtrack> {

	/**
	 * 新增工作流节点轨迹
	 * 
	 * @param model
	 */
	public void insertBySubTable(INSBWorkflowsub model);

	/**
	 * 批量新增工作流节点轨迹
	 *
	 * @param list
	 */
	public void insertBatchBySubTable(List<INSBWorkflowsub> list);

	
	/**
	 * 工作流轨迹更新操作  使用状态表model
	 * @param model
	 */
	public void updateySubTable(INSBWorkflowsub model);
	
	
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
	 * 通过实例id 节点名称判断当前信息
	 * String instanceid,String taskcode
	 * @param instanceid
	 * @return
	 */
	public INSBWorkflowsubtrack selectByInstanceIdTaskCode(Map<String,String> map);
	
	/**
	 * 更新轨迹表节点状态
	 * @param taskstate
	 */
	public void updateTaskStatusBylogicId(INSBWorkflowsubtrack model);
	
	/**
	 * 流程图：报价
	 */
	public List<Map<String,Object>> getQuoteInfo(String taskid);	
	
	public List<Map<String,String>> selectByInstanceId4h5(
			Map<String,String> param);
	
	/**
	 * 
	 * @param param  时间限制   未完成工作量   按工作量排序
	 * @return
	 */
	public List<Map<String,Object>> selectUserCodesByTimeAndTaskNum4Task(Map<String,Object> param);
	
	/**
	 * 
	 * @param param  时间限制   所有工作量   按工作量排序
	 * @return
	 */
	public List<Map<String,Object>> selectUserCodesByTimeAndTaskNumAll4Task(Map<String,Object> param);

	/**
	 * 判断是否有二次支付轨迹
	 * @param maininstanceid
	 * @param instanceid
	 * @return
	 */
	public boolean isThereSecondPayment(String maininstanceid, String instanceid);

	public INSBWorkflowsubtrack findPrevWorkFlowSub(String instanceid,
			String taskcode);

	public INSBWorkflowsubtrack selectOneByTaskidAndInscomcode(
			Map<String, Object> map);
	
	public List<INSBWorkflowsubtrack> getWorkflowsubBySubId(String instanceid);

	List<INSBWorkflowsubtrack> selectLockConf(Map<String, Object> map);
	/**
	 * 获取保险公司和主id(inscomcode  maininstanceid)
	 * xiaojiaming
	 * @param instanceid
	 * @return
	 */
	public Map<String,String> selectbyInstanceId(String instanceid);
	
	/**
	 * 查询含有taskcodelist 轨迹信息
	 * @param map
	 * @return
	 */
	public List<INSBWorkflowsubtrack> selectByTaskcodeList(Map<String, Object> map);

}