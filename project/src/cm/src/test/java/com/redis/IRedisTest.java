package com.redis;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

import com.cninsure.cssj.JUnit4ClassRunner;
import com.common.redis.Constants;
import com.common.redis.IRedisClient;

import java.util.HashMap;
import java.util.Map;

@RunWith(JUnit4ClassRunner.class)
@WebAppConfiguration(value = "src/main/webapp")
@ContextConfiguration(locations = { "classpath:config/spring-config.xml",
		"classpath:config/spring-security-config.xml",
		"classpath:config/spring-mvc-config.xml", })
public class IRedisTest {
	@Resource
	private IRedisClient redisClient;
	@Test
	public void test() throws InterruptedException {
		Map<String, String> rdValue = new HashMap<>(2);
		rdValue.put("tfb", "测试");
		rdValue.put("oprt", "dai");
		redisClient.set(2, "taskFeedBack", "sub:123456:18:Completed", rdValue, 20);
		System.out.println(redisClient.get(2, "taskFeedBack", "sub:123456:18:Completed", Map.class));
		Thread.sleep(21000);
		System.out.println(redisClient.get(2, "taskFeedBack", "sub:123456:18:Completed", Map.class));

		/*redisClient.del(Constants.CM_GLOBAL, "1_精灵_robot");
		redisClient.del(Constants.CM_GLOBAL, "2_精灵_robot");
		
		redisClient.set(Constants.CM_GLOBAL, "1_精灵_robot", "3");
		System.out.println(redisClient.get(Constants.CM_GLOBAL, "1_精灵_robot") +"--------");
		System.out.println(redisClient.atomicIncr(Constants.CM_GLOBAL, "1_精灵_robot") +"--------");
		System.out.println(redisClient.atomicIncr(Constants.CM_GLOBAL, "1_精灵_robot") +"--------");

		redisClient.set(Constants.CM_GLOBAL, "2_精灵_robot", 3);
		System.out.println(redisClient.get(Constants.CM_GLOBAL, "2_精灵_robot") +"--------");
		System.out.println(redisClient.atomicIncr(Constants.CM_GLOBAL, "2_精灵_robot") +"--------");*/
	}
	
	
	
	
}
