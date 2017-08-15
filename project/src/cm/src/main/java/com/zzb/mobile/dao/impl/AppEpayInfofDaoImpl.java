package com.zzb.mobile.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.mobile.dao.AppEpayInfoDao;
import com.zzb.mobile.dao.AppPaymentyzfDao;
import com.zzb.mobile.entity.AppEpayInfo;
import com.zzb.mobile.entity.AppPaymentyzf;
import com.zzb.model.AppPaymentyzfRModel;

@Repository
public class AppEpayInfofDaoImpl extends BaseDaoImpl<AppEpayInfo> implements AppEpayInfoDao {


	@Override
	public void insert(AppEpayInfo epay) {
		
		this.sqlSessionTemplate.insert(this.getSqlName("insert"),epay);
	}

	@Override
	public List<AppEpayInfo> querySignInfos(String jobNum) {
		
		return this.sqlSessionTemplate.selectList(this.getSqlName("querySignInfos"),jobNum);
	}

	
	public List<Map<String, Object>> getProvince() {
		
		return this.sqlSessionTemplate.selectList(this.getSqlName("getProvince"));
	}

	
	public List<Map<String, Object>> getCity(String provinceID) {
		
		return this.sqlSessionTemplate.selectList(this.getSqlName("getCity"),provinceID);
	}



}
