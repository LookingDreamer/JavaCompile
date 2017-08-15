package com.zzb.conf.dao;

import java.util.List;

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
public class INSBAgreementscopeDaoTest {
	
	@Resource
	private INSBAgreementscopeDao dao;
	
	/**
	 * 通过区域查找对应协议
	 * 
	 * @param deptid
	 * @return
	 */
	@Test
	public void testSelectAgreementIdByDeptId(){
		List<String>  rsult = dao.selectAgreementIdByDeptId("1212191000");
		System.out.println(rsult);
	}
	
}