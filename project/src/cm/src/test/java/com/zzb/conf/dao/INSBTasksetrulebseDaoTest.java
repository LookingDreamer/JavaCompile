package com.zzb.conf.dao;

import java.util.HashMap;
import java.util.List;
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
public class INSBTasksetrulebseDaoTest{

	@Resource
	private INSBTasksetrulebseDao dao;
	
	@Test
	public void testSelectListPage4Taskset(){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("limit", 10);
		map.put("orderflag", 0);
		map.put("offset", 0);
		map.put("order", "asc");
		map.put("ruleName", "7");
		
		
		
		List<String> list = dao.selectListPage4Taskset(map);
		System.out.println(list);
	}
}