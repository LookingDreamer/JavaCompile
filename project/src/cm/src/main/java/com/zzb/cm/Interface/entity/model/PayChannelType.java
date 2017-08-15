package com.zzb.cm.Interface.entity.model;

/**
 * 支付渠道类型
 * Created by wjl on 2015/10/9.
 */
public enum PayChannelType {
    Payeco("Payeco","易联支付"),
    AliPay("AliPay","支付宝"),
    Bestpay("Bestpay","翼支付"),
    Mobile99bill("Mobile99bill","快刷"),
    TenPay("TenPay","财付通"),
    BaiFuBao("BaiFuBao","百付宝");
    private String desc;
    private String code;

    private PayChannelType(String code,String desc)

    {
        code=code;
        desc=desc;
    }

    public String getDesc()
    {
        return desc;
    }
}
