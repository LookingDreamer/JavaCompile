package com.cninsure.jobpool.timer;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 * author: wz
 * date: 2017/3/30.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration(value = "src/main/webapp")
@ContextConfiguration(locations = {"classpath:config/spring-config.xml",
        "classpath:config/spring-security-config.xml",
        "classpath:config/spring-mvc-config.xml"})
public class FairyInsureQuerySchedulerImplTest {
    @Resource
    FairyInsureQueryScheduler scheduler;

    FairyInsureQuery query;

    @Before
    public void setUp() throws Exception {
        query = new FairyInsureQuery();
        query.setTaskId("123");
        query.setProviderId("321");

    }

    @Test
    public void scheduleQuery() throws Exception {
        scheduler.scheduleQuery(query);
    }

    @Test
    public void unscheduleQuery() throws Exception {
        scheduler.unscheduleQuery(query);
    }

}