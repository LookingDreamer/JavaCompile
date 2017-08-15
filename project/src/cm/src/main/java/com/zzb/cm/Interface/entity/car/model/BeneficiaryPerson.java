package com.zzb.cm.Interface.entity.car.model;

import java.math.BigDecimal;

/**
 * 受益人
 * Created by austinChen on 2015/10/9.
 */
public class BeneficiaryPerson extends InsurePerson {

    /*是否法定*/
    private Boolean isLegal;
    /*受益顺序*/
    private Integer orderNum;
    /*受益比例*/
    private BigDecimal ratio;

    public Boolean isLegal() {
        return isLegal;
    }

    public void setLegal(Boolean isLegal) {
        this.isLegal = isLegal;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public BigDecimal getRatio() {
        return ratio;
    }

    public void setRatio(BigDecimal ratio) {
        this.ratio = ratio;
    }
}
