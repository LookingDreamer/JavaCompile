package com.zzb.mobile.dao;

import java.util.Map;
import com.cninsure.core.dao.BaseDao;
import com.zzb.mobile.entity.Insbpaymentpassword;

public interface InsbpaymentpasswordDao extends BaseDao<Insbpaymentpassword>{
	
	public Insbpaymentpassword selectOnebyMap(Map<String,Object> map);
	 	
	public void updatePaypwd(Insbpaymentpassword insbpaypw);
		
	public void insertPaypwd(Insbpaymentpassword insbpaypw);
	
	
}
