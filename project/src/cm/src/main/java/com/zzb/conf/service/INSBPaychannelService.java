package com.zzb.conf.service;

import java.util.Map;

import com.cninsure.core.dao.BaseService;
import com.zzb.conf.entity.INSBPaychannel;

public interface INSBPaychannelService extends BaseService<INSBPaychannel> {

	public Map<String, Object> initPayChannelList(Map<String, Object> map);

	public Map<String, Object> initPayWayList(Map<String, Object> data, String id);

}