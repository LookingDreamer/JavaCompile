package com.zzb.conf.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.cninsure.core.utils.UUIDUtils;
import com.zzb.conf.dao.INSBPermissionallotDao;
import com.zzb.conf.entity.INSBPermissionallot;

@Repository
public class INSBPermissionallotDaoImpl extends
		BaseDaoImpl<INSBPermissionallot> implements INSBPermissionallotDao {

	@Override
	public List<INSBPermissionallot> selectListBySetId(String setId) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectBySetId"), setId);
	}

	@Override
	public List<Map<String, String>> selectPermissionBySetId(String setId) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectPermissionBySetId"), setId);
	}

	@Override
	public String insertDataReturnId(INSBPermissionallot allot) {
		String id = "";
		allot.setId(UUIDUtils.random());
		try {
			this.sqlSessionTemplate.insert(this.getSqlName("insert"), allot);
			id=allot.getId();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return id;
	}
	@Override
	public void deleteBySetId(String setId) {
		this.sqlSessionTemplate.delete(this.getSqlName("deleteBySetId"), setId);
	}

	@Override
	public List<String> selectBySetId(String setid) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectPermissionIdBySetId"), setid);
	}
}