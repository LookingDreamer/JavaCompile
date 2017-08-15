package com.zzb.warranty.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.system.dao.INSCDeptDao;
import com.cninsure.system.entity.INSCDept;
import com.zzb.conf.entity.INSBAgent;
import com.zzb.conf.service.INSBAgentService;
import com.zzb.warranty.exception.OrderNotFoundException;
import com.zzb.warranty.model.INSECar;
import com.zzb.warranty.model.INSEOrder;
import com.zzb.warranty.model.INSEPerson;
import com.zzb.warranty.model.INSEPolicy;
import com.zzb.warranty.service.INSECarService;
import com.zzb.warranty.service.INSEOrderService;
import com.zzb.warranty.service.INSEPersonService;
import com.zzb.warranty.service.INSEPolicyService;
import com.zzb.warranty.util.DateUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/19.
 */
@Controller
@RequestMapping(value = "business/warranty/*", method = {RequestMethod.GET})
public class CmWarrantyController extends com.cninsure.core.controller.BaseController {
    @Resource
    INSEOrderService orderService;
    @Resource
    INSBAgentService agentService;
    @Resource
    INSEPolicyService policyService;
    @Resource
    INSEPersonService personService;
    @Resource
    INSECarService carService;
    @Resource
    INSCDeptDao inscDeptDao;

    @RequestMapping(value = "orderQueryPage")
    @ResponseBody
    public ModelAndView warrantyOrderPage() {
        ModelAndView mav = new ModelAndView("cm/warranty/orderQueryPage");
        return mav;
    }

    @RequestMapping(value = "orderDetails")
    @ResponseBody
    public ModelAndView orderDetails(@RequestParam String orderNo) {
        ModelAndView mav = new ModelAndView("cm/warranty/orderDetails");
        // 代理人信息、车辆信息、车型信息、关系人信息、延保方案、支付信息、其他信息
        if (StringUtils.isBlank(orderNo)) {
            return null;
        }

        INSEOrder order = orderService.getOrderByOrderNo(orderNo);
        if (order == null) {
            throw new OrderNotFoundException();
        }
        INSBAgent agent = new INSBAgent();
        agent.setJobnum(order.getAgentcode());
        agent = agentService.getAgentInfo(agent);

        INSCDept inscDept = inscDeptDao.selectById(agent.getDeptid());


        INSEPolicy policy = policyService.selectPolicyByOrderId(order.getId());

        if (policy == null) {
            throw new RuntimeException("保单不存在，对应的订单号为：" + order.getOrderno());
        }

        INSEPerson insured = personService.getPersonById(policy.getInsuredid());
        INSEPerson applicant = personService.getPersonById(policy.getApplicantid());
        INSEPerson carownerinfo = personService.getPersonById(policy.getCarownerid());
        INSEPerson legalrightclaim = personService.getPersonById(policy.getLegalrightclaimid());
        INSECar carinfo = carService.getCarById(policy.getCarinfoid());

        mav.addObject("order", order);
        mav.addObject("policy", policy);
        mav.addObject("agent", agent);
        mav.addObject("insured", insured);
        mav.addObject("applicant", applicant);
        mav.addObject("carownerinfo", carownerinfo);
        mav.addObject("legalrightclaim", legalrightclaim);
        mav.addObject("car", carinfo);
        mav.addObject("dept", inscDept);


        return mav;
    }

    @RequestMapping(value = "queryOrders", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> queryOrders(
            HttpServletRequest request,
            @RequestParam(value = "orderNo", required = false) String orderNo,
            @RequestParam(value = "plateNumber", required = false) String plateNumber,
            @RequestParam(value = "insuredName", required = false) String insuredName,
            @RequestParam(value = "orderStatus", required = false) Integer orderStatus,
            @RequestParam(value = "payChannel", required = false) String payChannel,
            @RequestParam(value = "transactionId", required = false) String transactionId,
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endDate,
            @RequestParam(value = "offset", required = false) Integer offset,
            @RequestParam(value = "limit", required = false) Integer limit,
            @RequestParam(value = "orderBy", required = false) String orderBy) {

        LogUtil.info("参数：orderNo=%s, plateNumber=%s, insuredName=%s, orderStatus=%s, payChannel=%s, transactionId=%s",
                orderNo, plateNumber, insuredName, orderStatus, payChannel, transactionId);

        Map<String, Object> params = new HashMap<>();
        params.put("orderNo", orderNo);
        params.put("plateNumber", plateNumber);
        params.put("insuredName", insuredName);
        params.put("orderStatus", orderStatus);
        params.put("paymentChannel", payChannel);
        params.put("transactionId", transactionId);
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        params.put("limit", limit);
        params.put("offset", offset);
        params.put("orderBy", orderBy);

        long total = orderService.selectOrdersCountForCm(params);
        List<Map<String, Object>> list = orderService.selectOrdersForCm(params);
        JSONArray jsonArray = new JSONArray();
        Map<String, Object> row;

        for (Map<String, Object> map : list) {
            row = new HashMap<>();
            row.put("orderNo", map.get("order_orderno"));
            row.put("createTime", DateUtils.format((Date) map.get("order_createtime"), DateUtils.pattern1));
            row.put("carownerName", map.get("carowner_name"));
            row.put("plateNumber", map.get("car_platenumber"));
            row.put("payChannel", getPaymentMethod((String) map.get("order_paymentmethod")));
            row.put("orderStatus", getOrderStatus((int) map.get("order_orderstatus")));
            row.put("amount", map.get("order_amount"));
            row.put("other", "<a href=");
            jsonArray.add(row);
        }

        Map<String, Object> result = new JSONObject();
        result.put("total", total);
        result.put("rows", jsonArray);
        return result;
    }


    private String getOrderStatus(int statusCode) {
        switch (statusCode) {
            case 0:
                return "待支付";
            case 1:
                return "支付成功，已投保";
            case 2:
                return "承保成功";
            case 4:
                return "承保失败";
            default:
                return "未知状态";
        }
    }

    private String getPaymentMethod(String channelId) {
        if (StringUtils.isBlank(channelId)) {
            return "未知";
        }
        switch (channelId) {
            case "baofu-wqrcode":
                return "宝付微信";
            case "baofu-aqrcode":
                return "宝付支付宝";
            default:
                return "未知";
        }
    }
}
