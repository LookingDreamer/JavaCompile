package com.zzb.extra.service.impl;

import javax.annotation.Resource;

import com.zzb.extra.dao.INSBAgentTaskDao;
import com.zzb.extra.entity.INSBAgentTask;
import com.zzb.extra.service.INSBAgentTaskService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;

@Service
@Transactional
public class INSBAgentTaskServiceImpl extends BaseServiceImpl<INSBAgentTask> implements
        INSBAgentTaskService {
	@Resource
	private INSBAgentTaskDao insbAgentTaskDao;

	@Override
	protected BaseDao<INSBAgentTask> getBaseDao() {
		return insbAgentTaskDao;
	}

	@Override
	public void saveAgentTask(INSBAgentTask agentTask) {
		insbAgentTaskDao.insert(agentTask);
	}
}