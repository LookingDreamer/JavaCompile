package com.zzb.conf.service;

import java.util.List;

import com.cninsure.core.dao.BaseService;
import com.zzb.conf.entity.INSBRiskitem;

public interface INSBRiskitemService extends BaseService<INSBRiskitem> {

	public Long queryCountVo(INSBRiskitem riskitem);

	public List<INSBRiskitem> queryListVo(INSBRiskitem riskitem);
	
	/**
	 * 按照修改时间查找数据
	 * 
	 * @param modifydate
	 * @return
	 */
	public List<INSBRiskitem> selectByModifyDate(String modifydate);
	@Deprecated
	public List<INSBRiskitem> queryAll();

}	