package com.zzb.warranty.dao;

import com.cninsure.core.utils.UUIDUtils;
import com.zzb.warranty.model.CouponTimes;
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
public class UserCouponTimesDaoTest {
    @Resource
    UserCouponTimesDao userCouponTimesDao;

    @Test
    public void testInsertCouponTimes() {
        CouponTimes couponTimes = new CouponTimes();
        couponTimes.setId(UUIDUtils.create());
        couponTimes.setUserCode("11112");
        couponTimes.setCouponTimes(4);
        couponTimes.setCreateTime(new Date());
        couponTimes.setUpdateTime(new Date());
        couponTimes.setOperatorId("1111");
        couponTimes.setOperatorName("test name");
        userCouponTimesDao.insertCouponTimes(couponTimes);

        couponTimes = userCouponTimesDao.getCouponTimes("11112");
        Assert.assertNotNull(couponTimes);
    }

    @Test
    public void testUpdateCouponTimes() {
        CouponTimes couponTimes = userCouponTimesDao.getCouponTimes("11112");
        couponTimes.setCouponTimes(1);
        userCouponTimesDao.updateCouponTimes(couponTimes);
    }

}
