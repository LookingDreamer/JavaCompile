package com.zzb.conf.service;

import java.util.List;

import com.cninsure.core.dao.BaseService;
import com.zzb.conf.controller.vo.DistributionVo;
import com.zzb.conf.entity.INSBDistribution;

public interface INSBDistributionService extends BaseService<INSBDistribution> {
	
	public List<INSBDistribution> getAllDistribution(String deptid,
			String agreementid, String providerid);
    
	public DistributionVo getDistribution(String agreementid,String providerid, String deptid);

	public void saveDistribution(DistributionVo distributionVo);
	

}