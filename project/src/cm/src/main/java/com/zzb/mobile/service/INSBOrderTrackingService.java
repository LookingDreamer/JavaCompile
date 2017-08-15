package com.zzb.mobile.service;

import com.cninsure.core.dao.BaseService;
import com.zzb.cm.entity.INSBOrder;
/*
 * 订单跟踪，显示某一订单的流程信息
 * 未完成,未完成。
 */
public interface INSBOrderTrackingService extends BaseService<INSBOrder>{
	public String showOrderTracking(String processInstanceId,String prvCode,String subInstanceId);
}
