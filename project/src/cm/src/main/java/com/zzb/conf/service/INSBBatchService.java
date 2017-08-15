package com.zzb.conf.service;

import com.cninsure.core.dao.BaseService;
import com.zzb.conf.entity.INSBBatch;

public interface INSBBatchService extends BaseService<INSBBatch> {

	String initInsbBatch(INSBBatch insbBatch);

}