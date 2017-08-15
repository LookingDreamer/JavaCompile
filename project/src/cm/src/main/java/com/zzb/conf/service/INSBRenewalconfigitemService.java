package com.zzb.conf.service;

import com.cninsure.core.dao.BaseService;
import com.cninsure.system.entity.INSCUser;
import com.zzb.conf.entity.INSBRenewalconfigitem;

import java.util.List;

public interface INSBRenewalconfigitemService extends BaseService<INSBRenewalconfigitem> {

    public int deleteByAgreementid(String agreementid);

    public int save(String agreementid, List<String> configItemcodes, INSCUser operator);
}