package com.zzb.warranty.controller;

import com.sys.games.redPaper.util.MD5Util;
import com.zzb.conf.entity.INSBAgent;
import com.zzb.warranty.model.CouponStatus;
import com.zzb.warranty.model.Response;
import com.zzb.warranty.model.UserCoupon;
import com.zzb.warranty.service.CouponDrawService;
import org.apache.commons.lang.StringUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/2/7.
 */
@Controller
@RequestMapping(value = "warranty/coupon/*")
public class LotteryController extends BaseController {

    @Resource
    CouponDrawService couponDrawService;

    public static final String PASSWORD = "3e0dd65c092645445771436b7c2dbefb";

    @RequestMapping(value = "drawacoupon", method = RequestMethod.GET)
    @ResponseBody
    public Response drawACoupon(HttpServletRequest request) {
        INSBAgent agent = getCurrentAgent(request);
        UserCoupon userCoupon = couponDrawService.drawACoupon(agent);
        return new Response(Response.STATUS_SUCCESS, Response.MESSAGE_SUCCESS, userCoupon);
    }

    @RequestMapping(value = "getcoupontimesleft")
    @ResponseBody
    public Response getCouponTimesLeft(HttpServletRequest request) {
        INSBAgent agent = getCurrentAgent(request);
        int chances = couponDrawService.getCouponChancesLeft(agent.getJobnum());
        Map<String, Object> result = new HashMap<>();
        result.put("timesleft", chances);
        return new Response(Response.STATUS_SUCCESS, Response.MESSAGE_SUCCESS, result);
    }

    @RequestMapping(value = "usercoupons", method = RequestMethod.GET)
    @ResponseBody
    public Response getUserCoupons(HttpServletRequest request,
                                   @RequestParam(value = "productcode", required = false) String productCode) {
        INSBAgent agent = getCurrentAgent(request);
        List<UserCoupon> userCoupons;
        if (StringUtils.isBlank(productCode)) {
            userCoupons = couponDrawService.getUserCouponsByUserCode(agent.getJobnum());
        } else {
            userCoupons = couponDrawService.getUserCouponsByProductCode(agent.getJobnum(), productCode);
        }
        Date now = new Date();
        for (UserCoupon userCoupon : userCoupons) {
            if (userCoupon.isUsed()) {
                userCoupon.setStatus(CouponStatus.已用);
                continue;
            }

            if (now.before(userCoupon.getExpireTime())) {
                userCoupon.setStatus(CouponStatus.可用);
                continue;
            }

            userCoupon.setStatus(CouponStatus.过期);
        }
        return new Response(Response.STATUS_SUCCESS, Response.MESSAGE_SUCCESS, userCoupons);
    }

    @RequestMapping(value = "start", method = RequestMethod.GET)
    @ResponseBody
    @Async
    public Response start(@RequestParam String password) {
        // 验证

        if (!PASSWORD.equals(MD5Util.MD5Encode(password, "utf-8"))) {
            return new Response(Response.STATUS_FAIL, "密码错误");
        }

        couponDrawService.start();
        return new Response(Response.STATUS_SUCCESS, Response.MESSAGE_SUCCESS);
    }

    @RequestMapping(value = "stop", method = RequestMethod.GET)
    @ResponseBody
    public Response stop() {
        couponDrawService.stop();
        return new Response(Response.STATUS_SUCCESS, Response.MESSAGE_SUCCESS);
    }
}
