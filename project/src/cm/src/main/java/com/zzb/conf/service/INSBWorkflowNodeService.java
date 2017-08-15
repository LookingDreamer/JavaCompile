package com.zzb.conf.service;

import java.util.Map;

import com.cninsure.core.exception.ServiceException;

/**
 * @author hlj
 * @version 1.0
 * @since 16:59 2015/9/16
 */
public interface INSBWorkflowNodeService {
	/**
	 * 获得流程人员信息
	 * 
	 * @param paramMap
	 * @return
	 * @throws ServiceException
	 */
	public String getPersonInfo(Map<String, Object> paramMap)
			throws ServiceException;

	/**
	 * 获得流程人员信息
	 * 
	 * @param param
	 * @return
	 */
	public String getPersonInfo(String param) throws ServiceException;
}
