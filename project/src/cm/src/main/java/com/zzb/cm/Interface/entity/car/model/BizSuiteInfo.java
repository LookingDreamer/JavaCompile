package com.zzb.cm.Interface.entity.car.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 商业险投保信息
 * Created by austinChen on 2015/10/9.
 */
public class BizSuiteInfo {

    /*投保起始日期*/
    private Date start;
    /*投保结束日期*/
    private Date end;
    /*原始保费*/
    private BigDecimal orgCharge;
    /*折后保费*/
    private BigDecimal discountCharge;
    /*折扣率*/
    private BigDecimal discountRate;
    /*险种配置信息*/
    private List<SuiteDef> suites;

    public BigDecimal getDiscountCharge() {
        return discountCharge;
    }

    public void setDiscountCharge(BigDecimal discountCharge) {
        this.discountCharge = discountCharge;
    }

    public BigDecimal getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(BigDecimal discountRate) {
        this.discountRate = discountRate;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public BigDecimal getOrgCharge() {
        return orgCharge;
    }

    public void setOrgCharge(BigDecimal orgCharge) {
        this.orgCharge = orgCharge;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public List<SuiteDef> getSuites() {
        return suites;
    }

    public void setSuites(List<SuiteDef> suites) {
        this.suites = suites;
    }
}
