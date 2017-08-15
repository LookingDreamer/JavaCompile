package com.zzb.conf.dao;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseDao;
import com.zzb.conf.entity.INSBWorkflowsub;

public interface INSBWorkflowsubDao extends BaseDao<INSBWorkflowsub> {

	
	/**
	 * 根据实例id得到子流程信息
	 * 
	 * @param instanceid
	 * @return id,createby,taskid,taskcode
	 */
	public INSBWorkflowsub selectByInstanceId(String instanceid);
	
	/**
	 * @param instanceid
	 * @return
	 */
	public INSBWorkflowsub selectModelByInstanceId(String instanceid);

	/**
	 * @param instanceid
	 * @return Map 包含 INSBWorkflowsub 所有属性 和 对应的inscomcode
	 */
	public Map<String, Object> selectModelInfoByInstanceId(String instanceid);
	
	/**
	 * 
	 * 根据实例id删除流程节点状态
	 * @param instanceid
	 */
	public void deleteByInstanceId(String instanceid);
	
	/**
	 * 通过主流程id查询子流程id 
	 * @param instanceId
	 * @return
	 */
	public List<String> selectInstanceIdByMainInstanceId(String instanceId);
	
	
	/**
	 * 通过主流程id查询子流程信息
	 * @param instanceId
	 * @return
	 */
	public List<INSBWorkflowsub> selectSubModelByMainInstanceId(String instanceId);

	public List<Map<String, Object>> selectSubModelInfoByMainInstanceId(String mainInstanceId);
	
	/**
	 * 通过业管id查询传入业管处理任务数
	 * 
	 * @return
	 */
	public List<Map<String,Object>> selectOperatorCount(List<String> userIds);

	/**
	 * 根据taskid查询报价阶段的报价信息
	 * @param processInstanceId
	 * @return
	 */
	public List<Map<String, Object>> getQuoteInfoByTaskId(String processInstanceId);
	
	/**
	 * 根据taskid查询报价阶段的报价信息--渠道
	 * @param processInstanceId
	 * @return
	 */
	public List<Map<String, Object>> getQuoteInfoByTaskIdForChn(String processInstanceId);
	
	/**
	 * 车险任务查看任务流程
	 */
	public List<Map<String, Object>> showWorkflowTrack(Map<String, String> params);
	
	/**
	 * 得到流程示意图中报价节点的流程信息
	 */
	public List<Map<String, Object>> getSubWorkflowViewInfo(Map<String, Object> params);
	
	/**
	 * TODO
	 * 
	 * @param param
	 * @return
	 */
	public List<String> selectUserCodesByTimeAndTaskNum4Task(Map<String,Object> param);

	/**
	 * 获取二次支付任务的支付信息
	 * @param maininstanceid
	 * @return
	 */
	public Map<String, Object> getMediumPayInfo(Map<String, Object> map);
	
	
	/**
	 * 
	 * 通过子流程id得到主流程id
	 * @param subInstanceId
	 * @return
	 */
	public String selectMainInstanceIdBySubInstanceId(String subInstanceId);

	/**
	 * 得到子流程中进入各节点的时间
	 */
	public List<String> getInTaskcodeDate(Map<String, Object> params);
	
	/**
	 * 
	 * 通过子实例id得到uuid
	 * @param instanceid
	 * @return
	 */
	public Map<String,String> selectByInstanceId4Task(String instanceid);
	
	/**
	 * 精灵报价成功后回写数据前核对子流程节点状态 liu,解决精灵报价超时问题
	 * @param maininstanceid 主流程id
	 * @param inscomcode 保险公司code
	 * @return  taskcode 当前节点code，codename 当前节点名称
	 * taskcode字典：
	 * 3-EDI报价；4-精灵报价；32-规则报价；6-人工调整；7-人工规则报价；8-人工报价；13-报价退回；14-选择投保；31-人工回写；
	 * 16-EDI核保；17-精灵核保；18-人工核保；19-核保退回；
	 */
	public Map<String,String> getCurrentTaskcodeOfSubFlow(String maininstanceid, String inscomcode);

	public String getInstanceidByMaininstanceId(String maininstanceid);
	
	/**
	 * 获取正在执行的 子流程  没有 关闭 ，取消，拒绝
	 * @param maininstanceid
	 * @return
	 */
	public List<INSBWorkflowsub> selectSubInsIdExc(String maininstanceid);
	
	/**
	 * 得到正常子流程
	 * @param maininstanceid
	 * @return
	 */
	public List<String> selectSubInstanceIdByEnd(String maininstanceid);
	
	/**
	 * 通过主流程id的到主流程当前节点的名称 liuchao
	 */
	public String getTaskNameByMainInstanceId(String mainInstanceId);
	
	/**
	 * 通过子流程id的到子流程当前节点的名称 liuchao
	 */
	public String getTaskNameBySubInstanceId(String subInstanceId);

	public String getTransformTaskInstanceid(Map<String, String> map);
	
	/**
	 * 通过群组id得到当前未分配人的任务
	 * 
	 * @return
	 */
	public List<INSBWorkflowsub> getDataByGroupId4UserLogin(List<String> groupId);
	
	
	/**
	 * 得到所有 应该入池的任务数据 同步redis
	 * 7，8，18，21，27，23，24
	 * 
	 * @return
	 */
	public List<INSBWorkflowsub> getAllPoolData4Syn();

	/**\
	 * 查询超期关闭流程
	 * @return
     */
	public List<Map> selectExpiredClose();

	void updateBatch(List<INSBWorkflowsub> list);

	/**
	 * 仅供个人使用
	 * 查询处于支付状态的最新一条数据
	 */
	public INSBWorkflowsub querybymaininstanceid(String maininstanceid);
	
	/**
	 * 查询sub表中taskcode=33的数据,通过maininstanceid
	 * @param maininstanceid
	 * @return
	 */
	public INSBWorkflowsub queryBymInstanceid(String maininstanceid);

	public int updateUnderwritingsuccesstype(String underwritingsuccesstype, String id);
}