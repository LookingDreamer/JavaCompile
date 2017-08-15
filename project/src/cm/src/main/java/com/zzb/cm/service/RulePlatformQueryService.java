package com.zzb.cm.service;

import com.zzb.mobile.model.CommonModel;

import net.sf.json.JSONObject;

public interface RulePlatformQueryService {

	CommonModel startRuleQuery(String json);

	String saveRuleQueryInfo(String json);
	/**
	 * 定时器到期需要调用的接口
	 * @param taskid
	 * @param inscomcode
	 * @param subwkid
	 */
	void nextTodoRuleQuery(String taskid,String inscomcode,String subwkid);
	
	/**
	 * 江苏流程 平台查询回调
	 * @param json
	 * @return
	 */
	public  String saveRuleQueryInfoJiangSu(String json);
	
	/**
	 * 人工修改平台信息
	 * @param taskid
	 * @param jsonObject
	 */
	public void saveRulequeryotherinfo(String taskid,JSONObject jsonObject);
	/**
	 * 人工修改平台信息-平台起保日期终保日期
	 * @param taskid
	 * @param jsonObject
	 */
	public void saveAllPolicies(String taskid,Object sypolicies,Object jqpolicies);
}
