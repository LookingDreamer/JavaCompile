package com.zzb.extra.util;

 import java.util.HashMap;
 import java.util.Map;

/**
 * Created by Administrator on 2016/6/15.
 */
public class StatusMap {
    public static Map<String,String> states = new HashMap<>();
    public static Map<String,String> statesMap = new HashMap<>();
    static {
        states.put("1", "报价中");
        states.put("2", "报价失败");
        states.put("3", "报价成功待支付");
        states.put("4", "支付成功");
        states.put("5", "补齐影像");
        states.put("6", "核保成功");
        states.put("7", "核保成功，待补齐差额");
        states.put("8", "核保成功，待退回多收保费");
        states.put("9", "多收保费退款中");
        states.put("10", "多收保费退款成功");
        states.put("11", "承保成功待配送");
        states.put("12", "全额保费退款");
        states.put("13", "全额保费退款成功");
        states.put("14", "关闭");
        states.put("15", "完成");
        states.put("16", "人工报价中");
        states.put("17", "核保中");
        states.put("18", "承保政策限制");
        states.put("19", "核保失败");
        states.put("20", "报价失败"); //等待提交人工报价
        states.put("22", "拒绝承保");
        states.put("23", "差额补齐，待承保");
    }

    static {
        statesMap.put("2","1");
        statesMap.put("8","16");
        statesMap.put("14", "3");
        statesMap.put("16", "17");
        statesMap.put("19","5");
        statesMap.put("20","6");
        statesMap.put("21","6");
        statesMap.put("23","11");
        statesMap.put("25","6");
        statesMap.put("30","14");
        statesMap.put("33","15");
        statesMap.put("51","18");
        statesMap.put("52","20");
        statesMap.put("13","2");
        statesMap.put("22","22");
    }
}
