package com.lzgapi.yzt.service;

import com.cninsure.core.exception.ServiceException;
import com.lzgapi.yzt.bean.RegisterOrBecomeAgentBean;
import com.lzgapi.yzt.model.RegisterOrBecomeAgentModel;

public interface RegisterOrBecomeAgentService {
	/**
	 * 掌柜注册开通接口
	 * 
	 * @param model
	 * @return
	 * @throws ServiceException
	 */
	public RegisterOrBecomeAgentBean getAgentInfo(String model)
			throws ServiceException;
}
