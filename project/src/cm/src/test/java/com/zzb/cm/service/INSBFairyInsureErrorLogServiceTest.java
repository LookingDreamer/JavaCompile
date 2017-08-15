package com.zzb.cm.service;

import com.zzb.cm.entity.INSBFairyInsureErrorLog;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;

/**
 * author: wz
 * date: 2017/4/6.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration(value = "src/main/webapp")
@ContextConfiguration(locations = { "classpath:config/spring-config.xml",
        "classpath:config/spring-security-config.xml",
        "classpath:config/spring-mvc-config.xml",
        "classpath:config/spring-config-db.xml", })
public class INSBFairyInsureErrorLogServiceTest {

    @Resource
    INSBFairyInsureErrorLogService errorLogService;




    @Test
    public void testSelectOne() {

        INSBFairyInsureErrorLog fairyInsureErrorLog = new INSBFairyInsureErrorLog();

    }

    @Test
    public void testUdpateById() {

    }

}