package com.zzb.cm.service;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseService;
import com.zzb.cm.entity.INSBFlowerror;

public interface INSBFlowerrorService extends BaseService<INSBFlowerror> {

	public Map<String,String> getErrorInfo(String taskid, String inscomcode);
	
	public Map<String, Object> initErrorList(Map<String, Object> map);
	/** 
	 * 获取所有的任务状态
	 * @return 
	 */
	public List<INSBFlowerror> selectflowcode();

	public Map<String, Object> initPushtocoreErrorList(Map<String, Object> data);

	public Map<String, Object> initClosstaskList(Map<String, Object> queryMap);
	/**
	 * 更新或者修改流程错误信息
	 * @param taskId
	 * @param inscomcode
	 * @param flowcode
	 */
	public void insertInsbFlowerror(String taskId,String inscomcode,String flowcode,String flowName,String fairyorEdi,String taskStatus,String result,String errordesc,String operator);
	
}