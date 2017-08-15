package com.zzb.conf.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cninsure.core.exception.DaoException;
import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.conf.dao.INSBGroupmembersDao;
import com.zzb.conf.entity.INSBGroupmembers;

@Repository
public class INSBGroupmembersDaoImpl extends BaseDaoImpl<INSBGroupmembers>
		implements INSBGroupmembersDao {

	@Override
	public List<INSBGroupmembers> selectByGroupId(String groupid) throws DaoException{
		return this.sqlSessionTemplate.selectList(
				this.getSqlName("selectByGroupId"), groupid);
	}

	@Override
	public List<INSBGroupmembers> selectPageByParam(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectList(
				this.getSqlName("selectPageByParam"), map);
	}

	@Override
	public long selectPageCountByParam(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectOne(
				this.getSqlName("selectPageCountByParam"), map);
	}

	@Override
	public void deleteByUserId(String userid,String groupid) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userid", userid);
		map.put("groupid", groupid);
		this.sqlSessionTemplate.delete(this.getSqlName("deleteByUserId"),map);
	}

	@Override
	public Map<String, String> selectPrivilegeByGroupIdAndUserId(
			Map<String, String> map) {
		return this.sqlSessionTemplate.selectOne(
				this.getSqlName("selectPrivilegeByGroupIdAndUserId"), map);
	}

	@Override
	public int updataGroupPrivilegeById(Map<String, Object> map) {
		return this.sqlSessionTemplate.update(
				this.getSqlName("updateUserGroupPrivilegeById"), map);
	}

	@Override
	public List<String> selectUserCodeByGroupId(String groupid) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectUserCodeByGroupId"), groupid);
	}

	@Override
	public List<String> selectGroupIdsByUserCodes4Task(List<String> usercodes) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectGroupIdsByUserCodes4Task"), usercodes);
	}
	
	@Override
	public List<String> selectGroupIdsByUserCodes4Login(List<String> usercodes) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectGroupIdsByUserCodes4Login"), usercodes);
	}
	@Override
	public List<String> selectGroupIdsByUserId4UserDelete(List<String> usercodes) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectGroupIdsByUserId4UserDelete"), usercodes);
	}

	@Override
	public List<String> selecUserCodeByGroupIds4Task(Map<String,Object> map) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selecUserCodeByGroupIds4Task"), map);
	}

	/**
	 * 车险任务管理页面初始化分配业管列表使用liuchao
	 * @param params
	 */
	@Override
	public List<Map<String,String>> getUserInfoListByGroup(Map<String, Object> params) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("getUserInfoListByGroup"), params);
	}

	@Override
	public int updateMembersUserCode(Map<String,String> param) {
		return this.sqlSessionTemplate.update(this.getSqlName("updateMembersUserCode"), param);
	}

	@Override
	public int deleteByGroupId(String groupid) {
		return this.sqlSessionTemplate.delete(this.getSqlName("deleteByGroupId"), groupid);
	}

	@Override
	public long queryPrivileges(Map<String, String> param) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("queryPrivileges"), param);
	}

	@Override
	public List<Map<String,String>> selectGroupIdinfosByUserCodes4Tasklist(String usercode) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectGroupIdinfosByUserCodes4Tasklist"), usercode);
	}

}