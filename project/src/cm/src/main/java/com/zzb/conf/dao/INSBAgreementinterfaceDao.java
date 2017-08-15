package com.zzb.conf.dao;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseDao;
import com.zzb.conf.entity.INSBAgreementinterface;

public interface INSBAgreementinterfaceDao extends BaseDao<INSBAgreementinterface> {

	public List<Map<String, Object>> getInterfaceInfo(String string);

	/**
	 * 根据渠道id取得接口权限数据 
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getByChannelinnercode(Map<String, Object> map);
	/**
	 
	 * @param map
	 * @return
	 * 通过渠道获得要批量关闭的id
	 */
	public String getIdByChannelinnercode(Map<String, Object> map);
}