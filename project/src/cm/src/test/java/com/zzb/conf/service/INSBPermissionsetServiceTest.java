package com.zzb.conf.service;

import java.util.HashMap;
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
public class INSBPermissionsetServiceTest {
	
	@Resource
	private INSBPermissionsetService service;
	
	
	@Test
	public  void testGetPermissionsetListByPage(){
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("limit", 10);
		map.put("orderflag", 0);
		map.put("offset", 0);
		map.put("order", "asc");
		Map<String,Object> result= service.getPermissionsetListByPage(map);
		System.out.println(result);
	}
	
	/**
	 * 新注册代理人 初始化权限
	 * 
	 * @param deptId  代理人所属网点
	 * @param jobNo 代理人工号（临时工号）
	 * @return 1:根据代理人网点权限初始化成功 2：未找到对应初始化功能包（初始化通用代理人权限）
	 */
	@Test
	public void testInitTestAgentPermission(){
		String deptId="1235191001";
		String jobNo="10000004";
		
		//agentid='a9fe3c6248ff30c7c99bb3de1e46d1ca'
		service.initTestAgentPermission(deptId, jobNo);
	}
	
}