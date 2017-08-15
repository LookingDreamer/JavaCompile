package com.zzb.extra.service;

import com.cninsure.core.dao.BaseService;
import com.zzb.extra.controller.vo.AccountWithdrawVo;
import com.zzb.extra.entity.INSBAccountWithdraw;

import java.util.Map;

public interface INSBAccountWithdrawService extends BaseService<INSBAccountWithdraw> {

    String queryPagingList(Map<String, Object> map);

    public String putAccountWithdraw(AccountWithdrawVo withdrawVo);

    public void updateWithdrawStatus(INSBAccountWithdraw entity);

}