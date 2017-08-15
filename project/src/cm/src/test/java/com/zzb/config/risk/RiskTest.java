package com.zzb.config.risk;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.cninsure.system.entity.INSCCode;
import com.cninsure.system.service.INSCCodeService;
import com.zzb.conf.entity.INSBRisk;
import com.zzb.conf.entity.INSBRiskkind;
import com.zzb.conf.service.INSBRiskService;
import com.zzb.conf.service.INSBRiskkindService;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration(value = "src/main/webapp")
@ContextConfiguration(locations = { "classpath:config/spring-config.xml",
		"classpath:config/spring-security-config.xml",
		"classpath:config/spring-mvc-config.xml",
		"classpath:config/spring-config-db.xml",
		})
public class RiskTest{
	@Resource
	private INSBRiskService service;
	@Resource
	private INSBRiskkindService kindservice;
	@Resource
	private INSCCodeService inscCodeService;
	
//	@Test
	public void test(){
		INSBRisk insbrisk = new INSBRisk();
		service.insert(insbrisk);
	}
//	@Test
	public void testkind(){
		INSBRiskkind insbRiskkind = new INSBRiskkind();
		insbRiskkind.setKindcode("kindcode");
		insbRiskkind.setKindname("kindname");
		insbRiskkind.setKindtype("kindtype");
		kindservice.insert(insbRiskkind);
	}
	
	@Test
	public void testcode(){
		INSCCode inscCodevo = new INSCCode();
		inscCodevo.setParentcode("insuranceimage");
		List<INSCCode> list =inscCodeService.queryList(inscCodevo);
		System.out.println(list);
	}
}
