package com.zzb.extra.service;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseService;
import com.zzb.cm.entity.INSBOrder;
import com.zzb.extra.model.MiniOrderQueryModel;

public interface INSBMiniOrderManageService extends BaseService<INSBOrder> {

    public Map<String, Object> queryOrderList(MiniOrderQueryModel queryModel,String deptid);

    public List<Map<Object,Object>> queryOrderPagingList(Map map);

}
