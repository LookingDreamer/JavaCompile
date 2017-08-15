package com.zzb.cm.service;

/**
 * 自动规则报价数据校验服务
 * Created by Dai on 2016/7/26.
 */
public interface INSBAutomaticVerifyService {

    /**
     * 单交强 数据校验
     *
     * 注意（重要）：调用本接口需要以下前提条件：
     * 1、该单启用了平台查询，并回写了上年投保数据
     * 2、该单只投保了交强险（和车船税）
     *
     * @param processInstanceId 主流程ID
     * @param insComcode 供应商编码
     * @return true:校验通过, false:校验不通过
     */
    public boolean verifyTrafficTax(String processInstanceId, String insComcode);

    /**
     * 新车 数据校验
     * @param processInstanceId 主流程ID
     * @param insComcode 供应商编码
     * @return true:校验通过, false:校验不通过
     */
    public boolean verifyNewVehicle(String processInstanceId, String insComcode);

    /**
     * 单商/混保 数据校验
     *
     * 注意（重要）：调用本接口需要以下前提条件：
     * 1、该单启用了平台查询，并回写了上年投保数据
     * 2、该单只投保了商业险，或同时投保了商业险和交强险
     *
     * @param processInstanceId 主流程ID
     * @param insComcode 供应商编码
     * @return 0:校验不通过, 1:校验通过, 2:需要拒绝承保
     */
    public int verifyCommercial(String processInstanceId, String insComcode);
}
