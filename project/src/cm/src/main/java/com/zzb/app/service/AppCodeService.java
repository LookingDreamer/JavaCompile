package com.zzb.app.service;

import java.util.List;

import com.cninsure.core.dao.BaseService;
import com.cninsure.system.entity.INSCCode;
import com.zzb.model.AppCodeModel;

public interface AppCodeService extends BaseService<INSCCode> {
	
	
	/**
	 * 通过字段类型查询相应真实值表现值
	 * 
	 * @param types
	 * @return
	 */
	public List<AppCodeModel> queryByCodetype(String types);

}
