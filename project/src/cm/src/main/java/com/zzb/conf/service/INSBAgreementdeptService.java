package com.zzb.conf.service;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseService;
import com.zzb.conf.entity.INSBAgreementdept;
import com.zzb.conf.entity.INSBAgreementprovider;
import com.zzb.conf.entity.INSBChannel;

public interface INSBAgreementdeptService extends BaseService<INSBAgreementdept> {
 
	public List<String> getINSBAgreementdept(String agreementId);
	
	public List<Map<String,String>> getPrvider(INSBChannel channel); 
	
	public List<INSBAgreementprovider> checkedPrvider(INSBChannel channel);
	
	public List<Object> getdeptname(String agreementid, String providerid);
	
	public int delByAgreeid(String agreeid);
	
}