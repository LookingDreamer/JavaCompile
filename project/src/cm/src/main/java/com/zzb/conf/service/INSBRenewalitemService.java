package com.zzb.conf.service;

import com.cninsure.core.dao.BaseService;
import com.zzb.conf.entity.INSBRenewalitem;

import java.util.List;

public interface INSBRenewalitemService extends BaseService<INSBRenewalitem> {

    public List<INSBRenewalitem> selectAllByCodes(List<String> codes);
    @Deprecated
	public List<INSBRenewalitem> queryAll();

}