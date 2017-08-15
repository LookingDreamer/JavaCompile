package com.zzb.cm.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.cninsure.core.utils.StringUtil;
import com.zzb.cm.dao.RULE_engineDao;
import com.zzb.cm.entity.RULE_engine;
import com.zzb.cm.service.RULE_engineService;

import java.util.List;

@Service
@Transactional
public class RULE_engineServiceImpl extends BaseServiceImpl<RULE_engine> implements
        RULE_engineService {
    @Resource
    private RULE_engineDao rule_engineDao;

    @Override
    protected BaseDao<RULE_engine> getBaseDao() {
        return rule_engineDao;
    }

    @Override
    public void saveOrudpateRuleEngine(RULE_engine ruleEngine) throws Exception {
        if (ruleEngine != null && !StringUtil.isEmpty(ruleEngine.getRule_engine_id())) {
            RULE_engine old = null;
            RULE_engine condition = new RULE_engine();
            condition.setRule_engine_id(ruleEngine.getRule_engine_id());
            List<RULE_engine> ruleEngineList = rule_engineDao.selectList(condition);//根据rule_engine_id查出唯一一条记录
            if (ruleEngineList != null && ruleEngineList.size() > 0) {
                old = ruleEngineList.get(0);
            }
            if (old != null) {
                ruleEngine.setId(old.getId());//如果存在记录则用原本的id进行更新操作
                rule_engineDao.updateById(ruleEngine);
            } else {
                rule_engineDao.insert(ruleEngine);//如果不存在记录则进行新增操作
            }
        } else {
            throw new Exception("请录入正确的规则集ID！");
        }
    }

}