package com.zzb.cm.Interface.entity.car.model;

/**
 * 投保基本信息
 * Created by Administrator on 2015-10-12.
 */
public class BaseSuiteInfo {

    /*商业险投保信息*/
    private BizSuiteInfo bizSuiteInfo;
    /*交强险投保信息*/
    private EfcSuiteInfo efcSuiteInfo;
    /*车船税投保信息*/
    private TaxSuiteInfo taxSuiteInfo;

    public BizSuiteInfo getBizSuiteInfo() {
        return bizSuiteInfo;
    }

    public void setBizSuiteInfo(BizSuiteInfo bizSuiteInfo) {
        this.bizSuiteInfo = bizSuiteInfo;
    }

    public EfcSuiteInfo getEfcSuiteInfo() {
        return efcSuiteInfo;
    }

    public void setEfcSuiteInfo(EfcSuiteInfo efcSuiteInfo) {
        this.efcSuiteInfo = efcSuiteInfo;
    }

    public TaxSuiteInfo getTaxSuiteInfo() {
        return taxSuiteInfo;
    }

    public void setTaxSuiteInfo(TaxSuiteInfo taxSuiteInfo) {
        this.taxSuiteInfo = taxSuiteInfo;
    }
}
