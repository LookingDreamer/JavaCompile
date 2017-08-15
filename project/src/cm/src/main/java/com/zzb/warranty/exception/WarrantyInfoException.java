package com.zzb.warranty.exception;

/**
 * Created by Administrator on 2017/1/20.
 */
public class WarrantyInfoException extends Exception{
    public WarrantyInfoException() {
    }

    public WarrantyInfoException(String message) {
        super(message);
    }

    public WarrantyInfoException(String message, Throwable cause) {
        super(message, cause);
    }

    public WarrantyInfoException(Throwable cause) {
        super(cause);
    }
}
