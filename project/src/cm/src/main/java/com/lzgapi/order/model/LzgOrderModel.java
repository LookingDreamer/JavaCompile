package com.lzgapi.order.model;

import com.lzgapi.order.util.CustomDateSerializer;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by HWC on 2017/5/18.
 */
public class LzgOrderModel {
    /**
     * 订单创建日期
     */
    @JsonSerialize(using = CustomDateSerializer.class)
    private Date createdate;

    /**
     * 被保人姓名
     */
    private String username;

    /**
     * 订单编号或任务号
     */
    private String orderno;
    /**
     * 报价数量
     */
    private int quantity;
    /**
     * 代理人工号
     */
    private String agentNo;
    /**
     * 车牌号
     */
    private String vehicleNo;
    /**
     *订单状态
     * @return
     */
    private String status;
    /**
     * 报价有效期
     */
    @JsonSerialize(using = CustomDateSerializer.class)
    private Date quoteInvalidDate;
    /**
     * 支付有效期
     */
    @JsonSerialize(using = CustomDateSerializer.class)
    private Date payInvalidDate;

    private Map<String,Object> quoteInfoList;

    /**
     * quoteInfoList
     */
    private Object body;
    /**
     * 投保人证件号
     */
    private String idCardNo;
    /**
     * 投保人证件类型
     */
    private String idCardType;

    /**
     * 投保人姓名
     */
    private String applicantName;

    /**
     *
     * 投保人手机号
     */
    private String applicantPhone;

    public Date getCreatedate() {
        return createdate;
    }
    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getOrderno() {
        return orderno;
    }
    public void setOrderno(String orderno) {
        this.orderno = orderno;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public String getAgentNo() {
        return agentNo;
    }
    public void setAgentNo(String agentNo) {
        this.agentNo = agentNo;
    }
    public String getVehicleNo() {
        return vehicleNo;
    }
    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getQuoteInvalidDate() {
        return quoteInvalidDate;
    }

    public void setQuoteInvalidDate(Date quoteInvalidDate) {
        this.quoteInvalidDate = quoteInvalidDate;
    }

    public Date getPayInvalidDate() {
        return payInvalidDate;
    }

    public void setPayInvalidDate(Date payInvalidDate) {
        this.payInvalidDate = payInvalidDate;
    }

    public Map<String, Object> getQuoteInfoList() {
        return quoteInfoList;
    }

    public void setQuoteInfoList(Map<String, Object> quoteInfoList) {
        this.quoteInfoList = quoteInfoList;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    public String getIdCardNo() {
        return idCardNo;
    }

    public void setIdCardNo(String idCardNo) {
        this.idCardNo = idCardNo;
    }

    public String getIdCardType() {
        return idCardType;
    }

    public void setIdCardType(String idCardType) {
        this.idCardType = idCardType;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public void setApplicantName(String applicantName) {
        this.applicantName = applicantName;
    }

    public String getApplicantPhone() {
        return applicantPhone;
    }

    public void setApplicantPhone(String applicantPhone) {
        this.applicantPhone = applicantPhone;
    }
}
