package com.zzb.conf.dao;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseDao;
import com.zzb.conf.controller.vo.DeptPayTypeVo;
import com.zzb.conf.entity.INSBPaychannelmanager;

public interface INSBPaychannelmanagerDao extends BaseDao<INSBPaychannelmanager> {

	/**
	 * 
	 * 删除未选中的协议 平台信息
	 * 
	 * @param ids	平台id
	 * @param agreementid
	 * @return
	 */
	public Long deleteByExceptIds(List<String> ids,String agreementid);
	
	/**
	 * 根据协议id获取支付方式
	 * 
	 * @param Depcode 平台id
	 * @param prvid
	 * @return
	 */
	public List<String> selectPaychannelIdByAgreementId(String Depcode,String prvid);
	
	
	/**
	 * 根据支付渠道查询支付方式分页
	 * 
	 * @param map paychannelid
	 * @return
	 */
	public List<INSBPaychannelmanager> selectPagePaywayList(Map<String, Object> map);

	
	/**
	 * 
	 * 通过逻辑主键删除数据
	 * @param param	deptid(平台id)	providerid	channelid
	 */
	public void deleteByLogicId(Map<String,String> param);
	
	/**
	 * 移动快刷 根据网点和供应商找出平台账户
	 * @param param
	 * @return
	 */
	public INSBPaychannelmanager queryManager(Map<String, String> param);
	
	/**
	 * 渠道协议配置 获取支付方式
	 * 
	 * @param map
	 * @return
	 */
	public List<DeptPayTypeVo> getDeptPayType(Map<String, Object> map);
	
	public List<INSBPaychannelmanager> queryListByParam(Map<String, Object> param);
	public Long selectDataSize(Map<String, Object> param);
}