package com.zzb.mobile.dao.impl;

import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cninsure.core.dao.impl.BaseDaoImpl;
import com.zzb.mobile.dao.InsbpaymentpasswordDao;
import com.zzb.mobile.entity.Insbpaymentpassword;
@Repository
public class InsbpaymentpasswordDaoImpl extends BaseDaoImpl<Insbpaymentpassword> implements InsbpaymentpasswordDao {

	@Override
	public Insbpaymentpassword selectOnebyMap(Map<String,Object> map){
		
		return this.sqlSessionTemplate.selectOne(this.getSqlName("selectOnebyMap"), map);

	}
	@Override	
	public void updatePaypwd(Insbpaymentpassword insbpaypw){		
	
		this.sqlSessionTemplate.update(this.getSqlName("updatePaypwd"), insbpaypw);

	}
	@Override
	public void insertPaypwd(Insbpaymentpassword insbpaypw){
		
		this.sqlSessionTemplate.insert(this.getSqlName("insertPaypwd"), insbpaypw);
	
	}
	
}
