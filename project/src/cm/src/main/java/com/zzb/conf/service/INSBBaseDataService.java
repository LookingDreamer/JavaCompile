package com.zzb.conf.service;

import java.util.List;

import com.cninsure.core.dao.BaseService;
import com.zzb.conf.entity.INSBBaseData;


public interface INSBBaseDataService extends BaseService<INSBBaseData> {

	public List<INSBBaseData> queryBaseDataListByModel(INSBBaseData model,int page,int rows );
	public String queryBaseDataList(int page,int rows);
	public int addBaseData(INSBBaseData model);
	public int removeBaseData(String id);
	public int modifyBaseData(INSBBaseData baseDataModel);
}
