package com.lzgapi.order.service.impl;

import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.common.HttpClientUtil;
import com.common.HttpSender;
import com.lzgapi.order.aop.INSBOrderAdvice;
import com.lzgapi.order.model.LzgCheckTokenModel;
import com.lzgapi.order.model.LzgOrderModel;
import com.lzgapi.order.service.LzgDataTransferService;
import com.zzb.chn.util.JsonUtils;
import com.zzb.cm.dao.INSBOrderDao;
import com.zzb.cm.entity.INSBInsuredhis;
import com.zzb.cm.entity.INSBOrder;
import com.zzb.cm.entity.INSBPerson;
import com.zzb.conf.dao.*;
import com.zzb.conf.entity.*;
import com.zzb.conf.service.INSBUsercommentService;
import com.zzb.conf.service.INSBWorkflowsubService;
import com.zzb.mobile.model.CommonModel;
import com.zzb.mobile.service.AppLoginService;
import com.zzb.mobile.service.INSBMyOrderService;
import com.zzb.mobile.util.MappingType;
import com.zzb.mobile.util.PayConfigMappingMgr;
import net.sf.json.JSONObject;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by Administrator on 2017/5/23.
 */
@Service
public class LzgDataTransferServiceImpl implements LzgDataTransferService {

    private static ResourceBundle config = ResourceBundle.getBundle("config/config");
    /**
     * checkTokenURL 目标应用使用获取到的token获取代理人信息
     */
    private static String checkTokenURL = config.getString("lzg.interface.url") + "/rbk/lzgapi/oauth/checkToken";

    @Resource
    private AppLoginService appLoginService;

    @Resource
    private INSBMyOrderService insbMyOrderService;

    @Resource
    private INSBWorkflowsubDao insbWorkflowsubDao;

    @Resource
    private INSBWorkflowmainDao insbWorkflowmainDao;

    @Resource
    private INSBProviderDao insbProviderDao;

    @Resource
    private INSBUsercommentService insbUsercommentService;

    @Resource
    private INSBOrderDao insbOrderDao;

    @Resource
    private INSBPaychannelDao insbPaychannelDao;

    @Resource
    private INSBPolicyitemDao insbPolicyitemDao;

    @Resource
    private ThreadPoolTaskExecutor taskThreadPool;

    @Resource
    private INSBOrderAdvice insbOrderAdvice;

    @Resource
    private INSBOrderlistenpushDao insbOrderlistenpushDao;

    @Override
    public CommonModel checkToken(Map<String, Object> params) {
        CommonModel commonModel = new CommonModel();
        try {
            LogUtil.info("checkToken params = " + JsonUtils.serialize(params));
            String token = String.valueOf(params.get("checkToken"));
            String resp = HttpClientUtil.doGet(checkTokenURL + "/" + token, new HashMap<>());
            LzgCheckTokenModel model = JsonUtils.deserialize(resp, LzgCheckTokenModel.class);
            LogUtil.info("checkToken resp = " + resp);
            if ("OK".equals(model.getFlag()) && StringUtil.isNotEmpty(model.getAgentcode())) {
                LogUtil.info("checkToken model = " + model);
                commonModel = appLoginService.loginByAgentCodeForLzg(model.getAgentcode(), model.getUsercookie(), "web");
                LogUtil.info("checkToken commonModel = " + JsonUtils.serialize(commonModel));
            } else {
                commonModel.setStatus("fail");
                commonModel.setMessage("验证不通过!");
                LogUtil.info("checkToken commonModel = " + JsonUtils.serialize(commonModel));
            }
        } catch (Exception e) {
            commonModel.setStatus("fail");
            commonModel.setMessage("接口异常！");
            LogUtil.info("checkToken异常：%s params=%s" , e.getMessage(),JsonUtils.serialize(params));
            e.printStackTrace();
        }
        return commonModel;
    }

    private String sendToLzg(Map<String, Object> params, String url) {
        return HttpClientUtil.doPostJsonString(url, JsonUtils.serialize(params));
    }


    public String getQuoteInfoList(String taskId, String orderstatus) {
        LogUtil.info("getQuoteInfoList taskId=%s orderstatus=%s",taskId,orderstatus);
        CommonModel resultAll = new CommonModel();
        try {
            Map<String, Object> queryParams = new HashMap<String, Object>();
            Map<String, Object> result = new HashMap<String, Object>();
            //组织查询条件
            List<Map<String, Object>> resultlist = null;
            long totalnum = 0;
            //orderstatus = 1 待支付
            if ("2".equals(orderstatus)) {
                //orderstatus = "20";
                //queryParams.put("orderstatus", orderstatus);
                String processInstanceId = taskId;//主流程号
                List<Map<String, Object>> subQuoteInfoList = insbWorkflowsubDao.getQuoteInfoByTaskId(processInstanceId);
                LogUtil.info("getQuoteInfoList taskId=%s orderstatus=%s subQuoteInfoList=%s",taskId,orderstatus,subQuoteInfoList);
                if (subQuoteInfoList == null || (subQuoteInfoList.size() == 1 && StringUtil.isEmpty(subQuoteInfoList.get(0))))
                    subQuoteInfoList = insbWorkflowmainDao.getQuoteInfoByTaskId(processInstanceId);
                for (int i = subQuoteInfoList.size() - 1; i >= 0; i--) {
                    Map<String, Object> map = subQuoteInfoList.get(i);
                    if (!"20".equals(map.get("taskcode"))) {
                        subQuoteInfoList.remove(i);
                    }

                    else {
                        //去掉过期的单子 2016-03-30
                        if (insbMyOrderService.removeOverTimeData(processInstanceId, map.get("inscomcode"))) {
                            subQuoteInfoList.remove(i);
                        }
                    }

                    String inscomcode = (String) map.get("inscomcode");
                    if (StringUtil.isNotEmpty(inscomcode)) {
                        INSBProvider provider = new INSBProvider();
                        provider.setPrvcode(inscomcode.substring(0, 4));
                        provider = insbProviderDao.selectOne(provider);
                        map.put("inscomlogo", provider == null ? "" : provider.getLogo());//保险公司logo
                        List<INSBUsercomment> usercomment = insbUsercommentService.getNearestUserComment2(processInstanceId, (String) map.get("inscomcode"), null);
                        if (usercomment != null && usercomment.size() > 0) {
                            map.put("quotenoti", usercomment.get(0).getCommentcontent());//报价的提示语
                        } else {
                            map.put("quotenoti", "");//报价的提示语
                        }

                        //task1361 待支付订单表返回支付方式给前端
                        INSBOrder insbOrder = new INSBOrder();
                        insbOrder.setTaskid(processInstanceId);
                        insbOrder.setPrvid(inscomcode);
                        insbOrder = insbOrderDao.selectOne(insbOrder);            //获取订单表
                        if (insbOrder != null && insbOrder.getPaymentmethod() != null) {
                            INSBPaychannel paychannel = insbPaychannelDao.selectById(insbOrder.getPaymentmethod());//根据订单表ID、获取paychannel的支付方式
                            if (paychannel != null) {
                                    /*map.put("Paymentmethod", paychannel.getPaychannelname());
									map.put("Paytype", paychannel.getPaytype());*/
                                map.put("typecode", PayConfigMappingMgr.getPayCodeByCmCode(MappingType.PAY_TYPE, paychannel.getPaytype()));
                                map.put("channelId", paychannel.getId());
                            } else {
                                INSBWorkflowsub flowsub = this.insbWorkflowsubDao.selectByInstanceId((String) map.get("workflowinstanceid"));
                                if (!"admin".equals(flowsub.getOperator())) {
                                    map.put("typecode", PayConfigMappingMgr.getPayCodeByCmCode(MappingType.PAY_TYPE, "03"));
                                    map.put("channelId", "4");
                                } else {
                                    map.put("Paymentmethod", "");
                                    map.put("Paytype", "");
                                }
                            }

                            if ("20".equals(map.get("taskcode"))) {//当前状态为20 支付的时候判断 是否 为柜台支付  柜台支付返回 柜台支付 状态
                                if (!"admin".equals(map.get("operator")) && insbOrder.getPaymentmethod() != null && "4".equalsIgnoreCase(insbOrder.getPaymentmethod())) {
                                    map.put("taskcode", "201");//柜台支付状态
                                }
                            }
                        }
                    }

                }
                result.put("quoteInfoList", subQuoteInfoList);
                result.put("total", subQuoteInfoList.size());
                result.put("quoteStatue", "");
                //totalnum = insbWorkflowmainDao.myOrderCount2Pay(queryParams);
                //待投保 orderstatus=0
            } else if ("1".equals(orderstatus)) {
                orderstatus = null;
                queryParams.put("orderstatus", orderstatus);
                queryParams.replace("limit", null);
                queryParams.replace("offset", null);
                List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

                String processInstanceId = taskId;
                List<Map<String, Object>> subQuoteInfoList = insbWorkflowsubDao.getQuoteInfoByTaskId(processInstanceId);
                if (subQuoteInfoList == null || (subQuoteInfoList.size() == 1 && StringUtil.isEmpty(subQuoteInfoList.get(0)))) {
                    subQuoteInfoList = insbWorkflowmainDao.getQuoteInfoByTaskId(processInstanceId);
                }
                for (int i = subQuoteInfoList.size() - 1; i >= 0; i--) {
                    Map<String, Object> map = subQuoteInfoList.get(i);
                    boolean needtoremove = false;

                    if (StringUtil.isEmpty(map.get("inscomcode"))
                            || StringUtil.isEmpty(map.get("prvshotname"))
                            || StringUtil.isEmpty(map.get("taskcode"))) {
                        needtoremove = true;
                    }
                    //去掉过期的单子 2016-03-30
                    if (insbMyOrderService.removeOverTimeData2(processInstanceId, map.get("inscomcode"))) {
                        needtoremove = true;
                    }

                    if (needtoremove) {
                        subQuoteInfoList.remove(i);
                    }
                }
                result.put("quoteInfoList", subQuoteInfoList);
                result.put("total", subQuoteInfoList.size());
                result.put("quoteStatue", "");
                //待投保总共条数
            } else {
                String processInstanceId = taskId;
                List<Map<String, Object>> subQuoteInfoList = insbWorkflowsubDao.getQuoteInfoByTaskId(processInstanceId);
                if (subQuoteInfoList == null || (subQuoteInfoList.size() == 1 && StringUtil.isEmpty(subQuoteInfoList.get(0))))
                    subQuoteInfoList = insbWorkflowmainDao.getQuoteInfoByTaskId(processInstanceId);

                //主流程状态为"25","26","27","28","29","23","24"时推送主流程状态
                INSBWorkflowmain insbWorkflowmain = insbWorkflowmainDao.selectByInstanceId(processInstanceId);
                String quoteStatue = "";
                if(insbWorkflowmain != null){
                    quoteStatue = insbWorkflowmain.getTaskcode();
                    List<String> maintaskcode = Arrays.asList(new String[]{"25","26","27","28","29","23","24"});
                    if(!maintaskcode.contains(quoteStatue)){
                        quoteStatue = "";
                    }
                }
                Map<String,String> params = new HashMap<String, String>();
                params.put("taskid",taskId);
                INSBOrder order = insbOrderDao.queryOrder(params);
                String prvId = order == null ? "null" : order.getPrvid();
                //主流程状态
                for (int i = subQuoteInfoList.size() - 1; i >= 0; i--) {
                    Map<String, Object> map = subQuoteInfoList.get(i);
                    if (StringUtil.isEmpty(map.get("inscomcode"))
                            || StringUtil.isEmpty(map.get("prvshotname"))
                            || StringUtil.isEmpty(map.get("taskcode")))
                        subQuoteInfoList.remove(i);
                    if(StringUtil.isNotEmpty(quoteStatue) && prvId.equals(map.get("inscomcode"))){
                        map.put("taskcode",quoteStatue);
                    }
                }
                if (subQuoteInfoList != null && subQuoteInfoList.size() != 0 && subQuoteInfoList.get(0) != null) {
                    result.put("quoteInfoList", subQuoteInfoList);
                    result.put("total", subQuoteInfoList.size());
                }
                result.put("quoteStatue", quoteStatue);
            }

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("resultlist", resultlist);
            //    body.put("orderinfo", map);               //查询成功，完成结果的返回
            //if(resultlist!=null){
            //  body.put("totalnum",totalnum );          //总共的数据条数  resultlist.size()先前的
            //}
            //  bodyResult.put("myorderinfo", body);
            resultAll.setBody(result);
            resultAll.setMessage("报价列表查询成功");
            resultAll.setStatus("success");

            return JSONObject.fromObject(resultAll).toString();
        } catch (Exception e) {                         //查询失败，出现异常
            LogUtil.info("getQuoteInfoList 异常 taskId=%s orderstatus=%s errmsg=%s",taskId,orderstatus,e.getMessage());
            e.printStackTrace();
            resultAll.setMessage("我的订单查询失败");
            resultAll.setStatus("fail");
            return JSONObject.fromObject(resultAll).toString();
        }
    }

    public String getQuoteInfoList(String taskId, String orderstatus,String subInstanceId) {
        LogUtil.info("getQuoteInfoList taskId=%s orderstatus=%s",taskId,orderstatus);
        CommonModel resultAll = new CommonModel();
        try {
            Map<String, Object> queryParams = new HashMap<String, Object>();
            Map<String, Object> result = new HashMap<String, Object>();
            //组织查询条件
            List<Map<String, Object>> resultlist = null;
            long totalnum = 0;
            //orderstatus = 1 待支付
            if ("2".equals(orderstatus)) {
                //orderstatus = "20";
                //queryParams.put("orderstatus", orderstatus);
                String processInstanceId = taskId;//主流程号
                List<Map<String, Object>> subQuoteInfoList = insbWorkflowsubDao.getQuoteInfoByTaskId(processInstanceId);
                LogUtil.info("getQuoteInfoList taskId=%s orderstatus=%s subQuoteInfoList=%s",taskId,orderstatus,subQuoteInfoList);
                if (subQuoteInfoList == null || (subQuoteInfoList.size() == 1 && StringUtil.isEmpty(subQuoteInfoList.get(0))))
                    subQuoteInfoList = insbWorkflowmainDao.getQuoteInfoByTaskId(processInstanceId);
                for (int i = subQuoteInfoList.size() - 1; i >= 0; i--) {
                    Map<String, Object> map = subQuoteInfoList.get(i);
                    if (!"20".equals(map.get("taskcode")) || !subInstanceId.equals(map.get("workflowinstanceid"))) {
                        subQuoteInfoList.remove(i);
                    }

                    else {
                        //去掉过期的单子 2016-03-30
                        if (insbMyOrderService.removeOverTimeData(processInstanceId, map.get("inscomcode"))) {
                            subQuoteInfoList.remove(i);
                        }
                    }

                    String inscomcode = (String) map.get("inscomcode");
                    if (StringUtil.isNotEmpty(inscomcode)) {
                        INSBProvider provider = new INSBProvider();
                        provider.setPrvcode(inscomcode.substring(0, 4));
                        provider = insbProviderDao.selectOne(provider);
                        map.put("inscomlogo", provider == null ? "" : provider.getLogo());//保险公司logo
                        List<INSBUsercomment> usercomment = insbUsercommentService.getNearestUserComment2(processInstanceId, (String) map.get("inscomcode"), null);
                        if (usercomment != null && usercomment.size() > 0) {
                            map.put("quotenoti", usercomment.get(0).getCommentcontent());//报价的提示语
                        } else {
                            map.put("quotenoti", "");//报价的提示语
                        }

                        //task1361 待支付订单表返回支付方式给前端
                        INSBOrder insbOrder = new INSBOrder();
                        insbOrder.setTaskid(processInstanceId);
                        insbOrder.setPrvid(inscomcode);
                        insbOrder = insbOrderDao.selectOne(insbOrder);            //获取订单表
                        if (insbOrder != null && insbOrder.getPaymentmethod() != null) {
                            INSBPaychannel paychannel = insbPaychannelDao.selectById(insbOrder.getPaymentmethod());//根据订单表ID、获取paychannel的支付方式
                            if (paychannel != null) {
                                    /*map.put("Paymentmethod", paychannel.getPaychannelname());
									map.put("Paytype", paychannel.getPaytype());*/
                                map.put("typecode", PayConfigMappingMgr.getPayCodeByCmCode(MappingType.PAY_TYPE, paychannel.getPaytype()));
                                map.put("channelId", paychannel.getId());
                            } else {
                                INSBWorkflowsub flowsub = this.insbWorkflowsubDao.selectByInstanceId((String) map.get("workflowinstanceid"));
                                if (!"admin".equals(flowsub.getOperator())) {
                                    map.put("typecode", PayConfigMappingMgr.getPayCodeByCmCode(MappingType.PAY_TYPE, "03"));
                                    map.put("channelId", "4");
                                } else {
                                    map.put("Paymentmethod", "");
                                    map.put("Paytype", "");
                                }
                            }

                            if ("20".equals(map.get("taskcode"))) {//当前状态为20 支付的时候判断 是否 为柜台支付  柜台支付返回 柜台支付 状态
                                if (!"admin".equals(map.get("operator")) && insbOrder.getPaymentmethod() != null && "4".equalsIgnoreCase(insbOrder.getPaymentmethod())) {
                                    map.put("taskcode", "201");//柜台支付状态
                                }
                            }
                        }
                    }

                }
                result.put("quoteInfoList", subQuoteInfoList);
                result.put("total", subQuoteInfoList.size());
                result.put("quoteStatue", "");
                //totalnum = insbWorkflowmainDao.myOrderCount2Pay(queryParams);
                //待投保 orderstatus=0
            } else if ("1".equals(orderstatus)) {
                orderstatus = null;
                queryParams.put("orderstatus", orderstatus);
                queryParams.replace("limit", null);
                queryParams.replace("offset", null);
                List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

                String processInstanceId = taskId;
                List<Map<String, Object>> subQuoteInfoList = insbWorkflowsubDao.getQuoteInfoByTaskId(processInstanceId);
                if (subQuoteInfoList == null || (subQuoteInfoList.size() == 1 && StringUtil.isEmpty(subQuoteInfoList.get(0)))) {
                    subQuoteInfoList = insbWorkflowmainDao.getQuoteInfoByTaskId(processInstanceId);
                }
                for (int i = subQuoteInfoList.size() - 1; i >= 0; i--) {
                    Map<String, Object> map = subQuoteInfoList.get(i);
                    boolean needtoremove = false;

                    if (!subInstanceId.equals(map.get("workflowinstanceid"))) {
                        needtoremove = true;
                    }

                    if (StringUtil.isEmpty(map.get("inscomcode"))
                            || StringUtil.isEmpty(map.get("prvshotname"))
                            || StringUtil.isEmpty(map.get("taskcode"))) {
                        needtoremove = true;
                    }
                    //去掉过期的单子 2016-03-30
                    if (insbMyOrderService.removeOverTimeData2(processInstanceId, map.get("inscomcode"))) {
                        needtoremove = true;
                    }

                    if (needtoremove) {
                        subQuoteInfoList.remove(i);
                    }
                }
                result.put("quoteInfoList", subQuoteInfoList);
                result.put("total", subQuoteInfoList.size());
                result.put("quoteStatue", "");
                //待投保总共条数
            } else {
                String processInstanceId = taskId;
                List<Map<String, Object>> subQuoteInfoList = insbWorkflowsubDao.getQuoteInfoByTaskId(processInstanceId);
                if (subQuoteInfoList == null || (subQuoteInfoList.size() == 1 && StringUtil.isEmpty(subQuoteInfoList.get(0))))
                    subQuoteInfoList = insbWorkflowmainDao.getQuoteInfoByTaskId(processInstanceId);

                //主流程状态为"25","26","27","28","29","23","24"时推送主流程状态
                INSBWorkflowmain insbWorkflowmain = insbWorkflowmainDao.selectByInstanceId(processInstanceId);
                String quoteStatue = "";
                if(insbWorkflowmain != null){
                    quoteStatue = insbWorkflowmain.getTaskcode();
                    List<String> maintaskcode = Arrays.asList(new String[]{"25","26","27","28","29","23","24"});
                    if(!maintaskcode.contains(quoteStatue)){
                        quoteStatue = "";
                    }
                }
                Map<String,String> params = new HashMap<String, String>();
                params.put("taskid",taskId);
                INSBOrder order = insbOrderDao.queryOrder(params);
                String prvId = order == null ? "null" : order.getPrvid();
                //主流程状态
                for (int i = subQuoteInfoList.size() - 1; i >= 0; i--) {
                    Map<String, Object> map = subQuoteInfoList.get(i);
                    if (StringUtil.isEmpty(map.get("inscomcode"))
                            || StringUtil.isEmpty(map.get("prvshotname"))
                            || StringUtil.isEmpty(map.get("taskcode"))
                            || !subInstanceId.equals(map.get("workflowinstanceid")))
                        subQuoteInfoList.remove(i);
                    if(StringUtil.isNotEmpty(quoteStatue) && prvId.equals(map.get("inscomcode"))){
                        map.put("taskcode",quoteStatue);
                    }
                }
                if (subQuoteInfoList != null && subQuoteInfoList.size() != 0 && subQuoteInfoList.get(0) != null) {
                    result.put("quoteInfoList", subQuoteInfoList);
                    result.put("total", subQuoteInfoList.size());
                }
                result.put("quoteStatue", quoteStatue);
            }

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("resultlist", resultlist);
            //    body.put("orderinfo", map);               //查询成功，完成结果的返回
            //if(resultlist!=null){
            //  body.put("totalnum",totalnum );          //总共的数据条数  resultlist.size()先前的
            //}
            //  bodyResult.put("myorderinfo", body);
            resultAll.setBody(result);
            resultAll.setMessage("报价列表查询成功");
            resultAll.setStatus("success");

            return JSONObject.fromObject(resultAll).toString();
        } catch (Exception e) {                         //查询失败，出现异常
            LogUtil.info("getQuoteInfoList 异常 taskId=%s orderstatus=%s errmsg=%s",taskId,orderstatus,e.getMessage());
            e.printStackTrace();
            resultAll.setMessage("我的订单查询失败");
            resultAll.setStatus("fail");
            return JSONObject.fromObject(resultAll).toString();
        }
    }

    /**
     * 查询保单列表信息接口最新
     *
     * @param agentnum 代理人工号
     * @param taskid   任务号
     * @return
     */
    @Override
    public Map<String,Object> getPolicyitemList(String agentnum, String taskid) {
        Map<String, Object> result = new HashMap<>();
        try {
            String re = "";
            Map<String, Object> queryParams = new HashMap<String, Object>();
            if (StringUtil.isNotEmpty(agentnum) && StringUtil.isNotEmpty(taskid)) {
                queryParams.put("agentnum", agentnum);
                queryParams.put("taskid", taskid);
            } else {
                //没有代理人数据不进行查询
                result.put("status", "fail");
                result.put("message", "查询保单列表信息失败！");
                JSONObject jsonObject = JSONObject.fromObject(result);
                return result;
            }
            queryParams.put("limit", 10);
            queryParams.put("offset", 0);
            //组织响应json数据
            //查询保单列表
            List<Map<String, Object>> policyitemList = new ArrayList<Map<String, Object>>();
            policyitemList = insbPolicyitemDao.getPolicyitemList(queryParams);//保单列表
            //long totals = insbPolicyitemDao.getPolicyitemListTotalsByAgentnum(queryParams);
            result.put("status", "success");
            result.put("message", "操作已成功！");
            result.put("policyList", policyitemList);
            result.put("agentCode", agentnum);
            result.put("total", policyitemList != null ? policyitemList.size() : 0);
            result.put("taskId", taskid);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            result.put("status", "fail");
            result.put("message", "查询保单列表信息失败！");
            return result;
        }
    }

    public String pushOrderToLzg(String taskId,String subInstanceId,String prvId){
        taskThreadPool.execute(new Runnable(){
            @Override
            public void run() {
                try{
                    INSBOrderlistenpush insbOrderlistenpush = insbOrderlistenpushDao.selectDataByTaskId(taskId);
                    //懒掌柜订单才通知懒掌柜
                    if(insbOrderlistenpush != null && insbOrderlistenpush.getTaskid().equals(taskId)){
                        LogUtil.info("pushOrderToLzg update modelInfo subFlow=" + subInstanceId);
                        Map<String,String> params = new HashMap<String, String>();
                        params.put("taskid",taskId);
                        INSBOrder order = insbOrderDao.queryOrder(params);
                        if(order == null){
                            order = new INSBOrder() ;
                            order.setTaskid(taskId);
                            order.setOrderstatus("0");
                            order.setPrvid(prvId);
                            order.setCreatetime(new Date());
                        }
                        LzgOrderModel model = insbOrderAdvice.comLzgOrderModel(taskId, order.getOrderstatus(), order.getCreatetime(), order.getPrvid(), subInstanceId);
                        Map<String,Object> body = (Map)model.getBody();
                        if(body !=null){
                            if(body.containsKey("quoteInfoList")){
                                ArrayList<Map<String,String>> quoteInfoList = (ArrayList)body.get("quoteInfoList");
                                if(quoteInfoList!=null &&quoteInfoList.size()>0) {
                                    for (Map<String, String> info : quoteInfoList) {
                                        info.put("taskcode", "30");
                                        info.put("taskstate", "Closed");
                                    }
                                }
                            }
                        }
                        LogUtil.info("pushOrderToLzg update modelInfo=" + JsonUtils.serialize(model));
                        //System.out.println("pushOrderToLzg modelInfo=" + JsonUtils.serialize(model));
                        String result = insbOrderAdvice.sendToLzg(model, INSBOrderAdvice.updateOrderURL);
                        //System.out.printf("pushOrderToLzg modelInfo taskID=%s result=%s", order.getTaskid(), result);
                        LogUtil.info("pushOrderToLzg update modelInfo taskID=%s result=%s",taskId,result);
                        String status = order.getOrderstatus();
                        //待配送，完成状态推送
                        if("3".equals(status) ||"4".equals(status)) {
                            //推送保单信息
                            Map<String, Object> policyMap = getPolicyitemList(order.getAgentcode(), order.getTaskid());
                            //System.out.printf("update modelInfo taskID=%s resultpolicyMap=%s", order.getTaskid(), JsonUtils.serialize(policyMap));
                            if ((Integer) policyMap.getOrDefault("total", 0) > 0) {
                                //System.out.printf("update modelInfo taskID=%s policyMap=%s", order.getTaskid(),JsonUtils.serialize(policyMap));
                                LogUtil.info("pushOrderToLzg update modelInfo taskID=%s policyMap=%s",order.getTaskid(),JsonUtils.serialize(policyMap));
                                String res = HttpClientUtil.doPostJsonString(INSBOrderAdvice.policySynURL, JsonUtils.serialize(policyMap));
                                LogUtil.info("pushOrderToLzg update modelInfo taskID=%s policyRes=%s",order.getTaskid(),res);
                                //System.out.printf("update modelInfo taskID=%s res=%s",order.getTaskid(),res);
                            }
                        }
                    }
                }catch(Exception e){
                    LogUtil.info("pushOrderToLzg order update error taskId = %s, message = %s",taskId,e.getMessage());
                    e.printStackTrace();
                }
            }
        });

        return "";
    }

}
