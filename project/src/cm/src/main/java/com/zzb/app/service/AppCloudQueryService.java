package com.zzb.app.service;

public interface AppCloudQueryService {

	/**
	 * 根据商业险或者交强险保单号查询上年投保信息
	 * @param type
	 * @param insureno
	 * @return
	 */
	String queryByNumber(String type, String insureno);

	/**
	 * 根据车架号或者发动机号查询上年投保信息
	 * @param framenumber
	 * @param enginenumber
	 * @return
	 */
	String queryByCarInfo(String framenumber, String enginenumber);

}
