package com.zzb.conf.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.zzb.conf.entity.INSBPaychannelmanager;
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration(value = "src/main/webapp")
@ContextConfiguration(locations = { "classpath:config/spring-config.xml",
		"classpath:config/spring-security-config.xml",
		"classpath:config/spring-mvc-config.xml",
		"classpath:config/spring-config-db.xml", })
public class INSBPaychannelmanagerDaoTest{
	
	@Resource
	private INSBPaychannelmanagerDao dao;
	
	
	@Test
	public void testinsert(){
		testInsert("201137");
		testInsert("201637");
		testInsert("202737");
		testInsert("202237");
		
		testInsert("206637");
		testInsert("202637");
		
		testInsert("200537");
		testInsert("200237");
		testInsert("207737");
		testInsert("208537");
		testInsert("204444");
		testInsert("400144");
		testInsert("202537");
		
	}	
	private void testInsert(String pv){
		INSBPaychannelmanager pm6 = new INSBPaychannelmanager();
		pm6.setProviderid(pv);
		pm6.setDeptid("1237191013");
		pm6.setPaytarget("1");
		pm6.setCollectiontype("1");
		pm6.setPaychannelid("3");
		pm6.setCreatetime(new Date());
		dao.insert(pm6);
	}
	
	@Test
	public void testDeleteByLogicId(){
		Map<String,String> param  = new HashMap<String, String>();
		param.put("deptid", "1244000083");
		param.put("providerid", "204244");
		param.put("channelid", "5");
		dao.deleteByLogicId(param);
		
	}
	
	@Test
	public void testSelectPaychannelIdByAgreementId(){
		List<String> result = dao.selectPaychannelIdByAgreementId("1237191294", "20163717");
		System.out.println(result);
	}

}