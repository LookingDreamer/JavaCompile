package com.zzb.extra.controller;

import com.cninsure.core.controller.BaseController;
import com.cninsure.core.exception.ControllerException;
import com.cninsure.core.utils.BeanUtils;
import com.cninsure.core.utils.StringUtil;
import com.zzb.extra.model.AppInsuredMyModelForMinizzb;
import com.zzb.extra.model.MyOrderParamForMinizzb;
import com.zzb.extra.model.PolicyListQueryParamForMinizzb;
import com.zzb.extra.util.ParamUtils;
import com.zzb.mobile.model.AppInsuredMyModel;
import com.zzb.mobile.model.CommonModel;
import com.zzb.mobile.model.MyOrderParam;
import com.zzb.mobile.model.policyoperat.PolicyListQueryParam;
import com.zzb.mobile.service.AppInsuredMyService;
import com.zzb.mobile.service.AppInsuredQuoteService;
import com.zzb.mobile.service.AppMyOrderInfoService;
import com.zzb.mobile.service.INSBMyOrderService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

@Controller
@RequestMapping("/mobile/basedata/minizzb/task/*")
public class TaskApiController extends BaseController {

    @Resource
    private AppInsuredQuoteService insuredQuoteService;

    @Resource
    private AppInsuredMyService appInsuredMyService;

    @Resource
    private INSBMyOrderService insbMyOrderService;

    @Resource
    private AppMyOrderInfoService appMyOrderInfoService;

//    @Resource
//    private AppInsuredMyService appInsuredMyService;
//
//    @RequestMapping(value = "/getMyPoliciesByPagging", method = RequestMethod.POST)
//    @ResponseBody
//    public String getMyPoliciesByPagging(@RequestBody AppInsuredMyModel model) {
//        try {
//            if (model.getAgentid() != null && model.getAgentid().equals(""))
//                return ParamUtils.resultMap(false, "参数不正确");
//            return appInsuredMyService.getMyPoliciesByAgentId(model.getAgentid(), model.getKeyword(), StringUtil.isEmpty("offset") ? 0 : model.getOffset(), StringUtil.isEmpty("limit") ? 10 : model.getLimit()).toString();
//        } catch (Exception e) {
//            return ParamUtils.resultMap(false, "系统错误");
//        }
//    }

    /**
     * 参考：INSBMyOrderController 的 方法 getMyOrderList ，路径："/mobile/*" @RequestMapping(value = "basedata/myTask/getMyOrderList", method = RequestMethod.POST)
     * 我的订单接口
     */
    @RequestMapping(value = "/myTask/getMyOrderList", method = RequestMethod.POST)
    @ResponseBody
    public String getMyOrderList(@RequestBody MyOrderParamForMinizzb myOrderParam) {
        return insbMyOrderService.showMyOrderForMinizzb(myOrderParam.getChanneluserid(),
                myOrderParam.getCarlicenseno(), myOrderParam.getInsuredname(),
                myOrderParam.getOrderstatus(), myOrderParam.getLimit(),
                myOrderParam.getOffset(), myOrderParam.getTaskcreatetimeup(), myOrderParam.getTaskcreatetimedown());
    }


    /**
     * 参考：AppInsuredMyController 的方法 /mobile/basedata/my/getMyPoliciesByPagging
     */
    @RequestMapping(value="/my/getMyPoliciesByPagging",method=RequestMethod.POST)
    @ResponseBody
    public CommonModel getMyPoliciesByPagging(@RequestBody AppInsuredMyModelForMinizzb model) {
        return appInsuredMyService.getMyPoliciesByMinizzb(model.getChanneluserid(), model.getKeyword(), StringUtil.isEmpty("offset") ? 0 : model.getOffset(), StringUtil.isEmpty("limit") ? 10 : model.getLimit());

    }

    /**
     *  参考：com.zzb.mobile.controller.AppMyOrderInfoController#getPolicyitemList02(com.zzb.mobile.model.policyoperat.PolicyListQueryParam)
     *  参考的路径："/mobile/*" "basedata/myTask/getPolicyitemList02"
     * 查询保单列表信息接口最新
     * @param agentnum 代理人工号
     * @param queryinfo 模糊查询信息
     * @param pageSize 每页条数
     * @param currentPage 当前页码
     */
    @RequestMapping(value = "myTask/getPolicyitemList02", method = RequestMethod.POST)
    @ResponseBody
    public String getPolicyitemList02(@RequestBody PolicyListQueryParamForMinizzb policyListQueryParam)
            throws ControllerException {
        return appMyOrderInfoService.getPolicyitemList02ForMinizzb(policyListQueryParam.getChanneluserid(), policyListQueryParam.getQueryinfo(),
                policyListQueryParam.getQuerytype(), policyListQueryParam.getPageSize(), policyListQueryParam.getCurrentPage());
    }


}
