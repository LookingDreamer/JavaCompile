package com.zzb.conf.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.conf.dao.INSBPermissionDao;
import com.zzb.conf.entity.INSBPermission;

@Repository
public class INSBPermissionDaoImpl extends BaseDaoImpl<INSBPermission> implements
		INSBPermissionDao {


	@Override
	public List<Map<String, Object>> selectListByPage4add(Map<String,Object> map) {
		return this.sqlSessionTemplate.selectList("com.zzb.conf.entity.INSBPermission_selectPage4add",map);
	}

	@Override
	public List<Map<String, Object>> selectListByPage4allotupdate(Map<String,Object> map) {
		return this.sqlSessionTemplate.selectList("com.zzb.conf.entity.INSBPermission_selectPage4allotupdate",map);
	}

	/**
	 * 查询权限包 对应的权限
	 *
	 * @param map
	 * @return
	 */
	@Override
	public List<Map<String, Object>> selectSetPermission(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectList("com.zzb.conf.entity.INSBPermission_selectSetPermission",map);
	}

	@Override
	public List<Map<String, Object>> selectListByPage4update(Map<String,Object> map) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectPage4update"),map);
	}

	@Override
	public long selectCountByIstry(Map<String,Object> map) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectCountByIstry"), map);
	}

	@Override
	public List<INSBPermission> selectPermissionByIstry() {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectpermissionByIstry"));
	}

	@Override
	public List<INSBPermission> selectAll() {
		// TODO Auto-generated method stub
		return this.sqlSessionTemplate.selectList(getSqlName("select"));
	}

}