package com.zzb.cm.Interface.service;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import com.common.HttpClientUtil;

public enum FlowInfo {
	quoteapply("0","报价申请"),
	quotebegin("1","报价开始"),
	quotestorging("2","报价暂存中"),
	quotestorgesuc("3","报价暂存成功"),
	quotestorgefiled("4","报价暂存失败"),
	quotequery("5","报价查询中"),
	quotequerysuc("6","报价查询成功"),
	quotequeryfiled("7","报价查询失败"),
	quoteover("A","报价完成"),
	quotefiled("A1","报价失败"),
	underwritingapply("8","核保申请"),
	underwritingbegin("9","核保开始"),
	underwritingstorging("10","核保暂存中"),
	underwritingstorgesuc("11","核保暂存成功"),
	underwritingstorgefiled("12","核保暂存失败"),
	underwritingquery("13","核保查询中"),
	underwritingquerysuc("14","核保查询成功"),
	underwritingqueryfiled("15","核保查询失败"),
	underwritingover("B","核保完成"),
	underwritingoverfiled("B1","核保失败"),
	payapply("16","支付申请"),
	payapplyfiled("17","支付申请失败"),
	payapplysuc("18","支付申请成功"),
	payover("C","支付成功"),
	payfiled("C1","支付失败"),
	insquery("19","承保查询中"),
	insquerysuc("20","承保查询成功"),
	insqueryfield("21","承保查询失败"),
	insover("D","承保完成"),
	insfiled("D1","承保失败"),
	insdatanotover("D2","承保完成数据未回写"),
	guizequerty("RulesQuote","规则平台查询"),
	submitunderwritingover("E", "提核成功"),
	submitunderwritingfiled("E1", "提核失败"),
	
	autoinsuring("30", "自动核保中"),
	autoinsurestorgefiled("31", "自动核保暂存失败"),
	autoinsureover("32", "自动核保成功"),
	autoinsurefiled("33", "自动核保失败"),
	autoinsurequery("34", "自动核保待查询"),
	autoinsuresuc("35", "自动核保完成"),
	
	
	xb("xb","续保");
	
    private String desc;
    private String code;

    private FlowInfo(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public String getCode() {
        return code;
    }
    
    public static void main(String[] args) {
    	Map<String, Object>  resultmap=new HashMap<String,Object>();
		Map<String, String>  datamap=new HashMap<String,String>();
		String string = "483829,483623,483524,483526,483525,483490,483429,483436,476864,1,480567,480686,482429,480401,479775,479770,479758,479712,479713,479705,479707,479698,479686,479684,479679,477756,477908,477574,477248,476860,449851,447474,446922,446228,446139,443015,442716,442724,367083,327033,327030,264395,262460,246344,239759,226630,218664,218674,201170,153213,131460,131419,124047,122824,24293,24212,24211";
		String [] temp = string.split(",");
		for(int i =0;i<temp.length;i++){
			datamap.put("result", "1");
			resultmap.put("taskName", "精灵报价");
			resultmap.put("userid", "admin");
			resultmap.put("data", datamap);
			resultmap.put("processinstanceid", Long.parseLong(temp[i]));
			JSONObject jsonObject = JSONObject.fromObject(resultmap);
			Map<String, String> params = new HashMap<String, String>();
			params.put("datas", jsonObject.toString());
			String result1 = HttpClientUtil.doGet("http://cm.52zzb.com/workflow/process/completeSubTask", params);
			System.out.println(temp[i] + "====" + result1);
		}

	}

}
