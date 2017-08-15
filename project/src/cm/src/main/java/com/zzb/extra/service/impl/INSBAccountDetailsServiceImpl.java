package com.zzb.extra.service.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.common.JsonUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.cninsure.core.utils.BeanUtils;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.UUIDUtils;
import com.zzb.chn.util.JsonUtils;
import com.zzb.cm.dao.INSBCarinfoDao;
import com.zzb.cm.service.INSBOrderService;
import com.zzb.conf.service.INSBAgentService;
import com.zzb.conf.service.INSBPolicyitemService;
import com.zzb.conf.service.INSBProviderService;
import com.zzb.extra.controller.vo.ActivityPrizesVo;
import com.zzb.extra.dao.INSBAccountDetailsDao;
import com.zzb.extra.dao.INSBAccountWithdrawDao;
import com.zzb.extra.dao.INSBAgentPrizeDao;
import com.zzb.extra.dao.INSBAgentTaskDao;
import com.zzb.extra.dao.INSBCommissionDao;
import com.zzb.extra.dao.INSBCommissionRateDao;
import com.zzb.extra.entity.INSBAccount;
import com.zzb.extra.entity.INSBAccountDetails;
import com.zzb.extra.entity.INSBAgentPrize;
import com.zzb.extra.entity.INSBCommission;
import com.zzb.extra.entity.INSBCommissionRate;
import com.zzb.extra.model.AgentPrizeModel;
import com.zzb.extra.model.AgentTaskModel;
import com.zzb.extra.service.INSBAccountDetailsService;
import com.zzb.extra.service.INSBAccountService;
import com.zzb.extra.service.INSBAgentPrizeService;
import com.zzb.extra.service.INSBCommissionRateService;
import com.zzb.extra.service.INSBCommissionService;
import com.zzb.extra.service.INSBConditionsService;
import com.zzb.extra.service.INSBMarketActivityService;
import com.zzb.extra.service.WxMsgTemplateService;
import com.zzb.extra.util.ParamUtils;

@Service
@Transactional
public class INSBAccountDetailsServiceImpl extends BaseServiceImpl<INSBAccountDetails> implements
        INSBAccountDetailsService {
    @Resource
    private INSBAccountService insbAccountService;

    @Resource
    private INSBAccountDetailsDao insbAccountDetailsDao;

    @Resource
    private INSBAccountWithdrawDao insbAccountWithdrawDao;

    @Resource
    private INSBAgentTaskDao insbAgentTaskDao;

    @Resource
    private INSBAgentPrizeDao insbAgentPrizeDao;

    @Resource
    private INSBCarinfoDao insbCarinfoDao;

    @Resource
    private INSBCommissionDao insbCommissionDao;

    @Resource
    private INSBOrderService insbOrderService;

    @Resource
    private INSBAgentPrizeService insbAgentPrizeService;

    @Resource
    private INSBConditionsService insbConditionsService;

    @Resource
    private INSBPolicyitemService insbPolicyitemService;

    @Resource
    private INSBProviderService insbProviderService;

    @Resource
    private INSBCommissionService insbCommissionService;

    @Resource
    private INSBCommissionRateDao insbCommissionRateDao;

    @Resource
    private INSBCommissionRateService insbCommissionRateService;

    @Resource
    private INSBMarketActivityService insbMarketActivityService;

    @Resource
    private INSBAgentService insbAgentService;

    @Resource
    private WxMsgTemplateService wxMsgTemplateService;

    @Override
    protected BaseDao<INSBAccountDetails> getBaseDao() {
        return insbAccountDetailsDao;
    }

    @Override
    public String queryPagingList(Map<String, Object> map) {
        long total = insbAccountDetailsDao.queryPagingListCount(map);
        List<Map<Object, Object>> infoList = insbAccountDetailsDao.queryPagingList(map);
        return ParamUtils.resultMap(total, infoList);
    }

    /**
     * 获取账户当前余额
     *
     * @param agentid
     * @return
     */
    @Override
    public String queryBalance(String agentid) {

        if (agentid == null || agentid.equals("")) {
            return ParamUtils.resultMap(false, "参数不正确");
        }

        INSBAccount insbAccount = insbAccountService.initAccount(agentid);
        if (insbAccount == null) return ParamUtils.resultMap(false, "用户不存在或未验证");

        Double balance = insbAccountDetailsDao.queryBalance(insbAccount.getId());
        Double todayIncome = insbAccountDetailsDao.queryTodayIncome(insbAccount.getId());
        Double blockedFund = insbAccountWithdrawDao.queryBlocked(insbAccount.getId());
        Double monthWithdraw = insbAccountWithdrawDao.queryMonthWithdraw(insbAccount.getId());
        Double availableBalance = balance - blockedFund;

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("balance", balance);
        map.put("todayIncome", todayIncome);
        map.put("blockedFund", blockedFund);
        map.put("availableBalance", availableBalance);
        map.put("monthWithdraw", monthWithdraw);

        return ParamUtils.resultMap(true, map);
    }

    /**
     * 订单状态探测器
     *
     * @return
     */
    @Override
    public String detector() {
        /*
        List<AgentTaskModel> agentTasks = insbAgentTaskDao.queryTasks();
        LogUtil.info("execute detector method ！");
        //SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
       //Calendar ca = Calendar.getInstance();

        for (AgentTaskModel agentTask : agentTasks) {
            LogUtil.info("Process TaskID " + agentTask.getTaskid());
            INSBAccount insbAccount = insbAccountService.initAccount(agentTask.getAgentid());

            if (!insbCommissionDao.isCompleted(agentTask.getTaskid())) {

                Double commercialCommission = 0D;
                Double compulsoryCommission = 0D;
                Double adCommercialCommission = 0D;
                Double adCompulsoryCommission = 0D;


                Map<String, Object> map = new HashMap<String, Object>();
                map.put("taskid", agentTask.getTaskid());
                map.put("agreementid", agentTask.getAgreementid());

                List<INSBCommission> commissions = insbCommissionDao.queryCommissions(map);
                for (INSBCommission commission : commissions) {
                    switch (commission.getCommissiontype()) {
                        case "01":
                            commercialCommission = commission.getCounts();
                            break;
                        case "02":
                            adCommercialCommission = commission.getCounts();
                            break;
                        case "03":
                            compulsoryCommission = commission.getCounts();
                            break;
                        case "04":
                            adCompulsoryCommission = commission.getCounts();
                            break;
                    }
                }

                String noti = "任务号：" + agentTask.getTaskid();
                Double amount = commercialCommission + compulsoryCommission;

                if (intoAccountDetails(insbAccount.getId(), "1", "101", amount, noti, "admin")) {
                    if (agentTask.getReferrerid() != null) {
                        Double amountRef = adCommercialCommission + adCompulsoryCommission;
                        INSBAccount insbAccountRef = insbAccountService.initAccount(agentTask.getReferrerid());
                        intoAccountDetails(insbAccountRef.getId(), "1", "104", amountRef, noti, "admin");
                    }
                    insbCommissionDao.completeTask(agentTask.getTaskid());
                }

            }

            //已用红包兑现
            this.sendRedPackets(agentTask.getTaskid(),agentTask.getProvidercode(),insbAccount.getId());

            //发放可奖励红包
            this.giveRedPackets(agentTask.getTaskid(),agentTask.getAgentid(),agentTask.getProvidercode());

            insbAgentTaskDao.completeTask(agentTask.getTaskid());
        }
        //ca = Calendar.getInstance();
        */
        return ParamUtils.resultMap(true, "数据处理完成");
    }

    /**
     * 任务完成生成佣金、发放红包奖励、获取红包
     *
     * @return
     */
    @Override
    public String genCommissionAndRedPackets(String taskid,String providercode) {
       //任务99关联的mini营销活动代码发布cm后台
        Map<String,Object> qMap = new HashMap<String,Object>();
        qMap.put("taskid",taskid);
        qMap.put("providercode", providercode);
        List<AgentTaskModel> agentTasks =insbAgentTaskDao.queryCompletedTaskByTaskId(qMap);
        String agentid = "";
        Map<String,Object> logMap = new HashMap<>();
        logMap.put("method","genCommissionAndRedPackets");
        logMap.put("taskid",taskid);
        logMap.put("providercode",providercode);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar ca = Calendar.getInstance();
        logMap.put("beginDate",df.format(ca.getTime()));
        logMap.put("agentTasks",agentTasks);
        if(agentTasks!=null&&agentTasks.size()>0){
            AgentTaskModel agentTask = agentTasks.get(0);

            INSBAccount insbAccount = insbAccountService.initAccount(agentTask.getAgentid());
            logMap.put("insbAccount",insbAccount);
            if (!insbCommissionDao.isCompleted(agentTask.getTaskid())) {

                Double commercialCommission = 0D;
                Double compulsoryCommission = 0D;
                Double adCommercialCommission = 0D;
                Double adCompulsoryCommission = 0D;


                //支付前置，承保后重算佣金
                this.reGenTaskCommission(agentTask);


                Map<String, Object> map = new HashMap<String, Object>();
                map.put("taskid", agentTask.getTaskid());
                map.put("agreementid", agentTask.getAgreementid());

                List<INSBCommission> commissions = insbCommissionDao.queryCommissions(map);
                logMap.put("commissions",commissions);
                for (INSBCommission commission : commissions) {
                    switch (commission.getCommissiontype()) {
                        case "01":
                            commercialCommission = commission.getCounts();
                            break;
                        case "02":
                            adCommercialCommission = commission.getCounts();
                            break;
                        case "03":
                            compulsoryCommission = commission.getCounts();
                            break;
                        case "04":
                            adCompulsoryCommission = commission.getCounts();
                            break;
                    }
                }

                String noti = this.getNoti(taskid,"","","101");
                logMap.put("noti101",noti);
                Double amount = commercialCommission + compulsoryCommission;

                if (intoAccountDetails(insbAccount.getId(), "1", "101", amount, noti, "admin",taskid)) {
                    logMap.put("noti101",noti);
                    if (agentTask.getReferrerid() != null) {
                        Double amountRef = adCommercialCommission + adCompulsoryCommission;
                        INSBAccount insbAccountRef = insbAccountService.initAccount(agentTask.getReferrerid());
                        noti = this.getNoti(taskid,"","","104");
                        logMap.put("noti104",noti);
                        intoAccountDetails(insbAccountRef.getId(), "1", "104", amountRef, noti, "admin",taskid);
                        //发送赏金到账消息
                        try{
                          String result =  wxMsgTemplateService.sendRewardMsg(taskid, providercode, agentTask.getAgentid(), String.valueOf(amountRef), "04", agentTask.getReferrerid(),0);
                            logMap.put("msgResult04",result);
                        }catch (Exception e){
                            logMap.put("sendRewardMsgError04",e.getMessage());
                        }
                        //发送赏金到账消息结束
                    }

                    insbCommissionDao.completeTask(agentTask.getTaskid());
                    logMap.put("completeTask",agentTask.getTaskid());
                    //发送酬金到账消息
                    try{
                       String result2 = wxMsgTemplateService.sendRewardMsg(taskid, providercode, agentTask.getAgentid(), String.valueOf(amount), "03", "",0);
                        logMap.put("msgResult03",result2);
                    }catch (Exception e){
                        logMap.put("sendRewardMsgError03",e.getMessage());
                    }
                    //发送酬金到账消息结束

                }
            }

            //已用红包兑现
            this.sendRedPackets(agentTask.getTaskid(), agentTask.getProvidercode(), insbAccount.getId());
            //
            logMap.put("sendRedPackets","end");
            //发放可奖励红包
            this.giveRedPackets(agentTask.getTaskid(), agentTask.getAgentid(), agentTask.getProvidercode());
            logMap.put("giveRedPackets", "end");

            insbAgentTaskDao.completeTask(agentTask.getTaskid());
            logMap.put("completeTask", "end");
            agentid = agentTask.getAgentid();
        }
        ca = Calendar.getInstance();
        logMap.put("EndDate", df.format(ca.getTime()));

        //发送承保成功消息
        try {
            wxMsgTemplateService.sendInsuredMsg(taskid, providercode, agentid);
        }catch (Exception e){
            logMap.put("sendInsuredMsgError",e.getMessage());
        }
        //发送承保成功消息 结束
        LogUtil.info(JsonUtils.serialize(logMap));
        return ParamUtils.resultMap(true, "数据处理完成");
    }

    //将已经支付的的红包状态从临时状态9改成锁定状态5
    public String refreshRedPackets(String taskid,String providercode){
        Map<String,Object> logMap = new HashMap<>();
        logMap.put("method","refreshRedPackets");
        logMap.put("taskid",taskid);
        logMap.put("providercode",providercode);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar ca = Calendar.getInstance();
        logMap.put("refreshRedPacketsBeginDate",df.format(ca.getTime()));
        String agentid = "";
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("taskid",taskid);
            map.put("providercode",providercode);
            map.put("status","9");
            List<INSBAgentPrize> prizeList = insbAgentPrizeDao.queryPrizeByAgentIdAndTask(map);
            logMap.put("prizeList",prizeList);
            for (INSBAgentPrize prize : prizeList) {
                prize.setStatus("5");//"5" 锁定红包状态
                insbAgentPrizeService.updateById(prize);
                agentid = prize.getAgentid();
                //锁定佣金
                //支付前置后无需锁定佣金
                /*
                try {
                    AgentTaskModel agentTaskModel = new AgentTaskModel();
                    agentTaskModel.setTaskid(prize.getTaskid());
                    if(!insbCommissionService.isLocked(agentTaskModel)){
                        insbCommissionService.lockTaskCommission(agentTaskModel);
                        LogUtil.info("execute refreshRedPackets lockTaskCommission complete ");
                        message.append(" lockTaskCommission complete ,");
                    }
                }catch (Exception ex){
                    message.append(" execute refreshRedPackets insbCommissionService error ").append(ex.getMessage());
                }*/
            }
            //将已选奖券3 待发放状态 改成4已支付待发放状态
            this.updateStatusToFour(taskid, providercode);
            logMap.put("updateStatusToFourEndDate",df.format(ca.getTime()));
        }catch(Exception e ){
            logMap.put("errMsg",e.getMessage());
        }

        //调用支付完成消息模板 支付前置后由渠道service触发发送
      /*  try {
            if(StringUtil.isEmpty(agentid)){
                INSBAgentTask insbAgentTask = new INSBAgentTask();
                insbAgentTask.setTaskid(taskid);
                insbAgentTask = insbAgentTaskDao.selectOne(insbAgentTask);
                if(insbAgentTask!=null) {
                    agentid = insbAgentTask.getAgentid();
                }
            }
            INSBOrder order = new INSBOrder();
            order.setTaskid(taskid);
            order.setPrvid(providercode);
            order = insbOrderService.queryOne(order);
            wxMsgTemplateService.sendPaidMsg(taskid, providercode, agentid,String.valueOf(order.getTotalpaymentamount()),"01");//01 正常支付完成
        }catch (Exception e){
            LogUtil.info("send paidmsg error [taskid="+taskid+"] "+e.getMessage());
        }
        */
        //调用支付完成消息模板结束

        ca = Calendar.getInstance();
        logMap.put("refreshRedPacketsEndDate", df.format(ca.getTime()));
        LogUtil.info(JsonUtils.serialize(logMap));
        return ParamUtils.resultMap(true, "数据处理完成");
    }

    private void updateStatusToFour(String taskid,String providercode){
        try{
            LogUtil.info("updateStatusToFour called:"+taskid+" "+providercode);
            INSBAgentPrize insbAgentPrize = new INSBAgentPrize();
            insbAgentPrize.setTaskid(taskid);
            insbAgentPrize.setProvidercode(providercode);
            insbAgentPrize.setStatus("3");//待发放
            insbAgentPrize = insbAgentPrizeService.queryOne(insbAgentPrize);
            if(null != insbAgentPrize && taskid.equals(insbAgentPrize.getTaskid())){
                insbAgentPrize.setStatus("4");//支付完成改成 已支付待发放
                insbAgentPrizeService.updateById(insbAgentPrize);
            }
        }catch (Exception e){
            LogUtil.info("updateStatusToFour:"+e.getMessage());
        }
    }

    //将已经支付的的红包状态从临时状态9改成锁定状态5
    public String refreshRedPackage(){
       /* try {
            LogUtil.info("execute refreshRedPackage method ！");
            Map<String, Object> map = new HashMap<String, Object>();
            List<INSBAgentPrize> prizeList = insbAgentPrizeDao.queryPaidOrder(map);
            for (INSBAgentPrize prize : prizeList) {
                LogUtil.info("taskid = "+prize.getTaskid() +" agentprizeid="+prize.getId());
                prize.setStatus("5");//"5" 锁定红包状态
                insbAgentPrizeService.updateById(prize);

                //锁定佣金
                try {
                    AgentTaskModel agentTaskModel = new AgentTaskModel();
                    agentTaskModel.setTaskid(prize.getTaskid());
                    if(!insbCommissionService.isLocked(agentTaskModel)){
                        insbCommissionService.lockTaskCommission(agentTaskModel);
                        LogUtil.info("execute refreshRedPackage lockTaskCommission complete ");

                     }
                }catch (Exception ex){
                    LogUtil.info("execute refreshRedPackage insbCommissionService error "+ex.getMessage());
                }

                //
            }
        }catch(Exception e ){
            e.printStackTrace();
            LogUtil.info(" execute refreshRedPackage method error "+e.getMessage());
        }*/
        return ParamUtils.resultMap(true, "数据处理完成");
    }

    public String sendRedPackets(String taskid,String providercode,String acountid){
        //已用红包兑现
        try {
            LogUtil.info("sendRedPackets task="+taskid+" providercode="+providercode+" acountid="+acountid);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("taskid", taskid);
            map.put("providercode", providercode);
            List<AgentPrizeModel> agentPrizes = insbAgentPrizeDao.queryAgentPrizes(map);
            for (AgentPrizeModel prize : agentPrizes) {
                Double amount = prize.getCounts();
                String noti = this.getNoti(taskid,"","奖券","102");
                if (intoAccountDetails(acountid, "1", "102", amount, noti, "admin",taskid)) {
                    insbAgentPrizeDao.completeTask(taskid, prize.getActivityprizeid());
                    LogUtil.info("sendRedPackets success task="+taskid+" providercode="+providercode+" acountid="+acountid);
                    //已用红包兑现后发送消息
                    try {
                        wxMsgTemplateService.sendRewardMsg(taskid, providercode, prize.getAgentid(), amount.toString(), "05", "",0);
                    }catch (Exception e){
                        LogUtil.info("sendRewardMsg error!");
                    }
                    //已用红包兑现后发送消息结束
                }
            
           }
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.info("sendRedPackets！error "+e.getMessage());
            throw e;
        }
        return ParamUtils.resultMap(true, "数据处理完成");
    }

    public String giveRedPackets(String taskid,String agentid,String providercode){
        try {
            LogUtil.info("giveRedPackets task="+taskid+" providercode="+providercode+" agentid="+agentid);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("taskid", taskid);
            map.put("agentid", agentid);
            map.put("providercode", providercode);
            map.put("status", "5");
            List<INSBAgentPrize> insbAgentPrizeList = insbAgentPrizeDao.queryPrizeByAgentIdAndTask(map);
            
            Double autoUseAmount = 0d;
            Double notUseAmount = 0d;
            int redPacketCount = 0;
            
            
            for (INSBAgentPrize agentPrize : insbAgentPrizeList) {
                //调用资金流水接口
                ActivityPrizesVo activityPrizesVo = new ActivityPrizesVo();
                activityPrizesVo.setAgentprizeid(agentPrize.getId());
                activityPrizesVo.setTaskid(taskid);
                activityPrizesVo.setProvidercode(providercode);
                List<Map<Object, Object>> infoList = insbAgentPrizeDao.queryAgentPrizeList(BeanUtils.toMap(activityPrizesVo));
                for (Map<Object, Object> prize : infoList) {
                    if ("1".equals(prize.get("autouse"))) {
                        //自动使用 调用资金流水接口
                        //使用符合规则的红包
                        AgentTaskModel agentTaskModel = new AgentTaskModel();
                        agentTaskModel.setAgentid(agentPrize.getAgentid());
                        agentTaskModel.setTaskid(activityPrizesVo.getTaskid());
                        agentTaskModel.setProvidercode(activityPrizesVo.getProvidercode());
                        Map<String, Object> jparams = insbConditionsService.queryParams(agentTaskModel);
                        if (insbConditionsService.execute("03", (String) prize.get("prizeid"), jparams)) {
                            //调用资金流水接口
                            INSBAccount insbAccount = insbAccountService.initAccount(agentTaskModel.getAgentid());
                            Double amount = agentPrize.getCounts();
                            autoUseAmount = autoUseAmount + amount;//20160723 add
                            if ("奖金".equals((String) prize.get("prizetype"))) {
                                //String noti = "自动现金奖励 任务号：" + taskid + " 奖品ID "+agentPrize.getId()+" " + (String) prize.get("prizename");
                                String activityName = (String)prize.get("activityname");
                                String noti = this.getNoti(taskid,activityName,"奖金","103");
                                intoAccountDetails(insbAccount.getId(), "1", "103", amount, noti, "admin",taskid);
                            } else {
                                String noti = this.getNoti(taskid,"","自动奖券兑现","102");
                                intoAccountDetails(insbAccount.getId(), "1", "102", amount, noti, "admin",taskid);
                            }
                            agentPrize.setStatus("1");//已使用状态
                            agentPrize.setUsetime(new Date());
                            agentPrize.setGettime(new Date());
                            agentPrize.setModifytime(new Date());
                        } else {
                        	notUseAmount = notUseAmount +  agentPrize.getCounts();//20160723 add 合并奖券发送一条消息
                        	redPacketCount += 1;
                        	agentPrize.setGettime(new Date());
                            agentPrize.setStatus("0");//未使用
                        }
                    } else if ("0".equals(prize.get("autouse"))) {
                    	notUseAmount = notUseAmount +  agentPrize.getCounts();//20160723 add 合并奖券发送一条消息
                    	redPacketCount += 1;
                        agentPrize.setStatus("0");//未使用
                        agentPrize.setGettime(new Date());
                    }
                    agentPrize.setTaskid("");
                    agentPrize.setProvidercode("");
                    agentPrize.setNoti("获取任务号：taskid="+taskid+"; providercode="+providercode);
					agentPrize.setGettime(new Date());
                    insbAgentPrizeService.updateById(agentPrize);
                    LogUtil.info("giveRedPackets success agentprizeid="+agentPrize.getId());
                    
                }
            }
            
          //发送奖券消息
            try{
            	 //发放汇总未使用奖券
                if(notUseAmount>0){
                    wxMsgTemplateService.sendRewardMsg(taskid, providercode, agentid, String.valueOf(notUseAmount), "01", "",redPacketCount);
                }
                
                //发放汇总自动使用的奖金
                if(autoUseAmount>0){
                    wxMsgTemplateService.sendRewardMsg(taskid, providercode, agentid, String.valueOf(autoUseAmount),"02","",0);
                }
                
            }catch (Exception e){
                LogUtil.info("sendRewardMsg error!");
            }
            //发送奖券消息结束
            
            
            //承保完成后，删除该taskID其他providercode status为9的临时奖品
            map.put("status", "9");
            insbAgentPrizeDao.deleteTempAgentPrize(map);
        }catch (Exception e){
            e.printStackTrace();
            LogUtil.info("giveRedPackets！error "+e.getMessage());
            throw e;
        }
        return ParamUtils.resultMap(true, "数据处理完成");
    }

    /**
     * 添加流水记录
     *
     * @param accountId
     * @param fundType
     * @param fundSource
     * @param amount
     * @param noti
     * @param operator
     * @return
     */
    @Override
    public Boolean intoAccountDetails(String accountId, String fundType, String fundSource, Double amount, String noti, String operator,String taskid) {
        if (amount > 0) {

            if (!insbAccountService.exist(accountId))
                return false;

            try {
                Thread.sleep(1200);
            } catch (Exception e) {
                return false;
            }

            Double balance = insbAccountDetailsDao.queryBalance(accountId);

            switch (fundType) {
                case "1":
                    balance = balance + amount;
                    break;
                case "2":
                    balance = balance - amount;
                    break;
                default:
                    return false;
            }

            INSBAccountDetails accountDetails = new INSBAccountDetails();
            accountDetails.setId(UUIDUtils.random());
            accountDetails.setAccountid(accountId);
            accountDetails.setOperator(operator);
            accountDetails.setFundtype(fundType);
            accountDetails.setFundsource(fundSource);
            accountDetails.setAmount(amount);
            accountDetails.setBalance(balance);
            accountDetails.setNoti(noti);
            accountDetails.setTaskid(taskid);
            accountDetails.setCreatetime(new Date());
            return insbAccountDetailsDao.intoDetails(accountDetails) > 0;
        } else
            return true;
    }

    /**
     *
     * @param startDate
     * @param endDate
     * @return
     */
    @Override
    public List<Map<String,Object>> queryBusinessList(String startDate,String endDate){
        HashMap<String,Object> map = new HashMap<String,Object>();
        map.put("startDate",startDate);
        map.put("endDate",endDate);
        return insbAccountDetailsDao.queryBusinessList(map);
    }

    /**
     *
     * @param startDate
     * @param endDate
     * @return
     */
    @Override
    public List<Map<String,Object>> queryWithdrawListByDate(String startDate,String endDate,String withdrawStatus){
        HashMap<String,Object> map = new HashMap<String,Object>();
        map.put("startDate",startDate);
        map.put("endDate",endDate);
        map.put("withdrawStatus",withdrawStatus);
        return insbAccountDetailsDao.queryWithdrawListByDate(map);
    }

    /**
     *
     * @param startDate
     * @param endDate
     * @return
     */
    @Override
    public List<Map<String,Object>> queryCashPrizeListByDate(String startDate,String endDate){
        HashMap<String,Object> map = new HashMap<String,Object>();
        map.put("startDate",startDate);
        map.put("endDate",endDate);
        return insbAccountDetailsDao.queryCashPrizeListByDate(map);
    }

    /**
     *
     * @param startDate
     * @param endDate
     * @return
     */
    @Override
    public List<Map<String,Object>> queryPersonalWealthByDate(String startDate,String endDate){
        HashMap<String,Object> map = new HashMap<String,Object>();
        map.put("startDate",startDate);
        map.put("endDate",endDate);
        return insbAccountDetailsDao.queryPersonalWealthByDate(map);
    }

    /**
     *
     * @param startDate
     * @return
     */
    @Override
    public List<Map<String,Object>> queryPersonalWealthByMonth(String startDate){
        HashMap<String,Object> map = new HashMap<String,Object>();
        map.put("startDate",startDate);
        return insbAccountDetailsDao.queryPersonalWealthByMonth(map);
    }

    /**
     *
     * @param startDate
     * @return
     */
    @Override
    public List<Map<String,Object>> querySumBusinessByMonth(String startDate){
        HashMap<String,Object> map = new HashMap<String,Object>();
        map.put("startDate",startDate);
        return insbAccountDetailsDao.querySumBusinessByMonth(map);
    }

    /**
     *
     * @param taskid
     * @return
     */
    @Override
    public List<Map<String,Object>> queryCommissionAndPrizeByTask(String taskid,String agentid){
        HashMap<String,Object> map = new HashMap<String,Object>();
        map.put("taskid",taskid);
        map.put("agentid",agentid);
        return insbAccountDetailsDao.queryCommissionAndPrizeByTask(map);
    }


    private String reGenTaskCommission(AgentTaskModel agentTaskModel) {
        String commercialCommissionRateId = "";
        String compulsoryCommissionRateId = "";
        String adCommercialCommissionRateId = "";
        String adCompulsoryCommissionRateId = "";

        Double commercialCommission = 0D;
        Double compulsoryCommission = 0D;
        Double adCommercialCommission = 0D;
        Double adCompulsoryCommission = 0D;

        Double commercialCommissionRate = 1D;
        Double compulsoryCommissionRate = 1D;

        agentTaskModel.setAgreementid(insbConditionsService.queryAgreementIdByTask(agentTaskModel));
        if (agentTaskModel.getAgreementid() == null || agentTaskModel.getAgreementid().equals("")) {
            return ParamUtils.resultMap(false, "任务信息不正确");
        }
            Map<String, Object> params = insbConditionsService.queryParams(agentTaskModel);

            Double commercialPremium = Double.parseDouble(params.getOrDefault("commercialPremium", "0").toString());
            Double compulsoryPremium = Double.parseDouble(params.getOrDefault("compulsoryPremium", "0").toString());
            List<INSBCommissionRate> commissionRateList = insbCommissionRateDao.queryCommissionByAgreement(agentTaskModel.getAgreementid(),"minizzb","minizzb");
            for (INSBCommissionRate commissionRate : commissionRateList) {
                if (insbConditionsService.execute("02", commissionRate.getId(), params)) {
                    Double rate = commissionRate.getRate();
                    if (commissionRate.getCommissiontype().equals("01") && rate <= commercialCommissionRate) {
                        commercialCommissionRateId = commissionRate.getId();
                        commercialCommissionRate = rate;
                        commercialCommission = commercialPremium * rate;
                    }
                    if (commissionRate.getCommissiontype().equals("03") && rate <= compulsoryCommissionRate) {
                        compulsoryCommissionRateId = commissionRate.getId();
                        compulsoryCommissionRate = rate;
                        compulsoryCommission = compulsoryPremium * rate;
                    }
                }
            }
            if (commercialCommission > 0)
                adCommercialCommission = commercialPremium * 0.03;

            insbCommissionService.deleteCommissionByTask(agentTaskModel);
            if (!commercialCommissionRateId.equals(""))
                updateCommission(agentTaskModel, "01", commercialCommissionRateId, (double) commercialCommission.intValue());
            if (!compulsoryCommissionRateId.equals(""))
                updateCommission(agentTaskModel, "03", compulsoryCommissionRateId, (double) compulsoryCommission.intValue());

            if (commercialCommission > 0)
                updateCommission(agentTaskModel, "02", adCommercialCommissionRateId, (double) adCommercialCommission.intValue());
        LogUtil.info("reGenTaskCommission success !taskid="+agentTaskModel.getTaskid());

        return "sucess";
    }

    private void updateCommission(AgentTaskModel agentTaskModel, String commissionType, String commissionRateId, Double counts) {
        INSBCommission commission = new INSBCommission();
        commission.setTaskid(agentTaskModel.getTaskid());
        commission.setAgreementid(agentTaskModel.getAgreementid());
        commission.setCommissiontype(commissionType);
        commission.setCommissionrateid(commissionRateId);
        commission.setCounts(counts);
        commission.setStatus("1");//承保完成锁定
        commission.setOperator("admin");
        insbCommissionService.saveCommission(commission);
    }

    private String getNoti(String taskid,String activityname,String prizetype,String type){
        String noti = "";
        try {
            Map<String, Object> queryMap = new HashMap<String, Object>();
            queryMap.put("taskid", taskid);
            List<Map<String, Object>> results = insbAccountDetailsDao.queryBusinessListByTaskid(queryMap);
            Map<String, Object> result = new HashMap<String, Object>();
            if (null != results && results.size() > 0) {
                result = results.get(0);
                LogUtil.info(JsonUtils.serialize("获取noti资料--初始资料："+results));
            }else {
                result.put("任务号：",taskid);
            }
            switch (type) {
                case "101":   /*酬金*/
                    if (result.containsKey("推荐人")) {
                        result.remove("推荐人");
                    }
                    if (result.containsKey("赏金率")) {
                        result.remove("赏金率");
                    }
                    noti = JsonUtils.serialize(result);
                    break;
                case "102":  /*奖券兑现*/
                    if (result.containsKey("推荐人")) {
                        result.remove("推荐人");
                    }
                    if (result.containsKey("赏金率")) {
                        result.remove("赏金率");
                    }
                    if (result.containsKey("商业险酬金率")) {
                        result.remove("商业险酬金率");
                    }
                    if (result.containsKey("交强险酬金率")) {
                        result.remove("交强险酬金率");
                    }
                    result.put("奖券类型", prizetype);
                    noti = JsonUtils.serialize(result);
                    break;
                case "103":  /*奖金*/
                    if (result.containsKey("推荐人")) {
                        result.remove("推荐人");
                    }
                    if (result.containsKey("赏金率")) {
                        result.remove("赏金率");
                    }
                    if (result.containsKey("商业险酬金率")) {
                        result.remove("商业险酬金率");
                    }
                    if (result.containsKey("交强险酬金率")) {
                        result.remove("交强险酬金率");
                    }
                    result.put("奖金类型", prizetype);
                    result.put("活动名称", activityname);
                    noti = JsonUtils.serialize(result);
                    break;
                case "104":  /*赏金*/
                    if (result.containsKey("商业险酬金率")) {
                        result.remove("商业险酬金率");
                    }
                    if (result.containsKey("交强险酬金率")) {
                        result.remove("交强险酬金率");
                    }
                    noti = JsonUtils.serialize(result);
                    break;
                case "201":  /*提现*/

                default:
                    ;
            }
        }catch (Exception e){
            LogUtil.info("获取备注失败 taskid="+taskid+" errmsg="+e.getMessage());
        }
        LogUtil.info("获取noti资料："+noti+" taskid="+taskid);
        return noti;
    }

}