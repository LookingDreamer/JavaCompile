package com.lzgapi.yzt.service;

import java.util.Map;

import com.lzgapi.yzt.model.UserRegisterModel;
import com.zzb.mobile.model.CommonModel;

public interface UserAPIService {
	public UserRegisterModel register(String regInfoJSON);
	
	public UserRegisterModel registerAuto(String token);
	
	public CommonModel getIndex(String token, String lzgOtherUserId, String lzgManagerId);
	
	/**
	 * 
	 * 代理人升级掌柜
	 * 
	 * @param agentId 代理人id
	 * @param ismanager 是否是掌柜
	 * @param phone 代理人电话
	 * @param jobno 代理人工号
	 * @return
	 */
	public Map<String,String> up2LZGManager(String  agentId);
	
}
