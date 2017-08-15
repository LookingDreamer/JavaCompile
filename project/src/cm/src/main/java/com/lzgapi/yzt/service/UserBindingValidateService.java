package com.lzgapi.yzt.service;

import com.cninsure.core.exception.ServiceException;
import com.lzgapi.yzt.bean.CanBindUserValidateBean;
import com.lzgapi.yzt.bean.UptoManagerBean;
import com.lzgapi.yzt.bean.UserBindingValidateBean;

public interface UserBindingValidateService {

	/**
	 * 用户绑定验证
	 * 
	 * @param model
	 * @return
	 * @throws ServiceException
	 */
	public UserBindingValidateBean getBindingUserList(String model)
			throws ServiceException;

	/**
	 * 可绑用户验证
	 * 
	 * @param model
	 * @return
	 * @throws ServiceException
	 */
	public CanBindUserValidateBean getUserValidateResult(String model)
			throws ServiceException;
	
	/**
	 * 掌柜升级
	 * @param model
	 * @return
	 * @throws ServiceException
	 */
	public UptoManagerBean uptoManager(String model)
			throws ServiceException;
}
