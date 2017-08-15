package com.zzb.warranty.service.impl;

import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.UUIDUtils;
import com.common.JsonUtil;
import com.common.redis.CMRedisClient;
import com.zzb.conf.entity.INSBAgent;
import com.zzb.warranty.dao.CouponConfigDao;
import com.zzb.warranty.dao.CouponDao;
import com.zzb.warranty.dao.UserCouponDao;
import com.zzb.warranty.dao.UserCouponTimesDao;
import com.zzb.warranty.model.*;
import com.zzb.warranty.service.CouponDrawService;
import com.zzb.warranty.service.UserCouponService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2017/2/8.
 */
@Service
public class CouponDrawServiceImpl implements CouponDrawService {
    private static final String MODULE = "com:zzb:warranty:";
    private static final int TTL = 50 * 24 * 60 * 3600; //50天

    @Resource
    private CouponDao couponMapper;

    @Resource
    private UserCouponDao userCouponMapper;

    @Resource
    private UserCouponTimesDao userCouponTimesMapper;

    @Resource
    private CouponConfigDao couponConfigMapper;

    @Resource
    private CMRedisClient redisClient;

    @Resource(name = "asyncUserCouponServiceImpl")
    private UserCouponService asyncUserCouponService;

    @Resource(name = "asyncCouponTimesServiceImpl")
    private CouponTimesService asyncCouponTimesService;


    public boolean isStarted() {
        String value = (String) redisClient.get(MODULE, "coupon_is_started");
        return Boolean.parseBoolean(value);
    }

    private void setStarted(Boolean bool) {
        redisClient.set(MODULE, "coupon_is_started", bool.toString(), TTL);
    }

    private boolean isStarting() {
        String value = (String) redisClient.get(MODULE, "coupon_is_starting");
        return Boolean.parseBoolean(value);
    }

    private void setStarting(Boolean value) {
        redisClient.set(MODULE, "coupon_is_starting", value.toString(), TTL);
    }

    private void setUserTotalChances(Integer integer) {
        redisClient.set(MODULE, "user_chances", integer.toString(), TTL);
    }

    private Integer getUserTotalChances() {
        String chances = (String) redisClient.get(MODULE, "user_chances");
        if (StringUtils.isBlank(chances)) {
            return 0;
        }
        return Integer.parseInt(chances);
    }

    @Override
    public boolean isStoped() {
        return false;
    }

    @Override
    public void addCouponsToPool() {

    }

    // 强制初始化
    @Override
    public synchronized void start() {
        initCouponPool();
    }

    // 强制停止
    @Override
    public void stop() {

    }


    private void initCouponPool() {
        LogUtil.info("[抽奖活动]开始初始化奖池.....");
        setStarting(true);
        clearCouponCounter();
        List<Coupon> couponList = couponMapper.getCoupons();

        List<Coupon> newCouponList = new ArrayList<>();

        for (Coupon coupon : couponList) {
            for (int i = 1; i < coupon.getCouponCount() + 1; i++) {
                Coupon cou = new Coupon();
                cou.setEffectiveTime(coupon.getEffectiveTime());
                cou.setCreateTime(coupon.getCreateTime());
                cou.setExpireTime(coupon.getExpireTime());
                cou.setAmount(coupon.getAmount());
                cou.setDescription(coupon.getDescription());
                cou.setId(coupon.getId());
                newCouponList.add(cou);
            }
        }
        Random random = new Random();
        while (newCouponList.size() > 0) {
            int rand = random.nextInt(newCouponList.size());
            Coupon coupon = newCouponList.get(rand);
            redisClient.set(MODULE, String.valueOf(couponIncr()), JsonUtil.serialize(coupon), TTL);
            newCouponList.remove(rand);
        }

        CouponConfig couponConfig = couponConfigMapper.get("test");
        // 设置开始日期、结束日期
        setCouponStartTime(couponConfig.getStartTime());
        LogUtil.info("[抽奖活动]开始时间：" + couponConfig.getStartTime());
        setCouponEndTime(couponConfig.getEndTime());
        // 用户总共抽奖机会数
        LogUtil.info("[抽奖活动]结束时间：" + couponConfig.getEndTime());
        setUserTotalChances(couponConfig.getDefaultCouponTimes());

        clearConnCounter();
        setStarted(true);
        LogUtil.info("[抽奖活动]完成奖池初始化。");
    }

    private long getCouponStartTime() {
        return Long.parseLong((String) redisClient.get(MODULE, "couponStartTime"));
    }

    private void setCouponStartTime(Date date) {
        redisClient.set(MODULE, "couponStartTime", String.valueOf(date.getTime()), TTL);
    }

    private Date getCouponEndTime() {
        String s = (String) redisClient.get(MODULE, "couponEndTime");
        if (StringUtils.isBlank(s)) {
            return null;
        }
        return new Date(Long.parseLong(s));
    }

    private void setCouponEndTime(Date date) {
        redisClient.set(MODULE, "couponEndTime", String.valueOf(date.getTime()), TTL);
    }

    private boolean isEnded() {
        Date now = new Date();
        Date endDate = getCouponEndTime();
        if (endDate == null) {
            return false;
        }
        return getConnCount() >= getCouponTotal() || now.after(getCouponEndTime());
    }

    private void destroyCouponPool() {

    }

//    public Integer getUsedChances(String userCode) {
//        String str = (String) redisClient.get(MODULE, "used_chances:" + userCode);
//        if (StringUtils.isBlank(str)) {
//            return 0;
//        }
//        return Integer.parseInt(str);
//    }
//
//    public Integer incrUsedChances(String userCode) {
//        Integer result = redisClient.atomicIncr(MODULE, "used_chances:" + userCode);
//
//        return result;
//    }

    @Override
    public Integer getCouponChancesLeft(String userId) {

        CouponTimes couponTimes = userCouponTimesMapper.getCouponTimes(userId);

        return getCouponChancesLeft(couponTimes);
    }

    public Integer getCouponChancesLeft(CouponTimes couponTimes) {
        Integer totalChances = getUserTotalChances();
        if (couponTimes == null) {
            return totalChances;
        }

        return totalChances - couponTimes.getCouponTimes();
    }

    @Override
    public UserCoupon drawACoupon(INSBAgent agent) {
        // 限制非正式用户
        if (agent.getAgentkind() != 2) {
            throw new RuntimeException("非正式用户不能参与抽奖");
        }


        // 只有当已经启动，并且是抽奖时间，才可以抽奖
        if (!isStarted() && !isStarting()) {
            initCouponPool();
            throw new RuntimeException("抽奖正在初始化");
        }

        // 还未到抽奖日期
        long now = System.currentTimeMillis();
        if (now < getCouponStartTime()) {
            throw new RuntimeException("抽奖尚未开始");
        }

        // 奖券已拍完或者已经到了结束日期，抽奖已经结束
        if (isEnded()) {
            throw new RuntimeException("抽奖已经结束");
        }

        //判断用户是否还有抽奖机会
        CouponTimes couponTimes;
        LogUtil.debug("用户进入抽奖.userCode:" + agent.getJobnum());
        couponTimes = userCouponTimesMapper.getCouponTimes(agent.getJobnum());
        Integer chances = getCouponChancesLeft(couponTimes);
        if (chances < 1) {
            LogUtil.debug("用户抽奖机会已用完.userCode: " + agent.getJobnum());
            throw new RuntimeException("用户抽奖机会已经用完");
        }

        long counter = connIncr();
        LogUtil.info("当前连接的计数：" + counter);

        //获取coupon
        String json = (String) redisClient.get(MODULE, String.valueOf(counter));
        System.out.println(json);

        if (StringUtils.isBlank(json)) {
            throw new RuntimeException("奖品已抽完");
        }
        Coupon coupon = JsonUtil.deserialize(json, Coupon.class);

        if (coupon == null) {
            throw new IllegalStateException("coupon是空的");
        }

        UserCoupon userCoupon = new UserCoupon();
        userCoupon.setCouponCode(UUIDUtils.create().toUpperCase());
        userCoupon.setAmount(coupon.getAmount());
        userCoupon.setDescription(coupon.getDescription());
        userCoupon.setEffectiveTime(coupon.getEffectiveTime());
        userCoupon.setExpireTime(coupon.getExpireTime());
        userCoupon.setUserCode(agent.getJobnum());
        userCoupon.setUserName(agent.getName());
        userCoupon.setCouponId(coupon.getId());
        userCoupon.setCreateTime(new Date());
        userCoupon.setStatus(CouponStatus.可用);
        userCoupon.setProductCode("1001");
        userCoupon.setProductName("延保");
        CMProduct cmProduct = new CMProduct();
        cmProduct.setCode("1001");
        cmProduct.setName("延保");
        cmProduct.setDescription("延保");
        userCoupon.setAvailableProduct(cmProduct);

        LogUtil.info(String.format("用户%s[%s]抽中的奖券：%s", userCoupon.getUserName(), userCoupon.getUserCode(), JsonUtil.serialize(userCoupon)));

        if (coupon.getAmount() >= -1) {
            //TODO 下一版本使用asyncService
            UserCouponExecutorService.getInstance().execute(new DatabaseWriter(userCouponMapper, userCoupon));
        }

        //用户抽奖次数增加一次
        if (couponTimes == null) {
            couponTimes = new CouponTimes();
            couponTimes.setId(UUIDUtils.create());
            couponTimes.setCreateTime(new Date());
            couponTimes.setUpdateTime(new Date());
            couponTimes.setCouponTimes(1);
            couponTimes.setUserCode(agent.getJobnum());
            asyncCouponTimesService.insertCouponTimes(couponTimes);
        } else {
            couponTimes.setUpdateTime(new Date());
            couponTimes.setCouponTimes(couponTimes.getCouponTimes() + 1);
            asyncCouponTimesService.updateCouponTimes(couponTimes);
        }
        return userCoupon;
    }

    @Override
    public UserCoupon getUserCouponByCouponCode(String couponCode) {
        return userCouponMapper.getUserCouponByCouponCode(couponCode);
    }

    @Override
    public List<UserCoupon> getUserCouponsByUserCode(String userId) {
        return userCouponMapper.getUserCoupons(userId);
    }

    @Override
    public List<UserCoupon> getUserCouponsByProductCode(String userCode, String productCode) {
        return userCouponMapper.getUserCouponsByProduct(userCode, productCode);
    }

    @Override
    public UserCoupon getUserCoupon(String couponCode, String userCode) {
        return userCouponMapper.getUserCoupon(couponCode, userCode);
    }


    /**
     * 奖券池计数器
     *
     * @return
     */
    private long couponIncr() {
        return redisClient.atomicIncr(MODULE, "coupon");
    }

    private void clearCouponCounter() {
        redisClient.del(MODULE, "coupon");
    }

    /**
     * 连接计数器
     *
     * @return
     */
    private long connIncr() {
        return redisClient.atomicIncr(MODULE, "conn");
    }

    private void clearConnCounter() {
        redisClient.del(MODULE, "conn");
    }

    private long getCouponTotal() {
        String s = (String) redisClient.get(MODULE, "coupon");
        if (StringUtils.isBlank(s)) {
            return 0L;
        }
        return Long.parseLong(s);
    }

    private long getConnCount() {
        String s = (String) redisClient.get(MODULE, "conn");
        if (s == null || s.equals("")) {
            return 0L;
        }
        return Long.parseLong(s);
    }
}
