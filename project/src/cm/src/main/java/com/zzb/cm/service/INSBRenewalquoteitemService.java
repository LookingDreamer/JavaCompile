package com.zzb.cm.service;

import com.cninsure.core.dao.BaseService;
import com.zzb.cm.entity.INSBRenewalquoteitem;

import java.util.List;

public interface INSBRenewalquoteitemService extends BaseService<INSBRenewalquoteitem> {

    public int deleteByTask(String taskid, String inscomcode);

    public int save(List<INSBRenewalquoteitem> quoteitemList);
}