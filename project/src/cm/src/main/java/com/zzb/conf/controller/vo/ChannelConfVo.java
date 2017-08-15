package com.zzb.conf.controller.vo;

import com.zzb.conf.entity.INSBChannel;

import java.io.Serializable;

/**
 * 渠道配置Vo
 *
 * @author  wu-shangsen
 * @version 1.0, 2016/9/7
 */
public class ChannelConfVo implements Serializable {

    /*渠道ID*/
    private String channelId;
    /*渠道IP*/
    private String channelSecret;
    /*渠道回调地址*/
    private String callbackUrl;

    public ChannelConfVo() {
    }

    public ChannelConfVo(String channelId, String channelSecret, String callbackUrl) {
        this.channelId = channelId;
        this.channelSecret = channelSecret;
        this.callbackUrl = callbackUrl;
    }

    public static ChannelConfVo build(INSBChannel channel) {
        ChannelConfVo channelConf = new ChannelConfVo() ;
        channelConf.setCallbackUrl(channel.getWebaddress());
        channelConf.setChannelId(channel.getId());
        channelConf.setChannelSecret(channel.getChannelsecret());
        return channelConf ;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getChannelSecret() {
        return channelSecret;
    }

    public void setChannelSecret(String channelSecret) {
        this.channelSecret = channelSecret;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }
}
