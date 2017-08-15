package com.zzb.conf.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseService;
import com.zzb.conf.entity.INSBChannelcarinfo;

public interface INSBChannelcarinfoService extends BaseService<INSBChannelcarinfo> {
	//public String initChannelCarInfo(INSBChannelcarinfo channelcar);
	
	/**
	 * 分页初始化车辆信息 
	 * @param map
	 * @return
	 */
	public String initChannelCarInfo(Map<String, Object> map);
	

	public String exportChannelCarInfo(List<String> arrayid) throws IOException;
}