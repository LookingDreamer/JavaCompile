package com.zzb.model;

import java.util.Arrays;

/**
 * Created by @Author Chen Haoming on 2016/6/30.
 */
public enum DeliveryType {
    SELF_PICK_UP("0", "自取"), EXPRESS("1", "快递"), E_POLICY("3", "电子保单"), UNKNOWN("", "未知");

    DeliveryType(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String code;

    public String name;

    public static DeliveryType getDeliveryTypeByCode(String code) {
        return Arrays.stream(DeliveryType.values()).filter(dt -> dt.code.equals(code)).findFirst().get();
    }
}
