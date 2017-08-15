package com.zzb.cm.Interface.entity.car.model; /**
 *
 */

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * 针对以前的多方对象,仅仅对一个投保的数据进行存储，投保返回的记过放入到单方中
 *
 * @author austinChen
 *         created at 2015年6月12日 下午1:52:10
 */
public class OrderInfo implements Serializable {

    /**
     * 序列化ID
     */
    private static final long serialVersionUID = -3576383470236540152L;

    /*车辆信息*/
    private CarInfo carInfo;
    /*供应商信息*/
    private List<ProviderInfo> providers;
    /*通道信息*/
    private ChannelInfo channel;
    /*备注*/
    private String remark;
    /*订单创建时间*/
    private Date created;
    /*订单更新时间*/
    private Date updated;
    /*自身服务器所在地址，为集群预留的回写地址*/
    private String selfPath;
    /*车主信息*/
    private CarOwnerInfo carOwner;
    /*被保人*/
    private List<PersonInfo> insuredPersonList;
    /*受益人*/
    private List<PersonInfo> beneficiaryPersonList;
    /*险别配置*/
    private List<SuiteDef> suites;
    /*多方报价id*/
    private Integer mid;
    /*配送信息*/
    private DeliverInfo deliverInfo;


    public DeliverInfo getDeliverInfo() {
        return deliverInfo;
    }

    public void setDeliverInfo(DeliverInfo deliverInfo) {
        this.deliverInfo = deliverInfo;
    }

    public List<SuiteDef> getSuites() {
        return suites;
    }
    public void setSuites(List<SuiteDef> suites) {
        this.suites = suites;
    }

    public String getSuitesStream() {
        return JSONObject.toJSONString(suites);
    }
    public void setSuitesStream(String suites) {
        this.suites = JSON.parseArray(suites, SuiteDef.class);
    }

    public Integer getMid() {
        return mid;
    }

    public void setMid(Integer mid) {
        this.mid = mid;
    }

    public CarInfo getCarInfo() {
        return carInfo;
    }

    public void setCarInfo(CarInfo carInfo) {
        this.carInfo = carInfo;
    }

    public List<ProviderInfo> getProviders() {
        return providers;
    }
    public void setProviders(List<ProviderInfo> providers) {
        this.providers = providers;
    }
    public String getProvidersStream() {
        return JSONObject.toJSONString(providers);
    }

    public void setProvidersStream(String providers) {
        this.providers = JSON.parseArray(providers,ProviderInfo.class);
    }

    public ChannelInfo getChannel() {
        return channel;
    }

    public void setChannel(ChannelInfo channel) {
        this.channel = channel;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public String getSelfPath() {
        return selfPath;
    }

    public void setSelfPath(String selfPath) {
        this.selfPath = selfPath;
    }

    public CarOwnerInfo getCarOwner() {
        return carOwner;
    }

    public void setCarOwner(CarOwnerInfo carOwner) {
        this.carOwner = carOwner;
    }


    public List<PersonInfo> getInsuredPersonList() {
        return insuredPersonList;
    }

    public void setInsuredPersonList(List<PersonInfo> insuredPersonList) {
        this.insuredPersonList = insuredPersonList;
    }


    public List<PersonInfo> getBeneficiaryPersonList() {
        return beneficiaryPersonList;
    }

    public void setBeneficiaryPersonList(List<PersonInfo> beneficiaryPersonList) {
        this.beneficiaryPersonList = beneficiaryPersonList;
    }

}
