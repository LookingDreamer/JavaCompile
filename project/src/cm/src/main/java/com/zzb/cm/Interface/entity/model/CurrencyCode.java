package com.zzb.cm.Interface.entity.model;

/**
 * 货币种类
 * Created by austinChen on 2015/10/8.
 */
public enum CurrencyCode {

	USD("USD", "美元"),
    HKD("HKD", "港币"),
    TWD("TWD", "台币"),
    EUR("EUR", "欧元"),
    JPY("JPY", "日元"),
    RMB("RMB", "人民币");
    private String desc;
    private String code;

    private CurrencyCode(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public String getCode() {
        return code;
    }
}
