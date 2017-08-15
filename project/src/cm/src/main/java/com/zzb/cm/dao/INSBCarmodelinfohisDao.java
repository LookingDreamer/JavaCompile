package com.zzb.cm.dao;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseDao;
import com.zzb.cm.entity.INSBCarmodelinfohis;

public interface INSBCarmodelinfohisDao extends BaseDao<INSBCarmodelinfohis> {
	/**
	 * 按照车型名称查询车辆信息
	 * 
	 * @param map
	 * @return
	 */
	public List<INSBCarmodelinfohis> selectPageByStandardname(Map<String,Object> map);
	
	/**
	 * 按照车型名称查询车辆信息数量
	 * 
	 * @param map
	 * @return
	 */
	public long selectCountPageByStandardname(Map<String,Object> map);

	public void insertCarmodelhis(Map<String, Object> carinfohis);

	public INSBCarmodelinfohis selectModelInfoByTaskIdAndPrvId(Map<String, Object> map);

	public INSBCarmodelinfohis selectModelInfoByTaskIdAndPrvId(String taskid, String prvid);

}