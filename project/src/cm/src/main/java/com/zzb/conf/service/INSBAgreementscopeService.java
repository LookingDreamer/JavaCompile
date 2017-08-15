package com.zzb.conf.service;

import java.util.List;

import com.cninsure.core.dao.BaseService;
import com.cninsure.system.entity.INSCUser;
import com.zzb.conf.entity.INSBAgreementscope;
import com.zzb.conf.entity.INSBOutorderdept;
import com.zzb.mobile.model.CommonModel;
import com.zzb.mobile.model.InsbSaveScopeParam;

public interface INSBAgreementscopeService extends BaseService<INSBAgreementscope> {

	public List<INSBAgreementscope> selectAdrAndDeptName(INSBAgreementscope query);
	/**
	 * 查询所选区域下的所有网点
	 * @param inscdept
	 * @return
	 */
	public List<String> selectAreaDeptid(String city);
	
	/**
	 * 保存协议范围
	 * @param agreementId
	 * @return
	 */
	public void saveAgreementScop(INSCUser operator, INSBAgreementscope scope,INSBOutorderdept conndept);
	/**
	 * 根据city关联网点
	 * @param agreementId
	 * @return
	 */
	public CommonModel saveAgreementScopByCity(INSCUser operator,
			InsbSaveScopeParam model);
	
}