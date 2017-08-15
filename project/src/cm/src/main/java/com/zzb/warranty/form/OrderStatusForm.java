package com.zzb.warranty.form;

import javax.validation.constraints.NotNull;

/**
 * Created by Administrator on 2017/1/16.
 */
public class OrderStatusForm {

    @NotNull(message = "需要提供订单号")
    private String orderNo;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
}
