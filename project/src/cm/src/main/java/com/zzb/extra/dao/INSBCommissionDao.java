package com.zzb.extra.dao;

import com.cninsure.core.dao.BaseDao;
import com.zzb.extra.entity.INSBCommission;
import com.zzb.extra.entity.INSBConditions;

import java.util.List;
import java.util.Map;

public interface INSBCommissionDao extends BaseDao<INSBCommission> {
    public long queryPagingListCount(Map<String, Object> map);

    public List<Map<Object, Object>> queryPagingList(Map<String, Object> map);

    public List<INSBCommission> queryCommissions(Map<String, Object> map);

    public Boolean isLocked(String taskid);

    public Boolean isCompleted(String taskid);

    public int insertCommission(INSBCommission commission);

    public int updateCommission(INSBCommission commission);

    public int deleteCommissionByTask(Map<String, Object> map);

    public int lockTaskCommission(String taskid);
    //任务99关联的mini营销活动代码发布cm后台

    public int completeTask(String taskid);

    public Boolean existRate(Map<String, Object> map);

    public List<Map<String, Object>> queryCommissionInfo(Map<String, Object> map);

    public long  queryCommissionInfoCount(Map<String, Object> map);

    public List<Map<String, Object>> queryCommissionInfoForBC(Map<String, Object> map);

    public long  queryCommissionInfoForBCCount(Map<String, Object> map);
}