package com.zzb.conf.dao;

import javax.annotation.Resource;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.zzb.conf.entity.INSBTaskprivilege;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration(value = "src/main/webapp")
@ContextConfiguration(locations = { "classpath:config/spring-config.xml",
		"classpath:config/spring-security-config.xml",
		"classpath:config/spring-mvc-config.xml",
		"classpath:config/spring-config-db.xml", })
public class INSBTaskprivilegeDaoTest  {
	
	@Resource
	private INSBTaskprivilegeDao dao;
	
	public void insertTest(){
		INSBTaskprivilege model = new INSBTaskprivilege();
		
	}

}