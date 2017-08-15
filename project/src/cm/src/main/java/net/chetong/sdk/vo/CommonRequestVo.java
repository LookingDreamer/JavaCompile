/**
 * CHETONG.NET
 * Copyright (c) 2016, 产品研发部. All rights reserved.
 */
package net.chetong.sdk.vo;

import java.io.Serializable;

import com.alibaba.fastjson.JSONObject;

import net.chetong.sdk.enums.ChetongErrorEnum;
import net.chetong.sdk.exception.ChetongApiException;
import net.chetong.sdk.utils.JsonUtil;
import net.chetong.sdk.utils.StringUtil;

/**
 * 车童网SDK公用请求模型
 * @version 1.0
 * @author 温德彬
 * @time 2016年6月29日  上午10:54:47
 * @since JDK 1.7
 */
public class CommonRequestVo implements Serializable{

	/** 
	 * @Fields serialVersionUID:TODO
	 */  
	private static final long serialVersionUID = -8279523428955380804L;
    /**
     * 接口名称
     */
	private String serviceName;
	/**
	 * 业务数据参数
	 * (传对象)
	 */
	private Object params;
	/**
	 * 公用请求参数
	 */
	private CommonResponseVo responseVo;
	
	public CommonRequestVo() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * 构造函数
	 * @param serviceName
	 * @throws ChetongApiException
	 */
	public CommonRequestVo(String serviceName) throws ChetongApiException {
		if(StringUtil.isNullOrEmpty(serviceName)){
			throw new ChetongApiException(ChetongErrorEnum.PARAMS_ILLEGAL.getCode(),"serviceName不能为空");
		}
		this.serviceName = serviceName;
	}
	
	/**
	 * 验证系统参数是否添加
	 * @author 2016年6月29日  下午2:13:19  温德彬
	 * @throws ChetongApiException
	 */
	public void checkSysParam() throws ChetongApiException{
		if(StringUtil.isNullOrEmpty(getServiceName())){
			throw new ChetongApiException(ChetongErrorEnum.PARAMS_ILLEGAL.getCode(),"缺少必填的系统参数:serviceName");
		}
	}
	
	/**  
	 * 获取接口名称  
	 * @return serviceName 接口名称  
	 */
	public String getServiceName() {
		return serviceName;
	}
	/**  
	 * 设置接口名称  
	 * @param serviceName 接口名称  
	 */
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
    
	/**  
	 * 获取业务数据参数
	 * @return params 业务数据参数
	 */
	public JSONObject getParams() {
		JSONObject jsonObject = null;
		if(!(params instanceof JSONObject)){
			 jsonObject = JsonUtil.parseObject(params);
		}else{
			jsonObject = (JSONObject)params;
		}
		if(!jsonObject.containsKey("serviceName")){
			jsonObject.put("serviceName", getServiceName());
		}
		return jsonObject;
	}
	/**  
	 * 设置业务数据参数
	 * @param params 业务数据参数
	 */
	public void setParams(Object params) {
		this.params = params;
	}
	/**  
	 * 获取公用请求参数  
	 * @return responseVo 公用请求参数  
	 */
	public CommonResponseVo getResponseVo() {
		return responseVo;
	}
	/**  
	 * 设置公用请求参数  
	 * @param responseVo 公用请求参数  
	 */
	public void setResponseVo(CommonResponseVo responseVo) {
		this.responseVo = responseVo;
	}
	
	
    
}
