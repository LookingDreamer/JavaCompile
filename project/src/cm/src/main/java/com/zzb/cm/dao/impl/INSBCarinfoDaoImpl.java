package com.zzb.cm.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cninsure.core.exception.DaoException;
import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.cm.dao.INSBCarinfoDao;
import com.zzb.cm.entity.INSBCarinfo;

@Repository
public class INSBCarinfoDaoImpl extends BaseDaoImpl<INSBCarinfo> implements
		INSBCarinfoDao {
	public List<INSBCarinfo> selectCarinfoList(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("select"),map);

	}

	@Override
	public INSBCarinfo selectCarinfoByTaskId(String taskid) {
		//return this.sqlSessionTemplate.selectOne(this.getSqlName("selectByTaskId"), taskid);
		INSBCarinfo carinfo = new INSBCarinfo();
		carinfo.setTaskid(taskid);
		return this.selectOne(carinfo);
	}

	@Override
	public List<Map<Object, Object>> getCarTaskListByMapOld(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("getCarTaskListByMapOld"),map);
	}

	@Override
	public void updateByCarInfoId(Map<String, Object> map) {
		this.sqlSessionTemplate.update(this.getSqlName("updateByCarInfoId"),map);
		
	}
	
	@Override
	public List<Map<Object, Object>> getCarInfoByPerson(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("getCarInfoByPerson"),map);
	}
	
	/**
	 * 通过map参数条件查询车险任务信息最终版
	 */
	@Override
	public List<Map<String, Object>> getCarTaskListByMap(Map<String, Object> map)throws DaoException {
		return this.sqlSessionTemplate.selectList(this.getSqlName("getCarTaskListByMap"),map);
	}

	/**
	 * 通过map参数条件查询车险任务信息总条数最终版
	 */
	@Override
	public long getCarTaskCountByMap(Map<String, Object> map) throws DaoException{
		return this.sqlSessionTemplate.selectOne(this.getSqlName("getCarTaskCountByMap"),map);
	}

	@Override
	public List<INSBCarinfo> getCarinfos(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("getCarinfosByMap"),map);
	}

	@Override
	public long selectPagingCount(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("getCarinfosByMapCount"), map);
	}

	@Override
	public List<INSBCarinfo> queryBytaskid(String taskid) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("queryBytaskid"), taskid);
	}

	@Override
	public List<INSBCarinfo> getByCreattime(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("getByCreattime"), map);
	}
	
	/**
	 *  查询已完成的车险任务   杨威   
	 * @param map
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getCompletedCarTaskListByMap(Map<String, Object> map)throws DaoException {
		return this.sqlSessionTemplate.selectList(this.getSqlName("getCompletedCarTaskListByMap"), map);
	}

	@Override
	public Long getCompletedCarTaskListCountByMap(Map<String, Object> map)throws DaoException {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("getCompletedCarTaskListCountByMap"), map);
	}

	public boolean updateInsureconfigsameaslastyear(String taskid, String value) {
		Map<String, String> params = new HashMap<>(2);
		params.put("taskid", taskid);
		params.put("sameaslastyear", value);
		int result = this.sqlSessionTemplate.update(this.getSqlName("updateInsureconfigsameaslastyear"), params);
		return result>0;
	}

	/**
	 * 获取操作状态、分配状态
	 *
	 * @param map
	 * @return
	 */
	@Override
	public List<Map<String, String>> getState(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("getState"), map);
	}

	/**
	 * 优化查询车险任务
	 */
	@Override
	public List<Map<String, Object>> getSingleCarTaskListByMap(Map<String, Object> map)throws DaoException {
		return this.sqlSessionTemplate.selectList(this.getSqlName("getSingleCarTaskListByMap"),map);
	}
	
	/**
	 * 优化查询车险任务信息总条数
	 */
	@Override
	public long getSingleCarTaskCountByMap(Map<String, Object> map)throws DaoException {

		return this.sqlSessionTemplate.selectOne(this.getSqlName("getSingleCarTaskCountByMap"),map);
	}
	
	/**
	 * 优化查询已完成的车险任务
	 */
	@Override
	public List<Map<String, Object>> getSingleCompletedCarTaskListByMap(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("getSingleCompletedCarTaskListByMap"), map);
	}
	
	/**
	 * 优化查询已完成的车险任务信息条数
	 */
	@Override
	public Long getSingleCompletedCarTaskListCountByMap(Map<String, Object> map) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("getSingleCompletedCarTaskListCountByMap"), map);
	}
}