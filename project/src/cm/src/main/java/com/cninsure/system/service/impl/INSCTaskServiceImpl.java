package com.cninsure.system.service.impl;

import java.util.List;

import javax.annotation.Resource;

import net.sf.json.JSONArray;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.cninsure.system.dao.INSCTaskDao;
import com.cninsure.system.entity.INSCTask;
import com.cninsure.system.service.INSCTaskService;

@Service
@Transactional
public class INSCTaskServiceImpl extends BaseServiceImpl<INSCTask> implements
		INSCTaskService {
	@Resource
	private INSCTaskDao inscTaskDao;

	@Override
	protected BaseDao<INSCTask> getBaseDao() {
		return inscTaskDao;
	}

	@Override
	public String getTaskByTasktypeid(String id) {
		String result = "";
		if (StringUtils.isNotBlank(id)) {
			INSCTask task = new INSCTask();
			task.setTasktypeid(id);
			List<INSCTask> queryList = this.queryList(task);
			JSONArray jsonArray = JSONArray.fromObject(queryList);
			result = jsonArray.toString();
		}
		return result;
	}

}