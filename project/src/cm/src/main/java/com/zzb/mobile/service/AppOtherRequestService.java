package com.zzb.mobile.service;

import com.zzb.mobile.model.CommonModel;
import com.zzb.mobile.model.IsuredQuotationInfo;
import com.zzb.mobile.model.WorkFlowRuleInfo;

public interface AppOtherRequestService {

	CommonModel queryInsuredQuoteInfo(IsuredQuotationInfo model);

	CommonModel saveWorkFlowRuleInfo(WorkFlowRuleInfo model);
	
	/**
	 * 非费改调用的接口
	 * @param taskid
	 * @param inscomcode
	 * @return  0 数据库中存在数据（走人工规则报价）   1 不存在数据  自动规则
	 */
	int selectWorkFlowRuleInfoFfg(String taskid, String inscomcode); 
	
	int selectWorkFlowRuleInfoGZ(String taskid, String inscomcode);

	int selectWorkFlowRuleInfoRG(String taskid, String inscomcode);

	void updateClosedWorkFlowRuleInfoGZ(String taskid, String inscomcode);
	
	void updateClosedWorkFlowRuleInfoRG(String taskid, String inscomcode);
}
