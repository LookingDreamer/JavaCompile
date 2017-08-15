package com.zzb.warranty.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.zzb.cm.entity.INSBCarinfo;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by Administrator on 2017/1/11.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderForm2 {


    @NotNull(message = "需要车主信息")
    private INSEPerson carownerinfo;

    @NotNull(message = "需要被保人信息")
    private INSEPerson insured;

    @NotNull(message = "需要投保人信息")
    private INSEPerson applicant;

    @NotNull(message = "需要权益索取人信息")
    private INSEPerson legalrightclaim;


    private INSBCarinfo carinfo;

    private Double amount;
    private String orderno;

    private boolean needinvoice;
    private String invoiceemail;

    private String orderStatus;

    private String platenumber;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date createtime;

    public String getPlatenumber() {
        return platenumber;
    }

    public void setPlatenumber(String platenumber) {
        this.platenumber = platenumber;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getOrderno() {
        return orderno;
    }

    public void setOrderno(String orderno) {
        this.orderno = orderno;
    }

    public boolean isNeedinvoice() {
        return needinvoice;
    }

    public void setNeedinvoice(boolean needinvoice) {
        this.needinvoice = needinvoice;
    }

    public String getInvoiceemail() {
        return invoiceemail;
    }

    public void setInvoiceemail(String invoiceemail) {
        this.invoiceemail = invoiceemail;
    }

    public INSBCarinfo getCarinfo() {
        return carinfo;
    }

    public void setCarinfo(INSBCarinfo carinfo) {
        this.carinfo = carinfo;
    }

    private String deliveryAddress;

    public INSEPerson getCarownerinfo() {
        return carownerinfo;
    }

    public void setCarownerinfo(INSEPerson carownerinfo) {
        this.carownerinfo = carownerinfo;
    }

    public INSEPerson getInsured() {
        return insured;
    }

    public void setInsured(INSEPerson insured) {
        this.insured = insured;
    }

    public INSEPerson getApplicant() {
        return applicant;
    }

    public void setApplicant(INSEPerson applicant) {
        this.applicant = applicant;
    }

    public INSEPerson getLegalrightclaim() {
        return legalrightclaim;
    }

    public void setLegalrightclaim(INSEPerson legalrightclaim) {
        this.legalrightclaim = legalrightclaim;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }
}
