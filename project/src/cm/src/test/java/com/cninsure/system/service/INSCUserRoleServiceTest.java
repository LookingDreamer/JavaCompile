package com.cninsure.system.service;

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
public class INSCUserRoleServiceTest {
	@Resource
	private INSCUserRoleService service;

	@Test
	public void testDetUsersByRoleid() {
		Map<String, Object> p = new HashMap<String, Object>();
		p.put("limit", 20);
		p.put("orderflag", 0);
		p.put("offset", 11);
		p.put("order", "asc");
		p.put("roleId", "2");

		Map<String, Object> result = service.getUsersByRoleid(p);
		System.out.println("==result==" + result);
	}

}
