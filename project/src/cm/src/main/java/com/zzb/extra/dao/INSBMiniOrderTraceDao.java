package com.zzb.extra.dao;

import com.cninsure.core.dao.BaseDao;
import com.zzb.extra.entity.INSBMiniOrderTrace;

import java.util.List;
import java.util.Map;

public interface INSBMiniOrderTraceDao extends BaseDao<INSBMiniOrderTrace> {

    public List<Map<String,Object>> queryOrderList(Map<String,Object> map);

    public long queryOrderListCount(Map<String, Object> map);

    public int updateOrderOperateState(INSBMiniOrderTrace insbMiniOrderTrace);

    public int updateAgentOperateByTaskId(INSBMiniOrderTrace insbMiniOrderTrace);

    public int updateOrderTraceByTaskId(INSBMiniOrderTrace insbMiniOrderTrace);
}