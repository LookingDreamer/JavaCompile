package com.zzb.conf.dao;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseDao;
import com.zzb.conf.entity.INSBChannel;
import com.zzb.conf.entity.INSBPaychannel;
import com.zzb.extra.entity.INSBCommission;
import com.zzb.extra.model.SelectProviderBeanForMinizzb;

public interface INSBChannelDao extends BaseDao<INSBChannel> {
	public List<INSBChannel> selectByParentChannelCode(String parentcode);
	/**
	 * 新增
	 * @param model 
	 * @return
	 */
	public int addChannelDatas(INSBChannel channel);
	
	public List<INSBChannel> selectAllByChannel(String fieldName);
	
	public int updateChannelByid(String id);
	public int updateChannelByiddel(String id);
	
	/**
	 * 通过code查找父节点code
	 * 
	 * @param code
	 * @return
	 */
	public INSBChannel selectByChannelcode(String code);
	public List<Map<String, String>> selectChannelParent();
	public List<Map<String, String>> selectChildchannelByPid(String pId);
	public INSBChannel queryByNoti(String noti);
	/**
	 * 结算方式信息查询
	 * @param insbChannel
	 * @return
	 */
	public Map<String, Object> getBillTypeInfo(INSBChannel insbChannel);
	
	/**
	 * 更新渠道deleteflag标志位为0
	 * @param id
	 * @return
	 */
	public int updateChannelDeleteflagById(String id);

	/**
	 * 按渠道编码、投保地区，查询渠道供应商列表
	 * data 含 city 地区码（市）,channelinnercode 渠道编码 (如：minizzb)
	 */
	public List<SelectProviderBeanForMinizzb> queryChannelProviderList(Map<String,String> data);

	public List<INSBChannel> queryChildChannel(Map<String, Object> map);
	/**
	 * 按渠道编码、投保地区，查询渠道配置的代理人
	 * data 含 city 地区码（市）,channelinnercode 渠道编码 (如：minizzb)
	 */
	public String queryChannelAgentCode(Map<String,String> data);

	public  String selectForCallBackURl(INSBChannel channel);
	public long countForMerchant(Map<String, Object> map);
	public List<Map<String, Object>> selectForMerchant(Map<String, Object> map);
	public Map<String, Object> queryIllustration(Map<String, Object> map);
	public Map<String, String> querychanneltype(String taskid);

	long getQuoteTotalInfoCount(Map<String, Object> map);

	List<Map<String,Object>> getQuoteTotalInfo(Map<String, Object> map);

	public List<INSBCommission> queryCommissionList(Map<String, Object> map);

	
	public String selectChannelidByChannelinnercode(String channelinnercode);


	public List<INSBPaychannel> showPayChannelForChn(Map<String, Object> map);
	/**
	 * 根据城市和渠道编码查找寄件人信息
	 * @param channelinnercode
	 * @param city
	 * @return
	 */
	public List<Map<String, String>> selectSenderInfoByCityAndChannelcode(String city, String channelinnercode);

}