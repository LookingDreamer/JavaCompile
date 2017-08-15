package com.zzb.cm.dao;

import java.util.Map;

import com.cninsure.core.dao.BaseDao;
import com.zzb.cm.entity.INSBInvoiceinfo;

public interface INSBInvoiceinfoDao extends BaseDao<INSBInvoiceinfo> {
	/**
	 * 获取发票信息
	 * 
	 * @param paramMap
	 * @return
	 */
	public INSBInvoiceinfo selectByTaskidAndComcode(Map<String, String> paramMap);

	/**
	 * 更新发票信息
	 * @param queryinsbInvoiceinfo
	 */
	public void updateByTaskid(INSBInvoiceinfo queryinsbInvoiceinfo);
}