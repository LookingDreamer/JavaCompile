package com.zzb.conf.service;

import java.util.List;
import java.util.Map;









import com.cninsure.core.dao.BaseService;
import com.zzb.cm.entity.INSBCarinfo;

public interface INSBCarinfoManagerService extends BaseService<INSBCarinfo> {

	public String showCarinfoList(Map<String, Object> map);

	/**
	 * 初始化车辆列表
	 * @param car
	 * @return
	 */
	public Map<String, Object> initCarList(Map<String, Object> map);

	public String initCarInfoList(INSBCarinfo car);

	/**
	 * 查询车型信息列表
	 * @return
	 */
	public List<Map<String, String>> querycarmodellist(int offset,int limit);

}