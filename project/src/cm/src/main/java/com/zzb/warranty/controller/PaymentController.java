package com.zzb.warranty.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cninsure.core.tools.util.PropertiesUtil;
import com.cninsure.core.utils.LogUtil;
import com.zzb.conf.entity.INSBAgent;
import com.zzb.warranty.form.PayForm;
import com.zzb.warranty.model.INSEOrder;
import com.zzb.warranty.model.INSEPayment;
import com.zzb.warranty.model.Response;
import com.zzb.warranty.service.INSEOrderService;
import com.zzb.warranty.service.PaymentService;
import com.zzb.warranty.service.UserCouponService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Date;

/**
 * Created by Administrator on 2017/1/10.
 */
@Controller
@RequestMapping("warranty/payment/*")
public class PaymentController extends BaseController {

    private static final String json = "[\n" +
            "\t{\n" +
            "\t\t\"channelId\": \"baofu\",\n" +
            "\t\t\"channelName\": \"\u5b9d\u4ed8\u5fae\u4fe1\u652f\u4ed8\",\n" +
            "\t\t\"payType\": \"wqrcode\",\n" +
            "\t\t\"channelDescription\": \"\u5b9d\u4ed8\u5fae\u4fe1\u652f\u4ed8\",\n" +
            "\t\t\"icon\": \"http://icon.com\"\n" +
            "\t},\n" +
            "\t{\n" +
            "\t\t\"channelId\": \"baofu\",\n" +
            "\t\t\"channelName\": \"\u5b9d\u4ed8\u652f\u4ed8\u5b9d\u652f\u4ed8\",\n" +
            "\t\t\"payType\": \"aqrcode\",\n" +
            "\t\t\"channelDescription\": \"\u5b9d\u4ed8\u652f\u4ed8\u5b9d\u652f\u4ed8\",\n" +
            "\t\t\"icon\": \"http://icon.com\"\n" +
            "\t}\n" +
            "]";

    Logger logger = Logger.getLogger(PaymentController.class);

    @Resource
    PaymentService paymentService;

    @Resource
    INSEOrderService orderService;

    @Resource(name = "userCouponServiceImpl")
    UserCouponService userCouponService;

    @RequestMapping(value = "/paymentmethods", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public Response getPayChannels() {

        return new Response(Response.STATUS_SUCCESS, Response.MESSAGE_SUCCESS, JSONObject.parse(json));
    }

    @RequestMapping(value = "/pay", method = {RequestMethod.POST})
    @ResponseBody
    public Response pay(@RequestBody @Valid PayForm payForm, HttpServletRequest request) {
        LogUtil.info("[延保]开始支付订单，参数：%s", payForm);
        String callbackUrl = PropertiesUtil.getPropString("com.zzb.warranty.callbackurl");
        INSBAgent insbAgent = getCurrentAgent(request);

        INSEOrder order = orderService.getOrderByOrderNo(payForm.getOrderNo());

        if (order == null) {
            throw new RuntimeException("订单不存在");
        }


        if (order.getOrderstatus() >= 1) {
            return new Response(Response.STATUS_FAIL, "订单已支付");
        }

        Double paymentAmount = order.getTotalpaymentamount();
        if (paymentAmount == null) {
            paymentAmount = order.getTotalproductamount() - order.getTotalpromotionamount();
        }

//        // 在此给订单添加奖券??????????????
//        List<String> coupons = payForm.getCoupons();
//        if (coupons != null && coupons.size() > 0) {
//            orderService.updateOrder(insbAgent.getJobnum(), payForm.getOrderNo(), coupons);
//            INSEOrder updatedOrder = orderService.getOrderByOrderNo(payForm.getOrderNo());
//            paymentAmount = updatedOrder.getTotalpaymentamount();
//            if (paymentAmount == null) {
//                paymentAmount = order.getTotalproductamount() - order.getTotalpromotionamount();
//            }
//        }


        if (paymentAmount > 0) {
            String payResult = paymentService.pay(insbAgent, payForm.getOrderNo(), payForm.getChannelId(),
                    payForm.getPayType(), payForm.getPaySource(), payForm.getRedirectUrl(), callbackUrl);

            JSONObject jsonObject = JSON.parseObject(payResult);

            JSONObject jsonObject2 = new JSONObject();
            jsonObject2.put("orderNo", jsonObject.getString("bizId"));
            jsonObject2.put("transactionId", jsonObject.getString("bizTransactionId"));
            jsonObject2.put("channelId", jsonObject.getString("channelId"));
            jsonObject2.put("payType", jsonObject.getString("payType"));
            jsonObject2.put("amount", jsonObject.getInteger("amount"));
            jsonObject2.put("redirectUrl", jsonObject.getString("retUrl"));
            jsonObject2.put("payUrl", jsonObject.getString("payUrl"));

            LogUtil.info("[延保]支付返回：%s", jsonObject2);
            return new Response(Response.STATUS_SUCCESS, Response.MESSAGE_SUCCESS, jsonObject2);
        }
        orderService.updateOrderStatus(payForm.getOrderNo(), 1);
        JSONObject jsonObject3 = new JSONObject();
        jsonObject3.put("orderNo", payForm.getOrderNo());
        jsonObject3.put("payUrl", "0");
        return new Response(Response.STATUS_SUCCESS, Response.MESSAGE_SUCCESS, jsonObject3);
    }

    //TODO  需要做签名验证
    @RequestMapping(value = "/callback", method = {RequestMethod.POST})
    @ResponseBody
    public Response callback(@RequestBody String json) {

        logger.info("[延保]支付平台回调: " + json);

        JSONObject jsonObject = JSONObject.parseObject(json);

        String transactionId = jsonObject.getString("bizTransactionId");

        INSEPayment payment = paymentService.getPaymentByTransactionId(transactionId);
        if (payment == null) {
            LogUtil.info("支付流水不存在");
        }

        if (payment != null) {
            Date now = new Date();
            payment.setStatus(1);
//            payment.setPaymentTime();
            payment.setModifytime(now);
            paymentService.updatePayment(payment);

            INSEOrder order = orderService.getOrderByOrderNo(payment.getOrderNo());
            order.setOrderstatus(1);
            order.setModifytime(now);
            orderService.updateById(order);
        }


        return new Response(Response.STATUS_SUCCESS, Response.MESSAGE_SUCCESS);
    }

}
