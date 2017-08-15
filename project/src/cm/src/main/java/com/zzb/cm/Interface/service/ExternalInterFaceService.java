package com.zzb.cm.Interface.service;

import java.util.Map;

import com.zzb.mobile.model.IDCardModel;

public interface ExternalInterFaceService {

	/**
	 * 机构在某个时间段报价总数量, 对外(供大数据)接口
	 * @param deptInnerCode 机构内部编码
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public String queryDeptAllQuoteCount(String deptInnerCode, String startTime, String endTime);
	
	/**
	 * task-1152(供大数据调用) 这段时间内，cm向cif推送的所有保单个数，及对应的保单号（含商业险保单号和交强险保单号）和车牌号
	 * @return
	 */
	public String getAllPolicynoByTimePeriod(String startTime, String endTime);
	
	public Map<String, String> checkIDCardAndGetPinCode(IDCardModel idcardmodel, String from);
	
	public String savePinCodeFromFairy(String json);
	
	public String getPincodeResultInfo(String agentId, String taskId, String inscomcode);
	
	public Map<String, String> commitPinCode(String agentId, String taskId, String inscomcode, String pincode);
	
	public String reapplyPinCode(String agentId, String taskId, String inscomcode);
	
	public String getElecPolicyPathInfo(String taskId);
}