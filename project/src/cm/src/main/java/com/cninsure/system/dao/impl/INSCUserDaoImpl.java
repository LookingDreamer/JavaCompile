package com.cninsure.system.dao.impl;


import java.util.List;
import java.util.Map;

import com.cninsure.core.exception.DaoException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.cninsure.core.utils.UUIDUtils;
import com.cninsure.system.dao.INSCUserDao;
import com.cninsure.system.entity.INSCUser;


@Repository
public class INSCUserDaoImpl extends BaseDaoImpl<INSCUser> implements INSCUserDao {

	@Override
	public INSCUser selectByUserCode(String usercode) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectByUserCode"),usercode);
	}

	@Override
	public List<Map<Object, Object>> selectUserLitByMap(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectUserListByMap"),map);

	}

	@Override
	public List<Map<Object, Object>> selectUserListPaging(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectUserListPaging"),map);
	}
	@Override
	public long selectPagingCount(Map<String, Object> data) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectUserCountPaging"), data);
	}

	
	
	@Override
	public int updatePWDById(INSCUser user) {
		return this.sqlSessionTemplate.update("com.cninsure.system.entity.INSCUser_updateUserPWDById", user);
	}

	@Override
	public int updateUserStatusById(INSCUser user) {
		return this.sqlSessionTemplate.update(this.getSqlName("updateUserStatusById"), user);
	}
	
	@Override
	public int updateUserStatus2OnById(INSCUser user) {
		return this.sqlSessionTemplate.update(this.getSqlName("updateUserStatus2OnById"), user);
	}

	@Override
	public INSCUser selectByUserId(String id) {
		return this.sqlSessionTemplate.selectOne("com.cninsure.system.entity.INSCUser_selectUserById",id);
	}

	@Override
	public List<INSCUser> selectByGroupId(Map<String,Object> map) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectByGroupId"),map);
	}

	@Override
	public long selectCountByParam(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectCountByGroupId"),map);
	}

	
	
	@Override
	public List<INSCUser> selectUsersByDeptId(Map<String,Object> params) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectPageByDeptId"), params);
	}
	@Override
	public Long selectUsersCountByDeptId(Map<String, Object> params) throws DaoException {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectPageCountByDeptId"), params);
	}
	
	
	@Override
	public long selectCountUsersByDeptId(Map<String, Object> params) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectCountPageByDeptId"), params);
	}

	

	@Override
	public int updateGroupIdById(INSCUser user) {
		return this.sqlSessionTemplate.update(this.getSqlName("updateUserGroupIdById"), user);
	}

	@Override
	public String insertReturnId(INSCUser user) {
		String id = UUIDUtils.random();
		user.setId(id);
		this.sqlSessionTemplate.insert(this.getSqlName("insert"), user);
		return id;
	}

	@Override
	public List<String> selectUserByGroupid(String groupid) {
		List<String> list = null;
		if(StringUtils.isNotBlank(groupid)){
			list =  this.sqlSessionTemplate.selectList(this.getSqlName("selectUserByGroupid"),groupid);
		}
		return list;
	}

	@Override
	public List<Map<String, Object>> selectUsersUseLike(Map<String,Object> map) throws DaoException {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectUsersUseLike"), map);
	}
	@Override
	public long selectCountUsersUseLike(Map<String,Object> map) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectCountUsersUseLike"), map);
	}

	@Override
	public String selectCodeById(String id) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectCodeById"), id);
	}

	@Override
	public List<INSCUser> selectRmainUsersByIds(List<String> ids) {
		return this.sqlSessionTemplate.selectList("com.cninsure.system.entity.INSCUser_selectUsersRemainByIds",ids);
	}

	@Override
	public List<INSCUser> selectRmainUsersByMap(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectUsersRemainByMap"),map);
	}

	@Override
	public long selectCountRmainUsersByMap(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectCountUsersRemainByMap"), map);
	}

	@Override
	public String selectIdByCode4Menu(String code) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectIdByCode4Menu"), code);
	}

	@Override
	public List<INSCUser> queryuserlist() {
		return this.sqlSessionTemplate.selectList(this.getSqlName("queryuserlist"));
	}

	@Override
	public int updateid(Map<String, String> map) {
		return this.sqlSessionTemplate.update(this.getSqlName("updateid"),map);
	}

	@Override
	public List<Map<Object, Object>> selectonlineListPaging(
			Map<String, Object> map) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectOnlineUserListPaging"),map);
	}

	@Override
	public long selectonlineuserPagingCount(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectOnlineUserCountPaging"), map);
	}
	
	@Override
	public List<String> selectGroupnameByUsercode(String usercode) {	
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectGroupidByUserid"),usercode);
	}

	@Override
	public long selectunfinishedtasknumCount(String usercode) {
		if("admin".equals(usercode)){//如果是admin用户直接返回未处理任务=0
			return 0l; 
		}else{
			return this.sqlSessionTemplate.selectOne(this.getSqlName("selectunfinishedtasknumCount"), usercode);
		}
	}

	@Override
	public long selectGroupnameCountByUsercode(String usercode) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectGroupidCountByUserid"), usercode);
	}

	@Override
	public List<INSCUser> selectByIds(List<String> ids) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectByIds"), ids);
	}
	

	


	

}
