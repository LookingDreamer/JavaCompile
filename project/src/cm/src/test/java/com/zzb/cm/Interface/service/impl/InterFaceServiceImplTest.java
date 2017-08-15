package com.zzb.cm.Interface.service.impl;

import com.cninsure.core.utils.LogUtil;
import com.zzb.cm.Interface.service.InterFaceService;
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
public class InterFaceServiceImplTest {

    @Resource
    InterFaceService interFaceService;


    @Test
    public void goToFairyQuote() throws Exception {
        String taskId = "123456";
        String prvId = "22222";
        String toUserId = "admin";
        String taskType = "approvedquery@qrcode";
        try {
            interFaceService.goToFairyQuote(taskId, prvId, toUserId, taskType);

        } catch (Exception e) {
            LogUtil.error("请求精灵承保查询异常: " + e.getMessage());
            e.printStackTrace();
        }
    }

}