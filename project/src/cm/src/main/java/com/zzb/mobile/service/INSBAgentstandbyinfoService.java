package com.zzb.mobile.service;

import java.util.Map;

import com.cninsure.core.dao.BaseService;
import com.zzb.mobile.entity.INSBAgentstandbyinfo;
import com.zzb.mobile.model.CommonModel;

public interface INSBAgentstandbyinfoService extends BaseService<INSBAgentstandbyinfo> {

	CommonModel getQuickBrushTypeList();

	CommonModel getQuickBrushType(Map<Object, Object> hashMap);

	CommonModel saveOrUpdataQuickBrushType(Map<Object, Object> hashMap);
}