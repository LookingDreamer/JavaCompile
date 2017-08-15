package com.zzb.extra.service;

import com.cninsure.core.dao.BaseService;
import com.zzb.extra.entity.INSBMiniOrderTrace;

import java.util.Map;

public interface INSBMiniOrderTraceService extends BaseService<INSBMiniOrderTrace> {

    public String queryOrderList(Map<String,Object> map);

    public String queryOrderPaytime(String taskId);

    public int updateOrderOperateState(INSBMiniOrderTrace insbMiniOrderTrace);

    public int updateAgentOperateByTaskId(INSBMiniOrderTrace insbMiniOrderTrace);

    public int updateOrderTraceByTaskId(INSBMiniOrderTrace insbMiniOrderTrace);

    public String updateOrderTraceState(String taskid,String providercode,String taskState);

}