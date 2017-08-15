package com.zzb.conf.service;

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

import com.cninsure.system.dao.INSCDeptDao;
import com.cninsure.system.entity.INSCUser;
import com.zzb.conf.entity.INSBAgent;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration(value = "src/main/webapp")
@ContextConfiguration(locations = { "classpath:config/spring-config.xml",
		"classpath:config/spring-security-config.xml",
		"classpath:config/spring-mvc-config.xml",
		"classpath:config/spring-config-db.xml", })
public class INSBAgentServiceTestHZK {
	
	@Resource
	private INSBAgentService service;
	@Resource
	private INSCDeptDao deptDao;
	
	
	@Test
	public void selectcountByAgentCodeTest(){
		int result = service.selectcountByAgentCode("610195437", "00107b5053d111e5d12d53043cee09e7");
		System.out.println(result);
	}
	
	@Test
	public void getProviderTreeListTest(){
		List<Map<String, String>> result = service.getProviderTreeList("5bfb5c248b878dc3311ea72128d1151a", "8bac2671cffb380007666bf5c3e46001");
		System.out.println(result);
	}
	
	@Test
	public void getProviders(){
//		List<Map<String, Object>> resultList = new ArrayList<Map<String,Object>>();
		
//		List<Map<String, Object>> aa = service.getProviders(resultList, "100111");
//		System.out.println(aa);
	}
	
	
	@Test
	public void saveOrUpdateAgentTest(){
		INSCUser user = new INSCUser();
		user.setUsercode("测试save");
		
		
		INSBAgent agent = new INSBAgent();
		agent.setCreatetime(new Date());
		agent.setOperator(user.getUsercode());
		agent.setDeptid("测试save");
		agent.setIdno("123456789990");
		agent.setIdnotype("1");
		
		service.saveOrUpdateAgent(user, agent);
	}
	
	@Test
	public void test(){
		String output = service.getJSONOfCarTaskListByMap(null, null);
		System.out.println(""+output);
	}
	@Test
	public void test2(){
		Map<String,String> map = new HashMap<String,String>();
		map.put("id", "1211000000");
		map.put("comtype", "5");
		List<String> output = deptDao.queryWDidsByPatId(map);
		System.out.println("##"+output);
	}
}
