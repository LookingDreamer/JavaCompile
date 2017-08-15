package com.zzb.conf.service;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseService;
import com.zzb.cm.entity.INSBCarinfo;
import com.zzb.cm.entity.INSBPerson;
import com.zzb.conf.entity.INSBPolicyitem;
import com.zzb.model.PolicyDetailedModel;

public interface INSBPolicyManageService extends BaseService<INSBPolicyitem>{

	String queryPagingList(Map<String, Object> map,String comcode);

	PolicyDetailedModel queryPolictDetailedByPId(String id);

	PolicyDetailedModel queryOrderDetailedByOId(String id,String proid,String type);

	INSBCarinfo queryCarInfoByTaskid(String taskid);

	INSBPerson queryPersonInfoByTaskid(String taskid,String flag,String inscomcode);

	List<INSBPerson> queryDriverPersonByTaskid(String taskid);
	
	List<PolicyDetailedModel> queryImgInfo(String id);
}
