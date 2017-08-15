package com.zzb.model;

import java.util.Map;

/**
 * @author hewc
 * @Created by hewc for websocket on 2017/7/19 16:33.
 */
public class ChatObject {
    /**
     * 消息来源者
     */
    private String from;
    /**
     * 消息类型
     */
    private String type;
    /**
     * 消息推送对象
     */
    private String to;
    /**
     * 消息内容实体
     */
    private Map msg;

    public ChatObject() {
    }

    public ChatObject(String from,String to,String type, Map msg) {
        super();
        this.from = from;
        this.to=to;
        this.type=type;
        this.msg = msg;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Map getMsg() {
        return msg;
    }

    public void setMsg(Map msg) {
        this.msg = msg;
    }
}
