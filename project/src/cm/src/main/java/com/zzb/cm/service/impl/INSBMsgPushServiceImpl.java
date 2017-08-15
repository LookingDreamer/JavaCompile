package com.zzb.cm.service.impl;

import com.cninsure.core.tools.util.ValidateUtil;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.cninsure.core.utils.UUIDUtils;
import com.cninsure.system.dao.INSCMessageDao;
import com.cninsure.system.entity.INSCMessage;
import com.com.baoxian.push.HttpPush;
import com.com.baoxian.push.model.PushLog;
import com.zzb.cm.dao.INSBCarinfohisDao;
import com.zzb.cm.dao.INSBQuoteinfoDao;
import com.zzb.cm.dao.INSBQuotetotalinfoDao;
import com.zzb.cm.entity.INSBCarinfohis;
import com.zzb.cm.entity.INSBQuoteinfo;
import com.zzb.cm.entity.INSBQuotetotalinfo;
import com.zzb.cm.service.INSBMsgPushService;
import com.zzb.cm.service.INSBQuoteinfoService;
import com.zzb.conf.dao.INSBWorkflowmaintrackDao;
import com.zzb.conf.entity.INSBProvider;
import com.zzb.conf.entity.INSBWorkflowmaintrack;
import com.zzb.conf.service.INSBProviderService;
import com.zzb.model.WorkFlow4TaskModel;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liwucai on 2017/2/28 17:32.
 */
@Service
public class INSBMsgPushServiceImpl implements INSBMsgPushService {
    @Resource
    private INSBQuoteinfoService insbQuoteinfoService;
    @Resource
    private INSBQuotetotalinfoDao insbQuotetotalinfoDao;
    @Resource
    private ThreadPoolTaskExecutor taskthreadPool4workflow;
    @Resource
    private INSBProviderService insbProviderService;
    @Resource
    private INSBCarinfohisDao carinfohisDao;
    @Resource
    private INSCMessageDao messageDao;
    @Resource
    private INSBWorkflowmaintrackDao insbWorkflowmaintrackDao;
    @Resource
    private INSBQuotetotalinfoDao quotetotalinfoDao;
    @Resource
    private INSBQuoteinfoDao insbQuoteinfoDao;

    public void sendMsg(WorkFlow4TaskModel dataModel) {
        //发消息 核保成功
        sendInsureSuccessMsg(dataModel);

        //发支付消息
        sendPaySuccessMsg(dataModel);

        //发打单成功消息
        sendPrintTicketSuccessMsg(dataModel);
    }

    @Override
    public void sendInsureSuccessMsg(WorkFlow4TaskModel dataModel) {
        //发消息 核保成功 : taskcode=18、40、41， taskstate=Completed，因为精灵自动核保完成不能确认成功或失败，所以在生成支付任务时确认为核保成功
        // 20 支付
        if( ("20".equals(dataModel.getTaskCode()) )
                && ("Ready".equals(dataModel.getTaskStatus()) || "Reserved".equals(dataModel.getTaskStatus()))
                &&!StringUtil.isEmpty(dataModel.getSubInstanceId())){
            try{
                String taskid=dataModel.getMainInstanceId();
                INSBQuoteinfo insbQuoteinfo=insbQuoteinfoService.getQuoteinfoByWorkflowinstanceid(dataModel.getSubInstanceId());
                String inscomcode=insbQuoteinfo.getInscomcode();

                //获取报价区域
                INSBQuotetotalinfo insbQuotetotalinfo = new INSBQuotetotalinfo();
                insbQuotetotalinfo.setTaskid(taskid);
                INSBQuotetotalinfo quotetotalinfo = insbQuotetotalinfoDao.selectOne(insbQuotetotalinfo);
                final String account = quotetotalinfo.getAgentnum();

                taskthreadPool4workflow.execute(new Runnable(){
                    @Override
                    public void run() {
                        try{
                            LogUtil.info("生成支付（即：核保成功）,开始组装推送消息，taskid=" + taskid + ",inscomcode=" + inscomcode
                                    + " ,taskCode = " + dataModel.getTaskCode() + " ,taskStatus = " + dataModel.getTaskStatus());
                            //  "content": {"id":"44444","title":"测试","msg":"不告诉你消息内容","sendTime":"20161129",
                            // "type":"0","action":"0","taskId":"1852985","url":"","quoteStatue":""}
                            //获取供应商id和名称
                            INSBProvider provider = insbProviderService.queryByPrvcode(inscomcode);
                            if(provider == null){
                                String msg = "生成支付（即：核保成功）, :taskid=" + taskid + ",inscomcode=" + inscomcode + ",无 供应商 信息";
                                throw new Exception(msg);
                            }

                            //获取车牌
                            INSBCarinfohis carinfohisParam = new INSBCarinfohis();
                            carinfohisParam.setTaskid(taskid);
                            carinfohisParam.setInscomcode(inscomcode);
                            INSBCarinfohis carinfohis = carinfohisDao.selectOne(carinfohisParam);
                            String carLicenseno = org.springframework.util.StringUtils.isEmpty(carinfohis.getCarlicenseno()) ? "新车未上牌" : carinfohis.getCarlicenseno();

                            Map<String, Object> paraMap = new HashMap<String, Object>();
                            paraMap.put("taskid", taskid);
                            paraMap.put("inscomcode", inscomcode);
                            //获取被保人姓名
                            String insbpersonName = messageDao.getInsbpersonName(paraMap);
                            insbpersonName = insbpersonName==null?"":insbpersonName;

                            //获取金额
                            Double totalepremium = messageDao.getTotalepremium (paraMap);
                            totalepremium = totalepremium == null ? 0 : totalepremium;
                            //获取微信号
                            String openId = messageDao.getOpenId(account);

                            //推送
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
//							String msgId = taskid + dataModel.getSubInstanceId() + account + sdf.format(new Date()).substring(14);
                            String msgIdAndroid = UUIDUtils.create();
                            PushLog pushLog = new PushLog(carLicenseno, inscomcode, provider.getPrvshotname(), msgIdAndroid, taskid, PushLog.AllPlat,
                                    "tencent", "核保成功", dataModel.getTaskCode(), "0", "0", "1", "cninsure.net.zhangzhongbao.XWalkMainActivity",
                                    PushLog.quoteUrl + taskid, insbpersonName, totalepremium, openId);
                            HttpPush.singleAccountPushLog(pushLog, account);
                            if(ValidateUtil.getConfigValue("pushmsg.url") != null){
                                LogUtil.info("生成支付（即：核保成功）,开始进行消息推送，taskid=" + taskid + ",inscomcode=" + inscomcode
                                        + " ,taskCode = "+ dataModel.getTaskCode() +" ,taskStatus = "+ dataModel.getTaskStatus() );
                                HttpPush.push(pushLog);
                                INSCMessage msg = new INSCMessage();
                                HttpPush.setINSCMessage(msg, pushLog, msgIdAndroid);
                                messageDao.insert(msg);

                            }
                        }catch(Exception e){
                            LogUtil.info("生成支付（即：核保成功）调用核保成功 发消息接口异常,taskid=" + taskid + ",inscomcode=" + inscomcode+", message = "+e.getMessage());
                            e.printStackTrace();
                        }
                    }
                });
            }catch(Exception e){
                e.printStackTrace();
            }
        }

    }

    @Override
    public void sendPaySuccessMsg(WorkFlow4TaskModel dataModel) {
        //在代理人支付成功后，就要发支付成功消息，分成两种情况，只有一支和有一、二支
        //1、只有一支，可以在承保开始阶段发送，但需要过滤掉有二支的任务
        //2、有一支和二支的情况，需要在二支开始的时候就发送支付成功的消息

        //有一支和二支，需要在二支开始的时候就发送支付成功的消息  21 二次支付， taskstate 为 Ready或者Reserved
        if("21".equals(dataModel.getTaskCode())
                && ("Ready".equals(dataModel.getTaskStatus()) || "Reserved".equals(dataModel.getTaskStatus()))){
            try{
                String taskid=dataModel.getMainInstanceId();
//				INSBQuoteinfo insbQuoteinfo=insbQuoteinfoService.getQuoteinfoByWorkflowinstanceid(dataModel.getSubInstanceId());
//				String inscomcode=insbQuoteinfo.getInscomcode();
                String inscomcode = dataModel.getProviderId();

                //获取报价区域
                INSBQuotetotalinfo insbQuotetotalinfo = new INSBQuotetotalinfo();
                insbQuotetotalinfo.setTaskid(taskid);
                INSBQuotetotalinfo quotetotalinfo = insbQuotetotalinfoDao.selectOne(insbQuotetotalinfo);
                String account = quotetotalinfo.getAgentnum();

                taskthreadPool4workflow.execute(new Runnable(){
                    @Override
                    public void run() {
                        try{
                            LogUtil.info("二次支付开始阶段,开始组装推送支付成功消息，taskid=" + taskid + ",inscomcode=" + inscomcode
                                    + " ,taskCode = "+ dataModel.getTaskCode() +" ,taskStatus = "+ dataModel.getTaskStatus() );
                            //获取供应商id和名称
                            INSBProvider provider = insbProviderService.queryByPrvcode(inscomcode);
                            if(provider == null){
                                String msg = "二次支付开始阶段,开始组装推送支付成功消息, :taskid=" + taskid + ",inscomcode=" + inscomcode + ",无 供应商 信息";
                                throw new Exception(msg);
                            }

                            //获取车牌
                            INSBCarinfohis carinfohisParam = new INSBCarinfohis();
                            carinfohisParam.setTaskid(taskid);
                            carinfohisParam.setInscomcode(inscomcode);
                            INSBCarinfohis carinfohis = carinfohisDao.selectOne(carinfohisParam);
                            if(carinfohis == null){
                                String msg = "二次支付开始阶段,开始组装推送支付成功消息, :taskid=" + taskid + ",inscomcode=" + inscomcode + ",查询 INSBCarinfohis 无信息";
                                throw new Exception(msg);
                            }
                            String carLicenseno = org.springframework.util.StringUtils.isEmpty(carinfohis.getCarlicenseno()) ? "新车未上牌" : carinfohis.getCarlicenseno();

                            Map<String, Object> paraMap = new HashMap<String, Object>();
                            paraMap.put("taskid", taskid);
                            paraMap.put("inscomcode", inscomcode);
                            //获取被保人姓名
                            String insbpersonName = messageDao.getInsbpersonName(paraMap);
                            insbpersonName = insbpersonName==null?"":insbpersonName;

                            //获取金额
                            Double totalepremium = messageDao.getTotalepremium (paraMap);
                            totalepremium = totalepremium == null ? 0 : totalepremium;
                            //获取微信号
                            String openId = messageDao.getOpenId(account);

                            //推送
                            String msgIdAndroid = UUIDUtils.create();
                            PushLog pushLog = new PushLog(carLicenseno, inscomcode, provider.getPrvshotname(), msgIdAndroid, taskid, PushLog.AllPlat,
                                    "tencent", "支付成功", dataModel.getTaskCode(), "0", "0", "1", "cninsure.net.zhangzhongbao.XWalkMainActivity",
                                    PushLog.orderDetailUrl + inscomcode + "-" + taskid, insbpersonName, totalepremium,openId);
                            HttpPush.singleAccountPushLog(pushLog, account);
                            if(ValidateUtil.getConfigValue("pushmsg.url") != null){
                                LogUtil.info("支付成功,开始进行消息推送，taskid=" + taskid + ",inscomcode=" + inscomcode
                                        + " ,taskCode = "+ dataModel.getTaskCode() +" ,taskStatus = "+ dataModel.getTaskStatus() );
                                HttpPush.push(pushLog);
                                INSCMessage msg = new INSCMessage();
                                HttpPush.setINSCMessage(msg, pushLog, msgIdAndroid);
                                messageDao.insert(msg);
                            }

                        }catch(Exception e){
                            LogUtil.info("调用支付成功 发消息接口异常,taskid="+taskid+",inscomcode="+inscomcode+ inscomcode+", message = "+e.getMessage());
                            e.printStackTrace();
                        }
                    }
                });
            }catch (Exception e){
                e.printStackTrace();
            }

        }


        //只有一支: 在开始承保阶段发消息  25 EDI承保；26精灵承保；27人工承保 ，taskstate 为 Ready或者Reserved
        if( ("25".equals(dataModel.getTaskCode()) || "26".equals(dataModel.getTaskCode()) || "27".equals(dataModel.getTaskCode()))
                && ("Ready".equals(dataModel.getTaskStatus()) || "Reserved".equals(dataModel.getTaskStatus()))){
            try{
                //滤掉有二支的任务
                String taskid=dataModel.getMainInstanceId();
                String inscomcode = dataModel.getProviderId();

                INSBWorkflowmaintrack maintrack = new INSBWorkflowmaintrack();
                maintrack.setInstanceid(taskid);
                List<INSBWorkflowmaintrack> maintracklist = insbWorkflowmaintrackDao.selectList(maintrack);
                List taskCodeList = Arrays.asList(maintracklist.stream().map(item -> item.getTaskcode()).toArray());

                if(CollectionUtils.isEmpty(taskCodeList)){
                    throw new Exception("任务轨迹查询异常：taskid=" + taskid + ",inscomcode=" + inscomcode
                            + " ,taskCode = "+ dataModel.getTaskCode() +" ,taskStatus = "+ dataModel.getTaskStatus());
                }


                Map<String, String> paraMap = new HashMap();
                paraMap.put("taskid", taskid);
                paraMap.put("inscomcode", inscomcode);
                int code21Count = insbWorkflowmaintrackDao.count21CodeByTaskIdAndInscomcode(paraMap);


                LogUtil.info("承保开始阶段，判断任务是否推送支付成功，taskid=" + taskid + ",inscomcode=" + inscomcode
                        + " ,taskCode = "+ dataModel.getTaskCode() +" ,taskStatus = "+ dataModel.getTaskStatus()
                        + " ,该任务二支列表查询" + (code21Count > 0 ? "不为空[不需要进行推送]" : "为空[需要进行推送]"));

                if(code21Count <= 0){

                    if( (dataModel.getTaskCode().equals("25")) ||
                            (dataModel.getTaskCode().equals("26") && !taskCodeList.contains("25")) ||
                            (dataModel.getTaskCode().equals("27") && !taskCodeList.contains("25") && !taskCodeList.contains("26"))
                            ){
                        //获取报价区域
                        INSBQuotetotalinfo insbQuotetotalinfo = new INSBQuotetotalinfo();
                        insbQuotetotalinfo.setTaskid(taskid);
                        INSBQuotetotalinfo quotetotalinfo = insbQuotetotalinfoDao.selectOne(insbQuotetotalinfo);
                        String account = quotetotalinfo.getAgentnum();

                        taskthreadPool4workflow.execute(new Runnable(){
                            @Override
                            public void run() {
                                try{
                                    LogUtil.info("承保开始阶段[只有一支],开始组装推送支付成功消息，taskid=" + taskid + ",inscomcode=" + inscomcode
                                            + " ,taskCode = "+ dataModel.getTaskCode() +" ,taskStatus = "+ dataModel.getTaskStatus() );
                                    //获取供应商id和名称
                                    INSBProvider provider = insbProviderService.queryByPrvcode(inscomcode);
                                    if(provider == null){
                                        String msg = "承保开始阶段[只有一支],开始组装推送支付成功消息, :taskid=" + taskid + ",inscomcode=" + inscomcode + ",无 供应商 信息";
                                        throw new Exception(msg);
                                    }

                                    //获取车牌
                                    INSBCarinfohis carinfohisParam = new INSBCarinfohis();
                                    carinfohisParam.setTaskid(taskid);
                                    carinfohisParam.setInscomcode(inscomcode);
                                    INSBCarinfohis carinfohis = carinfohisDao.selectOne(carinfohisParam);
                                    if(carinfohis == null){
                                        String msg = "承保开始阶段[只有一支],开始组装推送支付成功消息, :taskid=" + taskid + ",inscomcode=" + inscomcode + ",查询 INSBCarinfohis 无信息";
                                        throw new Exception(msg);
                                    }
                                    String carLicenseno = org.springframework.util.StringUtils.isEmpty(carinfohis.getCarlicenseno()) ? "新车未上牌" : carinfohis.getCarlicenseno();

                                    Map<String, Object> paraMap = new HashMap<String, Object>();
                                    paraMap.put("taskid", taskid);
                                    paraMap.put("inscomcode", inscomcode);
                                    //获取被保人姓名
                                    String insbpersonName = messageDao.getInsbpersonName(paraMap);
                                    insbpersonName = insbpersonName==null?"":insbpersonName;

                                    //获取金额
                                    Double totalepremium = messageDao.getTotalepremium (paraMap);
                                    totalepremium = totalepremium == null ? 0 : totalepremium;
                                    //获取微信号
                                    String openId = messageDao.getOpenId(account);

                                    //推送
                                    String msgIdAndroid = UUIDUtils.create();
                                    PushLog pushLog = new PushLog(carLicenseno, inscomcode, provider.getPrvshotname(), msgIdAndroid, taskid, PushLog.AllPlat,
                                            "tencent", "支付成功", dataModel.getTaskCode(), "0", "0", "1", "cninsure.net.zhangzhongbao.XWalkMainActivity",
                                            PushLog.orderDetailUrl + inscomcode + "-" + taskid, insbpersonName, totalepremium,openId);
                                    HttpPush.singleAccountPushLog(pushLog, account);
                                    if(ValidateUtil.getConfigValue("pushmsg.url") != null){
                                        LogUtil.info("支付成功,开始进行消息推送，taskid=" + taskid + ",inscomcode=" + inscomcode
                                                + " ,taskCode = "+ dataModel.getTaskCode() +" ,taskStatus = "+ dataModel.getTaskStatus() );
                                        HttpPush.push(pushLog);
                                        INSCMessage msg = new INSCMessage();
                                        HttpPush.setINSCMessage(msg, pushLog, msgIdAndroid);
                                        messageDao.insert(msg);
                                    }

                                }catch(Exception e){
                                    LogUtil.info("调用支付成功 发消息接口异常,taskid="+taskid+",inscomcode="+inscomcode+ inscomcode+", message = "+e.getMessage());
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }


    }

    @Override
    public void sendPrintTicketSuccessMsg(WorkFlow4TaskModel dataModel) {

        //  配送开始 (打单成功): taskcode=24, taskstate=Ready, Reserved
        //配送开始 有些单没有打单流程，有些单有，所以在配送时发消息
        if("24".equals(dataModel.getTaskCode())
                && ("Ready".equals(dataModel.getTaskStatus()) || "Reserved".equals(dataModel.getTaskStatus())) ){
            String processInstanceId=dataModel.getMainInstanceId();//主流程id
            String inscomcode=dataModel.getProviderId();//保险公司id

            //获取报价区域
            INSBQuotetotalinfo insbQuotetotalinfo = new INSBQuotetotalinfo();
            insbQuotetotalinfo.setTaskid(processInstanceId);
            INSBQuotetotalinfo quotetotalinfo = insbQuotetotalinfoDao.selectOne(insbQuotetotalinfo);
            String account = quotetotalinfo.getAgentnum();

            try{
                taskthreadPool4workflow.execute(new Runnable(){
                    @Override
                    public void run() {
                        try{
                            LogUtil.info(" 配送开始 (打单成功) , 开始组装推送消息,taskid=" + processInstanceId + ",inscomcode=" + inscomcode
                                    + " ,taskCode = "+ dataModel.getTaskCode() +" ,taskStatus = "+ dataModel.getTaskStatus() );
                            //insbAccountDetailsService.genCommissionAndRedPackets(processInstanceId, inscomcode);
                            //获取供应商id和名称
                            INSBProvider provider = insbProviderService.queryByPrvcode(inscomcode);
                            if(provider == null){
                                String msg = "配送开始 (打单成功), :taskid=" + processInstanceId + ",inscomcode=" + inscomcode + ",无供应商信息";
                                throw new Exception(msg);
                            }

                            //获取车牌
                            INSBCarinfohis carinfohisParam = new INSBCarinfohis();
                            carinfohisParam.setTaskid(processInstanceId);
                            carinfohisParam.setInscomcode(inscomcode);
                            INSBCarinfohis carinfohis = carinfohisDao.selectOne(carinfohisParam);
                            if(carinfohis == null){
                                String msg = "配送开始 (打单成功), :taskid=" + processInstanceId + ",inscomcode=" + inscomcode + ",查询 INSBCarinfohis ,无信息 "  ;
                                throw new Exception(msg);
                            }
                            String carLicenseno = org.springframework.util.StringUtils.isEmpty(carinfohis.getCarlicenseno()) ? "新车未上牌" : carinfohis.getCarlicenseno();

                            Map<String, Object> paraMap = new HashMap<String, Object>();
                            paraMap.put("taskid", processInstanceId);
                            paraMap.put("inscomcode", inscomcode);
                            //获取被保人姓名
                            String insbpersonName = messageDao.getInsbpersonName(paraMap);
                            insbpersonName = insbpersonName==null?"":insbpersonName;

                            //获取金额
                            Double totalepremium = messageDao.getTotalepremium (paraMap);
                            totalepremium = totalepremium == null ? 0 : totalepremium;

                            //获取微信号
                            String openId = messageDao.getOpenId(account);

                            //推送
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
//							String msgId = processInstanceId + dataModel.getSubInstanceId() + account + sdf.format(new Date()).substring(14);
                            String msgIdAndroid = UUIDUtils.create();
                            PushLog pushLog = new PushLog(carLicenseno, inscomcode, provider.getPrvshotname(), msgIdAndroid, processInstanceId, PushLog.AllPlat,
                                    "tencent", "承保打单成功", "24", "0", "0", "1", "cninsure.net.zhangzhongbao.XWalkMainActivity",
                                    PushLog.orderDetailUrl + inscomcode + "-" + processInstanceId, insbpersonName, totalepremium, openId);
                            HttpPush.singleAccountPushLog(pushLog, account);
                            if(ValidateUtil.getConfigValue("pushmsg.url") != null){
                                LogUtil.info(" 配送开始 (打单成功) , 开始开始进行推送消息,taskid=" + processInstanceId + ",inscomcode=" + inscomcode
                                        + " ,taskCode = "+ dataModel.getTaskCode() +" ,taskStatus = "+ dataModel.getTaskStatus() );
                                HttpPush.push(pushLog);
                                INSCMessage msg = new INSCMessage();
                                HttpPush.setINSCMessage(msg, pushLog, msgIdAndroid);
                                messageDao.insert(msg);
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                            LogUtil.info("调用 配送开始 (打单成功) ,发消息报异常：" + processInstanceId + "," + inscomcode + " Error:"+ e.getMessage());
                        }
                    }
                });
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void sendInsureGoBackMsg(String processinstanceid, String maininstanceId, String usercode) {
        //发核保退回消息
        try{
            //获取报价区域
            LogUtil.info("核保退回, 开始进行消息推送信息组装:maininstanceId="+maininstanceId+" ,processinstanceid=" + processinstanceid + ",usercode=" + usercode);
            INSBQuotetotalinfo insbQuotetotalinfo = new INSBQuotetotalinfo();
            insbQuotetotalinfo.setTaskid(maininstanceId);
            INSBQuotetotalinfo quotetotalinfo = quotetotalinfoDao.selectOne(insbQuotetotalinfo);
            if(quotetotalinfo == null){
               String msg = "核保退回, :maininstanceId=" + maininstanceId + " ,processinstanceid="+processinstanceid+",无 总报价信息";
                throw new Exception(msg);
            }
            String account = quotetotalinfo.getAgentnum();
            if(StringUtil.isEmpty(account)){
                String msg = "核保退回, :maininstanceId=" + maininstanceId + " ,processinstanceid="+processinstanceid+",无工号信息";
                throw new Exception(msg);
            }

            //获取供应商id和名称
            INSBQuoteinfo quoteinfo = insbQuoteinfoDao.queryQuoteinfoByWorkflowinstanceid(processinstanceid);
            if(quoteinfo == null){
                String msg = "核保退回, :maininstanceId=" + maininstanceId + " ,processinstanceid="+processinstanceid+",无 报价信息";
                throw new Exception(msg);
            }
            String inscomcode = quoteinfo.getInscomcode();
            if(StringUtil.isEmpty(inscomcode)){
                String msg = "核保退回, :maininstanceId=" + maininstanceId + " ,processinstanceid="+processinstanceid+",无 供应商 id";
                throw new Exception(msg);
            }
            INSBProvider provider = insbProviderService.queryByPrvcode(inscomcode);
            if(provider == null){
                String msg = "核保退回, :maininstanceId=" + maininstanceId + " ,processinstanceid="+processinstanceid+",无 供应商 信息";
                throw new Exception(msg);
            }

            //获取车牌
            INSBCarinfohis carinfohisParam = new INSBCarinfohis();
            carinfohisParam.setTaskid(maininstanceId);
            carinfohisParam.setInscomcode(inscomcode);
            INSBCarinfohis carinfohis = carinfohisDao.selectOne(carinfohisParam);
            if(carinfohis == null){
                String msg = "核保退回, :maininstanceId=" + maininstanceId + " ,processinstanceid="+processinstanceid+",查询 INSBCarinfohis ，无信息";
                throw new Exception(msg);
            }
            String carLicenseno = org.springframework.util.StringUtils.isEmpty(carinfohis.getCarlicenseno()) ? "新车未上牌" : carinfohis.getCarlicenseno();

            Map<String, Object> paraMap = new HashMap<String, Object>();
            paraMap.put("taskid", maininstanceId);
            paraMap.put("inscomcode", inscomcode);
            //获取被保人姓名
            String insbpersonName = messageDao.getInsbpersonName(paraMap);
            insbpersonName = insbpersonName==null?"":insbpersonName;

            //获取金额
            Double totalepremium = messageDao.getTotalepremium (paraMap);
            totalepremium = totalepremium == null ? 0 : totalepremium;
            //获取微信号
            String openId = messageDao.getOpenId(account);
            //推送
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
//				String msgId = maininstanceId + processinstanceid + account + sdf.format(new Date()).substring(14);
            String msgIdAndroid = UUIDUtils.create();
            PushLog pushLog = new PushLog(carLicenseno, inscomcode, provider.getPrvshotname(), msgIdAndroid, maininstanceId, PushLog.AllPlat,
                    "tencent", "核保退回", "19", "0", "0", "1", "cninsure.net.zhangzhongbao.XWalkMainActivity",
                    PushLog.quoteUrl + maininstanceId, insbpersonName, totalepremium, openId);
            HttpPush.singleAccountPushLog(pushLog, account);
            if(ValidateUtil.getConfigValue("pushmsg.url") != null){
                LogUtil.info("核保退回, 开始进行消息推送:processinstanceid=" + processinstanceid + ",usercode=" + usercode);
                LogUtil.info("核保退回,发核保退回消息，taskid=" + maininstanceId + ",inscomcode=" + inscomcode );
                HttpPush.push(pushLog);
                INSCMessage msg = new INSCMessage();
                HttpPush.setINSCMessage(msg, pushLog, msgIdAndroid);
                messageDao.insert(msg);

            }


//				String msgIdIos = UUIDUtils.create();
//				PushLog pushLogIos = new PushLog(carLicenseno, inscomcode, provider.getPrvshotname(), msgIdIos, maininstanceId, PushLog.IOSPlat,
//						"tencent", "核保退回", "19", "0", "0", "1", "cninsure.net.zhangzhongbao.XWalkMainActivity",
//						PushLog.quoteUrl + inscomcode + "-" + maininstanceId);
//				HttpPush.singleAccountPushLog(pushLogIos, account);
//				HttpPush.push(pushLogIos);
//				INSCMessage msgIos = new INSCMessage();
//				HttpPush.setINSCMessage(msgIos, pushLogIos, msgIdIos);
//				messageDao.insert(msgIos);

        }catch(Exception ex){
            ex.printStackTrace();
            LogUtil.info("核保退回,发异常:processinstanceid=" + processinstanceid + ",usercode=" + usercode +", message = "+ex.getMessage());
        }
    }
}
