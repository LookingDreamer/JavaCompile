package com.zzb.app.service;

import java.util.Map;

import com.cninsure.core.exception.ServiceException;
import com.zzb.cm.Interface.entity.car.model.ConfigInfo;
import com.zzb.cm.entity.INSBQuoteinfo;
import com.zzb.cm.entity.INSBQuotetotalinfo;

public interface AppQuotationService {
	/**
	 * 获得报价信息
	 * 
	 * @param taskId
	 * @return
	 * @throws ServiceException
	 */
	public String getQuotationInfo(String subtaskId, String taskId,
			String inscomcode) throws ServiceException;

	/**
	 * 获得报价前规则校验信息
	 * 
	 * @param taskId
	 * @return
	 * @throws ServiceException
	 */
	public String getQuotationValidatedInfo(String subtaskId, String taskId,
			String inscomcode) throws ServiceException;

	/**
	 * 获得报价信息
	 * 
	 * @param taskId
	 * @return
	 * @throws ServiceException
	 */
	public String getQuotationInfoForFlow(String subtaskId, String taskId,
			String inscomcode) throws ServiceException;
	
	/**
	 * 获取 年款过滤车型参数 信息
	 * @param subtaskId  流程 ID
	 * @param taskId     主流程 ID
	 * @param inscomcode 供应商ID
	 * @param taxPrice	  新车购置价
	 * @param price		  不含税价
	 * @param analogyTaxPrice 含税类比价
	 * @param analogyPrice 不含税类比价
	 * @param regDate	初等日期  
	 * @param seats	   	 座位数
	 * @return
	 * @throws ServiceException
	 */
	public String getPriceInterval(String subtaskId, String taskId,
			String inscomcode, Double taxPrice, Double price,
			Double analogyTaxPrice, Double analogyPrice, String regDate,
			int seats) throws ServiceException;
	
	/**
	 * 获取 年款过滤车型参数 信息
	 * @param subtaskId  流程 ID
	 * @param taskId     主流程 ID
	 * @param inscomcode 供应商ID
	 * @param taxPrice	  新车购置价
	 * @param price		  不含税价
	 * @param analogyTaxPrice 含税类比价
	 * @param analogyPrice 不含税类比价
	 * @param regDate	初等日期  
	 * @param seats	   	 座位数
	 * @param agreementid	协议id
	 * @return
	 * @throws ServiceException
	 */
	public String getPriceIntervalByGZquery(String subtaskId, String taskId,
			String inscomcode, Double taxPrice, Double price,
			Double analogyTaxPrice, Double analogyPrice, String regDate,
			int seats,String agreementid) throws ServiceException;


	/**
	 * 调规则
	 * @param subTaskId
	 * @param taskId
	 * @param inscomcode
	 * @param methodName
	 * @return
	 * @throws Exception
	 */
	public String getQuotationValidOrQuotationInfo(String subTaskId,
												   String taskId, String inscomcode, String methodName) throws Exception;
	/**
	 * 组装给规则的参数
	 * @param paramMap return
	 * @param taskId
	 * @param inscomcode
	 * @param quotetotalinfo
	 * @param quoteinfo
	 * @return
	 */
	public Map<String, Object> packetParamdata(Map<String, Object> paramMap, 
												String taskId, String inscomcode, INSBQuotetotalinfo quotetotalinfo, INSBQuoteinfo quoteinfo);

	/**
	 * 从规则获取折扣系数并保存
	 * @param subTaskId
	 * @param taskId
	 * @param inscomcode
	 */
	public void updatePolicyDiscount(String subTaskId, String taskId, String inscomcode);

	public String getThreadLocalVal();
	public void setThreadLocalVal(String val);
	public void removeThreadLocalVal();

	/**
	 * 获取指定机构、指定投保公司、指定能力的配置参数
	 * @param inscomcode 指定投保公司
	 * @param deptid 指定机构
	 * @param processType 指定能力
	 * @return
	 */
	public ConfigInfo getConfigInfo(String inscomcode, String deptid, String processType);
}
