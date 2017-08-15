package com.zzb.conf.dao.impl;

import java.util.List;
import java.util.Map;

import com.cninsure.core.exception.DaoException;
import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.cninsure.core.utils.UUIDUtils;
import com.zzb.conf.dao.INSBBusinessmanagegroupDao;
import com.zzb.conf.entity.INSBBusinessmanagegroup;

@Repository
public class INSBBusinessmanagegroupDaoImpl extends BaseDaoImpl<INSBBusinessmanagegroup> implements
		INSBBusinessmanagegroupDao {

	@Override
	public List<INSBBusinessmanagegroup> selectListByPage(Map<String,Object> map)throws DaoException {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectByPage"), map);
	}

	@Override
	public int selectCountByParam(Map<String, Object> map)throws DaoException {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectCountByParam"),map);
	}

	@Override
	public String insertReturnId(INSBBusinessmanagegroup model) {
		String id = UUIDUtils.random();
		model.setId(id);
		this.sqlSessionTemplate.insert(this.getSqlName("insertReturnId"),model);
		return id;
	}

	@Override
	public List<INSBBusinessmanagegroup> selectListByMap(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("select"), map);
	}

	@Override
	public int updateGroupCount(INSBBusinessmanagegroup model) {
		return this.sqlSessionTemplate.update(this.getSqlName("updateGroupCountByGroupId"), model);
	}

	@Override
	public String selectByGroupCode(String code) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectBycode"), code);
	}

	@Override
	public List<INSBBusinessmanagegroup> selectByGroupName(String groupname) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectByGroupName"), groupname);
	}

	@Override
	public List<INSBBusinessmanagegroup> selectByTasksetid(String tasksetid) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectByTasksetid"), tasksetid);
	}

	@Override
	public List<String> selectIdsByIdsAndTaskType4Task(Map<String, Object> param) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectIdsByIdsAndTaskType4Task"), param);
	}
	@Override
	public List<String> selectIdsByIdsAndTaskType4CertifiTask(Map<String, String> param) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectIdsByIdsAndTaskType4CertifiTask"), param);
	}
	@Override
	public List<Map<String, Object>> selectWorkloadByUsers4Task(List<String> users) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectWorkloadByUsers4Task"), users);
	}

}