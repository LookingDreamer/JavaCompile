package com.zzb.cm.Interface.service;

import java.util.List;
import java.util.Map;
public interface CoreInterFaceService { 
	/**
	 * 核心查询接口
	 * @param orgCode 法人公司编码
	 * @param companyId 保险公司编码
	 * @param policyCode 保单号
	 * @param plateNum 车牌
	 * @param insuredName 保人姓名
	 * @return
	 */
	public Map<String,Object> insurance(String orgCode,String companyId,String policyCode,String plateNum,String insuredName);
	/**
	 * 核心反向关闭接口
	 * @param saveJson
	 * @return
	 */
	public List<Map<String,Object>> save(String saveJson);
	/**
	 * 推送到核心接口
	 * @param taskid
	 * @param companyid
	 * @return
	 */
	public String pushtocore(String taskid, String companyid);
	public String callback(String json);
	public String closstask(String taskid, String inscomcode);
	public String validatBeforpush(String taskid, String inscomcode);
	/**
	 * 请求费用政策系统，获取费用政策信息和费用结果信息
	 * @param requestJson
	 * @return resultJson
	 */
	public String feeserverinfo(String requestJson);
}