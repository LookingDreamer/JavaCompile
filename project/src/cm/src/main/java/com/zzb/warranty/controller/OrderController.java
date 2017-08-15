package com.zzb.warranty.controller;

import com.cninsure.core.utils.BeanUtils;
import com.cninsure.core.utils.JsonUtil;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.UUIDUtils;
import com.common.redis.IRedisClient;
import com.zzb.conf.entity.INSBAgent;
import com.zzb.warranty.exception.OrderNotFoundException;
import com.zzb.warranty.exception.QuoteNotExistException;
import com.zzb.warranty.form.OrderDetailsForm;
import com.zzb.warranty.form.OrderListForm;
import com.zzb.warranty.form.OrderStatusForm;
import com.zzb.warranty.form.PolicyDateForm;
import com.zzb.warranty.model.*;
import com.zzb.warranty.service.*;
import com.zzb.warranty.util.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by Administrator on 2017/1/6.
 */
@Controller
@RequestMapping("warranty/order/*")
public class OrderController extends BaseController {
    @Resource
    INSEOrderService orderService;
    @Resource
    INSEQuoteService quoteService;
    @Resource
    INSEPersonService personService;
    @Resource
    INSECarService carService;

    @Resource
    INSEPolicyService policyService;

    @Resource(name = "userCouponServiceImpl")
    UserCouponService userCouponService;


    @Resource
    private IRedisClient redisClient;

    Logger logger = Logger.getLogger(OrderController.class);


    @RequestMapping(value = {"/orderdetails"}, method = {RequestMethod.POST})
    @ResponseBody
    public Response orderDetails(HttpServletRequest request, @RequestBody OrderDetailsForm form) throws Exception {
        LogUtil.info("[延保]订单详情, 参数: %s", form.toString());

        INSBAgent agent = getCurrentAgent(request);

        INSEOrder order = orderService.getOrderByOrderNoAndAgentCode(agent.getAgentcode(), form.getOrderno());
        if (order == null) {
            throw new OrderNotFoundException();
        }

        INSEPolicy policy = policyService.selectPolicyByOrderId(order.getId());

        if (policy == null) {
            throw new Exception("保单不存在，对应的订单号为：" + order.getOrderno());
        }

        INSEPerson insured = personService.getPersonById(policy.getInsuredid());
        INSEPerson applicant = personService.getPersonById(policy.getApplicantid());
        INSEPerson carownerinfo = personService.getPersonById(policy.getCarownerid());
        INSEPerson legalrightclaim = personService.getPersonById(policy.getLegalrightclaimid());
        INSECar carinfo = carService.getCarById(policy.getCarinfoid());

        Map<String, Object> carinfoMap = new HashMap<>();
        if (carinfo != null) {
            carinfoMap = BeanUtils.toMap(carinfo);
            carinfoMap.put("registerdate", carinfo.getRegistdate() == null ? null : DateUtils.format(carinfo.getRegistdate(), "yyyy-MM-dd"));
            carinfoMap.put("plateNumber", carinfo.getCarlicenseno());
        }

        Map<String, Object> result = new HashMap<>();
        result.put("orderno", order.getOrderno());
        result.put("amount", order.getTotalpaymentamount());
        result.put("insured", insured);
        result.put("applicant", applicant);
        result.put("carownerinfo", carownerinfo);
        result.put("legalrightclaim", legalrightclaim);
        result.put("carinfo", carinfoMap);
        PolicyDateForm dateForm = new PolicyDateForm();
        dateForm.setStartDate(policy.getStartdate());
        dateForm.setEndDate(policy.getEnddate());
        result.put("policydate", dateForm);
        result.put("orderstatus", getOrderStatus(order.getOrderstatus()));
        result.put("extendwarrantytype", getExtendWarrantyType(policy.getExtendwarrantytype()));
        result.put("needinvoice", order.isNeedinvoice());
        result.put("invoiceemail", order.getInvoiceemail());

        return new Response(Response.STATUS_SUCCESS, Response.MESSAGE_SUCCESS, result);
    }

    private String getExtendWarrantyType(Integer extendwarrantytype) {
        if (extendwarrantytype == null) {
            return null;
        }
        switch (extendwarrantytype) {
            case 0:
                return "全面保修";
            case 1:
                return "三大部件保修";
            case 2:
                return "两大部件保修";
            default:
                return null;
        }
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public Response addOrder(@RequestBody @Valid OrderForm model, HttpServletRequest request) throws Exception {
        LogUtil.info("[延保]新增订单, 参数: %s", JsonUtil.getJsonString(model));

        INSBAgent agent = getCurrentAgent(request);
        INSEQuote quote;
        quote = quoteService.getQuote(model.getQuoteinfoid());

        if (quote == null) {
            throw new QuoteNotExistException();
        }
        INSECar carinfo = model.getCarinfo();
        INSEPerson carowner = model.getCarownerinfo();
        INSEPerson applicant = model.getApplicant();
        INSEPerson insured = model.getInsured();
        INSEPerson legalrightclaim = model.getLegalrightclaim();

        if (applicant.isSamecarowner()) {
            applicant = carowner;
        }
        if (insured.isSamecarowner()) {
            insured = carowner;
        }
        if (legalrightclaim.isSamecarowner()) {
            legalrightclaim = carowner;
        }

        if (carinfo == null) {
            carinfo = new INSECar();
            String carid = UUIDUtils.create();
            carinfo.setId(carid);
            carinfo.setOperator(agent.getName());
            carinfo.setCreatetime(new Date());
            carinfo.setVincode(quote.getVincode());
//        car.setOwner(carownerid);
            carinfo.setOwnername(carowner.getName());
            carinfo.setStandardfullname(quote.getStandardfullname());
            carinfo.setCifstandardname(quote.getStandardname());
            carinfo.setEngineno(quote.getEngineno());
            carinfo.setCarlicenseno(quote.getPlateNumber());
            carinfo.setRegistdate(quote.getRegisterdate());
            carinfo.setOrigialwarrantyperiod(quote.getOrigialwarrantyperiod());
            carinfo.setOperator(agent.getName());
            carinfo.setCreatetime(new Date());
        }

        INSEPolicy policy = new INSEPolicy();
        policy.setAgent(agent);
        policy.setAgentname(agent.getName());
        policy.setOperator(agent.getName());
        policy.setCreatetime(new Date());
        policy.setAgentnum(agent.getJobnum());
        policy.setAmount(quote.getAmount());
        policy.setCarinfoid(carinfo.getId());
        policy.setStartdate(quote.getStartdate());
        policy.setEnddate(quote.getEnddate());
        policy.setExtendwarrantytype(quote.getExtendwarrantytype());
//        policy.setOrderid(order.getId());

        policy.setCarowner(carowner);
        policy.setApplicant(applicant);
        policy.setInsured(insured);
        policy.setLegalrightclaim(legalrightclaim);
        policy.setCarinfo(carinfo);

        List<String> coupons = model.getCoupons();
        List<UserCoupon> userCoupons = new ArrayList<>();
        if (coupons != null && coupons.size() > 0) {
            for (String couponCode : coupons) {
                userCoupons.add(new UserCoupon(couponCode));
            }
        }

        /////
        INSEOrder order = new INSEOrder();
        order.setAgent(agent);
        Date now = new Date();
        order.setCreatetime(now);
        order.setModifytime(now);
        order.setOrderstatus(0);
        order.setTotalproductamount(quote.getAmount());
        order.setDeptcode(agent.getDeptid());
        order.setAgentcode(agent.getAgentcode());
        order.setAgentname(agent.getName());
        order.setOperator(agent.getName());
        order.setDeptcode(agent.getDeptid());
        order.setInvoiceemail(model.getInvoiceemail());
        order.setNeedinvoice(model.isNeedinvoice());

        order.setPolicy(policy);
        order.setUserCoupons(userCoupons);

        orderService.insertOrder(order);

//        String productCode = "ewarranty";
//        if (coupons != null && coupons.size() > 0) {
//            for (String couponCode : coupons) {
//                userCouponService.useUserCoupon(couponCode, agent.getJobnum(), order.getOrderno(), productCode);
//            }
//        }

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("orderno", order.getOrderno());
        result.put("amount", order.getTotalpaymentamount());
        result.put("carownerinfo", model.getCarownerinfo());
        result.put("insured", model.getInsured());
        result.put("applicant", model.getApplicant());
        result.put("legalrightclaim", model.getLegalrightclaim());

        Map<String, Object> carinfoMap = BeanUtils.toMap(carinfo);
        carinfoMap.put("registerdate", carinfo.getRegistdate() == null ? null : DateUtils.format(carinfo.getRegistdate(), "yyyy-MM-dd"));
        carinfoMap.put("plateNumber", carinfo.getCarlicenseno());

        result.put("carinfo", carinfoMap);
        result.put("needinvoice", order.isNeedinvoice());
        result.put("invoiceemail", order.getInvoiceemail());
        PolicyDateForm dateForm = new PolicyDateForm();
        dateForm.setStartDate(quote.getStartdate());
        dateForm.setEndDate(quote.getEnddate());
        result.put("policydate", dateForm);

        return new Response(Response.STATUS_SUCCESS, Response.MESSAGE_SUCCESS, result);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public INSEOrder updateOrder(INSEOrder order) {
        return new INSEOrder();
    }

    @RequestMapping(value = "/orderlist", method = {RequestMethod.POST})
    @ResponseBody
    public Response getOrderList(HttpServletRequest request, @RequestBody @Valid OrderListForm orderListForm) {

        LogUtil.info("[延保]获取订单列表, 参数: %s", orderListForm.toString());

        INSBAgent agent = getCurrentAgent(request);

        int limit = 10;
        int pagenum = 1;

        if (orderListForm.getPagesize() > 0) {
            limit = orderListForm.getPagesize();
        }

        if (orderListForm.getPagenum() > 0) {
            pagenum = orderListForm.getPagenum();
        }

        int offset = limit * (pagenum - 1);

        Integer orderstatus = orderListForm.getOrderstatus();

        List<Map<String, Object>> orderList = orderService.getOrderList(agent.getAgentcode(), orderstatus, limit, offset);

        List<OrderForm2> orderForms = new ArrayList<>();
        OrderForm2 orderForm;
        for (Map<String, Object> map : orderList) {
            orderForm = new OrderForm2();
            orderForm.setOrderno((String) map.get("orderno"));
            orderForm.setAmount((map.get("totalpaymentamount")) == null ? 0d : ((BigDecimal) map.get("totalpaymentamount")).doubleValue());
            orderForm.setOrderStatus(getOrderStatus((int) map.get("orderstatus")));
            orderForm.setPlatenumber((String) map.get("carlicenseno"));
            orderForm.setCreatetime((Date) map.get("createtime"));

            orderForms.add(orderForm);

        }


        long recordcount = orderService.selectCount(agent.getAgentcode(), orderstatus);

        long pagecount = recordcount % limit == 0 ? recordcount / limit : recordcount / limit + 1;

        Map<String, Object> result = new HashMap<>();
        result.put("pagecount", pagecount);
        result.put("pagesize", limit);
        result.put("recordcount", recordcount);
        result.put("currentpage", pagenum);
        result.put("orderlist", orderForms);

        return new Response(Response.STATUS_SUCCESS, Response.MESSAGE_SUCCESS, result);
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

    /**
     * 查询订单状态
     *
     * @return
     */
    @RequestMapping(value = "/orderstatus", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Response orderStatus(@RequestBody OrderStatusForm orderStatusForm) {

//        Map<String, Object> result = new HashMap<>();
//        // 查询cm
//        Map<String, Object> order = orderService.getOrderByOrderNo(orderStatusForm.getOrderNo());
//        int orderstatus;
//        if (order.containsKey("orderstatus")) {
//            orderstatus = (int) order.get("orderstatus");
//            String orderstatusString = getOrderStatus(orderstatus);
//            order.put("orderNo", orderStatusForm);
//            order.put("orderStatus", orderstatusString);
//            order.put("orderStatusDesc", orderstatusString);
//
//            return new Response(Response.STATUS_SUCCESS, Response.MESSAGE_SUCCESS, result);
//        }
        // 查询支付平台
        return null;
    }
}
