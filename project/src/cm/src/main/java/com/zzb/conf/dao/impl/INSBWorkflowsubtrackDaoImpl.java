package com.zzb.conf.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.cninsure.core.utils.StringUtil;
import com.cninsure.core.utils.UUIDUtils;
import com.zzb.conf.dao.INSBWorkflowsubtrackDao;
import com.zzb.conf.entity.INSBWorkflowsub;
import com.zzb.conf.entity.INSBWorkflowsubtrack;
 
@Repository
public class INSBWorkflowsubtrackDaoImpl extends BaseDaoImpl<INSBWorkflowsubtrack> implements
		INSBWorkflowsubtrackDao {

	@Override
	public void insertBySubTable(INSBWorkflowsub model) {
		String id = UUIDUtils.random();
		model.setId(id);
		if(StringUtil.isEmpty(model.getOperator())){
			model.setOperator("admin");
        }
		if(StringUtil.isEmpty(model.getFromoperator())){
			model.setFromoperator("admin");
        }  
		this.sqlSessionTemplate.insert(this.getSqlName("insertBySubTable"),model);
	}

	/**
	 * 批量新增工作流节点轨迹
	 *
	 * @param list
	 */
	@Override
	public void insertBatchBySubTable(List<INSBWorkflowsub> list) {
		if (list == null || list.isEmpty()) {
			return;
		}

		int batchCount = 1000;// 每批commit的个数
		int batchLastIndex = batchCount;// 每批最后一个的下标
		for (int index = 0; index < list.size();) {
			if (batchLastIndex > list.size()) {
				batchLastIndex = list.size();
				this.sqlSessionTemplate.insert(this.getSqlName("insertBatchBySubTable"), list.subList(index, batchLastIndex));
				break;// 数据插入完毕,退出循环

			} else {
				this.sqlSessionTemplate.insert(this.getSqlName("insertBatchBySubTable"), list.subList(index, batchLastIndex));
				index = batchLastIndex;// 设置下一批下标
				batchLastIndex = index + batchCount;
			}
		}
	}

	@Override
	public List<Map<String,Object>> selectAllTrack(String instanceid) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectAllTrackByInstanceId"), instanceid);
	}

	@Override
	public INSBWorkflowsubtrack selectByInstanceIdTaskCode(Map<String, String> map) {
		List<INSBWorkflowsubtrack> workflowsubtrackList = this.sqlSessionTemplate.selectList(this.getSqlName("selectByInstanceIdTaskCode"), map);
		if(null!=workflowsubtrackList && workflowsubtrackList.size()>0){
			return workflowsubtrackList.get(0);
		}else {
			return null;
		}
	}

	@Override
	public void updateTaskStatusBylogicId(INSBWorkflowsubtrack model) {
		this.sqlSessionTemplate.update(this.getSqlName("updateTaskStatusBylogicId"), model);
	}

	@Override
	public List<Map<String, Object>> getQuoteInfo(String taskid) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("getQuoteInfo"), taskid);
	}

	@Override
	public void updateySubTable(INSBWorkflowsub model) {
		this.sqlSessionTemplate.update(this.getSqlName("updateySubTable"), model);
	}
	
	@Override
	public List<Map<String,String>> selectByInstanceId4h5(
			Map<String,String> param) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectByInstanceId4h5"), param);
	}

	@Override
	public List<Map<String,Object>> selectUserCodesByTimeAndTaskNum4Task(Map<String, Object> param) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectUserCodesByTimeAndTaskNum4Task"), param);
	}

	@Override
	public boolean isThereSecondPayment(String maininstanceid, String instanceid) {
		INSBWorkflowsubtrack temp = new INSBWorkflowsubtrack();
		temp.setMaininstanceid(maininstanceid);
		temp.setInstanceid(instanceid);
		temp.setTaskcode("21");
		//INSBWorkflowsubtrack obj = selectOne(temp);
		List<INSBWorkflowsubtrack> objs = selectList(temp);
		if(null != objs&&objs.size()>0){
			return true;
		}else{
			return false;
		}
	}

	@Override
	public List<Map<String, Object>> selectUserCodesByTimeAndTaskNumAll4Task(
			Map<String, Object> param) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectUserCodesByTimeAndTaskNumAll4Task"), param);
	}
	@Override
	public INSBWorkflowsubtrack findPrevWorkFlowSub(String instanceid,
			String thistaskcode) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("instanceid",instanceid);
		param.put("taskcode",thistaskcode);
		List<INSBWorkflowsubtrack> list= this.sqlSessionTemplate.selectList(this.getSqlName("findPrevWorkFlowSub"), param);
		for (INSBWorkflowsubtrack insbWorkflowsubtrack : list) {
			String taskcode = insbWorkflowsubtrack.getTaskcode();
			if("3".equals(taskcode)||"4".equals(taskcode)||"32".equals(taskcode)
					||"6".equals(taskcode)||"7".equals(taskcode)||"8".equals(taskcode)){
				return insbWorkflowsubtrack;
			}
		}
		return null;
	}

	@Override
	public INSBWorkflowsubtrack selectOneByTaskidAndInscomcode(
			Map<String, Object> map) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectOneByTaskidAndInscomcode"), map);
	}
	
	@Override
	public List<INSBWorkflowsubtrack> getWorkflowsubBySubId(String instanceid) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("getWorkflowsubBySubId"), instanceid);
	}

	@Override
	public List<INSBWorkflowsubtrack> selectLockConf(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectLockConf"),map);
	}

	@Override
	public Map<String, String> selectbyInstanceId(String instanceid) {
		return this.sqlSessionTemplate.selectMap(this.getSqlName("selectbyInstanceId"), instanceid);
	}
	
	@Override
	public List<INSBWorkflowsubtrack> selectByTaskcodeList(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectByTaskcodeList"), map);
	}

}