package com.zzb.cm.dao.impl;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.cm.dao.INSBInsuresupplyparamDao;
import com.zzb.cm.entity.INSBInsuresupplyparam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class INSBInsuresupplyparamDaoImpl extends BaseDaoImpl<INSBInsuresupplyparam> implements
		INSBInsuresupplyparamDao {

	public int deleteByTask(String taskid, String inscomcode) {
		Map<String, String> param = new HashMap<>(2);
		param.put("taskid", taskid);
		param.put("inscomcode", inscomcode);
		return this.sqlSessionTemplate.delete(this.getSqlName("deleteByTask"), param);
	}

	public List<INSBInsuresupplyparam> getByTask(String taskid, String inscomcode) {
		INSBInsuresupplyparam param = new INSBInsuresupplyparam();
		param.setTaskid(taskid);
		param.setInscomcode(inscomcode);
		return selectList(param);
	}
}