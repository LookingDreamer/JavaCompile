package com.zzb.extra.dao;

import com.cninsure.core.dao.BaseDao;
import com.zzb.extra.entity.INSBAccount;

public interface INSBAccountDao extends BaseDao<INSBAccount> {

    public int createAccount(INSBAccount account);

    public INSBAccount queryByAgentid(String agentid);

    public long existAccountId(String accountid);

}