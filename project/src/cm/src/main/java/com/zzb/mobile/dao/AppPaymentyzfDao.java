package com.zzb.mobile.dao;

import java.util.List;
import java.util.Map;

import net.sf.ehcache.concurrent.Sync;

import com.cninsure.core.dao.BaseDao;
import com.zzb.mobile.entity.AppPaymentyzf;
import com.zzb.model.AppPaymentyzfRModel;


public interface AppPaymentyzfDao extends BaseDao<AppPaymentyzf> {
	/**
	 * 	添加
	 */
	public void insert(AppPaymentyzf yzf);
	/**
	 * 	获取银行列表
	 */
	public List<Map<String,Object>> getBankType();
	/**
	 * 	获取银行卡类型
	 */
	public List<Map<String,Object>> getBankCardType();
	
	
	/**
	 * 	获取证件类型列表
	 */
	public List<Map<String,Object>> getidCardType();
	/**
	 * 	查询签约信息
	 */
	public List<AppPaymentyzfRModel> querySignInfos(Map<String,Object> map);
	/**
	 * 	获取省级列表
	 */
	public  List<Map<String,Object>> getProvince();
	/**
	 * 	获取市级列表
	 */
	public List<Map<String,Object>> getCity(String provinceID);
	/**
	 * 查询insccode表
	 */
	public Map<String,Object> queryFromInsccodeByCode(Map<String,Object> map);
	/**
	 * 查询Insbregion表
	 */
	public Map queryFromInsbregionByCode(Map<String,Object> map);
	
	
	
	
}
