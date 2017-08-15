package com.zzb.warranty.service;

import com.common.redis.IRedisClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Administrator on 2017/2/10.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration(value = "src/main/webapp")
@ContextConfiguration(locations = { "classpath:config/spring-config.xml",
        "classpath:config/spring-security-config.xml",
        "classpath:config/spring-mvc-config.xml",
        "classpath:config/spring-config-db.xml", })
public class CMRedisClientTest {

    @Resource
    IRedisClient redisClient;

    @Test
    public void test() {
        for (int i=0;i<10000;i++) {
            int counter  = redisClient.atomicIncr("test_coupon", "counter");

            System.out.println(counter);
        }



    }

    @Test
    public void testLrem() {
        redisClient.lpush("com:zzb:cm::", "test", "one");
        redisClient.lpush("com:zzb:cm::", "test", "two");
        redisClient.lpush("com:zzb:cm::", "test", "three");

        List<String> list = redisClient.brpop("com:zzb:cm::", "test", 30);

        System.out.println(list);

        System.out.println(list.get(0));

        List<String> all = redisClient.lrange("com:zzb:cm::", "test", 0, -1);

        System.out.println(all);

        redisClient.lrem("com:zzb:cm::", "test", 0, "two");

        long removeCount = redisClient.lrem("com:zzb:cm::", "test", 0, "xxx");

        System.out.println("removed count: " + removeCount);


        all = redisClient.lrange("com:zzb:cm::", "test", 0, -1);
        System.out.println(all);
    }

    public void testLpush() {

    }


}
