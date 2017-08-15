package com.zzb.cm.service;

import java.util.Map;

import com.cninsure.core.dao.BaseService;
import com.zzb.cm.entity.INSBImagelibrary;

public interface INSBImagelibraryService extends BaseService<INSBImagelibrary> {
	/**
	 * 通过代理人code获得影像信息
	 * @param conditions
	 * @return
	 */
	public Map<String,Object> queryImageByAgent(String agentCode,String carlicenseno,String insuredname,int offset,int limit);
	
	/**
	 * 获得图片的详细信息
	 * @param imageLibraryId
	 * @param agentCode
	 * @return
	 */
	public Map<String,Object> queryDeatilInfo(String imageLibraryId, String agentCode,String imageTypeName,int offset,int limit);

}