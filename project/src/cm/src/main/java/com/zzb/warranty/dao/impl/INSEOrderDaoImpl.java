package com.zzb.warranty.dao.impl;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.warranty.dao.INSEOrderDao;
import com.zzb.warranty.model.INSEOrder;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/10.
 */
@Repository
public class INSEOrderDaoImpl extends BaseDaoImpl<INSEOrder> implements INSEOrderDao {

    @Override
    public List<Map<String, Object>> getMyOrderList(Map<String, Object> params) {
        return sqlSessionTemplate.selectList(this.getSqlName("myOrderList"), params);
    }

    @Override
    public Long selectCount(Map<String, Object> params) {
        return (Long) this.sqlSessionTemplate.selectOne(this.getSqlName("selectCount"), params);
    }

    @Override
    public INSEOrder getOrderByOrderNo(String orderno) {
        return this.sqlSessionTemplate.selectOne(getSqlName("getOrderByOrderNo"), orderno);
    }

    @Override
    public INSEOrder getOrderByOrderNoAndAgentCode(String orderNo, String agentCode) {
        Map<String, Object> params = new HashMap<>();
        params.put("orderNo", orderNo);
        params.put("agentCode", agentCode);
        return this.sqlSessionTemplate.selectOne(getSqlName("getOrderByOrderNoAndAgentCode"), params);
    }

    @Override
    public List<Map<String, Object>> selectOrdersForCm(Map<String, Object> params, RowBounds rowBounds) {

        return this.sqlSessionTemplate.selectList(this.getSqlName("selectOrdersForCm"), params, rowBounds);
    }

    @Override
    public Long selectOrdersCountForCm(Map<String, Object> params) {
        return this.sqlSessionTemplate.selectOne(this.getSqlName("selectOrdersCountForCm"), params);
    }

    @Override
    public void updateOrderPromotionAmount(String orderNo, Double promotionAmount, Double paymentAmount) {
        Map<String, Object> params = new HashMap<>();
        params.put("promotionAmount", promotionAmount);
        params.put("paymentAmount", paymentAmount);
        params.put("orderNo", orderNo);
        params.put("modifyTime", new Date());
        this.sqlSessionTemplate.update(getSqlName("updateOrderPromotionAmount"), params);
    }

    @Override
    public Map<String, Object> selectOne(Map<String, Object> params) {
        return sqlSessionTemplate.selectOne(this.getSqlName("selectOne"), params);
    }

    @Override
    public void updateOrderStatus(String orderNo, int status) {
        Map<String, Object> params = new HashMap<>();
        params.put("orderNo", orderNo);
        params.put("orderStatus", status);
        params.put("modifyTime", new Date());
        sqlSessionTemplate.update(getSqlName("updateOrderStatus"), params);
    }
}
