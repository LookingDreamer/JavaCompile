package com.zzb.mobile.dao;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseDao;
import com.zzb.mobile.entity.AppEpayInfo;
import com.zzb.mobile.entity.AppPaymentyzf;
import com.zzb.model.AppPaymentyzfRModel;


public interface AppEpayInfoDao extends BaseDao<AppEpayInfo> {
	/**
	 * 	添加
	 */
	public void insert(AppEpayInfo epay);

	/**
	 * 	查询签约信息
	 */
	public List<AppEpayInfo> querySignInfos(String jobNum);





}
