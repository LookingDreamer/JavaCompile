package com.zzb.extra.service;

import java.util.Map;

import com.cninsure.core.dao.BaseService;
import com.zzb.extra.entity.INSBDictinfo;

public interface INSBDictinfoService extends BaseService<INSBDictinfo> {
	public  String getList(Map map);
}