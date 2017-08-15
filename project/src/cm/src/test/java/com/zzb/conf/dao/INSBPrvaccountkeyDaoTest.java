package com.zzb.conf.dao;

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

import com.zzb.conf.entity.INSBPrvaccountkey;


@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration(value = "src/main/webapp")
@ContextConfiguration(locations = { "classpath:config/spring-config.xml",
		"classpath:config/spring-security-config.xml",
		"classpath:config/spring-mvc-config.xml",
		"classpath:config/spring-config-db.xml", })
public class INSBPrvaccountkeyDaoTest{
	@Resource
	private INSBPrvaccountkeyDao dao;
	
	@Test
	public void testInsert(){
		INSBPrvaccountkey model = new INSBPrvaccountkey();
		model.setCreatetime(new Date());
		model.setOperator("admin");
		model.setManagerid("361a3d50c56b11e56348d5e588cbe7da");
		model.setParamname("LoginPassword");
		model.setParamvalue("qqqq");
		model.setNoti("密码");
		dao.insert(model);
		
	}
	
	@Test
	public void  testSelectListPage(){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("limit", 10);
		map.put("managerid", 1);
		map.put("offset", 0);
		map.put("order", "asc");
		
		List<INSBPrvaccountkey> result = dao.selectListPage(map);
		System.out.println(result);
	}
	
	@Test
	public void  selectPageCountByMap(){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("limit", 10);
		map.put("managerid", 1);
		map.put("offset", 0);
		map.put("order", "asc");
		
		int result = dao.selectPageCountByMap(map);
		System.out.println(result);
		
	}
}