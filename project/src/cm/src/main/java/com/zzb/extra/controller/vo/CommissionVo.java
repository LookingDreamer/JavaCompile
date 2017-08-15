package com.zzb.extra.controller.vo;

import com.cninsure.core.utils.BeanUtils;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by MagicYuan on 2016/5/24.
 */
public class CommissionVo implements Serializable {

    /**
     * 用户id
     */
    private String agentid;

    /**
     * 任务id
     */
    private String taskid;

    /**
     * 供应商code
     */
    private String providercode;

    /**
     * 供应商协议id
     */
    private String agreementid;

    /**
     * 应用渠道
     */
    private String channelsource;

    /**
     * 产品代码

     */
    private String productcode;

    /**
     * 佣金类型
     */
    private String commissiontype;

    /**
     * 佣金启用状态
     */
    private String status;

    /**
     * 备注和commissionrateid关键字查询
     */
    private String keyword;

    /**
     * 渠道id

     */
    private String channelid;

    public String getChannelid() {
        return channelid;
    }

    public void setChannelid(String channelid) {
        this.channelid = channelid;
    }

    public String getAgentid() {
        return agentid;
    }

    public void setAgentid(String agentid) {
        this.agentid = agentid;
    }

    public String getTaskid() {
        return taskid;
    }

    public void setTaskid(String taskid) {
        this.taskid = taskid;
    }

    public String getProvidercode() {
        return providercode;
    }

    public void setProvidercode(String providercode) {
        this.providercode = providercode;
    }

    public String getAgreementid() {
        return agreementid;
    }

    public void setAgreementid(String agreementid) {
        this.agreementid = agreementid;
    }


    public String getChannelsource() {
        return channelsource;
    }

    public void setChannelsource(String channelsource) {
        this.channelsource = channelsource;
    }

    public String getProductcode() {
        return productcode;
    }

    public void setProductcode(String productcode) {
        this.productcode = productcode;
    }


    public String getCommissiontype() {
        return commissiontype;
    }

    public void setCommissiontype(String commissiontype) {
        this.commissiontype = commissiontype;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

}
