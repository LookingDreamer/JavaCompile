package com.zzb.cm.service;

import com.cninsure.system.entity.INSCUser;

/**
 * Created by hewc on 2017/7/17.
 */
public interface INSBNewMessagePushService {

    public void sendMessage(INSCUser fromUser, INSCUser toUser,String msgType);

    public void sendMessage(String fromUser, String toUser,String msgType);

    public void sendMessage(INSCUser fromUser, INSCUser toUser);

    public void sendMessage(String fromUser, String toUser);

    public void sendMessageToUser(String toUser, String msgType);
}
