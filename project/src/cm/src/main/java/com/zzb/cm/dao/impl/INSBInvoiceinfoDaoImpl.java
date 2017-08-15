package com.zzb.cm.dao.impl;

import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.cm.dao.INSBInvoiceinfoDao;
import com.zzb.cm.entity.INSBInvoiceinfo;

@Repository
public class INSBInvoiceinfoDaoImpl extends BaseDaoImpl<INSBInvoiceinfo> implements
		INSBInvoiceinfoDao {
	/**
	 * 
	 * 获取发票信息
	 * @param paramMap
	 * @return
	 */
	@Override
	public INSBInvoiceinfo selectByTaskidAndComcode(Map<String, String> paramMap) {		
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectByTaskidAndComcode"), paramMap);
	}
	/**
	 * 更新发票信息
	 */
	@Override
	public void updateByTaskid(INSBInvoiceinfo queryinsbInvoiceinfo) {
		this.sqlSessionTemplate.update(this.getSqlName("updateByTaskid"), queryinsbInvoiceinfo);
	}
}