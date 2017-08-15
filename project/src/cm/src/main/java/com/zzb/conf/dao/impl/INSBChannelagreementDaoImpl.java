package com.zzb.conf.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cninsure.system.entity.INSCDept;
import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.common.PagingParams;
import com.zzb.conf.dao.INSBChannelagreementDao;
import com.zzb.conf.entity.INSBChannelagreement;

@Repository
public class INSBChannelagreementDaoImpl extends BaseDaoImpl<INSBChannelagreement> implements
		INSBChannelagreementDao {

	@Override
	public List<INSBChannelagreement> getChannelagreement(String channelid) {
		Map<String,String> mappar=new HashMap<String, String>();
		mappar.put("channelid", channelid);
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectByChannelid"), mappar);
	}

	@Override
	public List<Map<String,Object>> getChannelAgreementProvider(String channelid,PagingParams para) {
		Map<String,Object> mappar=new HashMap<String,Object>();
		mappar.put("limit",para.getLimit());
		mappar.put("offset", para.getOffset());
		mappar.put("channelid", channelid);
		return this.sqlSessionTemplate.selectList(this.getSqlName("queryProviderSource"), mappar);
	}
	
	@Override
	public List<Map<String, Object>> getChannelAgreementProvider(Map<String, Object> mappar) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("queryProviderSource"), mappar);
	}
	
	@Override
	public List<Map<String, Object>> getChannelAgreementProviderMerchant(Map<String, Object> mappar) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("queryProviderSourceMerchant"), mappar);
	}

	@Override
	public Long getqueryProviderCount(String channelid) {
		Map<String,Object> mappar=new HashMap<String,Object>();
		mappar.put("channelid", channelid);
		return this.sqlSessionTemplate.selectOne(this.getSqlName("queryProviderCount"), mappar);
	}

	@Override
	public Map<String, Object> getPayChannelNames(String agreeid,
			String deptid, String providerid) {
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("agreeid", agreeid);
		param.put("deptid", deptid);
		param.put("providerid", providerid);
		return this.sqlSessionTemplate.selectOne(this.getSqlName("getPayChannelNames"),param);
	}

	@Override
	public INSBChannelagreement select4ChannelAgreement(Map<String, Object> tempMap) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("select4ChannelAgreement"),tempMap);
	}

	@Override
	public List<Map<String, Object>> getChannelDeptId(String channelinnercode, String city) {
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("channelinnercode", channelinnercode);
		param.put("city", city);
		return this.sqlSessionTemplate.selectList(this.getSqlName("getChannelDeptId"), param);
	}
	
	@Override
	public List<Map<String, Object>> getAgreementByArea(String channelinnercode, String id, String city) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("channelinnercode", channelinnercode);
		param.put("id", id);
		param.put("city", city);
		return this.sqlSessionTemplate.selectList(this.getSqlName("getAgreementByArea"), param) ;
	}

	@Override
	public List<INSCDept> getAgreementDeptByPrvcode(String chnagreementid, String prvcode) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("chnagreementid", chnagreementid);
		param.put("providerid", prvcode);
		return this.sqlSessionTemplate.selectList(this.getSqlName("getAgreementDeptByPrvcode"), param) ;
	}

	@Override
	public List<Map<String, Object>> queryAgreement(Map<String, Object> para) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("queryAgreement"), para);
	}

	@Override
	public Long queryAgreementCount(Map<String, Object> para) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("queryAgreementCount"), para);
	}

	@Override
	public List<Map<String, Object>> queryAgreementChn(Map<String, Object> para) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("queryAgreementChn"), para);
	}
}
