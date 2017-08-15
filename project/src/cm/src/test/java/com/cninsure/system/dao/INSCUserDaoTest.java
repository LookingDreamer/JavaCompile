package com.cninsure.system.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.cninsure.system.entity.INSCUser;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration(value = "src/main/webapp")
@ContextConfiguration(locations = { "classpath:config/spring-config.xml",
		"classpath:config/spring-security-config.xml",
		"classpath:config/spring-mvc-config.xml",
		"classpath:config/spring-config-db.xml", })
public class INSCUserDaoTest {
	@Resource
	private INSCUserDao dao;
	
	@Test
	public void testSelectCountUsersUseLike(){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("limit", 10);
		map.put("orderflag", 0);
		map.put("offset", 0);
		map.put("order", "asc");
		map.put("deptid", "1211000000".substring(0, 4));
		long result  = dao.selectCountUsersUseLike(map);
		System.out.println(result);
		System.out.println("1211000000".substring(0, 4));
	}
	
	@Test
	public void testSelectUsersUseLike(){
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("limit", 10);
		map.put("orderflag", 0);
		map.put("offset", 0);
		map.put("order", "asc");
		map.put("deptid", "1211");
		
		List<Map<String,Object>> result = dao.selectUsersUseLike(map);
		System.out.println(result);
	}
	
	

	/**
	 * 更新用户信息
	 */
	@Test
	public void testUpdateById(){
		INSCUser user = new INSCUser();
		user.setId("00435b104c5a11e57bb874ff063593e2");
		user.setName("test");
		user.setUsername("CS001");
		user.setUsercode("cs001");
		
		dao.updateById(user);
	}
	
	
	
	@Test
	public void testSelectUsersByDeptId() {
		//{limit=10, orderflag=0, offset=0, order=asc}
		List<String> t = new ArrayList<String>();
		t.add("1111000000");
		t.add("10");
		t.add("6");
		t.add("9");
		INSCUser user = new INSCUser();
		user.setUsercode("group");
		
		Map<String,Object> p = new HashMap<String,Object>();
		p.put("limit", 10);
		p.put("orderflag", 0);
		p.put("offset", 0);
		p.put("order", "asc");
		p.put("deptIds", t);
		p.put("model",user );
		
		List<INSCUser> userList = dao.selectUsersByDeptId(p);
		for(INSCUser model:userList){
			System.out.println("===="+model);
		}
	}
	
	@Test
	public void testSelectUsersCountByDeptId() {
		List<String> t = new ArrayList<String>();
		t.add("1111000000");
		t.add("10");
		t.add("6");
		t.add("9");
		
		
		Map<String,Object> p = new HashMap<String,Object>();
		p.put("limit", 10);
		p.put("orderflag", 0);
		p.put("offset", 0);
		p.put("order", "asc");
		p.put("deptIds", t);
		
		long count = dao.selectUsersCountByDeptId(p);
		Assert.assertEquals(5, count);
	}

	@Test
	public void testUpdateGroupIdById(){
		INSCUser user = new INSCUser();
		user.setId("a4f70420120011e53bf4f229fc53b5d9");
		user.setGroupid("1234567689");
		dao.updateGroupIdById(user);
	}
}
