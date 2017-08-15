package com.zzb.conf.dao.impl;

import java.util.*;

import com.cninsure.core.utils.BeanUtils;
import com.cninsure.core.utils.StringUtil;
import com.common.ModelUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.cninsure.core.dao.constants.SqlId;
import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.cninsure.core.exception.DaoException;
import com.cninsure.core.utils.LogUtil;
import com.zzb.app.model.ImageManagerModel;
import com.zzb.conf.dao.INSBPolicyitemDao;
import com.zzb.conf.entity.INSBPolicyitem;
import com.zzb.mobile.model.AppInsuredMycustomerModel;

@Repository
public class INSBPolicyitemDaoImpl extends BaseDaoImpl<INSBPolicyitem> implements INSBPolicyitemDao {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cninsure.dao.BaseDao#deleteById(java.io.Serializable)
	 */
	@Override
	public int deleteById(String id) {
		Assert.notNull(id);
		INSBPolicyitem entity=this.selectById(id);
		LogUtil.info("INSBPolicyitem taskid="+entity.getTaskid()+" inscomcode="+entity.getInscomcode()+" delete:"+entity);
		try {
			return sqlSessionTemplate.delete(getSqlName(SqlId.SQL_DELETE_BY_ID), id);
		} catch (Exception e) {
			throw new DaoException(String.format("根据ID删除对象出错！语句：%s", getSqlName(SqlId.SQL_DELETE_BY_ID)), e);
		}
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cninsure.dao.BaseDao#updateById(java.io.Serializable)
	 */
	@Override
	public int updateById(INSBPolicyitem entity) {
		Assert.notNull(entity);
		LogUtil.info("INSBPolicyitem taskid="+entity.getTaskid()+" inscomcode="+entity.getInscomcode()+" update:"+entity);
		try {
			return sqlSessionTemplate.update(getSqlName(SqlId.SQL_UPDATE_BY_ID), entity);
		} catch (Exception e) {
			throw new DaoException(String.format("根据ID更新对象出错！语句：%s", getSqlName(SqlId.SQL_UPDATE_BY_ID)), e);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cninsure.dao.BaseDao#insert(java.io.Serializable)
	 */
	@Override
	public void insert(INSBPolicyitem entity) {
		Assert.notNull(entity);
		LogUtil.info("INSBPolicyitem taskid="+entity.getTaskid()+" inscomcode="+entity.getInscomcode()+" insert:"+entity);
		try {
			if (StringUtils.isBlank(entity.getId()))
				entity.setId(generateId());
			sqlSessionTemplate.insert(getSqlName(SqlId.SQL_INSERT), entity);
		} catch (Exception e) {
			throw new DaoException(String.format("添加对象出错！语句：%s", getSqlName(SqlId.SQL_INSERT)), e);
		}
	}
	
	
	
	@Override
	public INSBPolicyitem selectPolicyitemByTaskId(String taskId) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectByTaskId"), taskId);
	} 

	@Override
	public List<INSBPolicyitem> selectPolicyitemList(Map<String, Object> map) {
		if (isEmpty(map)) {
			return Collections.emptyList();
		}
		return this.sqlSessionTemplate.selectList(this.getSqlName("select"), map);
	}

	@Override
	public ImageManagerModel queryModelList(String policyid) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("imagemanagermodel"), policyid);
	}

	@Override
	public List<Map<Object, Object>> queryImageList(String policyid) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("queryImageList"), policyid);
	}

	@Override
	public List<Map<String, Object>> getImageInfoList(String agentid) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("getImageInfoList"), agentid);
	}

	@Override
	public List<Map<String, Object>> queryPolicyInfobyPerson(Map<String, Object> Params) {

		return this.sqlSessionTemplate.selectList(this.getSqlName("queryPolicyInfobyPerson"), Params);
	}

	/**
	 * 查询保单列表信息接口使用
	 * */
	@Override
	public List<Map<String, Object>> getPolicyitemListByAgentnum(Map<String, Object> queryParams) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("getPolicyitemListByAgentnum"), queryParams);
	}

	@Override
	public long getPolicyitemListTotalsByAgentnum(Map<String, Object> queryParams) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("getPolicyitemListTotalsByAgentnum"), queryParams);
	}

	@Override
	public List<Map<String, Object>> getPolicyitems(Map<String, Object> queryParams) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("getPolicyitems"), queryParams);
	}

	@Override
	public List<Map<String, Object>> getPolicyitemsForMinizzb(Map<String, Object> queryParams) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("getPolicyitemsForMinizzb"), queryParams);
	}

	@Override
	public List<INSBPolicyitem> selectByEndDate(Map<String, Object> queryParams) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectByEndDate"), queryParams);
	}

	@Override
	public List<INSBPolicyitem> selectPolicyitemList(String taskid) {
		if (StringUtil.isEmpty(taskid)) {
			return Collections.emptyList();
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("taskid", taskid);
		return this.sqlSessionTemplate.selectList(this.getSqlName("select"), map);
	}

	/**
	 * 查询客户信息
	 */
	@Override
	public List<AppInsuredMycustomerModel> queryCustomersInfo(Map<String, Object> map) {

		return this.sqlSessionTemplate.selectList(this.getSqlName("queryCustomersInfo"), map);
	}

	@Override
	public List<INSBPolicyitem> queryPolicyByDate() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> selectAgentDeptIdByTaskId4Pool(String taskid) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectAgentDeptIdByTaskId4Pool"), taskid);
	}

	@Override
	public List<INSBPolicyitem> selectPolicyitemByInscomTask(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectPolicyitemByInscomTask"), map);
	}

	public List<Map> selectRemoveOverTimeData(Map<String, Object> map){
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectRemoveOverTimeData"), map);
	}

	@Override
	public List<INSBPolicyitem> getListByParam(Map<String, Object> param) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("getListByParam"), param);
	}

	@Override
	public List<INSBPolicyitem> getListByPolicyno(String policyno, String taskid) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("policyno", policyno);
		params.put("taskid", taskid);
		return this.sqlSessionTemplate.selectList(this.getSqlName("getListByPolicyno"), params);
	}

	@Override
	public List<INSBPolicyitem> getListByProposalformno(String proposalformno, String taskid) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("proposalformno", proposalformno);
		params.put("taskid", taskid);
		return this.sqlSessionTemplate.selectList(this.getSqlName("getListByProposalformno"), params);
	}

	@Override
	public List<Map<Object, Object>> policcyitembyagentid(Map<String, Object> param) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("policcyitembyagentid"), param);
	}

	@Override
	public Long policcyitembyagentidcount(Map<String, Object> param) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("policcyitembyagentidcount"), param);
	}

	/**
	 * 根据主流程id和保险公司编码查询保单记录liuchao
	 * 
	 * param mInstanceid
	 *       inscomcode
	 */
	@Override
	public List<INSBPolicyitem> getPolicyByMinstanceidAndComcode(Map<String, Object> params) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("getPolicyByMinstanceidAndComcode"), params);
	}

	@Override
	public List<INSBPolicyitem> getListByMap(Map<String, String> map) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("getPolicyByTaskidAndComcode"), map);
	}

	@Override
	public int deleteByObj(INSBPolicyitem insbPolicyitem) {
		return this.sqlSessionTemplate.delete(this.getSqlName("deleteByObj"), insbPolicyitem);
	}
	/**
	 * 查询保单列表信息接口使用
	 * */
	@Override
	public List<Map<String, Object>> getPolicyitemList(Map<String, Object> queryParams) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("getPolicyitemList"), queryParams);
	}
	/**
	 * 查询保单列表信息接口使用
	 * */
	@Override
	public List<Map<String, Object>> getPolicyitemListForMinizzb(Map<String, Object> queryParams) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("getPolicyitemListForMinizzb"), queryParams);
	}

	@Override
	public List<Map<Object, Object>> selectClosstaskPaging(
			Map<String, Object> queryParams) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectClosstaskPaging"), queryParams);
	}

	@Override
	public long selectClosstaskPagingCount(Map<String, Object> queryParams) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectClosstaskPagingCount"), queryParams);
	}

	@Override
	public List<INSBPolicyitem> selectLastest(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectLastest"), map);
	}
	
	@Override
	public List<Map<String, String>> selectAllPolicynoByTimePeriod(Map<String, String> queryParams) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("allPolicynoByTimePeriod"), queryParams);
	}
	
	/**
	 * 根据车主，车牌获取 已生效的保单（车童对接专用）
	 * 
	 * @param ownername
	 * @param carlicenseno
	 */
	public List<INSBPolicyitem> getPolicyByOwnernameAndCarlicenseno(String ownername,String carlicenseno){
		Map<String, String> params = new HashMap<String, String>();
		params.put("ownername", ownername);
		params.put("carlicenseno", carlicenseno);
		return this.sqlSessionTemplate.selectList(this.getSqlName("getPolicyByOwnernameAndCarlicenseno"), params); 
	}

	public List<INSBPolicyitem> selectList(INSBPolicyitem query) {
		Map<String, Object> params = BeanUtils.toMap(query);
		if (isEmpty(params)) return Collections.emptyList();
		return super.selectList(query);
	}

	public List<INSBPolicyitem> selectAll() {
		return Collections.emptyList();
	}

	public Map<String, INSBPolicyitem> selectMap(INSBPolicyitem query, String s) {
		Map<String, Object> params = BeanUtils.toMap(query);
		if (isEmpty(params)) return Collections.emptyMap();
		return super.selectMap(query, s);
	}

	public List<INSBPolicyitem> selectList(INSBPolicyitem query, Pageable pageable) {
		Map<String, Object> params = BeanUtils.toMap(query);
		if (isEmpty(params)) return Collections.emptyList();
		return super.selectList(query, pageable);
	}

	public Page<INSBPolicyitem> selectPageList(INSBPolicyitem query, Pageable pageable) {
		Map<String, Object> params = BeanUtils.toMap(query);
		if (isEmpty(params)) return null;
		return super.selectPageList(query, pageable);
	}

	public Map<String, INSBPolicyitem> selectMap(INSBPolicyitem query, String s, Pageable pageable) {
		Map<String, Object> params = BeanUtils.toMap(query);
		if (isEmpty(params)) return Collections.emptyMap();
		return super.selectMap(query, s, pageable);
	}

	public Long selectCount() {
		return Long.getLong("0");
	}

	public Long selectCount(INSBPolicyitem query) {
		Map<String, Object> params = BeanUtils.toMap(query);
		if (isEmpty(params)) return Long.getLong("0");
		return super.selectCount(query);
	}

	public List<INSBPolicyitem> queryList(String s, Object o) {
		if (StringUtil.isEmpty(s) || !s.toLowerCase().contains("where")) return Collections.emptyList();
		return super.queryList(s, o);
	}

	public boolean isEmpty(Map<String, Object> map) {
		if (map == null || map.isEmpty()) return true;

		boolean isnull = true;
		Iterator iterator = map.values().iterator();
		Object value = null;

		while (iterator.hasNext()) {
			value = iterator.next();
			if (value == null) continue;
			if (value instanceof String) {
				if (StringUtil.isNotEmpty(value.toString())) {
					isnull = false;
					break;
				}
			} else {
				isnull = false;
			}
		}

		if (isnull) {
			try {
				throw new Exception("参数为空");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return isnull;
	}
}