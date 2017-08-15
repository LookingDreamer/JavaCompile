package com.com.baoxian.push;

import com.alibaba.fastjson.JSON;
import com.cninsure.core.tools.util.ValidateUtil;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.system.entity.INSCMessage;
import com.com.baoxian.push.model.PushLog;
import com.common.HttpClientUtil;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by liwucai on 2016/12/1 17:45.
 */
public class HttpPush {
    /**
     * @param traceId
     * @param account   帐号
     * @tagFlag 标签发送标记，如果true给标签发送，则不给帐户发送
     * @param tags
     *
     *
     * @param title
     * @param body
     *
     * @param prodFlag 是否生产环境
     * @param platFlag
     */
    public static void push(String traceId, String account, boolean tagFlag, List<String> tags, String title, Map body, boolean prodFlag, int platFlag){
        PushLog log = new PushLog();
//        body=new HashMap<>();
//        body.put("key1","value1");
//        body.put("key2", "value2");
//        body.put("key3", "value3@#%$&$%&$");

        /*tags=new ArrayList<>();

        tags.add("zhaizy");*/
        log.setTagLists(tags);
        log.setTraceId(traceId); /*UUID.randomUUID().toString()*/
        log.setBody(JSON.toJSONString(body));
        log.setTitle(title); //"this is a title"
        if(log.isTagFlag())
        {
            log.setTitle(log.getTitle()+"-tag");
        }
        log.setPlatFlag(platFlag);//PushLog.AndroidPlat
        log.setProdFlag(false);
       // System.out.println("核保 退回 发消息给用户: "+ mediumPaymentVo.getUserid() + " 任务号"+traceId );
        HttpClientUtil.doPostJsonString(ValidateUtil.getConfigValue("pushmsg.url"), JSON.toJSONString(log));

    }

    /**
     * 推送消息
     * @param pushLog
     */
    public static void push(PushLog pushLog){
        LogUtil.info("send pushMsg to push server：the server url is [" + ValidateUtil.getConfigValue("pushmsg.url") + "], " +
                        "the taskid is [" + pushLog.getTraceId() + "], " +
                "the msg content is [" + JSON.toJSONString(pushLog) + "]");

        HttpClientUtil.doPostJsonString(ValidateUtil.getConfigValue("pushmsg.url"), JSON.toJSONString(pushLog));


    }


    /**
     *
     * @param pushLog
     * @param wxOpenId
     * @param platFlag
     * @param channel
     * @param allDeviceFlag
     * @param workerIdList
     * @param deviceTokenList
     * @param transFlag
     * @param traceId
     * @param tagFlag
     * @param tagLists
     * @param params
     * @param body
     */
    public static void commonSetPushLog(PushLog pushLog, String wxOpenId, String platFlag, String channel,
                                        boolean allDeviceFlag, List<String> workerIdList, List<String> deviceTokenList,
                                        boolean transFlag, String traceId, boolean tagFlag, List<String> tagLists,
                                        Map<String, String> params, String body){

    }

    /**
     * 单个账号对象
     * @param pushLog
     * @param workerId
     */
    public static void singleAccountPushLog(PushLog pushLog, String workerId){
        setMustPara(pushLog, false, false, false);

        List<String> workerIdList = new ArrayList<String>();
        workerIdList.add(workerId);
        pushLog.setWorkerIdList(workerIdList);
    }

    /**
     * 标识位  必须设值
     * @param pushLog
     * @param allDeviceFlag
     * @param transFlag
     * @param tagFlag
     */
    private static void setMustPara(PushLog pushLog, boolean allDeviceFlag, boolean transFlag, boolean tagFlag){
        pushLog.setAllDeviceFlag(false);
        pushLog.setTransFlag(false);
        pushLog.setTagFlag(false);

    }

    /**
     * 设置消息表存储对象
     * @param msg
     * @param pushLog
     * @param msgId
     */
    public static void setINSCMessage(INSCMessage msg, PushLog pushLog, String msgId) {
        msg.setId(msgId);
        String msgTitle = (String) ((Map) JSON.parseObject(pushLog.getBody(), Map.class).get("custom_content")).get("title");
        msg.setMsgtitle(msgTitle == null ? pushLog.getTitle() : msgTitle);
        msg.setMsgcontent((String) ((Map) JSON.parseObject(pushLog.getBody(), Map.class).get("custom_content")).get("msg"));
        msg.setLinkurl((String) ((Map) JSON.parseObject(pushLog.getBody(), Map.class).get("custom_content")).get("path"));
        msg.setReceiver(pushLog.getWorkerIdList().get(0));
        msg.setSender("admin");
        msg.setStatus(0);
        //标志位啊
        msg.setState(1);
        msg.setSendtime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
    }
}
