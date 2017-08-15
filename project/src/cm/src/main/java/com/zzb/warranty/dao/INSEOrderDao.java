package com.zzb.warranty.dao;

import com.cninsure.core.dao.BaseDao;
import com.zzb.warranty.model.INSEOrder;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

public interface INSEOrderDao extends BaseDao<INSEOrder> {
    Map<String, Object> selectOne(Map<String, Object> params);

    List<Map<String, Object>> getMyOrderList(Map<String, Object> params);

    Long selectCount(Map<String, Object> params);

    INSEOrder getOrderByOrderNo(String orderno);

    INSEOrder getOrderByOrderNoAndAgentCode(String orderNo, String agentCode);

    List<Map<String, Object>> selectOrdersForCm(Map<String, Object> params, RowBounds rowBounds);

    Long selectOrdersCountForCm(Map<String, Object> params);

    void updateOrderPromotionAmount(String orderNo, Double promotionAmount, Double paymentAmount);

    void updateOrderStatus(String orderNo, int status);
}