package com.zzb.cm.service;

import java.util.Date;

import com.cninsure.core.dao.BaseService;
import com.zzb.cm.entity.INSBOrdernumber;

public interface INSBOrdernumberService extends BaseService<INSBOrdernumber> {

	/**
	 * 得到订单生成号
	 */
	public String getGeneralOrdernumber(Date date);
}