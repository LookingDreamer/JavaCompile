package com.zzb.conf.service;

import java.util.List;

import com.cninsure.core.dao.BaseService;
import com.zzb.conf.controller.vo.DeptPayTypeVo;
import com.zzb.conf.entity.INSBAgreementpaymethod;

public interface INSBAgreementpaymethodService extends BaseService<INSBAgreementpaymethod> {
 
	public List<DeptPayTypeVo> getDeptPaymethod(String deptid,String agreementid,String providerid);
	
	public int delByAgreeid(String agreeid);
	
}