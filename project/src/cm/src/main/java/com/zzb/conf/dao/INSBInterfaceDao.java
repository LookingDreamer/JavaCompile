package com.zzb.conf.dao;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseDao;
import com.zzb.conf.entity.INSBInterface;

public interface INSBInterfaceDao extends BaseDao<INSBInterface> {
	@Deprecated
	List<INSBInterface> selectAll();
	
	List<INSBInterface> selectAll(Map<String, Object> params);

}