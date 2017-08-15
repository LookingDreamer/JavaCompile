package com.zzb.app.service;

public interface AppCarModelInfoService {

	/**
	 * 获取车型处理有关的数据项
	 * 
	 * @param taskId
	 * @param inscomcode
	 * @return
	 */
	public  String getCarModelInfo(String taskId, String inscomcode);

}