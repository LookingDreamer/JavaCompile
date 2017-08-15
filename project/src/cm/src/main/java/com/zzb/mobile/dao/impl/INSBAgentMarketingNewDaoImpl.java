package com.zzb.mobile.dao.impl;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.mobile.dao.INSBAgentMarketingNewDao;
import com.zzb.mobile.entity.INSBAgentMarketingNew;
import org.springframework.stereotype.Repository;
import java.util.*;

/**
 * Created by HaiJuan.Lei on 2016/10/9.
 * 活动新注册代理人信息 数据库操作实现
 *
 */
@Repository
public class INSBAgentMarketingNewDaoImpl extends BaseDaoImpl<INSBAgentMarketingNew> implements INSBAgentMarketingNewDao {
    @Override
    public void insert(INSBAgentMarketingNew agentInfo){
        super.insert(agentInfo);
    }

    @Override
    public List<INSBAgentMarketingNew> getReferrals(String agentCode) {
        return this.sqlSessionTemplate.selectList(this.getSqlName("getReferrals"),agentCode);
    }

    @Override
    public void update(INSBAgentMarketingNew insbAgentMarketingNew) {
        this.sqlSessionTemplate.update(this.getSqlName("update"), insbAgentMarketingNew);
    }

    @Override
    public List<INSBAgentMarketingNew> getAgentInfo(String agentCode) {
        return this.sqlSessionTemplate.selectList(this.getSqlName("getAgentInfo"),agentCode);
    }

    @Override
    public int isSendRedPaper(String agentCode) {
        return this.sqlSessionTemplate.selectOne(this.getSqlName("isSendRedPaper"), agentCode);
    }


}
