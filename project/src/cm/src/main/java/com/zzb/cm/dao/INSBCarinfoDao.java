package com.zzb.cm.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseDao;
import com.zzb.cm.entity.INSBCarinfo;

public interface INSBCarinfoDao extends BaseDao<INSBCarinfo> {
	public List<INSBCarinfo> selectCarinfoList(Map<String, Object> map);
	public INSBCarinfo selectCarinfoByTaskId(String taskid);
	
	/**
	 * 通过map参数条件查询车险任务信息
	 * @param map
	 * @return
	 */
	public List<Map<Object, Object>> getCarTaskListByMapOld(Map<String, Object> map);
	/**
	 * 保存车辆信息
	 * @param Map
	 */
	public void updateByCarInfoId(Map<String, Object> map);

	/**
	 * 根据代理人和客户身份证获取客户车辆信息
	 * @param map
	 * @return
	 */
	public List<Map<Object, Object>> getCarInfoByPerson(Map<String, Object> map);

		
	
	/**
	 * 通过map参数条件查询车险任务信息最终版
	 */
	public List<Map<String, Object>> getCarTaskListByMap(Map<String, Object> map);
	
	/**
	 * 通过map参数条件查询车险任务信息总条数最终版
	 */
	public long getCarTaskCountByMap(Map<String, Object> map);
	
	/**
	 * 优化查询车险任务
	 */
	public List<Map<String, Object>> getSingleCarTaskListByMap(Map<String, Object> map);
	
	/**
	 * 优化查询车险任务信息总条数
	 */
	public long getSingleCarTaskCountByMap(Map<String, Object> map);
	
	/**
	 * 分页
	 */
	public List<INSBCarinfo> getCarinfos(Map<String, Object> map);
	/**
	 * 通过map参数条件查询车辆信息的数量
	 * @param map
	 * @return
	 */
	public long selectPagingCount(Map<String, Object> map);
	
	/**
	 * 根据任务号找到车辆使用性质为特种车的数据
	 * @param taskid
	 * @return
	 */
	public List<INSBCarinfo> queryBytaskid(String taskid);
//	/**
//	 * 分页显示查询
//	 * @param map
//	 * @return
//	 */
//	public List<Map<Object, Object>> selectCarinfoListPaging(
//			Map<String, Object> map);

	/**
	 * 根据时间查询车辆信息
	 */
	public List<INSBCarinfo> getByCreattime(Map<String, Object> map);
	
	/**
	 * 查询已完成的车险任务   杨威   
	 */
	public List<Map<String, Object>> getCompletedCarTaskListByMap(Map<String, Object> map);
	
	/**
	 * 查询已完成的车险任务   杨威   
	 */
	public Long getCompletedCarTaskListCountByMap(Map<String, Object> map);
	
	/**
	 * 优化查询已完成的车险任务
	 */
	public List<Map<String, Object>> getSingleCompletedCarTaskListByMap(Map<String, Object> map);
	
	/**
	 * 优化查询已完成的车险任务信息条数
	 */
	public Long getSingleCompletedCarTaskListCountByMap(Map<String, Object> map);

	public boolean updateInsureconfigsameaslastyear(String taskid, String value);

	/**
	 * 获取操作状态、分配状态
	 * @param map
	 * @return
     */
	public List<Map<String, String>> getState(Map<String, Object> map);
}