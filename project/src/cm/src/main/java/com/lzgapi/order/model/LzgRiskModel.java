package com.lzgapi.order.model;

/**
 * Created by hwc on 2017/5/19.
 */
public class LzgRiskModel {
    /**
     * 保费
     */
    private Double contPrem;
    /**
     * 保额
     */
    private Double amnt;
    /**
     * 险种（车险使用）
     */
    private String riskCode;
    /**
     * 险种名称
     */
    private String riskName;


    public Double getContPrem() {
        return contPrem;
    }

    public void setContPrem(Double contPrem) {
        this.contPrem = contPrem;
    }

    public Double getAmnt() {
        return amnt;
    }

    public void setAmnt(Double amnt) {
        this.amnt = amnt;
    }

    public String getRiskCode() {
        return riskCode;
    }

    public void setRiskCode(String riskCode) {
        this.riskCode = riskCode;
    }

    public String getRiskName() {
        return riskName;
    }

    public void setRiskName(String riskName) {
        this.riskName = riskName;
    }
}
