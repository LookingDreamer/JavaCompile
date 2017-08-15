package com.zzb.warranty.exception;

/**
 * Created by Administrator on 2017/1/10.
 */
public class OrderNotFoundException extends RuntimeException {

    public OrderNotFoundException() {
        super("订单不存在");
    }
}
