package com.zzb.app.service;

import com.cninsure.core.exception.ServiceException;

public interface AppSupplementItemService {

	/**
	 * 获得供应商补充信息字段项
	 * 
	 * @param supplierId
	 * @param deptid
	 * @return
	 * @throws ServiceException
	 */
	public String getSupplierSupplementItem(String supplierId, String deptid)
			throws ServiceException;

}