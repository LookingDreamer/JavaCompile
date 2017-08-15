package com.zzb.mobile.dao.impl;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.mobile.dao.INSBCoreAgentDao;
import com.zzb.mobile.entity.INSBCoreAgent;
import org.springframework.stereotype.Repository;

/**
 * Created by HaiJuan.Lei on 2016/10/19.
 */
@Repository
public class INSBCoreAgentDaoImpl  extends BaseDaoImpl<INSBCoreAgent> implements INSBCoreAgentDao {


    @Override
    public int getAgentCnt(String agentCode) {
        return this.sqlSessionTemplate.selectOne(this.getSqlName("getAgentCnt"), agentCode);
    }
}
