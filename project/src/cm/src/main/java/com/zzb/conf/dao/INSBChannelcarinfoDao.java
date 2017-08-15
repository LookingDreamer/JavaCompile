package com.zzb.conf.dao;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseDao;
import com.zzb.conf.entity.INSBChannelcarinfo;

public interface INSBChannelcarinfoDao extends BaseDao<INSBChannelcarinfo> {

	/*
	 * 分页获取车辆信息
	 */
	public List<INSBChannelcarinfo> getChannelcarinfo(Map<String, Object> map);
}