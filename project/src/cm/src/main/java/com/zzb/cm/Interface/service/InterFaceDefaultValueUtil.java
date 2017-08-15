package com.zzb.cm.Interface.service;

import com.common.redis.CMRedisClient;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.cninsure.core.utils.DateUtil;
import com.cninsure.core.utils.IDCardNoGenerator;
import com.cninsure.core.utils.StringUtil;


public class InterFaceDefaultValueUtil {

	private static final String DefaultIdCardType = "0";//身份证类型
	private static final String DefaultIdCard = "131182198910126232";//默认idcardno
	private static final String DefaultProperty = "0";//用户车辆类型，个人用车
	private static final String DefaultCarProperty = "1";//家庭自用车
	private static final String DefaultDrivingArea = "0";//约定行驶区域,默认境内
	private static final String MODULE = "cm:zzb:interface:default_value_util";
	//过期时间为7天
	private static final int expire = 604800;

	private static String getStartDate(){
		return new SimpleDateFormat(DateUtil.Format_Date).format(DateUtil.addDay(new Date(), 1)) + " 00:00:00";
		
	}
	
	private static String getEndDate(){
		return new SimpleDateFormat(DateUtil.Format_Date).format(DateUtil.addYear(new Date(), 1)) + " 23:59:59";

	}
	
	public static String getAddress(String taskId, String insurancecompanyid, String classType, String property, String value){
		return value;
	}
	
	public static Object getDefaultValue(String taskId, String insurancecompanyid, String classType, String property){
		switch (property) {
			case "property": return DefaultProperty;
			case "carproperty": return DefaultCarProperty;
			case "idcardtype": return DefaultIdCardType;
			case "drivingarea": return DefaultDrivingArea;
			case "idcardno": return DefaultIdCard;
			case "startdate": return getStartDate();
			case "enddate": return getEndDate();
			default: return "";
		}
	}
}
