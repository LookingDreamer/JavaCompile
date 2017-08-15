package com.zzb.extra.service.impl;

import javax.annotation.Resource;

import com.cninsure.core.utils.UUIDUtils;
import com.zzb.extra.controller.vo.CommissionVo;
import com.zzb.extra.entity.INSBConditions;
import com.zzb.extra.model.AgentTaskModel;
import com.zzb.extra.util.ParamUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.extra.dao.INSBCommissionDao;
import com.zzb.extra.entity.INSBCommission;
import com.zzb.extra.service.INSBCommissionService;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class INSBCommissionServiceImpl extends BaseServiceImpl<INSBCommission> implements
        INSBCommissionService {
    @Resource
    private INSBCommissionDao insbCommissionDao;

    @Override
    protected BaseDao<INSBCommission> getBaseDao() {
        return insbCommissionDao;
    }

    @Override
    public String queryPagingList(Map<String, Object> map) {
        long total = insbCommissionDao.queryPagingListCount(map);
        List<Map<Object, Object>> infoList = insbCommissionDao.queryPagingList(map);
        return ParamUtils.resultMap(total, infoList);
    }

    @Override
    public Map<String, Object> queryPagingListReturnMap(Map<String, Object> map) {
        long total = insbCommissionDao.queryPagingListCount(map);
        List<Map<Object, Object>> infoList = insbCommissionDao.queryPagingList(map);
        Map<String,Object> resultMap = new HashMap<String,Object>();
        resultMap.put("total",total);
        resultMap.put("rows",infoList);
        return resultMap;
    }

    @Override
    public List<INSBCommission> queryCommissions(AgentTaskModel agentTaskModel) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("taskid", agentTaskModel.getTaskid());
        map.put("agreementid", agentTaskModel.getAgreementid());
        map.put("commissionFlag",agentTaskModel.getCommissionFlag());
        return insbCommissionDao.queryCommissions(map);
    }

    @Override
    public Boolean isLocked(AgentTaskModel agentTaskModel) {
        return insbCommissionDao.isLocked(agentTaskModel.getTaskid());
    }

    @Override
    public Boolean lockTaskCommission(AgentTaskModel agentTaskModel) {
        return insbCommissionDao.lockTaskCommission(agentTaskModel.getTaskid()) > 0;
        //任务99关联的mini营销活动代码发布cm后台
    }

    @Override
    public Boolean isCompleted(AgentTaskModel agentTaskModel) {
        return insbCommissionDao.isCompleted(agentTaskModel.getTaskid());
    }

    @Override
    public String saveCommission(INSBCommission commission) {
        int result = 0;
        if (commission.getId() == null || commission.getId().equals("")) {
            commission.setId(UUIDUtils.random());
            commission.setCreatetime(new Date());
            result = insbCommissionDao.insertCommission(commission);
        } else {
            commission.setModifytime(new Date());
            result = insbCommissionDao.updateCommission(commission);
        }

        if (result > 0)
            return ParamUtils.resultMap(true, "操作成功");
        else
            return ParamUtils.resultMap(false, "操作失败");
    }

    @Override
    public void deleteCommissionByTask(AgentTaskModel agentTaskModel) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("taskid", agentTaskModel.getTaskid());
        map.put("agreementid", agentTaskModel.getAgreementid());
        map.put("commissionFlag",agentTaskModel.getCommissionFlag());
        insbCommissionDao.deleteCommissionByTask(map);
    }

    @Override
    public Boolean existRate(Map<String, Object> map) {
        return insbCommissionDao.existRate(map);
    }

}