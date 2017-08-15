package com.zzb.cm.service;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseService;
import com.cninsure.system.entity.INSCUser;
import com.zzb.cm.controller.vo.CarinfoVO;
import com.zzb.cm.entity.INSBCarinfo;
import com.zzb.cm.entity.INSBCarmodelinfo;


public interface INSBCarinfoService extends BaseService<INSBCarinfo> {

	public String showMytaskInfoAll(Map<String, Object> map,int page);
	public String showMytaskInfo(Map<String, Object> map,int page);
	public String showMytaskInfo_ad(Map<String, Object> map,int page);
	
	/**
	 * 通过map参数查询车辆任务集合并返回对应JSON对象
	 * @param paramMap
	 * @return
	 */
	public String getJSONOfCarTaskListByMap(Map<String, Object> paramMap);
	/**
	 * 通过map参数查询车辆任务集合
	 * @param paramMap
	 * @return
	 */
	public List<Map<Object, Object>> getCarTaskListByMap(Map<String, Object> paramMap);
	/**
	 * 通过任务id和调用用途选择性查询车辆任务中车辆信息
	 * @param taskId
	 * @return
	 */
	public Map<String, Object> getCarTaskCarInfo(String taskId, String inscomcode, String opeType);
	/**
	 * 通过任务id查询车辆任务中其他信息
	 * @param taskId
	 * @return
	 */
	public Map<String, Object> getCarTaskOtherInfo(String taskId, String inscomcode, String opeType);
	
	public  Map<String,Object>  getCarInfoByTaskId(String taskid);

	/**
	 * 保存车辆信息
	 * @param 
	 * @param carmodelinfo
	 * @return
	 */
	public String editCarInfoAndModelInfo(CarinfoVO carinfo,INSBCarmodelinfo carmodelinfo);
	
	/**
	 * 车险任务分配
	 */
	public String carTaskDispatch(String toUser, String maininstanceId, String subInstanceId, 
			String inscomcode, String operator, INSCUser loginUser);
	
	/**
	 * 停止调度调用接口
	 */
	public String stopCarTaskDispatch(String maininstanceId, String subInstanceId, 
			String inscomcode, INSCUser loginUser);
	
	/**
	 * 重启调度调用接口
	 */
	public String restartCarTaskDispatch(String loginUsercode, String maininstanceId, String subInstanceId, 
			String inscomcode, INSCUser loginUser);
	
	/**
	 * 查询订单所有基础信息
	 */
	public Map<String, Object> getTaskAllInfo(String taskId, String inscomcode);
	
	/**
	 * 查询保险公司
	 */
	public Map<String,String> getInscompany(String inscomcode);
}


