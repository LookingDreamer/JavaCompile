package com.zzb.conf.service;

import java.util.Map;

import com.cninsure.core.dao.BaseService;
import com.zzb.cm.entity.INSBOrder;
import com.zzb.model.OrderQueryModel;

public interface INSBOrderManageService extends BaseService<INSBOrder> {

	Map<String, Object> queryOrderList(OrderQueryModel queryModel,String deptid);

}
