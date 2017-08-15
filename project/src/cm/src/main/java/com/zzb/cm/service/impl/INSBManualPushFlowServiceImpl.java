package com.zzb.cm.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import javax.annotation.Resource;

import com.zzb.cm.service.INSBOrderService;
import com.zzb.cm.service.INSBUnderwritingService;
import com.zzb.conf.entity.INSBOrderdelivery;
import com.zzb.conf.service.INSBOrderdeliveryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.utils.DateUtil;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.cninsure.system.service.INSCDeptService;
import com.common.HttpClientUtil;
import com.common.WorkFlowUtil;
import com.common.WorkflowAttachHandleUtil;
import com.common.WorkflowFeedbackUtil;
import com.common.redis.CMRedisClient;
import com.zzb.cm.Interface.service.InterFaceService;
import com.zzb.cm.controller.vo.ManualPushFlowVo;
import com.zzb.cm.dao.INSBManualPushFlowDao;
import com.zzb.cm.dao.INSBOrderDao;
import com.zzb.cm.dao.INSBQuoteinfoDao;
import com.zzb.cm.entity.INSBOrder;
import com.zzb.cm.entity.INSBQuoteinfo;
import com.zzb.cm.service.INSBManualPushFlowService;
import com.zzb.cm.service.INSBWorkflowDataService;
import com.zzb.conf.dao.INSBAgentDao;
import com.zzb.conf.dao.INSBOrderpaymentDao;
import com.zzb.conf.dao.INSBPaychannelmanagerDao;
import com.zzb.conf.entity.INSBAgent;
import com.zzb.conf.entity.INSBOrderpayment;
import com.zzb.conf.entity.INSBPaychannelmanager;
import com.zzb.conf.service.INSBWorkflowmainService;
import com.zzb.mobile.service.AppInsuredQuoteService;
import com.zzb.model.WorkFlow4TaskModel;

import net.sf.json.JSONObject;

/**
 * 人工推送工作流service实现类
 *
 * @author 杨威    2016/7/1
 */
@Service
@Transactional
public class INSBManualPushFlowServiceImpl implements INSBManualPushFlowService {

    private static String WORKFLOW = "";

    static {
        // 读取相关的配置
        ResourceBundle resourceBundle = ResourceBundle.getBundle("config/config");
        WORKFLOW = resourceBundle.getString("workflow.url");
    }

    @Resource
    private INSBPaychannelmanagerDao insbPaychannelmanagerDao;
    @Resource
    private INSBManualPushFlowDao insbManualPushFlowDao;
    @Resource
    private INSBAgentDao insbAgentDao;
    @Resource
    private INSBOrderDao insbOrderDao;
    @Resource
    private INSBQuoteinfoDao insbQuoteinfoDao;
    @Resource
    private INSCDeptService inscDeptService;
    @Resource
    private INSBOrderpaymentDao insbOrderpaymentDao;
    @Resource
    private INSBWorkflowDataService workflowDataService;
    @Resource
    private InterFaceService interFaceService;
    @Resource
    private AppInsuredQuoteService appInsuredQuoteService;
    @Resource
    private INSBOrderService insbOrderService;
    @Resource
	private INSBWorkflowmainService workflowmainService;
    @Resource
    private INSBUnderwritingService insbUnderwritingService;
    @Resource
    private INSBOrderdeliveryService insbOrderdeliveryService;

    /**
     * 任务列表查询		杨威
     */
    @Override
    public String showTaskList(Map<String, Object> param) {
        //查询总的数据条数,为分页查询使用
        Long num = insbManualPushFlowDao.showTaskListCount(param);
        //查询分页数据
        List<Map<String, Object>> tasklist = insbManualPushFlowDao.showTaskList(param);
        //特殊情况下状态是否需要同步
        boolean toSync = false;

        //处理工作流子流程已经完成，但CM子流程没有结束的情况
        for (Map<String, Object> task : tasklist) {
            if (!"sub".equalsIgnoreCase(getStrVal(task, "type"))) {
                task.put("toSync", "false");
                continue;
            }

            toSync = false;

            if (!"end".equalsIgnoreCase(getStrVal(task, "cmtaskstate")) && !"Closed".equalsIgnoreCase(getStrVal(task, "cmtaskstate"))) {
                //二支完成
                if ("21".equalsIgnoreCase(getStrVal(task, "taskcode")) && "Completed".equalsIgnoreCase(getStrVal(task, "taskstate"))) {
                    toSync = true;
                }
                //中止
                else if ("Exited".equalsIgnoreCase(getStrVal(task, "taskstate"))) {
                    toSync = true;
                }
                //一支完成，判断是否需要二支
                else if ("20".equalsIgnoreCase(getStrVal(task, "taskcode")) && "Completed".equalsIgnoreCase(getStrVal(task, "taskstate"))) {
                    String secPay = needSecPay(getStrVal(task, "mainInstanceId"), getStrVal(task, "inscomcode"));
                    //不需要二支
                    if (secPay != null && "0".equals(secPay)) {
                        toSync = true;
                    }
                }
            }

            if (toSync) {
                task.put("taskstatus", "异常");
            }
            task.put("toSync", String.valueOf(toSync));
        }

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("records", "10000");
        map.put("page", 1);
        map.put("total", num);
        map.put("rows", tasklist);
        JSONObject jsonObject = JSONObject.fromObject(map);//将map转为JSONObject对象
        return jsonObject.toString();//转为json型字符串
    }

    private String getStrVal(Map<String, Object> data, String key) {
        return data != null ? String.valueOf(data.get(key)) : null;
    }

    /**
     * 报价/核保任务成功推送工作流
     * 支付/二次支付
     */
    public String underWriteSuccess(String instanceid, String operator, String forname, String maininstanceid, String inscomcode) {
        Map<String, Object> params = new HashMap<String, Object>();
        Map<String, Object> data = new HashMap<String, Object>();
        params.put("userid", operator);
        String message = null;
        if ("18".equals(forname)) {
            if (StringUtil.isNotEmpty(maininstanceid) && StringUtil.isNotEmpty(inscomcode)) {
                //人工核保修改Orderstatus为待支付
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("taskid", maininstanceid);
                map.put("inscomcode", inscomcode);
                INSBOrder temp = insbOrderDao.selectOrderByTaskId(map);
                if (temp != null) {
                    temp.setOrderstatus("1");
                    insbOrderDao.updateById(temp);
                    LogUtil.info(maininstanceid + "," + instanceid + "," + inscomcode + "修改orderstatus为待支付(1)");
                }
            }

            params.put("taskName", "人工核保");
            params.put("processinstanceid", Integer.parseInt(instanceid));
            data.put("result", "3");
            params.put("data", data);
            JSONObject jsonb = JSONObject.fromObject(params);
            Map<String, String> par = new HashMap<String, String>();
            par.put("datas", jsonb.toString());
            WorkflowFeedbackUtil.setWorkflowFeedback(null, instanceid, forname, "Completed", params.get("taskName").toString(), WorkflowFeedbackUtil.manual_complete, operator);
            message = HttpClientUtil.doGet(WORKFLOW + "/process/completeSubTask", par);
        } else if ("8".equals(forname)) {
            params.put("taskName", "人工报价");
            params.put("processinstanceid", Integer.parseInt(instanceid));
            data.put("result", "3");
            params.put("data", data);
            JSONObject jsonb = JSONObject.fromObject(params);
            Map<String, String> par = new HashMap<String, String>();
            par.put("datas", jsonb.toString());
            WorkflowFeedbackUtil.setWorkflowFeedback(null, instanceid, forname, "Completed", params.get("taskName").toString(), WorkflowFeedbackUtil.manual_complete, operator);
            message = HttpClientUtil.doGet(WORKFLOW + "/process/completeSubTask", par);
        } else if ("7".equals(forname)) {
            params.put("taskName", "人工规则报价");
            params.put("processinstanceid", Integer.parseInt(instanceid));
            data.put("result", "3");
            data.put("gztorgway", "0");
            params.put("data", data);
            JSONObject jsonb = JSONObject.fromObject(params);
            Map<String, String> par = new HashMap<String, String>();
            par.put("datas", jsonb.toString());
            WorkflowFeedbackUtil.setWorkflowFeedback(null, instanceid, forname, "Completed", params.get("taskName").toString(), WorkflowFeedbackUtil.manual_complete, operator);
            message = HttpClientUtil.doGet(WORKFLOW + "/process/completeSubTask", par);
        } else if ("53".equals(forname)) {
            params.put("taskName", "平台查询");
            params.put("processinstanceid", Long.parseLong(instanceid));
            data.put("result", "1");
            data.put("ptisful", "1");
            appInsuredQuoteService.getPriceParamWay(data, maininstanceid, inscomcode, "1");
            params.put("data", data);
            JSONObject jsonb = JSONObject.fromObject(params);
            Map<String, String> par = new HashMap<String, String>();
            par.put("datas", jsonb.toString());
            WorkflowFeedbackUtil.setWorkflowFeedback(null, instanceid, forname, "Completed", params.get("taskName").toString(), WorkflowFeedbackUtil.manual_complete, operator);
            message = HttpClientUtil.doGet(WORKFLOW + "/process/completeSubTask", par);
        } else if ("38".equals(forname)) {
            String taskName = "核保查询";
            WorkflowFeedbackUtil.setWorkflowFeedback(null, instanceid, forname, "Completed", taskName, WorkflowFeedbackUtil.manual_complete, operator);
            interFaceService.TaskResultWriteBack(maininstanceid, inscomcode, operator, "true", taskName, "B", null, taskName, instanceid);
            message = "{\"message\":\"success\"}";
        }

        Map<String, String> map = new HashMap<String, String>();
        if (message != null && !("").equals(message)) {
            JSONObject result = JSONObject.fromObject(message);
            if ("success".equals(result.getString("message"))) {
                map.put("status", "success");
                map.put("msg", "操作成功!!!");
            } else if ("fail".equals(result.getString("message"))) {
                map.put("status", "fail");
                map.put("msg", "工作流操作失败!!");
            }
        } else {
            map.put("status", "fail");
            map.put("msg", "调用工作流,无返回值,失败!!");
        }
        return JSONObject.fromObject(map).toString();
    }

    //判断是否需要二次支付： 1需要二次支付，0不需要二次支付
    public String needSecPay(String maininstanceid, String inscomcode) {
        if (StringUtil.isEmpty(maininstanceid) || StringUtil.isEmpty(inscomcode)) return null;

        INSBOrder insbOrder = new INSBOrder();
        insbOrder.setTaskid(maininstanceid);
        insbOrder.setPrvid(inscomcode);
        insbOrder = insbOrderDao.selectOne(insbOrder);

        if (insbOrder != null && insbOrder.getId() != null) {
            Map<String, String> insbOrderMap = new HashMap<String, String>();
            insbOrderMap.put("taskid", insbOrder.getTaskid());
            insbOrderMap.put("companyid", insbOrder.getPrvid());
            INSBQuoteinfo insbQuoteinfo = insbQuoteinfoDao.getByTaskidAndCompanyid(insbOrderMap);

            if (insbQuoteinfo != null) {
                INSBOrderpayment insbOrderpayment = new INSBOrderpayment();
                insbOrderpayment.setOrderid(insbOrder.getId());
                insbOrderpayment.setSubinstanceid(insbQuoteinfo.getWorkflowinstanceid());
                insbOrderpayment.setTaskid(maininstanceid);
                insbOrderpayment = insbOrderpaymentDao.selectOne(insbOrderpayment);

                //获取数据
                if (insbOrderpayment != null && insbOrderpayment.getPaychannelid() != null) {
                    LogUtil.info("是否需要二支，订单号:" + insbOrder.getOrderno() + " provideid:" + insbQuoteinfo.getInscomcode() + " deptid:" + insbQuoteinfo.getDeptcode() + " paychannelId:" + insbOrderpayment.getPaychannelid());
                    INSBPaychannelmanager insbPaychannelmanager = new INSBPaychannelmanager();
                    insbPaychannelmanager.setProviderid(insbQuoteinfo.getInscomcode());
                    insbPaychannelmanager.setDeptid(insbQuoteinfo.getDeptcode());
                    insbPaychannelmanager.setPaychannelid(insbOrderpayment.getPaychannelid());
                    List<INSBPaychannelmanager> pcmList = insbPaychannelmanagerDao.selectList(insbPaychannelmanager);

                    if (pcmList.isEmpty()) {
                        //没有网点数据则获取平台数据
                        String ptdeptcode = inscDeptService.getPingTaiDeptId(insbQuoteinfo.getDeptcode());
                        insbPaychannelmanager.setDeptid(ptdeptcode);
                        pcmList = insbPaychannelmanagerDao.selectList(insbPaychannelmanager);
                    }

                    if (pcmList.size() != 0 && "1".equals(pcmList.get(0).getCollectiontype())) {
                        return "0";// 1需要二次支付，0不需要二次支付
                    } else {
                        return "1";
                    }
                } else {
                    return "0";
                }
            } else {
                return "0";
            }
        } else {
            return null;
        }
    }

    /**
     * 支付/二次支付  成功推送工作流
     */
    public String paySuccess(String instanceid, String operator, String forname, String maininstanceid, String inscomcode) {
        if ("20".equals(forname)) {
            String secPay = needSecPay(maininstanceid, inscomcode);

            if (secPay != null) {
                Map<String, String> map2 = new HashMap<String, String>();
                Map<String, Object> map1 = new HashMap<String, Object>();
                Map<String, Object> map = new HashMap<String, Object>();

                map1.put("issecond", secPay);
                map1.put("acceptway", JSONObject.fromObject(workflowmainService.getContractcbType(maininstanceid, inscomcode, "0", "contract")).getString("quotecode"));
                map.put("data", map1);
                map.put("userid", operator);
                map.put("processinstanceid", Long.parseLong(instanceid));
                map.put("taskName", "支付");
                JSONObject jsonObject = JSONObject.fromObject(map);
                Map<String, String> params = new HashMap<String, String>();
                params.put("datas", jsonObject.toString());

                WorkflowFeedbackUtil.setWorkflowFeedback(maininstanceid, instanceid, forname, "Completed", "支付", WorkflowFeedbackUtil.manual_complete, operator);

                String retStr = HttpClientUtil.doGet(WORKFLOW + "/process/completeSubTask", params);
                LogUtil.info(maininstanceid + "," + inscomcode + "推支付工作流结果:" + retStr);

                if (retStr != null && !("").equals(retStr)) {
                    JSONObject obj = JSONObject.fromObject(retStr);
                    if (obj.get("message").equals("success")) {
                        map2.put("status", "success");
                        map2.put("msg", "操作成功!!!");
                    } else if ("fail".equals(obj.getString("message"))) {
                        map2.put("status", "fail");
                        map2.put("msg", "工作流操作失败!!");
                    }
                } else {
                    map2.put("status", "fail");
                    map2.put("msg", "调用工作流,无返回值,失败!!");
                }
                return JSONObject.fromObject(map2).toString();
            }

        } else if ("21".equals(forname)) {

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("taskid", maininstanceid);
            map.put("inscomcode", inscomcode);
            //二次支付改Orderstatus为打单
            INSBOrder temp = insbOrderDao.selectOrderByTaskId(map);
            temp.setOrderstatus("2");
            insbOrderDao.updateById(temp);

            //如果首单时间为空则更新代理人首单时间和taskid
            INSBAgent agent = new INSBAgent();
            agent.setJobnum(temp.getAgentcode());
            agent = insbAgentDao.selectOne(agent);
            if (null != agent && null == agent.getFirstOderSuccesstime()) {
                agent.setFirstOderSuccesstime(new Date());
                agent.setTaskid(temp.getTaskid());
                insbAgentDao.updateById(agent);
            }
            Map<String, String> par = new HashMap<String, String>();
            Map<String, Object> params = new HashMap<String, Object>();
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("acceptway", JSONObject.fromObject(workflowmainService.getContractcbType(maininstanceid, inscomcode, "0", "contract")).getString("quotecode"));
            params.put("userid", operator);
            params.put("processinstanceid", Integer.parseInt(instanceid));
            params.put("taskName", "二次支付确认");
            params.put("result", "3");
            params.put("data", data);
            JSONObject bo = JSONObject.fromObject(params);
            par.put("datas", bo.toString());

            WorkflowFeedbackUtil.setWorkflowFeedback(maininstanceid, instanceid, forname, "Completed", "二次支付确认", WorkflowFeedbackUtil.manual_complete, operator);

            String message = HttpClientUtil.doGet(WORKFLOW + "/process/completeSubTask", par);
            LogUtil.info(maininstanceid + "," + inscomcode + "推二次支付确认工作流结果：" + message);

            if (message != null && !("").equals(message)) {
                JSONObject result = JSONObject.fromObject(message);
                if ("success".equals(result.getString("message"))) {
                    data.put("status", "success");
                    data.put("msg", "操作成功!!!");
                } else if ("fail".equals(result.getString("message"))) {
                    data.put("status", "fail");
                    data.put("msg", "工作流操作失败!!");
                }
            } else {
                data.put("status", "fail");
                data.put("msg", "调用工作流,无返回值,失败!!");
            }
            return JSONObject.fromObject(data).toString();
        }
        return null;
    }

    /**
     * 承保打单成功,推送工作流
     *
     * @param instanceid
     * @param operator
     * @return
     */
    @Override
    public String undwrtSuccess(String instanceid, String operator, String formane, String inscomcode) {
        String message = insbUnderwritingService.undwrtSuccess(instanceid,operator,formane,inscomcode);
        Map<String, String> map = new HashMap<String, String>();
        if( "success".equals(message) ) {
            map.put("status", "success");
            map.put("msg", "操作成功!!!");
        } else {
            map.put("status", "fail");
            map.put("msg", "操作失败："+message);
        }
        return JSONObject.fromObject(map).toString();
    }

    /**
     * 承保打单配送成功,推送工作流
     *
     * @param instanceid
     * @param operator
     * @return
     */
    @Override
    public String unwrtDeliverySuccess(String instanceid, String operator, String formane, String inscomcode) {
        INSBOrderdelivery orderdelivery = getDeliveryInfo(instanceid, inscomcode, "0"); //默认自取
        String message = insbOrderService.undwrtDeliverySuccess(instanceid, operator, inscomcode,
                orderdelivery.getDelivetype(), orderdelivery.getLogisticscompany(), orderdelivery.getTracenumber(), formane);

        Map<String, String> map = new HashMap<String, String>();
        if (message != null && !("").equals(message)) {
            JSONObject jo = JSONObject.fromObject(message);
            String status = jo.getString("status");
            if ("success".equals(status) || "error3".equals(status)) {
                map.put("status", "success");
                map.put("msg", "操作成功!");
            } else if ("fail".equals(status)) {
                map.put("status", "fail");
                map.put("msg", "操作失败："+jo.getString("msg"));
            } else if("error1".equals(status)){
                map.put("status", "fail");
                if("23".equals(formane)){
                    map.put("msg", "打单配送失败,打单环节失败！");
                }else{
                    map.put("msg", "承保打单配送失败,承保打单环节失败！");
                }
            }else if("error2".equals(status)){
                map.put("status", "fail");
                if("23".equals(formane)){
                    map.put("msg", "打单配送失败,配送环节失败!");
                }else{
                    map.put("msg", "承保打单配送失败,配送环节失败!");
                }
            }else if("taskcoderror".equals(status)){
                map.put("status", "fail");
                map.put("msg", "系统错误,稍后再试!");
            }
        } else {
            map.put("status", "fail");
            map.put("msg", "操作失败,无返回值");
        }
        return JSONObject.fromObject(map).toString();
    }

    /**
     * 配送成功,推送工作流
     *
     * @param instanceid
     * @param operator
     * @return
     */
    @Override
    public String deliverySuccess(String instanceid, String operator, String inscomcode) {
        String message = null;
        try {
            INSBOrderdelivery orderdelivery = getDeliveryInfo(instanceid, inscomcode, "0"); //默认自取
            message = insbOrderService.getDeliverySuccess(instanceid, operator, inscomcode,
                    orderdelivery.getDelivetype(), orderdelivery.getLogisticscompany(), orderdelivery.getTracenumber());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Map<String, String> map = new HashMap<String, String>();
        if (StringUtil.isNotEmpty(message)) {
            if ("success".equals(message)) {
                map.put("status", "success");
                map.put("msg", "操作成功!!!");
            } else {
                map.put("status", "fail");
                map.put("msg", "操作失败!!");
            }
        } else {
            map.put("status", "fail");
            map.put("msg", "操作失败,无返回值");
        }
        return JSONObject.fromObject(map).toString();
    }

    /**
     * 获取配送信息
     */
    public INSBOrderdelivery getDeliveryInfo(String instanceid, String inscomcode, String defaultDeltype) {
        INSBOrderdelivery orderdelivery = new INSBOrderdelivery();
        orderdelivery.setTaskid(instanceid);
        orderdelivery.setProviderid(inscomcode);
        orderdelivery =  insbOrderdeliveryService.queryOne(orderdelivery);

        if (orderdelivery == null) {
            orderdelivery = new INSBOrderdelivery();
            orderdelivery.setDelivetype(defaultDeltype);
        }

        return orderdelivery;
    }

    /**
     * 支付失败
     */
    public String underwritefail(String instanceid) {
        LogUtil.info("关闭支付---start---instanceId=" + instanceid);
        String message = WorkFlowUtil.abortProcessByIdWorkflow(instanceid, "sub", "back");
        Map<String, String> map = new HashMap<String, String>();
        if (message != null && !("").equals(message)) {
            JSONObject result = JSONObject.fromObject(message);
            if ("success".equals(result.getString("message"))) {
                map.put("status", "success");
                map.put("msg", "[支付失败]操作成功!!!");
            } else if ("fail".equals(result.getString("message"))) {
                map.put("status", "fail");
                map.put("msg", "工作流操作失败!!");
            }
        } else {
            map.put("status", "fail");
            map.put("msg", "调用工作流,无返回值,失败!!");
        }
        return JSONObject.fromObject(map).toString();
    }

    /**
     * 人工规则报价强转人工
     */
    public String pushManualInterface(String instanceId, String operator) {
        Map<String, String> params = new HashMap<String, String>();
        Map<String, Object> datas = new HashMap<String, Object>();
        Map<String, Object> data = new HashMap<String, Object>();
        datas.put("taskName", "人工规则报价");
        data.put("result", "1");
        data.put("gztorgway", "1");
        datas.put("data", data);//数据
        datas.put("processinstanceid", Integer.parseInt(instanceId));//流程实例ID
        datas.put("userid", operator);//userid
        JSONObject datasJSON = JSONObject.fromObject(datas);
        params.put("datas", datasJSON.toString());
        return HttpClientUtil.doGet(WORKFLOW + "/process/completeSubTask", params);
    }

    /**
     * 调用退回修改工作流接口
     */
    @Override
    public String callBackInterface(String instanceid, String operator, String forname) {
        Map<String, String> map = new HashMap<String, String>();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userid", operator);
        params.put("processinstanceid", Integer.parseInt(instanceid));

        if ("8".equals(forname)) {
            params.put("taskName", "人工报价");
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("result", "2");  //报价结果，成功是3
            params.put("data", data);
            JSONObject jsonb = JSONObject.fromObject(params);
            Map<String, String> par = new HashMap<String, String>();
            par.put("datas", jsonb.toString());

            String message = HttpClientUtil.doGet(WORKFLOW + "/process/completeSubTask", par);

            if (StringUtil.isNotEmpty(message)) {
                JSONObject result = JSONObject.fromObject(message);
                if ("success".equals(result.getString("message"))) {
                    map.put("status", "success");
                    map.put("msg", "操作成功!!!");
                } else if ("fail".equals(result.getString("message"))) {
                    map.put("status", "fail");
                    map.put("msg", "工作流操作失败!!");
                }
            } else {
                map.put("status", "fail");
                map.put("msg", "调用工作流,无返回值,失败!!");
            }
        } else if ("18".equals(forname)) {
            String result = insbOrderService.manualUnderWritingBack(instanceid, operator);

            if ("success".equalsIgnoreCase(result)) {
                map.put("status", "success");
                map.put("msg", "操作成功!!!");
            } else {
                map.put("status", "fail");
                map.put("msg", "工作流操作失败!!");
            }
        }

        return JSONObject.fromObject(map).toString();
    }

    /**
     * 精灵或者edi
     *
     * @param ruleResult
     * @param processinstanceid
     * @param userid
     * @param taskName
     * @return
     */
    public String completeTaskWorkflow(String ruleResult, String processinstanceid, String userid, String taskName, String taskCode) {
        String path = WORKFLOW + "/process/completeSubTask";
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("result", ruleResult);

        //核保
        if ("16".equals(taskCode) || "17".equals(taskCode) || "40".equals(taskCode) || "41".equals(taskCode) || "38".equals(taskCode)) {
            map1.put("underwriteway", "3");
        }else{
        	map1.put("acceptway", "3");
        	map1.put("getpriceway", "6");
        	map1.put("rulesDecision", true);
        }

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("data", map1);
        map.put("userid", userid);
        map.put("taskName", taskName);
        map.put("processinstanceid", Long.parseLong(processinstanceid));

        JSONObject jsonObject = JSONObject.fromObject(map);
        Map<String, String> params = new HashMap<String, String>();
        params.put("datas", jsonObject.toString());
        LogUtil.info(taskName + "转人工推工作流=" + jsonObject.toString());

        String result = HttpClientUtil.doGet(path, params);
        LogUtil.info(processinstanceid + "在completeTaskWorkflow调用工作流返回：" + result);

        return result;
    }

    private String buildRedisKeys(String instanceid) {
        return WorkflowAttachHandleUtil.module + ":" + instanceid + ":*";
    }

    public void autosyncWorkflowStatus(String operator) {
    	//组织入参
    	Date time = DateUtil.addDay(new Date(), -1);
    	DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    	Map<String,Object> param= new HashMap<String,Object>();
    	param.put("taskcreatetimeup",format.format(time)+" 00:00:00");
    	param.put("taskcreatetimedown", format.format(time)+" 24:00:00");
    	LogUtil.info("在autosyncWorkflowStatus处理业务，入参：%s,出参：%s",param.toString(),syncWorkflowStatus(insbManualPushFlowDao.queryErrorStateTasks(param),operator).toString());
    }
    
    public Map<String, String> syncWorkflowStatus(List<ManualPushFlowVo> manualPushFlowVoList, String operator) {
        Map<String, String> result = new HashMap<>();
        result.put("status", "success");
        result.put("msg", "同步成功");

        if (manualPushFlowVoList == null || manualPushFlowVoList.isEmpty()) {
            result.put("status", "fail");
            result.put("msg", "非法请求");
            return result;
        }

        String type = null, instanceid = null;
        for (ManualPushFlowVo vo : manualPushFlowVoList) {
            LogUtil.info("同步：" + JSONObject.fromObject(vo).toString());
            type = vo.getType();
            instanceid = vo.getInstanceid();
            boolean isMain = "main".equals(type);

            if (StringUtil.isEmpty(instanceid)) {
                if (isMain) {
                    instanceid = vo.getMaininstanceid();
                } else {
                    instanceid = vo.getSubinstanceid();
                }
            }
            if (StringUtil.isEmpty(instanceid)) {
                LogUtil.error(vo.getCmtaskname() + "," + vo.getTaskname() + "任务号为空");
                continue;
            }

            //查找redis上面这单所有的操作记录
            String keys = buildRedisKeys(instanceid);
            Set<String> redisKeys = CMRedisClient.getInstance().keys(WorkflowAttachHandleUtil.dbIndex, keys);
            List<Map<String, Object>> records = new ArrayList<>();

            //如果有redis记录
            if (redisKeys != null && !redisKeys.isEmpty()) {
                //根据redis记录key，获取记录详细数据
                for (String key : redisKeys) {
                    LogUtil.info("redis同步数据：" + key);
                    key = key.replace(WorkflowAttachHandleUtil.module + ":", "");
                    Map<String, Object> record = CMRedisClient.getInstance().get(WorkflowAttachHandleUtil.dbIndex, WorkflowAttachHandleUtil.module, key, Map.class);

                    if (record != null && !record.isEmpty()) {
                        records.add(record);
                    }
                }

                if (!records.isEmpty()) {
                    sortRecords(records);

                    for (Map<String, Object> record : records) {
                        try {
                            playback(record);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            //验证执行情况
            if (isMain) {
                Map<String, Object> mr = insbManualPushFlowDao.queryMainTaskByMaininstanceid(instanceid);

                if (mr == null || mr.isEmpty()) {
                    LogUtil.error(instanceid + "数据丢失");
                } else if (StringUtil.isEmpty(mr.get("status"))) {
                    LogUtil.error(instanceid + "工作流数据丢失");
                } else if (StringUtil.isEmpty(mr.get("taskstate"))) {
                    LogUtil.error(instanceid + "流程数据丢失");
                } else if (String.valueOf(mr.get("taskcode")).equalsIgnoreCase(String.valueOf(mr.get("formName"))) && !String.valueOf(mr.get("taskstate")).equalsIgnoreCase(String.valueOf(mr.get("status")))) {
                    //工作流已经终止
                    if ("Exited".equalsIgnoreCase(String.valueOf(mr.get("status")))) {
                        WorkFlow4TaskModel taskModel = buildTaskModel(vo.getMaininstanceid(), null, null, "关闭", "37", "Closed", 2);
                        workflowDataService.endTaskFromWorkFlow(taskModel);
                    } else if ("Completed".equalsIgnoreCase(String.valueOf(mr.get("status")))) {
                        //完成当前节点
                        WorkflowFeedbackUtil.setWorkflowFeedback(vo.getMaininstanceid(), null, vo.getCmtaskcode(), "Completed", vo.getCmtaskname(), WorkflowFeedbackUtil.manual_complete, operator);
                        WorkFlow4TaskModel taskModel = buildTaskModel(vo.getMaininstanceid(), null, null, vo.getCmtaskname(), vo.getCmtaskcode(), "Completed", 1);
                        workflowDataService.endTaskFromWorkFlow(taskModel);
                    }
                } else if (!String.valueOf(mr.get("taskcode")).equalsIgnoreCase(String.valueOf(mr.get("formName")))) {//未同步
                    //完成当前节点
                    WorkflowFeedbackUtil.setWorkflowFeedback(vo.getMaininstanceid(), null, vo.getCmtaskcode(), "Completed", vo.getCmtaskname(), WorkflowFeedbackUtil.manual_complete, operator);
                    WorkFlow4TaskModel taskModel = buildTaskModel(vo.getMaininstanceid(), null, null, vo.getCmtaskname(), vo.getCmtaskcode(), "Completed", 1);
                    workflowDataService.endTaskFromWorkFlow(taskModel);

                    //直接插入工作流节点
                    taskModel = buildTaskModel(vo.getMaininstanceid(), null, null, vo.getTaskname(), vo.getTaskcode(), "Ready", 1);
                    workflowDataService.startTaskFromWorlFlow(taskModel);

                }
            } else {
                Map<String, Object> sr = insbManualPushFlowDao.querySubTaskBySubinstanceid(instanceid);
                //未同步
                if (sr == null || sr.isEmpty()) {
                    LogUtil.error(instanceid + "数据丢失");
                } else if (StringUtil.isEmpty(sr.get("status"))) {
                    LogUtil.error(instanceid + "工作流数据丢失");
                } else if (StringUtil.isEmpty(sr.get("taskstate"))) {
                    LogUtil.error(instanceid + "流程数据丢失");
                } else if (String.valueOf(sr.get("taskcode")).equalsIgnoreCase(String.valueOf(sr.get("formName")))) {
                    //是否需要判断结束子流程
                    boolean checkFinishSub = false;

                    if (!String.valueOf(sr.get("taskstate")).equalsIgnoreCase(String.valueOf(sr.get("status")))) {
                        //工作流已经中止
                        if ("Exited".equalsIgnoreCase(String.valueOf(sr.get("status")))) {
                            WorkFlow4TaskModel taskModel = buildTaskModel(vo.getMaininstanceid(), vo.getSubinstanceid(), vo.getInscomcode(), "关闭", "37", "Closed", 2);
                            workflowDataService.endTaskFromWorkFlow(taskModel);
                        } else if ("Completed".equalsIgnoreCase(String.valueOf(sr.get("status")))) {
                            //完成当前节点
                            WorkflowFeedbackUtil.setWorkflowFeedback(vo.getMaininstanceid(), vo.getSubinstanceid(), vo.getCmtaskcode(), "Completed", vo.getCmtaskname(), WorkflowFeedbackUtil.manual_complete, operator);
                            WorkFlow4TaskModel taskModel = buildTaskModel(vo.getMaininstanceid(), vo.getSubinstanceid(), vo.getInscomcode(), vo.getCmtaskname(), vo.getCmtaskcode(), "Completed", 1);
                            workflowDataService.endTaskFromWorkFlow(taskModel);
                            //需要判断结束子流程
                            checkFinishSub = true;
                        }
                    } else if ("Completed".equalsIgnoreCase(String.valueOf(sr.get("taskstate")))) {
                        checkFinishSub = true;
                    }

                    if (checkFinishSub && StringUtil.isNotEmpty(vo.getMaininstanceid())) {
                        Map<String, Object> task = insbManualPushFlowDao.getTaskByProcessinstanceid(vo.getMaininstanceid());
                        //子流程没有“结束”数据
                        if (task != null && !"1".equals(task.get("formName")) && !"2".equals(task.get("formName"))) {
                            WorkFlow4TaskModel taskModel = buildTaskModel(vo.getMaininstanceid(), vo.getSubinstanceid(), vo.getInscomcode(), "结束", "33", "end", 2);
                            workflowDataService.endTaskFromWorkFlow(taskModel);
                        }
                    }

                } else if (!"19".equals(sr.get("taskcode")) && !String.valueOf(sr.get("taskcode")).equalsIgnoreCase(String.valueOf(sr.get("formName")))) {//未同步 且不是核保退回的情况需要强制同步
                    //完成当前节点
                    WorkflowFeedbackUtil.setWorkflowFeedback(vo.getMaininstanceid(), vo.getSubinstanceid(), vo.getCmtaskcode(), "Completed", vo.getCmtaskname(), WorkflowFeedbackUtil.manual_complete, operator);
                    WorkFlow4TaskModel taskModel = buildTaskModel(vo.getMaininstanceid(), vo.getSubinstanceid(), vo.getInscomcode(), vo.getCmtaskname(), vo.getCmtaskcode(), "Completed", 1);
                    workflowDataService.endTaskFromWorkFlow(taskModel);

                    //直接插入工作流节点
                    taskModel = buildTaskModel(vo.getMaininstanceid(), vo.getSubinstanceid(), vo.getInscomcode(), vo.getTaskname(), vo.getTaskcode(), "Ready", 1);
                    workflowDataService.startTaskFromWorlFlow(taskModel);
                }
            }
        }

        return result;
    }

    //重做
    private Object playback(Map<String, Object> record) {
        if (record == null || record.isEmpty()) {
            LogUtil.error("参数为空");
            return null;
        }

        String reqParamString = (String) record.get("reqParams");
        if (StringUtil.isEmpty(reqParamString)) {
            LogUtil.error("空的请求参数体:" + reqParamString);
            return null;
        }

        //重做CM的请求
        if ("cm".equals(record.get("source"))) {
            LogUtil.info("playback");
            Map<String, String> params = new HashMap<>(1);
            params.put("datas", reqParamString);
            return HttpClientUtil.doGet((String) record.get("reqService"), params, HttpClientUtil.CHARSET);
        }
        //重做工作流的请求
        else if ("wf".equals(record.get("source"))) {
            LogUtil.info("playback");
            String reqService = (String) record.get("reqService");
            if (reqService == null) {
                LogUtil.error("目标为空");
                return null;
            }

            boolean startTask = reqService.contains("/startTask");
            boolean endtask = reqService.contains("/endtask");
            if (!startTask && !endtask) {
                LogUtil.error("未支持的目标");
                return null;
            }

            Map<String, Object> reqParams = (Map<String, Object>) JSONObject.toBean(JSONObject.fromObject(reqParamString), Map.class);

            String dataflag = String.valueOf(reqParams.get("dataFlag"));
            int dataFlag = Integer.parseInt(StringUtil.isNotEmpty(dataflag) ? dataflag : "1");
            WorkFlow4TaskModel taskModel = buildTaskModel(String.valueOf(reqParams.get("mainInstanceId")), String.valueOf(reqParams.get("subInstanceId")),
                    String.valueOf(reqParams.get("providerId")), String.valueOf(reqParams.get("taskName")), String.valueOf(reqParams.get("taskCode")),
                    String.valueOf(reqParams.get("taskStatus")), dataFlag);

            if (startTask) {
                workflowDataService.startTaskFromWorlFlow(taskModel);
            } else {
                workflowDataService.endTaskFromWorkFlow(taskModel);
            }

            return true;

        } else {
            LogUtil.error("非法来源数据");
            return null;
        }
    }

    private WorkFlow4TaskModel buildTaskModel(String mainInstanceId, String subInstanceId, String providerId, String taskName, String taskCode, String taskStatus, int dataFlag) {
        WorkFlow4TaskModel taskModel = new WorkFlow4TaskModel();
        taskModel.setMainInstanceId(mainInstanceId);
        taskModel.setSubInstanceId(subInstanceId);
        taskModel.setProviderId(providerId);
        taskModel.setTaskName(taskName);
        taskModel.setTaskCode(taskCode);
        taskModel.setTaskStatus(taskStatus);
        taskModel.setDataFlag(dataFlag);
        return taskModel;
    }

    //根据记录时间排序，最先的记录在前面
    private void sortRecords(List<Map<String, Object>> records) {
        if (records == null || records.isEmpty()) return;

        records.sort(new Comparator<Map<String, Object>>() {
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                if (o1 == null && o2 == null) return 0;
                if (o1 == null) return -1;
                if (o2 == null) return 1;

                try {
                    long time1 = Long.parseLong(String.valueOf(o1.get("time")));
                    long time2 = Long.parseLong(String.valueOf(o2.get("time")));
                    return time1 > time2 ? 1 : (time1 < time2 ? -1 : 0);
                } catch (Exception e) {
                    e.printStackTrace();
                    return 0;
                }
            }
        });
    }
}