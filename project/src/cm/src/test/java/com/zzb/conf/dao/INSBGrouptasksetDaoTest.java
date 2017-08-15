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

import com.zzb.conf.entity.INSBGrouptaskset;


@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration(value = "src/main/webapp")
@ContextConfiguration(locations = { "classpath:config/spring-config.xml",
		"classpath:config/spring-security-config.xml",
		"classpath:config/spring-mvc-config.xml",
		"classpath:config/spring-config-db.xml", })
public class INSBGrouptasksetDaoTest {
	
	@Resource
	private INSBGrouptasksetDao dao;
	
	@Test
	public void testSelectPageByParam(){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("limit", 10);
		map.put("orderflag", 0);
		map.put("offset", 0);
		map.put("order", "asc");
//		map.put("groupname", "测");
		map.put("groupcode", "2");
		List<INSBGrouptaskset> list = dao.selectPageByParam(map);
		for(INSBGrouptaskset model:list){
			System.out.println(model);
		}
	}

}