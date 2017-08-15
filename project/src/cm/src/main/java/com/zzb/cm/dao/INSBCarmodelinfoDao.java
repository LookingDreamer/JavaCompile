package com.zzb.cm.dao;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseDao;
import com.zzb.cm.entity.INSBCarmodelinfo;

public interface INSBCarmodelinfoDao extends BaseDao<INSBCarmodelinfo> {
	/**
	 * 保存车型信息
	 * @param iNSBCarmodelinfo
	 */
	public void updateByCarInfoId(INSBCarmodelinfo  iNSBCarmodelinfo);
	
	/**
	 * 通过map参数条件查询重选车型信息
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> reselectCarModelInfo(Map<String, Object> map);
	
	
	/**
	 * 通过iNSBCarmodelinfode的id更改carinfoid信息
	 * @param iNSBCarmodelinfo
	 * @return
	 */
	public boolean updateCarinfoidById(INSBCarmodelinfo Carmodelinfo);
	
	
	/**
	 * 按照车型名称查询车辆信息
	 * 
	 * @param standardname
	 * @return
	 */
	public List<INSBCarmodelinfo> selectPageByStandardname(Map<String,Object> map);
	
	/**
	 * 按照车型名称查询车辆信息数量
	 * 
	 * @param standardname
	 * @return
	 */
	public long selectCountPageByStandardname(Map<String,Object> map);
	
	/**
	 * 按品牌系列查询车辆信息
	 * 
	 * @param standardname
	 * @return
	 */
	public List<INSBCarmodelinfo> selectPageByBrandName(Map<String,Object> map);
	
	/**
	 * 按品牌系列车辆信息数量
	 * 
	 * @param standardname
	 * @return
	 */
	public long selectCountPageByBrandName(Map<String,Object> map);
	/**
	 * 按车辆id
	 * 
	 * @param id
	 * @return
	 */
	public INSBCarmodelinfo selectByCarinfoId(String CarinfoId);
	@Deprecated
	public List<INSBCarmodelinfo> selectAll();
	
	public List<INSBCarmodelinfo> selectAll(Map<String, Object> params);
}