package com.zzb.mobile.service;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.zzb.cm.dao.INSBOrderDao;
import com.zzb.cm.entity.INSBOrder;
import com.zzb.conf.dao.INSBOrderManageDao;
import com.zzb.conf.dao.INSBPolicyManageDao;
import com.zzb.conf.dao.INSBPolicyitemDao;
import com.zzb.conf.entity.INSBPolicyitem;
import com.zzb.mobile.model.CommonModel;
import com.zzb.mobile.service.AppInsuredHomePageService;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration(value = "src/main/webapp")
@ContextConfiguration(locations = { "classpath:config/spring-config.xml",
		"classpath:config/spring-security-config.xml",
		"classpath:config/spring-mvc-config.xml",
		"classpath:config/spring-config-db.xml", })
/**
 * 
 * @Title: AppInsuredHomePageServiceTest.java
 * @Package com.zzb.mobile.service
 * @Description: TODO
 * @author GuoKey
 * @date 2015年10月17日 下午10:30:30
 * @version 1.0
 */
public class AppInsuredHomePageServiceTest{
	
    @Resource
	private AppInsuredHomePageService appInsuredHomePageService;

	@Test          //ok
	public void getTobePolicyNum() {
//		CommonModel result = appInsuredHomePageService.getTobePolicyNum("789789");
//		System.out.println("Status="+result.getStatus());
//		System.out.println("Message="+result.getMessage());
//		System.out.println("Body="+result.getBody());
	}

	@Test         // ok
	public void getTobePaymentOrderNum() {
//		CommonModel result =appInsuredHomePageService.getTobePaymentOrderNum("789789");
//		System.out.println("Status="+result.getStatus());
//		System.out.println("Message="+result.getMessage());
//		System.out.println("Body="+result.getBody());
	}

	@Test        //ok
	public void renewalRemindNum() {
//		CommonModel result = appInsuredHomePageService.renewalRemindNum("789789");
//		System.out.println("Status="+result.getStatus());
//		System.out.println("Message="+result.getMessage());
//		System.out.println("Body="+result.getBody());
		
	}

	
}
