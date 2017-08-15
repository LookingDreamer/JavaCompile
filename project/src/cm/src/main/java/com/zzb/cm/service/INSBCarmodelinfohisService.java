package com.zzb.cm.service;

import com.cninsure.core.dao.BaseService;
import com.zzb.cm.entity.INSBCarmodelinfohis;

public interface INSBCarmodelinfohisService extends BaseService<INSBCarmodelinfohis> {

	INSBCarmodelinfohis selectCarmodelinfoByTaskIdAndCode(String taskid,
			String inscomcode);

}