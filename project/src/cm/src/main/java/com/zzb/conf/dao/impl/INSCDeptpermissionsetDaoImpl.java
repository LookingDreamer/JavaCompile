package com.zzb.conf.dao.impl;
 
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.conf.dao.INSCDeptpermissionsetDao;
import com.zzb.conf.entity.INSCDeptpermissionset;

@Repository
public class INSCDeptpermissionsetDaoImpl extends BaseDaoImpl<INSCDeptpermissionset> implements
		INSCDeptpermissionsetDao {

	@Override
	public Map<String, String> queryByComcode(String comcode) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("queryByComcode"),comcode);
	}

	@Override
	public int updateBycomcode(INSCDeptpermissionset ps) {
		return this.sqlSessionTemplate.update(this.getSqlName("updateBycomcode"), ps);	
	}

	@Override
	public List<INSCDeptpermissionset> selectDeptByDeptid(String deptid) {
		Map<String,Object> param = new HashMap<String,Object>();
		param.put("deptid", deptid);
		return this.sqlSessionTemplate.selectList(this.getSqlName("selectDeptByDeptid"),param);
	}

	@Override
	public INSCDeptpermissionset selectDeptByComcode(String deptid) {
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectDeptByComcode"),deptid);
	}
	
	
}