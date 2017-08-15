package com.zzb.conf.service;

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

import com.cninsure.system.entity.INSCUser;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration(value = "src/main/webapp")
@ContextConfiguration(locations = { "classpath:config/spring-config.xml",
		"classpath:config/spring-security-config.xml",
		"classpath:config/spring-mvc-config.xml",
		"classpath:config/spring-config-db.xml", })
public class INSBTasksetServiceTest {

	@Resource
	private INSBTasksetService service;
	
	@Test
	public void testGetRenewalUserData(){
		Map<String,String> result = service.getRenewalUserData("1212191001");
		System.out.println(result);
	}
	
	@Test
	public void testSelectOnlineUser4WorkFlow(){
		Map<String, String> param = new HashMap<String,String>();
		param.put("instanceId", "255");
		param.put("providerId", "100613");
		param.put("taskType", "3");
		
		Map<String,String> result = service.selectOnlineUser4WorkFlow(param);
		System.out.println(result);
	}
	
	@Test
	public void testRemoveTaskSetScop(){
		String deptIds = "1211000003,1233193007";
		service.removeTaskSetScop(deptIds);
	}
	
	@Test
	public void testGetScopListByTaskSetId(){
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("limit", 10);
		map.put("orderflag", 0);
		map.put("offset", 0);
		map.put("order", "asc");
		
		Map<String,Object> result = service.getScopListByTaskSetId(map,"320a31e077d011e5566ba468be40dbf5");
		System.out.println(result); 
	}
	
	@Test
	public void testSaveTaskSetScop(){
		INSCUser user = new INSCUser();
		user.setUsercode("test");
		service.saveTaskSetScop(user, "320a31e077d011e5566ba468be40dbf5", "1211192043");
	}
	
	@Test
	public void testGetProviderTreeList(){
		List<Map<String, String>> result = service.getProviderTreeList(null, null, null, null);
		System.out.println(result);
	}
	
	@Test
	public  void testInitMain2editData(){
		Map<String, Object> result = service.initMain2editData("320a31e077d011e5566ba468be40dbf5");
		System.out.println(result);
	}
	

	@Test
	public void testQueryByParamPage() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("limit", 10);
		map.put("orderflag", 0);
		map.put("offset", 0);
		map.put("order", "asc");
		String tasksetkind ="0";
		String tasksetname = null;
//		String tasksetname = "北京泛华管理组";
		String setstatus = null;
		
		List<String> list1 = new ArrayList<String>();
		list1.add("0cf226f05aa811e55088e365ab28de24");
		list1.add("91204630550f11e5459f985dffde14a6");
		map.put("listId", list1);
		

		Map<String, Object> result = service.queryByParamPage(tasksetkind,
				tasksetname, setstatus, map);
		System.out.println(result);
	}
}