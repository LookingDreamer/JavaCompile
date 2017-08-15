package com.zzb.cm.service;

import com.zzb.model.WorkFlow4TaskModel;

/**
 * Created by liwucai on 2017/2/28 17:04.
 */
public interface INSBMsgPushService {
    /**
     * 发消息 主出口
     */
    public void sendMsg(WorkFlow4TaskModel dataModel);
    /**
     * 发消息 核保成功
     */
    public void sendInsureSuccessMsg(WorkFlow4TaskModel dataModel);
    /**
     * 发消息 支付成功
     */
    public void sendPaySuccessMsg(WorkFlow4TaskModel dataModel);
    /**
     * 发消息 打单成功
     */
    public void sendPrintTicketSuccessMsg(WorkFlow4TaskModel dataModel);

    /**
     * 发核保退回消息
     */
    public void sendInsureGoBackMsg(String processinstanceid, String maininstanceId, String usercode);
}
