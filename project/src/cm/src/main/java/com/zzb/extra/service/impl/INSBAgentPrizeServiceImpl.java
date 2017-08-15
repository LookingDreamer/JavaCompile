package com.zzb.extra.service.impl;

import javax.annotation.Resource;

import com.cninsure.core.utils.LogUtil;
import com.cninsure.system.entity.INSCCode;
import com.cninsure.system.service.INSCCodeService;
import com.common.HttpClientUtil;
import com.common.HttpSender;
import com.zzb.extra.controller.vo.CommissionVo;
import com.zzb.extra.dao.INSBAgentPrizeDao;
import com.zzb.extra.dao.INSBCommissionRateDao;
import com.zzb.extra.entity.INSBAgentPrize;
import com.zzb.extra.model.AgentTaskModel;
import com.zzb.extra.service.INSBAgentPrizeService;
import com.zzb.extra.service.INSBConditionsService;
import com.zzb.extra.util.ParamUtils;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cninsure.core.dao.BaseDao;
import com.cninsure.core.dao.impl.BaseServiceImpl;

import java.math.BigDecimal;
import java.util.*;

@Service
@Transactional
public class INSBAgentPrizeServiceImpl extends BaseServiceImpl<INSBAgentPrize> implements
        INSBAgentPrizeService {
    @Resource
    private INSBAgentPrizeDao insbAgentPrizeDao;

    @Resource
    private INSCCodeService codeService;

    @Resource
    private INSBCommissionRateDao insbCommissionRateDao;

    @Resource
    private INSBConditionsService insbConditionsService;

    @Override
    protected BaseDao<INSBAgentPrize> getBaseDao() {
        return insbAgentPrizeDao;
    }

    @Override
    public String queryPrizes(Map<String, Object> map) {

       return String.valueOf(insbAgentPrizeDao.queryRecommentAmount(map));

        /*//奖品类型
        List<INSCCode> marketPrizeTypeList = codeService.queryINSCCodeByCode("market", "prizeType");
        long total = insbAgentPrizeDao.queryPagingListCount(map);
        List<Map<Object, Object>> infoList = insbAgentPrizeDao.queryPagingList(map);
        Map<String,Object> resultMap = new HashMap<String,Object>();
        if (((Boolean)map.get("gettotal"))) {
            for (Map<Object, Object> tempMap : infoList) {
                for (INSCCode code : marketPrizeTypeList) {
                    if (code.getCodename().equals(tempMap.get("prizetype"))) {
                        //各个类型的奖品分类加总
                        BigDecimal counts = (BigDecimal) tempMap.get("counts");
                        resultMap.put(code.getCodename(), resultMap.get(code.getCodename()) == null ? counts.doubleValue() : (Double) resultMap.get(code.getCodename()) + counts.doubleValue());
                    }
                }
            }
        }

        resultMap.put("success", true);
        resultMap.put("total", total);
        resultMap.put("rows", infoList);
        return JSONObject.fromObject(resultMap).toString();
       */
    }

    @Override
    public int updateByTaskIdAndProviderCode(String taskid, String providercode,String agentid) {
        return insbAgentPrizeDao.updateByTaskIdAndProviderCode(taskid,providercode,agentid);
    }

    @Override
     public List<INSBAgentPrize> queryNoPaymentOrder(Map<String, Object> map) {
        return insbAgentPrizeDao.queryNoPaymentOrder(map);
    }

    @Override
    public String queryPrizeByAgentIdAndTask(Map<String, Object> map){
        ArrayList statusin = new ArrayList();
        statusin.add("3");
        statusin.add("4");
        map.put("statusin",statusin);
        List<INSBAgentPrize> prizes = insbAgentPrizeDao.queryPrizeByAgentIdAndTask(map);
        Map<String,Object> resultMap = new HashMap<String,Object>();
        resultMap.put("success",true);
        resultMap.put("rows",prizes);
        resultMap.put("total",prizes != null ? prizes.size() : 0);
        return JSONObject.fromObject(resultMap).toString();
    }

    @Override
    public String queryGotPrizes(Map<String, Object> map) {

        //奖品类型
        //只显示红包，不显示现金
        map.put("prizetype","01");
        List<INSCCode> marketPrizeTypeList = codeService.queryINSCCodeByCode("market", "prizeType");
        long total = insbAgentPrizeDao.queryPagingListCount(map);
        List<Map<Object, Object>> infoList = insbAgentPrizeDao.queryPagingList(map);
        Map<String,Object> resultMap = new HashMap<String,Object>();
        if (((Boolean)map.get("gettotal"))) {
            for (Map<Object, Object> tempMap : infoList) {
                for (INSCCode code : marketPrizeTypeList) {
                    if (code.getCodename().equals(tempMap.get("prizetype"))) {
                        //各个类型的奖品分类加总
                        BigDecimal counts = (BigDecimal) tempMap.get("counts");
                        resultMap.put(code.getCodename(), resultMap.get(code.getCodename()) == null ? counts.doubleValue() : (Double) resultMap.get(code.getCodename()) + counts.doubleValue());
                    }
                }
            }
        }
        resultMap.put("success", true);
        resultMap.put("total", total);
        resultMap.put("rows", infoList);
        return JSONObject.fromObject(resultMap).toString();
    }

    @Override
    public Map<String, Object> queryGotPrizesPolicy(String agentId) {
        List<Map<String, Object>> gotPrizesPolicyList = insbAgentPrizeDao.queryGotPrizesPolicy(agentId);
        Map<String, Object> reGotPrizesPolicy = new HashMap<>();
        Double amountNum = 0d;
        for (Map<String, Object> row:gotPrizesPolicyList){
            BigDecimal amount = (BigDecimal) row.get("amount");
            amountNum = amountNum + amount.doubleValue();
        }
        reGotPrizesPolicy.put("amount",amountNum) ; //总保币
        reGotPrizesPolicy.put("orderNum",gotPrizesPolicyList.size()); //有赏金总保单数
        reGotPrizesPolicy.put("rows",gotPrizesPolicyList);//有赏金总保单列表
        return reGotPrizesPolicy;
    }

    @Override
    public String queryAgentPrizeList(Map<String, Object> map) {
        long total = insbAgentPrizeDao.queryAgentPrizeListCount(map);
        List<Map<Object, Object>> infoList = insbAgentPrizeDao.queryAgentPrizeList(map);
        return ParamUtils.resultMap(total, infoList);
    }

    @Override
    public List<Map<Object, Object>> queryAgentPrizeListNoTotal(Map<String, Object> map) {
        return insbAgentPrizeDao.queryAgentPrizeList(map);
    }

    @Override
    public void deletePrizeByTaskId(Map<String, Object> map) {
        insbAgentPrizeDao.deletePrizeByAgentIdAndTaskId(map);
    }

    @Override
    public String queryAvailablePrizes(Map<String, Object> map) {
        //可用奖品
        //需要传入参数：agentid/taskid/providercode
        //获取已有的奖品//任务99关联的mini营销活动代码发布cm后台
        StringBuffer message = new StringBuffer();
        message.append("queryAvailablePrizes = ");
        ArrayList statusin = new ArrayList();
        statusin.add("0");
        statusin.add("3");
        map.put("statusin",statusin);
        map.put("effectivetime",new Date());
        List<Map<Object, Object>> agentPrizeList = insbAgentPrizeDao.queryAgentPrizeList(map);
        ArrayList prizeIdList = new ArrayList();
        //String prizeIdList = "";
        //规则接口处理
        AgentTaskModel agentTaskModel = new AgentTaskModel();
        agentTaskModel.setAgentid((String)map.get("agentid"));
        agentTaskModel.setTaskid((String)map.get("taskid"));
        agentTaskModel.setProvidercode((String)map.get("providercode"));
        message.append("[taskid=").append(agentTaskModel.getTaskid()).append(",");
        message.append("providercode=").append(agentTaskModel.getProvidercode()).append(",");
        message.append("agentid=").append(agentTaskModel.getAgentid()).append("] ");
        //封装task中投保供应商投保金额参数，用于活动规则比对
        Map<String, Object> params = queryTaskParams(agentTaskModel);
        //获取符合使用规则的奖品列表
        for (Map<Object, Object> agentPrize : agentPrizeList) {
            if (insbConditionsService.execute("03", (String)agentPrize.get("prizeid"), params))
                prizeIdList.add((String)agentPrize.get("prizeid"));
               // prizeIdList += (prizeIdList.equals("") ? "" : ",") + (String)agentPrize.get("prizeid");
        }
        //
        //没有可用奖品，直接返回，不查数据库//<!--add-->
        if(null==prizeIdList||prizeIdList.size()==0){
            Map<String, Object> resultMap = new HashMap<String, Object>();
            resultMap.put("success", true);
            resultMap.put("total", 0);
            resultMap.put("code", "0");
            resultMap.put("counts", 0);
            resultMap.put("rows", new ArrayList());
            message.append("no prize useable");
            return JSONObject.fromObject(resultMap).toString();
        }
        Map<String, Object> queryMap = new HashMap<String, Object>();

        queryMap.put("prizeidlist", prizeIdList);
        queryMap.put("agentid",map.get("agentid"));
        //queryMap.put("status","0");
        queryMap.put("statusin",statusin);
        //queryMap.put("taskid",map.get("taskid"));
        //queryMap.put("providercode",map.get("providercode"));
        queryMap.put("effectivetime",new Date());
        queryMap.put("sortbyterminal","true");
        long total = insbAgentPrizeDao.queryAgentPrizeListCount(queryMap);
        agentPrizeList = insbAgentPrizeDao.queryAgentPrizeList(queryMap);

        ArrayList<Map<Object, Object>> resultList = new ArrayList<Map<Object,Object>>();
        String tempTaskid = "";
        String tempProvidercode = "";
        String tempStatus = "";
        String code = "0";  //0 未选择使用奖券 3有选择使用奖券
        double counts = 0d;
        List<INSBAgentPrize> insbAgentPrizeList = new ArrayList<INSBAgentPrize>();
        for (Map<Object, Object> agentPrize : agentPrizeList) {
            tempTaskid = (String)agentPrize.get("taskid");
            tempProvidercode = (String)agentPrize.get("providercode");
            tempStatus = (String)agentPrize.get("agentprizestatus");

            if("3".equals(tempStatus)){
                if(agentTaskModel.getTaskid().equals(tempTaskid)&&agentTaskModel.getProvidercode().equals(tempProvidercode)) {
                    code = "3";
                    counts = ((BigDecimal)agentPrize.get("counts")).doubleValue();
                    resultList.add(agentPrize);
                    message.append(" status = 3 ");
                }else{
                    //将其他交易还未支付的订单奖券设置为未使用0状态
                    queryMap.clear();
                    queryMap.put("taskid",tempTaskid);
                    queryMap.put("providercode",tempProvidercode);
                    insbAgentPrizeList = this.queryNoPaymentOrder(queryMap);
                    //String sign = HttpSender.doPost();
                    //

                    if(insbAgentPrizeList!=null&&insbAgentPrizeList.size()>0){
                        for(INSBAgentPrize prize :insbAgentPrizeList ){
                            prize.setStatus("0");
                            prize.setTaskid("");
                            prize.setProvidercode("");
                            this.updateById(prize);
                        }
                        agentPrize.put("taskid","");
                        agentPrize.put("providercode","");
                        agentPrize.put("status","未使用");
                        agentPrize.put("agentprizestatus","0");
                        resultList.add(agentPrize);
                        message.append(" NoPaymentOrder1 status reset prizeid=").append(agentPrize.get("id"));
                    }else {
                        queryMap.clear();
                        queryMap.put("taskid",tempTaskid);
                        queryMap.put("providercode",tempProvidercode);
                        insbAgentPrizeList = insbAgentPrizeDao.queryPaidOrder(queryMap);
                        if(insbAgentPrizeList!=null&&insbAgentPrizeList.size()>0){
                            //已经在支付状态的订单中使用的奖券不显示,不需要返回
                            //resultList无需add
                        }else {
                           INSBAgentPrize aprize = this.queryById((String)agentPrize.get("id"));
                            aprize.setStatus("0");
                            aprize.setTaskid("");
                            aprize.setProvidercode("");
                            this.updateById(aprize);
                            agentPrize.put("taskid","");
                            agentPrize.put("providercode","");
                            agentPrize.put("status","未使用");
                            agentPrize.put("agentprizestatus","0");
                            resultList.add(agentPrize);
                            message.append(" NoPaymentOrder2 status reset 0 prizeid=").append(agentPrize.get("id"));
                        }
                    }
                }
            }else{
                resultList.add(agentPrize);
                message.append(" normal status  0 prizeid=").append(agentPrize.get("id"));
            }
        }
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("success", true);
        resultMap.put("code", code);
        resultMap.put("counts", counts);
        resultMap.put("total", resultList.size());
        resultMap.put("rows", resultList);
        LogUtil.info(message.toString());
        return JSONObject.fromObject(resultMap).toString();

    }

    private Map<String, Object> queryTaskParams(AgentTaskModel agentTaskModel) {
        return insbConditionsService.queryParams(agentTaskModel);
    }
}