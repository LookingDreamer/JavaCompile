package com.zzb.mobile.service;

import com.zzb.mobile.entity.LastYearRiskInfo;
import com.zzb.mobile.model.CommonModel;

public interface AppLastYearRiskInfoService {

	/**
	 * 获取上年险种信息接口
	 */
	public CommonModel getLastYearRiskInfo(LastYearRiskInfo lastYearRiskInfo);

}
