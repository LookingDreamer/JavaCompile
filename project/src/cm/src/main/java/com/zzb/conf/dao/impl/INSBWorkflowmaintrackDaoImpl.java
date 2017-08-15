package com.zzb.conf.dao.impl;
 
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.cninsure.core.utils.UUIDUtils;
import com.zzb.conf.dao.INSBWorkflowmaintrackDao;
import com.zzb.conf.entity.INSBWorkflowmain;
import com.zzb.conf.entity.INSBWorkflowmaintrack;

import net.sf.json.JSONObject;

@Repository
public class INSBWorkflowmaintrackDaoImpl extends BaseDaoImpl<INSBWorkflowmaintrack> implements
		INSBWorkflowmaintrackDao {

	@Override
	public void insertByMainTable(INSBWorkflowmain model) {
		String id = UUIDUtils.random();
		model.setId(id);
		if(StringUtil.isEmpty(model.getOperator())){
			model.setOperator("admin");
        }
		if(StringUtil.isEmpty(model.getFromoperator())){
			model.setFromoperator("admin");
        } 
		this.sqlSessionTemplate.insert(this.getSqlName("insertByMainTable"),model);
		LogUtil.info("INSBWorkflowmaintrack|报表数据埋点|"+JSONObject.fromObject(model).toString());
	}

	@Override
	public List<Map<String,Object>> selectAllTrack(String instanceid) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectAllTrackByInstanceId"), instanceid);
	}

	@Override
	public List<Map<String, Object>> selectAllComplateTrack(String instanceid) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectAllComplateTrackByInstanceId"), instanceid);
	}

	@Override
	public String selectByInstanceIdTaskCode(Map<String,String> map) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectByInstanceIdTaskCode"), map);
	}

	@Override
	public void updateTaskStatusBylogicId(INSBWorkflowmaintrack model) {
		this.sqlSessionTemplate.update(this.getSqlName("updateTaskStatusBylogicId"), model);
		LogUtil.info("INSBWorkflowmaintrack|报表数据埋点|"+JSONObject.fromObject(model).toString());
	}

	@Override
	public long countDayTask(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("countDayTask"), map);
	}

	@Override
	public long countMonthTask(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("countMonthTask"), map);
	}

	@Override
	public Map<String, Object>getUserInfo(String taskid) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("getUserInfo"), taskid);
	}

	@Override
	public void updateByMainTable(INSBWorkflowmain model) {
		this.sqlSessionTemplate.update(this.getSqlName("updateByMainTable"), model);
		LogUtil.info("INSBWorkflowmaintrack|报表数据埋点|"+JSONObject.fromObject(model).toString());
	}
	
	@Override
	public List<Map<String,String>> selectByInstanceId4h5(
			String instanceid) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectByInstanceId4h5"), instanceid);
	}

	@Override
	public List<Map<String,Object>> selectUserCodesByTimeAndTaskNum4Task(
			Map<String, Object> param) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectUserCodesByTimeAndTaskNum4Task"), param);
	}
	@Override
	public List<Map<String, Object>> selectUserCodesByTimeAndTaskNumAll4Task(
			Map<String, Object> param) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectUserCodesByTimeAndTaskNumAll4Task"), param);
	}
	
	
	
	@Override
	public List<Map<String,Object>> queryMyHistorytask(Map<String,Object> map){
		return this.sqlSessionTemplate.selectList(this.getSqlName("querymyhistorytask"),map);
	}

	@Override
	public long queryMyHistorytasknum(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("querymyhistorytasknum"),map);
	}

	@Override
	public Map<String, String> selectbyInstanceId(String instanceid) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectbyInstanceId"), instanceid);
	}

	@Override
	public int count21CodeByTaskIdAndInscomcode(Map<String, String> paraMap) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("count21CodeByTaskIdAndInscomcode"), paraMap);
	}

}