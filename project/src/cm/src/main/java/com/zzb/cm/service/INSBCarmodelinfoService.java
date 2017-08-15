package com.zzb.cm.service;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseService;
import com.common.PagingParams;
import com.zzb.cm.controller.vo.CarModelInfoVO;
import com.zzb.cm.entity.INSBCarmodelinfo;

public interface INSBCarmodelinfoService extends BaseService<INSBCarmodelinfo> {
/**
 * 通过车辆信息的id找到车型信息
 * @param carinfoid
 * @return
 */
	public Map<String,Object> getCarmodelInfoByCarinfoId(String taskid, String carinfoid, String inscomcode, String useType);
	
	
	/**
	 * 修改车型信息
	 */
	public String updateCarModelInfoByInstanceid(CarModelInfoVO carModelInfoVO);
	
	/**
	 * 重选车型信息
	 */
//	public String reselectCarModelInfo(Map<String, Object> paramMap);
	
	/**
	 * 重选车型信息
	 */
	public String reselectCarModelInfo(PagingParams pagingParams, String standardfullname);


	public List<INSBCarmodelinfo> queryAll(int offset,int limit);
}