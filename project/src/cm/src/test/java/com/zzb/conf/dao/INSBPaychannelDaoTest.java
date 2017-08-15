package com.zzb.conf.dao;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.zzb.conf.entity.INSBPaychannel;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration(value = "src/main/webapp")
@ContextConfiguration(locations = { "classpath:config/spring-config.xml",
		"classpath:config/spring-security-config.xml",
		"classpath:config/spring-mvc-config.xml",
		"classpath:config/spring-config-db.xml", })
public class INSBPaychannelDaoTest {

	@Resource
	private INSBPaychannelDao dao;
	
	/**
	 * 得到支付方式列表
	 */
	@Test
	public void testSelectPayChannelList(){
		
		List<Map<String, Object>> result = dao.selectPayChannelList();
		System.out.println(result);
	}
	
	/**
	 * 通过id  得到详细信息
	 */
	@Test
	public void  testSelectById(){
		INSBPaychannel model = dao.selectById("1");
		System.out.println(model);
	}
	

}