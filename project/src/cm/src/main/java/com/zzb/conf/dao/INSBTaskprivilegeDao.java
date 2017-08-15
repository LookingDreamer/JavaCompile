package com.zzb.conf.dao;

import java.util.List;

import com.cninsure.core.dao.BaseDao;
import com.zzb.conf.entity.INSBTaskprivilege;

public interface INSBTaskprivilegeDao extends BaseDao<INSBTaskprivilege> {

	public List<INSBTaskprivilege> selectByPCode(String pcode);
}