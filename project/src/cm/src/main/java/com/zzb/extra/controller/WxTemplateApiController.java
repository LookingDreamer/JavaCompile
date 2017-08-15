package com.zzb.extra.controller;

import com.alibaba.fastjson.JSONObject;
import com.cninsure.core.exception.ControllerException;
import com.cninsure.core.utils.StringUtil;
import com.cninsure.system.entity.INSCCode;
import com.cninsure.system.service.INSCCodeService;
import com.common.HttpClientUtil;
import com.common.JsonUtil;
import com.zzb.extra.model.AgentTaskModel;
import com.zzb.extra.model.WxMsgParamModel;
import com.zzb.extra.model.WxMsgTemplateModel;
import com.zzb.extra.service.INSBAccountDetailsService;
import com.zzb.extra.service.WxMsgTemplateService;
import com.zzb.extra.service.impl.INSBAccountDetailsServiceImpl;
import com.zzb.extra.util.ParamUtils;
import com.zzb.extra.util.WxMsgTemplateUtils;
import com.zzb.extra.util.wxmsgtemplate.TemplateData;
import com.zzb.extra.util.wxmsgtemplate.WxTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.xml.bind.SchemaOutputResolver;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Heweicheng on 2016/6/30.
 */
@Controller
@RequestMapping("/msgtemplate/api/*")
public class WxTemplateApiController {
    @Resource
    private WxMsgTemplateService wxMsgTemplateService;

    @Resource
    private INSBAccountDetailsService insbAccountDetailsService;
    @Resource
    private INSCCodeService codeService;
    /**
    *@param templatetype 传入数据库中设置的对应类型（01/02/...）
    *
    */
    @RequestMapping(value = "sendMsg", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
    @ResponseBody
    public String sendMsg(@RequestBody WxTemplate wxTemplate, String access_token,String templatetype) throws ControllerException {
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
                return wxMsgTemplateService.sendMsg(wxTemplate);
            } else {
                return wxMsgTemplateService.sendMsg(wxTemplate, access_token);
            }
        }catch(Exception ex){
            return ParamUtils.resultMap(false, ex.getMessage());
        }
    }

    /**
     * 不能对每个keyword单独设置颜色
     */
    @RequestMapping(value = "sendMsgSimplified", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
    @ResponseBody
    public String sendMsgSimplified(@RequestBody WxMsgTemplateModel wxMsgTemplateModel) throws ControllerException {
        if(StringUtil.isEmpty(wxMsgTemplateModel.getTouser())){
            return ParamUtils.resultMap(false, "参数不正确，touser不能为空！");
        }
        return wxMsgTemplateService.sendMsgSimplified(wxMsgTemplateModel);
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
    @RequestMapping(value = "sendUnCompeletePayment", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
    @ResponseBody
    public String sendUnCompeletePayment(@RequestBody WxMsgParamModel wxMsgParamModel) throws ControllerException {
        if(StringUtil.isEmpty(wxMsgParamModel.getTaskid())||StringUtil.isEmpty(wxMsgParamModel.getProvidercode())||StringUtil.isEmpty(wxMsgParamModel.getAgentid())){
            return ParamUtils.resultMap(false, "参数不正确");
        }
        return wxMsgTemplateService.sendUnCompeletePayment(wxMsgParamModel.getTaskid(),wxMsgParamModel.getProvidercode(),wxMsgParamModel.getAgentid(),wxMsgParamModel.getPaid(),wxMsgParamModel.getOutstanding());
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
    @RequestMapping(value = "sendUnCompeleteImage", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
    @ResponseBody
    public String sendUnCompeleteImage(@RequestBody WxMsgParamModel wxMsgParamModel) throws ControllerException {
        if(StringUtil.isEmpty(wxMsgParamModel.getTaskid())||StringUtil.isEmpty(wxMsgParamModel.getProvidercode())||StringUtil.isEmpty(wxMsgParamModel.getAgentid())){
            return ParamUtils.resultMap(false, "参数不正确");
        }
        return wxMsgTemplateService.sendUnCompeleteImage(wxMsgParamModel.getTaskid(), wxMsgParamModel.getProvidercode(), wxMsgParamModel.getAgentid(), wxMsgParamModel.getImagedata(), wxMsgParamModel.getPaytime());
    }

    /**
     * 发送申请退款消息，及退款消息
     * @param taskid
     * @param providercode
     * @param agentid
     * @param amount
     * @return
     */
    @RequestMapping(value = "sendApplyRefundMsg", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
    @ResponseBody
    public String sendApplyRefundMsg(@RequestBody WxMsgParamModel wxMsgParamModel){
        if(StringUtil.isEmpty(wxMsgParamModel.getTaskid())||StringUtil.isEmpty(wxMsgParamModel.getProvidercode())||StringUtil.isEmpty(wxMsgParamModel.getAgentid())){
            return ParamUtils.resultMap(false, "参数不正确");
        }
        return wxMsgTemplateService.sendApplyRefundMsg(wxMsgParamModel.getTaskid(), wxMsgParamModel.getProvidercode(), wxMsgParamModel.getAgentid(), wxMsgParamModel.getAmount());
    }

    /**
     *
     * @param wxMsgParamModel
     * @param amount 总保费
     * @param reason 核保失败原因
     * @return
     */
    @RequestMapping(value = "sendUnderwritingFailMsg", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
    @ResponseBody
    public String sendUnderwritingFailMsg(@RequestBody WxMsgParamModel wxMsgParamModel){
        if(StringUtil.isEmpty(wxMsgParamModel.getTaskid())||StringUtil.isEmpty(wxMsgParamModel.getProvidercode())){
            return ParamUtils.resultMap(false, "参数不正确");
        }
        return wxMsgTemplateService.sendUnderwritingFailMsg(wxMsgParamModel.getTaskid(), wxMsgParamModel.getProvidercode(), wxMsgParamModel.getAmount(), wxMsgParamModel.getReason());
    }

    /**
     *
     * @param amount 总保费
     * @return
     */
    @RequestMapping(value = "sendPaidOutstandingMsg", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
    @ResponseBody
    public String sendPaidOutstandingMsg(@RequestBody WxMsgParamModel wxMsgParamModel){
        if(StringUtil.isEmpty(wxMsgParamModel.getTaskid())||StringUtil.isEmpty(wxMsgParamModel.getProvidercode())||StringUtil.isEmpty(wxMsgParamModel.getAgentid())){
            return ParamUtils.resultMap(false, "参数不正确");
        }
        return wxMsgTemplateService.sendPaidMsg(wxMsgParamModel.getTaskid(), wxMsgParamModel.getProvidercode(), wxMsgParamModel.getAgentid(), wxMsgParamModel.getAmount(), "02");
    }

    /**
     *
     * @param amount 总保费
     * @return
     */
    @RequestMapping(value = "sendPaidMsg", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
    @ResponseBody
    public String sendPaidMsg(@RequestBody WxMsgParamModel wxMsgParamModel){
        if(StringUtil.isEmpty(wxMsgParamModel.getTaskid())||StringUtil.isEmpty(wxMsgParamModel.getProvidercode())||StringUtil.isEmpty(wxMsgParamModel.getAgentid())){
            return ParamUtils.resultMap(false, "参数不正确");
        }
        return wxMsgTemplateService.sendPaidMsg(wxMsgParamModel.getTaskid(), wxMsgParamModel.getProvidercode(), wxMsgParamModel.getAgentid(), wxMsgParamModel.getAmount(), "01");
    }

    /**
     *
     * @param amount 退款成功启动定时器修改状态
     * @return
     */
    @RequestMapping(value = "refundCompleteTask", method = RequestMethod.POST, produces = "text/json;charset=UTF-8")
    @ResponseBody
    public String refundCompleteTask(@RequestBody WxMsgParamModel wxMsgParamModel){
        if(StringUtil.isEmpty(wxMsgParamModel.getTaskid())||StringUtil.isEmpty(wxMsgParamModel.getProvidercode())||StringUtil.isEmpty(wxMsgParamModel.getChannelId())){
            return ParamUtils.resultMap(false, "参数不正确");
        }
        return wxMsgTemplateService.refundCompleteTask(wxMsgParamModel.getTaskid(), wxMsgParamModel.getProvidercode(), wxMsgParamModel.getChannelId());
    }


}
