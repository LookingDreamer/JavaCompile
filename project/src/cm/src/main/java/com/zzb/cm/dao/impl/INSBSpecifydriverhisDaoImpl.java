package com.zzb.cm.dao.impl;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.cm.dao.INSBSpecifydriverhisDao;
import com.zzb.cm.entity.INSBSpecifydriverhis;

@Repository
public class INSBSpecifydriverhisDaoImpl extends BaseDaoImpl<INSBSpecifydriverhis> implements
		INSBSpecifydriverhisDao {

	@Override
	public INSBSpecifydriverhis selectSpecifyDriverhis(String processInstanceId, String inscomcode, String personid) {
		INSBSpecifydriverhis specifydriverhis = new INSBSpecifydriverhis(); 
		specifydriverhis.setTaskid(processInstanceId);
		specifydriverhis.setInscomcode(inscomcode);
		specifydriverhis.setPersonid(personid);
		return selectOne(specifydriverhis);
	}

}