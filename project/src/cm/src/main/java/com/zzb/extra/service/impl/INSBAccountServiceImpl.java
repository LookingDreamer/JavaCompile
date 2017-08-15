package com.zzb.extra.service.impl;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.cninsure.core.utils.UUIDUtils;
import com.zzb.conf.dao.INSBAgentDao;
import com.zzb.conf.entity.INSBAgent;
import com.zzb.extra.dao.INSBAccountDao;
import com.zzb.extra.entity.INSBAccount;
import com.zzb.extra.service.INSBAccountService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

@Service
@Transactional
public class INSBAccountServiceImpl extends BaseServiceImpl<INSBAccount> implements
        INSBAccountService {
    @Resource
    private INSBAgentDao insbAgentDao;

    @Resource
    private INSBAccountDao insbAccountDao;

    @Override
    protected BaseDao<INSBAccount> getBaseDao() {
        return insbAccountDao;
    }

    @Override
    public INSBAccount initAccount(String agentId) {
        INSBAgent insbAgent = insbAgentDao.selectById(agentId);
        if (insbAgent != null) {
            INSBAccount account = insbAccountDao.queryByAgentid(agentId);
            if (account == null) {
                account = new INSBAccount();
                account.setId(UUIDUtils.random());
                account.setAgentid(agentId);
                account.setStatus(1);
                account.setOperator("admin");
                account.setCreatetime(new Date());
                if (insbAccountDao.createAccount(account) > 0)
                    return account;
            } else
                return account;
        }
        return null;
    }

    @Override
    public Boolean exist(String accountid) {
        return insbAccountDao.existAccountId(accountid) > 0;
    }
}