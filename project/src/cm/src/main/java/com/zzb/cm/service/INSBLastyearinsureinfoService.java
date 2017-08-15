package com.zzb.cm.service;

import com.cninsure.core.dao.BaseService;
import com.zzb.cm.entity.INSBLastyearinsureinfo;
import com.zzb.mobile.model.CommonModel;
import com.zzb.mobile.model.lastindanger.LastClaimBackInfo;

public interface INSBLastyearinsureinfoService extends BaseService<INSBLastyearinsureinfo> {

	CommonModel queryLastYearClainInfo(String taskid);

	CommonModel saveLastYearClaimsInfo(String taskid, LastClaimBackInfo lastClaimBackInfo);


}