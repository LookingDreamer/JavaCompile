package com.zzb.extra.service.impl;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.cninsure.core.utils.BeanUtils;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.UUIDUtils;
import com.cninsure.system.entity.INSCCode;
import com.cninsure.system.service.INSCCodeService;
import com.zzb.chn.util.JsonUtils;
import com.zzb.conf.entity.INSBOrderpayment;
import com.zzb.conf.service.INSBAgentService;
import com.zzb.conf.service.INSBOrderpaymentService;
import com.zzb.extra.controller.vo.ActivityPrizesVo;
import com.zzb.extra.dao.INSBAgentPrizeDao;
import com.zzb.extra.dao.INSBAgentTaskDao;
import com.zzb.extra.dao.INSBMarketActivityDao;
import com.zzb.extra.dao.INSBMarketActivityPrizeDao;
import com.zzb.extra.entity.INSBAccount;
import com.zzb.extra.entity.INSBAgentPrize;
import com.zzb.extra.entity.INSBAgentTask;
import com.zzb.extra.entity.INSBMarketActivity;
import com.zzb.extra.model.AgentTaskModel;
import com.zzb.extra.service.INSBAccountDetailsService;
import com.zzb.extra.service.INSBAccountService;
import com.zzb.extra.service.INSBAgentPrizeService;
import com.zzb.extra.service.INSBCommissionService;
import com.zzb.extra.service.INSBConditionsService;
import com.zzb.extra.service.INSBMarketActivityService;
import com.zzb.extra.service.WxMsgTemplateService;
import com.zzb.extra.util.ParamUtils;

import net.sf.json.JSONObject;

@Service
@Transactional
public class INSBMarketActivityServiceImpl extends BaseServiceImpl<INSBMarketActivity> implements
        INSBMarketActivityService,Serializable {
    @Resource
    private INSBMarketActivityDao insbMarketActivityDao;

    @Resource
    private INSBAgentTaskDao insbAgentTaskDao;

    @Resource
    private INSBMarketActivityPrizeDao insbMarketActivityPrizeDao;

    @Resource
    private INSBAgentPrizeDao insbAgentPrizeDao;

    @Resource
    private INSBAccountDetailsService insbAccountDetailsServiceImpl ;

    @Resource
    private INSBConditionsService insbConditionsService;

    @Resource
    private INSCCodeService codeService;

    @Resource
    private INSBAgentService insbAgentService;

    @Resource
    private WxMsgTemplateService wxMsgTemplateService;

    @Resource
    private INSBCommissionService insbCommissionService;

    @Resource
    private INSBAgentPrizeService insbAgentPrizeService;

    @Resource
    private INSBOrderpaymentService insbOrderpaymentService;

    @Resource
    private INSBAccountService insbAccountService;

    private static final String BUSINESSACTIVITY = "01";//交易活动

    private static final String REGEDITACTIVITY = "02";//注册活动

    private static final String SHAREACTIVITY = "03";//分享活动

    private static final String RECOMMENDACTIVITY= "04";//推荐活动

    private static final String LOTTERYACTIVITY= "05";//抽奖活动

    @Override
    protected BaseDao<INSBMarketActivity> getBaseDao() {
        return insbMarketActivityDao;
    }

    @Override
    public String queryActivity(Map<String, Object> map) {
        long total = insbMarketActivityDao.queryPagingListCount(map);
        List<Map<Object, Object>> infoList = insbMarketActivityDao.queryPagingList(map);
        return ParamUtils.resultMap(total, infoList);
    }

    @Override
    public String getList(Map map) {
        return null;
    }

    @Override
    public Map findById(String id) {
        return insbMarketActivityDao.findById(id);
    }

    @Override
    public void saveObject(INSBMarketActivity insbMarketActivity) {
        insbMarketActivityDao.saveObject(insbMarketActivity);
    }

    @Override
    public void updateObject(INSBMarketActivity insbMarketActivity) {
        insbMarketActivityDao.updateObject(insbMarketActivity);
    }

    @Override
    public void deleteObject(String id) {
        insbMarketActivityDao.deleteObject(id);
    }

    @Override
    public String queryActivityPrizeListById(String id) {
        long total = insbMarketActivityDao.queryActivityPrizeListCountById(id);
        List<Map<Object, Object>> infoList = insbMarketActivityDao.queryActivityPrizeListById(id);
        return ParamUtils.resultMap(total,infoList);
    }

    @Override
    public String queryActivityConditionListById(Map<Object, Object> map) {
        long total = insbMarketActivityDao.queryActivityCondCountById(map);
        List<Map<Object, Object>> infoList = insbMarketActivityDao.queryActivityCondListById(map);
        return ParamUtils.resultMap(total,infoList);
    }

    @Override
    public Map<String,Long> validatePrizeAndConditions(String id) {
        Map<Object,Object> map = new HashMap<Object,Object>();
        Map<String,Long> resultMap = new HashMap<String,Long>();
        map.put("id",id);
        map.put("conditionsource","01");
        long activityPrizeCount = insbMarketActivityDao.queryActivityPrizeListCountById(id);
        long activityConditionsCount = insbMarketActivityDao.queryActivityCondCountById(map);
        resultMap.put("activityPrizeCount",activityPrizeCount);
        resultMap.put("activityConditionsCount",activityConditionsCount);
        return resultMap;
    }

    @Override
    public String queryEffectiveActivity(Map<String, Object> map) {
        List<Map<Object,Object>> list = insbMarketActivityDao.queryEffectiveActivity(map);
        //<!--add refreshrefresh-->
        Long total = 0L;
        if(list!=null){
         total =  list.size()+0L;
        }
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("success", true);
        resultMap.put("total", total);
        resultMap.put("rows", list);
        JSONObject jsonObject = JSONObject.fromObject(resultMap);
        return  jsonObject.toString();
    }

    @Override
    public String queryTaskPrizes(ActivityPrizesVo activityPrizesVo) {
        //查询可获取奖励 && 接口传参operate=save时保存奖励
        //参数：activitytype/taskid/gettotal/operate
        String activityType = activityPrizesVo.getActivitytype();
        LogUtil.info("queryTaskPrizes called ：agentid="+activityPrizesVo.getAgentid()
                +"; activitytype="+activityType
                +"; taskid="+activityPrizesVo.getTaskid()
                +"; providercode="+activityPrizesVo.getProvidercode()
                +"; referrerid="+activityPrizesVo.getReferrerid()
                +"; Agentchannel="+activityPrizesVo.getAgentchannel()
                +"; operate="+activityPrizesVo.getOperate());
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("effectivetime", new Date());
        map.put("terminaltime", new Date());
        map.put("activitytype",activityType);
        map.put("taskid",activityPrizesVo.getTaskid());
        map.put("providercode",activityPrizesVo.getProvidercode());
        map.put("agentid",activityPrizesVo.getAgentid());
        map.put("jobnum",activityPrizesVo.getJobnum());
        boolean isCompletedTask = false;


        //01 交易活动
        if(BUSINESSACTIVITY.equals(activityType)) {
            //task 未结束,先删除临时存入到insbagentprize的奖品
            INSBAgentTask atask = new INSBAgentTask();
            atask.setTaskid(activityPrizesVo.getTaskid());
            atask.setAgentid(activityPrizesVo.getAgentid());
            atask.setStatus("1");
            isCompletedTask = this.isCompletedTask(atask);
            if(!isPaid(activityPrizesVo.getTaskid()) && !isCompletedTask && !"true".equals(activityPrizesVo.getPaid())){
                insbAgentPrizeService.deletePrizeByTaskId(map);
            }
            AgentTaskModel agentTaskModel = new AgentTaskModel();
            agentTaskModel.setAgentid(activityPrizesVo.getAgentid());
            agentTaskModel.setTaskid(activityPrizesVo.getTaskid());
            agentTaskModel.setProvidercode(activityPrizesVo.getProvidercode());
            agentTaskModel.setAgentChannel(activityPrizesVo.getAgentchannel());
            //封装task中投保供应商投保金额参数，用于活动规则比对
            Map<String, Object> params = queryTaskParams(agentTaskModel);
            return this.getPrizeList(map,params,activityPrizesVo,isCompletedTask);
        }
        else if(REGEDITACTIVITY.equals(activityType)){
            Map<String, Object> queryMap = new HashMap<String, Object>();
            //注册活动 需要参数:agentid activitytype=02 operate=save reffer;
            List<Map<Object,Object>> agent = null;
            if(activityPrizesVo.getAgentid()==null||"".equals(activityPrizesVo.getAgentid())){
                queryMap.put("jobnum",activityPrizesVo.getJobnum());
                agent = insbAgentPrizeDao.queryAgent(queryMap);
                activityPrizesVo.setAgentid((agent!=null&&agent.size()>0)?(agent.get(0)).get("id").toString():"000");
            }

            String result = "";
            String referrerid= activityPrizesVo.getReferrerid();
            //规则条件 BINGIN
            AgentTaskModel agentTaskModel = new AgentTaskModel();
            agentTaskModel.setAgentid(activityPrizesVo.getAgentid());
            agentTaskModel.setAgentChannel(activityPrizesVo.getAgentchannel());
            //封装task中投保供应商投保金额参数，用于活动规则比对
            Map<String, Object> params = queryTaskParams(agentTaskModel);
            //规则条件 END//<!--add-->
            /*for(Map.Entry para:params.entrySet()){
                System.out.println("key="+para.getKey()+" value="+para.getValue());
            }*/
            result = this.getPrizeList(map, params, activityPrizesVo,isCompletedTask);
            if(referrerid!=null&&!"".equals(referrerid)&&"save".equals(activityPrizesVo.getOperate())) {
                //注册完成，触发推荐分享活动，将满足获取条件的奖品发放给推荐人
                //referrer获取推荐人的jobnum /agentid
                queryMap.clear();
                queryMap.put("agentid", referrerid);
                //获取推荐人资料
                agent = insbAgentPrizeDao.queryAgent(queryMap);
                if(agent!=null&&agent.size()>0){
                    //System.out.println("save referrerid 红包 id=="+referrerid);
                    //activityPrizesVo.setJobnum(referrerid);//设置jobnum为推荐人jobnum
                    activityPrizesVo.setAgentid((agent.get(0)).get("id").toString());
                    activityPrizesVo.setOperate("save");//保存操作
                    activityPrizesVo.setActivitytype("04");//04推荐活动
                    map.clear();
                    map.put("effectivetime", new Date());
                    map.put("terminaltime", new Date());
                    map.put("activitytype",activityPrizesVo.getActivitytype());
                    //map.put("taskid",activityPrizesVo.getTaskid());
                    map.put("agentid",activityPrizesVo.getAgentid());
                   // map.put("jobnum",activityPrizesVo.getJobnum());
                    //规则条件BINGIN
                    AgentTaskModel agentRegeditTaskModel = new AgentTaskModel();
                    agentRegeditTaskModel.setAgentid(activityPrizesVo.getAgentid());
                    agentTaskModel.setAgentChannel(activityPrizesVo.getAgentchannel());
                    //封装task中投保供应商投保金额参数，用于活动规则比对
                    params = queryTaskParams(agentRegeditTaskModel);
                    /*for(Map.Entry param :params.entrySet()){
                        System.out.println("key="+param.getKey() +" value="+param.getValue() +"id="+activityPrizesVo.getAgentid());
                    }*/
                    //System.out.println("推荐人"+referrer+" =="+this.getPrizeList(map,params,activityPrizesVo));
                    //规则条件 END
                    this.getPrizeList(map, params, activityPrizesVo,isCompletedTask);
                }
            }
            return result;
        }
        else if(SHAREACTIVITY.equals(activityType)){
            //分享活动
            //规则条件 BINGIN
            AgentTaskModel agentTaskModel = new AgentTaskModel();
            agentTaskModel.setAgentid(activityPrizesVo.getAgentid());
            agentTaskModel.setTaskid(activityPrizesVo.getTaskid());
            agentTaskModel.setAgentChannel(activityPrizesVo.getAgentchannel());
            //封装task中投保供应商投保金额参数，用于活动规则比对
            Map<String, Object> params = queryTaskParams(agentTaskModel);
            //规则条件 END

            return this.getPrizeList(map,params,activityPrizesVo,isCompletedTask);
        }
        else if(RECOMMENDACTIVITY.equals(activityType)){
            //推荐活动
            AgentTaskModel agentTaskModel = new AgentTaskModel();
            agentTaskModel.setAgentid(activityPrizesVo.getAgentid());
            agentTaskModel.setAgentChannel(activityPrizesVo.getAgentchannel());
            //封装task中投保供应商投保金额参数，用于活动规则比对
            Map<String, Object> params = queryTaskParams(agentTaskModel);
            //System.out.println("推荐活动="+this.getPrizeList(map,params,activityPrizesVo));
            return this.getPrizeList(map,params,activityPrizesVo,isCompletedTask);
        }
        else if(LOTTERYACTIVITY.equals(activityType)){
            //抽奖活动
            AgentTaskModel agentTaskModel = new AgentTaskModel();
            agentTaskModel.setAgentid(activityPrizesVo.getAgentid());
            agentTaskModel.setAgentChannel(activityPrizesVo.getAgentchannel());
            //封装task中投保供应商投保金额参数，用于活动规则比对
            Map<String, Object> params = queryTaskParams(agentTaskModel);
            return this.getPrizeList(map,params,activityPrizesVo,isCompletedTask);
        }

        return ParamUtils.resultMap(false, "参数不正确");
    }

    /**
     * 获取任务参数
     *
     * @param agentTaskModel
     * @return
     */
    private Map<String, Object> queryTaskParams(AgentTaskModel agentTaskModel) {
        return insbConditionsService.queryParams(agentTaskModel);
    }

    private String getPrizeList(Map<String,Object> queryMap,Map<String,Object> params,ActivityPrizesVo activityPrizesVo,boolean isCompletedTask){
        //奖品类型
        List<INSCCode> marketPrizeTypeList = codeService.queryINSCCodeByCode("market", "prizeType");
        List<Map<Object, Object>> activityList = insbMarketActivityDao.queryEffectiveActivity(queryMap);
        //<!--add-->
        //String activityIdList = "";
        ArrayList activityIdList = new ArrayList();
        //获取符合规则的活动列表
        for (Map<Object, Object> activity : activityList) {
            if (insbConditionsService.execute("01", (String)activity.get("id"), params)){
                activityIdList.add((String)activity.get("id"));
                //activityIdList += (activityIdList.equals("") ? "" : "','") + (String)activity.get("id");
            }
        }
        Map<String,Object> queryMapTemp = new HashMap<String, Object>();
        Map<String,Object> resultMap = new HashMap<String,Object>();
        List<Map<Object, Object>> activityPrizeList = new ArrayList<Map<Object, Object>>();
        //无活动奖品
        //if(null==activityIdList||"".equals(activityIdList)){
        if(null==activityIdList||activityIdList.size()==0){
            resultMap.put("success", true);
            resultMap.put("total", 0);
            resultMap.put("rows", activityPrizeList);
            return JSONObject.fromObject(resultMap).toString();
        } else {
            queryMapTemp.put("activityidlist", activityIdList);
            long total = insbMarketActivityPrizeDao.queryPagingListCount(queryMapTemp);
            activityPrizeList = insbMarketActivityPrizeDao.queryPagingList(queryMapTemp);
            //task未完成，删除agentprize的奖品信息，重新插入
            Calendar ca = Calendar.getInstance();

            //01交易
            if((isPaid(activityPrizesVo.getTaskid()) || "true".equals(activityPrizesVo.getPaid())) && !isCompletedTask&&BUSINESSACTIVITY.equals(activityPrizesVo.getActivitytype())){
               //更新使用的奖品状态为待发放状态
                if(null!=activityPrizesVo.getAgentprizeid()&&!"".equals(activityPrizesVo.getAgentprizeid())) {
                    INSBAgentPrize insbAgentPrize = insbAgentPrizeService.queryById(activityPrizesVo.getAgentprizeid());
                    insbAgentPrize.setStatus("4");//"4" 已支付待发放状态  由后台自动job去查询发放
                    insbAgentPrize.setTaskid(activityPrizesVo.getTaskid());
                    insbAgentPrize.setProvidercode(activityPrizesVo.getProvidercode());
                    insbAgentPrizeService.updateById(insbAgentPrize);
                }
                //
                //锁定佣金
                AgentTaskModel agentTaskModel = new AgentTaskModel();
                agentTaskModel.setTaskid(activityPrizesVo.getTaskid());
                if(!insbCommissionService.isLocked(agentTaskModel)){
                    insbCommissionService.lockTaskCommission(agentTaskModel);
                }
                //
                queryMapTemp.clear();
                queryMapTemp.put("taskid",activityPrizesVo.getTaskid());
                queryMapTemp.put("providercode", activityPrizesVo.getProvidercode());
                queryMapTemp.put("status","9");
                List<Map<String,Object>> prizes = new ArrayList<Map<String,Object>>();
                List<INSBAgentPrize> agentPrizeList= insbAgentPrizeDao.queryPrizeByAgentIdAndTask(queryMapTemp);
                if(agentPrizeList!=null&&agentPrizeList.size()>0) {
                    for (INSBAgentPrize agentPrize : agentPrizeList) {
                        agentPrize.setStatus("5");//锁定状态
                        insbAgentPrizeService.updateById(agentPrize);
                    }
                }
              //  else{//当锁定后只查询锁定后的资料
                queryMapTemp.clear();
                queryMapTemp.put("taskid",activityPrizesVo.getTaskid());
                queryMapTemp.put("providercode", activityPrizesVo.getProvidercode());
                queryMapTemp.put("status","5");
                queryMapTemp.put("agentid",activityPrizesVo.getAgentid());
                prizes = insbAgentPrizeDao.queryAgentByTaskID(queryMapTemp);
                    //agentPrizeList= insbAgentPrizeDao.queryPrizeByAgentIdAndTask(queryMapTemp);
             //   }
                //锁定红包

                //锁定红包
                total = (null != prizes)?prizes.size():0;
                resultMap.clear();
                resultMap.put("success", true);
                resultMap.put("total", total);
                resultMap.put("rows", prizes);
               // resultMap.put("rows", agentPrizeList);
                return JSONObject.fromObject(resultMap).toString();
            }
            //
            Double autoUseAmount = 0d;
            Double notUseAmount = 0d;
            int redPacketCount = 0;
            
            for (Map<Object, Object> tempMap : activityPrizeList) {
                //交易活动时保存资料到insbagentprize 状态为：9 ，只要任务还没结束，每次都删除再Insert。
                //System.out.println("save success id= "+(String) tempMap.get("id"));
                INSBAgentPrize insbAgentPrize = new INSBAgentPrize();
                insbAgentPrize.setId(UUIDUtils.random());
                insbAgentPrize.setTaskid(activityPrizesVo.getTaskid());
                insbAgentPrize.setAgentid(activityPrizesVo.getAgentid());
                insbAgentPrize.setActivityprizeid((String) tempMap.get("id"));
                insbAgentPrize.setCounts(((BigDecimal) tempMap.get("counts")).doubleValue());
                insbAgentPrize.setOperator("admin");
                insbAgentPrize.setCreatetime(ca.getTime());
                if (BUSINESSACTIVITY.equals(activityPrizesVo.getActivitytype())) {
                    //交易奖励，任务未完成先设置奖品临时状态9，
                    if(!isPaid(activityPrizesVo.getTaskid()) && !isCompletedTask && !"true".equals(activityPrizesVo.getPaid())){
                        insbAgentPrize.setStatus("9");
                        insbAgentPrize.setTaskid(activityPrizesVo.getTaskid());
                        insbAgentPrize.setProvidercode(activityPrizesVo.getProvidercode());
                        insbAgentPrizeService.insert(insbAgentPrize);
                    }else{
                        System.out.println("this.isCompletedTask()="+isCompletedTask+" activityPrizesVo.getPaid()="+activityPrizesVo.getPaid());
                    }
                }else{
                    //
                    if("save".equals(activityPrizesVo.getOperate())){
                        insbAgentPrize.setStatus("0");
                        insbAgentPrizeService.insert(insbAgentPrize);
                        //System.out.println("save success "+insbAgentPrize.getAgentid());
                        //参数：agentid/agentprizeid
                        //根据agentprizeid获得prize autouse是自动使用还是手动使用
                        activityPrizesVo.setAgentprizeid(insbAgentPrize.getId());
                        List<Map<Object, Object>> infoList = insbAgentPrizeDao.queryAgentPrizeList(BeanUtils.toMap(activityPrizesVo));
                       // List<Map<Object, Object>> infoList = insbMarketActivityPrizeService.queryPagingList(BeanUtils.toMap(activityPrizesVo));
                        for(Map<Object, Object> prize : infoList){
                            if("1".equals(prize.get("autouse"))){
                                //自动使用 调用资金流水接口
                                //使用符合规则的红包
                                AgentTaskModel agentTaskModel = new AgentTaskModel();
                                agentTaskModel.setAgentid(activityPrizesVo.getAgentid());
                                agentTaskModel.setTaskid(activityPrizesVo.getTaskid());
                                agentTaskModel.setProvidercode(activityPrizesVo.getProvidercode());
                                agentTaskModel.setAgentChannel(activityPrizesVo.getAgentchannel());
                                Map<String, Object> jparams = queryTaskParams(agentTaskModel);
                                if (insbConditionsService.execute("03", (String)prize.get("prizeid"), jparams)) {
                                    //自动使用
                                    insbAgentPrize.setStatus("1");//已使用状态
									insbAgentPrize.setUsetime(ca.getTime());
                                    insbAgentPrize.setGettime(ca.getTime());
                                    insbAgentPrize.setModifytime(ca.getTime());
                                    //调用资金流水接口
                                    //调用资金流水接口
                                    //System.out.println("2==>MarketActivityServiceImpl调用资金流水接口");
                                    INSBAccount insbAccount = insbAccountService.initAccount(activityPrizesVo.getAgentid());
                                    Double amount = insbAgentPrize.getCounts();
                                    autoUseAmount = autoUseAmount + amount;//20160725 add
                                    String noti = "";
                                    String info = "";
                                    if("奖金".equals((String)prize.get("prizetype"))){
                                        if(this.RECOMMENDACTIVITY.equals(activityPrizesVo.getActivitytype())||this.SHAREACTIVITY.equals(activityPrizesVo.getActivitytype())){
                                            //noti = "推荐分享现金奖励：" + (String)prize.get("id") + " " + (String)prize.get("prizename");
                                            info = "推荐分享";
                                        }else if(this.REGEDITACTIVITY.equals(activityPrizesVo.getActivitytype())){
                                            //noti = "注册现金奖励：" + (String)prize.get("id") + " " + (String)prize.get("prizename");
                                            info = "注册";
                                        }else{
                                           // noti = "抽奖现金奖励：" + (String)prize.get("id") + " " + (String)prize.get("prizename");
                                            info = "抽奖";
                                        }
                                        Map<String,Object> notiMap = new HashMap<String,Object>();
                                        notiMap.put("活动名称",(String)prize.get("activityname"));
                                        notiMap.put("奖金类型",info+"奖金");
                                        noti = JsonUtils.serialize(notiMap);
                                        insbAccountDetailsServiceImpl.intoAccountDetails(insbAccount.getId(), "1", "103", amount, noti, "admin","");
                                        //wxMsgTemplateService.sendRewardMsg(insbAgentPrize.getAgentid(),String.valueOf(amount), "02", info);
                                    }else{
                                        if(this.RECOMMENDACTIVITY.equals(activityPrizesVo.getActivitytype())||this.SHAREACTIVITY.equals(activityPrizesVo.getActivitytype())){
                                            noti = "推荐分享红包奖励：" + (String)prize.get("id") + " " + (String)prize.get("prizename");
                                            info = "推荐分享";
                                        }else if(this.REGEDITACTIVITY.equals(activityPrizesVo.getActivitytype())){
                                            noti = "注册红包奖励：" + (String)prize.get("id") + " " + (String)prize.get("prizename");
                                            info = "注册";
                                        }else{
                                            noti = "抽奖红包奖励：" + (String)prize.get("id") + " " + (String)prize.get("prizename");
                                            info = "抽奖";
                                        }
                                        Map<String,Object> notiMap = new HashMap<String,Object>();
                                        notiMap.put("活动名称",(String)prize.get("activityname"));
                                        notiMap.put("奖券类型",info+"奖券");
                                        noti = JsonUtils.serialize(notiMap);
                                        insbAccountDetailsServiceImpl.intoAccountDetails(insbAccount.getId(), "1", "102", amount, noti, "admin","");
                                        //wxMsgTemplateService.sendRewardMsg(insbAgentPrize.getAgentid(), String.valueOf(amount), "05", info);
                                    }

                                    //调用资金流水接口
                                }else {
                                	notUseAmount = notUseAmount + insbAgentPrize.getCounts();//20160725 add 合并奖券发送一条消息
                                	redPacketCount += 1;
                                    insbAgentPrize.setStatus("0");//未使用
									insbAgentPrize.setGettime(new Date());
                                    //wxMsgTemplateService.sendRewardMsg(insbAgentPrize.getAgentid(), String.valueOf(insbAgentPrize.getCounts()), "01", activityPrizesVo.getActivitytype());
                                }
                            }else if("0".equals(prize.get("autouse"))){
                                insbAgentPrize.setStatus("0");//未使用
								insbAgentPrize.setGettime(new Date());
                                notUseAmount = notUseAmount + insbAgentPrize.getCounts();//20160725 add 合并奖券发送一条消息
                                redPacketCount += 1;
                                //wxMsgTemplateService.sendRewardMsg(insbAgentPrize.getAgentid(), String.valueOf(insbAgentPrize.getCounts()), "01", activityPrizesVo.getActivitytype());
                            }
                            insbAgentPrizeService.updateById(insbAgentPrize);
                        }
                    }
                }
                //是否获取各个类型的奖品分类总额
                if (activityPrizesVo.isGettotal()) {
                    for (INSCCode code : marketPrizeTypeList) {
                        if (code.getCodename().equals(tempMap.get("prizetype"))) {
                            //各个类型的奖品分类加总
                            BigDecimal counts = (BigDecimal) tempMap.get("counts");
                            resultMap.put(code.getCodename(), resultMap.get(code.getCodename()) == null ? counts.doubleValue() : (Double) resultMap.get(code.getCodename()) + counts.doubleValue());
                        }
                    }
                }
            }
            
            
            //
          //发送奖券消息
            try{
            	 //发放汇总未使用奖券
                if(notUseAmount>0){
                    wxMsgTemplateService.sendRewardMsg(activityPrizesVo.getAgentid(), String.valueOf(notUseAmount), "01", activityPrizesVo.getActivitytype(),redPacketCount);
                }
                
                //发放汇总自动使用的奖金
                if(autoUseAmount>0){
                    wxMsgTemplateService.sendRewardMsg(activityPrizesVo.getAgentid(), String.valueOf(autoUseAmount), "02", activityPrizesVo.getActivitytype(),0);
                }
                
            }catch (Exception e){
                LogUtil.info("sendRewardMsg error!");
            }
            //发送奖券消息结束
            //


            /*
            //TASKcomplete
            if(this.isCompletedTask()&&"01".equals(activityPrizesVo.getActivitytype())){
                queryMapTemp.clear();
                queryMapTemp.put("agentid", activityPrizesVo.getAgentid());
                queryMapTemp.put("taskid",activityPrizesVo.getTaskid());
                queryMapTemp.put("providercode",activityPrizesVo.getProvidercode());
                queryMapTemp.put("status","3"); //发放待发放奖励
                List<INSBAgentPrize> agentPrizeList = insbAgentPrizeDao.queryPrizeByAgentIdAndTask(queryMapTemp);
                for(INSBAgentPrize insbAgentPrize:agentPrizeList) {
                    activityPrizesVo.setAgentprizeid(insbAgentPrize.getId());
                    List<Map<Object, Object>> infoList = insbAgentPrizeDao.queryAgentPrizeList(BeanUtils.toMap(activityPrizesVo));
                    for(Map<Object, Object> prize : infoList){
                            //调用资金流水接口
                            AgentTaskModel agentTaskModel = new AgentTaskModel();
                            agentTaskModel.setAgentid(activityPrizesVo.getAgentid());
                            agentTaskModel.setTaskid(activityPrizesVo.getTaskid());
                            agentTaskModel.setProvidercode(activityPrizesVo.getProvidercode());
                            Map<String, Object> jparams = queryTaskParams(agentTaskModel);
                            if (insbConditionsService.execute("03", (String)prize.get("prizeid"), jparams)) {
                                //自动使用
                                insbAgentPrize.setStatus("1");//已使用状态
                                insbAgentPrize.setModifytime(ca.getTime());
                                //调用资金流水接口
                                INSBAccount insbAccount = insbAccountService.initAccount(activityPrizesVo.getAgentid());
                                Double amount = insbAgentPrize.getCounts();

                                if("02".equals((String)prize.get("prizetype"))){
                                    String noti = "自动交易现金奖励：" + (String)prize.get("id") +" 任务号："+activityPrizesVo.getTaskid()+  "" + (String)prize.get("prizename");
                                    insbAccountDetailsServiceImpl.intoAccountDetails(insbAccount.getId(), "1", "103", amount, noti, "admin");
                                }else{
                                    String noti = "自动交易红包奖励：" + (String)prize.get("id") +" 任务号："+activityPrizesVo.getTaskid()+  "" + (String)prize.get("prizename");
                                    insbAccountDetailsServiceImpl.intoAccountDetails(insbAccount.getId(), "1", "102", amount, noti, "admin");
                                }
                                //System.out.println("2==>MarketActivityServiceImpl调用资金流水接口");
                                //调用资金流水接口
                            }
                        insbAgentPrizeService.updateById(insbAgentPrize);
                    }
                }

            }*/

            resultMap.put("success", true);
            resultMap.put("total", total);
            resultMap.put("rows", activityPrizeList);
            return JSONObject.fromObject(resultMap).toString();
        }
    }

    private boolean isCompletedTask(INSBAgentTask agentTask){
        List<INSBAgentTask> taskList = insbAgentTaskDao.selectList(agentTask);
        if(taskList!=null&&taskList.size()>0){
            return true;
        }else {
            return false;
        }
    }

    private boolean isPaid(String taskid){
        boolean isPaid = false;
        try {
            INSBOrderpayment insbOrderpayment = new INSBOrderpayment();
            insbOrderpayment.setTaskid(taskid);
            insbOrderpayment.setPayresult("02");
            insbOrderpayment = insbOrderpaymentService.queryOne(insbOrderpayment);
            if(null != insbOrderpayment && insbOrderpayment.getId() != null && !"".equals(insbOrderpayment.getId()) ){
                isPaid = true;
            }
        }catch (Exception e){
            isPaid = false;
            LogUtil.info("查询支付数据失败! taskid="+taskid + " errmsg="+e.getMessage());
        }
        return isPaid;
    }

    public void testTaskJob(){
        //LogUtil.info("===="+new Date().toString()+"开始执行定时器方法testTaskJob！");
        //System.out.println("===="+new Date().toString()+"===========testTask Job begin==========");
        //LogUtil.info("===="+new Date().toString()+"结束执行定时器方法testTaskJob！");
    }

	@Override
	public long selectMaxTmpcode() {
		
		return insbMarketActivityDao.selectMaxTmpcode();
	}

}