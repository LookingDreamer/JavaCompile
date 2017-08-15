package com.zzb.app.dao;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseDao;
import com.zzb.app.model.InsureConfigModel;
import com.zzb.conf.entity.INSBAgentprovider;
import com.zzb.conf.entity.INSBPaychannel;
import com.zzb.conf.entity.INSBProvider;
import com.zzb.conf.entity.INSBRiskitem;

public interface AppFastRenewalDao extends BaseDao<INSBAgentprovider>{

	List<Map<Object, Object>> getAllProviderList(Map<String, String> map);

	List<INSBProvider> getChildProviderList(String pid);
	
	List<Map<Object, Object>> ShippingAddressinfo(String uuid);

	List<Map<String, Object>> queryAgentPowerByAgentId(String agentid);
	
	List<Map<Object, Object>> queryPayExpressinfo(Map<String, String> map);
	
	public String queryPayTarget(String codevalue);
	
	INSBPaychannel queryPay(String paychannelid);

	List<InsureConfigModel> queryInsureConfigByKey(String plankey);

	List<INSBRiskitem> queryRiskItemsByProviderids(List<String> ids);
}
