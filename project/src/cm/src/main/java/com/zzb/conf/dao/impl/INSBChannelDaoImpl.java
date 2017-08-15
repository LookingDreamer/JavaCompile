package com.zzb.conf.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.zzb.conf.entity.INSBPaychannel;
import com.zzb.extra.entity.INSBCommission;
import com.zzb.extra.model.SelectProviderBeanForMinizzb;
import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.conf.dao.INSBChannelDao;
import com.zzb.conf.entity.INSBChannel;

@Repository
public class INSBChannelDaoImpl extends BaseDaoImpl<INSBChannel> implements
		INSBChannelDao {

	
	@Override
	public List<INSBChannel> selectByParentChannelCode(String parentcode) { 
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectByParentChannelCode"),parentcode);
	}

	@Override
	public int addChannelDatas(INSBChannel channel) {
		return this.sqlSessionTemplate.insert(this.getSqlName("insert"), channel);
	}

	@Override
	public List<INSBChannel> selectAllByChannel(String fieldName) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectByChannel"), fieldName);
	}

	@Override
	public int updateChannelByid(String id) {
		return this.sqlSessionTemplate.update(this.getSqlName("updateChannelByid"), id);
	}

	@Override
	public int updateChannelByiddel(String id) {
		return this.sqlSessionTemplate.update(this.getSqlName("updateChannelByiddel"),id);
	}

	@Override
	public INSBChannel selectByChannelcode(String code) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectByChannelCode"), code);
	}

	@Override
	public INSBChannel queryByNoti(String noti) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("queryPid"),noti);
	}

	@Override
	public Map<String, Object> getBillTypeInfo(INSBChannel channel) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("getBillTypeInfo"),channel);
	}

	/**
	 * 更新渠道deleteflag标志位为0
	 */
	@Override
	public int updateChannelDeleteflagById(String id) {
		return this.sqlSessionTemplate.update(this.getSqlName("updateChannelDeleteflag"), id);
	}

	/**
	 * 按渠道编码、投保地区，查询渠道供应商列表
	 * data 含 city 地区码（市）,channelinnercode 渠道编码 (如：minizzb)
	 */
	public List<SelectProviderBeanForMinizzb> queryChannelProviderList(Map<String,String> data){
		return this.sqlSessionTemplate.selectList(this.getSqlName("queryChannelProviderListData"), data);
	}

	/**
	 * 按渠道编码、投保地区，查询渠道配置的代理人
	 * data 含 city 地区码（市）,channelinnercode 渠道编码 (如：minizzb)
	 */
	public String queryChannelAgentCode(Map<String,String> data){
		return this.sqlSessionTemplate.selectOne(this.getSqlName("queryChannelAgentCode"), data);
	}
	public  String selectForCallBackURl(INSBChannel channel){
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectForCallBackURl"),channel);
	}
	
	@Override
	public long countForMerchant(Map<String, Object> map) { 
		return this.sqlSessionTemplate.selectOne(this.getSqlName("countForMerchant"), map);
	}
	@Override
	public List<Map<String, Object>> selectForMerchant(Map<String, Object> map) { 
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectForMerchant"), map);
	}
	
	@Override
	public Map<String, Object> queryIllustration(Map<String, Object> map) { 
		return this.sqlSessionTemplate.selectOne(this.getSqlName("queryIllustration"), map);
	}

	@Override
	public Map<String, String> querychanneltype(String taskid) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("querychanneltype"), taskid);
	}

	@Override
	public long getQuoteTotalInfoCount(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("getQuoteTotalInfoCount"), map);
	}

	@Override
	public List<Map<String, Object>> getQuoteTotalInfo(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("getQuoteTotalInfo"),map);
	}

	@Override
	public List<INSBChannel> queryChildChannel(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.sqlSessionTemplate.selectList(this.getSqlName("queryChildChannel"), map);
	}

	@Override
	public List<INSBCommission> queryCommissionList(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return this.sqlSessionTemplate.selectList(this.getSqlName("queryCommissionList"), map);
	}

	@Override
	public List<Map<String, String>> selectChannelParent() {
		// TODO Auto-generated method stub
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectChannelParent"));
	}

	@Override
	public List<Map<String, String>> selectChildchannelByPid(String pId) {
		// TODO Auto-generated method stub
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectChildchannelByPid"), pId);
	}

	@Override
	public String selectChannelidByChannelinnercode(String channelinnercode) {
		// TODO Auto-generated method stub
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectChannelidByChannelinnercode"), channelinnercode);
	}
	@Override
	public List<INSBPaychannel> showPayChannelForChn(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectPayChannelListByChannelIdAndPrvId"), map);
	}

	@Override
	public List<Map<String, String>> selectSenderInfoByCityAndChannelcode(String city, String channelinnercode) {
		// TODO Auto-generated method stub
		Map<String,Object> map = new HashMap<>();
		map.put("city", city);
		map.put("channelinnercode", channelinnercode);
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectSenderInfoByCityAndChannelcode"), map);
	}

}