package com.zzb.conf.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.conf.dao.INSBTaskprivilegeDao;
import com.zzb.conf.entity.INSBTaskprivilege;

@Repository
public class INSBTaskprivilegeDaoImpl extends BaseDaoImpl<INSBTaskprivilege> implements
		INSBTaskprivilegeDao {

	@Override
	public List<INSBTaskprivilege> selectByPCode(String pcode) {
		return this.sqlSessionTemplate.selectList("com.zzb.conf.entity.INSBTaskprivilege_selectByPCode", pcode);
	}

}