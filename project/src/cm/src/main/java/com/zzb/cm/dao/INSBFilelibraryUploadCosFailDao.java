package com.zzb.cm.dao;

import com.cninsure.core.dao.BaseDao;
import com.zzb.cm.entity.INSBFilelibrary;
import com.zzb.cm.entity.INSBFilelibraryUploadCosFail;

import java.util.List;
import java.util.Map;


public interface INSBFilelibraryUploadCosFailDao extends BaseDao<INSBFilelibraryUploadCosFail> {

	public List<INSBFilelibraryUploadCosFail> selectListByMap(Map<String, Object> map);

	public int deleteIn(List<String> ids);


	public List<INSBFilelibraryUploadCosFail> selectOneTimeReUploadCos();
}