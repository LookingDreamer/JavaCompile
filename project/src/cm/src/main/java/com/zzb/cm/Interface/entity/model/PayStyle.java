package com.zzb.cm.Interface.entity.model;

/**
 * 支付方式
 * Created by austinChen on 2015/10/9.
 */
public enum  PayStyle {

    Virtual("Virtual","虚拟支付"),
    Mobile("Mobile","移动支付"),
    Web("Web","Web支付"),
    OffLine("OffLine","线下支付");
    private String desc;
    private String code;

    private PayStyle(String code,String desc)

    {
        code=this.code;
        desc=this.desc;
    }

    public String getDesc()
    {
        return desc;
    }
}
