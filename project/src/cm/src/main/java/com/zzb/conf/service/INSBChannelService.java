package com.zzb.conf.service;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseService;
import com.zzb.conf.controller.vo.BaseInfoVo;
import com.zzb.conf.controller.vo.ChannelRespVo;
import com.zzb.conf.controller.vo.TreeVo;
import com.zzb.conf.entity.INSBChannel;
import com.zzb.extra.model.SelectProviderBeanForMinizzb;

public interface INSBChannelService extends BaseService<INSBChannel> {

	/**
	 *  查询渠道列表
	 * @param parentCode 父节点ID
	 * @return
	 */
	public List<INSBChannel> queryListByPid(String parentCode);

	/**
	 * 查询管道列表树
	 * @param parentCode 父节点ID
	 * @param hideChildren 隐藏子节点，默认不隐藏false
	 * @return
	 */
	public List<TreeVo> queryTreeListByPid(String parentCode, boolean... hideChildren);
	public List<Map<String, String>> queryTreeListByPidDim();

	/**
	 * 查询渠道详情
	 * @param id 渠道ID
	 * @return
	 */
	public ChannelRespVo queryDetailById(String id) ;

	/**
	 * 删除
	 * @param id 渠道ID
	 * @return
	 */
	public int deleteById(String id) ;

	/**
	 * 添加渠道
	 * @param channel
	 * @return
	 */
	public int addChannel(INSBChannel channel, BaseInfoVo baseInfoVo);

	/**
	 * 更新渠道
	 * @param channel
	 * @return
	 */
	public int updateChannel(INSBChannel channel, BaseInfoVo baseInfoVo) ;

	/**
	 * 获取渠道协议信息
	 * @param id
	 * @return
	 */
	public Map<String, Object> getChannelAgreementInfo(String id);

	/**
	 * 按渠道编码、投保地区，查询渠道供应商列表
	 * @param city 地区码（市）
	 * @param channelinnercode 渠道编码 (如：minizzb)
	 */
	public List<SelectProviderBeanForMinizzb> queryChannelProviderList(String city, String channelinnercode);

	/**
	 * 按渠道编码、投保地区，查询渠道配置的代理人
	 * data 含 city 地区码（市）,channelinnercode 渠道编码 (如：minizzb)
	 */
	public String queryChannelAgentCode(String city, String channelinnercode);


	public String updateAgentForChannel(INSBChannel channel, BaseInfoVo baseInfoVo);





	/**
	 * 将子节点变为父节点
	 * @param id
	 * @return
	 */
	/*public int updateChannelById(String id);*/

	/**
	 * 将父节点转换为子节点
	 * @param id
	 * @return
	 */
	/*public int updateChannelByIddel(String id);*/

	/**
	 * 通过渠道ID查询是否为父级渠道
	 * @param id
	 * @return
	 */
	/*public int queryChannelByUpchannelcode(String id);*/

	/**
	 * 更新渠道deleteflag标志位
	 * @param id
	 */
	/*public int updateChannelDeleteflagById(String id);*/
}