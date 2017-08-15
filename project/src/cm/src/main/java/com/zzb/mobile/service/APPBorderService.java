package com.zzb.mobile.service;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseService;
import com.zzb.cm.entity.INSBOrder;

public interface APPBorderService extends BaseService<INSBOrder> {
	
	public List<Object> borderList(String processInstanceId,String prvid);
	
	public List<Object> borderListByOrderId(String orderId);
	
	public List<Object> policyitem(String processInstanceId,String inscomcode);
	
	public List<Object> borderDelivery(String processInstanceId,String prvid);
	
	public String borderListToJson(String processInstanceId,String prvid);

}
