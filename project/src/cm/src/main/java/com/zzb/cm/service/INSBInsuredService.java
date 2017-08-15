package com.zzb.cm.service;

import java.util.Map;

import com.cninsure.core.dao.BaseService;
import com.zzb.cm.entity.INSBInsured;

public interface INSBInsuredService extends BaseService<INSBInsured> {
	public Map<String, Object> getInsuredInfo(String taskid);
}