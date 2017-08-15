package com.zzb.mobile.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.zzb.chn.util.JsonUtils;
import com.zzb.conf.service.INSBPermissionallotService;
import com.zzb.mobile.model.CommonModel;
import com.zzb.mobile.model.LastInsuredRenewaByCarModel;
import com.zzb.mobile.model.SearchProviderModel;
import com.zzb.mobile.model.SelectProviderBean;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration(value = "src/main/webapp")
@ContextConfiguration(locations = { "classpath:config/spring-config.xml",
		"classpath:config/spring-security-config.xml",
		"classpath:config/spring-mvc-config.xml",
		"classpath:config/spring-config-db.xml",
		})
public class AppInsuredQuoteServiceTest {
	@Resource
	private AppInsuredQuoteService appInsuredQuoteService;
	@Resource
	INSBPermissionallotService insbPermissionallotService;
	@Test
	public void testkind(){
		LastInsuredRenewaByCarModel ddd=new LastInsuredRenewaByCarModel();
//		ddd.setProcessinstanceid("8");
//		ddd.setCarNumber("辽AS3B50");
//		ddd.setCarOwer("luo");
//		appInsuredQuoteService.lastInsuredRenewaByCar(ddd);
//		System.out.println("ok");
	}
	
	@Test
	public void queryOtherPersonInfo(){
//		LastInsuredRenewaByCarModel ddd=new LastInsuredRenewaByCarModel();
		
//		CommonModel commonModel = appInsuredQuoteService.queryOtherPersonInfo("45391");
		CommonModel commonModel = appInsuredQuoteService.queryOtherPersonInfo("33138");
		System.out.println(JsonUtils.serialize(commonModel));
	}
	
	@Test
	public void queryinsbPermissionallotService(){
		insbPermissionallotService.getPermissionsetByAgentId("1186f54053ed11e5d12d53043cee09e7");
	}
	@Test
	public void getproviderListBeansByAgengid(){
		List<SelectProviderBean> selectProviderBeans = new ArrayList<>();
		//to-do
		SelectProviderBean bean = new SelectProviderBean();
		bean.setAgreementid("b96a55a001de11e69ccd232f22485b43");
		bean.setProvid("20263717");
		bean.setAgreementname("菏泽安盛天平");
		bean.setParentid("2026");
		selectProviderBeans.add(bean);
		bean = new SelectProviderBean();
		bean.setAgreementid("290b4cc001df11e69ccd232f22485b43");
		bean.setProvid("20053717");
		bean.setAgreementname("菏泽人保");
		bean.setParentid("2005");
		selectProviderBeans.add(bean);
		bean = new SelectProviderBean();
		bean.setAgreementid("05987da06ff111e6ed9c162a48190daf");
		bean.setProvid("20583717");
		bean.setAgreementname("菏泽安诚");
		bean.setParentid("2058");
		selectProviderBeans.add(bean);
//		appInsuredQuoteService.getproviderListBeansByAgengid(selectProviderBeans, "1222", "1237191285");
	}
	
}
