package com.zzb.cm.dao.impl;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.cm.dao.INSBLoopunderwritingdetailDao;
import com.zzb.cm.entity.INSBLoopunderwritingdetail;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class INSBLoopunderwritingdetailDaoImpl extends BaseDaoImpl<INSBLoopunderwritingdetail> implements
		INSBLoopunderwritingdetailDao {

	public List<INSBLoopunderwritingdetail> queryByLoopId(String loopId) {
		Map<String, String> params = new HashMap<>(1);
		params.put("loopid", loopId);
		return this.sqlSessionTemplate.selectList(this.getSqlName("select"), params);
	}
}