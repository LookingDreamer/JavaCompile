package com.zzb.conf.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.conf.dao.INSBGroupprivilegDao;
import com.zzb.conf.entity.INSBGroupprivileg;

@Repository
public class INSBGroupprivilegDaoImpl extends BaseDaoImpl<INSBGroupprivileg> implements
		INSBGroupprivilegDao {

	@Override
	public List<INSBGroupprivileg> selectListByGruopId(String groupId) {
		return this.sqlSessionTemplate.selectList("com.zzb.conf.entity.INSBGroupprivileg_selectByGroupId", groupId);
	}

	@Override
	public int deleteByGroupId(String groupId) {
		return this.sqlSessionTemplate.delete("com.zzb.conf.entity.INSBGroupprivileg_deleteByGroupId", groupId);
	}

	@Override
	public int deleteByPrivilegeCodel(String privilegeCode) {
		return this.sqlSessionTemplate.delete("com.zzb.conf.entity.INSBGroupprivileg_deleteByPrivilegeCode", privilegeCode);
	}

}