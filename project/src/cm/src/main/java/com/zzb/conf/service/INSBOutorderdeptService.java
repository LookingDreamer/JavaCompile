package com.zzb.conf.service;

import java.util.List;

import com.cninsure.core.dao.BaseService;
import com.cninsure.system.entity.INSCUser;
import com.zzb.conf.entity.INSBOutorderdept;

public interface INSBOutorderdeptService extends BaseService<INSBOutorderdept> {
	
	public List<INSBOutorderdept> queryListVo(INSBOutorderdept query);
	 
	/**
	 * 协议新增出单网点
	 * @param outdept
	 * @param operator
	 */
	public void saveDeptIds(INSBOutorderdept outdept,INSCUser operator);
	
	/**
	 * 
	 * 通过协议id查询网点信息
	 * @param agreementId
	 * @return
	 */
	public List<String> getDeptByAgreementId(String agreementId);

	public INSBOutorderdept getOutorderdept(String agreementid,String deptid5);
}