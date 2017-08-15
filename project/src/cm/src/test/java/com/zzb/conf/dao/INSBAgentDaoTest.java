package com.zzb.conf.dao;

import java.util.Date;

import javax.annotation.Resource;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.cninsure.core.utils.UUIDUtils;
import com.zzb.conf.entity.INSBAgent;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration(value = "src/main/webapp")
@ContextConfiguration(locations = { "classpath:config/spring-config.xml",
		"classpath:config/spring-security-config.xml",
		"classpath:config/spring-mvc-config.xml",
		"classpath:config/spring-config-db.xml", })
public class INSBAgentDaoTest {
	@Resource
	public INSBAgentDao dao;
	
	public void testAdd(){
		INSBAgent model = new INSBAgent();
		model.setId(UUIDUtils.random());
		model.setJobnum("620509219");
		model.setAgentcode("620509219");
		model.setName("李华伟");
		model.setIdno("44072119671021031X");
		model.setPhone("13702202563");
		model.setMobile("13702202563");
		model.setDeptid("1244000000");
		model.setCreatetime(new Date());
		model.setOpenid("admin");
		model.setAgentstatus(2);
		model.setIdnotype("0");
		
		dao.insert(model);
	}

}