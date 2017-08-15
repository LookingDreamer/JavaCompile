package com.zzb.cm.dao.impl;

import com.zzb.cm.dao.INSBFairyInsureErrorLogDao;
import com.zzb.cm.entity.INSBFairyInsureErrorLog;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * author: wz
 * date: 2017/3/29.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration(value = "src/main/webapp")
@ContextConfiguration(locations = {"classpath:config/spring-config.xml",
        "classpath:config/spring-security-config.xml",
        "classpath:config/spring-mvc-config.xml"})
public class INSBFairyInsureErrorLogDaoImplTest {
    @Resource
    INSBFairyInsureErrorLogDao fairyInsureErrorLogDao;

    @Test
    public void insert() throws Exception {
        INSBFairyInsureErrorLog log = new INSBFairyInsureErrorLog();
        log.setTaskId("1234568888");
        log.setInsuranceCompanyId("22333");
        log.setOperator("admin");
        Date d = new Date();
        log.setCreateTime(d);
        log.setModifyTime(d);
        log.setErrorCode("11");
        log.setErrorDesc("error log");
        fairyInsureErrorLogDao.insert(log);
    }

    @Test
    public void queryPageList() throws Exception {

    }

    @Test
    public void selectCount() throws Exception {

    }


    @Test
    public void testSelectOne() {

        INSBFairyInsureErrorLog fairyInsureErrorLog = new INSBFairyInsureErrorLog();
        fairyInsureErrorLog.setTaskId("123456");
        fairyInsureErrorLog.setInsuranceCompanyId("22333");
        fairyInsureErrorLogDao.selectOne(fairyInsureErrorLog);

    }

    @Test
    public void testUpdateById() {
        INSBFairyInsureErrorLog fairyInsureErrorLog = new INSBFairyInsureErrorLog();
        fairyInsureErrorLog.setTaskId("123456");
        fairyInsureErrorLog.setInsuranceCompanyId("22333");

        INSBFairyInsureErrorLog target = null;
        target = fairyInsureErrorLogDao.selectOne(fairyInsureErrorLog);
        Assert.assertNotNull(target);
        target.setOperator("xxx");
        target.setModifyTime(new Date());
        target.setErrorCode("00");
        target.setErrorDesc("hello error");
        target.setRequestSuccess(true);

        fairyInsureErrorLogDao.updateById(target);

    }

}