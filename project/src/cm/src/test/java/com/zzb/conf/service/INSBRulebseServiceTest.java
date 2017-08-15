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
public class INSBRulebseServiceTest {

	@Resource
	INSBRulebseService service;

	@Test
	public void testGetListPageByParam() {
		
		
		//{limit=10, ruleGroup=, orderflag=0, offset=0, order=asc}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("limit", 10);
		map.put("orderflag", 0);
		map.put("offset", 0);
		map.put("order", "asc");
		map.put("ruleGroup", "");
		
		Map<String, Object> tempMap = new HashMap<String, Object>();
		tempMap.put("ruletype", 0);
		tempMap.put("param2", 0);
		tempMap.put("rulebaseStatus", 1);
		
		Map<String, Object> result = service.getListPageByParam(2, tempMap, map);
		System.out.println(result);
	};
}