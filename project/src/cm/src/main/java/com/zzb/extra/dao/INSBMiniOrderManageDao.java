package com.zzb.extra.dao;

import java.util.List;
import java.util.Map;
import com.cninsure.core.dao.BaseDao;
import com.zzb.cm.entity.INSBOrder;
/**
 * Created by Administrator on 2016/11/30.
 */
public interface INSBMiniOrderManageDao extends BaseDao<INSBOrder> {
    long queryOrderPagingListCount(Map<String, Object> map);

    List<Map<Object, Object>> queryOrderPagingList(Map<String, Object> map);

    List<Map<Object, Object>> queryPolicyInfoByOrderId(String orderid);
}
