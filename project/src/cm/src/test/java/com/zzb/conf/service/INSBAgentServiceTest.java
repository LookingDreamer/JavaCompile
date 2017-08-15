package com.zzb.conf.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.cninsure.system.entity.INSCUser;
import com.zzb.conf.entity.INSBAgent;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration(value = "src/main/webapp")
@ContextConfiguration(locations = { "classpath:config/spring-config.xml",
		"classpath:config/spring-security-config.xml",
		"classpath:config/spring-mvc-config.xml",
		"classpath:config/spring-config-db.xml", })
public class INSBAgentServiceTest {
	
	@Resource
	private INSBAgentService service;
	
	@Test
	public void updateagent(){
		Map<String, Object> map = service.setAgentAttribute("p+1000000000+1200000000+1244000000","1244000000");
		System.out.println(map.toString());
	}
	@Test
	public void testSelectcountByAgentCode(){
		int result = service.selectcountByAgentCode("610195437", "00107b5053d111e5d12d53043cee09e7");
		System.out.println(result);
	}
	
	@Test
	public void testGetProviderTreeList(){
		List<Map<String, String>> result = service.getProviderTreeList("5bfb5c248b878dc3311ea72128d1151a", "8bac2671cffb380007666bf5c3e46001");
		System.out.println(result);
	}
	
	
	
	@Test
	public void testSaveOrUpdateAgent(){
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
}
