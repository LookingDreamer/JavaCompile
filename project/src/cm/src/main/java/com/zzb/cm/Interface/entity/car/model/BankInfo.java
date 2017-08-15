package com.zzb.cm.Interface.entity.car.model;

import java.math.BigDecimal;

/**
 * Created by austinChen on 2015/10/8.
 */
public class BankInfo {

    /*银行编号*/
    private String bankNo;
    /*银行名称*/
    private String bankName;
    private BigDecimal singleTimeLimit;
    private BigDecimal singleDayLimit;
    private BigDecimal singleMonthLimit;
    private String logoUrl;

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankNo() {
        return bankNo;
    }

    public void setBankNo(String bankNo) {
        this.bankNo = bankNo;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public BigDecimal getSingleDayLimit() {
        return singleDayLimit;
    }

    public void setSingleDayLimit(BigDecimal singleDayLimit) {
        this.singleDayLimit = singleDayLimit;
    }

    public BigDecimal getSingleMonthLimit() {
        return singleMonthLimit;
    }

    public void setSingleMonthLimit(BigDecimal singleMonthLimit) {
        this.singleMonthLimit = singleMonthLimit;
    }

    public BigDecimal getSingleTimeLimit() {
        return singleTimeLimit;
    }

    public void setSingleTimeLimit(BigDecimal singleTimeLimit) {
        this.singleTimeLimit = singleTimeLimit;
    }
}
