package com.zzb.cm.dao;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseDao;
import com.zzb.cm.entity.INSBInsured;

public interface INSBInsuredDao extends BaseDao<INSBInsured> {
	public List<INSBInsured> selectInsuredList(Map<String, Object> map);
	public INSBInsured selectInsuredByTaskId(String taskid);
	public void deletebyID(String id);
}