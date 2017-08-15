package com.zzb.conf.dao;

import com.cninsure.core.dao.BaseDao;
import com.zzb.conf.entity.INSBRenewalitem;

import java.util.List;

public interface INSBRenewalitemDao extends BaseDao<INSBRenewalitem> {

    public List<INSBRenewalitem> selectAllByCodes(List<String> codes);
    @Deprecated
	public List<INSBRenewalitem> selectAll();
}