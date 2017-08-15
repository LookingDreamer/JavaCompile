package com.zzb.extra.service.impl;

import javax.annotation.Resource;

import com.cninsure.core.utils.UUIDUtils;
import com.zzb.chn.util.JsonUtils;
import com.zzb.extra.controller.vo.ConditionsVo;
import com.zzb.extra.dao.INSBConditionParamsDao;
import com.zzb.extra.entity.INSBConditionParams;
import com.zzb.extra.model.AgentTaskModel;
import com.zzb.extra.util.ParamUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;
import com.zzb.extra.dao.INSBConditionsDao;
import com.zzb.extra.entity.INSBConditions;
import com.zzb.extra.service.INSBConditionsService;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional
public class INSBConditionsServiceImpl extends BaseServiceImpl<INSBConditions> implements
        INSBConditionsService {
    @Resource
    private INSBConditionsDao insbConditionsDao;

    @Resource
    private INSBConditionParamsDao insbConditionParamsDao;

    @Override
    protected BaseDao<INSBConditions> getBaseDao() {
        return insbConditionsDao;
    }

    @Override
    public String queryPagingList(Map<String, Object> map) {
        long total = insbConditionsDao.queryPagingListCount(map);
        List<Map<Object, Object>> infoList = insbConditionsDao.queryPagingList(map);
        return ParamUtils.resultMap(total, infoList);
    }

    @Override
    public String saveConditions(INSBConditions conditions) {
        conditions.setExpression(conditions.getExpression().replace("& gt;", ">").replace("&gt;",">").replace("& lt;","<").replace("&lt;","<"));
        //System.out.println("conditions="+JsonUtils.serialize(conditions));
        INSBConditionParams param = insbConditionParamsDao.queryByName(conditions.getParamname());
        conditions.setParamdatatype(param.getDatatype());
        int result = 0;
        if (conditions.getId() == null || conditions.getId().equals("")) {
            conditions.setId(UUIDUtils.random());
            conditions.setCreatetime(new Date());
            result = insbConditionsDao.insertConditions(conditions);
        } else {
            conditions.setModifytime(new Date());
            result = insbConditionsDao.updateConditions(conditions);
        }

        if (result > 0)
            return ParamUtils.resultMap(true, "操作成功");
        else
            return ParamUtils.resultMap(false, "操作失败");
    }

    @Override
    public String deleteConditionsById(String id) {
        int result = insbConditionsDao.deleteConditionsById(id);

        if (result > 0)
            return ParamUtils.resultMap(true, "操作成功");
        else
            return ParamUtils.resultMap(false, "操作失败");
    }

    @Override
    public void deleteConditionsBySourceId(String sourceId) {
        insbConditionsDao.deleteConditionsBySourceId(sourceId);
    }

    @Override
    public String queryAgreementIdByTask(AgentTaskModel agentTaskModel) {
        return insbConditionsDao.queryAgreementIdByTask(agentTaskModel);
    }
    @Override
    public String queryDeptCodeByTask(AgentTaskModel agentTaskModel) {
        return insbConditionsDao.queryDeptCodeByTask(agentTaskModel);
    }


    /**
     * 复制条件
     *
     * @param source
     * @param sourceId
     * @param fromId
     * @return
     */
    public Boolean copyConditions(String source, String sourceId, String fromId) {
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("conditionsource", source);
            map.put("sourceid", fromId);
            List<INSBConditions> conditionsList = insbConditionsDao.queryConditions(map);
            for (INSBConditions condition : conditionsList) {
                condition.setId(UUIDUtils.random());
                condition.setSourceid(sourceId);
                condition.setCreatetime(new Date());
                insbConditionsDao.insertConditions(condition);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String queryConditionsText(String sourceId) {
        String conditionsText = "";
        Map<String, Object> map = new HashMap<>();
        map.put("sourceid", sourceId);
        List<INSBConditions> conditions = insbConditionsDao.queryConditions(map);
        for (INSBConditions condition : conditions) {
            conditionsText += (conditionsText.equals("") ? "" : ";") + condition.getParamname() + condition.getExpression() + condition.getParamvalue();
        }
        return conditionsText;
    }

    /**
     * 执行条件检索
     *
     * @param source
     * @param sourceId
     * @param params
     * @return
     */
    public Boolean execute(String source, String sourceId, Map<String, Object> params) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("conditionsource", source);
        map.put("sourceid", sourceId);
        List<INSBConditions> conditionsList = insbConditionsDao.queryConditions(map);
        for (INSBConditions condition : conditionsList) {
            if (params.containsKey(condition.getParamname())) {
                if (!judge(params.get(condition.getParamname()), condition.getParamvalue(), condition.getExpression(), condition.getParamdatatype()))
                    return false;
            } else return false;
        }
        return true;
    }

    /**
     * 条件判断
     *
     * @param paramValue
     * @param conditionValue
     * @param expression
     * @param dataType
     * @return
     */
    private Boolean judge(Object paramValue, Object conditionValue, String expression, String dataType) {
        try {
            if (paramValue != null) {
                paramValue = convertData(paramValue, dataType);
                conditionValue = convertData(conditionValue, dataType);
                switch (expression) {
                    case "=":
                        return paramValue.equals(conditionValue);
                    case "!=":
                        return !paramValue.equals(conditionValue);
                    case ">":
                        return Double.parseDouble(paramValue.toString()) > Double.parseDouble(conditionValue.toString());
                    case ">=":
                        return Double.parseDouble(paramValue.toString()) >= Double.parseDouble(conditionValue.toString());
                    case "<":
                        return Double.parseDouble(paramValue.toString()) < Double.parseDouble(conditionValue.toString());
                    case "<=":
                        return Double.parseDouble(paramValue.toString()) <= Double.parseDouble(conditionValue.toString());
                    case "contain":
                        return paramValue.toString().contains(conditionValue.toString());
                    case "un-contain":
                        return !paramValue.toString().contains(conditionValue.toString());
                    case "in":
                        String[] strings = conditionValue.toString().replace("，", ",").split(",");
                        boolean flag = false;
                        for(String str : strings){
                            if(paramValue.toString().contains(str.toString())){
                                flag = true;
                            }
                        }
                        return flag;
                       // return Arrays.asList(strings).contains(paramValue.toString());
                    case "un-in":
                        String[] uStrings = conditionValue.toString().replace("，", ",").split(",");
                        boolean flag2 = true;
                        for(String str : uStrings){
                            if(paramValue.toString().contains(str.toString())){
                                flag2 = false;
                            }
                        }
                        return flag2;
                        //return !Arrays.asList(uStrings).contains(paramValue.toString());
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 数据类型转换
     *
     * @param dataValue
     * @param dataType
     * @return
     */
    private Object convertData(Object dataValue, String dataType) {
        switch (dataType) {
            case "Boolean":
                try {
                    if (dataValue instanceof Boolean)
                        return dataValue;

                    return (dataValue.toString().equals("是") || dataValue.toString().equals("1") || dataValue.toString().toLowerCase().equals("true"));

                } catch (Exception e) {
                    return false;
                }
            case "Long":
                try {
                    if (dataValue instanceof Long)
                        return dataValue;
                    return Long.parseLong(dataValue.toString());
                } catch (Exception e) {
                    return 0L;
                }
            case "Integer":
                try {
                    if (dataValue instanceof Integer)
                        return dataValue;
                    return Integer.parseInt(dataValue.toString());
                } catch (Exception e) {
                    return 0;
                }
            case "Double":
                try {
                    if (dataValue instanceof Double)
                        return dataValue;
                    return Double.parseDouble(dataValue.toString());
                } catch (Exception e) {
                    return 0D;
                }
            case "Date":
                try {
                    if (dataValue instanceof Date)
                        return dataValue;
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    return sdf.parse(dataValue.toString());
                } catch (Exception e) {
                    return null;
                }
            default:
                return dataValue.toString();
        }
    }

    /**
     * 获取用户任务参数
     *
     * @param agentTaskModel
     * @return
     */
    @Override
    public Map<String, Object> queryParams(AgentTaskModel agentTaskModel) {
        Map<String, Object> params = new HashMap<String, Object>();

        if (agentTaskModel.getTaskid() != null && agentTaskModel.getProvidercode() != null)
            params = insbConditionsDao.queryParamsByTask(agentTaskModel);

        if (agentTaskModel.getTaskid() != null) {
            Map<String,Object> map = insbConditionsDao.queryLastInsertInfo(agentTaskModel);
            if(map==null){
                map = new HashMap<String,Object>();
                map.put("commercialClaimTimes",-1);
                map.put("compulsoryClaimTimes",-1);
                map.put("lastInsureCo","0");
            }
            params.putAll(map);
            //处理空指针异常
            //params.putAll(insbConditionsDao.queryLastInsertInfo(agentTaskModel));
        }

        if (agentTaskModel.getAgentid() != null)
            params.put("agentChannel", agentTaskModel.getAgentChannel());

        //合计商业险保单总数
        if (agentTaskModel.getAgentid() != null)
            params.put("totalCommercialPolicyCount", insbConditionsDao.queryParamsBySqlName("queryTotalCommercialPolicyCount", "counts", agentTaskModel));

        //活动期间商业险保单总数
        if (agentTaskModel.getAgentid() != null)
            params.put("activeCommercialPolicyCount", insbConditionsDao.queryParamsBySqlName("queryActiveCommercialPolicyCount", "counts", agentTaskModel));

        //是否首次出单
        if (agentTaskModel.getAgentid() != null)
            params.put("isFirstPolicy", (long) params.get("totalCommercialPolicyCount") == 0L);

        //投保险种类别
        if (agentTaskModel.getTaskid() != null && agentTaskModel.getProvidercode() != null)
            params.put("insureType", insbConditionsDao.queryParamsBySqlName("queryInsureType", "insuretype", agentTaskModel));

        //推荐注册人数
        if (agentTaskModel.getAgentid() != null)
            params.put("recommendedRegistration", insbConditionsDao.queryParamsBySqlName("queryRegistrationCount", "counts", agentTaskModel));

        //车辆损失险
        if (agentTaskModel.getTaskid() != null && agentTaskModel.getProvidercode() != null)
            params.putAll(insbConditionsDao.queryInsured("VehicleDemageIns", agentTaskModel));

        //第三者责任险
        if (agentTaskModel.getTaskid() != null && agentTaskModel.getProvidercode() != null)
            params.putAll(insbConditionsDao.queryInsured("ThirdPartyIns", agentTaskModel));

        //驾驶员责任险
        if (agentTaskModel.getTaskid() != null && agentTaskModel.getProvidercode() != null)
            params.putAll(insbConditionsDao.queryInsured("DriverIns", agentTaskModel));

        //乘客责任险
        if (agentTaskModel.getTaskid() != null && agentTaskModel.getProvidercode() != null)
            params.putAll(insbConditionsDao.queryInsured("PassengerIns", agentTaskModel));

        //全车盗抢险
        if (agentTaskModel.getTaskid() != null && agentTaskModel.getProvidercode() != null)
            params.putAll(insbConditionsDao.queryInsured("TheftIns", agentTaskModel));

        //交强险
        if (agentTaskModel.getTaskid() != null && agentTaskModel.getProvidercode() != null)
            params.putAll(insbConditionsDao.queryInsured("VehicleCompulsoryIns", agentTaskModel));

        //车船税
        if (agentTaskModel.getTaskid() != null && agentTaskModel.getProvidercode() != null)
            params.putAll(insbConditionsDao.queryInsured("VehicleTax", agentTaskModel));

        return params;
    }
}