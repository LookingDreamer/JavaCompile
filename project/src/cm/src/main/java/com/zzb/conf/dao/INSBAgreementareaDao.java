package com.zzb.conf.dao;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseDao;
import com.zzb.conf.entity.INSBAgreementarea;

public interface INSBAgreementareaDao extends BaseDao<INSBAgreementarea> {
	/**
	 * 根据渠道内部编码取得上线的投保地区
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getAgreeAreaByChninnercode(Map<String, Object> map);
	/**
	 * 根据渠道内部编码取得上线的投保地区及对应出单工号
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getAgreeAreaAndJobNumByChninnercode(Map<String, Object> map);
}