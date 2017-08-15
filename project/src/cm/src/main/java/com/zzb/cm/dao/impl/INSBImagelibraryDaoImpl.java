package com.zzb.cm.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.cm.dao.INSBImagelibraryDao;
import com.zzb.cm.entity.INSBImagelibrary;

@Repository
public class INSBImagelibraryDaoImpl extends BaseDaoImpl<INSBImagelibrary> implements
		INSBImagelibraryDao {
	@Override
	public List<Map<Object, Object>> selectImageByAgent(Map<String, Object> mapconditions) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectImageListPaging"),mapconditions);
	}

	@Override
	public int selectCount(Map<String, Object> mapconditions) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectPageCount"), mapconditions);
	}

	@Override
	public List<Map<Object, Object>> selectDeatilInfo(Map<String,Object> mapconditions) {
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectImageInfoListPaging"),mapconditions);
	}

	@Override
	public int selectDeatilCount(Map<String, Object> mapconditions) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectDetailPageCount"), mapconditions);
	}
}