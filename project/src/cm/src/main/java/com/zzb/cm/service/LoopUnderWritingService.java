package com.zzb.cm.service;

import java.util.Map;

public interface LoopUnderWritingService {
	/**
	 * 执行核保轮询逻辑
	 * @param contextParam
	 * @throws Exception
	 */
	public void executeService(Map<String, Object> contextParam) throws Exception;
	
}
