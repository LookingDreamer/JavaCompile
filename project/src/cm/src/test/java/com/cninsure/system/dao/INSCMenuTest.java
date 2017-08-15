package com.cninsure.system.dao;

import java.util.Date;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.cninsure.system.dao.INSCMenuDao;
import com.cninsure.system.entity.INSCMenu;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration(value = "src/main/webapp")
@ContextConfiguration(locations = { "classpath:config/spring-config.xml",
		"classpath:config/spring-security-config.xml",
		"classpath:config/spring-mvc-config.xml",
		"classpath:config/spring-config-db.xml", })
public class INSCMenuTest {
	@Resource
	private INSCMenuDao dao;

	@Test
	public void testInsert() {
		INSCMenu c = new INSCMenu();
		c.setCreatetime(new Date());
		c.setOperator("1");
		c.setNoti("业务权限管理-群组管理");
		c.setNodecode("m00082");
		c.setParentnodecode("m00080");
		c.setNodelevel("2");
		c.setNodename("业务权限配置");
		c.setChildflag("0");
		c.setActiveurl("groupprivilege/menu2list");
		c.setOrderflag(2);
		c.setStatus("1");
		dao.insert(c);
		
	}

}
