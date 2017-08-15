package com.common;

import com.common.redis.CMRedisClient;
import com.common.redis.IRedisClient;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.cninsure.core.utils.StringUtil;
import com.zzb.mobile.model.lastinsured.CarModel;
import com.zzb.mobile.model.lastinsured.LastYearCarinfoBean;
import com.zzb.mobile.model.lastinsured.LastYearClaimBean;
import com.zzb.mobile.model.lastinsured.LastYearPersonBean;
import com.zzb.mobile.model.lastinsured.LastYearPolicyBean;
import com.zzb.mobile.model.lastinsured.LastYearPolicyInfoBean;
import com.zzb.mobile.model.lastinsured.LastYearRiskinfo;
import com.zzb.mobile.model.lastinsured.LastYearSupplierBean;
import com.zzb.mobile.model.lastinsured.RiskBean;

public class ModelUtilTest {

	public static void main(String[] args) throws Exception {
//		JSONObject object = new JSONObject();
//		//object.put("taskSequence", "12345");
//		object.put("command", "xmppStartTask");
//		JSONObject params = new JSONObject();
//		params.put("abilityName", "getCarInfo_yongcheng");
//		params.put("areaId", "370100");
//		params.put("jabberId", "caradar_org@imadmin.org/groupuserlistener-shipper");
//		params.put("reqId", "bc562263-636a-4d32-9ef5-86a721ef7e0d_of_carShipperRobotActor-carinfo_gz@imadmin.org~schdule-shipper-0000-278");
//		params.put("robotId", "278");
//		params.put("taskSequence", "12345");
//		JSONObject inParas = new JSONObject();
//		inParas.put("car.specific.license", "鲁AHJ801");
//		params.put("inParas", inParas);
//		object.put("params", params);
		
		
//		RiskBean value = new RiskBean();
//		value.setSuppliername("1");
//		RiskBean value2 = new RiskBean();
//		value2.setSuppliername("1");
//		JSONArray array = new JSONArray();
//		array.add(value);
//		array.add(value2);
//		System.out.println(array.toString());
//		
//		JSONObject object = new JSONObject();
//		object.put("command", "");
//		JSONObject params = new JSONObject();
//		params.put("abilityName", "");
//		params.put("areaId", "");
//		params.put("jabberId", "");
//		//params.put("reqId", "");
//		params.put("robotId", "");
//		params.put("taskSequence", "12345");
//		JSONObject inParas = new JSONObject();
//		inParas.put("car.specific.license", "辽AN1H83");
//		//inParas.put("vehicleno", "鲁AHJ801");
//		params.put("inParas", inParas);
//		object.put("params", params);
//		
//		System.out.println(object.toString());
//		String url = "http://182.254.225.12/cif/rnpolicy/queryLastYearPolicy";
//		String result = HttpClientUtil.doPostJsonString(url, object.toString());
//		System.out.println(result);
//		JSONObject jsonObject=JSONObject.fromObject(result);
		
//		Map<String, Class> classMap = new HashMap<String, Class>();
//		classMap.put("carowner", LastYearPersonBean.class);
//		classMap.put("lastYearCarinfoBean", LastYearCarinfoBean.class);
//		classMap.put("carModel", CarModel.class);
//		classMap.put("riskBean", RiskBean.class);
//		classMap.put("lastYearPolicyBean", LastYearPolicyBean.class);
//		classMap.put("lastYearClaimBean", LastYearClaimBean.class);
//		classMap.put("lastYearRiskinfos", LastYearRiskinfo.class);
		
//		LastYearPolicyInfoBean lastYearPolicyInfoBean= (LastYearPolicyInfoBean) JSONObject.toBean(jsonObject,LastYearPolicyInfoBean.class);
//		if("0".equals(lastYearPolicyInfoBean.getStatus())){
//			//返回值放入ridis,key值 实例id_车牌号
//			RedisClient.set("110", lastYearPolicyInfoBean);
//			RedisClient.set("120", lastYearPolicyInfoBean);
//		}
		
//		Map<String, Boolean> result = new HashMap<String, Boolean>();
//			result.put("carowner", false);
//			result.put("carinfo", false);
//			result.put("carmodel", true);
//			result.put("provider", true);
//		result.put("insuredconf", false);
//		JSONObject object = JSONObject.fromObject(result);
//		System.out.println(object.toString());
		LastYearPolicyInfoBean lastYearPolicyInf = CMRedisClient.getInstance().get("cm:test", "967", LastYearPolicyInfoBean.class);
		JSONArray carmodels = lastYearPolicyInf.getCarModels();
//		System.out.println(lastYearPolicyInf.getCarModel().getDisplacement());
//		System.out.println(lastYearPolicyInf.getLastYearPolicyBean().getInsureder().getName());
//		
//		JSONArray lastYearRiskinfos = lastYearPolicyInf.getLastYearRiskinfos();
//		System.out.println(lastYearRiskinfos.size());
//		for(int i = 0;i < lastYearRiskinfos.size() ; i++){
//			JSONObject object3 = JSONObject.fromObject(lastYearRiskinfos.get(i));
//			System.out.println(object3.get("kindname"));
//		}
//		System.out.println(isNumeric("试试"));
		
//		List<CarModelInfoBean> list = new ArrayList<CarModelInfoBean>();
//		String res = HttpClientUtil.doGet("http://cx.baoxian.com/searchList/一汽奥迪",null);
//		JSONObject jsonObject = JSONObject.fromObject(res);
//		JSONArray jsonArray = JSONArray.fromObject(jsonObject.get("carModelList"));
//		for(int i = 0;i < jsonArray.size(); i ++){
//			System.out.println(jsonArray.get(i));
//			JSONObject object = JSONObject.fromObject(jsonArray.get(i));
//			CarModelInfoBean carModelInfoBean = (CarModelInfoBean) JSONObject.toBean(object, CarModelInfoBean.class);
//			list.add(carModelInfoBean);   
//		}
//		System.out.println(list.get(0).getBrandName());
	}

	public static boolean isNumeric(String str){ 
	    Pattern pattern = Pattern.compile("-?[0-9]+.?[0-9]+"); 
	    return pattern.matcher(str).matches();    
	 }
}
