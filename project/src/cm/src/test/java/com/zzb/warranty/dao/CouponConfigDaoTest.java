package com.zzb.warranty.dao;

import com.cninsure.core.utils.UUIDUtils;
import com.zzb.warranty.model.CouponConfig;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Created by Administrator on 2017/1/13.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration(value = "src/main/webapp")
@ContextConfiguration(locations = {"classpath:config/spring-config.xml",
        "classpath:config/spring-security-config.xml",
        "classpath:config/spring-mvc-config.xml",
        "classpath:config/spring-config-db.xml",})
public class CouponConfigDaoTest {
    @Resource
    CouponConfigDao couponConfigDao;

    @Test
    public void testInsert() {

        CouponConfig couponConfig = new CouponConfig();
        couponConfig.setId(UUIDUtils.create());
        couponConfig.setKey("test");
        couponConfig.setValue("test");

        couponConfigDao.insert(couponConfig);

        couponConfig = couponConfigDao.get("test");

        Assert.assertNotNull(couponConfig);

    }

    @Test
    public void testUpdate() {
        CouponConfig couponConfig = new CouponConfig();
        couponConfig.setKey("test");
        couponConfig.setValue("udpate");
        couponConfig.setDeadline(new Date());
        couponConfig.setDefaultCouponTimes(4);

        couponConfigDao.update(couponConfig);
        couponConfig = couponConfigDao.get("test");

        Assert.assertEquals("update", couponConfig.getValue());
    }
}
