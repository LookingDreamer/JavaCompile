package com.zzb.extra.service.impl;

import javax.annotation.Resource;

import com.cninsure.core.utils.BeanUtils;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.UUIDUtils;
import com.zzb.chn.util.JsonUtils;
import com.zzb.extra.controller.vo.ActivityPrizesVo;
import com.zzb.extra.controller.vo.CommissionVo;
import com.zzb.extra.dao.INSBCommissionRateDao;
import com.zzb.extra.entity.INSBCommission;
import com.zzb.extra.entity.INSBCommissionRate;
import com.zzb.extra.model.AgentTaskModel;
import com.zzb.extra.service.INSBCommissionRateService;
import com.zzb.extra.service.INSBCommissionService;
import com.zzb.extra.service.INSBConditionsService;
import com.zzb.extra.util.ParamUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class INSBCommissionRateServiceImpl extends BaseServiceImpl<INSBCommissionRate> implements
        INSBCommissionRateService {
    @Resource
    private INSBCommissionRateDao insbCommissionRateDao;

    @Resource
    private INSBConditionsService insbConditionsService;

    @Resource
    private INSBCommissionService insbCommissionService;

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
        Map<String,Object> logMap = new HashMap<String,Object>();
        logMap.put("AgentTaskModel",agentTaskModel);
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
                    logMap.put("commissionRate",commissionRate);
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
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("commercialCommission", (double) commercialCommission.intValue());
        map.put("compulsoryCommission", (double) compulsoryCommission.intValue());
        map.put("adCommercialCommission", (double) adCommercialCommission.intValue());
        map.put("adCompulsoryCommission", (double) adCompulsoryCommission.intValue());
        logMap.put("result",map);
        LogUtil.info("queryTaskCommission: "+JsonUtils.serialize(logMap));
        return ParamUtils.resultMap(true, map);
    }

    @Override
    public String addCommissionRate(INSBCommissionRate commissionRate) {
        if (commissionRate.getAgreementid() == null || commissionRate.getAgreementid().equals(""))
            return ParamUtils.resultMap(false, "参数不正确");

        commissionRate.setId(UUIDUtils.random());
        commissionRate.setRate(0D);
        commissionRate.setCommissiontype("01");
        commissionRate.setStatus("0");
        commissionRate.setEffectivetime(new Date());
        commissionRate.setCreatetime(new Date());
        int result = insbCommissionRateDao.insertCommissionRate(commissionRate);

        if (result > 0) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", commissionRate.getId());
            map.put("status", commissionRate.getStatus());
            map.put("commissiontype", commissionRate.getCommissiontype());
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
        commissionRate.setEffectivetime(new Date());
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
            commissionRate.setEffectivetime(temp.getEffectivetime());
            insbCommissionRateDao.updateTerminalTimeByNoti(commissionRate);
        }else{
            INSBCommissionRate temp = insbCommissionRateDao.queryCommissionRateById(commissionRate.getId());
            commissionRate.setEffectivetime(temp.getEffectivetime());
        }
        commissionRate.setModifytime(new Date());
        int result = insbCommissionRateDao.updateCommissionRateStatus(commissionRate);
        if (result > 0)
            return ParamUtils.resultMap(true, "操作成功");
        else
            return ParamUtils.resultMap(false, "操作失败");
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
        commission.setStatus("0");
        commission.setOperator("admin");
        insbCommissionService.saveCommission(commission);
    }

    @Override
    public String testCommissionRule(Map<String, Object> params) {
        Double commercialCommission = 0D;
        Double compulsoryCommission = 0D;
        Double adCommercialCommission = 0D;
        Double adCompulsoryCommission = 0D;

        Double commercialCommissionRate = 1D;
        Double compulsoryCommissionRate = 1D;

        String commercialConditionParams = "";
        String compulsoryConditionParams = "";

        String agreementId = params.getOrDefault("agreementid", "").toString();

        Double commercialPremium = Double.parseDouble(params.getOrDefault("commercialPremium", "0").toString());
        Double compulsoryPremium = Double.parseDouble(params.getOrDefault("compulsoryPremium", "0").toString());
        List<INSBCommissionRate> commissionRateList = insbCommissionRateDao.queryCommissionByAgreement(agreementId,"minizzb","minizzb");
        for (INSBCommissionRate commissionRate : commissionRateList) {
            if (insbConditionsService.execute("02", commissionRate.getId(), params)) {
                Double rate = commissionRate.getRate();
                if (commissionRate.getCommissiontype().equals("01") && rate <= commercialCommissionRate) {
                    commercialCommissionRate = rate;
                    commercialCommission = commercialPremium * rate;
                    commercialConditionParams = commissionRate.getNoti();
                }
                if (commissionRate.getCommissiontype().equals("03") && rate <= compulsoryCommissionRate) {
                    compulsoryCommissionRate = rate;
                    compulsoryCommission = compulsoryPremium * rate;
                    compulsoryConditionParams = commissionRate.getNoti();
                }
            }
        }

        if (commercialCommission > 0)
            adCommercialCommission = commercialPremium * 0.03;

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("commercialCommission", (double) commercialCommission.intValue());
        map.put("compulsoryCommission", (double) compulsoryCommission.intValue());
        map.put("adCommercialCommission", (double) adCommercialCommission.intValue());
        map.put("adCompulsoryCommission", (double) adCompulsoryCommission.intValue());
        map.put("commercialCommissionRate", commercialCommissionRate);
        map.put("compulsoryCommissionRate", compulsoryCommissionRate);
        map.put("commercialConditionParams", commercialConditionParams);
        map.put("compulsoryConditionParams", compulsoryConditionParams);
        return ParamUtils.resultMap(true, map);
    }

    @Override
    public String queryCommissionProvider(String productcode) {
        List<Map<Object, Object>> providerList = insbCommissionRateDao.queryCommissionProvider(productcode);
        return ParamUtils.resultMap(providerList.size(), providerList);
    }

}