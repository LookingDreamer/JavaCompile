package com.zzb.conf.dao;


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
public class INSBRiskkindDaoTest {

	@Resource
	private INSBRiskkindDao dao;
	
	/**
	 * 通过险别编码查询当前险种是否存在重复险别
	 * @param kindcode
	 * @return
	 */
	@Test
	public void testSelectCOuntByKindCode(){
		Map<String,String> map = new HashMap<String,String>();
		map.put("kindcode", "GlassIns");
		map.put("riskid", "e7b05e6090f911e5f318e3b17b905e37");
		long  result = dao.selectCOuntByKindCode(map);
		System.out.println(result);
	}

}