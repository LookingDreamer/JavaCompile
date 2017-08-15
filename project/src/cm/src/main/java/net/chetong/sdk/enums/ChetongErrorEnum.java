/**
 * CHETONG.NET
 * Copyright (c) 2016, 产品研发部. All rights reserved.
 */
package net.chetong.sdk.enums;

import net.chetong.sdk.exception.ChetongApiException;

/**
 * 错误信息枚举类
 * @version 1.0
 * @author 温德彬
 * @time 2016年6月29日  上午11:06:58
 * @since JDK 1.7
 */
public enum ChetongErrorEnum {
	 HTTP_SUCCESS("1001","访问成功"),
     HTTP_TIMEOUT("1002","访问超时"),
	 PARAMS_MISSING("1003", "缺少必要的参数"), 
	 PARAMS_ILLEGAL("1004", "参数不合法"), 
	 ENCRYPT_SIGN_FAILED("1005", "签名加密失败"),
	 CHECK_SIGN_FAILED("1006","签名验证失败"),
	 SYSTEM_ERROR("1007","系统异常"),
	 REQUET_OFTEN("1008","操作太频繁"),
	 CHECK_APPKEY_FAILED("1009","验证APPKEY失败"),
	 CHECK_SYSENV_FAILED("1010","请求环境未被授权");
     
	/**
	 * 错误代码
	 */
	private String code;
	/**
	 * 错误信息
	 */
	private String errMsg;
    /**
     * 错误信息构造方法
     * @param code
     * @param errMsg
     */
	private ChetongErrorEnum(String code, String errMsg) {
		this.code = code;
		this.errMsg = errMsg;
	}
	
	/**
	 * 根据CODE获取错误信息
	 * @author 2016年6月29日  上午11:21:15  温德彬
	 * @param code
	 * @return
	 * @throws ChetongApiException
	 */
	public static ChetongErrorEnum getChetongErr(String code) throws ChetongApiException{
		for (ChetongErrorEnum errorEnum : values()) {
			if(code!=null && errorEnum.code.equals(code)){
				return errorEnum;
			}
		}
		throw new ChetongApiException("错误信息Code无效:"+code);
	}
	
	/**  
	 * 获取错误代码  
	 * @return code 错误代码  
	 */
	public String getCode() {
		return code;
	}
	/**  
	 * 设置错误代码  
	 * @param code 错误代码  
	 */
	public void setCode(String code) {
		this.code = code;
	}
	/**  
	 * 获取错误信息  
	 * @return errMsg 错误信息  
	 */
	public String getErrMsg() {
		return errMsg;
	}
	/**  
	 * 设置错误信息  
	 * @param errMsg 错误信息  
	 */
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
	 
	 
}
