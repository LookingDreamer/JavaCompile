package com.zzb.warranty.exception;

/**
 * Created by Administrator on 2017/1/14.
 */
public class PayPlatformException extends RuntimeException {

    public PayPlatformException() {
    }

    public PayPlatformException(String message) {
        super(message);
    }

    public PayPlatformException(String message, Throwable cause) {
        super(message, cause);
    }
}
