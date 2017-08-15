package com.zzb.cm.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.cm.controller.vo.OrderListVo;
import com.zzb.cm.dao.INSBQuotetotalinfoDao;
import com.zzb.cm.entity.INSBQuotetotalinfo;

@Repository
public class INSBQuotetotalinfoDaoImpl extends BaseDaoImpl<INSBQuotetotalinfo> implements
        INSBQuotetotalinfoDao {

    @Override
    public INSBQuotetotalinfo select(Map<String, Object> map) {
        return this.sqlSessionTemplate.selectOne(this.getSqlName("select"), map);
    }

    @Override
    public Map<String, Object> selectByTaskId(String taskid) {
        return this.sqlSessionTemplate.selectOne(this.getSqlName("selectByTaskId"), taskid);
    }

    @Override
    public List<Map<String, Object>> getInscomInfo(String taskid) {
        return this.sqlSessionTemplate.selectList(this.getSqlName("getInscomInfo"), taskid);
    }

    @Override
    public String selectByTaskId4TaskSet(String taskid) {
        return this.sqlSessionTemplate.selectOne(this.getSqlName("selectByTaskId4TaskSet"), taskid);
    }

    @Override
    public Map<String, Object> getAgentInfoByTaskId(String taskid) {
        return this.sqlSessionTemplate.selectOne(this.getSqlName("getAgentInfo"), taskid);
    }

    @Override
    public void updateQuoteDiscountAmount(double sumPrice, String taskInstanceId, String inscomcode) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("sumPrice", sumPrice);
        map.put("inscomcode", inscomcode);
        map.put("taskInstanceId", taskInstanceId);
        this.sqlSessionTemplate.selectOne(this.getSqlName("updateQuoteDiscountAmount"), map);
    }

    @Override
    public List<OrderListVo> getQuotetotalinfoByUserid(Map<String, Object> map) {
        return this.sqlSessionTemplate.selectList(this.getSqlName("getQuotetotalinfoByUserid"), map);
    }

    @Override
    public long getCountQuotetotalinfoByUserid(Map<String, Object> map) {
        return this.sqlSessionTemplate.selectOne(this.getSqlName("getCountQuotetotalinfoByUserid"), map);
    }

    @Override
    public List<OrderListVo> getQuotetotalinfoByParams(Map<String, Object> map) {
        return this.sqlSessionTemplate.selectList(this.getSqlName("getQuotetotalinfoByParams"), map);
    }

    @Override
    public long getCountQuotetotalinfoByParams(
            Map<String, Object> map) {
        return this.sqlSessionTemplate.selectOne(this.getSqlName("getCountQuotetotalinfoByParams"), map);
    }

    @Override
    public Map<String, String> selectAgentDataByTaskIdAndSub(Map<String, String> param) {
        return this.sqlSessionTemplate.selectOne(this.getSqlName("selectAgentDataByTaskIdAndSub"), param);
    }

    @Override
    public Map<String, String> selectAgentDataByTaskId(String taskId) {
        return this.sqlSessionTemplate.selectOne(this.getSqlName("selectAgentDataByTaskId"), taskId);
    }


    @Override
    public List<INSBQuotetotalinfo> selectTotal(Map<String, Object> map) {
        return this.sqlSessionTemplate.selectList(this.getSqlName("selectTotal"), map);
    }


    @Override
    public long selectTotalCount(Map<String, Object> map) {
        return this.sqlSessionTemplate.selectOne(this.getSqlName("selectTotalCount"), map);

    }

    @Override
    public int deleteByTaskid(String taskid) {
        return this.sqlSessionTemplate.delete(this.getSqlName("deleteByTaskid"), taskid);
    }

    @Override
    public int updateDeleteflagByTaskid(Map<String, Object> map) {
        return this.sqlSessionTemplate.update(this.getSqlName("updateDeleteflagByTaskid"), map);
    }


}