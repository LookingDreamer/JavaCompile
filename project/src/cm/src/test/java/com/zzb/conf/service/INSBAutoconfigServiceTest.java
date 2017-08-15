package com.zzb.conf.service;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;


@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration(value = "src/main/webapp")
@ContextConfiguration(locations = { "classpath:config/spring-config.xml",
		"classpath:config/spring-security-config.xml",
		"classpath:config/spring-mvc-config.xml",
		"classpath:config/spring-config-db.xml", })
public class INSBAutoconfigServiceTest {
	@Resource
	private INSBAutoconfigService service;
	
	
	@Test
	public void testGetElfpathByAutoConfig(){
		Map<String,String> p = new HashMap<String,String>();
		p.put("deptid", "1213192001");
		p.put("providerid", "2002 ");
		p.put("quotetype", null);
		p.put("conftype", null);
		
		
		String path = service.getEpathByAutoConfig(p);
		System.out.println(path);
		
	}
}