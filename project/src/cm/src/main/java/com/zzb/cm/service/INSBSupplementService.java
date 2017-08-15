package com.zzb.cm.service;

import java.util.List;

import com.cninsure.core.dao.BaseService;
import com.zzb.cm.entity.INSBSupplement;

public interface INSBSupplementService extends BaseService<INSBSupplement> {

	public List<INSBSupplement> getSupplementsBytaskid(String taskid,List<String> inscomcodeList);
	public List<INSBSupplement> getSupplementsBytaskid(String taskid,String inscom); 
	public void updateBykeyidandproviderValue(INSBSupplement model);

}