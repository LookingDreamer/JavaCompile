package com.zzb.conf.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

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
public class INSBBusinessmanagegroupServiceTest {
	@Resource
	private INSBBusinessmanagegroupService service;

	@Test
	public void testQueryUsetListByGroupId() {

		// 1399100000、1300000000、1113000000
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("limit", 10);
		map.put("orderflag", 0);
		map.put("offset", 0);
		map.put("order", "asc");

//		Map<String, Object> result = service.queryUsetListByGroupId("2f89ee214ce74418c84f110f0cf2d869", map,"","","1", "1");
//		System.out.println("==result==" + result);
	}

	@Test
	public void testGetGroupMemberList() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("limit", 10);
		map.put("orderflag", 0);
		map.put("offset", 0);
		map.put("order", "asc");
		map.put("groupid", "38d9c5d0544911e5ddccc1607aab3445");
		Map<String, Object> mapResult = service.getGroupMemberList(map);
		System.out.println("===" + mapResult);
	}
	
	@Test
	public void testUpdatGroupMemberPrivilege(){
		String id="99a398c0133811e54d765b28db89d006";
		String groupprivilege="1";
		
		service.updatGroupMemberPrivilege(id, groupprivilege);
	}
	
	@Test
	public void testGetMembersByMember(){
		INSCUser member = new INSCUser();
		member.setUsercode("zzww");
		
		List<Map<String,String>> list = service.getMembersByMember(member);
		System.out.println(list);
	}
	
}