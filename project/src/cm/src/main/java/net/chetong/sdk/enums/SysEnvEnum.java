/**
 * CHETONG.NET
 * Copyright (c) 2016, 产品研发部. All rights reserved.
 */
package net.chetong.sdk.enums;

import net.chetong.sdk.exception.ChetongApiException;

/**
 * 系统环境类别枚举
 * 
 * @version 1.0
 * @author 温德彬
 * @time 2016年6月29日 上午9:23:26
 * @since JDK 1.7
 */
public enum SysEnvEnum {

	DEVELOP(
			    "develop", 
			    "http://dev.chetong.net/ct-api/api/gateWay.do", 
			    "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDBjsQcJG/W74xruQd/qamkSnBe5crqiFyzhN9fQONIatVC0zbTd64kiOb8+BEPqe/VMmPpecKALOiJf7DsStxQuC2Zxkg/gJ8lSIA7Ft5X6YHNI2Qv451h8N2YjGUwOfekUdU5v1pre+R0sxe5ruQS0+2rvua6PZuk9L+LcSZ6uwIDAQAB", 
			    "开发环境车童公钥"), 
	RELEASE(
			    "release", 
			    "", 
			    "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDBjsQcJG/W74xruQd/qamkSnBe5crqiFyzhN9fQONIatVC0zbTd64kiOb8+BEPqe/VMmPpecKALOiJf7DsStxQuC2Zxkg/gJ8lSIA7Ft5X6YHNI2Qv451h8N2YjGUwOfekUdU5v1pre+R0sxe5ruQS0+2rvua6PZuk9L+LcSZ6uwIDAQAB/6FTFY99uhpiq0qadD/uSzQsefWo0aTvP/65zi3eof7TcZ32oWpwIDAQAB/6FTFY99uhpiq0qadD/uSzQsefWo0aTvP/65zi3eof7TcZ32oWpwIDAQAB", 
			    "测试环境车童公钥"), 
	BETA(
			    "beta", 
			    "", 
			    "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDBjsQcJG/W74xruQd/qamkSnBe5crqiFyzhN9fQONIatVC0zbTd64kiOb8+BEPqe/VMmPpecKALOiJf7DsStxQuC2Zxkg/gJ8lSIA7Ft5X6YHNI2Qv451h8N2YjGUwOfekUdU5v1pre+R0sxe5ruQS0+2rvua6PZuk9L+LcSZ6uwIDAQAB/6FTFY99uhpiq0qadD/uSzQsefWo0aTvP/65zi3eof7TcZ32oWpwIDAQAB", 
			    "预发环境车童公钥"), 
	MASTER(
			    "master", 
			    "", 
			    "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDBjsQcJG/W74xruQd/qamkSnBe5crqiFyzhN9fQONIatVC0zbTd64kiOb8+BEPqe/VMmPpecKALOiJf7DsStxQuC2Zxkg/gJ8lSIA7Ft5X6YHNI2Qv451h8N2YjGUwOfekUdU5v1pre+R0sxe5ruQS0+2rvua6PZuk9L+LcSZ6uwIDAQAB/6FTFY99uhpiq0qadD/uSzQsefWo0aTvP/65zi3eof7TcZ32oWpwIDAQAB", 
			    "生产环境车童公钥");

	/**
	 * 调用环境
	 */
	private String sysEnv;
	/**
	 * 调用路径
	 */
	private String url;
	/**
	 * 车童公钥
	 */
	private String publicKey;
	/**
	 * 环境描述
	 */
	private String description;
	
    /**
     * 构造函数
     * @param sysEnv 系统类别
     * @param url 访问地址
     * @param publicKey 车童公钥
     * @param description 环境描述
     */
	private SysEnvEnum(String sysEnvCode, String url, String publicKey, String description){
	    this.sysEnv = sysEnvCode;
	    this.url = url;
	    this.publicKey = publicKey;
	    this.description = description;
	 }

	/**
	 * 根据系统环境CODE获取系统配置
	 * @author 2016年6月29日  上午10:43:42  温德彬
	 * @param sysEnvCode
	 * @return
	 * @throws ChetongApiException 
	 */
	public static SysEnvEnum getSysEnvByCode(String sysEnvCode) throws ChetongApiException{
		for (SysEnvEnum sysEnvEnum : values()) {
			if(sysEnvCode!=null&&sysEnvEnum.sysEnv.equals(sysEnvCode)){
				return sysEnvEnum;
			}
		}
		throw new ChetongApiException(ChetongErrorEnum.PARAMS_ILLEGAL.getCode(),"系统类型Code无效:"+sysEnvCode);
	}

	/**  
	 * 获取调用环境  
	 * @return sysEnv 调用环境  
	 */
	public String getSysEnv() {
		return sysEnv;
	}


	/**  
	 * 设置调用环境  
	 * @param sysEnv 调用环境  
	 */
	public void setSysEnv(String sysEnv) {
		this.sysEnv = sysEnv;
	}


	/**  
	 * 获取调用路径  
	 * @return url 调用路径  
	 */
	public String getUrl() {
		return url;
	}


	/**  
	 * 设置调用路径  
	 * @param url 调用路径  
	 */
	public void setUrl(String url) {
		this.url = url;
	}


	/**  
	 * 获取车童公钥  
	 * @return publicKey 车童公钥  
	 */
	public String getPublicKey() {
		return publicKey;
	}


	/**  
	 * 设置车童公钥  
	 * @param publicKey 车童公钥  
	 */
	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}


	/**  
	 * 获取环境描述  
	 * @return description 环境描述  
	 */
	public String getDescription() {
		return description;
	}


	/**  
	 * 设置环境描述  
	 * @param description 环境描述  
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	

}
