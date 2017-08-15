package com.cninsure.system.dao;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.system.entity.INSCTask;

public interface INSCTaskDao extends BaseDao<INSCTask> {
	@Deprecated
	List<INSCTask> selectAll();
	List<INSCTask> selectAll(Map<String, Object> params);

}