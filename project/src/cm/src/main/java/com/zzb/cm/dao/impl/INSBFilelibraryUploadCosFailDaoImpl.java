package com.zzb.cm.dao.impl;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.cm.dao.INSBFilelibraryDao;
import com.zzb.cm.dao.INSBFilelibraryUploadCosFailDao;
import com.zzb.cm.entity.INSBFilelibrary;
import com.zzb.cm.entity.INSBFilelibraryUploadCosFail;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


@Repository
public class INSBFilelibraryUploadCosFailDaoImpl extends BaseDaoImpl<INSBFilelibraryUploadCosFail> implements
		INSBFilelibraryUploadCosFailDao {

	@Override
	public List<INSBFilelibraryUploadCosFail> selectListByMap(Map<String,Object> map) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectListBymap"),map);
	}

	@Override
	public int deleteIn(List<String> list){
		return this.sqlSessionTemplate.delete(this.getSqlName("deleteInIds"), list);
	}


	@Override
	public List<INSBFilelibraryUploadCosFail> selectOneTimeReUploadCos() {
		return this.sqlSessionTemplate.selectList(this.getSqlName("oneTimeReUploadCos"));
	}
}