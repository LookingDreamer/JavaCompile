package com.zzb.conf.dao;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseDao;
import com.zzb.cm.entity.INSBOrder;

public interface INSBOrderManageDao extends BaseDao<INSBOrder> {

	long queryOrderPagingListCount(Map<String, Object> map);

	List<Map<Object, Object>> queryOrderPagingList(Map<String, Object> map);

	List<Map<Object, Object>> queryPolicyInfoByOrderId(String orderid);

}
