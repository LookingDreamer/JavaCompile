package com.zzb.conf.dao.impl;
 
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cninsure.core.utils.StringUtil;
import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.cninsure.core.utils.UUIDUtils;
import com.zzb.conf.dao.INSBPermissionsetDao;
import com.zzb.conf.entity.INSBPermissionset;

@Repository
public class INSBPermissionsetDaoImpl extends BaseDaoImpl<INSBPermissionset> implements
		INSBPermissionsetDao {

	@Override
	public List<INSBPermissionset> selectListPage(Map<String,Object> map) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectPage"),map);
	}

	@Override
	public String insertReturnId(INSBPermissionset set) {
		set.setId(UUIDUtils.random());
		 this.sqlSessionTemplate.insert("com.zzb.conf.entity.INSBPermissionset_insertreturnId",set);
		 return set.getId();
	}

	@Override
	public long selectCountByParam(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectCountByParam"), map);
	}
	
	@Override
	public List<INSBPermissionset> selectByOnUseSet() {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectOnUseSet"));
	}

	@Override
	public List<String> selectByDeptId(String deptid, Integer istest) {
		Map<String,Object> param = new HashMap<String,Object>(); 
		param.put("deptid", deptid);
		param.put("istest", istest);
		param.put("status", 2);
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectByDeptidIstest"),param);
	}

	@Override
	public List<INSBPermissionset> selectByDeptAgentkindAndOnUseSet(String deptid,
			int agentkind) {
		Map<String,Object> param = new HashMap<String,Object>(); 
		param.put("deptid", deptid);
		param.put("agentkind", agentkind);
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectByDeptAgentkindAndOnUseSet"),param);
	}

	/**
	 * 根据代理人所属机构，筛选出启用的试用功能包
	 *
	 * @param deptid
	 * @return
	 */
	@Override
	public List<INSBPermissionset> selectTrialSet(String deptid) {
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("deptid", deptid);
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectTrialSet"),param);
	}

	/**
	 * 根据权限包id，筛选出关联的用户
	 *
	 * @param map@return
	 */
	@Override
	public List<Map<String, Object>> getUserByPage(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("getUserByPage"),map);
	}

	/**
	 * 根据权限包id，筛选出关联的用户数量
	 *
	 * @param map@return
	 */
	@Override
	public long getUserByPageCount(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("getUserByPageCount"), map);
	}

	@Override
	public List<String> selectDefaultPermissionallot(int istest) {
		Map<String,Object> param = new HashMap<String,Object>(); 
		param.put("istest", istest);
		param.put("status", 2);
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectDefaultPermissionallot"),param);
	}

	@Override
	public int selectCountBySetCode(Map<String, Object> map) {
		int count = this.sqlSessionTemplate.selectOne(this.getSqlName("selectCountBySetCode"), map);
		return count;
				
	}

	/**
	 * 查询部门下启用的权限包
	 *
	 * @param map
	 * @return
	 */
	@Override
	public List<INSBPermissionset> selectByDeptinnercode(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectByDeptinnercode"),map);
	}

	/**
	 * 根据代理人所属机构和用户类型，筛选出启用的权限包
	 * @param deptid
	 * @param agentkind
	 * @return
	 */
	@Override
	public List<INSBPermissionset> selectSetByKindAndDeptid(String deptid, String agentkind) {
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("deptid", deptid);
		if( agentkind != null ) {
			param.put("agentkind", agentkind);
		}
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectSetByKindAndDeptid"),param);
	}

	@Override
	public String queryBytrysetcode(String tryset) {
		String bag = tryset;
		if (StringUtil.isEmpty(bag)) {
			return null;
		}
		return this.sqlSessionTemplate.selectOne(this.getSqlName("queryBySetcode"),bag);
	}

	@Override
	public String queryByformalsetcode(String formalset) {
		String bag = formalset;
		if (StringUtil.isEmpty(bag)) {
			return null;
		}
		return this.sqlSessionTemplate.selectOne(this.getSqlName("queryBySetcode"),bag);
	}

	@Override
	public String queryBychannelsetcode(String channelset) {
		String bag = channelset;
		if (StringUtil.isEmpty(bag)) {
			return null;
		}
		return this.sqlSessionTemplate.selectOne(this.getSqlName("queryBySetcode"),bag);
	}

	/**
	 * 通过deptid和agentkind查询
	 */
	@Override
	public  List<INSBPermissionset> selectByDeptAndAgentkind(Map<String, Object> param) {
		return  this.sqlSessionTemplate.selectList(this.getSqlName("selectByDeptAndAgentkind"),param);
	}

}