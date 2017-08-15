package com.zzb.chn.service;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.zzb.chn.bean.DeliveryBean;
import com.zzb.chn.bean.QuoteBean;
import com.zzb.cm.entity.INSBQuotetotalinfo;
import com.zzb.model.WorkFlow4TaskModel;

public interface CHNChannelService {
    String CHANNEL_MODULE = "cm:zzb:channel:access_token";
    String CHANNEL_INNER_TOKEN = "cm:zzb:channel:inner_token";
    String HEBEI_DEPTCODE = "1213000000";
    String INSURE_SUCCESS_TASKCODE = "20";
    String ACCEPT_SUCCESS_TASKCODE = "23";

    /**
     * 获取token
     *
     * @param channelSecret
     * @return
     */
    public QuoteBean getToken(String channelId, String channelSecret);

    /**
     * 获取供应商接口
     *
     * @param insureAreaCode
     * @return
     */
    public QuoteBean getProviders(String channelId, String insureAreaCode);

    /**
     * 获取车型信息
     *
     * @param paramJson
     * @return
     */
    public QuoteBean queryCarModelInfos(QuoteBean quoteBean) throws Exception ;

    /**
     * 影像识别
     *
     * @param bean
     * @return
     */
    public QuoteBean recognizeImage(HttpServletRequest request, QuoteBean bean);

    /**
     * 影像上传
     *
     * @param bean
     * @return
     */
    public QuoteBean uploadImage(HttpServletRequest request, QuoteBean bean);

    /**
     * 根据任务ID与供应商ID，返回渠道接口要求的保单信息
     *
     * @param taskId
     * @param prvId
     * @return
     */
    public QuoteBean getTaskQueryByTaskIdAndDeptId(String taskId, String prvId, boolean qDetail);

    /**
     * 回调测试方法
     */
    public void callback(WorkFlow4TaskModel dataModel);

    public String queryList(QuoteBean quoteBean, String channelId);

    public QuoteBean query(QuoteBean quoteBean, String channelId);
    
    /**
     * 获取投保地区接口
     */
    public QuoteBean getAgreementAreas(QuoteBean quoteBean);
    
    /**
     * 获取token--渠道内嵌页面
     * @param quoteBeanIn
     * @return
     * @throws Exception
     */
    public QuoteBean getInnerToken(QuoteBean quoteBeanIn) throws Exception;
    
    /**
     * 验证token--渠道内嵌页面
     * @param quoteBeanIn
     * @return
     * @throws Exception
     */
    public QuoteBean verifyInnerToken(QuoteBean quoteBeanIn, HttpServletRequest request) throws Exception;
    
    public Date getPayValidDate(String taskId, String prvId, boolean addOneDay, boolean queryFlag) throws Exception;
    public Date getQuoteValidDate(String taskId, String prvId, String subInstanceId, boolean addOneDay, boolean queryFlag) throws Exception;
    //取得影像信息
    public boolean getImageInfos(QuoteBean quoteBean, String taskCode);
    public boolean getImageInfos(QuoteBean quoteBean, String taskCode, boolean queryAlread);

    /**
     * 获取配送信息
     * @param taskid
     * @param inscomcode
     * @return
     */
    public DeliveryBean getDelivery(String taskid, String inscomcode);

    public String queryPlatInfo(String taskid, String prvId ,String channelId,String flowType);

    public String recordPhaseTime(Map<String,String> map);
    
    public QuoteBean checkIDCardAndGetPin(QuoteBean quoteBeanIn) throws Exception;
    public QuoteBean commitPinCode(QuoteBean quoteBeanIn) throws Exception;
    public QuoteBean getPinCodeBJ(QuoteBean quoteBeanIn) throws Exception;
    public QuoteBean reapplyPin(QuoteBean quoteBeanIn) throws Exception;
    public boolean pinCodeBJCheck(INSBQuotetotalinfo insbQuotetotalinfo, String prvId, Date payValidDate) throws Exception;

    /**
     * 掌中保内嵌页面获取投保地区接口
     */
    public QuoteBean getAgreementAreasForChn(QuoteBean quoteBean);
}
 