package com.zzb.conf.service;

import com.cninsure.core.dao.BaseService;
import com.zzb.conf.entity.INSBOrderdelivery;
import java.util.Map;

public interface INSBOrderdeliveryService extends BaseService<INSBOrderdelivery> {

    Map<String, Object> getOrderDeliveryDetail(String taskId, String insComCode);
}