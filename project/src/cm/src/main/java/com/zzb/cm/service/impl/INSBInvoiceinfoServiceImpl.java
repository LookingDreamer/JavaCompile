package com.zzb.cm.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.cm.dao.INSBInvoiceinfoDao;
import com.zzb.cm.entity.INSBInvoiceinfo;
import com.zzb.cm.service.INSBInvoiceinfoService;

@Service
@Transactional
public class INSBInvoiceinfoServiceImpl extends BaseServiceImpl<INSBInvoiceinfo> implements
		INSBInvoiceinfoService {
	@Resource
	private INSBInvoiceinfoDao insbInvoiceinfoDao;

	@Override
	protected BaseDao<INSBInvoiceinfo> getBaseDao() {
		return insbInvoiceinfoDao;
	}

	/**
	 * 
	 * 查询发票信息
	 * @param taskid 主任务id
	 * @param inscomcode 供应商编码
	 * @return
	 */
	@Override
	public INSBInvoiceinfo selectByTaskidAndComcode(String taskid, String inscomcode) {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("taskid", taskid);
		paramMap.put("inscomcode", inscomcode);
		return insbInvoiceinfoDao.selectByTaskidAndComcode(paramMap);
	}
}