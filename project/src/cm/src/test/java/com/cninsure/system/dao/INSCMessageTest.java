package com.cninsure.system.dao;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.cninsure.system.entity.INSCMessage;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration(value = "src/main/webapp")
@ContextConfiguration(locations = { "classpath:config/spring-config.xml",
		"classpath:config/spring-security-config.xml",
		"classpath:config/spring-mvc-config.xml",
		"classpath:config/spring-config-db.xml", })
public class INSCMessageTest {
	@Resource
	private INSCMessageDao dao;
	@Test
	public void insertTest() {
		INSCMessage msg = new INSCMessage();
		msg.setMsgcontent("aaaaaaa");
		msg.setMsgtitle("asdasd");
		msg.setReceiver("zhangsan");
		msg.setSender("admin");
		msg.setSendtime("2015-08-09 11:00:00");
		msg.setState(1);
		
		
		dao.insert(msg);
		
	}

}
