package com.zzb.extra.dao.impl;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.extra.dao.INSBAccountDao;
import com.zzb.extra.entity.INSBAccount;
import org.springframework.stereotype.Repository;

@Repository
public class INSBAccountDaoImpl extends BaseDaoImpl<INSBAccount> implements
        INSBAccountDao {

    @Override
    public int createAccount(INSBAccount account) {
        return this.sqlSessionTemplate.insert(this.getSqlName("insert"), account);
    }

    @Override
    public INSBAccount queryByAgentid(String agentid) {
        return this.sqlSessionTemplate.selectOne(this.getSqlName("selectByAgentId"), agentid);
    }

    @Override
    public long existAccountId(String accountid) {
        return this.sqlSessionTemplate.selectOne(this.getSqlName("existAccountId"), accountid);
    }
}