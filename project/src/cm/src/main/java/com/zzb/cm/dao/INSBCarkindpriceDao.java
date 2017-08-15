package com.zzb.cm.dao;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseDao;
import com.zzb.cm.entity.INSBCarkindprice;

public interface INSBCarkindpriceDao extends BaseDao<INSBCarkindprice> {

	public List<INSBCarkindprice> selectCarkindpriceList(Map<String, Object> map);

	public List<INSBCarkindprice> selectOrderRiskkind(Map<String, Object> map);

	public INSBCarkindprice selectINSBCarkindprice(Map<String, Object> map);

	/*
	 * 手机端投保书页面(商业险计划数据)
	 */
	public List<INSBCarkindprice> businussKind(String taskId);

	/**
	 * 手机端状态比较
	 */
	public List<Map<String, String>> selectStatus(Map<String, Object> map);

	public List<Map<String, String>> selectAllriskkind(String inscomcode);

	/**
	 * @param map
	 * @return
	 */
	public List<INSBCarkindprice> selectByTaskidAndInscomcode(Map<String, Object> map);

	/**
	 * 获取订单总金额
	 * 
	 * @param map
	 * @return
	 */
	public double getTotalProductAmount(Map<String, Object> map);

	/**
	 * 查询交强险险别记录条数liuchao
	 * 
	 * @param mInstanceid
	 *            inscomcode
	 */
	public int getStrCount(Map<String, Object> params);

	/**
	 * 查询商业险险别记录条数liuchao
	 * 
	 * @param mInstanceid
	 *            inscomcode
	 */
	public int getBusCount(Map<String, Object> params);

	/**
	 * 根据主任务号和供应商删除商业险信息
	 * 
	 * @param params
	 * @return
	 */
	public int deleteBizByTaskid(Map<String, Object> params);

	/**
	 * 根据主任务号和供应商删除交强险和车船税信息
	 * 
	 * @param params
	 * @return
	 */
	public int deleteEfcByTaskid(Map<String, Object> params);

	public int deleteByObj(INSBCarkindprice insbCarkindprice);

	
}