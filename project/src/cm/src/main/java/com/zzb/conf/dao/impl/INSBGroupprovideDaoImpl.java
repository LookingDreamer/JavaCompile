package com.zzb.conf.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.conf.dao.INSBGroupprovideDao;
import com.zzb.conf.entity.INSBGroupprovide;
import com.zzb.conf.entity.INSBProvider;

@Repository
public class INSBGroupprovideDaoImpl extends BaseDaoImpl<INSBGroupprovide> implements
		INSBGroupprovideDao {

	@Override
	public List<INSBGroupprovide> selectListByGroupId(String groupId) {
		return this.sqlSessionTemplate.selectList("com.zzb.conf.entity.INSBGroupprovide_selectByGroupId", groupId);
	}

	@Override
	public int deleteByGroupId(String groupId) {
		return this.sqlSessionTemplate.delete("com.zzb.conf.entity.INSBGroupprovide_deleteByGroupId", groupId);
	}

	@Override
	public int deleteByProviderId(String provideId) {
		return this.sqlSessionTemplate.delete("com.zzb.conf.entity.INSBGroupprovide_deleteByProviderId", provideId);
	}

	@Override
	public List<INSBProvider> selectprovidePageByParam(
			Map<String, Object> map) {
		return this.sqlSessionTemplate.selectList("com.zzb.conf.entity.INSBGroupprovide_selectprovidebygroupid", map);
	}

	@Override
	public List<String> selectGroupIdByProvideid(String providerId) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectGroupIdByProvideid"), providerId);
	}

	/**
	 * 查询没有供应商的业管群组
	 *
	 * @return
	 */
	@Override
	public List<String> selectNoProvideGroupId() {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectNoProvideGroupId"));
	}

}