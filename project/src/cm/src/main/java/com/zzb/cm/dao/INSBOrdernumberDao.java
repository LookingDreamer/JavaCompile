package com.zzb.cm.dao;

import com.cninsure.core.dao.BaseDao;
import com.zzb.cm.entity.INSBOrdernumber;

public interface INSBOrdernumberDao extends BaseDao<INSBOrdernumber> {

	/**
	 * 通过日期字段查询此日期下的订单号生成记录
	 * @param date
	 * @return
	 */
	public INSBOrdernumber selectByDate(String date);
}