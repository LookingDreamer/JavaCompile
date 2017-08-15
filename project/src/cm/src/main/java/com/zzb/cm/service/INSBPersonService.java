package com.zzb.cm.service;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseService;
import com.zzb.cm.controller.vo.INSBRelationPersonVO;
import com.zzb.cm.entity.INSBPerson;

public interface INSBPersonService extends BaseService<INSBPerson> {
	/**
	 * 通过任务id和调用用途选择性查询车辆任务中关系人信息
	 * @param taskId
	 * @return
	 */
	public Map<String, Object> getCarTaskRelationPersonInfo(String taskId, String inscomcode, String opeType);
	/**
	 * 通过人员id查询车辆任务中单个关系人详细信息
	 * @param taskId
	 * @return
	 */
	public Map<String, Object> getOneOfCarTaskRelationPersonInfo(String taskid, String inscomcode, String opeType);
	/**
	 * 保存关系人信息
	 * @param taskId
	 */
	public String editRelationPersonInfo(INSBRelationPersonVO insbSBRelationPersonVO);
	
	
	public void deletebyID(String id);
	/**
	 * 公共页面修改车主信息
	 * @param carowner
	 * @param taskid
	 * @return 
	 */
	public String updateCarOwnerInfo(INSBPerson carowner, String taskid);
	/**
	 * 获取需要删除insbPerson表无效记录id
	 * @param taskID 工作流号
	 * @return
	 */
	public List<String> getSelectDelId(String taskID);
	/**
	 * 根据工作流号，获取车主人员信息
	 * @param taskId 工作流号
	 * @return
	 */
	public INSBPerson selectCarOwnerPersonByTaskId(String taskId);
	
}