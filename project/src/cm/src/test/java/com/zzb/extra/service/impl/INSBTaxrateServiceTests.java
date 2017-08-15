package com.zzb.extra.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.cninsure.core.utils.LogUtil;
import com.zzb.cm.entity.INSBQuotetotalinfo;
import com.zzb.cm.service.INSBQuotetotalinfoService;
import com.zzb.conf.dao.INSBChannelDao;
import com.zzb.extra.service.INSBTaxrateService;

/**
 * 
 * @author shiguiwu
 * @date 2017年4月19日
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration(value = "src/main/webapp")
@ContextConfiguration(locations = {"classpath:config/spring-config.xml", "classpath:config/spring-mvc-config.xml"
		, "classpath:config/spring-security-config.xml", "classpath:config/spring-config-db.xml"})
public class INSBTaxrateServiceTests {

	@Resource
	private INSBTaxrateService taxService;
	
	@Resource
	private INSBQuotetotalinfoService q;
	
	@Resource
	private INSBChannelDao channelDao;
	
	@Test
	public void testQueryPag() {
		Map<String, Object> result = taxService.getTaxrateList(new HashMap<>());
		LogUtil.info("result"+result.toString());
	}
	
	@Test
	public void testQ() {
		INSBQuotetotalinfo qq = new INSBQuotetotalinfo();
		qq.setTaskid("1485258");
		 qq= q.queryOne(qq);
		System.out.println(qq);
	}
	
	@Test
	public void testChannel() {
		List<Map<String, String>> lists = channelDao.selectSenderInfoByCityAndChannelcode("440100", "nqd_minizzb2016");
		System.out.println(lists.toString());
	}
}
