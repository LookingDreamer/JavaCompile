/**
 * CHETONG.NET
 * Copyright (c) 2016, 产品研发部. All rights reserved.
 */
package net.chetong.sdk.vo;

import java.io.Serializable;

import net.chetong.sdk.utils.JsonUtil;

/**
 * 公用车童网sdk返回模型
 * @version 1.0
 * @author 温德彬
 * @time 2016年6月29日  上午10:48:07
 * @since JDK 1.7
 */
public class CommonResponseVo implements Serializable{

	/** 
	 * @Fields serialVersionUID:TODO
	 */  
	private static final long serialVersionUID = 3730935464107588948L;
    /**
     * 存储义务数据
     */
    private String data;
    /**
     * 字符格式
     */
    private String charset;
    /**
     * 返回类型
     */
	private String format;
	/**
	 * 时间戳
	 * (UNIX)
	 */
	private String timestamp;
	/**  
	 * 获取返回数据  
	 * @return data 返回数据  
	 */
	public String getData() {
		return data;
	}
	/**  
	 * 设置返回数据  
	 * @param data 返回数据  
	 */
	public void setData(String data) {
		this.data = data;
	}
	/**  
	 * 获取字符格式  
	 * @return charset 字符格式  
	 */
	public String getCharset() {
		return charset;
	}
	/**  
	 * 设置字符格式  
	 * @param charset 字符格式  
	 */
	public void setCharset(String charset) {
		this.charset = charset;
	}
	/**  
	 * 获取返回类型  
	 * @return format 返回类型  
	 */
	public String getFormat() {
		return format;
	}
	/**  
	 * 设置返回类型  
	 * @param format 返回类型  
	 */
	public void setFormat(String format) {
		this.format = format;
	}

	/**  
	 * 获取时间戳(UNIX)  
	 * @return timestamp 时间戳(UNIX)  
	 */
	public String getTimestamp() {
		return timestamp;
	}
	/**  
	 * 设置时间戳(UNIX)  
	 * @param timestamp 时间戳(UNIX)  
	 */
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * 根据传入的对象类型获取对象值
	 * @author 2016年7月13日  上午10:43:16  温德彬
	 * @param t
	 * @return
	 */
	public <T> T getBuildVo(Class<T> t){
		return JsonUtil.toBean(data, t);
	}
}
