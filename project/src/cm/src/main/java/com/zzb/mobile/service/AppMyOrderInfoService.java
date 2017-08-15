package com.zzb.mobile.service;

import com.cninsure.core.dao.BaseService;
import com.zzb.cm.entity.INSBInvoiceinfo;
import com.zzb.cm.entity.INSBOrder;
import com.zzb.mobile.model.policyoperat.EditPolicyInfoParam;

public interface AppMyOrderInfoService extends BaseService<INSBOrder> {
	/**
	 * 提交投保前获得简要的基本信息显示接口 
	 * */
	public String getBriefTaskInfoBeforePolicy(String processInstanceId, String inscomcode);
	
	/**
	 * 提交修改投保信息接口
	 * */
	public String editPolicyInfoBeforePolicy(EditPolicyInfoParam editPolicyInfoParam);
	
	/**
	 * 提交投保前获得被保人信息接口
	 * */
	public String getInsuredInfoBeforePolicy(String processInstanceId, String inscomcode);
	
	/**
	 * 提交投保前修改被保人信息接口
	 * */
	public String editInsuredInfoBeforePolicy(String processInstanceId, String inscomcode, String operator, String insuredInfoJSON);

	/**
	 * 提交投保前获得投保人信息接口
	 * */
	public String getApplicantInfoBeforePolicy(String processInstanceId, String inscomcode);
	
	/**
	 * 提交投保前修改投保人信息接口
	 * */
	public String editApplicantInfoBeforePolicy(String processInstanceId, String inscomcode, String operator, String applicantInfoJSON, boolean isSameWithInsured);

	/**
	 * 提交投保前获得权益索赔人信息接口
	 * */
	public String getLegalrightclaimInfoBeforePolicy(String processInstanceId, String inscomcode);

	/**
	 * 提交投保前修改权益索赔人信息接口
	 * */
	public String editLegalrightclaimInfoBeforePolicy(String processInstanceId, String inscomcode, String operator, String legalrightclaimInfoJSON, boolean isSameWithInsured);

	/**
	 * 提交投保前获得所有投保信息接口
	 * */
	public String getCarTaskInfoBeforePolicy(String processInstanceId, String inscomcode);
	
	/**
	 * 提交投保推送工作流接口
	 * */
	public String policySubmit(String processInstanceId, String inscomcode, String agentnum, double totalproductamount, double totalpaymentamount);
	
	/**
	 * 查询保单列表信息接口
	 * */
	public String getPolicyitemList(String agentnum, String carlicenseno, String insuredname, String queryinfo, Integer limit, Long offset, Integer idcardtype, String idcardno, String code);
	
	/**
	 * 查询保单列表信息接口最新
	 * @param agentnum 代理人工号
	 * @param queryinfo 模糊查询信息
	 * @param querytype 查询类型
	 * @param pageSize 每页条数
	 * @param currentPage 当前页码
	 * @return
	 */
	public String getPolicyitemList02(String agentnum, String queryinfo, String querytype, Integer pageSize, Integer currentPage);
	public String getPolicyitemList02ForMinizzb(String agentnum, String queryinfo, String querytype, Integer pageSize, Integer currentPage);

	/**
	 * 查询保单详细信息接口
	 * */
	public String getPolicyitemDetailInfo(String policyitemid, String prvid);
	
	/**
	 * 查询保单详细信息接口最新
	 * @param policyno 保单号
	 * @param queryflag 查询标记位（cm、cf）
	 * @return json数据
	 */
	public String getPolicyitemDetailInfo02(String policyno, String queryflag, String policytype);

	/**
	 * 提交投保前获得车主信息接口
	 * */
	public String getCarOwnerInfoBeforePolicy(String processInstanceId, String inscomcode);

	/**
	 * 提交投保前修改车主信息接口
	 * */
	public String editCarOwnerInfoBeforePolicy(String processInstanceId, String inscomcode, String operator,
			String carOwnerInfoJSON, boolean isSameWithInsured);

	/**
	 * 541 【1029】CM，提供接口，能够判断订单是否为“已失效订单”，并按不同情况进行不同的删除操作
	 * @param jsonObject
	 * @return
     */
	String deleteOrder(String jsonObject);
}
