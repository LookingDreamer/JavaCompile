/**
 * CHETONG.NET
 * Copyright (c) 2016, 产品研发部. All rights reserved.
 */
package net.chetong.sdk.enums;

import java.util.ArrayList;
import java.util.List;

import net.chetong.sdk.exception.ChetongApiException;

/**
 * 公用参数定义枚举
 * @version 1.0
 * @author 温德彬
 * @time 2016年6月29日  下午1:50:23
 * @since JDK 1.7
 */
public enum CommonParamEnum {
	
	APP_KEY("appKey",  "", "应用接入唯一key"), 
	BIZ_DATA("data",  "", "业务级参数，需加密"), 
	TIME_STAMP("timestamp",  "", "时间搓"), 
	SERVICE_NAME("serviceName", "", "服务名"), 
	CHARSET("charset", "UTF-8", "字符集"), 
	SIGN("sign", "", "签名"),
	FORMAT("format","json","响应格式"),
	VERSION("version","1.0.0","版本信息");

	/**
	 * 参数CODE
	 */
	private String code;
	/**
	 * 参数默认值
	 */
	private String defaultValue;
	/**
	 * 参数描述
	 */
	private String description;
	
	/**
	 * 构造方法
	 * @param code
	 * @param defaultValue
	 * @param description
	 */
	private CommonParamEnum(String code, String defaultValue, String description) {
		this.code = code;
		this.defaultValue = defaultValue;
		this.description = description;
	}
	/**
	 * 判断参数CODE是否存在
	 * @author 2016年6月29日  下午1:55:49  温德彬
	 * @param code
	 * @return
	 */
	public static boolean isExist(String code){
		for (CommonParamEnum paramEnum : values()) {
			if(code!=null && paramEnum.getCode().equals(code)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 根据CODE获取公用参数
	 * @author 2016年6月29日  下午1:58:00  温德彬
	 * @param code
	 * @return
	 * @throws ChetongApiException 
	 */
	public static CommonParamEnum getSysParams(String code) throws ChetongApiException{
		for (CommonParamEnum paramEnum : values()) {
			if(code!=null && paramEnum.getCode().equals(code)){
				return paramEnum;
			}
		}
		throw new  ChetongApiException(ChetongErrorEnum.PARAMS_ILLEGAL.getCode(),"无效的系统参数");
	}
	
	/**
	 * 获取所有的CODE列表
	 * @author 2016年6月29日  下午2:26:25  温德彬
	 * @return
	 */
	public static List<String> getCodeList() {
		List<String> codeList = new ArrayList<String>();
		for (CommonParamEnum paramEnum : values()) {
			codeList.add(paramEnum.getCode());
		}
		return codeList;
	}
	
	
	/**  
	 * 获取参数CODE  
	 * @return code 参数CODE  
	 */
	public String getCode() {
		return code;
	}
	/**  
	 * 设置参数CODE  
	 * @param code 参数CODE  
	 */
	public void setCode(String code) {
		this.code = code;
	}
	/**  
	 * 获取参数默认值  
	 * @return defaultValue 参数默认值  
	 */
	public String getDefaultValue() {
		return defaultValue;
	}
	/**  
	 * 设置参数默认值  
	 * @param defaultValue 参数默认值  
	 */
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	/**  
	 * 获取参数描述  
	 * @return description 参数描述  
	 */
	public String getDescription() {
		return description;
	}
	/**  
	 * 设置参数描述  
	 * @param description 参数描述  
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	

	
}
