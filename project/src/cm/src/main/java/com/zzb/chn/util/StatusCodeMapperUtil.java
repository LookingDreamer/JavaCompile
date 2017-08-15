package com.zzb.chn.util;

import java.util.*;

/**
 * Created on 2016/4/8. 
 */
public class StatusCodeMapperUtil {
    public static List<String> callbackState = new ArrayList<>();
    public static List<String> quoteFaileState = new ArrayList<>();
    public static List<String> commentState = new ArrayList<String>();
    public static List<String> insureState = new ArrayList<String>();
    public static final String Traffic = "VehicleCompulsoryIns";
    public static final String Tax = "VehicleTax";
    public static final String TaxOverDueFine = "VehicleTaxOverdueFine";
    public static Map<String, String> states = new HashMap<>();
    public static Map<String, String> stateDescription = new HashMap<>();
    // 先支付后核保状态映射 给新渠道区分用
    public static Map<String, String> newStates = new HashMap<>();
    public static Map<String, String> newStateDes = new HashMap<>();
    
    public static List<String> enterpriseIdCardType = new ArrayList<String>(); //企业车被保人证件类型
    public static List<String> pinCodePrv = new ArrayList<String>(); //北京平台流程验证码保险公司

    static {
        states.put("1", "2");    //信息录入=报价中
        states.put("2", "2");    //报价=报价中
        states.put("3", "2");    //EDI报价=报价中
        states.put("31", "2");    //人工回写=报价中
        states.put("32", "2");    //规则报价=报价中
        states.put("4", "2");    //精灵报价=报价中
        states.put("6", "2");    //人工调整=报价中
        states.put("7", "2");    //人工规则报价=报价中
        states.put("8", "2");    //人工报价=报价中
        states.put("15", "2");    //快速续保=报价中
        states.put("53", "2");    //平台查询=报价中
        states.put("51", "51");    //承保政策限制
        states.put("52", "13");    //等待人工触发报价请求
        states.put("13", "13");    //报价退回=报价失败,退回修改
        states.put("14", "14");    //选择投保=报价成功
        states.put("16", "16");    //EDI核保=核保中
        states.put("17", "16");    //精灵核保=核保中
        states.put("38", "16");    //精灵核保=核保中
        states.put("18", "16");    //人工核保=核保中
        states.put("19", "19");    //核保退回=核保失败,退回修改
        states.put("20", "20");    //支付=核保成功
        states.put("21", "25");    //二次支付确认=核保成功
        states.put("23", "23");    //打单=承保成功
        states.put("24", "23");    //配送=承保成功
        states.put("25", "25");    //EDI承保=支付成功
        states.put("26", "25");    //精灵承保=支付成功
        states.put("27", "25");    //人工承保=支付成功
        states.put("28", "25");    //承保退回=承保失败,退回修改
        states.put("29", "33");    //完成=承保成功
        states.put("30", "30");    //关闭
        states.put("33", "33");    //结束=
        states.put("34", "30");    //放弃=拒绝承保
        states.put("37", "30");    // 关闭
        states.put("39", "30");    //反向关闭

        newStates.put("1", "2");    //信息录入=报价中
        newStates.put("2", "2");    //报价=报价中
        newStates.put("3", "2");    //EDI报价=报价中
        newStates.put("31", "2");    //人工回写=报价中
        newStates.put("32", "2");    //规则报价=报价中
        newStates.put("4", "2");    //精灵报价=报价中
        newStates.put("6", "2");    //人工调整=报价中
        newStates.put("7", "2");    //人工规则报价=报价中
        newStates.put("53", "2");    //平台查询=报价中
        newStates.put("8", "8");    //人工报价=报价中
        newStates.put("15", "2");    //快速续保=报价中
        newStates.put("51", "51");    //承保政策限制
        newStates.put("52", "52");    //等待人工触发报价请求
        newStates.put("13", "13");    //报价退回=报价失败,退回修改
        newStates.put("14", "14");    //选择投保=报价成功
        newStates.put("16", "16");    //EDI核保=核保中
        newStates.put("17", "16");    //精灵核保=核保中
        newStates.put("38", "16");    //精灵核保=核保中
        newStates.put("18", "16");    //人工核保=核保中
        newStates.put("19", "19");    //核保退回=核保失败,退回修改
        newStates.put("20", "20");    //支付=核保成功
        newStates.put("21", "25");    //二次支付确认=核保成功
        newStates.put("23", "23");    //打单=承保成功
        newStates.put("24", "23");    //配送=承保成功
        newStates.put("25", "25");    //EDI承保=支付成功
        newStates.put("26", "25");    //精灵承保=支付成功
        newStates.put("27", "25");    //人工承保=支付成功
        newStates.put("28", "25");    //承保退回=承保失败,退回修改
        newStates.put("29", "33");    //完成=承保成功
        newStates.put("30", "30");    //关闭
        newStates.put("33", "33");    //结束=
        newStates.put("34", "30");    //放弃=拒绝承保
        newStates.put("37", "30");    // 关闭
        newStates.put("39", "30");    //反向关闭

        stateDescription.put("2", "报价中");
        stateDescription.put("13", "报价失败");
        stateDescription.put("14", "报价成功");
        stateDescription.put("16", "核保中");
        stateDescription.put("19", "核保退回修改");
        stateDescription.put("20", "核保成功");
        stateDescription.put("21", "支付确认中");    //二次支付确认=核保成功-+
        stateDescription.put("23", "承保成功");
        stateDescription.put("25", "支付成功");
        stateDescription.put("30", "关闭");
        stateDescription.put("33", "完成");
        stateDescription.put("51", "承保政策限制");

        newStateDes.put("2", "报价中");
        newStateDes.put("8", "人工报价中");
        newStateDes.put("13", "报价失败");
        newStateDes.put("14", "报价成功");
        newStateDes.put("16", "核保中");
        newStateDes.put("19", "核保退回修改");
        newStateDes.put("20", "核保成功");
        newStateDes.put("21", "支付确认中");    //二次支付确认=核保成功-+
        newStateDes.put("23", "承保成功");
        newStateDes.put("25", "支付成功");
        newStateDes.put("30", "关闭");
        newStateDes.put("33", "完成");
        newStateDes.put("51", "承保政策限制");
        newStateDes.put("52", "等待提交人工报价");

        callbackState.addAll(Arrays.asList(new String[]{"14", "13", "51", "52", "34", "30", "20", "19", "23", "24","33","29", "37", "39", "21"}));
        quoteFaileState.addAll(Arrays.asList(new String[]{"51", "52","37"}));
        commentState.addAll(Arrays.asList(new String[]{"13","19", "30", "34", "37", "39"}));
        insureState.addAll(Arrays.asList(new String[]{"16", "18", "19", "20", "21", "36"}));

        //“6-组织机构代码证”、“8-社会信用代码证”、“9-税务登记证”、“10-营业执照”
        enterpriseIdCardType.addAll(Arrays.asList(new String[]{"6", "8", "9", "10"}));
        //国泰=2002，太保=2011，人保=2005，平安=2007；取供应商id的前4位代表保险公司
        pinCodePrv.addAll(Arrays.asList(new String[]{"2002", "2011", "2005", "2007"}));
    }
}
