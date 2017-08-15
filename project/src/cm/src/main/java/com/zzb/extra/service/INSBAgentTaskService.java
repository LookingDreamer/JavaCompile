package com.zzb.extra.service;

import com.cninsure.core.dao.BaseService;
import com.zzb.extra.entity.INSBAgentTask;

public interface INSBAgentTaskService extends BaseService<INSBAgentTask> {

    public void saveAgentTask(INSBAgentTask agentTask);
}