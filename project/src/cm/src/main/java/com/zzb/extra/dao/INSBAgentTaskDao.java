package com.zzb.extra.dao;

import com.cninsure.core.dao.BaseDao;
import com.zzb.extra.entity.INSBAgentTask;
import com.zzb.extra.model.AgentTaskModel;

import java.util.List;
import java.util.Map;

public interface INSBAgentTaskDao extends BaseDao<INSBAgentTask> {
    public Boolean exist(Map<String, Object> map);

    public int completeTask(String taskid);//任务99关联的mini营销活动代码发布cm后台

    public List<AgentTaskModel> queryTasks();

    public List<AgentTaskModel> queryCompletedTaskByTaskId(Map<String,Object> map);
    
    public Map<String, Object> selectNameByTaskId(String taskid);
    
    List<Map<Object, Object>> selectIdByName(Map<String, Object> map);

}