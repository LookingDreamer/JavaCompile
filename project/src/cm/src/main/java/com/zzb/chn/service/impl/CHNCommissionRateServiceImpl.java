package com.zzb.chn.service.impl;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.cninsure.core.utils.DateUtil;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.cninsure.core.utils.UUIDUtils;
import com.zzb.chn.dao.INSBCommissionratioDao;
import com.zzb.chn.entity.INSBCommissionratio;
import com.zzb.chn.service.CHNCommissionRateService;
import com.zzb.chn.util.JsonUtils;
import com.zzb.extra.dao.INSBCommissionDao;
import com.zzb.extra.dao.INSBCommissionRateDao;
import com.zzb.extra.dao.INSBConditionsDao;
import com.zzb.extra.dao.INSBTaxrateDao;
import com.zzb.extra.entity.INSBChnTaxrate;
import com.zzb.extra.entity.INSBCommission;
import com.zzb.extra.entity.INSBCommissionRate;
import com.zzb.extra.model.AgentTaskModel;
import com.zzb.extra.service.INSBCommissionService;
import com.zzb.extra.service.INSBConditionsService;
import com.zzb.extra.util.ParamUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by hwc on 2016/11/7.
 */
@Service
@Transactional
public class CHNCommissionRateServiceImpl extends BaseServiceImpl<INSBCommissionRate> implements CHNCommissionRateService {
    @Resource
    private INSBCommissionRateDao insbCommissionRateDao;

    @Resource
    private INSBConditionsService insbConditionsService;

    @Resource
    private INSBCommissionService insbCommissionService;

    @Resource
    private INSBCommissionDao insbCommissionDao;

    @Resource
    private INSBCommissionratioDao insbCommissionratioDao;

    @Resource
    private INSBConditionsDao insbConditionsDao;
    
    @Resource
    private INSBTaxrateDao insbTaxrateDao;

    @Override
    protected BaseDao<INSBCommissionRate> getBaseDao() {
        return insbCommissionRateDao;
    }

    @Override
    public String queryPagingList(Map<String, Object> map) {
        String agreementid = map.getOrDefault("agreementid", "").toString();
        String productcode = map.getOrDefault("productcode", "").toString();
        if (!agreementid.equals(""))
            insbCommissionRateDao.cleanCommissionRateStatus(agreementid,productcode);

        long total = insbCommissionRateDao.queryPagingListCount(map);
        List<Map<Object, Object>> infoList = insbCommissionRateDao.queryPagingList(map);
        return ParamUtils.resultMap(total, infoList);
    }

    @Override
    public List<Map<String, String>> initAgreementList() {
        return insbCommissionRateDao.initAgreementList();
    }

    @Override
    public String queryTaskCommission(AgentTaskModel agentTaskModel) {
        LogUtil.info("queryTaskCommission called params="+JsonUtils.serialize(agentTaskModel));
        Map<String,Object> logMap = new HashMap<String,Object>();
        logMap.put("agentTaskModel",agentTaskModel);

        if(StringUtil.isEmpty(agentTaskModel.getTaskid()) || StringUtil.isEmpty(agentTaskModel.getProvidercode())||StringUtil.isEmpty(agentTaskModel.getChannelid())){
            return ParamUtils.resultMap(false, "任务号供应商编号渠道ID不能为空！");
        }
        String taskId = agentTaskModel.getTaskid();
        String providerCode = agentTaskModel.getProvidercode();
        String commercialCommissionRateId = "";
        String compulsoryCommissionRateId = "";
        String commercialCommissionRatioId = "";
        String compulsoryCommissionRatioId = "";
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
        if (insbCommissionService.isLocked(agentTaskModel)) {
            List<INSBCommission> commissions = insbCommissionService.queryCommissions(agentTaskModel);
            for (INSBCommission commission : commissions) {
                Double counts = commission.getCounts();
                switch (commission.getCommissiontype()) {
                    case "01":
                        commercialCommission = counts;
                        break;
                    case "02":
                        adCommercialCommission = counts;
                        break;
                    case "03":
                        compulsoryCommission = counts;
                        break;
                }
            }
        } else {

            Map<String, Object> params = insbConditionsService.queryParams(agentTaskModel);
            logMap.put("params",params);

            Double commercialPremium = Double.parseDouble(params.getOrDefault("commercialPremium", "0").toString());
            Double compulsoryPremium = Double.parseDouble(params.getOrDefault("compulsoryPremium", "0").toString());

            /*
            * 先查询渠道是否配置专用佣金率，有就用专用佣金率，否则用公共佣金率
            * */
            Map<String, Object> map = new HashMap<String,Object>();
            map.put("channelsource",agentTaskModel.getChannelid());
            long total = insbCommissionRateDao.queryPagingListCount(map);
            String channelSource = "";
            if(total > 0){
                channelSource = agentTaskModel.getChannelid();
            }else{
                channelSource = "channel";
            }

            List<INSBCommissionRate> commissionRateList = insbCommissionRateDao.queryCommissionByAgreement(agentTaskModel.getAgreementid(),"channel",channelSource);
            for (INSBCommissionRate commissionRate : commissionRateList) {
                if (insbConditionsService.execute("02", commissionRate.getId(), params)) {
                    Double rate = commissionRate.getRate();
                    /*
                     *查询佣金系数
                     * */
                    Map<String,Object> ratioParams = new HashMap<String,Object>();
                    String deptCode = insbConditionsService.queryDeptCodeByTask(agentTaskModel);
                    ratioParams.put("deptCode",deptCode);
                    if(commissionRate.getCommissiontype().equals("03")) {
                        ratioParams.put("compulsoryRate", rate);
                    }
                    logMap.put("ratioParams",ratioParams);
                    List<INSBCommissionratio> commissionRatioList = insbCommissionratioDao.queryCommissionRatioByChannel(agentTaskModel.getChannelid());
                    Double commercialCommissioRatio = 1D;
                    Double compulsoryCommissionRatio = 1D;
                    String commercialCalculatetype = "3";//默认乘  1,2,3,4 对应加减乘除
                    String compulsoryCalculatetype = "3";//默认乘  1,2,3,4 对应加减乘除
                    Double taxrate = 1D;
                    INSBChnTaxrate chnTaxrate = insbTaxrateDao.queryTaxrateByChannelid(agentTaskModel.getChannelid());
                    if(chnTaxrate != null && (chnTaxrate.getTerminaltime().getTime() > new Date().getTime())) 
                    	taxrate = chnTaxrate.getTaxrate();
                    for(INSBCommissionratio commissionratio:commissionRatioList){
                        if (insbConditionsService.execute("04", commissionratio.getId(), ratioParams)) {
                            Double ratio = commissionratio.getRatio();
                            if(commissionratio.getCommissiontype().equals("01")  && ratio <= commercialCommissioRatio){
                                commercialCommissioRatio = ratio;
                                commercialCalculatetype = commissionratio.getCalculatetype();
                                commercialCommissionRatioId = commissionratio.getId();
                                logMap.put("commercialCommissioRatio",commercialCommissioRatio);
                                logMap.put("commercialCommissioRatioId",commissionratio.getId());
                            }
                            if(commissionratio.getCommissiontype().equals("03")  && ratio <= compulsoryCommissionRatio){
                                compulsoryCommissionRatio = ratio;
                                compulsoryCalculatetype = commissionratio.getCalculatetype();
                                compulsoryCommissionRatioId = commissionratio.getId();
                                logMap.put("compulsoryCommissionRatio",compulsoryCommissionRatio);
                                logMap.put("compulsoryCommissionRatioId",commissionratio.getId());
                            }
                        }
                    }
                    //
                    DecimalFormat df=new DecimalFormat("#0.##");
                    df.setRoundingMode(RoundingMode.FLOOR);
                     if (commissionRate.getCommissiontype().equals("01") && rate <= commercialCommissionRate) {
                        commercialCommissionRateId = commissionRate.getId();
                        commercialCommissionRate = rate;
                       // commercialCommission = commercialPremium * Double.parseDouble(df.format(rate * commercialCommissioRatio));
                         switch (commercialCalculatetype){
                             case "1":
                                 commercialCommission  =  Double.parseDouble(df.format((commercialPremium/taxrate)*(rate + commercialCommissioRatio)));
                                 logMap.put("commercialCalculatetype","1");
                                 logMap.put("commercialCommissionRateAfterRatio",rate + commercialCommissioRatio);
                                 break;
                             case "2":
                                 commercialCommission  =  Double.parseDouble(df.format((commercialPremium/taxrate)*(rate - commercialCommissioRatio)));
                                 logMap.put("commercialCalculatetype","2");
                                 logMap.put("commercialCommissionRateAfterRatio",rate - commercialCommissioRatio);
                                 break;
                             case "3":
                                 commercialCommission  =  Double.parseDouble(df.format((commercialPremium/taxrate)*(rate * commercialCommissioRatio)));
                                 logMap.put("commercialCalculatetype","3");
                                 logMap.put("commercialCommissionRateAfterRatio",rate * commercialCommissioRatio);
                                 break;
                             case "4":
                                 commercialCommission  =  Double.parseDouble(df.format((commercialPremium/taxrate)*(rate / commercialCommissioRatio)));
                                 logMap.put("commercialCalculatetype","4");
                                 logMap.put("commercialCommissionRateAfterRatio",rate / commercialCommissioRatio);
                                 break;
                         }

                         logMap.put("commercialRemark",commissionRate.getRemark());
                     }
                    if (commissionRate.getCommissiontype().equals("03") && rate <= compulsoryCommissionRate) {
                        compulsoryCommissionRateId = commissionRate.getId();
                        compulsoryCommissionRate = rate;
                       // compulsoryCommission = compulsoryPremium * Double.parseDouble(df.format(rate * compulsoryCommissionRatio));
                        switch (compulsoryCalculatetype){
                            case "1":
                                compulsoryCommission  =  Double.parseDouble(df.format((compulsoryPremium/taxrate)*(rate + compulsoryCommissionRatio)));
                                logMap.put("compulsoryCalculatetype","1");
                                logMap.put("compulsoryCommissionRateAfterRatio",rate + compulsoryCommissionRatio);
                                break;
                            case "2":
                                compulsoryCommission  =  Double.parseDouble(df.format((compulsoryPremium/taxrate)*(rate - compulsoryCommissionRatio)));
                                logMap.put("compulsoryCalculatetype","2");
                                logMap.put("compulsoryCommissionRateAfterRatio",rate - compulsoryCommissionRatio);
                                break;
                            case "3":
                                compulsoryCommission  =  Double.parseDouble(df.format((compulsoryPremium/taxrate)*(rate * compulsoryCommissionRatio)));
                                logMap.put("compulsoryCalculatetype","3");
                                logMap.put("compulsoryCommissionRateAfterRatio",rate * compulsoryCommissionRatio);
                                break;
                            case "4":
                                compulsoryCommission  =  Double.parseDouble(df.format((compulsoryPremium/taxrate)*(rate / compulsoryCommissionRatio)));
                                logMap.put("compulsoryCalculatetype","4");
                                logMap.put("compulsoryCommissionRateAfterRatio",rate / compulsoryCommissionRatio);
                                break;
                        }
                        logMap.put("compulsoryRemark",commissionRate.getRemark());
                    }
                }
            }

           /* 不生成推荐佣金
              if (commercialCommission > 0)
                adCommercialCommission = commercialPremium * 0.03;
            */
            //报价先删除佣金，后重算
            if("quote".equals(agentTaskModel.getCommissionFlag())) {
                insbCommissionService.deleteCommissionByTask(agentTaskModel);
            }

            if (!commercialCommissionRateId.equals("")) {
                agentTaskModel.setCommissionRatioId(commercialCommissionRatioId);
                updateCommission(agentTaskModel, "01", commercialCommissionRateId, (double) commercialCommission);
            }else{
                commercialCommissionRate = 0D;
            }
            if (!compulsoryCommissionRateId.equals("")) {
                agentTaskModel.setCommissionRatioId(compulsoryCommissionRatioId);
                updateCommission(agentTaskModel, "03", compulsoryCommissionRateId, (double) compulsoryCommission);
            }else{
                compulsoryCommissionRate = 0D;
            }

            /* 删除推荐奖励
            if (commercialCommission > 0)
                updateCommission(agentTaskModel, "02", adCommercialCommissionRateId, (double) adCommercialCommission.intValue());
            */
        }
        logMap.put("commercialCommissionRateId",commercialCommissionRateId);
        logMap.put("commercialCommissionRate",commercialCommissionRate);
        logMap.put("compulsoryCommissionRateId",compulsoryCommissionRateId);
        logMap.put("compulsoryCommissionRate",compulsoryCommissionRate);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("taskid",taskId);map.put("providercode",providerCode);
        map
        .put("commissionFlag",agentTaskModel.getCommissionFlag());
        Map<String, Object> res = insbCommissionService.queryPagingListReturnMap(map);
        map.put("commercialCommission", (double) commercialCommission);
        map.put("compulsoryCommission", (double) compulsoryCommission);
        map.put("adCommercialCommission", (double) adCommercialCommission);
        map.put("adCompulsoryCommission", (double) adCompulsoryCommission);
        map.putAll(res);
        logMap.putAll(map);
        LogUtil.info("queryTaskCommission called values="+JsonUtils.serialize(logMap));
        return ParamUtils.resultMapByJackson(true, map);
    }

    @Override
    public String queryTaskCommissionRate(AgentTaskModel agentTaskModel){
        LogUtil.info("queryTaskCommissionRate called params="+JsonUtils.serialize(agentTaskModel));
        Map<String,Object> logMap = new HashMap<String,Object>();
        logMap.put("agentTaskModel",agentTaskModel);
        if(StringUtil.isEmpty(agentTaskModel.getTaskid()) || StringUtil.isEmpty(agentTaskModel.getProvidercode())||StringUtil.isEmpty(agentTaskModel.getChannelid())){
            return ParamUtils.resultMap(false, "任务号供应商编号渠道ID不能为空！");
        }
        String commercialCommissionRateId = "";
        String compulsoryCommissionRateId = "";
        String adCommercialCommissionRateId = "";
        String adCompulsoryCommissionRateId = "";

        Double commercialCommissionRate = 1D;
        Double commercialCommissionRateAfterRatio = 0D;
        Double compulsoryCommissionRate = 1D;
        Double compulsoryCommissionRateAfterRatio = 0D;

        String commercialCommissionRateReminder = "";
        String compulsoryCommissionRateReminder = "";

        String commercialCalculateType = "3"; // 1,2,3,4对应加减乘除
        String compulsoryCalculateType = "3"; // 1,2,3,4对应加减乘除
        Double commercialTaxrate= 1D;
        Double compulsoryTaxrate = 1D;

        agentTaskModel.setAgreementid(insbConditionsService.queryAgreementIdByTask(agentTaskModel));
        if (agentTaskModel.getAgreementid() == null || agentTaskModel.getAgreementid().equals("")) {
            return ParamUtils.resultMap(false, "任务信息不正确");
        }


            Map<String, Object> params = insbConditionsService.queryParams(agentTaskModel);
            logMap.put("params",params);

            Double commercialPremium = Double.parseDouble(params.getOrDefault("commercialPremium", "0").toString());
            Double compulsoryPremium = Double.parseDouble(params.getOrDefault("compulsoryPremium", "0").toString());

            /*
            * 先查询渠道是否配置专用佣金率，有就用专用佣金率，否则用公共佣金率
            * */
            Map<String, Object> map = new HashMap<String,Object>();
            map.put("channelsource",agentTaskModel.getChannelid());
            long total = insbCommissionRateDao.queryPagingListCount(map);
            String channelSource = "";
            if(total > 0){
                channelSource = agentTaskModel.getChannelid();
            }else{
            channelSource = "channel";
           }

            List<INSBCommissionRate> commissionRateList = insbCommissionRateDao.queryCommissionByAgreement(agentTaskModel.getAgreementid(),"channel",channelSource);
            for (INSBCommissionRate commissionRate : commissionRateList) {
                if (insbConditionsService.execute("02", commissionRate.getId(), params)) {
                    Double rate = commissionRate.getRate();
                    /*
                     *查询佣金系数
                     * */
                    Map<String,Object> ratioParams = new HashMap<String,Object>();
                    String deptCode = insbConditionsService.queryDeptCodeByTask(agentTaskModel);
                    ratioParams.put("deptCode",deptCode);
                    if(commissionRate.getCommissiontype().equals("03")) {
                        ratioParams.put("compulsoryRate", rate);
                    }
                    logMap.put("ratioParams",ratioParams);
                    List<INSBCommissionratio> commissionRatioList = insbCommissionratioDao.queryCommissionRatioByChannel(agentTaskModel.getChannelid());
                    Double commercialCommissioRatio = 1D;
                    Double compulsoryCommissionRatio = 1D;
                    commercialTaxrate= 1D;
                    compulsoryTaxrate = 1D;
                    commercialCalculateType = "3"; // 1,2,3,4对应加减乘除
                    compulsoryCalculateType = "3"; // 1,2,3,4对应加减乘除
                    for(INSBCommissionratio commissionratio:commissionRatioList){
                        if (insbConditionsService.execute("04", commissionratio.getId(), ratioParams)) {
                            Double ratio = commissionratio.getRatio();
                            if(commissionratio.getCommissiontype().equals("01")  && ratio <= commercialCommissioRatio){
                                commercialCommissioRatio = ratio;
                                commercialTaxrate = commissionratio.getTaxrate();
                                commercialCalculateType = commissionratio.getCalculatetype();
                                logMap.put("commercialTaxrate", commercialTaxrate);
                                logMap.put("commercialCalculateType", commercialCalculateType);
                                logMap.put("commercialCommissioRatioId", commissionratio.getId());
                                logMap.put("commercialCommissioRatioRemark", commissionratio.getRemark());
                                logMap.put("commercialCommissioRatio", commercialCommissioRatio);
                            }
                            if(commissionratio.getCommissiontype().equals("03")  && ratio <= compulsoryCommissionRatio){
                                compulsoryCommissionRatio = ratio;
                                compulsoryTaxrate = commissionratio.getTaxrate();
                                compulsoryCalculateType = commissionratio.getCalculatetype();
                                logMap.put("compulsoryTaxrate", compulsoryTaxrate);
                                logMap.put("compulsoryCalculateType", compulsoryCalculateType);
                                logMap.put("compulsoryCommissionRatioId", commissionratio.getId());
                                logMap.put("compulsoryCommissionRatio", compulsoryCommissionRatio);
                                logMap.put("compulsoryCommissionRatioRemark", commissionratio.getRemark());
                            }
                        }
                    }
                    //
                    DecimalFormat df=new DecimalFormat("#0.##");
                    df.setRoundingMode(RoundingMode.FLOOR);
                    if (commissionRate.getCommissiontype().equals("01") && rate <= commercialCommissionRate) {
                        commercialCommissionRateId = commissionRate.getId();
                        commercialCommissionRate = rate;
                       // commercialCommissionRateAfterRatio = Double.parseDouble(df.format(rate * commercialCommissioRatio));
                         switch (commercialCalculateType){
                            case "1":
                                commercialCommissionRateAfterRatio  =  (rate + commercialCommissioRatio);
                                break;
                            case "2":
                                commercialCommissionRateAfterRatio  =  (rate - commercialCommissioRatio);
                                break;
                            case "3":
                                commercialCommissionRateAfterRatio  =  (rate * commercialCommissioRatio);
                                break;
                            case "4":
                                commercialCommissionRateAfterRatio  =  (rate / commercialCommissioRatio);
                                break;
                        }
                        commercialCommissionRateReminder = commissionRate.getReminder();
                        logMap.put("commercialCommissionRate",commercialCommissionRate);
                        logMap.put("commercialCommissionRateAfterRatio",commercialCommissionRateAfterRatio);
                        logMap.put("commercialCommissionRateReminder",commercialCommissionRateReminder);
                        logMap.put("commercialCommissionRateRemark",commissionRate.getRemark());
                        //commercialCommission = commercialPremium * commercialCommissionRateAfterRatio;
                    }
                    if (commissionRate.getCommissiontype().equals("03") && rate <= compulsoryCommissionRate) {
                        compulsoryCommissionRateId = commissionRate.getId();
                        compulsoryCommissionRate = rate;
                        switch (compulsoryCalculateType){
                            case "1":
                                compulsoryCommissionRateAfterRatio  =  (rate + compulsoryCommissionRatio);
                                break;
                            case "2":
                                compulsoryCommissionRateAfterRatio  =  (rate - compulsoryCommissionRatio);
                                break;
                            case "3":
                                compulsoryCommissionRateAfterRatio  =  (rate * compulsoryCommissionRatio);
                                break;
                            case "4":
                                compulsoryCommissionRateAfterRatio  =  (rate / compulsoryCommissionRatio);
                                break;
                        }
                        compulsoryCommissionRateReminder = commissionRate.getReminder();
                        logMap.put("compulsoryCommissionRate",compulsoryCommissionRate);
                        logMap.put("compulsoryCommissionRateAfterRatio",compulsoryCommissionRateAfterRatio);
                        logMap.put("compulsoryCommissionRateReminder",compulsoryCommissionRateReminder);
                        logMap.put("compulsoryCommissionRateRemark",commissionRate.getRemark());
                        //compulsoryCommission = compulsoryPremium * Double.parseDouble(df.format(rate * compulsoryCommissionRatio));
                    }
                }
            }
        if(commercialCommissionRateId.equals("")){
            commercialCommissionRateAfterRatio = 0D;
        }
        if(compulsoryCommissionRateId.equals("")){
            compulsoryCommissionRateAfterRatio = 0D;
        }

        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("commercialCommissionRate", commercialCommissionRateAfterRatio);
        resultMap.put("compulsoryCommissionRate", compulsoryCommissionRateAfterRatio);
        resultMap.put("commercialCommissionRateReminder",commercialCommissionRateReminder);
        resultMap.put("compulsoryCommissionRateReminder", compulsoryCommissionRateReminder);
        LogUtil.info("queryTaskCommissionRate called values="+JsonUtils.serialize(logMap));
        return ParamUtils.resultMap(true, resultMap);
    }

    @Override
    public String addCommissionRate(INSBCommissionRate commissionRate) {
        if (commissionRate.getAgreementid() == null || commissionRate.getAgreementid().equals(""))
            return ParamUtils.resultMap(false, "参数不正确");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        commissionRate.setId(UUIDUtils.random());
        //commissionRate.setRate(0D);
        commissionRate.setProductcode("channel");
        //commissionRate.setCommissiontype("01");
        commissionRate.setStatus("0");
        if(dateFormat.format(commissionRate.getEffectivetime()).equals(dateFormat.format(new Date()))){
            commissionRate.setEffectivetime(new Date());
        }
        commissionRate.setCreatetime(new Date());
        int result = insbCommissionRateDao.insertCommissionRate(commissionRate);

        if (result > 0) {

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", commissionRate.getId());
            map.put("status", commissionRate.getStatus());
            map.put("commissiontype", commissionRate.getCommissiontype());
            map.put("channelsource", commissionRate.getChannelsource());
            map.put("rate", commissionRate.getRate());
            map.put("effectivetime", dateFormat.format(commissionRate.getEffectivetime()));
            return ParamUtils.resultMap(true, map);
        } else
            return ParamUtils.resultMap(false, "操作失败");
    }

    @Override
    public String copyCommissionRate(String commissionRateId, String operator) {
        INSBCommissionRate commissionRate = insbCommissionRateDao.queryCommissionRateById(commissionRateId);
        commissionRate.setId(UUIDUtils.random());
        commissionRate.setStatus("0");
        commissionRate.setEffectivetime(getNextDate());
        commissionRate.setCreatetime(new Date());
        commissionRate.setModifytime(null);
        commissionRate.setTerminaltime(null);
        commissionRate.setNoti(null);
        commissionRate.setOperator(operator);
        int result = insbCommissionRateDao.insertCommissionRate(commissionRate);
        if (result > 0) {
            if (insbConditionsService.copyConditions("02", commissionRate.getId(), commissionRateId)) {
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("id", commissionRate.getId());
                map.put("status", commissionRate.getStatus());
                map.put("commissiontype", commissionRate.getCommissiontype());
                map.put("rate", commissionRate.getRate());
                map.put("effectivetime", dateFormat.format(commissionRate.getEffectivetime()));
                return ParamUtils.resultMap(true, map);
            }
        }
        return ParamUtils.resultMap(false, "操作失败");
    }

    private Date getNextDate(){
        //生效时间默认为下一天0点
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());   //设置当前日期
        calendar.add(Calendar.DATE, 1); //日期加1天
        Date effectivetime =calendar.getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(effectivetime);
        ParsePosition parsePosition = new ParsePosition(0);
        Date effectivetime_2 = formatter.parse(dateString,parsePosition);
        return effectivetime_2;
    }

    @Override
    public String updateCommissionRate(INSBCommissionRate commissionRate) {
        int result = insbCommissionRateDao.updateCommissionRate(commissionRate);
        if (result > 0)
            return ParamUtils.resultMap(true, "操作成功");
        else
            return ParamUtils.resultMap(false, "操作失败");
    }

    @Override
    public String updateCommissionRateStatus(INSBCommissionRate commissionRate) {
        if (commissionRate.getStatus().equals("1")) {
            String conditionsText = insbConditionsService.queryConditionsText(commissionRate.getId());
            commissionRate.setNoti(conditionsText);
            INSBCommissionRate temp = insbCommissionRateDao.queryCommissionRateById(commissionRate.getId());
            //System.out.println("DateUtil.getCurrentDate(DateUtil.Format_Date)="+DateUtil.getCurrentDate(DateUtil.Format_Date));
            //System.out.println("DateUtil.toString(temp.getEffectivetime(), DateUtil.Format_Date))="+DateUtil.toString(temp.getEffectivetime(), DateUtil.Format_Date));
            INSBCommissionRate tempCommissionRate = new INSBCommissionRate();
            tempCommissionRate.setId(commissionRate.getId());
            tempCommissionRate.setAgreementid(commissionRate.getAgreementid());
            tempCommissionRate.setProductcode(commissionRate.getProductcode());
            tempCommissionRate.setChannelsource(commissionRate.getChannelsource());
            tempCommissionRate.setCommissiontype(commissionRate.getCommissiontype());

            tempCommissionRate.setStatus(commissionRate.getStatus());
            tempCommissionRate.setNoti(commissionRate.getNoti());
            tempCommissionRate.setModifytime(new Date());
            tempCommissionRate.setOperator(commissionRate.getOperator());

            if(DateUtil.getCurrentDate(DateUtil.Format_Date).equals(DateUtil.toString(temp.getEffectivetime(), DateUtil.Format_Date))
                    ||(DateUtil.getCurrentDate(DateUtil.Format_Date).compareTo(DateUtil.toString(temp.getEffectivetime(), DateUtil.Format_Date)))>0){
                commissionRate.setEffectivetime(new Date());
                tempCommissionRate.setTerminaltime(commissionRate.getEffectivetime());
            }else{
                commissionRate.setEffectivetime(temp.getEffectivetime());
                tempCommissionRate.setTerminaltime(this.getLastDay(temp.getEffectivetime()));
            }
            tempCommissionRate.setEffectivetime(commissionRate.getEffectivetime());

           insbCommissionRateDao.updateTerminalTimeByNoti(tempCommissionRate);
        }else{
            INSBCommissionRate temp = insbCommissionRateDao.queryCommissionRateById(commissionRate.getId());
            commissionRate.setEffectivetime(temp.getEffectivetime());
            commissionRate.setTerminaltime(new Date());
        }
        commissionRate.setModifytime(new Date());
        int result = insbCommissionRateDao.updateCommissionRateStatus(commissionRate);
        if (result > 0)
            return ParamUtils.resultMap(true, "操作成功");
        else
            return ParamUtils.resultMap(false, "操作失败");
    }

    private Date getLastDay(Date day){
        //生效时间默认为下一天0点
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(day);   //设置当前日期
        calendar.add(Calendar.DATE, -1); //日期加1天
        Date time =calendar.getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(time);
        dateString +=" 23:59:59";
        ParsePosition parsePosition = new ParsePosition(0);
        Date time2 = formatter2.parse(dateString,parsePosition);
        return time2;
    }

    @Override
    public String updateCommissionRateTerminalTime(INSBCommissionRate commissionRate){
        INSBCommissionRate insbCommissionRate = insbCommissionRateDao.selectById(commissionRate.getId());
        insbCommissionRate.setTerminaltime(commissionRate.getTerminaltime());
        insbCommissionRate.setModifytime(new Date());
        return this.updateCommissionRate(insbCommissionRate);

    }

    @Override
    public String delCommissionRate(String id) {
        Map<String ,Object> params = new HashMap<>();
        params.put("commissionrateid",id);
        if (insbCommissionService.existRate(params))
            return ParamUtils.resultMap(false, "佣金配置已生效生成过佣金，不可删除");

        INSBCommissionRate commissionRate = insbCommissionRateDao.queryCommissionRateById(id);
        if (commissionRate.getStatus().equals("1"))
            return ParamUtils.resultMap(false, "佣金配置需关闭后才可删除");

        int result = insbCommissionRateDao.delCommissionRate(id);

        insbConditionsService.deleteConditionsBySourceId(id);

        if (result > 0)
            return ParamUtils.resultMap(true, "操作成功");
        else
            return ParamUtils.resultMap(false, "操作失败");
    }

    /**
     * 更新任务佣金
     *
     * @param agentTaskModel
     * @return
     */
    private void updateCommission(AgentTaskModel agentTaskModel, String commissionType, String commissionRateId, Double counts) {
        INSBCommission commission = new INSBCommission();
        commission.setTaskid(agentTaskModel.getTaskid());
        commission.setAgreementid(agentTaskModel.getAgreementid());
        commission.setCommissiontype(commissionType);
        commission.setCommissionrateid(commissionRateId);
        commission.setCounts(counts);
        commission.setProvidercode(agentTaskModel.getProvidercode());
        commission.setCommissionFlag(agentTaskModel.getCommissionFlag());
        commission.setCommissionRatioId(agentTaskModel.getCommissionRatioId());
        commission.setChannelId(agentTaskModel.getChannelid());
        if("insured".equals(agentTaskModel.getCommissionFlag())){
            commission.setStatus("2");
        }else {
            commission.setStatus("0");
        }
        commission.setOperator("admin");
        insbCommissionService.saveCommission(commission);
    }

    @Override
    public String testCommissionRule(Map<String, Object> params) {
        Double commercialCommission = 0D;
        Double compulsoryCommission = 0D;

        Double commercialCommissionRate = 1D;
        Double compulsoryCommissionRate = 1D;
        Double commercialCommissioRatio = 1D;
        Double compulsoryCommissionRatio = 1D;

        Double commercialCommissionRateAfterRatio = 0D;
        Double compulsoryCommissionRateAfterRatio = 0D;

        String commercialConditionParams = "";
        String compulsoryConditionParams = "";

        String commercialCommissionRateId = "";
        String compulsoryCommissionRateId = "";

        String commercialCalculateType = "3";//默认乘  1,2,3,4 对应加减乘除
        String compulsoryCalculateType = "3";//默认乘  1,2,3,4 对应加减乘除

        String commercialCommissionRateRemark = "";
        String compulsoryCommissionRateRemark = "";

        String commercialCommissionRatioRemark = "";
        String compulsoryCommissionRatioRemark = "";

        String commercialCommissionRatioId = "";
        String compulsoryCommissionRatioId = "";

        Double commercialTaxrate = 1D;
        Double compulsoryTaxrate = 1D;

        String agreementId = params.getOrDefault("agreementid", "").toString();

        Double commercialPremium = Double.parseDouble(params.getOrDefault("commercialPremium", "0").toString());
        Double compulsoryPremium = Double.parseDouble(params.getOrDefault("compulsoryPremium", "0").toString());

        Map<String, Object> qmap = new HashMap<String,Object>();
        qmap.put("channelsource",params.getOrDefault("channelid", "").toString());
        long total = insbCommissionRateDao.queryPagingListCount(qmap);
        String channelSource = "";
        if(total > 0){
            channelSource = params.getOrDefault("channelid", "").toString();
        }else{
            channelSource = "channel";
        }
        String statustype =  params.getOrDefault("statustype", "").toString();
        ArrayList statusList = new ArrayList();
        if("1".equals(statustype)){
            statusList.add("1");
        }else {
            statusList.add("0");
            statusList.add("1");
        }

        Map<String,Object> ratioParams = new HashMap<String,Object>();

        List<INSBCommissionRate> commissionRateList = insbCommissionRateDao.queryCommissionByAgreementTest(agreementId, "channel", channelSource,statusList);
        for (INSBCommissionRate commissionRate : commissionRateList) {
            if (insbConditionsService.execute("02", commissionRate.getId(), params)) {
                Double rate = commissionRate.getRate();
                //==========================
                 /*
                     *查询佣金系数
                     * */
                ratioParams = new HashMap<String,Object>();
                //AgentTaskModel agentTaskModel = new AgentTaskModel();
               // String deptCode = insbConditionsService.queryDeptCodeByTask(agentTaskModel);
                ratioParams.put("deptCode",params.getOrDefault("deptCode", "").toString());
                if(commissionRate.getCommissiontype().equals("03")) {
                    ratioParams.put("compulsoryRate", params.getOrDefault("compulsoryRate", "").toString());
                }
                List<INSBCommissionratio> commissionRatioList = insbCommissionratioDao.queryCommissionRatioByChannel(params.getOrDefault("channelid", "").toString());

                for(INSBCommissionratio commissionratio:commissionRatioList){
                    if (insbConditionsService.execute("04", commissionratio.getId(), ratioParams)) {
                        Double ratio = commissionratio.getRatio();
                        if(commissionratio.getCommissiontype().equals("01")  && ratio <= commercialCommissioRatio){
                            commercialCommissioRatio = ratio;
                            commercialCalculateType = commissionratio.getCalculatetype();
                            commercialTaxrate = commissionratio.getTaxrate();
                            commercialCommissionRatioRemark =  commissionratio.getRemark();
                            commercialCommissionRatioId = commissionratio.getId();
                        }
                        if(commissionratio.getCommissiontype().equals("03")  && ratio <= compulsoryCommissionRatio){
                            compulsoryCommissionRatio = ratio;
                            compulsoryCalculateType = commissionratio.getCalculatetype();
                            compulsoryTaxrate = commissionratio.getTaxrate();
                            compulsoryCommissionRatioRemark =  commissionratio.getRemark();
                            compulsoryCommissionRatioId = commissionratio.getId();
                        }
                    }
                }
                //
                DecimalFormat df=new DecimalFormat("#0.##");
                df.setRoundingMode(RoundingMode.FLOOR);
                //==========================
                if (commissionRate.getCommissiontype().equals("01") && rate <= commercialCommissionRate) {
                    commercialCommissionRateId = commissionRate.getId();
                    commercialCommissionRate = rate;
                   // commercialCommissionRateAfterRatio = Double.parseDouble(df.format(rate * commercialCommissioRatio));
                   // commercialCommission = commercialPremium * commercialCommissionRateAfterRatio;
                    commercialCommissionRatioRemark = commissionRate.getRemark();

                    switch (commercialCalculateType){
                        case "1":
                            commercialCommission  =  Double.parseDouble(df.format((commercialPremium/commercialTaxrate)*(rate + commercialCommissioRatio)));
                            commercialCommissionRateAfterRatio = rate + commercialCommissioRatio;
                            break;
                        case "2":
                            commercialCommission  =  Double.parseDouble(df.format((commercialPremium/commercialTaxrate)*(rate - commercialCommissioRatio)));
                            commercialCommissionRateAfterRatio = rate - commercialCommissioRatio;
                            break;
                        case "3":
                            commercialCommission  =  Double.parseDouble(df.format((commercialPremium/commercialTaxrate)*(rate * commercialCommissioRatio)));
                            commercialCommissionRateAfterRatio = rate * commercialCommissioRatio;
                            break;
                        case "4":
                            commercialCommission  =  Double.parseDouble(df.format((commercialPremium/commercialTaxrate)*(rate / commercialCommissioRatio)));
                            commercialCommissionRateAfterRatio = rate / commercialCommissioRatio;
                            break;
                    }

                    commercialConditionParams = commissionRate.getNoti();
                }
                if (commissionRate.getCommissiontype().equals("03") && rate <= compulsoryCommissionRate) {
                    compulsoryCommissionRate = rate;
                    compulsoryCommissionRateId = commissionRate.getId();
                   // compulsoryCommissionRateAfterRatio = Double.parseDouble(df.format(rate * compulsoryCommissionRatio));
                    compulsoryCommissionRatioRemark = commissionRate.getRemark();
                    switch (compulsoryCalculateType){
                        case "1":
                            compulsoryCommission  =  Double.parseDouble(df.format((compulsoryPremium/compulsoryTaxrate)*(rate + compulsoryCommissionRatio)));
                            compulsoryCommissionRateAfterRatio = rate + compulsoryCommissionRatio;
                            break;
                        case "2":
                            compulsoryCommission  =  Double.parseDouble(df.format((compulsoryPremium/compulsoryTaxrate)*(rate - compulsoryCommissionRatio)));
                            compulsoryCommissionRateAfterRatio = rate - compulsoryCommissionRatio;
                            break;
                        case "3":
                            compulsoryCommission  =  Double.parseDouble(df.format((compulsoryPremium/compulsoryTaxrate)*(rate * compulsoryCommissionRatio)));
                            compulsoryCommissionRateAfterRatio = rate * compulsoryCommissionRatio;
                            break;
                        case "4":
                            compulsoryCommission  =  Double.parseDouble(df.format((compulsoryPremium/compulsoryTaxrate)*(rate / compulsoryCommissionRatio)));
                            compulsoryCommissionRateAfterRatio = rate / compulsoryCommissionRatio;
                            break;
                    }
                    compulsoryConditionParams = commissionRate.getNoti();
                }
            }
        }

        if("".equals(commercialCommissionRateId)){
            commercialCommissionRate = 0D;
        }
        if("".equals(compulsoryCommissionRateId)){
            compulsoryCommissionRate = 0D;
        }

        Map<String, Object> map = new HashMap<String, Object>();
        Map<String, Object> commercialMap = new HashMap<String, Object>();
        Map<String, Object> compulsoryMap = new HashMap<String, Object>();
        commercialMap.put("commercialCommissionRateId",  commercialCommissionRateId);
        compulsoryMap.put("compulsoryCommissionRateId",  compulsoryCommissionRateId);
        commercialMap.put("commercialCommissionRatioId",  commercialCommissionRatioId);
        compulsoryMap.put("compulsoryCommissionRatioId",  compulsoryCommissionRatioId);
        commercialMap.put("commercialCommissionRateRemark",  commercialCommissionRateRemark);
        compulsoryMap.put("compulsoryCommissionRateRemark",  compulsoryCommissionRateRemark);
        commercialMap.put("commercialCommissionRatioRemark",  commercialCommissionRatioRemark);
        compulsoryMap.put("compulsoryCommissionRatioRemark",  compulsoryCommissionRatioRemark);
        commercialMap.put("commercialCommission",  commercialCommission);
        compulsoryMap.put("compulsoryCommission",  compulsoryCommission);
        commercialMap.put("commercialCalculateType",  commercialCalculateType);
        compulsoryMap.put("compulsoryCalculateType",  compulsoryCalculateType);
        commercialMap.put("commercialTaxrate",  commercialTaxrate);
        compulsoryMap.put("compulsoryTaxrate",  compulsoryTaxrate);
        commercialMap.put("commercialCommissionRate", commercialCommissionRate);
        compulsoryMap.put("compulsoryCommissionRate", compulsoryCommissionRate);
        commercialMap.put("commercialCommissioRatio", commercialCommissioRatio);
        compulsoryMap.put("compulsoryCommissionRatio", compulsoryCommissionRatio);
        commercialMap.put("commercialCommissionRateAfterRatio", commercialCommissionRateAfterRatio);
        compulsoryMap.put("compulsoryCommissionRateAfterRatio", compulsoryCommissionRateAfterRatio);
        commercialMap.put("commercialConditionParams", commercialConditionParams);
        compulsoryMap.put("compulsoryConditionParams", compulsoryConditionParams);
        map.put("ratioParams",ratioParams);
        map.put("commercialMap",commercialMap);
        map.put("compulsoryMap",compulsoryMap);
        return ParamUtils.resultMap(true, map);
    }

    @Override
    public String queryCommissionProvider(String productcode) {
        List<Map<Object, Object>> providerList = insbCommissionRateDao.queryCommissionProvider(productcode);
        return ParamUtils.resultMap(providerList.size(), providerList);
    }

    @Override
    public String completeTaskCommission(String taskid){
        String result = "操作成功";
        try {
            insbCommissionDao.lockTaskCommission(taskid);
            insbCommissionDao.completeTask(taskid);
        }catch(Exception e){
            result = e.getMessage();
            LogUtil.info("completeTaskCommission 异常:"+e.getMessage());
        }
        return ParamUtils.resultMap(true, result);
    }

    @Override
    public Map<String, String> batchUpdateCommissionRateStatus(String rateIds,String status,String userCode){
        Map<String, String> resultMap = new HashMap<String, String>();
        try {
            String[] rateIdArray = rateIds.split(",");
            for (String rateId : rateIdArray) {
                INSBCommissionRate commissionRate = insbCommissionRateDao.selectById(rateId);
                commissionRate.setId(rateId);
                commissionRate.setOperator(userCode);
                commissionRate.setModifytime(new Date());
                commissionRate.setStatus(status);
                this.updateCommissionRateStatus(commissionRate);
            }
            resultMap.put("code", "0");
            resultMap.put("message", "操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            resultMap.put("code", "1");
            resultMap.put("message", "操作失败，请稍候重试");
        }
        return resultMap;
    }
    @Override
    public String batchCopyCommissionRate(String rateIds,String agreementIds,String userCode){
        String[] rateIdArray = rateIds.split(",");
        String[] agreementIdArray = agreementIds.split(",");
        //生效时间默认为下一天0点
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());   //设置当前日期
        calendar.add(Calendar.DATE, 1); //日期加1天
        Date effectivetime =calendar.getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(effectivetime);
        ParsePosition parsePosition = new ParsePosition(0);
        Date effectivetime_2 = formatter.parse(dateString,parsePosition);
        for (String rateId : rateIdArray) {
            for(String agreementId : agreementIdArray){
                INSBCommissionRate commissionRate = insbCommissionRateDao.queryCommissionRateById(rateId);
                commissionRate.setId(UUIDUtils.random());
                commissionRate.setStatus("0");
                commissionRate.setEffectivetime(effectivetime_2);
                commissionRate.setCreatetime(new Date());
                commissionRate.setModifytime(null);
                commissionRate.setTerminaltime(null);
                commissionRate.setNoti(null);
                commissionRate.setOperator(userCode);
                commissionRate.setAgreementid(agreementId);
                int result = insbCommissionRateDao.insertCommissionRate(commissionRate);
                if (result > 0) {
                    if (insbConditionsService.copyConditions("02", commissionRate.getId(), rateId)) {
                        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("id", commissionRate.getId());
                        map.put("status", commissionRate.getStatus());
                        map.put("commissiontype", commissionRate.getCommissiontype());
                        map.put("rate", commissionRate.getRate());
                        map.put("effectivetime", dateFormat.format(commissionRate.getEffectivetime()));
                    }
                }
            }
        }
        return ParamUtils.resultMap(true, "操作成功！");
    }

    @Override
    public String batchUpdateCommissionRateTerminalTime(String rateIds,Date terminalTime,String userCode){
        String[] rateIdArray = rateIds.split(",");
        INSBCommissionRate insbCommissionRate = new INSBCommissionRate();
        for (String rateId : rateIdArray) {
            insbCommissionRate = insbCommissionRateDao.selectById(rateId);
            insbCommissionRate.setTerminaltime(terminalTime);
            insbCommissionRate.setModifytime(new Date());
            insbCommissionRate.setOperator(userCode);
            this.updateCommissionRate(insbCommissionRate);
        }
        return ParamUtils.resultMap(true, "操作成功！");
    }
    @Override
    public String queryCommissionRateRule(Map<String, Object> params){
        String channelid = String.valueOf(params.get("channelid"));
        int pageSize = Integer.parseInt(params.getOrDefault("pageSize","10").toString());
        int page = Integer.parseInt(params.getOrDefault("page","1").toString());
        if(page == 0){
            page = 1;
        }
        int offset = (page - 1)*pageSize;
        params.put("limit",pageSize);
        params.put("offset",offset);
        long total = insbCommissionRateDao.queryCommissionRateRuleCount(params);
        Map<String,Object> resultMap = new HashMap<>();
        if(total == 0){
            String providerid = String.valueOf(params.get("providerid"));
            String deptid = String.valueOf(params.get("deptid5"));//应用平台机构传参
            String commissiontype = String.valueOf(params.get("commissiontype"));
            resultMap.put("total",total);
            resultMap.put("code","01");
            if(!StringUtil.isEmpty(deptid) && !StringUtil.isEmpty(providerid)){
                resultMap.put("msg","所属平台没有该家供应商佣金配置！");
            }else if(StringUtil.isEmpty(deptid) && !StringUtil.isEmpty(providerid)){
                resultMap.put("msg","没有该家供应商佣金配置！");
            }else if(StringUtil.isEmpty(deptid) && !StringUtil.isEmpty(providerid)  && !StringUtil.isEmpty(commissiontype)){
                resultMap.put("msg","没有该家供应商佣金配置！");
            }
            resultMap.put("rows",new ArrayList());
           return JsonUtils.serialize(resultMap);
        }
        List<Map<String,Object>> commissionInfoList =  insbCommissionRateDao.queryCommissionRateRule(params);
        String commissiontType = "";
        String deptCode = "";
        Map<String,Object> conditionParams = new HashMap<>();
        Map<String,Object> ratioParams = new HashMap<>();
        Double rate = 0D;
        INSBCommissionratio tempCommissionRatio = new INSBCommissionratio();
        for(Map<String,Object> row :commissionInfoList ){
            conditionParams.clear();
            ratioParams.clear();
            commissiontType =  row.get("commissiontype").toString();
            rate =  Double.parseDouble(row.get("rate").toString());
            deptCode =  row.get("deptid").toString();
            conditionParams.put("sourceid",row.get("id"));
            row.put("conditions", insbConditionsDao.queryPagingList(conditionParams));
            ratioParams.put("deptCode",deptCode);
            if("03".equals(commissiontType)) {
               // ratioParams.put("compulsoryRate", params.getOrDefault("compulsoryRate", "").toString());
                ratioParams.put("compulsoryRate", rate);
            }
            //row.put("ratioParams",ratioParams);

            List<INSBCommissionratio> commissionRatioList = insbCommissionratioDao.queryCommissionRatioByChannel(channelid);
            Double compulsoryRatio = 1D;
            Double commercialRatio = 1D;
            //初始化商业险佣金系数
            INSBCommissionratio commercialCommissionratio = new INSBCommissionratio();
            commercialCommissionratio.setTaxrate(1D);
            commercialCommissionratio.setChannelid(channelid);
            commercialCommissionratio.setCalculatetype("3");
            commercialCommissionratio.setRatio(1D);
            commercialCommissionratio.setCommissiontype("01");
            commercialCommissionratio.setId("123456789");
            commercialCommissionratio.setRemark("默认数据");
            commercialCommissionratio.setNoti("");
            commercialCommissionratio.setStatus("");
            commercialCommissionratio.setEffectivetime(new Date());
            commercialCommissionratio.setTerminaltime(new Date());
            commercialCommissionratio.setOperator("admin");
            //初始化交强险佣金系数
            INSBCommissionratio compulsoryCommissionratio = new INSBCommissionratio();
            compulsoryCommissionratio.setTaxrate(1D);
            compulsoryCommissionratio.setChannelid(channelid);
            compulsoryCommissionratio.setCalculatetype("3");
            compulsoryCommissionratio.setRatio(1D);
            compulsoryCommissionratio.setCommissiontype("03");
            compulsoryCommissionratio.setId("123456789");
            compulsoryCommissionratio.setRemark("默认数据");
            compulsoryCommissionratio.setNoti("");
            compulsoryCommissionratio.setStatus("");
            compulsoryCommissionratio.setEffectivetime(new Date());
            compulsoryCommissionratio.setTerminaltime(new Date());
            compulsoryCommissionratio.setOperator("admin");
            for(INSBCommissionratio commissionratio:commissionRatioList){
                if (insbConditionsService.execute("04", commissionratio.getId(), ratioParams)) {
                    Double ratio = commissionratio.getRatio();
                    if(commissionratio.getCommissiontype().equals("01")  && ratio <= commercialRatio){
                        commercialRatio = ratio;
                        commercialCommissionratio = commissionratio;
                    }
                    if(commissionratio.getCommissiontype().equals("03")  && ratio <= compulsoryRatio){
                        compulsoryRatio = ratio;
                        compulsoryCommissionratio = commissionratio;
                    }
                }
            }
            if("01".equals(commissiontType)){
                tempCommissionRatio = commercialCommissionratio;
                row.put("ratio",tempCommissionRatio);
            }else {
                tempCommissionRatio = compulsoryCommissionratio;
                row.put("ratio",tempCommissionRatio);
            }
            switch (tempCommissionRatio.getCalculatetype()){
                case "1" :
                    row.put("feepolicy",rate + tempCommissionRatio.getRatio());
                    break;
                case "2" :
                    row.put("feepolicy",rate - tempCommissionRatio.getRatio());
                    break;
                case "3" :
                    row.put("feepolicy",rate * tempCommissionRatio.getRatio());
                    break;
                case "4" :
                    row.put("feepolicy",rate / tempCommissionRatio.getRatio());
                    break;
            }


        }

        resultMap.put("total",total);
        resultMap.put("code","00");
        resultMap.put("msg","查询成功！");
        resultMap.put("rows",commissionInfoList);
        return JsonUtils.serialize(resultMap);
    }

    public String queryCommissionInfo(Map<String, Object> params){
        int pageSize = Integer.parseInt(params.getOrDefault("pageSize","10").toString());
        int page = Integer.parseInt(params.getOrDefault("page","1").toString());
        if(page == 0){
            page = 1;
        }
        int offset = (page - 1)*pageSize;
        params.put("limit",pageSize);
        params.put("offset",offset);
        long total = insbCommissionDao.queryCommissionInfoCount(params);
        List<Map<String,Object>> commissionInfoList =  insbCommissionDao.queryCommissionInfo(params);
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("total",total);
        resultMap.put("rows",commissionInfoList);
        return JsonUtils.serialize(resultMap);
    }

    public String batchDeleteCommissionRate(String rateIds,String userCode){
        Map<String, Object> resultMap = new HashMap<>();
        try {
            String[] rateIdArray = rateIds.split(",");
            StringBuffer msg = new StringBuffer();
            msg.append("注意：");
            Map<String ,Object> params = new HashMap<>();
            boolean eff = false;
            for (String rateId : rateIdArray) {
                params.put("commissionrateid",rateId);
                if (insbCommissionService.existRate(params)){
                    eff = true;
                    msg.append("rateId=");
                    msg.append(rateId);
                    msg.append(";");
                }else{
                    delCommissionRate(rateId);
                }
            }
            resultMap.put("code", "0");
            resultMap.put("msg", (eff?msg.append(" 佣金配置已生成过佣金没有删除，其他规则删除完成!").toString():"删除成功！"));
        }catch (Exception e){
            resultMap.put("code", "1");
            resultMap.put("msg", "删除异常！");
            LogUtil.info("批量删除异常！"+e.getMessage());
        }
        return JsonUtils.serialize(resultMap);
    }

    public String queryCommissionInfoForBC(Map<String, Object> params){
        /*int pageSize = Integer.parseInt(params.getOrDefault("pageSize","10").toString());
        int page = Integer.parseInt(params.getOrDefault("page","1").toString());
        if(page == 0){
            page = 1;
        }
        int offset = (page - 1)*pageSize;
        params.put("limit",pageSize);
        params.put("offset",offset);*/
        long total = insbCommissionDao.queryCommissionInfoForBCCount(params);
        List<Map<String,Object>> commissionInfoList =  insbCommissionDao.queryCommissionInfoForBC(params);
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("total",total);
        resultMap.put("rows",commissionInfoList);
        return JsonUtils.serialize(resultMap);
    }

}
