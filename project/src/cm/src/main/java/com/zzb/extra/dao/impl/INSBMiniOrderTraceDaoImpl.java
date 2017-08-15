package com.zzb.extra.dao.impl;

import com.zzb.extra.entity.INSBMiniOrderTrace;
import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.extra.dao.INSBMiniOrderTraceDao;

import java.util.List;
import java.util.Map;

@Repository
public class INSBMiniOrderTraceDaoImpl extends BaseDaoImpl<INSBMiniOrderTrace> implements
		INSBMiniOrderTraceDao {
	public List<Map<String,Object>> queryOrderList(Map<String,Object> map){
		return this.sqlSessionTemplate.selectList("INSBMiniOrderTrace_queryOrderList", map);
	}

	public long queryOrderListCount(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectOne("INSBMiniOrderTrace_queryOrderListCount", map);
	}

	public int updateOrderOperateState(INSBMiniOrderTrace insbMiniOrderTrace){
		return this.sqlSessionTemplate.update("INSBMiniOrderTrace_updateOrderOperateState",insbMiniOrderTrace);
	}

	public int updateAgentOperateByTaskId(INSBMiniOrderTrace insbMiniOrderTrace){
		return this.sqlSessionTemplate.update("INSBMiniOrderTrace_updateAgentOperateByTaskId",insbMiniOrderTrace);
	}

	public int updateOrderTraceByTaskId(INSBMiniOrderTrace insbMiniOrderTrace){
		return this.sqlSessionTemplate.update("INSBMiniOrderTrace_updateOrderTraceByTaskId",insbMiniOrderTrace);
	}


}