package com.cninsure.system.dao;

import java.util.Date;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.cninsure.system.entity.INSCMethodCode;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration(value = "src/main/webapp")
@ContextConfiguration(locations = { "classpath:config/spring-config.xml",
		"classpath:config/spring-security-config.xml",
		"classpath:config/spring-mvc-config.xml",
		"classpath:config/spring-config-db.xml", })
public class INSCMethodCodeDaoTest{

	@Resource
	private INSCMethodCodeDao dao;
	
	
	@Test
	public void testInsert(){
		INSCMethodCode methodCodeModel  = new INSCMethodCode();
		methodCodeModel.setCreatetime(new Date());
		methodCodeModel.setMethod("endTaskFromWorkFlow");
		methodCodeModel.setClassname("INSBWorkFlowDataContorller");
		methodCodeModel.setMethodname("工作流---结点结束操作");
		
		dao.insert(methodCodeModel);
	}
	
}