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
public class INSBRuleEngineDaoTest {

	@Resource
	private INSBRuleEngineDao dao;
	
	@Test
	public void testSelectByParamMap(){
		Map<String,Object> map = new HashMap<String,Object>();
//		map.put("city_id", value)
		map.put("company_id", "424");
		List<Map<String,Object>> result = dao.selectByParamMap(map);
		System.out.println(result);
	}
	
}