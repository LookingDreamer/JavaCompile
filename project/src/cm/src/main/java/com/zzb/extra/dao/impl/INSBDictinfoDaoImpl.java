package com.zzb.extra.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.extra.dao.INSBDictinfoDao;
import com.zzb.extra.entity.INSBDictinfo;

@Repository
public class INSBDictinfoDaoImpl extends BaseDaoImpl<INSBDictinfo> implements
		INSBDictinfoDao {
	@Override
	public  List<Map<Object, Object>> getList(Map map) {
		return this.sqlSessionTemplate.selectList("INSBDictinfo_select", map);
	}
}