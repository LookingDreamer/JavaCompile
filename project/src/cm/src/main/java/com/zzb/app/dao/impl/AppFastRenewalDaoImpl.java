package com.zzb.app.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.app.dao.AppFastRenewalDao;
import com.zzb.app.model.InsureConfigModel;
import com.zzb.conf.entity.INSBAgentprovider;
import com.zzb.conf.entity.INSBPaychannel;
import com.zzb.conf.entity.INSBProvider;
import com.zzb.conf.entity.INSBRiskitem;
@Repository
public class AppFastRenewalDaoImpl extends BaseDaoImpl<INSBAgentprovider> implements AppFastRenewalDao {

	@Override
	public List<Map<Object, Object>> getAllProviderList(
			Map<String, String> map) {
		return this.sqlSessionTemplate.selectList("appFastRenewal.getAllProviderList", map);
	}

	@Override
	public List<INSBProvider> getChildProviderList(String pid) {
		return this.sqlSessionTemplate.selectList("appFastRenewal.getChildProviderList", pid);
	}

	@Override
	public List<Map<Object, Object>> ShippingAddressinfo(String uuid) {
		return this.sqlSessionTemplate.selectList("appFastRenewal.getShippingAddressList",uuid);
	}

	@Override
	public List<Map<String, Object>> queryAgentPowerByAgentId(String agentid) {
		return this.sqlSessionTemplate.selectList("appFastRenewal.queryAgentPowerByAgentId",agentid);
	}
	@Override
	public List<Map<Object, Object>> queryPayExpressinfo(Map<String, String> map) {
		return this.sqlSessionTemplate.selectList("appFastRenewal.queryPayExpressinfo",map);
	}

	@Override
	public String queryPayTarget(String codevalue) {
		return this.sqlSessionTemplate.selectOne("appFastRenewal.queryPayTarget",codevalue);
	}

	@Override
	public INSBPaychannel queryPay(String paychannelid) {
		return this.sqlSessionTemplate.selectOne("appFastRenewal.queryPay",paychannelid);
	}

	@Override
	public List<InsureConfigModel> queryInsureConfigByKey(String plankey) {
		return this.sqlSessionTemplate.selectList("appFastRenewal.queryInsureConfigByKeyModel",plankey);
	}

	@Override
	public List<INSBRiskitem> queryRiskItemsByProviderids(List<String> ids) {
		return this.sqlSessionTemplate.selectList("appFastRenewal.queryRiskItemsByProviderids",ids);
	}
}
