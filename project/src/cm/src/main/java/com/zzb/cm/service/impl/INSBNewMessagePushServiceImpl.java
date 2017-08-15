package com.zzb.cm.service.impl;

import com.cninsure.core.tools.util.ValidateUtil;
import com.cninsure.core.utils.DateUtil;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.system.entity.INSCUser;
import com.cninsure.system.service.INSCUserService;
import com.common.ConfigUtil;
import com.common.HttpClientUtil;
import com.common.XMPPUtils;
import com.zzb.chn.util.JsonUtils;
import com.zzb.cm.controller.vo.NotReadMessageVo;
import com.zzb.cm.service.INSBNewMessagePushService;
import com.zzb.model.ChatObject;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by hewc for websocket on 2017/7/17.
 */
@Service
public class INSBNewMessagePushServiceImpl implements INSBNewMessagePushService {

    @Resource
    public INSCUserService inscUserService;

    private static String websocketServerURL = "";

    static {
        // 读取相关的配置
        ResourceBundle resourceBundle = ResourceBundle
                .getBundle("config/config");
        websocketServerURL = resourceBundle.getString("cm.websocket.httpserver");
    }


    @Override
    public void sendMessage(INSCUser fromUser, INSCUser toUser, String msgType) {
        LogUtil.info(msgType + "====1向业管发送消息websocket---fromUser=" + fromUser.getUsercode() + "---touser=" + toUser);
        //String xmppUserName = toUser.getUsercode().toLowerCase() + "@" + ValidateUtil.getConfigValue("fairy.serviceName");
        //LogUtil.info(msgType + "====2向业管发送消息---fromUser=" + "xmppUserName=" + xmppUserName + fromUser.getUsercode() + "---touser=" + toUser);
        Map<String,String> body = new HashMap<>();
        if (msgType.contains("@")) {
            body.put("msgtype",msgType.split("@")[0]);
            body.put("content",msgType.split("@")[1]);
        } else {
            body.put("msgtype",msgType);
            body.put("content","");
        }
        ChatObject chatObject = new ChatObject();
        chatObject.setFrom(fromUser.getUsercode());
        chatObject.setTo(toUser.getUsercode());
        chatObject.setType("chat");
        chatObject.setMsg(body);

        JSONObject jsonMessage = JSONObject.fromObject(chatObject);
        LogUtil.info("====2向业管发送消息websocket---fromUser=" + fromUser.getUsercode() + "---touser=" + toUser + "---message=" + jsonMessage.toString());
        sendMessageToWebsocketServer(chatObject);
    }

    @Override
    public void sendMessage(String fromUserCode, String toUserCode, String msgType) {
        INSCUser fromUser = inscUserService.getByUsercode(fromUserCode);
        INSCUser toUser = inscUserService.getByUsercode(toUserCode);
        sendMessage(fromUser,toUser,msgType);
    }

    @Override
    public void sendMessage(INSCUser fromUser, INSCUser toUser) {
        LogUtil.info("====向业管发送消息websocket---fromUser=" + fromUser.getUsercode() + "---touser=" + toUser);
        Map<String,String> body = new HashMap<>();
        body.put("content","");
        body.put("msgtype","taskmsg");
        ChatObject chatObject = new ChatObject();
        chatObject.setFrom(fromUser.getUsercode());
        chatObject.setTo(toUser.getUsercode());
        chatObject.setType("chat");
        chatObject.setMsg(body);

        JSONObject jsonMessage = JSONObject.fromObject(chatObject);
        LogUtil.info("====向业管发送消息websocket---fromUser=" + fromUser.getUsercode() + "---touser=" + toUser + "---message=" + jsonMessage.toString());
        sendMessageToWebsocketServer(chatObject);
    }

    @Override
    public void sendMessage(String fromUserCode, String toUserCode) {
        INSCUser fromUser = inscUserService.getByUsercode(fromUserCode);
        INSCUser toUser = inscUserService.getByUsercode(toUserCode);
        sendMessage(fromUser,toUser);
    }

    @Override
    public void sendMessageToUser(String toUser, String msgType) {

    }

    private String getNowDate() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    private String sendMessageToWebsocketServer(ChatObject chatObject){
        String res = "";
        try {
            res = HttpClientUtil.doPostJsonString(websocketServerURL + "/sendMessage", JsonUtils.serialize(chatObject));
            LogUtil.info("发送消息到websocket服务器成功："+JsonUtils.serialize(chatObject)+" res="+res);
        }catch (Exception e){
            LogUtil.warn("====向业管发送消息websocket---fromUser=" + chatObject.getFrom() + "---touser=" + chatObject.getTo() + "---消息异常="+e.getMessage());
            e.printStackTrace();
        }
        return  res;
    }

    public static String offline(String userCode){
        String res = "";
        try {
            res = HttpClientUtil.doGet(websocketServerURL + "/offline/"+userCode, new HashMap<>());
            LogUtil.info("发送offline消息到websocket服务器成功：userCode="+userCode+" res="+res);
        }catch (Exception e){
            LogUtil.info("发送offline消息到websocket服务器异常：userCode=" + userCode + " res=" + res);
            e.printStackTrace();
        }
        return  res;
    }
}
