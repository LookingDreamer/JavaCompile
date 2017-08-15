package com.zzb.conf.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.zzb.conf.entity.INSBAutoconfigshow;


@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration(value = "src/main/webapp")
@ContextConfiguration(locations = { "classpath:config/spring-config.xml",
		"classpath:config/spring-security-config.xml",
		"classpath:config/spring-mvc-config.xml",
		"classpath:config/spring-config-db.xml", })
public class INSBAutoconfigshowDaoTest  {
	
	@Resource
	private INSBAutoconfigshowDao dao;
	//'1244191079','1244191078','1244000001','1244000000','1200000000','p'
	
	
	
	
	@Test
	public void testSelectDataByDeptIds4New(){
		List<String> aa = new ArrayList<String>();
		aa.add("1211000000");
		aa.add("1211191001");
		aa.add("p");
		
		Map<String,Object> param = new HashMap<String, Object>();
		param.put("deptList", aa);
		param.put("providerid", "200537");
		
		
		List<INSBAutoconfigshow> model = dao.selectDataByDeptIds4New(param);
		System.out.println(model);
	}
}