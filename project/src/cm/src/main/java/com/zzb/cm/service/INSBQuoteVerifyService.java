package com.zzb.cm.service;

/**
 * 报价数据校验服务（原初审自动化校验规则）
 * Created by Dai on 2016/4/14.
 */
public interface INSBQuoteVerifyService {

    /**
     * 单商/混保 数据校验
     *
     * 注意（重要）：调用本接口需要以下前提条件：
     * 1、该单启用了平台查询，并回写了上年投保数据
     * 2、该单只投保了商业险，或同时投保了商业险和交强险
     *
     * @param processInstanceId 主流程ID
     * @param insComcode 供应商编码
     * @return 1:校验通过, 0:校验不通过, 2:需要拒绝承保, 3:需要剔除所有商业险, 4:需要剔除交强险以及车船税,
     *         5:校验通过,但指定（自定义车价）小于最低价,需要将车损保额设置为最低价
     */
    public int verifyCommercial(String processInstanceId, String insComcode);

    /**
     * 单交强 数据校验
     *
     * 注意（重要）：调用本接口需要以下前提条件：
     * 1、该单启用了平台查询，并回写了上年投保数据
     * 2、该单只投保了交强险
     *
     * @param processInstanceId 主流程ID
     * @param insComcode 供应商编码
     * @return true:校验通过, false:校验不通过
     */
    public boolean verifyTraffic(String processInstanceId, String insComcode);

    /**
     * 新车 数据校验
     * @param processInstanceId 主流程ID
     * @param insComcode 供应商编码
     * @return true:校验通过, false:校验不通过
     */
    public boolean verifyNewVehicle(String processInstanceId, String insComcode);
}
