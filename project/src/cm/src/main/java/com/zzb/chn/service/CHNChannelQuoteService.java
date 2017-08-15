package com.zzb.chn.service;

import java.util.Date;

import com.cninsure.core.dao.BaseService;
import com.zzb.chn.bean.QuoteBean;
import com.zzb.conf.entity.INSBWorkflowmain;

public interface CHNChannelQuoteService extends BaseService<INSBWorkflowmain> {
	/**
	 * 提交报价  
	 */ 
	public QuoteBean submitQuote(QuoteBean quoteBean) throws Exception;
	/**
	 * 提交核保
	 */
	public QuoteBean submitInsure(QuoteBean quoteBean) throws Exception;
	/**
	 * 修改报价/核保
	 */
	public QuoteBean updateTask(QuoteBean quoteBean) throws Exception;
	
	/**
	 * 创建报价 
	 */
	public QuoteBean chnCreateTaskA(QuoteBean quoteBean) throws Exception;

	/**
	 * 创建报价标准接口
	 */
	public QuoteBean createTaskB(QuoteBean quoteBean) throws Exception;
	
	/**
	 * 判断渠道有没有调用接口的权限
	 */
	boolean hasInterfacePower(String id, String city, String channelinnercode);

	boolean isWorkTime(String taskId, String prvId);

	Date getStartDate(String taskId, String prvId);
	public QuoteBean pay(QuoteBean quoteBean) throws Exception;
	public String getWorkVaildTime(String taskId, String prvId, Date date, boolean isQuote);
	
	//单供应商修改保险配置重新提交报价
	public QuoteBean submitSingleQuote(QuoteBean quoteBeanIn) throws Exception;
}
 