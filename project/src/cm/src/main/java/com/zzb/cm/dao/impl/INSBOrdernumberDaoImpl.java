package com.zzb.cm.dao.impl;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.cm.dao.INSBOrdernumberDao;
import com.zzb.cm.entity.INSBOrdernumber;

@Repository
public class INSBOrdernumberDaoImpl extends BaseDaoImpl<INSBOrdernumber> implements
		INSBOrdernumberDao {

	/**
	 * 通过日期字段查询此日期下的订单号生成记录
	 * @param date
	 * @return
	 */
	@Override
	public INSBOrdernumber selectByDate(String date) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectByDate"), date);
	}

}