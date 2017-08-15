package com.zzb.warranty.dao;

import com.common.JsonUtil;
import com.common.redis.CMRedisClient;
import com.zzb.warranty.model.Coupon;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Created by Administrator on 2017/2/9.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration(value = "src/main/webapp")
@ContextConfiguration(locations = {"classpath:config/spring-config.xml",
        "classpath:config/spring-security-config.xml",
        "classpath:config/spring-mvc-config.xml",
        "classpath:config/spring-config-db.xml",})
public class UserCouponDaoTest {

    @Resource
    UserCouponDao userCouponDao;

    @Resource
    CMRedisClient redisClient;

    @Test
    public void testInsertUserCoupon() {
//        UserCoupon userCoupon = new UserCoupon();
//        String id = UUIDUtils.create();
//        userCoupon.setId(id);
//        userCoupon.setUserCode("111");
//        userCoupon.setCouponId("222");
//        userCoupon.setCreateTime(new Date());
//        userCoupon.setExpireTime(new Date());
//        userCoupon.setCouponCode(id);
//        userCoupon.setModifyTime(new Date());
//        userCouponDao.insertUserCoupon(userCoupon);
//
//        userCoupon = userCouponDao.getUserCouponByCouponCode(id);
//        Assert.assertNotNull(userCoupon);
//
//        userCoupon = userCouponDao.getUserCoupon(id, "111");


        Coupon coupon = new Coupon();
        coupon.setCreateTime(new Date());
        coupon.setEffectiveTime(new Date());

        redisClient.set("test", "test_key", JsonUtil.serialize(coupon));

        String json = (String) redisClient.get("test", "test_key");

        coupon = JsonUtil.deserialize(json, Coupon.class);


        System.out.println(json);

        System.out.println(coupon);


    }

    @Test
    public void testGetUserCoupon() {

    }
}
