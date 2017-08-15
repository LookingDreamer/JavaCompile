package com.lzgapi.order.aop;

import com.alibaba.fastjson.JSONObject;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.common.HttpClientUtil;
import com.common.JsonUtil;
import com.common.ModelUtil;
import com.lzgapi.order.service.LzgDataTransferService;
import com.zzb.chn.util.JsonUtils;
import com.zzb.cm.dao.*;
import com.zzb.cm.entity.*;
import com.lzgapi.order.model.LzgOrderModel;
import com.zzb.conf.dao.INSBOrderlistenpushDao;
import com.zzb.conf.dao.INSBPolicyitemDao;
import com.zzb.conf.dao.INSBWorkflowmainDao;
import com.zzb.conf.dao.INSBWorkflowsubDao;
import com.zzb.conf.entity.INSBOrderlistenpush;
import com.zzb.conf.entity.INSBWorkflowmain;
import com.zzb.conf.entity.INSBWorkflowsub;
import com.zzb.warranty.util.DateUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Hwc on 2017/5/18.
 */

/**
 * 订单状态变更通知懒掌柜
 */
@Aspect
@Component
public class INSBOrderAdvice {

    private static ResourceBundle config = ResourceBundle.getBundle("config/config");
    /**
     * 订单保存通知接口
     */
    public static String insertOrderURL = config.getString("lzg.interface.url")+"/rbk/lzgapi/requirement/add";
    /**
     * 订单变更通知接口
     */
    public static String updateOrderURL = config.getString("lzg.interface.url")+"/rbk/lzgapi/requirement/add";

    /**
     * 保单信息同步
     */
    public static String policySynURL = config.getString("lzg.interface.url")+"/rbk/lzgapi/policyagentindex/synczzbpolicy";

    /**
     * checkTokenURL 目标应用使用获取到的token获取代理人信息
     */
    public static String checkTokenURL = config.getString("lzg.interface.url") + "/rbk/lzgapi/oauth/checkToken";

    @Resource
    private INSBInsuredDao insbInsuredDao;

    @Resource
    private INSBPersonDao insbPersonDao;

    @Resource
    private INSBQuotetotalinfoDao insbQuotetotalinfoDao;

    @Resource
    private INSBCarinfoDao insbCarinfoDao;

    @Resource
    private INSBOrderlistenpushDao insbOrderlistenpushDao;

    @Resource
    private INSBPolicyitemDao insbPolicyitemDao;

    @Resource
    private INSBWorkflowsubDao insbWorkflowsubDao;

    @Resource
    private INSBWorkflowmainDao insbWorkflowmainDao;

    @Resource
    private LzgDataTransferService lzgDataTransferService;

    @Resource
    private ThreadPoolTaskExecutor taskThreadPool;

    @Resource
    private INSBOrderDao insbOrderDao;

    @Resource
    private INSBApplicantDao insbApplicantDao;

    //定义切点--订单数据变更
    @AfterReturning("execution(* com.zzb.conf.service.INSBWorkflowmainService.updateWorkFlowMainData(..))")
    public void updateWorkFlowMainData(JoinPoint point) throws Throwable{
        try{
            Object[] args = point.getArgs();
            if( args != null && args.length > 0 && args[0] instanceof INSBWorkflowmain) {
                INSBWorkflowmain mainFlow = (INSBWorkflowmain) args[0];
                taskThreadPool.execute(new Runnable(){
                    @Override
                    public void run() {
                        try{
                            String taskId = mainFlow.getInstanceid();
                            INSBOrderlistenpush insbOrderlistenpush = insbOrderlistenpushDao.selectDataByTaskId(taskId);

                            //懒掌柜订单才通知懒掌柜
                            if(insbOrderlistenpush != null && insbOrderlistenpush.getTaskid().equals(taskId) && !"Completed".equals(mainFlow.getTaskstate())){
                                LogUtil.info("updateWorkFlowMainData update modelInfo mainFlow=" + JsonUtils.serialize(mainFlow));
                                Map<String,String> params = new HashMap<String, String>();
                                params.put("taskid",taskId);
                                INSBOrder order = insbOrderDao.queryOrder(params);
                                if(order == null){
                                    order = new INSBOrder() ;
                                    order.setTaskid(taskId);
                                    order.setOrderstatus("0");
                                    order.setPrvid(mainFlow.getProviderId());
                                    order.setCreatetime(new Date());
                                }
                                LzgOrderModel model = comLzgOrderModel(taskId,order.getOrderstatus(),order.getCreatetime(),order.getPrvid(),"");
                                LogUtil.info("updateWorkFlowMainData update modelInfo=" + JsonUtils.serialize(model));
                                //System.out.println("updateWorkFlowMainData modelInfo=" + JsonUtils.serialize(model));
                                String result = sendToLzg(model, updateOrderURL);
                                //System.out.printf("updateWorkFlowMainData modelInfo taskID=%s result=%s", order.getTaskid(), result);
                                LogUtil.info("updateWorkFlowMainData update modelInfo taskID=%s result=%s",taskId,result);
                                String status = order.getOrderstatus();
                                //待配送，完成状态推送
                                if("3".equals(status) ||"4".equals(status)) {
                                    //推送保单信息
                                    Map<String, Object> policyMap = lzgDataTransferService.getPolicyitemList(order.getAgentcode(), order.getTaskid());
                                    //System.out.printf("update modelInfo taskID=%s resultpolicyMap=%s", order.getTaskid(), JsonUtils.serialize(policyMap));
                                    if ((Integer) policyMap.getOrDefault("total", 0) > 0) {
                                        //System.out.printf("update modelInfo taskID=%s policyMap=%s", order.getTaskid(),JsonUtils.serialize(policyMap));
                                        LogUtil.info("updateWorkFlowMainData update modelInfo taskID=%s policyMap=%s",order.getTaskid(),JsonUtils.serialize(policyMap));
                                        String res = HttpClientUtil.doPostJsonString(policySynURL, JsonUtils.serialize(policyMap));
                                        LogUtil.info("updateWorkFlowMainData update modelInfo taskID=%s policyRes=%s",order.getTaskid(),res);
                                        //System.out.printf("update modelInfo taskID=%s res=%s",order.getTaskid(),res);
                                    }
                                }
                            }
                        }catch(Exception e){
                            LogUtil.info("updateWorkFlowMainData order update error taskId = %s, message = %s",mainFlow.getInstanceid(),e.getMessage());
                            e.printStackTrace();
                        }
                    }
                });


            }
        }catch (Exception e){
            LogUtil.info("orderUpdateWorkFlowMain order update error %s",e.getMessage());
            e.printStackTrace();
        }

    }

    //定义切点--订单数据变更
    @AfterReturning("execution(* com.zzb.conf.service.INSBWorkflowsubService.updateWorkFlowSubData(..))")
    public void updateWorkFlowSubData(JoinPoint point) throws Throwable{
        try{
            Object[] args = point.getArgs();
            if( args != null && args.length > 0 && args[0] instanceof INSBWorkflowsub) {
                INSBWorkflowsub subFlow = (INSBWorkflowsub) args[0];
                taskThreadPool.execute(new Runnable(){
                    @Override
                    public void run() {
                        try{
                            String taskId = subFlow.getMaininstanceid();
                            String subInstanceId = subFlow.getInstanceid();
                            String taskCode = subFlow.getTaskcode();
                            String taskState = subFlow.getTaskstate();
                            INSBOrderlistenpush insbOrderlistenpush = insbOrderlistenpushDao.selectDataByTaskId(taskId);
                            //懒掌柜订单才通知懒掌柜
                            if(insbOrderlistenpush != null && insbOrderlistenpush.getTaskid().equals(taskId)  && !"Completed".equals(subFlow.getTaskstate())){
                                LogUtil.info("updateWorkFlowSubData update modelInfo subFlow=" + JsonUtils.serialize(subFlow));
                                Map<String,String> params = new HashMap<String, String>();
                                params.put("taskid",taskId);
                                INSBOrder order = insbOrderDao.queryOrder(params);
                                if(order == null){
                                    order = new INSBOrder() ;
                                    order.setTaskid(taskId);
                                    order.setOrderstatus("0");
                                    order.setPrvid(subFlow.getProviderId());
                                    order.setCreatetime(new Date());
                                }
                                LzgOrderModel model = comLzgOrderModel(taskId,order.getOrderstatus(),order.getCreatetime(),order.getPrvid(),subInstanceId);
                               try {
                                   Map<String, Object> body = (Map) model.getBody();
                                   if (body != null) {
                                       if (body.containsKey("quoteInfoList")) {
                                           ArrayList<Map<String, String>> quoteInfoList = (ArrayList) body.get("quoteInfoList");
                                           if (quoteInfoList != null && quoteInfoList.size() > 0) {
                                               for (Map<String, String> info : quoteInfoList) {
                                                   info.put("taskcode", taskCode);
                                                   info.put("taskstate", taskState);
                                               }
                                           }
                                       }
                                   }
                               }catch (Exception e){
                                   LogUtil.info("updateWorkFlowSubData exception modelInfo=" + e.getMessage());
                                   e.printStackTrace();
                               }
                                LogUtil.info("updateWorkFlowSubData update modelInfo=" + JsonUtils.serialize(model));
                                //System.out.println("updateWorkFlowSubData modelInfo=" + JsonUtils.serialize(model));
                                String result = sendToLzg(model, updateOrderURL);
                                //System.out.printf("updateWorkFlowSubData modelInfo taskID=%s result=%s", order.getTaskid(), result);
                                LogUtil.info("updateWorkFlowSubData update modelInfo taskID=%s result=%s",taskId,result);
                                String status = order.getOrderstatus();
                                //待配送，完成状态推送
                                if("3".equals(status) ||"4".equals(status)) {
                                    //推送保单信息
                                    Map<String, Object> policyMap = lzgDataTransferService.getPolicyitemList(order.getAgentcode(), order.getTaskid());
                                    //System.out.printf("update modelInfo taskID=%s resultpolicyMap=%s", order.getTaskid(), JsonUtils.serialize(policyMap));
                                    if ((Integer) policyMap.getOrDefault("total", 0) > 0) {
                                        //System.out.printf("update modelInfo taskID=%s policyMap=%s", order.getTaskid(),JsonUtils.serialize(policyMap));
                                        LogUtil.info("updateWorkFlowSubData update modelInfo taskID=%s policyMap=%s",order.getTaskid(),JsonUtils.serialize(policyMap));
                                        String res = HttpClientUtil.doPostJsonString(policySynURL, JsonUtils.serialize(policyMap));
                                        LogUtil.info("updateWorkFlowSubData update modelInfo taskID=%s policyRes=%s",order.getTaskid(),res);
                                        //System.out.printf("update modelInfo taskID=%s res=%s",order.getTaskid(),res);
                                    }
                                }
                            }
                        }catch(Exception e){
                            LogUtil.info("updateWorkFlowSubData order update error taskId = %s, message = %s",subFlow.getMaininstanceid(),e.getMessage());
                            e.printStackTrace();
                        }
                    }
                });


            }
        }catch (Exception e){
            LogUtil.info("orderUpdateWorkFlowSub order update error %s",e.getMessage());
            e.printStackTrace();
        }

    }

    /*
    //定义切点--订单数据变更
    @AfterReturning("execution(* com.cninsure.core.dao.BaseDao.updateById(..)) && target(com.zzb.cm.dao.INSBOrderDao)")
    public void orderUpdate(JoinPoint point) throws Throwable{
        try{
            Object[] args = point.getArgs();
            if( args != null && args.length > 0 && args[0] instanceof INSBOrder) {
                INSBOrder order = (INSBOrder) args[0];
                taskThreadPool.execute(new Runnable(){
                    @Override
                    public void run() {
                        try{
                            INSBOrderlistenpush insbOrderlistenpush = insbOrderlistenpushDao.selectDataByTaskId(order.getTaskid());
                            //懒掌柜订单才通知懒掌柜
                            if(insbOrderlistenpush != null && insbOrderlistenpush.getTaskid().equals(order.getTaskid())){
                                LzgOrderModel model = comLzgOrderModel(order.getTaskid(),order.getOrderstatus(),order.getCreatetime(),order.getPrvid(),"");
                                LogUtil.info("update modelInfo=" + JsonUtils.serialize(model));
                                //System.out.println("update modelInfo=" + JsonUtils.serialize(model));
                                String result = sendToLzg(model, updateOrderURL);
                                //System.out.printf("update modelInfo taskID=%s result=%s", order.getTaskid(), result);
                                LogUtil.info("update modelInfo taskID=%s result=%s",order.getTaskid(),result);
                                String status = order.getOrderstatus();
                                //待配送，完成状态推送
                                if("3".equals(status) ||"4".equals(status)) {
                                    //推送保单信息
                                    Map<String, Object> policyMap = lzgDataTransferService.getPolicyitemList(order.getAgentcode(), order.getTaskid());
                                    //System.out.printf("update modelInfo taskID=%s resultpolicyMap=%s", order.getTaskid(), JsonUtils.serialize(policyMap));
                                    if ((Integer) policyMap.getOrDefault("total", 0) > 0) {
                                        //System.out.printf("update modelInfo taskID=%s policyMap=%s", order.getTaskid(),JsonUtils.serialize(policyMap));
                                        LogUtil.info("update modelInfo taskID=%s policyMap=%s",order.getTaskid(),JsonUtils.serialize(policyMap));
                                        String res = HttpClientUtil.doPostJsonString(policySynURL, JsonUtils.serialize(policyMap));
                                        LogUtil.info("update modelInfo taskID=%s policyRes=%s",order.getTaskid(),res);
                                        //System.out.printf("update modelInfo taskID=%s res=%s",order.getTaskid(),res);
                                    }
                                }
                            }
                        }catch(Exception e){
                            LogUtil.info("order update error taskId = %s, message = %s",order.getTaskid(),e.getMessage());
                            e.printStackTrace();
                        }
                    }
                });


            }
        }catch (Exception e){
            LogUtil.info("order update error %s",e.getMessage());
            e.printStackTrace();
        }

    }*/
    /*
    //定义切点--订单数据变更
    @AfterReturning("execution(* com.cninsure.core.dao.BaseDao.updateById(..)) && target(com.zzb.conf.dao.INSBWorkflowmainDao)")
    public void orderUpdateWorkFlowMain(JoinPoint point) throws Throwable{
        try{
            Object[] args = point.getArgs();
            if( args != null && args.length > 0 && args[0] instanceof INSBWorkflowmain) {
                INSBWorkflowmain mainFlow = (INSBWorkflowmain) args[0];
                taskThreadPool.execute(new Runnable(){
                    @Override
                    public void run() {
                        try{
                            String taskId = mainFlow.getInstanceid();
                            INSBOrderlistenpush insbOrderlistenpush = insbOrderlistenpushDao.selectDataByTaskId(taskId);

                            //懒掌柜订单才通知懒掌柜
                            if(insbOrderlistenpush != null && insbOrderlistenpush.getTaskid().equals(taskId)){
                                LogUtil.info("orderUpdateWorkFlowMain update mainFlow=" + JsonUtils.serialize(mainFlow));
                                Map<String,String> params = new HashMap<String, String>();
                                params.put("taskid",taskId);
                                INSBOrder order = insbOrderDao.queryOrder(params);
                                if(order == null){
                                    order = new INSBOrder() ;
                                    order.setTaskid(taskId);
                                    order.setOrderstatus("0");
                                    order.setPrvid(mainFlow.getProviderId());
                                    order.setCreatetime(new Date());
                                }
                                LzgOrderModel model = comLzgOrderModel(taskId,order.getOrderstatus(),order.getCreatetime(),order.getPrvid());
                                LogUtil.info("orderUpdateWorkFlowMain update modelInfo=" + JsonUtils.serialize(model));
                                //System.out.println("update modelInfo=" + JsonUtils.serialize(model));
                                String result = sendToLzg(model, updateOrderURL);
                                //System.out.printf("update modelInfo taskID=%s result=%s", order.getTaskid(), result);
                                LogUtil.info("orderUpdateWorkFlowMain update modelInfo taskID=%s result=%s",taskId,result);
                                String status = order.getOrderstatus();
                                //待配送，完成状态推送
                                if("3".equals(status) ||"4".equals(status)) {
                                    //推送保单信息
                                    Map<String, Object> policyMap = lzgDataTransferService.getPolicyitemList(order.getAgentcode(), order.getTaskid());
                                    //System.out.printf("update modelInfo taskID=%s resultpolicyMap=%s", order.getTaskid(), JsonUtils.serialize(policyMap));
                                    if ((Integer) policyMap.getOrDefault("total", 0) > 0) {
                                        //System.out.printf("update modelInfo taskID=%s policyMap=%s", order.getTaskid(),JsonUtils.serialize(policyMap));
                                        LogUtil.info("orderUpdateWorkFlowMain update modelInfo taskID=%s policyMap=%s",order.getTaskid(),JsonUtils.serialize(policyMap));
                                        String res = HttpClientUtil.doPostJsonString(policySynURL, JsonUtils.serialize(policyMap));
                                        LogUtil.info("orderUpdateWorkFlowMain update modelInfo taskID=%s policyRes=%s",order.getTaskid(),res);
                                        //System.out.printf("update modelInfo taskID=%s res=%s",order.getTaskid(),res);
                                    }
                                }
                            }
                        }catch(Exception e){
                            LogUtil.info("orderUpdateWorkFlowMain order update error taskId = %s, message = %s",mainFlow.getInstanceid(),e.getMessage());
                            e.printStackTrace();
                        }
                    }
                });


            }
        }catch (Exception e){
            LogUtil.info("orderUpdateWorkFlowMain order update error %s",e.getMessage());
            e.printStackTrace();
        }

    }
    */

    /*
    //定义切点--订单数据变更
    @AfterReturning("execution(* com.cninsure.core.dao.BaseDao.updateById(..)) && target(com.zzb.conf.dao.INSBWorkflowsubDao)")
    public void orderUpdateWorkFlowSub(JoinPoint point) throws Throwable{
        try{
            Object[] args = point.getArgs();
            if( args != null && args.length > 0 && args[0] instanceof INSBWorkflowsub) {
                INSBWorkflowsub subFlow = (INSBWorkflowsub) args[0];
                taskThreadPool.execute(new Runnable(){
                    @Override
                    public void run() {
                        try{
                            String taskId = subFlow.getMaininstanceid();
                            INSBOrderlistenpush insbOrderlistenpush = insbOrderlistenpushDao.selectDataByTaskId(taskId);

                            //懒掌柜订单才通知懒掌柜
                            if(insbOrderlistenpush != null && insbOrderlistenpush.getTaskid().equals(taskId)){
                                LogUtil.info("orderUpdateWorkFlowSub update subFlow=" + JsonUtils.serialize(subFlow));
                                Map<String,String> params = new HashMap<String, String>();
                                params.put("taskid",taskId);
                                INSBOrder order = insbOrderDao.queryOrder(params);
                                if(order == null){
                                    order = new INSBOrder() ;
                                    order.setTaskid(taskId);
                                    order.setOrderstatus("0");
                                    order.setPrvid(subFlow.getProviderId());
                                    order.setCreatetime(new Date());
                                }
                                LzgOrderModel model = comLzgOrderModel(taskId,order.getOrderstatus(),order.getCreatetime(),order.getPrvid());
                                LogUtil.info("orderUpdateWorkFlowSub update modelInfo=" + JsonUtils.serialize(model));
                                //System.out.println("orderUpdateWorkFlowSub modelInfo=" + JsonUtils.serialize(model));
                                String result = sendToLzg(model, updateOrderURL);
                                //System.out.printf("orderUpdateWorkFlowSub modelInfo taskID=%s result=%s", order.getTaskid(), result);
                                LogUtil.info("orderUpdateWorkFlowSub update modelInfo taskID=%s result=%s",taskId,result);
                                String status = order.getOrderstatus();
                                //待配送，完成状态推送
                                if("3".equals(status) ||"4".equals(status)) {
                                    //推送保单信息
                                    Map<String, Object> policyMap = lzgDataTransferService.getPolicyitemList(order.getAgentcode(), order.getTaskid());
                                    //System.out.printf("update modelInfo taskID=%s resultpolicyMap=%s", order.getTaskid(), JsonUtils.serialize(policyMap));
                                    if ((Integer) policyMap.getOrDefault("total", 0) > 0) {
                                        //System.out.printf("update modelInfo taskID=%s policyMap=%s", order.getTaskid(),JsonUtils.serialize(policyMap));
                                        LogUtil.info("orderUpdateWorkFlowSub update modelInfo taskID=%s policyMap=%s",order.getTaskid(),JsonUtils.serialize(policyMap));
                                        String res = HttpClientUtil.doPostJsonString(policySynURL, JsonUtils.serialize(policyMap));
                                        LogUtil.info("orderUpdateWorkFlowSub update modelInfo taskID=%s policyRes=%s",order.getTaskid(),res);
                                        //System.out.printf("update modelInfo taskID=%s res=%s",order.getTaskid(),res);
                                    }
                                }
                            }
                        }catch(Exception e){
                            LogUtil.info("orderUpdateWorkFlowSub order update error taskId = %s, message = %s",subFlow.getMaininstanceid(),e.getMessage());
                            e.printStackTrace();
                        }
                    }
                });


            }
        }catch (Exception e){
            LogUtil.info("orderUpdateWorkFlowSub order update error %s",e.getMessage());
            e.printStackTrace();
        }

    }
   */

/*
    //定义切点--订单数据入库--核保数据推懒掌柜
    @AfterReturning(pointcut="execution(* com.cninsure.core.dao.BaseDao.insert(..)) && target(com.zzb.cm.dao.INSBOrderDao)")
    public void orderInsert1(JoinPoint point) throws Throwable{
        try {
            Object[] args = point.getArgs();
            if( args != null && args.length > 0 && args[0] instanceof INSBOrder) {
                INSBOrder order = (INSBOrder) args[0];
                taskThreadPool.execute(new Runnable(){
                    @Override
                    public void run() {
                        try{
                            INSBOrderlistenpush insbOrderlistenpush = insbOrderlistenpushDao.selectDataByTaskId(order.getTaskid());
                            //懒掌柜订单才通知懒掌柜
                            if(insbOrderlistenpush != null && insbOrderlistenpush.getTaskid().equals(order.getTaskid())) {
                                LzgOrderModel model = comLzgOrderModel(order.getTaskid(), "0", new Date(),order.getPrvid(),"");
                                //System.out.printf("update modelInfo=" + JsonUtils.serialize(model));
                                LogUtil.info("insert modelInfo=" + JsonUtils.serialize(model));
                                String result = sendToLzg(model, updateOrderURL);
                                LogUtil.info("insert modelInfo taskID=%s result=%s", order.getTaskid(), result);
                                //System.out.printf("update modelInfo taskID=%s result=%s", order.getTaskid(), result);
                            }
                        }catch(Exception e){
                            LogUtil.info("order insert error taskId = %s, message = %s",order.getTaskid(),e.getMessage());
                            e.printStackTrace();
                        }
                    }
                });

            }
        }catch (Exception e){
            LogUtil.info("order insert error %s",e.getMessage());
            e.printStackTrace();
        }

    }*/
/*
    //定义切点--掌中保获取多方报价完成后推送到懒掌柜
    @AfterReturning(pointcut="execution(* com.zzb.mobile.service.INSBMultiQuoteInfoService.getMultiQuoteInfo(..))")
    public void multiQuoteInfo(JoinPoint point) throws Throwable{
        try {
            Object[] args = point.getArgs();
            if( args != null && args.length > 0) {
                String taskId = (String) args[1];
                taskThreadPool.execute(new Runnable(){
                    @Override
                    public void run() {
                        try{
                            INSBOrderlistenpush insbOrderlistenpush = insbOrderlistenpushDao.selectDataByTaskId(taskId);
                            //懒掌柜订单才通知懒掌柜
                            if(insbOrderlistenpush != null && insbOrderlistenpush.getTaskid().equals(taskId)) {
                                INSBOrder order = new INSBOrder();
                                order.setTaskid(taskId);
                                Long orderCount = insbOrderDao.selectCount(order);
                                //System.out.printf("multiQuoteInfo update orderCount=" + orderCount);
                                LogUtil.info("multiQuoteInfo modelInfo  taskId=%s orderCount= %s" ,taskId, orderCount);
                                if( orderCount != null && orderCount <= 0 ) {
                                    LogUtil.info("multiQuoteInfo modelInfo after taskId=%s orderCount= %s" ,taskId, orderCount);
                                    //System.out.printf("multiQuoteInfo modelInfo after taskId=%s orderCount= %s" ,taskId, orderCount);
                                    LzgOrderModel model = comLzgOrderModel(taskId, "0", new Date(), "","");
                                    //System.out.printf("multiQuoteInfo update modelInfo=" + JsonUtils.serialize(model));
                                    LogUtil.info("multiQuoteInfo modelInfo=" + JsonUtils.serialize(model));
                                    String result = sendToLzg(model, updateOrderURL);
                                    LogUtil.info("multiQuoteInfo modelInfo taskID=%s result=%s", taskId, result);
                                    //System.out.printf("multiQuoteInfo update modelInfo taskID=%s result=%s", taskId, result);
                                }
                            }
                        }catch(Exception e){
                            LogUtil.info("multiQuoteInfo order update error taskId = %s, message = %s",taskId,e.getMessage());
                            e.printStackTrace();
                        }
                    }
                });

            }
        }catch (Exception e){
            LogUtil.info("multiQuoteInfo order update error %s",e.getMessage());
            e.printStackTrace();
        }

    }
*/
/* 不在创建任务时推送信息的懒掌柜
    //定义切点--懒掌柜订单数据入库 --报价推懒掌柜
    @AfterReturning(pointcut="execution(* com.cninsure.core.dao.BaseDao.insert(..)) && target(com.zzb.conf.dao.INSBOrderlistenpushDao)")
    public void orderInsert2(JoinPoint point) throws Throwable{
        try {
            Object[] args = point.getArgs();
            if( args != null && args.length > 0 && args[0] instanceof INSBOrderlistenpush) {
                INSBOrderlistenpush order = (INSBOrderlistenpush) args[0];
                taskThreadPool.execute(new Runnable(){
                    @Override
                    public void run() {
                        try{
                            //懒掌柜订单才通知懒掌柜
                            LzgOrderModel model = comLzgOrderModel(order.getTaskid(), "0", new Date(),"");
                            LogUtil.info("insert modelInfo=" + JsonUtils.serialize(model));
                            //System.out.printf("insert modelInfo=" + JsonUtils.serialize(model));
                            String result = sendToLzg(model, insertOrderURL);
                            LogUtil.info("insert modelInfo taskID=%s result=%s", order.getTaskid(), result);
                            //System.out.printf("insert modelInfo taskID=%s result=%s", order.getTaskid(), result);
                        }catch(Exception e){
                            LogUtil.info("order update error taskId = %s, message = %s",order.getTaskid(),e.getMessage());
                            e.printStackTrace();
                        }
                    }
                });
            }
        }catch (Exception e){
            LogUtil.info("order insert error %s",e.getMessage());
            e.printStackTrace();
        }

    }
*/
    /**
     * 组装model，用于LZG数据交互
     * @param taskId
     * @return
     */
    public LzgOrderModel comLzgOrderModel(String taskId,String orderStatus ,Date createDate,String prvId ,String subInstanceId){
        //被保人信息
        INSBInsured insured = insbInsuredDao.selectInsuredByTaskId(taskId);
        INSBPerson insp = new INSBPerson();
        if (insured != null && !StringUtil.isEmpty(insured.getPersonid())) {
            insp = insbPersonDao.selectById(insured.getPersonid());
        }else {
            insp = insbPersonDao.selectPersonByTaskId(taskId);
        }

        //投保人信息
        INSBApplicant applicant = insbApplicantDao.selectByTaskID(taskId);
        INSBPerson applicantPerson = new INSBPerson();
        if (applicant != null && !StringUtil.isEmpty(applicant.getPersonid())) {
            applicantPerson = insbPersonDao.selectById(applicant.getPersonid());
        }else {
            applicantPerson = insbPersonDao.selectPersonByTaskId(taskId);
        }
        //报价总表
        INSBQuotetotalinfo insbQuotetotalinfo = new INSBQuotetotalinfo();
        insbQuotetotalinfo.setTaskid(taskId);
        insbQuotetotalinfo = insbQuotetotalinfoDao.selectOne(insbQuotetotalinfo);

        //投保车辆信息
        INSBCarinfo insbCarinfo = insbCarinfoDao.selectCarinfoByTaskId(taskId);

        LzgOrderModel model = new LzgOrderModel();
        model.setUsername(insp == null ? "" : insp.getName());
        model.setStatus(orderStatus);
        model.setAgentNo(insbQuotetotalinfo == null ? "" : insbQuotetotalinfo.getAgentnum());
        model.setCreatedate(createDate);
        model.setVehicleNo(insbCarinfo == null ? "" : insbCarinfo.getCarlicenseno());
        model.setOrderno(taskId);
        model.setQuantity(1);
        model.setIdCardNo(applicantPerson == null ? "" : applicantPerson.getIdcardno());
        model.setQuoteInvalidDate(this.getQuoteInvalidDate(taskId));
        model.setPayInvalidDate(this.getPayInvalidDate(taskId,prvId));
        model.setApplicantName(applicantPerson == null ? "" :applicantPerson.getName());
        model.setApplicantPhone(applicantPerson == null ? "" :applicantPerson.getCellphone());

        //获取报价列表
        String status = "3";
        if("0".equals(orderStatus)){
            status = "1";  //待投保
        }
        String quoteInfoStr = "";
        if(StringUtil.isEmpty(subInstanceId)){
             quoteInfoStr = lzgDataTransferService.getQuoteInfoList(taskId,status);
        }else {
             quoteInfoStr = lzgDataTransferService.getQuoteInfoList(taskId,status,subInstanceId);
        }

        Map<String,Object> result = JsonUtils.deserialize(quoteInfoStr,Map.class);
        Map<String,Object> quoteInfoList = (Map)result.get("body");
        model.setBody(quoteInfoList);
        return model;
    }

    /**
     * 组装model，用于LZG数据交互
     * @param
     * @return
     */
   /* private LzgOrderModel comLzgOrderModel(INSBOrder order){
        String taskId = order.getTaskid();
        String prvId = order.getPrvid();
        Double amount = order.getTotalpaymentamount();

        LzgOrderModel lzgOrderModel = new LzgOrderModel();
        lzgOrderModel.setOrderno(taskId);
        lzgOrderModel.setStatus(order.getOrderstatus());
        lzgOrderModel.setUsername(order.getAgentname());
        lzgOrderModel.setAgentcode(order.getAgentcode());
        lzgOrderModel.setPrice(amount);
        lzgOrderModel.setTotalprice(amount);
        lzgOrderModel.setQuantity(1);
        lzgOrderModel.setModifydate(new Date());
        lzgOrderModel.setProductcode("zzb");
        lzgOrderModel.setTitle("掌中保");
        lzgOrderModel.setCreatedate(order.getCreatetime()==null?new Date():order.getCreatetime());

        //获取保单列表
        INSBPolicyitem insbPolicyitem = new INSBPolicyitem();
        insbPolicyitem.setTaskid(taskId);
        insbPolicyitem.setInscomcode(prvId);
        List<INSBPolicyitem> insbPolicyitemList = insbPolicyitemService.queryList(insbPolicyitem);

        //获取投保险种列表
        INSBCarkindprice insbCarkindprice = new INSBCarkindprice();
        insbCarkindprice.setTaskid(taskId);
        insbCarkindprice.setInscomcode(prvId);
        List<INSBCarkindprice> insbCarkindpriceList = insbCarkindpriceService.queryList(insbCarkindprice);

        List<LzgPolicyListModel> lzgPolicyList = new ArrayList<LzgPolicyListModel>();
        //交强险和车船税
        List<LzgRiskModel> lzgRiskModelJQList = new ArrayList<LzgRiskModel>();
        //商业险
        List<LzgRiskModel> lzgRiskModelSYList = new ArrayList<LzgRiskModel>();
        if ( insbCarkindpriceList != null ){
            String riskCode = "";
            for (INSBCarkindprice carkindprice : insbCarkindpriceList){
                riskCode = carkindprice.getInskindcode();
                LzgRiskModel riskModel = new LzgRiskModel();
                riskModel.setRiskCode(riskCode);
                riskModel.setAmnt(carkindprice.getAmount());
                riskModel.setContPrem(carkindprice.getDiscountCharge());
                riskModel.setRiskName(carkindprice.getRiskname());
                if("VehicleCompulsoryIns".equals(riskCode) || "VehicleTax".equals(riskCode)  ){
                    lzgRiskModelJQList.add(riskModel);
                }else{
                    lzgRiskModelSYList.add(riskModel);
                }

            }
        }
        //组装保单信息
        if( null != insbPolicyitemList){
            for(INSBPolicyitem item : insbPolicyitemList){
                INSBPerson person = new INSBPerson();
                person = insbPersonService.queryById(item.getInsuredid());
                LzgPolicyListModel policyListModel = new LzgPolicyListModel();
                policyListModel.setContNo(item.getPolicyno());
                policyListModel.setInsuredIdno(person != null ?person.getIdcardno():"");
                policyListModel.setCinvaliDate(item.getEnddate());
                policyListModel.setCvaliDate(item.getStartdate());
                policyListModel.setInsuredName(item.getInsuredname());
                policyListModel.setSignDate(item.getInsureddate());
                policyListModel.setSupplierCode(item.getInscomcode());
                policyListModel.setRiskType(item.getRisktype());
                lzgOrderModel.setApplicant(item.getApplicantname());
                 //商业险
                if("0".equals(item.getRisktype())){
                    policyListModel.setRiskList(lzgRiskModelSYList);
                }else{
                    policyListModel.setRiskList(lzgRiskModelJQList);
                }
                lzgPolicyList.add(policyListModel);
            }

        }
        lzgOrderModel.setPolicylist(lzgPolicyList);

        return lzgOrderModel;
    }*/

    public String sendToLzg(LzgOrderModel model,String url){
        return HttpClientUtil.doPostJsonString(url, JsonUtils.serialize(model));
    }

    /**
     * 判断支付有效期 true 过期不能支付
     * @param taskid
     * @param inscomcode
     * @return
     */
    public Date getPayInvalidDate(Object taskid, Object inscomcode) {
        Date result = new Date();
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("taskid", taskid);
            map.put("inscomcode", inscomcode);
            //List<INSBPolicyitem> policyitemList = insbPolicyitemDao.selectPolicyitemByInscomTask(map);
            List<Map> policyitemList = insbPolicyitemDao.selectRemoveOverTimeData(map);
            if (policyitemList == null || policyitemList.isEmpty()) {
                LogUtil.error("getPayInvalidDate:" + taskid + ",prvid:" + inscomcode + ",保单数据为空");
                return result;
            }
            Date syStartDate = null, jqStartDate = null;
            for (Map m : policyitemList) {
                if (m == null || m.get("risktype") == null) continue;
                if ("0".equals(m.get("risktype"))) {
                    syStartDate = m.get("startdate") == null ? null : (Date) m.get("startdate");
                } else {
                    jqStartDate = m.get("startdate") == null ? null : (Date) m.get("startdate");
                }
            }
            if (syStartDate == null && jqStartDate == null) {
                LogUtil.error("getPayInvalidDate:" + taskid + ",prvid:" + inscomcode + ",起保日期为空");
                return result;
            }

            //选取先过期的日期
            Date quotesuccessTimes = null;


            Map myMap = policyitemList.get(0);
            if (myMap != null) {
                Date createDate = myMap.get("createtime") == null ? null : (Date) myMap.get("createtime");
                if (null == myMap.get("payvalidity")) {
                    quotesuccessTimes = ModelUtil.gatFastPaydate(syStartDate, jqStartDate);
                } else {
                    quotesuccessTimes = ModelUtil.gatFastPaydateToNow(syStartDate, jqStartDate, createDate, (int) myMap.get("payvalidity"));
                }
           /* if(!(ModelUtil.daysBetween(quotesuccessTimes, new Date()) > 0)){
                result = false;
            }*/
            }
            result = quotesuccessTimes;
            LogUtil.debug("getPayInvalidDate:" + taskid + ",prvid:" + inscomcode + ",到期时间:" + quotesuccessTimes);
        }catch (Exception e){
            LogUtil.debug("getPayInvalidDate:" + taskid + ",prvid:" + inscomcode + ",到期时间:" + result);
        }
        return result;
    }

    /**
     * 报价有效期
     * @param taskid
     * @param inscomcode
     * @return
     */
    public Date quoteOverTimeDate(Object taskid, Object inscomcode) {
        Date result = new Date();
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("taskid", taskid);
            map.put("inscomcode", inscomcode);
            //List<INSBPolicyitem> policyitemList = insbPolicyitemDao.selectPolicyitemByInscomTask(map);
            Date syStartDate = null, jqStartDate = null;
            List<Map> policyitemList = insbPolicyitemDao.selectRemoveOverTimeData(map);
            if (policyitemList == null || policyitemList.isEmpty()) {
                LogUtil.error("removeOverTimeData:" + taskid + ",prvid:" + inscomcode + ",保单数据为空");
                return result;
            }
            for (Map m : policyitemList) {
                if (m == null || m.get("risktype") == null) continue;
                if ("0".equals(m.get("risktype"))) {
                    syStartDate = m.get("startdate") == null ? null : (Date) m.get("startdate");
                } else {
                    jqStartDate = m.get("startdate") == null ? null : (Date) m.get("startdate");
                }
            }
            if (syStartDate == null && jqStartDate == null) {
                LogUtil.error("quoteOverTimeDate:" + taskid + ",prvid:" + inscomcode + ",起保日期为空");
                return result;
            }

            //选取先过期的日期
            Date quotesuccessTimes = null;

            quotesuccessTimes = ModelUtil.gatFastPaydate(syStartDate, jqStartDate);
            LogUtil.debug("quoteOverTimeDate:" + taskid + ",prvid:" + inscomcode + ",到期时间:" + quotesuccessTimes);
            result = quotesuccessTimes;
        /*if(!(ModelUtil.daysBetween(quotesuccessTimes, new Date()) > 0)){
            result = false;
        }*/
        }catch (Exception e){
            LogUtil.debug("quoteOverTimeDate:" + taskid + ",prvid:" + inscomcode + ",到期时间:" + result);
        }
        return result;
    }

    public Date getQuoteInvalidDate(String taskId){
        String processInstanceId = taskId;
        List<Map<String,Object>> subQuoteInfoList = insbWorkflowsubDao.getQuoteInfoByTaskId(processInstanceId);
        if(subQuoteInfoList==null||(subQuoteInfoList.size()==1&&StringUtil.isEmpty(subQuoteInfoList.get(0)))){
            subQuoteInfoList = insbWorkflowmainDao.getQuoteInfoByTaskId(processInstanceId);
        }
        Date quoteInvalidDate = new Date();
        Date tempQuoteInvalidDate = new Date();
        for (int i=subQuoteInfoList.size()-1;i>0;i--) {
            Map<String, Object> map =subQuoteInfoList.get(i);
            if(!(StringUtil.isEmpty(map.get("inscomcode"))
                    ||StringUtil.isEmpty(map.get("prvshotname"))
                    ||StringUtil.isEmpty(map.get("taskcode")))){
                tempQuoteInvalidDate = quoteOverTimeDate(taskId,map.get("inscomcode"));
               if((ModelUtil.compareDate(tempQuoteInvalidDate, quoteInvalidDate) > 0)){
                    quoteInvalidDate = tempQuoteInvalidDate;
                }

            }
        }

        return quoteInvalidDate;

    }


}
