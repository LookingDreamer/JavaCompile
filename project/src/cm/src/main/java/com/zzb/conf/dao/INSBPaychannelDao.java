package com.zzb.conf.dao;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseDao;
import com.zzb.conf.entity.INSBPaychannel;

public interface INSBPaychannelDao extends BaseDao<INSBPaychannel> {

	public List<Map<Object, Object>> selectPayChannelListPaging(Map<String, Object> data);
	

	public List<Map<String,Object>> selectPayChannelList();

	public List<INSBPaychannel> selectPayChannelListByAgreementId(String Depcode,String prvid);	//获取stateflag = 0(启用)的对象
}