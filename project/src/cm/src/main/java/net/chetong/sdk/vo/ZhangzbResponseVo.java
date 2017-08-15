/**
 * CHETONG.NET
 * Copyright (c) 2016, 产品研发部. All rights reserved.
 */
package net.chetong.sdk.vo;

public class ZhangzbResponseVo {

	private String errorCode;
	
	private String errorMsg;
	
	private String requestUrl;
	
	private String requestNo;

	/**  
	 * 获取errorCode  
	 * @return errorCode errorCode  
	 */
	public String getErrorCode() {
		return errorCode;
	}

	/**  
	 * 设置errorCode  
	 * @param errorCode errorCode  
	 */
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	/**  
	 * 获取errorMsg  
	 * @return errorMsg errorMsg  
	 */
	public String getErrorMsg() {
		return errorMsg;
	}

	/**  
	 * 设置errorMsg  
	 * @param errorMsg errorMsg  
	 */
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	/**  
	 * 获取requestUrl  
	 * @return requestUrl requestUrl  
	 */
	public String getRequestUrl() {
		return requestUrl;
	}

	/**  
	 * 设置requestUrl  
	 * @param requestUrl requestUrl  
	 */
	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}

	/**  
	 * 获取requestNo  
	 * @return requestNo requestNo  
	 */
	public String getRequestNo() {
		return requestNo;
	}

	/**  
	 * 设置requestNo  
	 * @param requestNo requestNo  
	 */
	public void setRequestNo(String requestNo) {
		this.requestNo = requestNo;
	}
	
	
}
