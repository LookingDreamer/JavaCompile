package com.zzb.extra.service;

import com.zzb.extra.model.WxMsgTemplateModel;
import com.zzb.extra.util.wxmsgtemplate.WxTemplate;

import java.util.Map;

/**
 * Created by Heweicheng on 2016/6/30.
 */
public interface WxMsgTemplateService {
    public String sendMsg(WxTemplate wxTemplate);
    public String sendMsg(WxTemplate wxTemplate,String access_token);
    public String sendMsg(WxTemplate wxTemplate,String access_token,String templatetype);
    public String sendMsg(String jsonString);
    public String sendMsg(String jsonString,String access_token);
    public String sendMsgSimplified(WxMsgTemplateModel wxMsgTemplateModel);
    public String getAccessToken(String parentcode,String codetype,String codeValue);
    public String getBaseUrl(String parentcode, String codetype, String codevalue);
    public String getBaseUrl(String parentcode, String codetype, String codevalue,String openid,String type);
    //核保成功和失败提醒
    public String sendUnderwritingSuccessMsg(String taskid,String providercode,String amount);
    public String sendUnderwritingFailMsg(String taskid,String providercode,String amount,String reason);
    //配送完成提醒
    public String sendDeliveryMsg(String taskid,String providercode);
    //交易奖励到账提醒
    public String sendRewardMsg(String taskid,String providercode,String agentid,String amount,String type,String referrerid,Integer redPacketCount);
    //注册推荐分享奖励到账提醒
    public String sendRewardMsg(String agentid,String amount,String type,String info,Integer redPacketCount);
    //支付完成
    public String sendPaidMsg(String taskid,String providercode,String agentid ,String amount,String payType);
    //承保完成
    public String sendInsuredMsg(String taskid,String providercode,String agentid);
    //提现成功和失败提醒
    public String sendCashMsg(String accountid,String amount,String templatetype);
    //发送申请退款消息
    public String sendApplyRefundMsg(String taskid,String providercode,String agentid,String amount);
    //发送退款消息
    public String sendRefundMsg(String taskid,String providercode,String agentid,String amount);
    //发送资料补充通知
    public String sendUnCompeleteImage(String taskid,String providercode,String agentid,String imagedata,String paytime);

    public String sendUnCompeletePayment(String taskid,String providercode,String agentid,String paid,String outstanding);

    public String resendRegisterMsg(String agentid);

    public String refundCompleteTask(String taskid,String providercode,String channelId);
    //发送后台申请订单退款通知
    public String systemApplyRefundMsg(String taskid,String providercode,String agentid,String amount);

}
