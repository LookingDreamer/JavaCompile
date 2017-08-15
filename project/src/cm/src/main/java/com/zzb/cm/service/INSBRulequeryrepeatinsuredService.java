package com.zzb.cm.service;

import com.cninsure.core.dao.BaseService;
import com.zzb.cm.entity.INSBRulequeryrepeatinsured;

import java.util.List;

public interface INSBRulequeryrepeatinsuredService extends BaseService<INSBRulequeryrepeatinsured> {

    public void sortPolicies(List<INSBRulequeryrepeatinsured> policieslist);
}