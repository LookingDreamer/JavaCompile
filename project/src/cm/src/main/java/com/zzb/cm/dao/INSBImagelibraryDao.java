package com.zzb.cm.dao;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseDao;
import com.zzb.cm.entity.INSBImagelibrary;

public interface INSBImagelibraryDao extends BaseDao<INSBImagelibrary> {
	
	public List<Map<Object,Object>> selectImageByAgent(Map<String,Object> mapconditions);
	
	public int selectCount(Map<String,Object> mapconditions);
	
	public List<Map<Object,Object>> selectDeatilInfo(Map<String,Object> mapconditions);
	
	public int selectDeatilCount(Map<String,Object> mapconditions);
	
}