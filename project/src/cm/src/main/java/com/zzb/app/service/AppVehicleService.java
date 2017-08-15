package com.zzb.app.service;

import java.io.IOException;


public interface AppVehicleService{
	
	/**
	 * @param header token 信息   
	 * @param param 参数信息 key  分页信息
	 * @return
	 */
	public String queryVehicleByType(String header,String param)throws IOException;
	
	
	/**
	 * 通过品牌系类查找车辆数据
	 * 
	 * @param header
	 * @param param
	 * @return
	 */
	public String queryVehicleByBrandName(String header,String param);
	
}
