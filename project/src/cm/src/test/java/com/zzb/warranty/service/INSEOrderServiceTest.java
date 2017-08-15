package com.zzb.warranty.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2017/1/13.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration(value = "src/main/webapp")
@ContextConfiguration(locations = { "classpath:config/spring-config.xml",
        "classpath:config/spring-security-config.xml",
        "classpath:config/spring-mvc-config.xml",
        "classpath:config/spring-config-db.xml", })
public class INSEOrderServiceTest {
    @Resource
    INSEOrderService orderService;
    @Test
    public void testSelectCount() {
        long count = orderService.selectCount("123", 0);

        System.out.println("count = " + count);
    }
}
