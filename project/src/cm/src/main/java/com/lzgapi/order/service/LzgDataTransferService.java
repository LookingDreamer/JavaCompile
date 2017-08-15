package com.lzgapi.order.service;

import com.zzb.mobile.model.CommonModel;

import java.util.Map;

/**
 * Created by HWC on 2017/5/23.
 */
public interface LzgDataTransferService {
    /**
     * 验证LZG Token，用返回的账号登录掌中保
     * @param params
     * @return
     */
    public CommonModel checkToken(Map<String,Object> params);

    /**
     * 获取报价列表
     * @param taskId
     * @param orderstatus
     * @return
     */
    public String getQuoteInfoList(String taskId,String orderstatus);

    /**
     * 获取当前子任务报价
     * @param taskId
     * @param orderstatus
     * @return
     */
    public String getQuoteInfoList(String taskId, String orderstatus,String subInstanceId);

    /**
     * 查询保单信息
     * @param agentnum
     * @param taskid
     * @return
     */
    public Map<String,Object> getPolicyitemList(String agentnum, String taskid);

    public String pushOrderToLzg(String taskId,String subInstanceId,String prvId);
}
