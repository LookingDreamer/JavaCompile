package com.zzb.extra.service.impl;

import static org.quartz.SimpleScheduleBuilder.simpleSchedule;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.zzb.extra.entity.*;
import com.zzb.extra.service.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.quartz.DateBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SimpleTrigger;
import org.quartz.TriggerBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.cninsure.core.utils.UUIDUtils;
import com.cninsure.system.entity.INSCCode;
import com.cninsure.system.service.INSCCodeService;
import com.common.HttpClientUtil;
import com.common.ModelUtil;
import com.zzb.cm.dao.INSBCarinfoDao;
import com.zzb.cm.entity.INSBCarinfo;
import com.zzb.cm.entity.INSBOrder;
import com.zzb.cm.service.INSBOrderService;
import com.zzb.conf.entity.INSBAgent;
import com.zzb.conf.entity.INSBOrderpayment;
import com.zzb.conf.entity.INSBPolicyitem;
import com.zzb.conf.entity.INSBProvider;
import com.zzb.conf.service.INSBAgentService;
import com.zzb.conf.service.INSBOrderpaymentService;
import com.zzb.conf.service.INSBPolicyitemService;
import com.zzb.conf.service.INSBProviderService;
import com.zzb.extra.dao.INSBAgentPrizeDao;
import com.zzb.extra.dao.INSBAgentTaskDao;
import com.zzb.extra.dao.INSBMiniPublicDao;
import com.zzb.extra.model.WxMsgTemplateModel;
import com.zzb.extra.task.SendRefundCompleteJob;
import com.zzb.extra.task.SendRefundMsgJob;
import com.zzb.extra.util.ParamUtils;
import com.zzb.extra.util.WxMsgTemplateUtils;
import com.zzb.extra.util.wxmsgtemplate.TemplateData;
import com.zzb.extra.util.wxmsgtemplate.WxTemplate;

/**
 * Created by Heweicheng on 2016/6/30.
 */
@Service
@Transactional
public class WxMsgTemplateServiceImpl implements WxMsgTemplateService {

    @Resource
    private INSCCodeService codeService;
    @Resource
    private INSBAgentService insbAgentService;
    @Resource
    private INSBCarinfoDao insbCarinfoDao;
    @Resource
    private INSBAgentTaskDao insbAgentTaskDao;
    @Resource
    private INSBProviderService insbProviderService;
    @Resource
    private INSBOrderService insbOrderService;
    @Resource
    private INSBPolicyitemService insbPolicyitemService;
    @Resource
    private INSBAccountService insbAccountService;
    @Resource
    public Scheduler sched;
    @Resource
    private INSBMinimsglogService insbMinimsglogService;
    @Resource
    private INSBAgentPrizeDao insbAgentPrizeDao;
    @Resource
    private INSBMiniPublicDao insbMiniPublicDao;
    @Resource
    private INSBOrderpaymentService insbOrderpaymentService;
    @Resource
    private INSBMiniDateService insbMiniDateService;
    @Resource
    private INSBAgentPrizeService insbAgentPrizeService;
    

    @Resource
    private INSBMiniOrderTraceService insbMiniOrderTraceService;

    /**
     * @param parentcode 值为 WeChat
     * @param codetype   值为 AccessTokenURL
     * @param codevalue  值为 01  去哪保
     */
    @Override
    public String getAccessToken(String parentcode, String codetype, String codevalue) {
        String access_token = "";
        INSCCode inscCode = new INSCCode();
        inscCode.setParentcode(parentcode);
        inscCode.setCodetype(codetype);
        inscCode.setCodevalue(codevalue);
        inscCode = codeService.queryOne(inscCode);
        if (null != inscCode) {
            String url = inscCode.getProp2() +"/"+ inscCode.getProp3();
            String jsonString = HttpClientUtil.doGet(url, new HashMap<>());
            HashMap token = JSONObject.parseObject(jsonString, HashMap.class);
            access_token = (String) token.get("access_token");
        }
        return access_token;
    }
    /**
     * @param parentcode 值为 WeChat
     * @param codetype 值为 minizzburl
     * @param codevalue 值为 01
     * @param openid 用户的微信ID
     * @param type order/mycenter/myWealth
     */
    @Override
    public String getBaseUrl(String parentcode, String codetype, String codevalue,String openid,String type) {
        StringBuffer url = new StringBuffer();
        INSCCode inscCode = new INSCCode();
        inscCode.setParentcode(parentcode);
        inscCode.setCodetype(codetype);
        inscCode.setCodevalue(codevalue);
        inscCode = codeService.queryOne(inscCode);
        if (null != inscCode) {
            url.append(inscCode.getProp2());
        }else {
            return ParamUtils.resultMap(false, "获取mini前端URL失败！");
        }
        switch (type){
            //我的订单URL
            case "order":url.append("/").append(openid).append("/").append("order");
                break;
            //个人中心URL
            case "mycenter":url.append("/").append(openid).append("/").append("mycenter");
                break;
            //我的钱包URL
            case "myWealth":url.append("/").append(openid).append("/").append("myWealth");
                break;
            //我的奖券URL
            case "myRedPacket":url.append("/").append(openid).append("/").append("myRedPacket");
                break;
            //我的订单详情URL
            case "orderDetail":url.append("/").append(openid).append("/").append("orderDetail-");
                break;
            default:
                return ParamUtils.resultMap(false, "type参数错误！");
        }
        return url.toString();
    }
    /**
     * @param parentcode 值为 WeChat
     * @param codetype 值为 minizzburl
     * @param codevalue 值为 01
     * 只获取基础URL
     */
    @Override
    public String getBaseUrl(String parentcode, String codetype, String codevalue) {
        String url = "";
        INSCCode inscCode = new INSCCode();
        inscCode.setParentcode(parentcode);
        inscCode.setCodetype(codetype);
        inscCode.setCodevalue(codevalue);
        inscCode = codeService.queryOne(inscCode);
        if (null != inscCode) {
            url = inscCode.getProp2();
        }else {
            return ParamUtils.resultMap(false, "获取URL失败！");
        }
        return url;
    }

    @Override
    public String sendMsg(WxTemplate wxTemplate, String access_token) {
        return WxMsgTemplateUtils.sendMsg(wxTemplate, access_token);
    }

    @Override
    public String sendMsg(String jsonString, String access_token) {
        return WxMsgTemplateUtils.sendMsg(jsonString, access_token);
    }

    /**
     * 去哪保发送消息，从数据库中获取接口URL，获取ACCESS_TOKEN
     */
    @Override
    public String sendMsg(WxTemplate wxTemplate) {
        String access_token = this.getAccessToken("WeChat", "AccessTokenURL", "01");
        StringBuffer logmsg = new StringBuffer();
        if (StringUtil.isEmpty(access_token)) {
            return ParamUtils.resultMap(false, "access_token获取失败！");
        }
        return WxMsgTemplateUtils.sendMsg(wxTemplate, access_token);
    }

    /**
     *@param templatetype 传入数据库中设置的对应类型（01/02/...）
     *
     */
    public String sendMsg(WxTemplate wxTemplate, String access_token,String templatetype){
        try {
            if(StringUtil.isEmpty(wxTemplate.getTemplate_id())){
                if(StringUtil.isEmpty(templatetype)){
                    return ParamUtils.resultMap(false, "参数不正确,templatetype不能为空!");
                }else{
                    //从数据库获取配置的模板ID
                    String template_id = "";
                    INSCCode inscCode = new INSCCode();
                    inscCode.setParentcode("WeChat");
                    inscCode.setCodetype("templateId");
                    inscCode.setCodevalue(templatetype);//01  02 ..
                    inscCode = codeService.queryOne(inscCode);
                    if (null != inscCode) {
                        wxTemplate.setTemplate_id(inscCode.getProp2());
                    }else{
                        return ParamUtils.resultMap(false, "template_id获取失败！");
                    }
                }
            }
            if (StringUtil.isEmpty(wxTemplate.getTouser())) {
                return ParamUtils.resultMap(false, "参数不正确,touser不能为空!");
            }
            if (StringUtil.isEmpty(access_token)) {
                return this.sendMsg(wxTemplate);
            } else {
                return this.sendMsg(wxTemplate, access_token);
            }
        }catch(Exception ex){
            return ParamUtils.resultMap(false, ex.getMessage());
        }
    }

    /**
     * 去哪保发送消息，从数据库中获取接口URL，获取ACCESS_TOKEN
     */
    @Override
    public String sendMsg(String jsonString) {
        String access_token = this.getAccessToken("WeChat", "AccessTokenURL", "01");
        StringBuffer logmsg = new StringBuffer();
        if (StringUtil.isEmpty(access_token)) {
            return ParamUtils.resultMap(false, "access_token获取失败！");
        }
        return WxMsgTemplateUtils.sendMsg(jsonString, access_token);
    }
    /**
     * 去哪保发送消息，从数据库中获取接口URL，获取ACCESS_TOKEN
     * 设置WxMsgTemplateModel的值的时候可以用::分隔value和color，简化设置
     * 这里模板对应的关键字是first/keyword1/keyword2/.../keyword6/remark时使用，
     * 如有其他关键字请用其他方法装配消息发送
     */
    @Override
    public String sendMsgSimplified(WxMsgTemplateModel wxMsgTemplateModel) {
        /*可以用::分隔value和color  ios不能直接用red/blue，所以全改成#0000ff这种格式
         {
            "touser":"oYT6zv7gPGxmYttq9gF_phOizuyY",
            "templatetype":"01",
            "first":"客户你好，核保成功::#0000ff",
            "keyword1":"机动车商业险，交强险::#0000ff",
            "keyword2":"平安财险::#eeddff",
            "keyword3":"3000元::#0000ff",
            "remark":"感谢您的支持！"
            }
        */
        SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        LogUtil.info("sendMsgSimplified Date= "+df.format(new Date())+" json="+JSONObject.toJSONString(wxMsgTemplateModel));
        String result = "";
        try {
            String toUser = wxMsgTemplateModel.getTouser();
            String msgTemplateType = wxMsgTemplateModel.getTemplatetype();
            if (StringUtil.isEmpty(toUser) || StringUtil.isEmpty(msgTemplateType)) {
                return ParamUtils.resultMap(false, "参数错误，touser和templateType不能为空！");
            }
            String templateId = wxMsgTemplateModel.getTemplate_id();
            if (StringUtil.isEmpty(templateId)) {
                INSCCode inscCode = new INSCCode();
                inscCode.setParentcode("WeChat");
                inscCode.setCodetype("templateId");
                inscCode.setCodevalue(msgTemplateType);//01 02 ..
                inscCode = codeService.queryOne(inscCode);
                if (null != inscCode) {
                    templateId = inscCode.getProp2();
                } else {
                    LogUtil.info(" get template_id error！");
                    return ParamUtils.resultMap(false, "template_id获取失败！");
                }
            }
            String url = wxMsgTemplateModel.getUrl();
            String topcolor = wxMsgTemplateModel.getTopcolor();
            if (StringUtil.isEmpty(topcolor)) {
                topcolor = "#FF0000";
            }
            String fontcolor = wxMsgTemplateModel.getFontcolor();
            String access_token = this.getAccessToken("WeChat", "AccessTokenURL", "01");
            StringBuffer logmsg = new StringBuffer();
            if (StringUtil.isEmpty(access_token)) {
                logmsg.append("sendMsgSimplified ").append(msgTemplateType).append(" get access_token error");
                LogUtil.info(logmsg.toString());
                return ParamUtils.resultMap(false, "access_token获取失败！");
            }

                /*
                   {{first.DATA}}
                    被保险人：{{keyword1.DATA}}
                    车牌号：{{keyword2.DATA}}
                    实际支付保费金额：{{keyword3.DATA}}
                    起保日期：{{keyword4.DATA}}
                    {{remark.DATA}}
                */
            String sfirst = wxMsgTemplateModel.getFirst();
            String skeyword1 = wxMsgTemplateModel.getKeyword1();
            String skeyword2 = wxMsgTemplateModel.getKeyword2();
            String skeyword3 = wxMsgTemplateModel.getKeyword3();
            String skeyword4 = wxMsgTemplateModel.getKeyword4();
            String skeyword5 = wxMsgTemplateModel.getKeyword5();
            String skeyword6 = wxMsgTemplateModel.getKeyword6();
            String sremark = wxMsgTemplateModel.getRemark();
            WxTemplate wxTemplate = new WxTemplate();
            wxTemplate.setTouser(toUser);
            wxTemplate.setUrl(url);
            wxTemplate.setTopcolor(topcolor);
            wxTemplate.setTemplate_id(templateId);
            Map<String, TemplateData> data = new HashMap<String, TemplateData>();
            if (!StringUtil.isEmpty(sfirst)) {
                TemplateData first = new TemplateData();
                String[] valueAndColor = sfirst.split("::");
                if (valueAndColor.length > 1) {
                    first.setValue(valueAndColor[0]);
                    first.setColor(valueAndColor[1]);
                } else {
                    first.setValue(sfirst);
                    first.setColor(fontcolor);
                }
                data.put("first", first);
            }
            if (!StringUtil.isEmpty(skeyword1)) {
                TemplateData keyword1 = new TemplateData();
                String[] valueAndColor = skeyword1.split("::");
                if (valueAndColor.length > 1) {
                    keyword1.setValue(valueAndColor[0]);
                    keyword1.setColor(valueAndColor[1]);
                } else {
                    keyword1.setValue(skeyword1);
                    keyword1.setColor(fontcolor);
                }
                data.put("keyword1", keyword1);
            }
            if (!StringUtil.isEmpty(skeyword2)) {
                TemplateData keyword2 = new TemplateData();
                String[] valueAndColor = skeyword2.split("::");
                if (valueAndColor.length > 1) {
                    keyword2.setValue(valueAndColor[0]);
                    keyword2.setColor(valueAndColor[1]);
                } else {
                    keyword2.setValue(skeyword2);
                    keyword2.setColor(fontcolor);
                }
                data.put("keyword2", keyword2);
            }
            if (!StringUtil.isEmpty(skeyword3)) {
                TemplateData keyword3 = new TemplateData();
                String[] valueAndColor = skeyword3.split("::");
                if (valueAndColor.length > 1) {
                    keyword3.setValue(valueAndColor[0]);
                    keyword3.setColor(valueAndColor[1]);
                } else {
                    keyword3.setValue(skeyword3);
                    keyword3.setColor(fontcolor);
                }
                data.put("keyword3", keyword3);
            }
            if (!StringUtil.isEmpty(skeyword4)) {
                TemplateData keyword4 = new TemplateData();
                String[] valueAndColor = skeyword4.split("::");
                if (valueAndColor.length > 1) {
                    keyword4.setValue(valueAndColor[0]);
                    keyword4.setColor(valueAndColor[1]);
                } else {
                    keyword4.setValue(skeyword4);
                    keyword4.setColor(fontcolor);
                }
                data.put("keyword4", keyword4);
            }
            if (!StringUtil.isEmpty(skeyword5)) {
                TemplateData keyword5 = new TemplateData();
                String[] valueAndColor = skeyword5.split("::");
                if (valueAndColor.length > 1) {
                    keyword5.setValue(valueAndColor[0]);
                    keyword5.setColor(valueAndColor[1]);
                } else {
                    keyword5.setValue(skeyword5);
                    keyword5.setColor(fontcolor);
                }
                data.put("keyword5", keyword5);
            }
            if (!StringUtil.isEmpty(skeyword6)) {
                TemplateData keyword6 = new TemplateData();
                String[] valueAndColor = skeyword6.split("::");
                if (valueAndColor.length > 1) {
                    keyword6.setValue(valueAndColor[0]);
                    keyword6.setColor(valueAndColor[1]);
                } else {
                    keyword6.setValue(skeyword6);
                    keyword6.setColor(fontcolor);
                }
                data.put("keyword6", keyword6);
            }
            if (!StringUtil.isEmpty(sremark)) {
                TemplateData remark = new TemplateData();
                String[] valueAndColor = sremark.split("::");
                if (valueAndColor.length > 1) {
                    remark.setValue(valueAndColor[0]);
                    remark.setColor(valueAndColor[1]);
                } else {
                    remark.setValue(sremark);
                    remark.setColor(fontcolor);
                }
                data.put("remark", remark);
            }
            wxTemplate.setData(data);
            LogUtil.info("sendMsgSimplified sendMsg begindate= "+df.format(new Date()));
            result = this.sendMsg(wxTemplate, access_token);
            LogUtil.info("sendMsgSimplified sendMsg enddate= "+df.format(new Date())+" user="+wxMsgTemplateModel.getTouser()+" result=" + result);

        }catch (Exception e){
            LogUtil.info("sendMsgSimplifiederror user="+wxMsgTemplateModel.getTouser()+" templateid="+wxMsgTemplateModel.getTemplate_id()+" errinfo="+e.getMessage());
        }
        return result;
    }

    /**
     * 给去哪保发送核保成功消息，新框架调用
     */
    @Override
    public String sendUnderwritingSuccessMsg(String taskid,String providercode,String amount) {
            return this.sendUnderwritingMsg(taskid, providercode,amount,"","01");
    }
    /**
     * 给去哪保发送核保失败消息，新框架调用
     */
    @Override
    public String sendUnderwritingFailMsg(String taskid,String providercode,String amount,String reason) {
        return this.sendUnderwritingMsg(taskid, providercode,amount,reason, "03");
    }

    /**
     * 给去哪保发送配送完成消息，新框架调用
     */
    @Override
    public String sendDeliveryMsg(String taskid,String providercode) {
        String result = "";
        try {
            INSBAgentTask agentTask = new INSBAgentTask();
            agentTask.setTaskid(taskid);
            List<INSBAgentTask> agentTaskList = insbAgentTaskDao.selectList(agentTask);
            if(null==agentTaskList){
                LogUtil.info("sendDeliveryMsg agentTaskList null "+taskid);
                return "sendDeliveryMsg agentTaskList null";
            }
            String agentid =agentTaskList.get(0).getAgentid();
            //SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
            INSBAgent insbAgent = new INSBAgent();
            insbAgent.setId(agentid);
            insbAgent = insbAgentService.queryOne(insbAgent);
            if(null==insbAgent){
                LogUtil.info("sendDeliveryMsg insbAgent null "+taskid);
                return "sendDeliveryMsg insbAgent null";
            }
            String toUser = insbAgent.getOpenid();
            INSBCarinfo insbCarinfo = insbCarinfoDao.selectCarinfoByTaskId(taskid);
            if(null==insbCarinfo){
                LogUtil.info("sendDeliveryMsg insbCarinfo null "+taskid);
                return "sendDeliveryMsg insbCarinfo null";
            }
            INSBProvider insbProvider = new INSBProvider();
            insbProvider.setPrvcode(providercode);
            insbProvider = insbProviderService.queryOne(insbProvider);
            if(null==insbProvider){
                LogUtil.info("sendDeliveryMsg insbProvider null "+taskid);
                return "sendDeliveryMsg insbProvider null";
            }
            INSBOrder order = new INSBOrder();
            order.setTaskid(taskid);
            order.setPrvid(providercode);
            order = insbOrderService.queryOne(order);
            if(null==order){
                LogUtil.info("sendDeliveryMsg order null "+taskid);
                return "sendDeliveryMsg order null";
            }
            //设置微信ID
            WxMsgTemplateModel wxMsgTemplateModel = new WxMsgTemplateModel();
            wxMsgTemplateModel.setTemplatetype("06");
            wxMsgTemplateModel.setTouser(toUser);
            wxMsgTemplateModel.setUrl(this.getBaseUrl("WeChat", "minizzburl", "01", insbAgent.getOpenid(), "order"));
            wxMsgTemplateModel.setFirst("尊敬的" + getName(insbAgent) + "，您为【" + insbCarinfo.getCarlicenseno() + "】投保的车险保单已配送完成。");
            wxMsgTemplateModel.setKeyword1(taskid + "::#0000ff");
            wxMsgTemplateModel.setKeyword2("¥"+order.getTotalpaymentamount() + "::#0000ff");
            wxMsgTemplateModel.setRemark("配送内容：机动车商业险保单，交强险保单\r\n感谢您选择去哪保购买车险！");

            result = this.sendMsgSimplified(wxMsgTemplateModel);
            LogUtil.info("sendDeliveryMsg user="+wxMsgTemplateModel.getTouser()+" result=" + result);
        }catch (Exception e){
            LogUtil.info("sendDeliveryMsg taskid="+taskid+" providercode="+providercode+ " errinfo="+e.getMessage());
            //e.printStackTrace();
        }
        return result;
    }
    /**
     * 给去哪保发送核保成功获失败消息，新框架调用 templatetype 03 失败 01 成功
     */
    private String sendUnderwritingMsg(String taskid,String providercode,String amount,String reason,String templatetype) {
        String result = "";
        try {
            INSBAgentTask agentTask = new INSBAgentTask();
            agentTask.setTaskid(taskid);
            List<INSBAgentTask> agentTaskList = insbAgentTaskDao.selectList(agentTask);
            if(null==agentTaskList){
                LogUtil.info("sendUnderwritingMsg agentTaskList null "+taskid);
                return "sendUnderwritingMsg agentTaskList null";
            }
            String agentid =agentTaskList.get(0).getAgentid();
            SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
            INSBAgent insbAgent = new INSBAgent();
            insbAgent.setId(agentid);
            insbAgent = insbAgentService.queryOne(insbAgent);
            if(null==insbAgent){
                LogUtil.info("sendUnderwritingMsg insbAgent null "+taskid);
                return "sendUnderwritingMsg insbAgent null";
            }
            if(!"minizzb".equals(insbAgent.getUsersource())){
                return "not minizzb user";
            }
            String toUser = insbAgent.getOpenid();
            INSBCarinfo insbCarinfo = insbCarinfoDao.selectCarinfoByTaskId(taskid);
            if(null==insbCarinfo){
                LogUtil.info("sendUnderwritingMsg insbCarinfo null "+taskid);
                return "sendUnderwritingMsg insbCarinfo null";
            }
            INSBProvider insbProvider = new INSBProvider();
            insbProvider.setPrvcode(providercode);
            insbProvider = insbProviderService.queryOne(insbProvider);
            if(null==insbProvider){
                LogUtil.info("sendUnderwritingMsg insbProvider null "+taskid);
                return "sendUnderwritingMsg insbProvider null";
            }
            //设置微信ID
            WxMsgTemplateModel wxMsgTemplateModel = new WxMsgTemplateModel();
            wxMsgTemplateModel.setTemplatetype(templatetype);
            wxMsgTemplateModel.setTouser(toUser);
            wxMsgTemplateModel.setUrl(this.getBaseUrl("WeChat", "minizzburl", "01", insbAgent.getOpenid(), "orderDetail")+taskid+"-"+providercode);
            if("01".equals(templatetype)) {
                wxMsgTemplateModel.setFirst("尊敬的" + getName(insbAgent) + "，您为【" + insbCarinfo.getCarlicenseno() + "】投保的车险，已核保通过。");
                wxMsgTemplateModel.setKeyword1("机动车商业险，交强险::#0000ff");
                wxMsgTemplateModel.setKeyword2(insbProvider.getPrvshotname()+"::#0000ff");
                wxMsgTemplateModel.setKeyword3("¥"+amount + "::#0000ff");
                wxMsgTemplateModel.setRemark("请及时支付，逾期订单将自动取消。那就悲催了。");
            }else{
                Map<String,Object> queryMap = new HashMap<String,Object>();
                queryMap.put("taskid",taskid);
                queryMap.put("providercode",providercode);
                Map<String,Object> res = insbMiniPublicDao.queryUserComment(queryMap);
                String reasonContent = "";
                if(res !=null && res.get("commentcontent") != null) {
                    reasonContent = res.get("commentcontent").toString()+",可申请退款。";
                }
                if(StringUtil.isEmpty(reasonContent)){
                    reasonContent = reason;
                }
                wxMsgTemplateModel.setFirst("尊敬的" + getName(insbAgent)  + "，您为【" + insbCarinfo.getCarlicenseno() + "】投保的车险，核保不通过。");
                wxMsgTemplateModel.setKeyword1(insbProvider.getPrvshotname() + "::#0000ff");
                wxMsgTemplateModel.setKeyword2("¥"+amount+"::#0000ff");
                wxMsgTemplateModel.setRemark(reasonContent);
            }
            result = this.sendMsgSimplified(wxMsgTemplateModel);
            LogUtil.info("sendUnderwritingMsg user="+wxMsgTemplateModel.getTouser()+" result=" + result);
        }catch (Exception e){
            LogUtil.info("sendUnderwritingMsg taskid="+taskid+" providercode="+providercode+ " errinfo="+e.getMessage());
            //e.printStackTrace();
        }
        return result;
    }

    /**
     * 交易类到账提醒
     * @param taskid
     * @param providercode
     * @param agentid
     * @param amount
     * @param type
     * @param referrerid
     * @return
     */
    public String sendRewardMsg(String taskid,String providercode,String agentid,String amount,String type,String referrerid,Integer redPacketCount){
        String result = ""; 
	    try {
	        if(amount!=null&&Double.valueOf(amount)>0){
	            WxMsgTemplateModel wxMsgTemplateModel = new WxMsgTemplateModel();
	            SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
	            INSBAgent insbAgent = new INSBAgent();
	            INSBAgent refAgent = new INSBAgent();
	            insbAgent.setId(agentid);
	            insbAgent = insbAgentService.queryOne(insbAgent);
                if(null==insbAgent){
                    LogUtil.info("sendRewardMsg insbAgent null "+taskid);
                    return "sendRewardMsg insbAgent null";
                }
                if(!"minizzb".equals(insbAgent.getUsersource())){
                    return "not minizzb user";
                }
	            String toUser = insbAgent.getOpenid();
	            if(!StringUtil.isEmpty(referrerid)){
	                refAgent = new INSBAgent();
	                refAgent.setId(referrerid);
	                refAgent = insbAgentService.queryOne(refAgent);
                    if(null==refAgent){
                        LogUtil.info("sendRewardMsg refAgent null "+taskid);
                        return "sendRewardMsg refAgent null";
                    }
	                toUser = refAgent.getOpenid();
	            }
	            //设置微信ID
	            wxMsgTemplateModel.setTouser(toUser);
	            //设置模板类型12，到账提醒
	            wxMsgTemplateModel.setTemplatetype("12");
	            wxMsgTemplateModel.setKeyword1(amount + "元::#0000ff");
	            wxMsgTemplateModel.setKeyword2(df.format(new Date()));
	            INSBProvider insbProvider = new INSBProvider();
	            insbProvider.setPrvcode(providercode);
	            insbProvider = insbProviderService.queryOne(insbProvider);
                if(null==insbProvider){
                    LogUtil.info("sendRewardMsg insbProvider null "+taskid);
                    return "sendRewardMsg insbProvider null";
                }
	            INSBOrder order = new INSBOrder();
	            order.setTaskid(taskid);
	            order.setPrvid(providercode);
	            order = insbOrderService.queryOne(order);
                if(null==order){
                    LogUtil.info("sendRewardMsg order null "+taskid);
                    return "sendRewardMsg order null";
                }
	            if("01".equals(type)) {//奖券
	                wxMsgTemplateModel.setFirst("尊敬的" + getName(insbAgent)  + "，您有"+redPacketCount+"张奖券到账。");
	                wxMsgTemplateModel.setUrl(this.getBaseUrl("WeChat", "minizzburl", "01", insbAgent.getOpenid(), "myRedPacket"));
	                wxMsgTemplateModel.setRemark("请前往“个人中心->我的奖券”查看。");
	                wxMsgTemplateModel.setKeyword3("投保"+insbProvider.getPrvshotname()+"获得奖券。");
	            }else if("02".equals(type)){//奖金
	                wxMsgTemplateModel.setFirst("尊敬的" + getName(insbAgent) + "，您有一笔收入到账。");
	                wxMsgTemplateModel.setUrl(this.getBaseUrl("WeChat", "minizzburl", "01", insbAgent.getOpenid(), "myWealth"));
	                wxMsgTemplateModel.setRemark("请前往“我的钱包”查看。");
	                wxMsgTemplateModel.setKeyword3("投保"+insbProvider.getPrvshotname()+"获得奖金。");
	            }else if("03".equals(type)){//酬金
	                wxMsgTemplateModel.setFirst("尊敬的" + getName(insbAgent) + "，您有一笔收入到账。");
	                wxMsgTemplateModel.setUrl(this.getBaseUrl("WeChat", "minizzburl", "01", insbAgent.getOpenid(), "myWealth"));
	                wxMsgTemplateModel.setKeyword3("投保"+insbProvider.getPrvshotname()+"获得酬金。");
	                wxMsgTemplateModel.setRemark("请前往“我的钱包”查看。");
	            }else if("04".equals(type)){//赏金
	                wxMsgTemplateModel.setFirst("尊敬的" + getName(refAgent)  + "，您有一笔收入到账。");
	                wxMsgTemplateModel.setUrl(this.getBaseUrl("WeChat", "minizzburl", "01", refAgent.getOpenid(), "myWealth"));
	                wxMsgTemplateModel.setKeyword3("您朋友"+getRefName(insbAgent) +"投保" + insbProvider.getPrvshotname()+ "成功，获得赏金。");
	                wxMsgTemplateModel.setRemark("请前往“我的钱包”查看。");
	            }else if("05".equals(type)){//已用红包兑现成功
	                wxMsgTemplateModel.setFirst("尊敬的" + getName(insbAgent) + "，您有一笔收入到账。");
	                wxMsgTemplateModel.setKeyword3("投保"+insbProvider.getPrvshotname()+"所用奖券兑现成功。");
	                wxMsgTemplateModel.setRemark("请前往“我的钱包”查看。");
	                wxMsgTemplateModel.setUrl(this.getBaseUrl("WeChat", "minizzburl", "01", insbAgent.getOpenid(), "myWealth"));
	            }
	            result = this.sendMsgSimplified(wxMsgTemplateModel);
	            LogUtil.info("sendRewardMsg user="+wxMsgTemplateModel.getTouser()+" result=" + result);
	          }
	        }catch (Exception e){
	            LogUtil.info("sendRewardMsg error "+taskid+" "+type+e.getMessage());
	        }
       
        return result;
    }

    /**
     * 注册推荐分享奖励到账提醒
     * @param agentid
     * @param amount
     * @param type 01 奖券 02 奖金 04 推荐赏金 05 已用红包兑现成功
     * @param info
     * @param redPacketCount 红包数量
     * @return
     */
    public String sendRewardMsg(String agentid,String amount,String type,String info,Integer redPacketCount){
        String result = "";
        String msgtype = "";
        try {
            if(amount!=null&&Double.valueOf(amount)>0) {
                if ("02".equals(info)||"注册".equals(info)) {
                    info = "注册";
                    msgtype = "02";
                } else if ("03".equals(info)) {
                    info = "分享";
                } else if ("04".equals(info)) {
                    info = "推荐";
                } else if ("05".equals(info)) {
                    info = "抽奖";
                }
                WxMsgTemplateModel wxMsgTemplateModel = new WxMsgTemplateModel();
                SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
                INSBAgent insbAgent = new INSBAgent();
                insbAgent.setId(agentid);
                insbAgent = insbAgentService.queryOne(insbAgent);
                if(null==insbAgent){
                    LogUtil.info("sendRewardMsg insbAgent null "+agentid);
                    return "sendRewardMsg insbAgent null";
                }
                if(!"minizzb".equals(insbAgent.getUsersource())){
                    return "not minizzb user";
                }
                String toUser = insbAgent.getOpenid();
                //设置微信ID
                wxMsgTemplateModel.setTouser(toUser);
                //设置模板类型12，到账提醒
                wxMsgTemplateModel.setTemplatetype("12");
                wxMsgTemplateModel.setKeyword1(amount + "元::#0000ff");
                wxMsgTemplateModel.setKeyword2(df.format(new Date()));
                if ("01".equals(type)) {//奖券
                    wxMsgTemplateModel.setFirst("尊敬的" + getName(insbAgent) + "，您有" + redPacketCount + "张奖券到账。");
                    wxMsgTemplateModel.setUrl(this.getBaseUrl("WeChat", "minizzburl", "01", insbAgent.getOpenid(), "myRedPacket"));
                    wxMsgTemplateModel.setRemark("请前往“个人中心->我的奖券”查看。");
                    wxMsgTemplateModel.setKeyword3(info + "奖券到账");
                } else if ("02".equals(type)) {//奖金
                    wxMsgTemplateModel.setFirst("尊敬的" + getName(insbAgent) + "，您有一笔收入到账。");
                    wxMsgTemplateModel.setUrl(this.getBaseUrl("WeChat", "minizzburl", "01", insbAgent.getOpenid(), "myWealth"));
                    wxMsgTemplateModel.setKeyword3(info + "奖金到账");
                    wxMsgTemplateModel.setRemark("请前往“我的钱包”查看。");
                } else if ("04".equals(type)) {//推荐赏金
                    wxMsgTemplateModel.setFirst("尊敬的" + getName(insbAgent) + "，您有一笔收入到账。");
                    wxMsgTemplateModel.setUrl(this.getBaseUrl("WeChat", "minizzburl", "01", insbAgent.getOpenid(), "myWealth"));
                    wxMsgTemplateModel.setKeyword3("您推荐朋友注册成功,获得赏金到账。");
                    wxMsgTemplateModel.setRemark("请前往“我的钱包”查看。");
                } else if ("05".equals(type)) {//已用红包兑现成功
                    wxMsgTemplateModel.setFirst("尊敬的" + getName(insbAgent) + "，您有一笔收入到账。");
                    wxMsgTemplateModel.setUrl(this.getBaseUrl("WeChat", "minizzburl", "01", insbAgent.getOpenid(), "myWealth"));
                    wxMsgTemplateModel.setKeyword3(info + "奖券自动兑现成功。");
                    wxMsgTemplateModel.setRemark("请前往“我的钱包”查看。");
                }
                result = this.sendMsgSimplified(wxMsgTemplateModel);
                LogUtil.info("sendRewardMsg user=" + wxMsgTemplateModel.getTouser()+" type="+type + " result=" + result);

                //记录注册发送奖券消息日志
                if ("02".equals(msgtype)) {
                    try {
                        String errcode = "";
                        String errmsg = "";
                        String msgid = "";
                        JSONObject jsonObject = JSON.parseObject(result);
                        if (jsonObject.containsKey("errcode")) {
                            errcode = jsonObject.get("errcode").toString();
                        }
                        if (jsonObject.containsKey("errmsg")) {
                            errmsg = jsonObject.get("errmsg").toString();
                        }
                        if (jsonObject.containsKey("msgid")) {
                            msgid = jsonObject.get("msgid").toString();
                        }
                        INSBMinimsglog insbMinimsglog = new INSBMinimsglog();
                        insbMinimsglog.setId(UUIDUtils.random());
                        insbMinimsglog.setAgentid(agentid);
                        insbMinimsglog.setErrcode(errcode);
                        insbMinimsglog.setErrmsg(errmsg);
                        insbMinimsglog.setMsgid(StringUtil.isEmpty(msgid) ? "00000" : msgid);
                        insbMinimsglog.setMsgtype(msgtype);
                        insbMinimsglog.setStatus("ok".equals(errmsg) ? "1" : "0");//1发送成功 ，0 发送失败
                        insbMinimsglog.setCreatetime(new Date());
                        insbMinimsglog.setOperator("admin");
                        insbMinimsglogService.insert(insbMinimsglog);
                        LogUtil.info(JSONObject.toJSONString(insbMinimsglog));
                    }catch (Exception e){
                        LogUtil.info("save insbMinimsglog error "+e.getMessage());
                    }
                }
                //

            }
        }catch (Exception e){
            LogUtil.info("sendRewardMsg error agentid="+agentid+" "+type+e.getMessage());
        }
        return result;
    }

    public String resendRegisterMsg(String agentid){
        String result = "";
        try {
            HashMap<String, Object> queryMap = new HashMap<String, Object>();
            queryMap.put("agentid", agentid);
            List<Map<String, Object>> prizeList = insbAgentPrizeDao.queryRegisterPrize(queryMap);
            if (prizeList == null || prizeList.size() == 0) {
                return "No prize found !!";
            }
            queryMap.put("msgtype", "02");
            INSBMinimsglog insbMinimsglog = new INSBMinimsglog();
            insbMinimsglog.setAgentid(agentid);
            insbMinimsglog.setMsgtype("02");
            insbMinimsglog.setStatus("0");
            insbMinimsglog = insbMinimsglogService.queryOne(insbMinimsglog);
            if (insbMinimsglog == null) {
                return "No need resend msessage !!";
            } else {
                Double counts = 0d;
                for (Map<String, Object> prize : prizeList) {
                    counts += Double.parseDouble(prize.get("counts").toString());
                }

                WxMsgTemplateModel wxMsgTemplateModel = new WxMsgTemplateModel();
                SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
                INSBAgent insbAgent = new INSBAgent();
                insbAgent.setId(agentid);
                insbAgent = insbAgentService.queryOne(insbAgent);
                if (null == insbAgent) {
                    LogUtil.info("resendRegisterMsg insbAgent null " + agentid);
                    return "resendRegisterMsg insbAgent null";
                }
                String toUser = insbAgent.getOpenid();
                //设置微信ID
                wxMsgTemplateModel.setTouser(toUser);
                //设置模板类型12，到账提醒
                wxMsgTemplateModel.setTemplatetype("12");
                wxMsgTemplateModel.setKeyword1(counts + "元::#0000ff");
                wxMsgTemplateModel.setKeyword2(df.format(new Date()));
                wxMsgTemplateModel.setFirst("尊敬的" + getName(insbAgent) + "，您有" + prizeList.size() + "张奖券到账。");
                wxMsgTemplateModel.setUrl(this.getBaseUrl("WeChat", "minizzburl", "01", insbAgent.getOpenid(), "myRedPacket"));
                wxMsgTemplateModel.setRemark("请前往“个人中心->我的奖券”查看。");
                wxMsgTemplateModel.setKeyword3("注册奖券到账");

                result = this.sendMsgSimplified(wxMsgTemplateModel);

                JSONObject jsonObject = JSON.parseObject(result);
                String errmsg = "";
                if (jsonObject.containsKey("errmsg")) {
                    errmsg = jsonObject.get("errmsg").toString();
                }
                if ("ok".equals(errmsg)) {
                    insbMinimsglog.setStatus("1");
                    insbMinimsglog.setModifytime(new Date());
                    insbMinimsglogService.updateById(insbMinimsglog);
                }
            }
        }catch (Exception e){
            LogUtil.info("resendRegisterMsg error agentid="+agentid+" "+e.getMessage());
        }
        return result;
    }

    /**
     * 支付完成发送消息
     * @param taskid
     * @param providercode
     * @param agentid
     * @param payType 01 正常支付 ，other 保费差额
     * @return
     */
    public String sendPaidMsg(String taskid,String providercode,String agentid ,String amount,String payType){
        String result = "";
        try {
            INSBAgent insbAgent = new INSBAgent();
            insbAgent.setId(agentid);
            insbAgent = insbAgentService.queryOne(insbAgent);
            if(null==insbAgent){
                LogUtil.info("sendPaidMsg insbAgent null "+taskid);
                return "sendPaidMsg insbAgent null";
            }
            if(!"minizzb".equals(insbAgent.getUsersource())){
                return "not minizzb user";
            }
            INSBCarinfo insbCarinfo = insbCarinfoDao.selectCarinfoByTaskId(taskid);
            //车牌号
            String carlicenseno = "新车未上牌";
            if(insbCarinfo!=null){
                carlicenseno = insbCarinfo.getCarlicenseno();
            }else{
                LogUtil.info("sendPaidMsg insbCarinfo null "+taskid);
                return "sendPaidMsg insbCarinfo null";
            }
            WxTemplate wxTemplate = new WxTemplate();
            wxTemplate.setUrl(this.getBaseUrl("WeChat", "minizzburl", "01", insbAgent.getOpenid(), "orderDetail")+taskid+"-"+providercode);
            wxTemplate.setTouser(insbAgent.getOpenid());
            Map<String, TemplateData> data = new HashMap<String, TemplateData>();
            //头部信息
            TemplateData first = new TemplateData();
            if("01".equals(payType)) {
                first.setValue("尊敬的" + getName(insbAgent) + "，您为【" + carlicenseno + "】投保的车险已支付成功。");
            }else {
                first.setValue("尊敬的" + getName(insbAgent) + "，您为【" + carlicenseno + "】补齐的保费差额已支付成功。");
            }
            data.put("first", first);
            //订单号
            TemplateData keyword1 = new TemplateData();
            keyword1.setValue(taskid);
            data.put("keyword1", keyword1);
            //产品名
            TemplateData keyword2 = new TemplateData();
            if("01".equals(payType)) {
                keyword2.setValue("机动车商业险，交强险");
            }else{
                keyword2.setValue("保费差额");
            }
            data.put("keyword2", keyword2);
            //实付金额
            TemplateData keyword3 = new TemplateData();
            keyword3.setValue("¥"+amount);
            keyword3.setColor("#0000ff");
            data.put("keyword3", keyword3);
            //备注
            TemplateData remark = new TemplateData();
            if("01".equals(payType)) {
                remark.setValue("请您耐心等待承保结果。承保后将第一时间消息通知您。");
            }else{
                remark.setValue("请您耐心等待承保结果。承保后将第一时间消息通知您。");
            }
            data.put("remark", remark);
            wxTemplate.setData(data);
            //模板类型templatetype 04
            result =  this.sendMsg(wxTemplate, "", "04");
            LogUtil.info("sendPaidMsg  agentid="+agentid+" taskid="+taskid+" result="+result);
        } catch(Exception e){
            LogUtil.info("sendPaidMsg error agentid="+agentid+" taskid="+taskid+e.getMessage());
            //e.printStackTrace();
        }
        return result;
    }

    /**
     * 承保完成发送消息
     * @param taskid
     * @param providercode
     * @param agentid
     * @return
     */
    public String sendInsuredMsg(String taskid,String providercode,String agentid){
        String result = "";
        try {
            INSBAgent insbAgent = new INSBAgent();
            insbAgent.setId(agentid);
            insbAgent = insbAgentService.queryOne(insbAgent);
            if(null==insbAgent){
                LogUtil.info("sendInsuredMsg insbAgent null "+taskid);
                return "sendInsuredMsg insbAgent null";
            }
            if(!"minizzb".equals(insbAgent.getUsersource())){
                return "not minizzb user";
            }
            INSBCarinfo insbCarinfo = insbCarinfoDao.selectCarinfoByTaskId(taskid);
            if(null==insbCarinfo){
                LogUtil.info("sendInsuredMsg insbCarinfo null "+taskid);
                return "sendInsuredMsg insbCarinfo null";
            }
            INSBPolicyitem insbPolicyitem = new INSBPolicyitem();
            insbPolicyitem.setTaskid(taskid);
            insbPolicyitem.setInscomcode(providercode);
            List<INSBPolicyitem> insbPolicyitemList = insbPolicyitemService.queryList(insbPolicyitem);
            if(null==insbPolicyitemList){
                LogUtil.info("sendInsuredMsg insbPolicyitemList null "+taskid);
                return "sendInsuredMsg insbPolicyitemList null";
            }
            //保单号
            StringBuffer policyNo = new StringBuffer();
            SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日");
            //保单起始和结束日期
            String startDate = "";
            String endDate = "";
            String bPolicyNo = "";
            String cPolicyNo = "";
            for (INSBPolicyitem item : insbPolicyitemList) {
                if ("0".equals(item.getRisktype())) {
                    bPolicyNo = item.getPolicyno() + "(商业险) ， ";
                    //policyNo.append(item.getPolicyno()).append("(商业险) ， ");
                } else {
                    cPolicyNo = item.getPolicyno() +"(交强险) ";
                    //policyNo.append(item.getPolicyno()).append("(交强险) ");
                }
                startDate = df.format(item.getStartdate());
                endDate = df.format(item.getEnddate());
            }
            policyNo.append(bPolicyNo).append(cPolicyNo);
            INSBOrder order = new INSBOrder();
            order.setTaskid(taskid);
            order.setPrvid(providercode);
            order = insbOrderService.queryOne(order);
            if(null==order){
                LogUtil.info("sendInsuredMsg order null "+taskid);
                return "sendInsuredMsg order null";
            }
            //车牌号
            String carlicenseno = insbCarinfo.getCarlicenseno();
            WxTemplate wxTemplate = new WxTemplate();
            wxTemplate.setUrl(this.getBaseUrl("WeChat", "minizzburl", "01", insbAgent.getOpenid(), "orderDetail")+taskid+"-"+providercode);
            wxTemplate.setTouser(insbAgent.getOpenid());
            Map<String, TemplateData> data = new HashMap<String, TemplateData>();
            //头部信息
            TemplateData first = new TemplateData();
            first.setValue("尊敬的" + getName(insbAgent) + "，您好。您为【" + carlicenseno + "】购买的车险已承保。");
            data.put("first", first);
            //保单号
            TemplateData keyword1 = new TemplateData();
            keyword1.setValue(policyNo.toString());
            data.put("keyword1", keyword1);
            //产品名
            TemplateData keyword2 = new TemplateData();
            keyword2.setValue("机动车商业险，交强险");
            data.put("keyword2", keyword2);
            //被保人
            TemplateData keyword3 = new TemplateData();
            keyword3.setValue(insbCarinfo.getOwnername());
            data.put("keyword3", keyword3);
            //保费金额
            TemplateData keyword4 = new TemplateData();
            keyword4.setValue("¥"+order.getTotalpaymentamount().toString());
            keyword4.setColor("#0000ff");
            data.put("keyword4", keyword4);
            //备注
            TemplateData remark = new TemplateData();
            remark.setValue(startDate +"0时"+ "\r\n承保后，请等待保单配送");
            data.put("remark", remark);
            wxTemplate.setData(data);
            //模板类型templatetype 05
            result = this.sendMsg(wxTemplate, "", "05");
            LogUtil.info("sendInsuredMsg agentid="+agentid+" taskid="+taskid+" result="+result);
        }catch (Exception e){
            LogUtil.info("sendInsuredMsg error agentid="+agentid+" taskid="+taskid+e.getMessage());
            //e.printStackTrace();
        }
        return result;
    }

    /**
     * 提现成功和失败提醒
     * @param accountid
     * @param amount
     * @param templatetype
     * @return
     */
    public String sendCashMsg(String accountid,String amount,String templatetype){
        String result = "";
        try {
            INSBAccount insbAccount = new INSBAccount();
            insbAccount = insbAccountService.queryById(accountid);
            if(null==insbAccount){
                LogUtil.info("sendCashMsg order null "+accountid);
                return "sendCashMsg order null";
            }
            INSBAgent insbAgent = new INSBAgent();
            insbAgent.setId(insbAccount.getAgentid());
            insbAgent = insbAgentService.queryOne(insbAgent);
            if(null==insbAgent){
                LogUtil.info("sendCashMsg insbAgent null "+accountid);
                return "sendCashMsg insbAgent null";
            }
            SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
            WxTemplate wxTemplate = new WxTemplate();
            wxTemplate.setTouser(insbAgent.getOpenid());
            wxTemplate.setUrl(this.getBaseUrl("WeChat", "minizzburl", "01", insbAgent.getOpenid(), "myWealth"));
            Map<String, TemplateData> data = new HashMap<String, TemplateData>();

            TemplateData first = new TemplateData();
            if ("13".equals(templatetype)) {
                first.setValue("尊敬的" + getName(insbAgent) + "，您有一笔提现申请已成功处理。");
            } else {
                first.setValue("尊敬的" + getName(insbAgent)+ "，很遗憾您的提现失败了。资金已退回到“我的钱包”余额。");
            }
            data.put("first", first);

            TemplateData money = new TemplateData();
            money.setValue("¥"+amount);
            money.setColor("#0000ff");
            data.put("money", money);

            TemplateData timet = new TemplateData();
            timet.setValue(df.format(new Date()));
            if ("13".equals(templatetype)) {
                data.put("timet", timet);
            } else {
                data.put("time", timet);
            }

            TemplateData remark = new TemplateData();
            if ("13".equals(templatetype)) {
                remark.setValue("提现金额已通过银行转账到您设置的银行账户中，请尽早查看。");
            } else {
                remark.setValue("可前往“我的钱包”重新提现。");
            }
            data.put("remark", remark);

            wxTemplate.setData(data);
            result =  this.sendMsg(wxTemplate, "", templatetype);
            LogUtil.info("sendCashMsg accountid="+accountid+" templatetype="+templatetype+" result="+result);
        }catch (Exception e){
            LogUtil.info("sendCashMsg error accountid="+accountid+" templatetype="+templatetype+e.getMessage());
            //e.printStackTrace();
        }
        LogUtil.info(result);
        return result;
    }

    /**
     * 发送申请退款消息，及退款消息
     * @param taskid
     * @param providercode
     * @param agentid
     * @param amount
     * @return
     */
    public String sendApplyRefundMsg(String taskid,String providercode,String agentid,String amount){
        //1.发送退款申请消息
        String result = "";
         try {
             INSBAgent insbAgent = new INSBAgent();
             insbAgent.setId(agentid);
             insbAgent = insbAgentService.queryOne(insbAgent);
             if(null==insbAgent){
                 LogUtil.info("sendApplyRefundMsg insbAgent null "+taskid);
                 return "sendApplyRefundMsg insbAgent null";
             }
             if(!"minizzb".equals(insbAgent.getUsersource())){
                 return "not minizzb user";
             }

             long count = getOperateStateCount(taskid,providercode);

             WxMsgTemplateModel wxMsgTemplateModel = new WxMsgTemplateModel();
             wxMsgTemplateModel.setTouser(insbAgent.getOpenid());
             wxMsgTemplateModel.setTemplatetype("15");
             wxMsgTemplateModel.setUrl(this.getBaseUrl("WeChat", "minizzburl", "01", insbAgent.getOpenid(), "orderDetail")+taskid+"-"+providercode);
             if(count>0) {
                 //后台申请退款
                this.systemApplyRefundMsg(taskid,providercode,agentid,amount);
             }else{
                wxMsgTemplateModel.setFirst("尊敬的用户，您已申请了全额退款。");
                wxMsgTemplateModel.setKeyword1("¥" + amount + "::#0000ff");
                wxMsgTemplateModel.setKeyword2("商业险，交强险");
                wxMsgTemplateModel.setKeyword3(taskid);
                wxMsgTemplateModel.setKeyword4("订单核保失败，用户主动申请退款。");
                wxMsgTemplateModel.setRemark("退款将于申请后的3个工作日内退还到您的付款账户。请耐心等待。");
                 result = this.sendMsgSimplified(wxMsgTemplateModel);
                 LogUtil.info("sendApplyRefundMsg taskid="+ taskid+" providercode="+providercode+" result="+result);
             }

         }catch (Exception e){
             LogUtil.info("sendApplyRefundMsg taskid="+ taskid+" providercode="+providercode+" errinfo="+e.getMessage());
         }

        //2.更新跟单状态
        insbMiniOrderTraceService.updateOrderTraceState(taskid,providercode,"12");

        //3.将4已支付待发放状态奖券重置成未使用
        this.updateStatusToZero(taskid, providercode);


        //4.启动job发送退款消息
        JobDataMap dataMap = new JobDataMap();
        try {
            String random = RandomStringUtils.randomAlphanumeric(32);
            //设置7天后发送消息
            Date startTime = DateBuilder.futureDate(insbMiniDateService.getWorkday(3), DateBuilder.IntervalUnit.DAY);
            dataMap.put("taskid", taskid);
            dataMap.put("providercode", providercode);
            dataMap.put("agentid", agentid);
            dataMap.put("amount", amount);
            JobDetail job = JobBuilder.newJob(SendRefundMsgJob.class).withIdentity("RefundMsg"+taskid+random).setJobData(dataMap).build();
            SimpleTrigger trigger = (SimpleTrigger) TriggerBuilder.newTrigger().withIdentity("RefundMsg"+taskid+random).startAt(startTime).build();
            sched.scheduleJob(job, trigger);
        }catch (Exception e){
            LogUtil.info("RefundMsg "+ JSONObject.toJSONString(dataMap)+" errinfo="+e.getMessage());
        }
        return result;
    }

    private void updateStatusToZero(String taskid,String providercode){
        try{
            LogUtil.info("updateStatusToZero called:"+taskid+" "+providercode);
            INSBAgentPrize insbAgentPrize = new INSBAgentPrize();
            insbAgentPrize.setTaskid(taskid);
            insbAgentPrize.setProvidercode(providercode);
            insbAgentPrize.setStatus("4");//已支付待发放
            insbAgentPrize = insbAgentPrizeService.queryOne(insbAgentPrize);
            if(null != insbAgentPrize && taskid.equals(insbAgentPrize.getTaskid())){
                insbAgentPrize.setStatus("0");//改成未使用
                insbAgentPrize.setTaskid("");
                insbAgentPrize.setProvidercode("");
                insbAgentPrizeService.updateById(insbAgentPrize);
            }
        }catch (Exception e){
            LogUtil.info("updateStatusToZero:"+e.getMessage());
        }
    }

    private long getOperateStateCount(String taskid,String providercode){
        long count = 0;
        try{
            INSBMiniOrderTrace insbMiniOrderTrace = new INSBMiniOrderTrace();
            insbMiniOrderTrace.setTaskid(taskid);
            insbMiniOrderTrace.setProvidercode(providercode);
            insbMiniOrderTrace.setOperatestate("3");
            count = insbMiniOrderTraceService.queryCount(insbMiniOrderTrace);
        }catch (Exception e){
            count = 0;
            LogUtil.info(taskid+"getOperateStateCoun errmsg="+e.getMessage());
        }
        return count;
    }

    /**
     * 发送退款消息
     * @param agentid
     * @param amount
     * @return
     */
    public String sendRefundMsg(String taskid,String providercode,String agentid,String amount){
        String result = "";
        try {
            INSBAgent insbAgent = new INSBAgent();
            insbAgent.setId(agentid);
            insbAgent = insbAgentService.queryOne(insbAgent);
            if(null==insbAgent){
                LogUtil.info("sendRefundMsg insbAgent null "+taskid);
                return "sendRefundMsg insbAgent null";
            }
            if(!"minizzb".equals(insbAgent.getUsersource())){
                return "not minizzb user";
            }
            INSBProvider insbProvider = new INSBProvider();
            insbProvider.setPrvcode(providercode);
            insbProvider = insbProviderService.queryOne(insbProvider);
            if(null==insbProvider){
                LogUtil.info("sendRefundMsg insbProvider null "+taskid);
                return "sendRefundMsg insbProvider null";
            }

            //获取INSBMiniOrderTrace的 operateState是否有资料为3已申请退款的笔数
            long count = getOperateStateCount(taskid,providercode);


            WxTemplate wxTemplate = new WxTemplate();
            wxTemplate.setTouser(insbAgent.getOpenid());
            //wxTemplate.setUrl(this.getBaseUrl("WeChat", "minizzburl", "01", insbAgent.getOpenid(), "order"));
            wxTemplate.setUrl(this.getBaseUrl("WeChat", "minizzburl", "01", insbAgent.getOpenid(), "orderDetail")+taskid+"-"+providercode);
            Map<String, TemplateData> data = new HashMap<String, TemplateData>();
            //头部信息
            TemplateData first = new TemplateData();
            first.setValue("尊敬的" + getName(insbAgent) + "，您的订单已成功退款。");
            data.put("first", first);
            //退款原因
            TemplateData reasonData = new TemplateData();
            if(count>0){
                reasonData.setValue("订单超时未处理，系统退款");
            }else{
                reasonData.setValue("核保失败/待补齐影像，用户申请全额退款");
            }

            data.put("reason", reasonData);
            //退款金额
            TemplateData refund = new TemplateData();
            refund.setValue("¥"+amount);
            refund.setColor("#0000ff");
            data.put("refund", refund);
            //备注
            TemplateData remark = new TemplateData();
            remark.setValue("保费已原路退还至您的付款账户.请及时查看您的银行账户信息。");
            data.put("remark", remark);
            wxTemplate.setData(data);
            //模板类型templatetype 05
            result = this.sendMsg(wxTemplate, "", "16");
            LogUtil.info("sendRefundMsg "+ taskid+" :providercode="+providercode +" result="+result);

            //更新跟单状态
            insbMiniOrderTraceService.updateOrderTraceState(taskid,providercode,"13");

        }catch (Exception e){
            LogUtil.info("sendRefundMsg "+ taskid+" :providercode="+providercode +" errinfo="+e.getMessage());
        }
        return result;
    }

    /**
     * 发送补齐影像消息
     * @param taskid
     * @param providercode
     * @param agentid
     * @param imagedata 需要补齐哪些影像资料
     * @param paytime 订单支付时间
     * @return
     */
    public String sendUnCompeleteImage(String taskid,String providercode,String agentid,String imagedata,String paytime){
        String result = "";
        try {
            INSBAgent insbAgent = new INSBAgent();
            insbAgent.setId(agentid);
            insbAgent = insbAgentService.queryOne(insbAgent);
            if(null == insbAgent){
                LogUtil.info("sendUnCompeleteImage insbAgent null "+taskid);
                return "sendUnCompeleteImage insbAgent null";
            }
            if(!"minizzb".equals(insbAgent.getUsersource())){
                return "not minizzb user";
            }

            Map<String,Object> queryMap = new HashMap<String,Object>();
            queryMap.put("taskid",taskid);
            queryMap.put("providercode",providercode);
            Map<String,Object> res = insbMiniPublicDao.queryUserComment(queryMap);
            if(null != res && null != res.get("commentcontent")) {
                imagedata = res.get("commentcontent").toString();
            }

            INSBOrderpayment insbOrderpayment = new INSBOrderpayment();
            insbOrderpayment.setTaskid(taskid);
            insbOrderpayment.setPayresult("02");
            insbOrderpayment = insbOrderpaymentService.queryOne(insbOrderpayment);
            Map<String,Object> map = new HashMap<String,Object>();
            if(insbOrderpayment != null){
                paytime = (!"".equals(ModelUtil.conbertToStringsdf(insbOrderpayment.getPaytime())))?ModelUtil.conbertToStringsdf(insbOrderpayment.getPaytime()):ModelUtil.conbertToStringsdf(insbOrderpayment.getCreatetime());
            }else{
                paytime = "";
            }
            WxMsgTemplateModel wxMsgTemplateModel = new WxMsgTemplateModel();
            wxMsgTemplateModel.setTouser(insbAgent.getOpenid());
            wxMsgTemplateModel.setTemplatetype("17");
            //wxMsgTemplateModel.setUrl(this.getBaseUrl("WeChat", "minizzburl", "01", insbAgent.getOpenid(), "order"));
            wxMsgTemplateModel.setUrl(this.getBaseUrl("WeChat", "minizzburl", "01", insbAgent.getOpenid(), "orderDetail")+taskid+"-"+providercode);
            wxMsgTemplateModel.setFirst("尊敬的用户，您的订单正在核保中，还需补齐相关影像资料。");
            wxMsgTemplateModel.setKeyword1(taskid);
            wxMsgTemplateModel.setKeyword2(paytime);
            wxMsgTemplateModel.setKeyword3("待补齐影像");
            wxMsgTemplateModel.setKeyword4(imagedata);
            wxMsgTemplateModel.setRemark("请及时补充上述影像，以免报价失效需要重新报价。");
            result = this.sendMsgSimplified(wxMsgTemplateModel);
            LogUtil.info("sendUnCompeleteImage taskid="+ taskid+" providercode="+providercode+" result="+result);
        }catch (Exception e){
            LogUtil.info("sendUnCompeleteImage taskid="+ taskid+" providercode="+providercode+" errinfo="+e.getMessage());
        }
          return result;
    }

    /**
     * 发送待补齐差额消息
     * @param taskid
     * @param providercode
     * @param agentid
     * @param paid 已经支付的金额
     * @param outstanding 待补齐差额
     * @return
     */
    public String sendUnCompeletePayment(String taskid,String providercode,String agentid,String paid,String outstanding){
        String result = "";
        try {
            INSBAgent insbAgent = new INSBAgent();
            insbAgent.setId(agentid);
            insbAgent = insbAgentService.queryOne(insbAgent);
            if(null == insbAgent){
                LogUtil.info("sendUnCompeleteImage insbAgent null "+taskid);
                return "sendUnCompeleteImage insbAgent null";
            }
            if(!"minizzb".equals(insbAgent.getUsersource())){
                return "not minizzb user";
            }
            INSBPolicyitem insbPolicyitem = new INSBPolicyitem();
            insbPolicyitem.setTaskid(taskid);
            insbPolicyitem.setInscomcode(providercode);
            List<INSBPolicyitem> insbPolicyitemList = insbPolicyitemService.queryList(insbPolicyitem);
            if(null==insbPolicyitemList){
                LogUtil.info("sendUnCompeletePayment insbPolicyitemList null "+taskid);
                return "sendUnCompeletePayment insbPolicyitemList null";
            }
            String insuredName = "";
            for(INSBPolicyitem item : insbPolicyitemList){
                insuredName = item.getInsuredname();
            }
            WxMsgTemplateModel wxMsgTemplateModel = new WxMsgTemplateModel();
            wxMsgTemplateModel.setTouser(insbAgent.getOpenid());
            wxMsgTemplateModel.setTemplatetype("18");
            wxMsgTemplateModel.setUrl(this.getBaseUrl("WeChat", "minizzburl", "01", insbAgent.getOpenid(), "order"));
            wxMsgTemplateModel.setFirst("尊敬的用户，您好。您的订单核保后还需补交保费才能承保。");
            wxMsgTemplateModel.setKeyword1(taskid);
            wxMsgTemplateModel.setKeyword2(insuredName);
            wxMsgTemplateModel.setKeyword3((Double.parseDouble(paid)+Double.parseDouble(outstanding))+"元");
            wxMsgTemplateModel.setKeyword4(paid+"元");
            wxMsgTemplateModel.setKeyword5(outstanding+"元");
            wxMsgTemplateModel.setRemark("请在支付有效期内补齐保费，以免超期需重新报价。");
            result = this.sendMsgSimplified(wxMsgTemplateModel);
            LogUtil.info("sendApplyRefundMsg taskid="+ taskid+" providercode="+providercode+" result="+result);
        }catch (Exception e){
            LogUtil.info("sendApplyRefundMsg taskid="+ taskid+" providercode="+providercode+" errinfo="+e.getMessage());
        }
        return result;
    }

    @Override
    public String refundCompleteTask(String taskid,String providercode,String channelId){
        //2.启动job发送退款消息

        JobDataMap dataMap = new JobDataMap();
        try {
            String random = RandomStringUtils.randomAlphanumeric(32);
            //设置7天后发送消息
            INSCCode inscCode = new INSCCode();
            inscCode.setParentcode("ChannelForMini");
            inscCode.setCodetype("channeljob");
            inscCode.setCodevalue("01");//01  02 ..
            inscCode = codeService.queryOne(inscCode);
            int minute = 0;
            int days = 0;
            Date startTime = null;
            if(null != inscCode){
                if("nqd_minizzb2016".equals(channelId)){
                    minute =  inscCode.getProp1();
                }else{
                    minute = Integer.parseInt(inscCode.getProp2());
                }
                 startTime = DateBuilder.futureDate(minute, DateBuilder.IntervalUnit.MINUTE);
            }else{
                if("nqd_minizzb2016".equals(channelId)){
                	
                    days = insbMiniDateService.getWorkday(3);
                    
                }else{
                    days = insbMiniDateService.getWorkday(3);
                }
                 startTime = DateBuilder.futureDate(days, DateBuilder.IntervalUnit.DAY);
            }
             //startTime = DateBuilder.futureDate(minute, DateBuilder.IntervalUnit.MINUTE);
            dataMap.put("taskid", taskid);
            dataMap.put("providercode", providercode);
            dataMap.put("channelId", channelId);
            LogUtil.info("refundCompleteTask "+ JSONObject.toJSONString(dataMap));
            JobDetail job = JobBuilder.newJob(SendRefundCompleteJob.class).withIdentity("RefundCompleteMsg"+taskid+random).setJobData(dataMap).build();
            SimpleTrigger trigger = (SimpleTrigger) TriggerBuilder.newTrigger().withSchedule(simpleSchedule().withIntervalInMinutes(1).withRepeatCount(10).withMisfireHandlingInstructionFireNow()).withIdentity("RefundCompleteMsg"+taskid+random).withPriority(10).startAt(startTime).build();

            sched.scheduleJob(job, trigger);
            LogUtil.info("refundCompleteTask job schedule "+startTime);
        }catch (Exception e){
            //e.printStackTrace();
            LogUtil.info("refundCompleteTask "+ JSONObject.toJSONString(dataMap)+" errinfo="+e.getMessage());
        }

        return "refundCompleteTask";
    }

    public String systemApplyRefundMsg(String taskid,String providercode,String agentid,String amount){
        //1.发送退款申请消息
        String result = "";
        try {
            INSBAgent insbAgent = new INSBAgent();
            insbAgent.setId(agentid);
            insbAgent = insbAgentService.queryOne(insbAgent);
            if(null==insbAgent){
                LogUtil.info("systemApplyRefundMsg找不到用户数据 "+taskid);
                return "sendApplyRefundMsg insbAgent null";
            }
            if(!"minizzb".equals(insbAgent.getUsersource())){
                return "not minizzb user";
            }
            WxMsgTemplateModel wxMsgTemplateModel = new WxMsgTemplateModel();
            wxMsgTemplateModel.setTouser(insbAgent.getOpenid());
            wxMsgTemplateModel.setTemplatetype("19");
            wxMsgTemplateModel.setUrl(this.getBaseUrl("WeChat", "minizzburl", "01", insbAgent.getOpenid(), "orderDetail")+taskid+"-"+providercode);
            wxMsgTemplateModel.setFirst("尊敬的用户，由于您太久未操作订单已超期，系统为您发起了全额退款");
            wxMsgTemplateModel.setKeyword1(taskid);
            wxMsgTemplateModel.setKeyword2("¥" + amount + "::#0000ff");
            wxMsgTemplateModel.setKeyword3("退回支付卡");
            wxMsgTemplateModel.setKeyword4("3个工作日");
           // wxMsgTemplateModel.setRemark("3个工作日");

            result = this.sendMsgSimplified(wxMsgTemplateModel);
            LogUtil.info("sendApplyRefundMsg taskid="+ taskid+" providercode="+providercode+" result="+result);
        }catch (Exception e){
            LogUtil.info("sendApplyRefundMsg taskid="+ taskid+" providercode="+providercode+" errinfo="+e.getMessage());
        }
        return result;
    }

    private String getName(INSBAgent insbAgent){
        return "用户";
    }

    private String getRefName(INSBAgent insbAgent){
        return (StringUtil.isEmpty(insbAgent.getName())?(StringUtil.isEmpty(insbAgent.getNickname())?"":insbAgent.getNickname()):insbAgent.getName());
    }


}
