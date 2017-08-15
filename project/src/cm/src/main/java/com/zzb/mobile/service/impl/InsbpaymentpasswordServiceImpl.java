package com.zzb.mobile.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.cninsure.core.utils.StringUtil;
import com.cninsure.core.utils.UUIDUtils;
import com.zzb.conf.dao.INSBAgentDao;
import com.zzb.conf.entity.INSBAgent;
import com.zzb.mobile.dao.InsbpaymentpasswordDao;
import com.zzb.mobile.entity.Insbpaymentpassword;
import com.zzb.mobile.model.CommonModel;
import com.zzb.mobile.service.InsbpaymentpasswordService;
import com.zzb.mobile.util.EncodeUtils;


@Service
@Transactional
public class InsbpaymentpasswordServiceImpl extends BaseServiceImpl<Insbpaymentpassword> implements InsbpaymentpasswordService {

@Resource
private InsbpaymentpasswordDao insbpaymentpasswordDao;
@Resource
private INSBAgentDao insbAgentDao; 

	@Override
	public CommonModel passwordValiadate(String jobNum,String logpwd) {
		CommonModel model=null;	            
		//LdapMgr ldap = new LdapMgr();
		INSBAgent agentModel = insbAgentDao.selectByJobnum(jobNum);
		//LdapAgentModel agentModel = ldap.searchAgent(jobNum);   //根据工号查询在线用户
		String pwd = StringUtil.md5Base64(logpwd);            
	   if(agentModel!=null){	   
		   int flag=0;                       
		   if(pwd.equals(agentModel.getPwd())){
			   model=new CommonModel();
			   flag=1;                       //输入密码与登录密码返回1
	    	   model.setStatus("success");
	    	   model.setMessage("验证成功");
	    	   model.setBody("status:"+flag);
		   }else{
			   model=new CommonModel();
	    	   model.setStatus("fail");	 
	    	   model.setMessage("验证失败");
	    	   model.setBody("status:"+flag);        //输入密码与登录密码返回0     
	       }
       }
	  
	   return model;		   
	}

	@Override
	public CommonModel paypwdSetting(String jobNum,String paypwd) {
		CommonModel model=new CommonModel();
		Insbpaymentpassword insbpaypw=new Insbpaymentpassword();
		Map<String,Object> map=new HashMap<String,Object>();
		String pwd = EncodeUtils.encodeMd5(paypwd);
		map.put("jobNum", jobNum);            
		Insbpaymentpassword insbpaypwd=insbpaymentpasswordDao.selectOnebyMap(map);
		if(insbpaypwd!=null){    //如果用户已设置支付密码，更新
			if(insbpaypwd.getPassword()!=null){
				insbpaypw=new Insbpaymentpassword();
				insbpaypw.setId(insbpaypwd.getId());
				insbpaypw.setJobNum(insbpaypwd.getJobNum());
				insbpaypw.setOperator(insbpaypwd.getOperator());
				insbpaypw.setPassword(pwd);
				insbpaypw.setModifytime(new Date());
				insbpaymentpasswordDao.updatePaypwd(insbpaypw);	
			}
		}else{                 //如果用户没有设置支付密码，添加
			insbpaypw=new Insbpaymentpassword();
			insbpaypw.setId(UUIDUtils.random());
			insbpaypw.setPassword(pwd);
			insbpaypw.setJobNum(jobNum);
			insbpaypw.setOperator(jobNum);
			insbpaypw.setCreatetime(new Date());
			insbpaymentpasswordDao.insertPaypwd(insbpaypw);
		}
		model.setMessage("密码设置成功");
		model.setStatus("success");
		
		return model;
	}



	@Override
	protected BaseDao<Insbpaymentpassword> getBaseDao() {
		// TODO Auto-generated method stub
		return insbpaymentpasswordDao;
	}

	@Override
	public CommonModel hasPayPassword(String jobNum) {
		CommonModel model=new CommonModel();	            
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("jobNum", jobNum);            
		Insbpaymentpassword insbpaypwd=insbpaymentpasswordDao.selectOnebyMap(map);
		if(insbpaypwd!=null){
			model.setMessage("有二次支付密码");
			model.setStatus("success");
		}else{
			model.setMessage("没有二次支付密码");
			model.setStatus("fail");
		}
	   return model;
	}

}
