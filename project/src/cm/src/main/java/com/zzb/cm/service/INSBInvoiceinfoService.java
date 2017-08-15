package com.zzb.cm.service;

import com.cninsure.core.dao.BaseService;
import com.zzb.cm.entity.INSBInvoiceinfo;

public interface INSBInvoiceinfoService extends BaseService<INSBInvoiceinfo> {
	/**
	 * 
	 * 查询发票信息
	 * @param taskid 主任务id
	 * @param inscomcode 供应商编码
	 * @return
	 */
	public INSBInvoiceinfo selectByTaskidAndComcode(String taskid, String inscomcode);
}