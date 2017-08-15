package com.zzb.conf.dao;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseDao;
import com.zzb.conf.entity.INSBBusinessmanagegroup;

public interface INSBBusinessmanagegroupDao extends BaseDao<INSBBusinessmanagegroup> {

	
	/**
	 * 通过map
	 * 
	 * @param map
	 * @return
	 */
	public List<INSBBusinessmanagegroup> selectListByMap(Map<String,Object> map);
	/**
	 * 分页查询
	 * 
	 * @return
	 */
	public List<INSBBusinessmanagegroup> selectListByPage(Map<String,Object> map);
	
	/**
	 * 按条件查出所有数据条数
	 * 
	 * @param map
	 * @return
	 */
	public int selectCountByParam(Map<String,Object> map);
	
	
	/**
	 * 新增数据返回id
	 * 
	 * @param model
	 * @return
	 */
	public String insertReturnId(INSBBusinessmanagegroup model);
	
	
	/**
	 * 更新群组成员人数
	 * 
	 * @param groupId
	 */
	public int updateGroupCount(INSBBusinessmanagegroup model);
	
	
	/**
	 * 通过code查询群组id
	 * 
	 * @param code
	 * @return
	 */
	public String selectByGroupCode(String code);
	
	
	/**
	 * 通过名称模糊查询
	 * @param groupname
	 * @return
	 */
	public List<INSBBusinessmanagegroup> selectByGroupName(String groupname);
	
	/**
	 * 通过任务组ID查询
	 * @param tasksetid
	 * @return
	 */
	public  List<INSBBusinessmanagegroup> selectByTasksetid(String tasksetid);
	
	/**
	 * 按照任务类型过滤群组
	 * 
	 * @param param
	 * @return
	 */
	public List<String> selectIdsByIdsAndTaskType4Task(Map<String,Object> param);
	/**
	 * 认证任务类型群组
	 * 
	 * @param param
	 * @return
	 */
	public List<String> selectIdsByIdsAndTaskType4CertifiTask(Map<String, String> param);
	/**
	 * 得到业管工作量阈值
	 * @param users
	 * @return
	 */
	public List<Map<String,Object>> selectWorkloadByUsers4Task(List<String> users);
	
}