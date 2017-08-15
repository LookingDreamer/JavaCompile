package com.zzb.extra.dao.impl;

/**
 * Created by Administrator on 2016/11/30.
 */
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.cm.entity.INSBOrder;
import com.zzb.extra.dao.INSBMiniOrderManageDao;

@Repository
public class INSBMiniOrderManageDaoImpl extends BaseDaoImpl<INSBOrder> implements
        INSBMiniOrderManageDao {

    @Override
    public long queryOrderPagingListCount(Map<String, Object> map) {
        return this.sqlSessionTemplate.selectOne("minipolicyandordermanage.queryOrderPagingListCount", map);
    }

    @Override
    public List<Map<Object, Object>> queryOrderPagingList(
            Map<String, Object> map) {
        return this.sqlSessionTemplate.selectList("minipolicyandordermanage.queryOrderPagingList", map);
    }

    @Override
    public List<Map<Object, Object>> queryPolicyInfoByOrderId(String orderid) {
        return this.sqlSessionTemplate.selectList("minipolicyandordermanage.queryPolicyInfoByOrderId", orderid);
    }

}
