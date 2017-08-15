/**
 * CHETONG.NET
 * Copyright (c) 2016, 产品研发部. All rights reserved.
 */
package net.chetong.sdk.client;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import net.chetong.sdk.enums.ChetongErrorEnum;
import net.chetong.sdk.enums.CommonParamEnum;
import net.chetong.sdk.enums.SysEnvEnum;
import net.chetong.sdk.exception.ChetongApiException;
import net.chetong.sdk.utils.HttpClientUtils;
import net.chetong.sdk.utils.JsonUtil;
import net.chetong.sdk.utils.RSAUtils;
import net.chetong.sdk.utils.StringUtil;
import net.chetong.sdk.vo.CommonRequestVo;
import net.chetong.sdk.vo.CommonResponseVo;

/**
 * 针对车童网的请求入口
 * @version 1.0
 * @author 温德彬
 * @time 2016年6月29日  上午9:09:48
 * @since JDK 1.7
 */
public class ChetongHttpClient {
    /**
     * 请求连接超时时间
     */
	private int timeout = 3000;
	/**
	 * 应答超时时间
	 */
	private int readTimeout = 80000;
	/**
	 * 对接系统环境
	 */
	private String sysEnv;
	/**
	 * 客户端识别码
	 */
	private String appKey;
	/**
	 * 客户端私钥
	 */
	private String privateKey;
	/**
	 * 客户端请求路径
	 */
	private String url;
	/**
	 * 车童网公钥
	 */
	private String ctPublicKey;
	/**
	 * 编码类型
	 */
	private String charset = "UTF-8";
	/**
	 * 时间戳Unix
	 */
	private String timestamp;
	
	/**
	 * 对外构造函数
	 * @param sysEnv 系统类别
	 * @param appKey 客户端识别码
	 * @param privateKey 客户私钥
	 */
	public ChetongHttpClient(String sysEnv, String appKey, String privateKey) {
		super();
		this.sysEnv = sysEnv;
		this.appKey = appKey;
		this.privateKey = privateKey;
	}

    /**
     * 对外构造函数
     * @param timeout 超时时间
     * @param sysEnv 系统类别
     * @param appKey 客户端识别码
     * @param privateKey 客户私钥
     * @param charset 字符类型
     * @param timestamp 时间戳
     */
	public ChetongHttpClient( String sysEnv, String appKey, String privateKey, String charset,
			String timestamp,int timeout) {
		super();
		this.timeout = timeout;
		this.sysEnv = sysEnv;
		this.appKey = appKey;
		this.privateKey = privateKey;
		this.charset = charset;
		this.timestamp = timestamp;
	}
	
	/**
	 * 初始化请求参数
	 * @author 2016年6月29日  下午12:39:37  温德彬
	 * @throws ChetongApiException 
	 */
	private void init() throws ChetongApiException{
		this.timestamp = (System.currentTimeMillis()/1000l)+"";
		SysEnvEnum sysEnvEnum = SysEnvEnum.getSysEnvByCode(sysEnv);
		if(StringUtil.isNullOrEmpty(this.url)){
			this.url = sysEnvEnum.getUrl();
		}
		//验证是否有该环境的URL
		if(this.url.equals("")){
			throw new ChetongApiException(ChetongErrorEnum.CHECK_SYSENV_FAILED);
		}
		this.ctPublicKey = sysEnvEnum.getPublicKey();
	}
	
	/**
	 * 客户端请求入口
	 * @author 2016年6月29日  下午1:45:22  温德彬
	 * @param requestVo 请求参数
	 * @return
	 * @throws ChetongApiException
	 */
	public CommonResponseVo call(CommonRequestVo requestVo) throws ChetongApiException{
		//1）初始化请求参数
		init();
		//2）初始化系统参数
		requestVo.setResponseVo(new CommonResponseVo());
		requestVo.checkSysParam();
		JSONObject signObject = requestVo.getParams();
		signObject.put(CommonParamEnum.APP_KEY.getCode(), this.appKey);
		signObject.put(CommonParamEnum.CHARSET.getCode(), this.charset);
		signObject.put(CommonParamEnum.TIME_STAMP.getCode(), this.timestamp);
		signObject.put(CommonParamEnum.FORMAT.getCode(), CommonParamEnum.FORMAT.getDefaultValue());
		signObject.put(CommonParamEnum.VERSION.getCode(),  CommonParamEnum.VERSION.getDefaultValue());
		//3)对业务数据进行加密并加签
		Map<String,Object> paramsMap = null;
		try {
			paramsMap = RSAUtils.encryptAndSign(signObject, this.ctPublicKey, this.privateKey);
		} catch (Exception e) {
			throw new ChetongApiException(ChetongErrorEnum.ENCRYPT_SIGN_FAILED.getCode(),e.getMessage());
		}
		String result = HttpClientUtils.sendPost(this.url, paramsMap, this.timeout, this.readTimeout);
		JSONObject responseJson = JSONObject.parseObject(result);
		//判断是否正确返回数据
		if(!responseJson.containsKey("errorCode")){
			//判断是否有业务参数传过来
			boolean isDecrypt = !StringUtil.isNullOrEmpty(responseJson.get(CommonParamEnum.BIZ_DATA.getCode()));
			//对返回结果进行验签并解密
			String responseResult = RSAUtils.checkSignAndDecrypt(responseJson,this.ctPublicKey, this.privateKey, isDecrypt);
			responseJson.put(CommonParamEnum.BIZ_DATA.getCode(), responseResult);
		}else{
			//异常处理
			responseJson.put(CommonParamEnum.BIZ_DATA.getCode(), responseJson.toJSONString());
		}
		
		return JsonUtil.toBean(responseJson.toJSONString(), CommonResponseVo.class);
	}
	
	
}
