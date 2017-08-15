package com.zzb.warranty.controller;

import com.alibaba.fastjson.JSONArray;
import com.zzb.warranty.model.CouponStatus;
import com.zzb.warranty.model.UserCoupon;
import com.zzb.warranty.service.UserCouponService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/2/20.
 */
@Controller
@RequestMapping(value = "business/warranty/*")
public class CmUserCouponController extends com.cninsure.core.controller.BaseController {
    @Resource(name = "userCouponServiceImpl")
    UserCouponService userCouponService;

    @RequestMapping(value = "couponQueryPage", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView queryUserCouponsPage() {
        ModelAndView mav = new ModelAndView("cm/warranty/couponQueryPage");
        //查询usercoupon
        return mav;
    }

    @RequestMapping(value = "queryUserCoupons", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> queryUserCoupons(
            @RequestParam(required = false) String userCode,
            @RequestParam(required = false) String effectiveTime,
            @RequestParam(required = false) Date expireTime,
            @RequestParam(required = false) String orderBy,
            @RequestParam(required = false) int offset,
            @RequestParam(required = false) int limit) {

        Map<String, Object> query = new HashMap<>();
        query.put("userCode", userCode);
        query.put("effectiveTime", effectiveTime);
        query.put("expireTime", expireTime);
        query.put("orderBy", orderBy);
        query.put("offset", offset);
        query.put("limit", limit);

//        long total = userCouponService.getUserCouponCount(query);

        List<UserCoupon> userCoupons = userCouponService.getUserCouponsPageable(query);

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

        Map<String, Object> result = new HashMap<>();

        JSONArray jsonArray = new JSONArray();
        jsonArray.addAll(userCoupons);
        result.put("total", userCoupons.size());
        result.put("rows", jsonArray);

        return result;
    }

    @RequestMapping(value = "couponConfig", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView couponConfigPage() {
        ModelAndView mav = new ModelAndView("cm/warranty/couponConfig");
        return mav;
    }
}
