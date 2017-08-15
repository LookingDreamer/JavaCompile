package com.zzb.conf.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.conf.dao.INSBWorkflowsubDao;
import com.zzb.conf.entity.INSBWorkflowsub;

@Repository
public class INSBWorkflowsubDaoImpl extends BaseDaoImpl<INSBWorkflowsub> implements
		INSBWorkflowsubDao {

	@Override
	public INSBWorkflowsub selectByInstanceId(String instanceid) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectIdByInstanceId"), instanceid);
	}

	@Override
	public void deleteByInstanceId(String instanceid) {
		this.sqlSessionTemplate.delete(this.getSqlName("deleteByInstanceId"), instanceid);
	}

	@Override
	public List<String> selectInstanceIdByMainInstanceId(String maininstanceid) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectInstanceIdByMainInstanceId"),maininstanceid);
	}
	
	@Override
	public List<INSBWorkflowsub> selectSubInsIdExc(String maininstanceid) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectSubInsIdExc"),maininstanceid);
	}
	
	@Override
	public List<String> selectSubInstanceIdByEnd(String maininstanceid) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectSubInstanceIdByEnd"),maininstanceid);
	}
	@Override
	public List<Map<String, Object>> selectOperatorCount(List<String> userIds) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectOperatorCount"), userIds);
	}

	@Override
	public INSBWorkflowsub selectModelByInstanceId(String instanceid) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectModelByInstanceId"), instanceid);
	}

	public Map<String, Object> selectModelInfoByInstanceId(String instanceid) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectModelInfoByInstanceId"), instanceid);
	}

	@Override
	public List<Map<String, Object>> getQuoteInfoByTaskId(String processInstanceId) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("getQuoteInfoByTaskId"), processInstanceId);
	}
	
	@Override
	public List<Map<String, Object>> getQuoteInfoByTaskIdForChn(String processInstanceId) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("getQuoteInfoByTaskIdForChn"), processInstanceId);
	}

	/**
	 * 车险任务查看任务流程
	 */
//	@Override
//	public List<Map<String, Object>> showWorkflowTrack(Map<String, String> params){
//		return this.sqlSessionTemplate.selectList(this.getSqlName("showWorkflowTrack"), params);
//	}
//	
	/**
	 * 车险任务查看任务流程-最新版
	 * hxx 2016-05-28 还原 没有主流程数据sql 错误
	 */
	@Override
	public List<Map<String, Object>> showWorkflowTrack(Map<String, String> params){
		return this.sqlSessionTemplate.selectList(this.getSqlName("showWorkflowTrack1"), params);
	}
	
	/**
	 * 得到流程示意图中报价节点的流程信息
	 */
	@Override
	public List<Map<String, Object>> getSubWorkflowViewInfo(Map<String, Object> params){
		return this.sqlSessionTemplate.selectList(this.getSqlName("getSubWorkflowViewInfo"), params);
	}

	@Override
	public List<String> selectUserCodesByTimeAndTaskNum4Task(
			Map<String, Object> param) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectUserCodesByTimeAndTaskNum4Task"), param);
	}

	@Override
	public Map<String, Object> getMediumPayInfo(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("getMediumPayInfo"), map);
	}

	@Override
	public String selectMainInstanceIdBySubInstanceId(String instanceid) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectMainInstanceIdBySubInstanceId"), instanceid);
	}

	/**
	 * 得到子流程中进入各节点的时间
	 */
	@Override
	public List<String> getInTaskcodeDate(Map<String, Object> params) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("getInTaskcodeDate"), params);
	}

	@Override
	public Map<String, String> selectByInstanceId4Task(String instanceid) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectByInstanceId4Task"), instanceid);
	}

	/**
	 * 精灵报价成功后回写数据前核对子流程节点状态 liu,解决精灵报价超时问题
	 * @param maininstanceid 主流程id
	 * @param inscomcode 保险公司code
	 * @return  taskcode 当前节点code，codename 当前节点名称
	 * taskcode字典：
	 * 3-EDI报价；4-精灵报价；32-规则报价；6-人工调整；7-人工规则报价；8-人工报价；13-报价退回；14-选择投保；31-人工回写；
	 * 16-EDI核保；17-精灵核保；18-人工核保；19-核保退回；
	 */
	@Override
	public Map<String, String> getCurrentTaskcodeOfSubFlow(
			String maininstanceid, String inscomcode) {
		Map<String, String> params = new HashMap<String, String>();
		if(StringUtils.isEmpty(maininstanceid)){
			return params;
		}
		if(StringUtils.isEmpty(inscomcode)){
			return params;
		}
		params.put("maininstanceid", maininstanceid);
		params.put("inscomcode", inscomcode);
		return this.sqlSessionTemplate.selectOne(this.getSqlName("getCurrentTaskcodeOfSubFlow"), params);
	}

	@Override
	public String getInstanceidByMaininstanceId(String maininstanceid) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("getInstanceidByMaininstanceId"), maininstanceid);
	}

	/**
	 * 通过主流程id的到主流程当前节点的名称 liuchao
	 */
	@Override
	public String getTaskNameByMainInstanceId(String mainInstanceId) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("getTaskNameByMainInstanceId"), mainInstanceId);
	}

	/**
	 * 通过子流程id的到子流程当前节点的名称 liuchao
	 */
	@Override
	public String getTaskNameBySubInstanceId(String subInstanceId) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("getTaskNameBySubInstanceId"), subInstanceId);
	}

	@Override
	public String getTransformTaskInstanceid(Map<String, String> map) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("getTransformTaskInstanceid"), map);
	}
	
	@Override
	public List<INSBWorkflowsub> getDataByGroupId4UserLogin(List<String> groupId) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("getDataByGroupId4UserLogin"), groupId);
	}

	@Override
	public List<INSBWorkflowsub> getAllPoolData4Syn() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * \
	 * 查询超期关闭流程
	 *
	 * @return
	 */
	@Override
	public List<Map> selectExpiredClose() {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectExpiredClose"));
	}

	@Override
	public void updateBatch(List<INSBWorkflowsub> list) {
		if (list == null || list.isEmpty()) {
			return;
		}

		int batchCount = 1000;// 每批commit的个数
		int batchLastIndex = batchCount;// 每批最后一个的下标
		for (int index = 0; index < list.size();) {
			if (batchLastIndex > list.size()) {
				batchLastIndex = list.size();
				this.sqlSessionTemplate.update(this.getSqlName("updateBatch"), list.subList(index, batchLastIndex));
				break;// 数据插入完毕,退出循环

			} else {
				this.sqlSessionTemplate.update(this.getSqlName("updateBatch"), list.subList(index, batchLastIndex));
				index = batchLastIndex;// 设置下一批下标
				batchLastIndex = index + batchCount;
			}
		}
	}


	@Override
	public List<INSBWorkflowsub> selectSubModelByMainInstanceId(String maininstanceid) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectSubModelByMainInstanceId"), maininstanceid);
	}

	public List<Map<String, Object>> selectSubModelInfoByMainInstanceId(String mainInstanceId) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectSubModelInfoByMainInstanceId"), mainInstanceId);
	}

	/**
	 * 查询子流程处于支付状态的最新一条数据,通过maininstanceid
	 */
	@Override
	public INSBWorkflowsub querybymaininstanceid(String maininstanceid) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectbymaininstanceid"));
	}
	
	/**
	 * 查询子流程taskcode='33'的数据,通过maininstanceid
	 */
	@Override
	public INSBWorkflowsub queryBymInstanceid(String maininstanceid){
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectBymInstanceid"));
	}

	public int updateUnderwritingsuccesstype(String underwritingsuccesstype, String id) {
		Map<String, String> param = new HashMap<>(2);
		param.put("underwritingsuccesstype", underwritingsuccesstype);
		param.put("id", id);
		return this.sqlSessionTemplate.update(this.getSqlName("updateUnderwritingsuccesstype"), param);
	}
}