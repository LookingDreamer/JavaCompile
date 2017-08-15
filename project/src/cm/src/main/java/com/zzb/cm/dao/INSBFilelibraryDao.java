package com.zzb.cm.dao;

import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseDao;
import com.zzb.cm.entity.INSBFilelibrary;

public interface INSBFilelibraryDao extends BaseDao<INSBFilelibrary> {
	/**
	 * 根据filebusinessCode查询文件列表
	 * @param code 
	 * @return
	 */
	public List<INSBFilelibrary> selectByFilebusinessCode(String code);
	
	public List<INSBFilelibrary> selectListByMap(Map<String, Object> map);
	@Deprecated
	public List<INSBFilelibrary> selectAll();
	
	public List<INSBFilelibrary> selectAll(Map<String, Object> Params);

	public int deleteIn(List<String> ids);


}