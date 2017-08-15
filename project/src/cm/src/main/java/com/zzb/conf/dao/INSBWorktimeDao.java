package com.zzb.conf.dao;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseDao;
import com.zzb.conf.entity.INSBWorktime;

public interface INSBWorktimeDao extends BaseDao<INSBWorktime> {

	public List<INSBWorktime> selectByPage(Map<Object, Object> map);

	public INSBWorktime selectOneBydeptId(String inscdeptid);
	
	public int insertOne(INSBWorktime iscbworktime);

	public List<Map<Object, Object>> selectOneFuture(Map<Object, Object> map);

}