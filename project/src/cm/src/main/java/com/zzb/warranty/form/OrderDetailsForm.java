package com.zzb.warranty.form;

/**
 * Created by Administrator on 2017/1/12.
 */
public class OrderDetailsForm {
    private String orderno;

    public OrderDetailsForm() {

    }

    public OrderDetailsForm(String orderno) {
        this.orderno = orderno;
    }

    public String getOrderno() {
        return orderno;
    }

    public void setOrderno(String orderno) {
        this.orderno = orderno;
    }

    @Override
    public String toString() {
        return "OrderDetailsForm{" +
                "orderno='" + orderno + '\'' +
                '}';
    }
}
