package com.zzb.extra.service;

import com.cninsure.core.dao.BaseService;
import com.zzb.extra.entity.INSBAccount;
import com.zzb.extra.entity.INSBAccountWithdraw;

public interface INSBAccountService extends BaseService<INSBAccount> {

    public INSBAccount initAccount(String agentid);

    public Boolean exist(String accountid);
}