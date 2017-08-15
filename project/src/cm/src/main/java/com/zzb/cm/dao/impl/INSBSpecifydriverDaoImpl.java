package com.zzb.cm.dao.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.cm.dao.INSBSpecifydriverDao;
import com.zzb.cm.entity.INSBSpecifydriver;

@Repository
public class INSBSpecifydriverDaoImpl extends BaseDaoImpl<INSBSpecifydriver> implements
		INSBSpecifydriverDao {

	@Override
	public List<INSBSpecifydriver> selectSpecifyDriverByTaskid(String processInstanceId) {
		INSBSpecifydriver specifydriver = new INSBSpecifydriver();
		specifydriver.setTaskid(processInstanceId);
		return selectList(specifydriver);
	}

}