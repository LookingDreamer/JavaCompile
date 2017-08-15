package com.zzb.conf.dao;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.system.entity.INSCDept;
import com.common.PagingParams;
import com.zzb.conf.entity.INSBChannelagreement;

public interface INSBChannelagreementDao extends BaseDao<INSBChannelagreement> {

	public List<INSBChannelagreement> getChannelagreement(String channelid);

	public List<Map<String, Object>> getChannelAgreementProvider(String channelid, PagingParams para);
	
	public List<Map<String, Object>> getChannelAgreementProvider(Map<String, Object> para);
	
	public List<Map<String, Object>> getChannelAgreementProviderMerchant(Map<String, Object> para);

	public Long getqueryProviderCount(String channelid);

	public Map<String, Object> getPayChannelNames(String agreeid, String deptid, String providerid);

	public INSBChannelagreement select4ChannelAgreement(Map<String, Object> tempMap);

	public List<Map<String, Object>> getChannelDeptId(String channelinnercode, String city);

	public List<Map<String, Object>> getAgreementByArea(String channelinnercode, String prvcode, String city) ;

	public List<INSCDept> getAgreementDeptByPrvcode(String chnagreementid, String prvcode) ;

	public List<Map<String, Object>> queryAgreement(Map<String, Object> para);
	
	public Long queryAgreementCount(Map<String, Object> para);
	
	public List<Map<String, Object>> queryAgreementChn(Map<String, Object> para);
}