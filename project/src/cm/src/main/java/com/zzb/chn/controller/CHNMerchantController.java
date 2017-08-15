package com.zzb.chn.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.zzb.chn.service.CHNCommissionRateService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cninsure.core.utils.DateUtil;
import com.cninsure.core.utils.LogUtil;
import com.zzb.chn.aop.SecureValid;
import com.zzb.chn.bean.DeliveryBean;
import com.zzb.chn.bean.QuoteBean;
import com.zzb.chn.service.CHNChannelService;
import com.zzb.chn.service.CHNMerchantService;
import com.zzb.chn.util.JsonUtils;
import com.zzb.cm.entity.INSBOrder;
import com.zzb.cm.service.INSBCarinfoService;
import com.zzb.cm.service.INSBCarkindpriceService;
import com.zzb.cm.service.INSBCarmodelinfoService;
import com.zzb.cm.service.INSBOrderService;
import com.zzb.conf.entity.INSBOrderpayment;
import com.zzb.conf.entity.INSBPaychannel;
import com.zzb.conf.entity.INSBWorkflowsub;
import com.zzb.conf.service.INSBAgentService;
import com.zzb.conf.service.INSBOrderpaymentService;
import com.zzb.conf.service.INSBPaychannelService;
import com.zzb.conf.service.INSBWorkflowsubService;
import com.zzb.mobile.model.CommonModel;

@Controller
@RequestMapping("/channelMerchant/*")
public class CHNMerchantController {
	@Resource
    private CHNMerchantService chnMerchantService;

    @Resource
    private INSBAgentService insbAgentService;

    @Resource
    private INSBCarinfoService insbCarinfoService;

    @Resource
    private INSBCarmodelinfoService insbCarmodelinfoService;

    @Resource
    private INSBCarkindpriceService insbCarkindpriceService;

    @Resource
    private CHNChannelService chnChannelService;

    @Resource
    private INSBOrderpaymentService insbOrderpaymentService;

    @Resource
    private INSBWorkflowsubService insbWorkflowsubService;

    @Resource
    private INSBOrderService insbOrderService;

    @Resource
    private INSBPaychannelService insbPaychannelService;

    @Resource
    private CHNCommissionRateService chnCommissionRateService;

    //车险任务页面任务类型标记对应的工作流程编码集合
    //报价
    private final String[] TASK_STATE_2 = {"1","2","3","4","6","7","8","15","31","32","53"};
    //报价失败
    private final String[] TASK_STATE_13 = {"51","52","13"};
    //报价成功
    private final String[] TASK_STATE_14 = {"14"};
    //核保中
    private final String[] TASK_STATE_16 = {"17","18","38","16"};
    //核保失败，退回修改
    private final String[] TASK_STATE_19 = {"19"};
    //核保成功
    private final String[] TASK_STATE_20 = {"20","21"};
    //承保成功
    private final String[] TASK_STATE_23 = {"23","24"};
    //支付成功
    private final String[] TASK_STATE_25 = {"25","26","27","28"};
    //完成
    private final String[] TASK_STATE_33 = {"29","33"};
    //关闭
    private final String[] TASK_STATE_30 = {"30","34","37","39"};

	@SecureValid
    @RequestMapping(value = "/getProviders", method = RequestMethod.POST)
    @ResponseBody
    public CommonModel getProviders(@RequestBody QuoteBean quoteBeanIn) {
        LogUtil.info("channelMerchant-getProviders-in:" + JsonUtils.serialize(quoteBeanIn));
        CommonModel resultOut = new CommonModel();
        
        try {
        	resultOut = chnMerchantService.getProviders(quoteBeanIn);
        } catch (Exception e) {
            LogUtil.error("channelMerchant-getProviders发生异常：" + e.getMessage());
            e.printStackTrace();
            resultOut.setStatus(CommonModel.STATUS_FAIL);
            resultOut.setMessage("getProviders异常：" + e.getMessage());
        }
        
        LogUtil.info("channelMerchant-getProviders-out:" + JsonUtils.serialize(resultOut));
        return resultOut;
    }
    
	@SecureValid
    @RequestMapping(value = "/getInterCallDetail", method = RequestMethod.POST)
    @ResponseBody
    public CommonModel getInterCallDetail(@RequestBody Map<String, Object> mapIn) {
        LogUtil.info("channelMerchant-getInterCallDetail-in:" + JsonUtils.serialize(mapIn));
        CommonModel resultOut = new CommonModel();
        
        try {
        	resultOut = chnMerchantService.getInterCallDetail(mapIn);
        } catch (Exception e) {
        	String msg = e.getMessage();
            LogUtil.error("channelMerchant-getInterCallDetail发生异常：" + msg);
            e.printStackTrace();
            if (msg.contains("doesn't exist")) { //分表不存在代表没有使用过a接口
	            resultOut.setStatus(CommonModel.STATUS_SUCCESS);
	            resultOut.setMessage(null);
            } else {
            	resultOut.setStatus(CommonModel.STATUS_FAIL);
	            resultOut.setMessage("getInterCallDetail异常：" + msg);
            }
        }
        
        LogUtil.info("channelMerchant-getInterCallDetail-out:" + JsonUtils.serialize(resultOut));
        return resultOut;
    }
    
	@SecureValid
    @RequestMapping(value = "/getInterCallDayDetail", method = RequestMethod.POST)
    @ResponseBody
    public CommonModel getInterCallDayDetail(@RequestBody Map<String, Object> mapIn) {
        LogUtil.info("channelMerchant-getInterCallDayDetail-in:" + JsonUtils.serialize(mapIn));
        CommonModel resultOut = new CommonModel();
        
        try {
        	resultOut = chnMerchantService.getInterCallDayDetail(mapIn);
        } catch (Exception e) {
        	String msg = e.getMessage();
            LogUtil.error("channelMerchant-getInterCallDayDetail发生异常：" + msg);
            e.printStackTrace();
            if (msg.contains("doesn't exist")) { //分表不存在代表没有使用过a接口
	            resultOut.setStatus(CommonModel.STATUS_SUCCESS);
	            resultOut.setMessage(null);
            } else {
            	resultOut.setStatus(CommonModel.STATUS_FAIL);
	            resultOut.setMessage("getInterCallDayDetail异常：" + msg);
            }
        }
        
        LogUtil.info("channelMerchant-getInterCallDayDetail-out:" + JsonUtils.serialize(resultOut));
        return resultOut;
    }
    
	@SecureValid
    @RequestMapping(value = "/querySummaryCount", method = RequestMethod.POST)
    @ResponseBody
    public CommonModel querySummaryCount(@RequestBody Map<String, Object> mapIn) {
        LogUtil.info("channelMerchant-querySummaryCount-in:" + JsonUtils.serialize(mapIn));
        CommonModel resultOut = new CommonModel();
        
        try {
        	resultOut = chnMerchantService.querySummaryCount(mapIn);
        } catch (Exception e) {
        	String msg = e.getMessage();
            LogUtil.error("channelMerchant-querySummaryCount发生异常：" + msg);
            e.printStackTrace();
            if (msg.contains("doesn't exist")) { //分表不存在代表没有使用过a接口
	            resultOut.setStatus(CommonModel.STATUS_SUCCESS);
	            resultOut.setMessage(null);
            } else {
            	resultOut.setStatus(CommonModel.STATUS_FAIL);
	            resultOut.setMessage("querySummaryCount异常：" + msg);
            }
        }
        
        LogUtil.info("channelMerchant-querySummaryCount-out:" + JsonUtils.serialize(resultOut));
        return resultOut;
    }

    /**
     * 根据渠道ID 查询对应渠道开通的所有地市
     * @param mapIn
     * @return
     */
	@SecureValid
    @ResponseBody
    @RequestMapping(value = "/queryAgreementArea", method = RequestMethod.POST)
    public CommonModel queryAgreementArea(@RequestBody Map<String, Object> mapIn) {
        LogUtil.info("channelMerchant-queryAgreementArea-in:" + JsonUtils.serialize(mapIn));
        CommonModel resultOut = new CommonModel();

        try {
            resultOut = chnMerchantService.queryAgreementArea(mapIn);
        } catch (Exception e) {
            LogUtil.error("channelMerchant-queryAgreementArea 发生异常：" + e.getMessage());
            e.printStackTrace();
            resultOut.setStatus(CommonModel.STATUS_FAIL);
            resultOut.setMessage("queryAgreementArea异常：" + e.getMessage());
        }

        LogUtil.info("channelMerchant-queryAgreementArea-out:" + JsonUtils.serialize(resultOut));
        return resultOut;
    }
    
	@SecureValid
    @RequestMapping(value = "/getChannels", method = RequestMethod.POST)
    @ResponseBody
    public CommonModel getChannels(@RequestBody Map<String, Object> mapIn) {
        LogUtil.info("channelMerchant-getChannels-in:" + JsonUtils.serialize(mapIn));
        CommonModel resultOut = new CommonModel();
        
        try {
        	resultOut = chnMerchantService.getChannels(mapIn);
        } catch (Exception e) {
            LogUtil.error("channelMerchant-getChannels发生异常：" + e.getMessage());
            e.printStackTrace();
            resultOut.setStatus(CommonModel.STATUS_FAIL);
            resultOut.setMessage("getChannels异常：" + e.getMessage());
        }
        
        //LogUtil.info("channelMerchant-getChannels-out:" + JsonUtils.serialize(resultOut));
        return resultOut;
    }

    @SecureValid
    @RequestMapping(value = "/getOldCarTaskList", method = RequestMethod.POST)
    @ResponseBody
    public CommonModel getOldCarTaskList(@RequestBody Map<String, Object> mapIn) {
        LogUtil.info("channelMerchant-getOldCarTaskList-in:" + JsonUtils.serialize(mapIn));
        CommonModel resultOut = new CommonModel();

        try {
            String taskState = mapIn.get("taskState") == null ? "" : mapIn.get("taskState").toString();
            List<String> taskStateList = null;
            switch (taskState){
                case "2" :
                    taskStateList = Arrays.asList(TASK_STATE_2);
                    break;
                case "13" :
                    taskStateList = Arrays.asList(TASK_STATE_13);
                    break;
                case "14" :
                    taskStateList = Arrays.asList(TASK_STATE_14);
                    break;
                case "16" :
                    taskStateList = Arrays.asList(TASK_STATE_16);
                    break;
                case "19" :
                    taskStateList = Arrays.asList(TASK_STATE_19);
                    break;
                case "20" :
                    taskStateList = Arrays.asList(TASK_STATE_20);
                    break;
                case "23" :
                    taskStateList = Arrays.asList(TASK_STATE_23);
                    break;
                case "25" :
                    taskStateList = Arrays.asList(TASK_STATE_25);
                    break;
                case "33" :
                    taskStateList = Arrays.asList(TASK_STATE_33);
                    break;
                case "30" :
                    taskStateList = Arrays.asList(TASK_STATE_30);
                    break;
                default : break;
            }
            mapIn.put("taskStateList",taskStateList);
            resultOut = chnMerchantService.getOldCarTaskList(mapIn);
        } catch (Exception e) {
            LogUtil.error("channelMerchant-getOldCarTaskList发生异常：" + e.getMessage());
            e.printStackTrace();
            resultOut.setStatus(CommonModel.STATUS_FAIL);
            resultOut.setMessage("getOldCarTaskList异常：" + e.getMessage());
        }

        return resultOut;
    }

    @SecureValid
    @RequestMapping(value = "/getCarTaskDetail", method = RequestMethod.POST)
    @ResponseBody
    public CommonModel getCarTaskDetail(@RequestBody Map<String, Object> mapIn) {
        LogUtil.info("channelMerchant-getCarTaskDetail-in:" + JsonUtils.serialize(mapIn));
        CommonModel resultOut = new CommonModel();
        try {
            String maininstanceid = mapIn.get("taskid").toString();
            String inscomcode = mapIn.get("inscomcode").toString();
            //代理人信息
            Map<String, Object> agentInfo = insbAgentService.getCarTaskAgentInfo(maininstanceid, "DETAIL");
            //车辆信息
            Map<String, Object> carInfo = insbCarinfoService.getCarTaskCarInfo(maininstanceid, inscomcode, "SHOW");
            Map<String, Object> carmodelinfo = insbCarmodelinfoService.getCarmodelInfoByCarinfoId(maininstanceid, (String) carInfo.get("carinfoId"), inscomcode, "CARINFODIALOG");
            //其他信息
            Map<String, Object> otherInfo = insbCarinfoService.getCarTaskOtherInfo(maininstanceid, inscomcode, "SHOW");
            //支付有效日期
            Date payValidDate = chnChannelService.getPayValidDate(maininstanceid, inscomcode, false, true);
            otherInfo.put("payValidDate",DateUtil.toDateTimeString(payValidDate));
            //已选保险配置信息
            Map<String, Object> insConfigInfo = insbCarkindpriceService.getCarInsConfigByInscomcode(inscomcode, maininstanceid);
            //配送信息
            DeliveryBean delivery = chnChannelService.getDelivery(maininstanceid, inscomcode);

            INSBOrder order=insbOrderService.getOneOrderByTaskIdAndInscomcode(maininstanceid, inscomcode);
            INSBOrderpayment insbOrderpayment = new INSBOrderpayment();
            insbOrderpayment.setTaskid(maininstanceid);
            String subinstanceid = mapIn.get("subInstanceId") + "";
            if(subinstanceid!=null && !"null".equals(subinstanceid)){
                if(maininstanceid.equals(subinstanceid)){
                    INSBWorkflowsub insbWorkflowsub = new INSBWorkflowsub();
                    insbWorkflowsub.setMaininstanceid(maininstanceid);
                    insbWorkflowsub.setTaskcode("33");
                    insbWorkflowsub = insbWorkflowsubService.queryOne(insbWorkflowsub);
                    if(insbWorkflowsub!=null && insbWorkflowsub.getInstanceid()!=null){
                        insbOrderpayment.setSubinstanceid(insbWorkflowsub.getInstanceid());
                    }
                }else{
                    insbOrderpayment.setSubinstanceid(subinstanceid);
                }
            }

            List<INSBOrderpayment> payInfos = insbOrderpaymentService.queryList(insbOrderpayment);//zhifu信息
            List<INSBOrderpayment> payInfoss = new ArrayList<INSBOrderpayment>();
            if(payInfos!=null){
                for (INSBOrderpayment payment : payInfos) {
                    INSBOrderpayment payments = new INSBOrderpayment();
                    payments.setAmount(payment.getAmount());
                    payments.setPaymentransaction(payment.getPaymentransaction());
                    payments.setCreatetime(payment.getCreatetime());
                    payments.setTradeno(payment.getTradeno());
                    payments.setCurrencycode(payment.getCurrencycode());
                    payments.setPayflowno(payment.getPayflowno());
                    payments.setPayorderno(order.getOrderno());//订单编号
                    if(payment.getPaychannelid()!=null && !"".equals(payment.getPaychannelid())){
                        INSBPaychannel paychannel = insbPaychannelService.queryById(payment.getPaychannelid());
                        String channelId = payment.getPaychannelid();
                        payments.setPaychannelid(channelId);
                        payments.setPaychannelname(paychannel.getPaychannelname());
                        //28分期保，30银联
                        if(channelId.equals("28") || channelId.equals("30")){
                            //获得调用接口url(from config)
                            payments.setSecpayorderrequrl(insbOrderService.getQuerySecondPayURL(channelId));
                        }
                    }
                    if(payment.getRefundtype()!=null && !"".equals(payment.getRefundtype())){
                        if("02".equals(payment.getRefundtype())){
                            payments.setRefundtype("差额退款");
                        }else {
                            payments.setRefundtype("全额退款");
                        }
                    }
                    if(payment.getPayresult()!=null && !"".equals(payment.getPayresult())){
                        if("01".equals(payment.getPayresult())){
                            payments.setPayresult("未支付");
                        }else if("02".equals(payment.getPayresult())){
                            payments.setPayresult("支付成功");
                        }else if("03".equals(payment.getPayresult())){
                            payments.setPayresult("退款");
                        }else if("04".equals(payment.getPayresult())){
                            payments.setPayresult("已过期");
                        }else if("05".equals(payment.getPayresult())){
                            payments.setPayresult("支付失败");
                        }else{
                            payments.setPayresult(payment.getPayresult());
                        }
                    }
                    if(subinstanceid.equals(payment.getSubinstanceid()) || maininstanceid.equals(subinstanceid)){//是当前报价公司，子流程实例的支付信息才显示
                        payInfoss.add(payments);
                    }
                }
            }

            Map<String,Object> body = new HashMap<String,Object>();
            body.put("insConfigInfo",insConfigInfo);
            body.put("agentInfo",agentInfo);
            body.put("carInfo",carInfo);
            body.put("carmodelinfo",carmodelinfo);
            body.put("otherInfo", otherInfo);
            body.put("delivery",delivery);
            body.put("payInfos",payInfoss);
            resultOut.setStatus(CommonModel.STATUS_SUCCESS);
            resultOut.setBody(body);
        } catch (Exception e) {
            LogUtil.error("channelMerchant-getCarTaskDetail发生异常：" + e.getMessage());
            e.printStackTrace();
            resultOut.setStatus(CommonModel.STATUS_FAIL);
            resultOut.setMessage("getCarTaskDetail异常：" + e.getMessage());
        }

        return resultOut;
    }
    
    //@SecureValid
    @RequestMapping(value = "/buildCountData", method = RequestMethod.POST)
    @ResponseBody
    public CommonModel buildCountData(@RequestBody Map<String, Object> mapIn) {
        LogUtil.info("channelMerchant-buildCountData-in:" + JsonUtils.serialize(mapIn));
        CommonModel resultOut = new CommonModel();
        
        try {
        	resultOut = chnMerchantService.buildCountData(mapIn);
        } catch (Exception e) {
            LogUtil.error("channelMerchant-buildCountData发生异常：" + e.getMessage());
            e.printStackTrace();
            resultOut.setStatus(CommonModel.STATUS_FAIL);
            resultOut.setMessage("buildCountData异常：" + e.getMessage());
        }
        
        LogUtil.info("channelMerchant-buildCountData-out:" + JsonUtils.serialize(resultOut));
        return resultOut;
    }

    /**
     * 查询佣金信息 传参
     * @param params
     * @return
     */
    @RequestMapping(value = "/queryCommissionInfoForBC", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String queryCommissionInfoForBC(@RequestBody Map<String,Object> params){
        return chnCommissionRateService.queryCommissionInfoForBC(params);
    }
}
