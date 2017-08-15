package com.zzb.extra.dao.impl;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.extra.dao.INSBCommissionDao;
import com.zzb.extra.entity.INSBCommission;

import java.util.List;
import java.util.Map;

@Repository
public class INSBCommissionDaoImpl extends BaseDaoImpl<INSBCommission> implements
        INSBCommissionDao {
    @Override
    public List<Map<Object, Object>> queryPagingList(Map<String, Object> map) {
        return this.sqlSessionTemplate.selectList("queryCommissionList", map);
    }

    @Override
    public long queryPagingListCount(Map<String, Object> map) {
        return this.sqlSessionTemplate.selectOne("queryCommissionListCount", map);
    }

    @Override
    public List<INSBCommission> queryCommissions(Map<String, Object> map) {
        return this.sqlSessionTemplate.selectList("queryCommissions", map);
    }

    @Override
    public Boolean isLocked(String taskid) {
        int result = this.sqlSessionTemplate.selectOne("queryLockedCommissionCountByTaskId", taskid);
        return result > 0;
    }

    @Override
    public Boolean isCompleted(String taskid) {
        int result = this.sqlSessionTemplate.selectOne("queryCompletedCommissionCountByTaskId", taskid);
        return result > 0;
    }

    @Override
    public int insertCommission(INSBCommission commission) {
        return this.sqlSessionTemplate.insert(this.getSqlName("insert"), commission);
    }

    @Override
    public int updateCommission(INSBCommission commission) {
        return this.sqlSessionTemplate.update(this.getSqlName("updateById"), commission);
    }

    @Override
    public int deleteCommissionByTask(Map<String, Object> map) {
        return this.sqlSessionTemplate.delete(this.getSqlName("deleteByTask"), map);
    }

    @Override
    public int lockTaskCommission(String taskid) {
        return this.sqlSessionTemplate.update(this.getSqlName("lockByTaskId"), taskid);
        //任务99关联的mini营销活动代码发布cm后台
    }

    @Override
    public int completeTask(String taskid) {
        return this.sqlSessionTemplate.update(this.getSqlName("completeByTaskId"), taskid);
    }

    @Override
    public Boolean existRate(Map<String, Object> map) {
        int result = this.sqlSessionTemplate.selectOne("queryCountByRateId", map);
        return result > 0;
    }
    @Override
    public List<Map<String, Object>> queryCommissionInfo(Map<String, Object> map){
        return this.sqlSessionTemplate.selectList("INSBCommission_queryCommissionInfo", map);
    }
    @Override
    public long  queryCommissionInfoCount(Map<String, Object> map){
        return this.sqlSessionTemplate.selectOne("INSBCommission_queryCommissionInfoCount", map);
    }

    @Override
    public List<Map<String, Object>> queryCommissionInfoForBC(Map<String, Object> map){
        return this.sqlSessionTemplate.selectList("INSBCommission_queryCommissionInfoForBC", map);
    }
    @Override
    public long  queryCommissionInfoForBCCount(Map<String, Object> map){
        return this.sqlSessionTemplate.selectOne("INSBCommission_queryCommissionInfoForBCCount", map);
    }
}