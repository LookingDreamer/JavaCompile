package com.zzb.conf.service;

import com.cninsure.core.dao.BaseService;
import com.cninsure.system.entity.INSCUser;
import com.zzb.conf.entity.INSBAgreementarea;
import com.zzb.conf.entity.INSBAgreementzone;
import com.zzb.mobile.model.CommonModel;
import com.zzb.mobile.model.InsbSaveScopeParam;

public interface INSBAgreementzoneService extends BaseService<INSBAgreementzone> {

	void savezones(INSCUser operator, InsbSaveScopeParam model);

	CommonModel getZone(String agreementid);
 
}