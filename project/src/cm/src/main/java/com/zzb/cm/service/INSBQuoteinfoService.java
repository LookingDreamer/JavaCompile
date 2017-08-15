package com.zzb.cm.service;

import java.util.List;

import com.cninsure.core.dao.BaseService;
import com.zzb.cm.entity.INSBQuoteinfo;

public interface INSBQuoteinfoService extends BaseService<INSBQuoteinfo> {
	/**
	 * 通过子流程ID获得报价信息
	 * 
	 * @param workflowinstanceid
	 * @return
	 */
	public INSBQuoteinfo getQuoteinfoByWorkflowinstanceid(
			String workflowinstanceid);

	/**
	 * 获得报价信息
	 * @param quotetotalinfoid 
	 * @param Inscomcode
	 * @return
	 */
	public INSBQuoteinfo getQuoteinfo(String quotetotalinfoid, String inscomcode);
	
	public INSBQuoteinfo getByTaskidAndCompanyid(String taskid,String companyid);
	
	public List<INSBQuoteinfo> getQuoteinfosByInsbQuotetotalinfoid(String taskId);
}