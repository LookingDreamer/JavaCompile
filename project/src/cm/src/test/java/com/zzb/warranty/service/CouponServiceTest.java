package com.zzb.warranty.service;

import com.cninsure.core.utils.UUIDUtils;
import com.zzb.warranty.model.Coupon;
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
public class CouponServiceTest {
    @Resource
    CouponService couponService;

    @Test
    public void testInsertCoupon() {
        Coupon coupon = new Coupon();
        coupon.setId(UUIDUtils.create());
        coupon.setAmount(20d);
        coupon.setDescription("代金券20元");
        coupon.setCreateTime(new Date());
        coupon.setModifyTime(new Date());
        couponService.insertCoupon(coupon);

        coupon = new Coupon();
        coupon.setId(UUIDUtils.create());
        coupon.setAmount(50d);
        coupon.setDescription("代金券50元");
        coupon.setCreateTime(new Date());
        coupon.setModifyTime(new Date());
        couponService.insertCoupon(coupon);
        coupon = new Coupon();
        coupon.setId(UUIDUtils.create());
        coupon.setAmount(100d);
        coupon.setDescription("代金券100元");
        coupon.setCreateTime(new Date());
        coupon.setModifyTime(new Date());
        couponService.insertCoupon(coupon);
        coupon = new Coupon();
        coupon.setId(UUIDUtils.create());
        coupon.setAmount(-1d);
        coupon.setDescription("免费延保");
        coupon.setCreateTime(new Date());
        coupon.setModifyTime(new Date());
        couponService.insertCoupon(coupon);
        coupon = new Coupon();
        coupon.setId(UUIDUtils.create());
        coupon.setAmount(-2d);
        coupon.setDescription("谢谢参与");
        coupon.setCreateTime(new Date());
        coupon.setModifyTime(new Date());
        couponService.insertCoupon(coupon);
    }
}
