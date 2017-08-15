package com.zzb.warranty.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.cninsure.core.utils.UUIDUtils;
import com.common.HttpClientUtil;
import com.zzb.conf.entity.INSBAgent;
import com.zzb.mobile.util.PayConfigMappingMgr;
import com.zzb.warranty.dao.INSEOrderDao;
import com.zzb.warranty.dao.INSEPaymentDao;
import com.zzb.warranty.exception.OrderNotFoundException;
import com.zzb.warranty.exception.PayPlatformException;
import com.zzb.warranty.model.INSEOrder;
import com.zzb.warranty.model.INSEPayment;
import com.zzb.warranty.service.PaymentService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

/**
 * Created by Administrator on 2017/1/14.
 */
@Service
public class PaymentServiceImpl extends BaseServiceImpl<INSEPayment> implements PaymentService {

    private static final String PAY_SIGN_KEY;
    private static final String PAY_URL;

    static {
        PAY_SIGN_KEY = PayConfigMappingMgr.getSignKey();
        PAY_URL = PayConfigMappingMgr.getPayUrl();
    }

    @Resource
    INSEOrderDao orderDao;
    @Resource
    INSEPaymentDao paymentDao;

    private Logger logger = Logger.getLogger(PaymentServiceImpl.class);

    @Transactional
    @Override
    public String pay(INSBAgent agent, String orderno, String paychannelid, String paytype,
                      String paysource, String redirecturl, String callbackurl) {

        INSEOrder order = orderDao.getOrderByOrderNo(orderno);

        if (order == null) {
            throw new OrderNotFoundException();
        }

        if (order.getOrderstatus() > 0) {
            throw new RuntimeException("订单已经支付");
        }

//        Double totalproductamount = order.getTotalproductamount();
//        Double promotionAmount = order.getTotalpromotionamount();
        Double paymentAmount = order.getTotalpaymentamount();

        logger.info(String.format("开始支付订单， 参数: orderno: %s, paychannelid: %s, paytype: %s, " +
                        "paysource: %s, redirecturl: %s, callbackurl:%s, amount: %s",
                orderno, paychannelid, paytype, paysource, redirecturl, callbackurl, paymentAmount));

        JSONObject params = new JSONObject(true);
        params.put("channelId", paychannelid);
        params.put("payType", paytype);
        params.put("paySource", paysource);
        params.put("amount", paymentAmount * 100);
        params.put("retUrl", redirecturl);
        params.put("notifyUrl", callbackurl.trim());
        params.put("notifyType", "POST");
        params.put("bizId", orderno);

        params.put("insOrg", "20054419");
        params.put("agentOrg", "1035405");

        logger.info("参数:" + params.toJSONString());

        String result = null;
        try {
            result = HttpClientUtil.doPostJsonString(PAY_URL, params.toJSONString());

        } catch (Exception e) {
            throw new PayPlatformException(e.getMessage(), e.getCause());
        }
        logger.info("请求支付平台响应：" + result);
        // 此工具如果不是200抛出异常， 如果是200返回结果
        JSONObject jsonResult = JSONObject.parseObject(result);

        String code = jsonResult.getString("code");
        String transactionId = jsonResult.getString("bizTransactionId");
        INSEPayment payment;
        //已支付
        if ("502".equals(code)) {

            order.setOrderstatus(1);
            orderDao.updateById(order);
            payment = paymentDao.getPaymentByTransactionId(order.getTransactionId());
            payment.setStatus(1);
            paymentDao.updateById(payment);

            throw new RuntimeException("订单已经支付");
        }

        payment = new INSEPayment();
        String paymentId = UUIDUtils.create();
        payment.setId(paymentId);
        payment.setTransactionId(transactionId);
        payment.setOrderNo(order.getOrderno());
        payment.setAmount(order.getTotalpaymentamount());
        payment.setChannelId(paychannelid);
        payment.setPayType(paytype);
        payment.setCreatetime(new Date());
        payment.setOperator(agent.getName());
        paymentDao.insert(payment);

        order.setTransactionId(transactionId);
        order.setPaymentmethod(paychannelid);
        orderDao.updateById(order);

        return result;
    }

    public INSEPayment getPaymentByTransactionId(String transactionId) {
        return paymentDao.getPaymentByTransactionId(transactionId);
    }

    @Override
    public void updatePayment(INSEPayment payment) {
        paymentDao.updateById(payment);
    }

    @Override
    protected BaseDao<INSEPayment> getBaseDao() {
        return paymentDao;
    }
}
