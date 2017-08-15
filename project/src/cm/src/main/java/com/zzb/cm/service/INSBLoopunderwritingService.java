package com.zzb.cm.service;

import com.cninsure.core.dao.BaseService;
import com.zzb.cm.entity.INSBLoopunderwriting;

import java.util.Map;

public interface INSBLoopunderwritingService extends BaseService<INSBLoopunderwriting> {

    public String searchList(Map<String, Object> paramMap);
}