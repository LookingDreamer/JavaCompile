package com.common;

import net.sf.json.JSONObject;

import com.zzb.mobile.model.lastinsured.LastYearPolicyInfoBean;

public class CifQueryLastPolicyTest {

	public static void main(String[] args) {
		
		getLastYearPolicyInfoBean("湘A38V38");
	}

	private static LastYearPolicyInfoBean getLastYearPolicyInfoBean(String carnum){
		JSONObject object = new JSONObject();
		object.put("flag", "XB");
		//车主名称
		//object.put("personName", "潘垞玙");
		//object.put("personIdno","513821198612194905");
		//上年投保公司有就传，没有不传
//		if(!StringUtil.isEmpty(provid)){
//			quickflag = "1";
//			object.put("robotId", provid);
//		}
//		if(!StringUtil.isEmpty(parityflag) && "1".equals(parityflag)){
//			quickflag = "1";
//		}
		//object.put("quickflag", quickflag);
		JSONObject inParas = new JSONObject();
		inParas.put("car.specific.license", carnum);
		object.put("inParas", inParas);
		object.put("provinceCode", "510000");
		object.put("areaId", "510100");
		object.put("eid", "88867898");
		//获取代理人所属机构id
		object.put("singlesite", "1251191005");
		System.out.println(object.toString());
		String url = "http://182.254.232.184:8080/cif/rnpolicy/queryLastYearPolicy";
		//String url = "http://119.29.53.84:8080/cif/rnpolicy/queryLastYearPolicy";
		String result = HttpClientUtil.doPostJsonString(url, object.toString());
		JSONObject jsonObject=JSONObject.fromObject(result);
		System.out.println("====="+jsonObject.toString());
		return (LastYearPolicyInfoBean) JSONObject.toBean(jsonObject, LastYearPolicyInfoBean.class);
	}
}
