package com.zzb.extra.dao.impl;

import com.zzb.extra.dao.INSBAgentTaskDao;
import com.zzb.extra.entity.INSBAgentTask;
import com.zzb.extra.model.AgentTaskModel;
import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;

import java.util.List;
import java.util.Map;

@Repository
public class INSBAgentTaskDaoImpl extends BaseDaoImpl<INSBAgentTask> implements
        INSBAgentTaskDao {
    @Override
    public Boolean exist(Map<String, Object> map) {
        INSBAgentTask insbAgentTask = this.sqlSessionTemplate.selectOne("queryAgentTaskByPolicyNo", map);
        return insbAgentTask != null;
    }

    @Override
    public int completeTask(String taskid) {
        return this.sqlSessionTemplate.update(this.getSqlName("completeByTaskId"), taskid);
    }

    @Override
    public List<AgentTaskModel> queryCompletedTaskByTaskId(Map<String, Object> map) {
        return this.sqlSessionTemplate.selectList("queryCompletedTaskByTaskId",map);
        //任务99关联的mini营销活动代码发布cm后台
    }

    @Override
    public List<AgentTaskModel> queryTasks() {
        return this.sqlSessionTemplate.selectList("queryAgentTasks");
    }

	@Override
	public Map<String, Object> selectNameByTaskId(String taskid) {
		// TODO Auto-generated method stub
		return this.sqlSessionTemplate.selectOne("selectNameByTaskid", taskid);
	}
	
	@Override
	public List<Map<Object, Object>> selectIdByName(Map<String, Object> map){
		
		return this.sqlSessionTemplate.selectList("selectIdByName", map);
	}
}