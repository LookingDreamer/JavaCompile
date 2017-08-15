package com.zzb.extra.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.cninsure.core.controller.BaseController;
import com.cninsure.core.exception.ControllerException;
import com.cninsure.core.utils.BeanUtils;
import com.cninsure.core.utils.LogUtil;
import com.common.ModelUtil;
import com.common.PagingParams;
import com.zzb.chn.service.CHNCommissionRateService;
import com.zzb.chn.service.CHNCommissionRatioService;
import com.zzb.chn.util.JsonUtils;
import com.zzb.conf.entity.INSBOrderpayment;
import com.zzb.conf.service.INSBOrderpaymentService;
import com.zzb.extra.controller.vo.ActivityPrizesVo;
import com.zzb.extra.dao.INSBAgentPrizeDao;
import com.zzb.extra.dao.INSBMarketActivityDao;
import com.zzb.extra.entity.INSBAgentPrize;
import com.zzb.extra.model.AgentTaskModel;
import com.zzb.extra.service.INSBAccountDetailsService;
import com.zzb.extra.service.INSBAgentPrizeService;
import com.zzb.extra.service.INSBCommissionRateService;
import com.zzb.extra.service.INSBConditionParamsService;
import com.zzb.extra.service.INSBMarketActivityService;
import com.zzb.extra.service.INSBMiniCronJobService;
import com.zzb.extra.task.TaskMonitor;
import com.zzb.extra.util.ParamUtils;

@Controller
@RequestMapping("/market/api/*")
public class MarketApiController extends BaseController {

    @Resource
    private INSBAgentPrizeService insbAgentPrizeService;

    @Resource
    private INSBMarketActivityService insbMarketActivityService;

    @Resource
    private INSBAgentPrizeDao insbAgentPrizeDao;

    @Resource
    private INSBAccountDetailsService insbAccountDetailsService;


    @Resource
    private INSBCommissionRateService insbCommissionRateService;

    @Resource
    private INSBConditionParamsService insbConditionParamsService;

    @Resource
    private INSBMiniCronJobService insbMiniCronJobService;

    @Resource
    private INSBOrderpaymentService insbOrderpaymentService;

    @Resource
    private CHNCommissionRatioService chnCommissionRatioService;

    @Resource
    private CHNCommissionRateService chnCommissionRateService;
    @Resource
    private INSBMarketActivityDao insbMarketActivityDao;

    /**
     * 活动查询接口
     *
     * @param session
     * @param para
     * @return
     * @throws ControllerException
     */
    @RequestMapping(value = "queryActivity", method = RequestMethod.GET, produces = "text/json;charset=UTF-8")
    @ResponseBody
    public String queryPrizes(HttpSession session, @ModelAttribute PagingParams para) throws ControllerException {
        try {
            Map<String, Object> map = BeanUtils.toMap(para);
            map.put("terminaltime", new Date());
            map.put("effectivetime", new Date());
            map.put("status", '1');
            //System.out.println(map);
            map.put("agentid", "000");
            //System.out.println("recomamount="+(insbAgentPrizeDao.queryRecommentAmount(map)));
            return insbMarketActivityService.queryEffectiveActivity(map);
        } catch (Exception e) {
            LogUtil.info("queryActivity:" + e.getMessage());
            return ParamUtils.resultMap(false, "系统错误");
        }
    }

    /**
     * 已获奖励查询接口
     *
     * @param session
     * @param para
     * @param activityPrizesVo agentid/gettotal/
     * @return
     * @throws ControllerException
     */
    @RequestMapping(value = "queryPrizes", method = RequestMethod.GET, produces = "text/json;charset=UTF-8")
    @ResponseBody
    public String queryPrizes(HttpSession session, @ModelAttribute PagingParams para, @ModelAttribute ActivityPrizesVo activityPrizesVo) throws ControllerException {
        try {
            if (activityPrizesVo.getAgentid() == null || activityPrizesVo.getAgentid().equals("")) {
                return ParamUtils.resultMap(false, "参数不正确");
            }
            Map<String, Object> map = BeanUtils.toMap(activityPrizesVo, para);
            //System.out.println(map);
            return insbAgentPrizeService.queryGotPrizes(map);
        } catch (Exception e) {
            LogUtil.info("queryPrizes:" + e.getMessage());
            return ParamUtils.resultMap(false, "系统错误");
        }
    }

    /**
     * 可获奖励查询接口
     *
     * @param session
     * @param activityPrizesVo
     * @return
     * @throws ControllerException
     */
    @RequestMapping(value = "queryTaskPrizes", method = RequestMethod.GET, produces = "text/json;charset=UTF-8")
    @ResponseBody
    public String queryTaskPrizes(HttpSession session, @ModelAttribute PagingParams para, @ModelAttribute ActivityPrizesVo activityPrizesVo) throws ControllerException {
        try {
            //参数： <!--add-->activitytype/taskid/gettotal/operate 交易类型加taskid，非交易类型不需要taskid,如需获取分类total数,gettotal=true
            if (("01".equals(activityPrizesVo.getActivitytype())) && (activityPrizesVo.getAgentid() == null || activityPrizesVo.getAgentid().equals("") || activityPrizesVo.getTaskid() == null || activityPrizesVo.getTaskid().equals(""))) {
                return ParamUtils.resultMap(false, "参数不正确");
            } else if (("01".equals(activityPrizesVo.getActivitytype())) && (activityPrizesVo.getAgentid() == null || activityPrizesVo.getAgentid().equals("")) && (activityPrizesVo.getJobnum() == null || activityPrizesVo.getJobnum().equals(""))) {
                return ParamUtils.resultMap(false, "参数不正确");
            }
            return insbMarketActivityService.queryTaskPrizes(activityPrizesVo);
        } catch (Exception e) {
            LogUtil.info("queryTaskPrizes:" + e.getMessage());
            return ParamUtils.resultMap(false, "系统错误");
        }
    }

    /**
     * 可用奖励查询接口
     *
     * @param session
     * @param activityPrizesVo
     * @return
     * @throws ControllerException
     */
    @RequestMapping(value = "queryAvailablePrizes", method = RequestMethod.GET, produces = "text/json;charset=UTF-8")
    @ResponseBody
    public String queryAvailablePrizes(HttpSession session, @ModelAttribute PagingParams para, @ModelAttribute ActivityPrizesVo activityPrizesVo) throws ControllerException {
        try {
            if (activityPrizesVo.getAgentid() == null || activityPrizesVo.getAgentid().equals("") || activityPrizesVo.getTaskid() == null || activityPrizesVo.getTaskid().equals("")) {
                return ParamUtils.resultMap(false, "参数不正确");
            }
            Map<String, Object> map = BeanUtils.toMap(activityPrizesVo, para);
            return insbAgentPrizeService.queryAvailablePrizes(map);
        } catch (Exception e) {
            LogUtil.info("queryAvailablePrizes:" + e.getMessage());
            return ParamUtils.resultMap(false, "系统错误");
        }
    }

    /**
     * 已选奖券查询接口（查询待发放3和已支付待发放4状态）
     *
     * @param session
     * @param activityPrizesVo
     * @return
     * @throws ControllerException
     */
    @RequestMapping(value = "queryUsedPrize", method = RequestMethod.GET, produces = "text/json;charset=UTF-8")
    @ResponseBody
    public String queryUsedPrize(HttpSession session, @ModelAttribute PagingParams para, @ModelAttribute ActivityPrizesVo activityPrizesVo) throws ControllerException {
        try {
            if (activityPrizesVo.getProvidercode() == null || activityPrizesVo.getProvidercode().equals("") || activityPrizesVo.getTaskid() == null || activityPrizesVo.getTaskid().equals("")) {
                return ParamUtils.resultMap(false, "参数不正确");
            }
            Map<String, Object> map = BeanUtils.toMap(activityPrizesVo, para);
            return insbAgentPrizeService.queryPrizeByAgentIdAndTask(map);
        } catch (Exception e) {
            LogUtil.info("queryUsedPrize:" + e.getMessage());
            return ParamUtils.resultMap(false, "系统错误");
        }
    }

    /**
     * 获取最大可用红包以及可获现金接口
     *
     * @param session
     * @param activityPrizesVo
     * @return
     * @throws ControllerException
     */
    @RequestMapping(value = "getMaxRedPacketAndCash", method = RequestMethod.GET, produces = "text/json;charset=UTF-8")
    @ResponseBody
    public String getMaxRedPacketAndCash(HttpSession session, @ModelAttribute PagingParams para, @ModelAttribute ActivityPrizesVo activityPrizesVo) throws ControllerException {
        try {
            Map<String, Object> resultMap = new HashMap<String, Object>();
            if (activityPrizesVo.getAgentid() == null || activityPrizesVo.getAgentid().equals("") || activityPrizesVo.getTaskid() == null || activityPrizesVo.getTaskid().equals("") || activityPrizesVo.getProvidercode() == null || activityPrizesVo.getProvidercode().equals("") || activityPrizesVo.getActivitytype() == null || activityPrizesVo.getActivitytype().equals("")) {
                return ParamUtils.resultMap(false, "参数不正确");
            }
            Map<String, Object> map = BeanUtils.toMap(activityPrizesVo, para);
            String redPacketJsonStr = insbAgentPrizeService.queryAvailablePrizes(map);
            //System.out.println("redPacketJsonStr="+redPacketJsonStr);
            activityPrizesVo.setGettotal(true);
            //20160716 注销 前端不用这两个栏位
            //String cashJsonstr = insbMarketActivityService.queryTaskPrizes(activityPrizesVo);
            //System.out.println("cashJsonstr="+cashJsonstr);
            HashMap redPackets = JSONObject.parseObject(redPacketJsonStr, HashMap.class);
            List<Map<Object, Object>> redPacketList = (List<Map<Object, Object>>) redPackets.get("rows");
            int redCounts = 0;
            int tempCounts = 0;
            for (Map<Object, Object> agentPrize : redPacketList) {
                redCounts = (Integer) agentPrize.get("counts");
                if (redCounts > tempCounts) {
                    tempCounts = redCounts;
                }
            }
            resultMap.put("useableredpacket", tempCounts);
            resultMap.put("availablecash", 0);//20160716 注销 前端不用这两个栏位
            resultMap.put("availableredpacket", 0);//20160716 注销 前端不用这两个栏位
            // System.out.println("红包=" + tempCounts);
            //HashMap cash = JSONObject.parseObject(cashJsonstr,HashMap.class);
            //System.out.println("现金=" + cash.get("现金"));
            //20160716 注销 前端不用这两个栏位
            //resultMap.put("availablecash", (cash.get("奖金") == null || "".equals(cash.get("奖金")))?0:cash.get("奖金"));
            //resultMap.put("availableredpacket",(cash.get("奖券") == null || "".equals(cash.get("奖券")))?0:cash.get("奖券"));
            return JSONObject.toJSONString(resultMap);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.info("getMaxRedPacketAndCash:" + e.getMessage());
            return ParamUtils.resultMap(false, "系统错误");
        }
    }

    /**
     * 自动保存奖品接口
     *
     * @param session
     * @param activityPrizesVo
     * @return
     * @throws ControllerException
     */
    @RequestMapping(value = "saveAgentPrizes", method = RequestMethod.GET, produces = "text/json;charset=UTF-8")
    @ResponseBody
    public String saveAgentPrizes(HttpSession session, @ModelAttribute PagingParams para, @ModelAttribute ActivityPrizesVo activityPrizesVo) throws ControllerException {
        //agentid activitype operate=save
        activityPrizesVo.setOperate("save");
        return this.queryTaskPrizes(session, para, activityPrizesVo);
    }

    /**
     * 更新奖品使用状态
     *
     * @param session
     * @param activityPrizesVo
     * @return
     * @throws ControllerException
     */
    @RequestMapping(value = "updateAgentPrize", method = RequestMethod.GET, produces = "text/json;charset=UTF-8")
    @ResponseBody
    public String updateAgentPrize(HttpSession session, @ModelAttribute PagingParams para, @ModelAttribute ActivityPrizesVo activityPrizesVo) throws ControllerException {
        //参数：agentid/agentprizeid/taskid/status
        //先修改其他奖品状态，taskid和providercode修改为空，status=0
        insbAgentPrizeService.updateByTaskIdAndProviderCode(activityPrizesVo.getTaskid(), activityPrizesVo.getProvidercode(), activityPrizesVo.getAgentid());
        //然后修改选择的状态为3
        INSBAgentPrize insbAgentPrize = insbAgentPrizeService.queryById(activityPrizesVo.getAgentprizeid());
        insbAgentPrize.setStatus(activityPrizesVo.getStatus());//"3" 待发放状态  由后台自动job去查询发放
        insbAgentPrize.setTaskid(activityPrizesVo.getTaskid());
        insbAgentPrize.setProvidercode(activityPrizesVo.getProvidercode());
        insbAgentPrize.setModifytime(new Date());
        insbAgentPrizeService.updateById(insbAgentPrize);
        //根据agentprizeid获得prize autouse是自动使用还是手动使用
        /*activityPrizesVo.setAgentprizeid(insbAgentPrize.getId());
        List<Map<Object, Object>> infoList = insbAgentPrizeService.queryAgentPrizeListNoTotal(BeanUtils.toMap(activityPrizesVo));
        for(Map<Object, Object> prize : infoList){
            if("1".equals(prize.get("autouse"))){
                //自动使用 调用资金流水接口
                System.out.println("==>调用资金流水接口");
                //自动使用
                insbAgentPrize.setStatus("1");//已使用状态
                insbAgentPrize.setModifytime(new Date());
            }else if("0".equals(prize.get("autouse"))){}

        }*/
        return ParamUtils.resultMap(true, "更新成功");
    }

    /**
     * 任务监控
     *
     * @return
     * @throws ControllerException
     */
    @RequestMapping(value = "taskMonitor", method = RequestMethod.GET, produces = "text/json;charset=UTF-8")
    @ResponseBody
    public String taskMonitor() throws ControllerException {
        return TaskMonitor.getValues();
    }

    /**
     * 任务监控
     *
     * @return
     * @throws ControllerException
     */
    @RequestMapping(value = "clearTaskMonitor", method = RequestMethod.GET, produces = "text/json;charset=UTF-8")
    @ResponseBody
    public String clearTaskMonitor() throws ControllerException {
        TaskMonitor.clearMap();
        return "sucess";
    }

    /**
     * 开始job
     *
     * @return
     * @throws ControllerException
     */
    @RequestMapping(value = "beginCronJob", method = RequestMethod.GET, produces = "text/json;charset=UTF-8")
    @ResponseBody
    public String beginCronJob() throws ControllerException {
        //return insbMiniCronJobService.beginCronJob();
        return "";
    }

    /**
     * 刷红包佣金
     *
     * @return
     * @throws ControllerException
     */
    @RequestMapping(value = "detector", method = RequestMethod.GET, produces = "text/json;charset=UTF-8")
    @ResponseBody
    public String detector() throws ControllerException {
        //return insbAccountDetailsService.detector();
        return "";
    }

    /**
     * 测试 refreshRedPackage
     *
     * @return
     * @throws ControllerException
     */
    @RequestMapping(value = "refreshRedPackage", method = RequestMethod.GET, produces = "text/json;charset=UTF-8")
    @ResponseBody
    public String refreshRedPackage() throws ControllerException {
        //return insbAccountDetailsService.refreshRedPackage();
        return "";
    }

    /**
     * 刷红包佣金
     *
     * @return
     * @throws ControllerException
     */
    @RequestMapping(value = "genCommissionAndRedPackets", method = RequestMethod.GET, produces = "text/json;charset=UTF-8")
    @ResponseBody
    public String genCommissionAndRedPackets(String taskid, String providercode) throws ControllerException {
        if (taskid == null || "".equals(taskid) || providercode == null || "".equals(providercode)) {
            return ParamUtils.resultMap(false, "参数不正确");
        }
        return insbAccountDetailsService.genCommissionAndRedPackets(taskid, providercode);
    }

    /**
     * refreshRedPackets
     *
     * @return
     * @throws ControllerException
     */
    @RequestMapping(value = "refreshRedPackets", method = RequestMethod.GET, produces = "text/json;charset=UTF-8")
    @ResponseBody
    public String refreshRedPackets(String taskid, String providercode) throws ControllerException {
        if (taskid == null || "".equals(taskid) || providercode == null || "".equals(providercode)) {
            return ParamUtils.resultMap(false, "参数不正确");
        }
        return insbAccountDetailsService.refreshRedPackets(taskid, providercode);
    }

    /**
     * 测试 sendRedPackets
     *
     * @return
     * @throws ControllerException
     */
    @RequestMapping(value = "sendRedPackets", method = RequestMethod.GET, produces = "text/json;charset=UTF-8")
    @ResponseBody
    public String sendRedPackets(String taskid, String providercode, String acountid) throws ControllerException {
        return insbAccountDetailsService.sendRedPackets(taskid, providercode, acountid);
    }

    /**
     * 测试 giveRedPackets
     *
     * @return
     * @throws ControllerException
     */
    @RequestMapping(value = "giveRedPackets", method = RequestMethod.GET, produces = "text/json;charset=UTF-8")
    @ResponseBody
    public String giveRedPackets(String taskid, String agentid, String providercode) throws ControllerException {
        return insbAccountDetailsService.giveRedPackets(taskid, agentid, providercode);
    }

    /**
     * 佣金查询接口
     *
     * @param session
     * @param agentTaskModel
     * @return
     * @throws ControllerException
     */
    @RequestMapping(value = "queryTaskCommission", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
    @ResponseBody
    public String queryTaskCommission(HttpSession session, @RequestBody AgentTaskModel agentTaskModel) throws ControllerException {
        try {
            if (agentTaskModel.getAgentid() == null || agentTaskModel.getAgentid().equals("") ||
                    agentTaskModel.getTaskid() == null || agentTaskModel.getTaskid().equals("") ||
                    agentTaskModel.getProvidercode() == null || agentTaskModel.getProvidercode().equals("")) {
                return ParamUtils.resultMap(false, "参数不正确");
            }
            agentTaskModel.setChannelid("nqd_minizzb2016");
            return chnCommissionRateService.queryTaskCommission(agentTaskModel);
        } catch (Exception e) {
            return ParamUtils.resultMap(false, "系统错误");
        }
    }

    /**
     * 佣金测试接口
     *
     * @param session
     * @param params
     * @return
     * @throws ControllerException
     */
    @RequestMapping(value = "testCommissionRule", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
    @ResponseBody
    public String testCommissionRule(HttpSession session, @RequestBody Map<String, Object> params) throws ControllerException {
        try {
            if (params.getOrDefault("agreementid", "") == "") {
                return ParamUtils.resultMap(false, "参数不正确");
            }
            if(params.getOrDefault("productcode", "").equals("channel")) {
                return chnCommissionRateService.testCommissionRule(params);
            }else {
                return insbCommissionRateService.testCommissionRule(params);
            }
        } catch (Exception e) {
            return ParamUtils.resultMap(false, "系统错误");
        }
    }

    /**
     * 佣金供应商查询接口
     *
     * @param session
     * @return
     * @throws ControllerException
     */
    @RequestMapping(value = "queryCommissionProvider", method = RequestMethod.GET, produces = "text/json;charset=UTF-8")
    @ResponseBody
    public String queryCommissionProvider(HttpSession session,String productcode) throws ControllerException {
        try {
            if(productcode == null || "".equals(productcode)) {
                return insbCommissionRateService.queryCommissionProvider("minizzb");
            }else {
                return insbCommissionRateService.queryCommissionProvider("channel");
            }
        } catch (Exception e) {
            return ParamUtils.resultMap(false, "系统错误");
        }
    }

    /**
     * 渠道佣金供应商查询接口
     *
     * @param session
     * @return
     * @throws ControllerException
     */
    @RequestMapping(value = "queryCommissionProviderForChannel", method = RequestMethod.GET, produces = "text/json;charset=UTF-8")
    @ResponseBody
    public String queryCommissionProviderForChannel(HttpSession session) throws ControllerException {
        try {
            return insbCommissionRateService.queryCommissionProvider("channel");
        } catch (Exception e) {
            return ParamUtils.resultMap(false, "系统错误");
        }
    }

    /**
     * 渠道佣金供应商查询接口
     *
     * @param session
     * @return
     * @throws ControllerException
     */
    @RequestMapping(value = "queryChannelList", method = RequestMethod.GET, produces = "text/json;charset=UTF-8")
    @ResponseBody
    public String queryChannelList(HttpSession session) throws ControllerException {
        try {
            Map<String,Object> map = new HashMap<String,Object>();
            List<Map<String, String>> channelList = chnCommissionRatioService.initChannelList();
            map.put("total",channelList.size());
            map.put("success",true);
            map.put("rows",channelList);
            return  JsonUtils.serialize(map);
        } catch (Exception e) {
            return ParamUtils.resultMap(false, "系统错误");
        }
    }

    /**
     * 规则条件数据项查询接口
     *
     * @param session
     * @return
     * @throws ControllerException
     */
    @RequestMapping(value = "queryConditionParam", method = RequestMethod.GET, produces = "text/json;charset=UTF-8")
    @ResponseBody
    public String queryConditionParam(HttpSession session) throws ControllerException {
        try {
            return insbConditionParamsService.queryPagingList(new HashMap<>());
        } catch (Exception e) {
            return ParamUtils.resultMap(false, "系统错误");
        }
    }

    @RequestMapping(value = "queryOrderPaytime", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
    @ResponseBody
    public String queryOrderPaytime(HttpSession session, @RequestBody AgentTaskModel agentTaskModel) throws ControllerException {
        try {
            INSBOrderpayment insbOrderpayment = new INSBOrderpayment();
            insbOrderpayment.setTaskid(agentTaskModel.getTaskid());
            insbOrderpayment.setPayresult("02");
            insbOrderpayment = insbOrderpaymentService.queryOne(insbOrderpayment);
            Map<String,Object> map = new HashMap<String,Object>();
            if(insbOrderpayment!=null){
                map.put("paytime", (!"".equals(ModelUtil.conbertToStringsdf(insbOrderpayment.getPaytime())))?ModelUtil.conbertToStringsdf(insbOrderpayment.getPaytime()):ModelUtil.conbertToStringsdf(insbOrderpayment.getCreatetime()));
            }else{
                map.put("paytime","");
            }
            return JsonUtils.serialize(map);
        } catch (Exception e) {
            return ParamUtils.resultMap(false, "系统错误");
        }
    }
    

}
