package com.zzb.warranty.service.impl;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.cninsure.core.utils.UUIDUtils;
import com.zzb.warranty.dao.*;
import com.zzb.warranty.model.*;
import com.zzb.warranty.service.INSEOrderService;
import com.zzb.warranty.util.OrderUtils;
import org.apache.ibatis.session.RowBounds;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/10.
 */
@Service
public class INSEOrderServiceImpl extends BaseServiceImpl<INSEOrder> implements INSEOrderService {
    @Resource
    INSEOrderDao orderDao;

    @Resource
    INSEPersonDao personDao;

    @Resource
    INSECarDao carDao;

    @Resource
    INSEPolicyDao policyDao;

    @Resource
    UserCouponDao userCouponDao;

    Logger logger = Logger.getLogger(INSEOrderServiceImpl.class);

    @Override
    protected BaseDao<INSEOrder> getBaseDao() {
        return orderDao;
    }

    public void updateOrder(String agentCode, String orderNo, List<String> couponCodes) {

        INSEOrder order = orderDao.getOrderByOrderNo(orderNo);

        if (order == null) {
            throw new RuntimeException("订单不存在");
        }

        Double totalPromotionAmount = 0d;

        if (couponCodes != null && couponCodes.size() > 0) {

            //奖券
            for (String couponCode : couponCodes) {
                UserCoupon userCoupon = userCouponDao.getUserCoupon(couponCode, agentCode);
                //验证奖券先
                if (userCoupon == null) {
                    throw new RuntimeException("奖券不存在");
                }
                // 是否已使用
                if (userCoupon.isUsed()) {
                    throw new RuntimeException("奖券已使用");
                }

                //有效性，是否在有效期内
                Date effectiveTime = userCoupon.getEffectiveTime();
                Date expireTime = userCoupon.getExpireTime();
                Date currentTime = new Date();

                if (currentTime.after(expireTime)) {
                    throw new RuntimeException("奖券已过期");
                }
                if (!currentTime.after(effectiveTime)) {
                    throw new RuntimeException("奖券未生效");
                }
                //更新奖券状态，订单号、使用日期、已被使用
                userCouponDao.useUserCoupon(userCoupon.getCouponCode(), order.getOrderno());

                if (userCoupon.getAmount() == -1) {
                    totalPromotionAmount += 1000;
                }
                totalPromotionAmount += userCoupon.getAmount();
            }
        }

        // 计算奖券金额
        // 免费延保，最高1000元
        // TODO 计算奖券不完善
        if (totalPromotionAmount >= 1000) {
            if (order.getTotalproductamount() >= 1000) {
                totalPromotionAmount = 1000d;
            } else {
                totalPromotionAmount = order.getTotalproductamount();
            }
        }

        Double totalPaymentAmount = order.getTotalproductamount() - totalPromotionAmount;

        updateOrderPromotionAmount(orderNo, totalPromotionAmount, totalPaymentAmount);
    }


    @Override
    public void insertOrder(INSEOrder order) {
        String orderno = OrderUtils.createOrderNo(order.getAgent().getJobnum());
        order.setOrderno(orderno);

        Double totalPromotionAmount = 0d;
        //奖券
        for (UserCoupon userCoupon : order.getUserCoupons()) {
            userCoupon = userCouponDao.getUserCoupon(userCoupon.getCouponCode(), order.getAgent().getJobnum());
            //验证奖券先
            if (userCoupon == null) {
                throw new RuntimeException("奖券不存在");
            }
            // 是否已使用
            if (userCoupon.isUsed()) {
                throw new RuntimeException("奖券已使用");
            }

            //有效性，是否在有效期内
            Date effectiveTime = userCoupon.getEffectiveTime();
            Date expireTime = userCoupon.getExpireTime();
            Date currentTime = new Date();

            if (currentTime.after(expireTime)) {
                throw new RuntimeException("奖券已过期");
            }
            if (!currentTime.after(effectiveTime)) {
                throw new RuntimeException("奖券未生效");
            }
            //更新奖券状态，订单号、使用日期、已被使用
            userCouponDao.useUserCoupon(userCoupon.getCouponCode(), orderno);

            if (userCoupon.getAmount() == -1) {
                totalPromotionAmount += 1000;
            } else {
                totalPromotionAmount += userCoupon.getAmount();
            }
        }

        // 计算奖券金额
        // 免费延保，最高1000元
        // TODO 计算奖券不完善
        if (totalPromotionAmount >= 1000) {
            if (order.getTotalproductamount() >= 1000) {
                totalPromotionAmount = 1000d;
            } else {
                totalPromotionAmount = order.getTotalproductamount();
            }
        }

        Double totalPaymentAmount = order.getTotalproductamount() - totalPromotionAmount;
        if (totalPaymentAmount < 0d) {
            totalPaymentAmount = 0d;
        }
        order.setTotalpaymentamount(totalPaymentAmount);
        order.setTotalpromotionamount(totalPromotionAmount);
        if (totalPaymentAmount <= 0) {
            order.setOrderstatus(1);
        }

        //保单
        INSEPolicy policy = order.getPolicy();
        policy.setId(UUIDUtils.create());

        //车主
        INSEPerson carowner = policy.getCarowner();
        carowner.setId(UUIDUtils.create());
        carowner.setCreatetime(new Date());
        personDao.insertPerson(carowner);

        //承保人
        INSEPerson applicant = policy.getApplicant();
        applicant.setId(UUIDUtils.create());
        personDao.insertPerson(applicant);

        //被保人
        INSEPerson insured = policy.getInsured();
        insured.setId(UUIDUtils.create());
        personDao.insert(insured);

        //权益索取人
        INSEPerson legalrightclaim = policy.getLegalrightclaim();
        legalrightclaim.setId(UUIDUtils.create());
        personDao.insert(legalrightclaim);

        //车信息
        INSECar carinfo = policy.getCarinfo();
        carinfo.setId(UUIDUtils.create());
        carinfo.setOwner(carowner);
        carinfo.setOwnerid(carowner.getId());
        carDao.insert(carinfo);

        //保单
        order.setId(UUIDUtils.create());
        policy.setOrderid(order.getId());
        policyDao.insert(policy);
        //订单
        orderDao.insert(order);
    }


    @Override
    public List<Map<String, Object>> getOrderList(String agentcode, Integer orderstatus, int limit, long offset) {
        Map<String, Object> map = new HashMap<>();
        map.put("agentcode", agentcode);
        map.put("orderstatus", orderstatus);
        map.put("limit", limit);
        map.put("offset", offset);
        logger.info("获取订单列表，查询参数：" + map);
        return orderDao.getMyOrderList(map);
    }

    @Override
    public Long selectCount(String agentcode, Integer orderstatus) {
        Map<String, Object> map = new HashMap<>();
        map.put("agentcode", agentcode);
        map.put("orderstatus", orderstatus);

        return orderDao.selectCount(map);
    }

    @Override
    public List<Map<String, Object>> selectOrdersForCm(Map<String, Object> params) {
        int limit;
        try {
            limit = (int) params.get("limit");
        } catch (Exception e) {
            limit = 10;
        }
        int offset;
        try {
            offset = (int) params.get("offset");
        } catch (Exception e) {
            offset = 0;
        }
        RowBounds rowBounds = null;
        if (limit > 0 && offset >= 0) {
            rowBounds = new RowBounds(offset, limit);
        }

        return orderDao.selectOrdersForCm(params, rowBounds);

    }

    @Override
    public Long selectOrdersCountForCm(Map<String, Object> params) {
        return orderDao.selectOrdersCountForCm(params);
    }

    @Override
    public void updateOrderPromotionAmount(String orderNo, Double promotionAmount, Double paymentAmount) {
        orderDao.updateOrderPromotionAmount(orderNo, promotionAmount, paymentAmount);
    }

    @Override
    public INSEOrder getOrderByOrderNoAndAgentCode(String agentCode, String orderno) {
        return orderDao.getOrderByOrderNoAndAgentCode(orderno, agentCode);
    }

    public Map<String, Object> getOrder(String agentcode, String orderno) {
        Map<String, Object> params = new HashMap<>();
        params.put("agentcode", agentcode);
        params.put("orderno", orderno);
        return orderDao.selectOne(params);
    }

    public INSEOrder getOrderByOrderNo(String orderno) {
        return orderDao.getOrderByOrderNo(orderno);
    }

    @Override
    public void updateOrderStatus(String orderNo, int status) {
        orderDao.updateOrderStatus(orderNo, status);
    }
}
