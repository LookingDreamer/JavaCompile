package com.zzb.conf.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.cninsure.core.utils.UUIDUtils;
import com.zzb.conf.dao.INSBAutoconfigshowDao;
import com.zzb.conf.entity.INSBAutoconfig;
import com.zzb.conf.entity.INSBAutoconfigshow;

@Repository
public class INSBAutoconfigshowDaoImpl extends BaseDaoImpl<INSBAutoconfigshow> implements
		INSBAutoconfigshowDao {

	@Override
	public int deleteautoshow(Map<String, String> param) {
		return this.sqlSessionTemplate.delete(this.getSqlName("deleteautoshow"), param);
	}

	@Override
	public int deleterepetitionautoshow(Map<String, String> para) {
		return this.sqlSessionTemplate.delete(this.getSqlName("deleterepetitionautoshow"), para);
	}

	@Override
	public List<INSBAutoconfigshow> selectDataByDeptIds4New(Map<String, Object> param) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectDataByDeptIds4New"), param);
	}
	
	@Override
	public List<String> queryByProId(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("providerid"),map);
	}
	
	@Override
	public INSBAutoconfig getQuoteType(Map<String, String> para) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("getQuoteType"),para);
	}

	@Override
	public int deleteByAgreementId(String agreementid,String conftype) {
		Map<String, String> parm = new HashMap<String, String>();
		parm.put("agreementid", agreementid);
		parm.put("conftype", conftype);
		return this.sqlSessionTemplate.delete(this.getSqlName("deleteByAgreementId"),parm);
	}

	@Deprecated
	@Override
	public int deletebyelfid(String contentid, String quoteType) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("contentid", contentid);
		map.put("quotetype", quoteType);
		return this.sqlSessionTemplate.delete(this.getSqlName("deletebyelfid"),map);
	}

	@Override
	public String selectByAgreementId(Map<String, String> para) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectByAgreementId"), para);
	}

	@Override
	public INSBAutoconfig getContracthbType(Map<String, String> para) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("getContracthbType"),para);
	}

	@Override
	public List<Map<String, String>> selectComcodeBbyContenIdAndProviderId(
			Map<String, String> param) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectComcodeBbyContenIdAndProviderId"), param);
	}
	
	@Override
	public List<INSBAutoconfigshow> selectOneAbilityByDeptid(Map<String, String> param) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectOneAbilityByDeptid"), param);
	}

	@Override
	public String selectContendIdByParam(Map<String, String> param) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectContendIdByParam"), param);
	}

	@Override
	public int deleteautobyshowid(Map<String, String> param) {
		return this.sqlSessionTemplate.delete(this.getSqlName("deleteautobyshowid"), param);
	}

	@Override
	public void deleteautobyshowid(String id, String quotetype) {
		Map<String, String> para = new HashMap<String, String>();
		para.put("id", id);
		para.put("quotetype", quotetype);
		this.sqlSessionTemplate.delete(this.getSqlName("deleteautobyshowidonly"), para);
		
	}

	@Override
	public String insertReturnId(INSBAutoconfigshow autoconfig) {
		String id=UUIDUtils.random();
		autoconfig.setId(id);
		this.sqlSessionTemplate.insert("insert",autoconfig);
		return id;
	}

}