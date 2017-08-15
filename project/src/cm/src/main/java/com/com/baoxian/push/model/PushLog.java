package com.com.baoxian.push.model;

import com.alibaba.fastjson.JSON;
import com.cninsure.core.tools.util.ValidateUtil;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 推送日志对象
 * Created by liwucai on 2016/11/28 16:58.
 *
 */
public final class PushLog {
    //多方报价页面
    public static final String quoteUrl = "/moreOffer/";

    //订单详情页面
    public static final String orderDetailUrl ="/insuredInfo/";


    public PushLog(){}

    public PushLog(String carLicenseno, String inscomcode, String insComName, String msgId, String taskId, int type, String channel,
                   String cus_title, String cus_quoteStatue, String cus_type, String cus_action, String actionType,
                   String activity, String cus_path, String insbpersonName, double totalepremium, String openId) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Map body = new HashMap();
        Map<String, String> bodyContent = new HashMap<>();
        bodyContent.put("id", msgId);
        bodyContent.put("title", carLicenseno + " " + insComName + " " + cus_title);
        this.title = carLicenseno + " " + insComName + " " + cus_title;
        bodyContent.put("msg", "车牌：" + carLicenseno + "|被保人：" + insbpersonName +
                "|供应商：" + insComName + "|状态：" + cus_title + "|金额：" + totalepremium);
        bodyContent.put("sendTime", sdf.format(new Date()));
        bodyContent.put("type", cus_type);
        bodyContent.put("action", cus_action);
        bodyContent.put("taskId", taskId);
        bodyContent.put("inscomcode", inscomcode);
        bodyContent.put("quoteStatue", cus_quoteStatue);
        //这个是打开指定页面的参数
        bodyContent.put("path", cus_path);
        if(!StringUtils.isEmpty(actionType)){
            Map action = new HashMap();
            action.put("action_type", actionType);
            action.put("activity", activity);
            body.put("action", action);
        }

        if(type == PushLog.IOSPlat || type == PushLog.AllPlat){
            Map aps = new HashMap();
            aps.put("alert", carLicenseno + " " + insComName + " " + cus_title);
            body.put("aps", aps);
        }
        if(type == PushLog.AndroidPlat || type == PushLog.AllPlat){
            body.put("title", carLicenseno + " " + insComName + " " + cus_title);
            body.put("content", bodyContent.get("msg"));
        }
        body.put("custom_content", bodyContent);

        Map paras = new HashMap();
        if(type == PushLog.IOSPlat){
            if(channel.contains("tencent")){
                paras.put("tencentAccessId_Ios", ValidateUtil.getConfigValue("ios.TencentTestAccessId"));
                paras.put("tencentSecretKey_Ios", ValidateUtil.getConfigValue("ios.TencentTestSecretKey"));

                paras.put("environment", ValidateUtil.getConfigValue("ios.environment"));

            }

            if(channel.contains("baidu")){

            }


        }else if(type == PushLog.AndroidPlat){
            if(channel.contains("tencent")){
                paras.put("tencentAccessId_Android", ValidateUtil.getConfigValue("android.TencentTestAccessId"));
                paras.put("tencentSecretKey_Android", ValidateUtil.getConfigValue("android.TencentTestSecretKey"));
            }

            if(channel.contains("baidu")){

            }

        }else if(type == PushLog.AllPlat){
            if(channel.contains("tencent")){
                paras.put("tencentAccessId_Ios", ValidateUtil.getConfigValue("ios.TencentTestAccessId"));
                paras.put("tencentSecretKey_Ios", ValidateUtil.getConfigValue("ios.TencentTestSecretKey"));
                paras.put("environment", ValidateUtil.getConfigValue("ios.environment"));


                paras.put("tencentAccessId_Android", ValidateUtil.getConfigValue("android.TencentTestAccessId"));
                paras.put("tencentSecretKey_Android", ValidateUtil.getConfigValue("android.TencentTestSecretKey"));
            }
        }

        this.body = JSON.toJSONString(body);
        this.channel = channel;
        this.platFlag = type;
        this.traceId = taskId;

        //微信
        this.wxOpenId = openId;
        Map<String, String> wxParaMap = new HashMap();
        wxParaMap.put("insPerson", insbpersonName);
        wxParaMap.put("platNum", carLicenseno);
        wxParaMap.put("status", cus_title);
        wxParaMap.put("insComName", insComName);
        wxParaMap.put("totalepremium", String.valueOf(totalepremium));
        wxParaMap.put("taskId", taskId);

        paras.put("wxData", JSON.toJSONString(WxTemplateInitUtil.packWxData("insureSuccess", wxParaMap)));

        //获取模板ID
        String templateNamekey = "";
        switch (cus_title){
            case "核保成功":
                templateNamekey = "insureSuccess";
                break;
            case "核保退回":
                templateNamekey = "insureReturn";
                break;
            case "支付成功":
                templateNamekey = "paySuccess";
                break;
            case "承保打单成功":
                templateNamekey = "approvedSuccess";
                break;
            default:
                templateNamekey = "error";
        }

        if(templateNamekey.equals("error")){
            throw new Exception("消息对象[" + taskId + "_" + insComName + "]推送状态错误" + cus_title);
        }


        paras.put("templateId", WxTemplateInitUtil.getTemplateId(templateNamekey));
        this.params = paras;

    }


    public static final String BaiDuChannel="baidu";
    public static final String TencentChannel="tencent";


    public static final int IOSPlat=3;
    public static final int AllPlat=4;
    public static final int AndroidPlat=2;
    public static final int WxPlat=1;
    public static final int H5Plat=0;

    /**
     * 微信推送的openId
     */
    private String wxOpenId;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getPlatFlag() {
        return platFlag;
    }

    public void setPlatFlag(int platFlag) {
        this.platFlag = platFlag;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public boolean isTagFlag() {
        return tagFlag;
    }

    public void setTagFlag(boolean tagFlag) {
        this.tagFlag = tagFlag;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private String title;

    /**
     * 默认推送渠道腾讯
     */
    private String channel="tencent";

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    private String traceId;

    /**
     * 设备token或者标识
     */
    private List<String> deviceTokenList;
    /**
     * 账号或者别名
     */
    private List<String> workerIdList;

    public List<String> getDeviceTokenList() {
        return deviceTokenList;
    }

    public void setDeviceTokenList(List<String> deviceTokenList) {
        this.deviceTokenList = deviceTokenList;
    }

    public List<String> getWorkerIdList() {
        return workerIdList;
    }

    public void setWorkerIdList(List<String> workerIdList) {
        this.workerIdList = workerIdList;
    }

    /**
     * 标签列表
     */
    private List<String> tagLists;
    private boolean tagFlag;
    /**
     * 全量设备消息
     */
    private boolean allDeviceFlag=false;
    /**
     * 透传标志
     */
    private boolean transFlag=true;
    /**
     * 是否生产环境
     */
    private boolean prodFlag=true;
    /**
     * 默认是全平台推送
     */
    private int platFlag=4;
    private String body;
    /*推送参数传递*/
    private Map<String,String> params;
    /**
     * 推送结果提示 比如IOS 成功，Android 失败 微信 成功 ，H5 成功
     */
    private String result;

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getPushDate() {
        return pushDate;
    }

    public void setPushDate(Date pushDate) {
        this.pushDate = pushDate;
    }

    private Date createdDate;

    private Date pushDate;

    /*id*/
    private long id;

    public boolean isTransFlag() {
        return transFlag;
    }

    public void setTransFlag(boolean transFlag) {
        this.transFlag = transFlag;
    }

    public boolean isProdFlag() {
        return prodFlag;
    }

    public void setProdFlag(boolean prodFlag) {
        this.prodFlag = prodFlag;
    }

    public List<String> getTagLists() {
        return tagLists;
    }

    public void setTagLists(List<String> tagLists) {
        this.tagLists = tagLists;
    }

    public boolean isAllDeviceFlag() {
        return allDeviceFlag;
    }

    public void setAllDeviceFlag(boolean allDeviceFlag) {
        this.allDeviceFlag = allDeviceFlag;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public String getWxOpenId() {
        return wxOpenId;
    }

    public void setWxOpenId(String wxOpenId) {
        this.wxOpenId = wxOpenId;
    }
}

