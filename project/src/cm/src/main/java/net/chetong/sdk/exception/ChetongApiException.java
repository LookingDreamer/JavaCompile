/**
 * CHETONG.NET
 * Copyright (c) 2016, 产品研发部. All rights reserved.
 */
package net.chetong.sdk.exception;

import net.chetong.sdk.enums.ChetongErrorEnum;

/**
 * 车童网SDK统一异常信息处理
 * @version 1.0
 * @author 温德彬
 * @time 2016年6月29日  上午11:15:22
 * @since JDK 1.7
 */
public class ChetongApiException extends Exception{

	/** 
	 * @Fields serialVersionUID:TODO
	 */  
	private static final long serialVersionUID = 303473695679331391L;
	/**
	 * 错误代码
	 */
	private String errCode;
	/**
	 * 错误信息
	 */
	private String errMsg;
	
	/**
	 * 空构造
	 */
	public ChetongApiException() {
		super();
	}
	
	/**
	 * 带信息及异常
	 * @param message
	 * @param throwable
	 */
	public ChetongApiException(String message,Throwable throwable) {
		super(message,throwable);
	}
	

    /**
     * 带信息
     * @param message
     */
	public ChetongApiException(String message) {
		super(message);
	}
	
	/**
	 * 带异常
	 * @param throwable
	 */
	public ChetongApiException(Throwable throwable) {
		super(throwable);
	}

    /**
     * 带错误代码及错误信息
     * @param errCode
     * @param errMsg
     */
	public ChetongApiException(String errCode, String errMsg) {
		super("错误代码："+errCode+" 错误信息:"+errMsg);
		this.errCode = errCode;
		this.errMsg = errMsg;
	}
	
	/**
	 * 错误枚举异常
	 * @param chetongErrorEnum
	 */
	public ChetongApiException(ChetongErrorEnum chetongErrorEnum) {
		super("错误代码："+chetongErrorEnum.getCode()+" 错误信息:"+chetongErrorEnum.getErrMsg());
		this.errCode = chetongErrorEnum.getCode();
		this.errMsg = chetongErrorEnum.getErrMsg();
	}



	/**  
	 * 获取错误代码  
	 * @return errCode 错误代码  
	 */
	public String getErrCode() {
		return errCode;
	}


	/**  
	 * 获取错误信息  
	 * @return errMsg 错误信息  
	 */
	public String getErrMsg() {
		return errMsg;
	}


}
