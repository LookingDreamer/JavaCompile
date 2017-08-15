package com.cninsure.system.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.cninsure.system.dao.INSCFdcomDao;
import com.cninsure.system.entity.INSCFdcom;

@Repository
public class INSCFdcomDaoImpl extends BaseDaoImpl<INSCFdcom> implements
		INSCFdcomDao {

	@Override
	public List<INSCFdcom> selectOrganizationData(String maxSyncdateStr,String comcode) {
		Map<String, String> param = new HashMap<String, String>();
		param.put("comcode", comcode);
		param.put("syncdate", maxSyncdateStr);
		return sqlSessionTemplate.selectList(getSqlName("selectOrganizationData"),param);
	}

	@Override
	public List<Map<String, String>> selectOrgCode() {
		return sqlSessionTemplate.selectList(getSqlName("selectOrgCode"));
	}

	@Override
	public List<Map<String, Object>> selectProviderData(String maxSyncdateStr) {
		return sqlSessionTemplate.selectList(getSqlName("selectProviderData"),maxSyncdateStr);
	}
}