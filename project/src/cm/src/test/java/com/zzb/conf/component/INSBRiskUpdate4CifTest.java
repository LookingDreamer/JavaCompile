package com.zzb.conf.component;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.zzb.conf.entity.INSBRisk;
import com.zzb.model.CifRiskModel;


@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration(value = "src/main/webapp")
@ContextConfiguration(locations = { "classpath:config/spring-config.xml",
		"classpath:config/spring-security-config.xml",
		"classpath:config/spring-mvc-config.xml",
		"classpath:config/spring-config-db.xml", })
public class INSBRiskUpdate4CifTest {

	@Resource
	private INSBRiskUpdate4Cif cif;
	
	@Test
	public void testAddRiskData(){
		
		CifRiskModel model = new CifRiskModel();
		model.setIds("");
		
		INSBRisk risk = new INSBRisk();
		risk.setRiskcode("2222222222222");
		risk.setRiskname("2222222222222");
		risk.setRiskshortname("22222222222");
		risk.setRisktype("2");
		risk.setProvideid("");
		
		model.setInsbrisk(risk);
		cif.addRiskData(model);
	}
}
