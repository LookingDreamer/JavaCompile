package com.zzb.warranty.service;

import com.cninsure.core.dao.BaseService;
import com.zzb.warranty.model.INSEOrder;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/9.
 */
public interface INSEOrderService extends BaseService<INSEOrder> {
    void updateOrder(String agentCode, String orderNo, List<String> couponCodes);

    void insertOrder(INSEOrder order);

    INSEOrder getOrderByOrderNoAndAgentCode(String agentCode, String orderno);

    Map<String, Object> getOrder(String agentCode, String orderno);

    INSEOrder getOrderByOrderNo(String orderno);

    List<Map<String, Object>> getOrderList(String agentcode, Integer orderstatus, int limit, long offset);

    Long selectCount(String agentcode, Integer orderstatus);

    List<Map<String, Object>> selectOrdersForCm(Map<String, Object> params);

    Long selectOrdersCountForCm(Map<String, Object> params);

    void updateOrderPromotionAmount(String orderNo, Double promotionAmount, Double paymentAmount);

    void updateOrderStatus(String orderNo, int status);
}
