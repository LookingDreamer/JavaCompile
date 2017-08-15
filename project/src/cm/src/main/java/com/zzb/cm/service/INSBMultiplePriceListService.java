package com.zzb.cm.service;

import java.util.Map;

import com.zzb.mobile.model.CommonModel;

public interface INSBMultiplePriceListService {

	CommonModel insuredConfList(String taskid, String inscomcode);

	Map<String, Object> getDeptInfo(String taskid, String inscomcode);

}
