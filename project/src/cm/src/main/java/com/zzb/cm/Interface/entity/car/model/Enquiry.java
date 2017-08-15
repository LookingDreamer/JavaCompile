package com.zzb.cm.Interface.entity.car.model; /**
 * 
 */

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 单方对象，给EDI的对象
 * @author austinChen
 * created at 2015年6月12日 下午1:51:53
 */
public class Enquiry implements Serializable,ToMapInterface{

	/**
	 * 序列化ID
	 */
	private static final long serialVersionUID = -8951813245158515440L;
    /*业务号/多方ID*/
    private String orderId;
    /*单方ID*/
    private String sqId;
    /*来源ID*/
    private String sourceId;
    /*投保区域*/
    private String insureArea;
    /*车辆信息*/
    private CarInfo carInfo;
    /*车主信息*/
    private CarOwnerInfo carOwnerInfo;
    /*用户信息*/
    private SubAgentInfo subAgentInfo;
    /*代理人信息*/
    private AgentInfo agentInfo;
    /*投保基本信息*/
    private BaseSuiteInfo suiteInfo;
    /*投保人*/
    private InsurePerson insurePerson;
    /*受益人*/
    private List<BeneficiaryPerson> beneficiaryPersons;
    /*被保人*/
    private List<InsurePerson> insurePersons;
    /*驾驶人信息*/
    private List<DriverPerson> driverPersons;
    /*供应商列表*/
    private ProviderInfo providerInfo;
    /*平台信息*/
    private Map<String,String> platformInfo;
    /*任务流轨迹*/
    private TrackInfo trackInfo;
    /*支付信息*/
    private PayInfo payInfo;
    /*配送信息*/
    private DeliverInfo deliverInfo;
    /*出单信息*/
    private IssueInfo issueInfo;
    /*动态数据杂项*/
    Map<String,String> misc;

	public Map<String, Object> toMap() {
        return null;
	}

    public AgentInfo getAgentInfo() {
        return agentInfo;
    }

    public void setAgentInfo(AgentInfo agentInfo) {
        this.agentInfo = agentInfo;
    }

    public List<BeneficiaryPerson> getBeneficiaryPersons() {
        return beneficiaryPersons;
    }

    public void setBeneficiaryPersons(List<BeneficiaryPerson> beneficiaryPersons) {
        this.beneficiaryPersons = beneficiaryPersons;
    }

    public String getSqId() {
        return sqId;
    }

    public void setSqId(String sqId) {
        this.sqId = sqId;
    }

    public CarInfo getCarInfo() {
        return carInfo;
    }

    public void setCarInfo(CarInfo carInfo) {
        this.carInfo = carInfo;
    }

    public CarOwnerInfo getCarOwnerInfo() {
        return carOwnerInfo;
    }

    public void setCarOwnerInfo(CarOwnerInfo carOwnerInfo) {
        this.carOwnerInfo = carOwnerInfo;
    }

    public DeliverInfo getDeliverInfo() {
        return deliverInfo;
    }

    public void setDeliverInfo(DeliverInfo deliverInfo) {
        this.deliverInfo = deliverInfo;
    }

    public List<DriverPerson> getDriverPersons() {
        return driverPersons;
    }

    public void setDriverPersons(List<DriverPerson> driverPersons) {
        this.driverPersons = driverPersons;
    }

    public InsurePerson getInsurePerson() {
        return insurePerson;
    }

    public void setInsurePerson(InsurePerson insurePerson) {
        this.insurePerson = insurePerson;
    }

    public List<InsurePerson> getInsurePersons() {
        return insurePersons;
    }

    public void setInsurePersons(List<InsurePerson> insurePersons) {
        this.insurePersons = insurePersons;
    }

    public IssueInfo getIssueInfo() {
        return issueInfo;
    }

    public void setIssueInfo(IssueInfo issueInfo) {
        this.issueInfo = issueInfo;
    }

    public Map<String, String> getMisc() {
        return misc;
    }

    public void setMisc(Map<String, String> misc) {
        this.misc = misc;
    }

    public PayInfo getPayInfo() {
        return payInfo;
    }

    public void setPayInfo(PayInfo payInfo) {
        this.payInfo = payInfo;
    }

    public Map<String, String> getPlatformInfo() {
        return platformInfo;
    }

    public void setPlatformInfo(Map<String, String> platformInfo) {
        this.platformInfo = platformInfo;
    }

    public ProviderInfo getProviderInfo() {
        return providerInfo;
    }

    public void setProviderInfo(ProviderInfo providerInfo) {
        this.providerInfo = providerInfo;
    }

    public SubAgentInfo getSubAgentInfo() {
        return subAgentInfo;
    }

    public void setSubAgentInfo(SubAgentInfo subAgentInfo) {
        this.subAgentInfo = subAgentInfo;
    }

    public BaseSuiteInfo getSuiteInfo() {
        return suiteInfo;
    }

    public void setSuiteInfo(BaseSuiteInfo suiteInfo) {
        this.suiteInfo = suiteInfo;
    }

    public TrackInfo getTrackInfo() {
        return trackInfo;
    }

    public void setTrackInfo(TrackInfo trackInfo) {
        this.trackInfo = trackInfo;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getInsureArea() {
        return insureArea;
    }

    public void setInsureArea(String insureArea) {
        this.insureArea = insureArea;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }
}
