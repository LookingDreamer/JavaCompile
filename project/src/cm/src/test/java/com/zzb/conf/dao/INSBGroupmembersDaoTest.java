package com.zzb.conf.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
public class INSBGroupmembersDaoTest {
	
	@Resource
	private INSBGroupmembersDao dao;
	
	@Resource
	private INSBGroupprovideDao aa;
	
	@Test
	public void TestSelectGroupIdsByUserCodes4Login(){
		List<String> usercodes = new ArrayList<String>();
		usercodes.add("zzww");
		List<String> re = dao.selectGroupIdsByUserCodes4Login(usercodes);
		System.out.println(re);
	}
	
	@Test
	public void TestselectGroupIdsByUserCodes4Task(){
		List<String> usercodes = new ArrayList<String>();
		usercodes.add("xiaoliu");
		List<String> result = dao.selectGroupIdsByUserCodes4Task(usercodes);
		System.out.println(result);
	}
	
	@Test
	public void Test1(){
		List<String> tempWorkedOperator = new ArrayList<String>();
		tempWorkedOperator.add("zzww");
		tempWorkedOperator.add("wudx_sd");
		tempWorkedOperator.add(null);
		tempWorkedOperator.add(null);
		
		List<String> t1 = new ArrayList<String>();
		t1.add("zzww");
//		tempWorkedOperator.removeAll(t1);
		tempWorkedOperator.remove(null);
		System.out.println(tempWorkedOperator.get(3));
//		System.out.println("==="+tempWorkedOperator);
	}
	public static void main(String[] args) {
		List<String> tempWorkedOperator = new ArrayList<String>();
		tempWorkedOperator.add("zzww");
		tempWorkedOperator.add("wudx_sd");
		tempWorkedOperator.add(null);
		tempWorkedOperator.add(null);
		
		List<String> t1 = new ArrayList<String>();
		t1.add("zzww");
//		tempWorkedOperator.removeAll(t1);
//		tempWorkedOperator.remove(null);
		System.out.println(tempWorkedOperator.get(2));
//		System.out.println("==="+tempWorkedOperator);
	}
	@Test
	public void TestSelecUserCodeByGroupIds4Task(){
		
		System.out.println(aa.selectGroupIdByProvideid("100611"));
		
//		List<String> groupIds = new ArrayList<String>();
//		List<String> refuseUsers = new ArrayList<String>();
//		
//		Map<String,Object> getUserCodes = new HashMap<String,Object>();
//		getUserCodes.put("groupIds", groupIds);
//		getUserCodes.put("refuseUsers", refuseUsers);
//		dao.selecUserCodeByGroupIds4Task(getUserCodes);
	}
	/**
	 * 通过逻辑主键得到权限信息
	 * 
	 * @param map
	 * @return
	 */
	@Test
	public void  testSelectPrivilegeByGroupIdAndUserId(){
		Map<String,String> map = new HashMap<String,String>();
		map.put("userid", "00d550d07ead11e5e8ddd80412f383c3");
		map.put("groupid", "2f89ee214ce74418c84f110f0cf2d869");
		
		Map<String,String> result = dao.selectPrivilegeByGroupIdAndUserId(map);
		System.out.println(result);
	};
}