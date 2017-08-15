package com.zzb.chn.service;

import com.alibaba.fastjson.JSONObject;
import com.zzb.model.WorkFlow4TaskModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 * author: wz
 * date: 2017/4/1.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration(value = "src/main/webapp")
@ContextConfiguration(locations = { "classpath:config/spring-config.xml",
        "classpath:config/spring-security-config.xml",
        "classpath:config/spring-mvc-config.xml",
        "classpath:config/spring-config-db.xml", })
public class CHNChannelServiceTest {
    @Resource
    CHNChannelService chnChannelService;


    @Test
    public void callback() throws Exception {
        String json = "{\"mainInstanceId\":\"1890305\",\"subInstanceId\":\"1890306\",\"providerId\":\"20194419\"," +
                "\"taskName\":\"承保政策限制\",\"taskCode\":\"51\",\"taskStatus\":\"Reserved\",\"dataFlag\":2}";

        WorkFlow4TaskModel model = new WorkFlow4TaskModel();
        model.setMainInstanceId("1890258");
        model.setSubInstanceId("1890259");
        model.setProviderId("20194419");
        model.setTaskName("承保政策限制");
        model.setTaskCode("51");
        model.setTaskStatus("Reserved");
        model.setDataFlag(2);

        chnChannelService.callback(model);
    }

}