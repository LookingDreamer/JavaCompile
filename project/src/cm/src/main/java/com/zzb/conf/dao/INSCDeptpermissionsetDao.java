package com.zzb.conf.dao;
 
import java.util.List;
import java.util.Map;

import com.cninsure.core.dao.BaseDao;
import com.zzb.conf.entity.INSCDeptpermissionset;

public interface INSCDeptpermissionsetDao extends BaseDao<INSCDeptpermissionset> {

	public Map<String, String> queryByComcode(String comcode);

	public int updateBycomcode(INSCDeptpermissionset ps);

	public List<INSCDeptpermissionset> selectDeptByDeptid(String deptid);
	
	public INSCDeptpermissionset selectDeptByComcode(String deptid);
}