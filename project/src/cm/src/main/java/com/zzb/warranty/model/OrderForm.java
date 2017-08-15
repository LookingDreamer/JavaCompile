package com.zzb.warranty.model;

import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by Administrator on 2017/1/11.
 */
public class OrderForm {
    @NotNull(message = "需要提供报价请求返回的id作为quoteinfoid的值")
    private String quoteinfoid;

    @NotNull(message = "需要车主信息")
    private INSEPerson carownerinfo;

    @NotNull(message = "需要被保人信息")
    private INSEPerson insured;

    @NotNull(message = "需要投保人信息")
    private INSEPerson applicant;

    @NotNull(message = "需要权益索取人信息")
    private INSEPerson legalrightclaim;

    private INSECar carinfo;

    private boolean needinvoice;

    @Email(message = "邮箱格式不正确")
    private String invoiceemail;

    private List<String> coupons;

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

    public INSECar getCarinfo() {
        return carinfo;
    }

    public void setCarinfo(INSECar carinfo) {
        this.carinfo = carinfo;
    }

    private String deliveryAddress;

    public String getQuoteinfoid() {
        return quoteinfoid;
    }

    public void setQuoteinfoid(String quoteinfoid) {
        this.quoteinfoid = quoteinfoid;
    }

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

    public List<String> getCoupons() {
        return coupons;
    }

    public void setCoupons(List<String> coupons) {
        this.coupons = coupons;
    }

    @Override
    public String toString() {
        return "OrderForm{" +
                "quoteinfoid='" + quoteinfoid + '\'' +
                ", carownerinfo=" + carownerinfo +
                ", insured=" + insured +
                ", applicant=" + applicant +
                ", legalrightclaim=" + legalrightclaim +
                ", carinfo=" + carinfo +
                ", needinvoice=" + needinvoice +
                ", invoiceemail='" + invoiceemail + '\'' +
                ", coupons=" + coupons +
                ", deliveryAddress='" + deliveryAddress + '\'' +
                '}';
    }
}
