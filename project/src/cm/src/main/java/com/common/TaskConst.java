package com.common;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

public class TaskConst {
	/**
	 * 认证任务
	 */
	public static String  VERIFY_0 = "0";   //认证任务
	/**
	 * 报价中-信息录入
	 */
	public static String  QUOTING_1 = "1";   //信息录入=报价中
	/**
	 * 报价中-报价
	 */
	public static String  QUOTING_2 = "2";    //报价=报价中
	/**
	 * EDI报价=报价中
	 */
	public static String  QUOTING_3 = "3";   //EDI报价=报价中
	/**
	 * 人工回写=报价中
	 */
	public static String  QUOTING_31 = "31"; //人工回写=报价中
	/**
	 * 规则报价=报价中
	 */
	public static String  QUOTING_32 = "32"; //规则报价=报价中
	/**
	 * 精灵报价=报价中
	 */
    public static String  QUOTING_4 = "4"; //精灵报价=报价中
    /**
	 * 人工调整=报价中
	 */
    public static String  QUOTING_6 = "6"; //人工调整=报价中
    /**
	 * 人工规则报价=报价中
	 */
    public static String  QUOTING_7 = "7";  //人工规则报价=报价中
    public static final String QUOTING_7_NAME = "人工规则报价";
    /**
	 * 人工报价=报价中
	 */
    public static String  QUOTING_8 = "8"; //人工报价=报价中
    /**
	 * 报价退回=报价失败,退回修改
	 */
    public static String  QUOTING_BACKTOMODIFY_13 = "13"; //报价退回=报价失败,退回修改
    /**
	 * 选择投保=报价成功
	 */
    public static String  QUOTINGSUCCESS_14 = "14";  //选择投保=报价成功
    /**
	 * 快速续保=报价中
	 */
    public static String  QUOTING_CONTINUE_15 = "15";	//快速续保=报价中
    /**
	 * EDI核保=核保中
	 */
    public static String  VERIFYING_16 = "16"; //EDI核保=核保中
    /**
	 * 精灵核保=核保中
	 */
    public static String  VERIFYING_17 = "17"; //精灵核保=核保中
    /**
	 * 人工核保=核保中
	 */
    public static String  VERIFYING_18 = "18"; //人工核保=核保中
    /**
	 * 核保退回=核保失败,退回修改
	 */
    public static String  VERIFYING_BACKTOMODIFY_19 = "19"; //核保退回=核保失败,退回修改
    /**
	 * 支付=核保成功
	 */
    public static String  PAYING_20 = "20"; //支付=核保成功
    /**
	 * 二次支付确认=核保成功
	 */
    public static String  PAYINGSUCCESS_SECOND_21 = "21"; //二次支付确认=核保成功
    /**
	 * 打单=承保成功
	 */
    public static String  UNDERWRITESUCCESS_23 = "23"; //打单=承保成功
    /**
	 * 配送=承保成功
	 */
    public static String  STARTPOST_24 = "24";//配送=承保成功
    /**
	 * EDI承保=支付成功
	 */
    public static String  PAYINGSUCCESS_25 = "25"; //EDI承保=支付成功
    /**
	 * 精灵承保=支付成功
	 */
    public static String  PAYINGSUCCESS_26 = "26";//精灵承保=支付成功
    /**
	 * 人工承保=支付成功
	 */
    public static String  PAYINGSUCCESS_27 = "27";//人工承保=支付成功
    /**
	 * 承保退回=承保失败,退回修改
	 */
    public static String  UNDERWRITEFAILED_28 = "28"; //承保退回=承保失败,退回修改
    /**
	 * 完成=承保成功
	 */
    public static String  DISTRIBUTESUCCESS_29 = "29";  //完成=承保成功
    /**
	 * 关闭=拒绝承保
	 */
    public static String  VERIFYINGFAILED_30 = "30"; //关闭=拒绝承保
    /**
	 * 结束
	 */
    public static String  END_33 = "33"; //结束=
    /**
	 * 放弃=取消承保
	 */
    public static String  CANCEL_34 = "34"; //放弃=取消承保
    /**
	 * 暂停支付
	 */
    public static String  PAYINGSTOP_36 = "36"; //暂停支付
    /**
	 * 关闭
	 */
    public static String  CLOSE_37 = "37"; //关闭
	
	/**
     * EDI自动核保暂存
     */
    public static String EDI_AUTO_INSURE = "40";
    /**
     * 精灵自动核保暂存
     */
    public static String ELF_AUTO_INSURE = "41";
    /**
	 * 承保政策限制
	 */
    public static String  QUOTEFAILED_51 = "51"; //承保政策限制
    /**
	 * 等待报价请求
	 */
    public static String  QUOTEWAIT_52 = "52";   //等待报价请求
    /**
	 * 平台查询
	 */
    public static String  QUOTEWAIT_53 = "53";   //平台查询
    public static void main(String[] args) {
    	Map<String,Object> params = new HashMap<String,Object>();
        //INSBWorkflowmain workflowmain =  insbWorkflowmainDao.selectINSBWorkflowmainByInstanceId(processinstanceid);
        params.put("userid", "admin");
        params.put("processinstanceid", Integer.parseInt("556666"));
        params.put("taskName","人工核保");
        Map<String,Object> data = new HashMap<String,Object>();
        data.put("result", "3");//人工核保完成的值是3
        params.put("data", data);
        JSONObject jsonb = JSONObject.fromObject(params);
        Map<String,String> par = new HashMap<String,String>();
        par.put("datas", jsonb.toString());
        
					String message = HttpClientUtil.doGet(WORKFLOW+"/process/completeSubTask", par);
					
					System.out.println("人工核保完成推送工作流:message="+message);
	}
    private static String WORKFLOW = "http://10.104.154.106/workflow";

//	static {
//		// 读取相关的配置
//		ResourceBundle resourceBundle = ResourceBundle
//				.getBundle("config/config");
//		WORKFLOW = resourceBundle.getString("workflow.url");
//	}
}
