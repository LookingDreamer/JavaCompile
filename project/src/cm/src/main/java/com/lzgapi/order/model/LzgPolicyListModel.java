package com.lzgapi.order.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.lzgapi.order.util.CustomDateSerializer;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;
import java.util.List;

/**
 * Created by HWC on 2017/5/19.
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class LzgPolicyListModel {
    /**
     * 保单号
     */
    private String contNo;
    /**
     * 保险公司编号
     */
    private String supplierCode;
    /**
     * 被保人姓名
     */
    private String  insuredName;
    /**
     * 被保人证件号
     */
    private String insuredIdno;
    /**
     * 出单日期
     */
    @JsonSerialize(using = CustomDateSerializer.class)
    private Date signDate;
    /**
     * 起始日期
     */
    @JsonSerialize(using = CustomDateSerializer.class)
    private Date cvaliDate;
    /**
     * 终止日期
     */
    @JsonSerialize(using = CustomDateSerializer.class)
    private Date cinvaliDate;
    /**
     * 投保险种
     */
    private List<LzgRiskModel> riskList;

    /**
     *险种类型 0：商业险 1：交强险
     */
    private String riskType;


    public String getContNo() {
        return contNo;
    }

    public void setContNo(String contNo) {
        this.contNo = contNo;
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    public String getInsuredName() {
        return insuredName;
    }

    public void setInsuredName(String insuredName) {
        this.insuredName = insuredName;
    }

    public String getInsuredIdno() {
        return insuredIdno;
    }

    public void setInsuredIdno(String insuredIdno) {
        this.insuredIdno = insuredIdno;
    }

    public Date getSignDate() {
        return signDate;
    }

    public void setSignDate(Date signDate) {
        this.signDate = signDate;
    }

    public Date getCvaliDate() {
        return cvaliDate;
    }

    public void setCvaliDate(Date cvaliDate) {
        this.cvaliDate = cvaliDate;
    }

    public Date getCinvaliDate() {
        return cinvaliDate;
    }

    public void setCinvaliDate(Date cinvaliDate) {
        this.cinvaliDate = cinvaliDate;
    }

    public List<LzgRiskModel> getRiskList() {
        return riskList;
    }

    public void setRiskList(List<LzgRiskModel> riskList) {
        this.riskList = riskList;
    }

    public String getRiskType() {
        return riskType;
    }

    public void setRiskType(String riskType) {
        this.riskType = riskType;
    }
}
