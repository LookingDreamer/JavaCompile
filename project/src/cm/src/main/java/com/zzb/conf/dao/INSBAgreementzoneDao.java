package com.zzb.conf.dao;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseDao;
import com.zzb.conf.entity.INSBAgreementzone;

public interface INSBAgreementzoneDao extends BaseDao<INSBAgreementzone> {

	List<Map<String, Object>> getProvinces(String agreementid);

	List<Map<String, Object>> getCityByProvince(Map<String, String> map);

	void deleteGuanlian(String agreementid);

	List<String> getCityByAgreementId(String agreementid);

}