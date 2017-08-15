package com.zzb.conf.dao;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.zzb.conf.entity.INSBBankcardconf;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration(value = "src/main/webapp")
@ContextConfiguration(locations = { "classpath:config/spring-config.xml",
		"classpath:config/spring-security-config.xml",
		"classpath:config/spring-mvc-config.xml",
		"classpath:config/spring-config-db.xml", })
public class INSBBankcardconfDaoTest {

	@Resource
	private INSBBankcardconfDao dao;

	/**
	 * 支付渠道id 查询银行卡配置信息
	 * 
	 * @param paychannelid
	 * @return
	 */
	@Test
	public void testQueryBankcardConfInfo() {
		List<INSBBankcardconf> list = dao.queryBankcardConfInfo("1");
		System.out.println(list);
	};

}