package com.cninsure.monitor.main;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import com.cninsure.core.utils.DateUtil;
import com.cninsure.core.utils.JsonUtil;
import com.cninsure.core.utils.LogUtil;
import com.cninsure.core.utils.StringUtil;
import com.common.HttpClientUtil;
import com.common.redis.CMRedisClient;
import com.zzb.conf.entity.INSBEdiconfiguration;
import com.zzb.conf.entity.INSBElfconf;

public class MonitorUtil {

	
	private static final String CM = "cm";
	private static final int expireTime = 60*60*24*7;
	public static Map<String,String> go2Edi(String taskId, String insComCode, String taskType, Map<String,String> params, List<Object> urls ){
		String currentDate =  DateUtil.getCurrentDate();
		Map<String,String> resultMap = new HashMap<String, String>();
		String keyOne = "";
		String targetUrl = "";
		String source = CM;
		long minCount = 99999999999l;
		String targetID = "";
		if(urls.size()==1){
			targetUrl = ((INSBEdiconfiguration) urls.get(0)).getInterfaceaddress();
			targetID = ((INSBEdiconfiguration) urls.get(0)).getId();
			keyOne = targetID + "_" +  ((INSBEdiconfiguration) urls.get(0)).getNoti();
		}else{
			for(Object tempData : urls){
				keyOne = targetID + "_" +   ((INSBEdiconfiguration) tempData).getNoti();
				String tempUrl = ((INSBEdiconfiguration) tempData).getInterfaceaddress();
				long tempCount = 0;
				if(StringUtil.isEmpty(CMRedisClient.getInstance().get(source, "newmonitor:edi:" + keyOne + ":" + "currentdate:" + currentDate + ":" + "inorder:" + "total"))){
				}else{
					tempCount = Long.parseLong(String.valueOf(CMRedisClient.getInstance().get(source, "newmonitor:edi:" + keyOne + ":" + "currentdate:" + currentDate + ":" + "inorder:" + "total")));
				}
				if(minCount>=tempCount){
					targetUrl=tempUrl;
					minCount=tempCount;
					targetID = ((INSBEdiconfiguration) tempData).getId();
				}
			}
		}
		
		/*String CM_MONITOR_EDI = "newmonitor:edi:" + keyOne + ":";
		String CM_MONITOR_EDI_TOTAL = CM_MONITOR_EDI + "total";
		String CM_MONITOR_EDI_ERRORCOUNT = CM_MONITOR_EDI + "errorcount";
		String CM_MONITOR_EDI_CURRENTDATE = CM_MONITOR_EDI + "currentdate:" + currentDate + ":";
		String CM_MONITOR_EDI_CURRENTDATE_TOTAL = CM_MONITOR_EDI_CURRENTDATE + "total";
		String CM_MONITOR_EDI_CURRENTDATE_FAILTOTAL = CM_MONITOR_EDI_CURRENTDATE + "failtotal";
		String CM_MONITOR_EDI_CURRENTDATE_ORDER = CM_MONITOR_EDI_CURRENTDATE + "inorder:";
		String CM_MONITOR_EDI_CURRENTDATE_ORDER_TOTAL = CM_MONITOR_EDI_CURRENTDATE_ORDER + "total";
		String CM_MONITOR_EDI_CURRENTDATE_ORDER_TYPE = CM_MONITOR_EDI_CURRENTDATE_ORDER + taskType;*/
		
        LogUtil.info("获得EDI地址====EDI任务通知接口：taskid=" + taskId + ",inscomcode=" + insComCode + ",taskType=" + taskType + ",调用地址:" + targetUrl + "====");
        params.put("monitorid", keyOne + "_" + currentDate);
        resultMap.put("monitorid", keyOne + "_" + currentDate);
        String result = "";
        try {
        	/*CMRedisClient.getInstance().addOne(CM, CM_MONITOR_EDI_TOTAL);
        	CMRedisClient.getInstance().addOne(CM, CM_MONITOR_EDI_CURRENTDATE_TOTAL);
        	CMRedisClient.getInstance().expire(CM, CM_MONITOR_EDI_CURRENTDATE_TOTAL, expireTime);
        	if(!StringUtil.isEmpty(String.valueOf(CMRedisClient.getInstance().get(CM, CM_MONITOR_EDI_ERRORCOUNT)))){
        		int errcount = Integer.parseInt(String.valueOf(CMRedisClient.getInstance().get(CM, CM_MONITOR_EDI_ERRORCOUNT)));
        		if(errcount>=10){
        			resultMap.put("flag", "false");
                	resultMap.put("msg", "服务器：" + targetUrl + ",存在问题，请检查服务器");
                	resultMap.put("monitorid", keyOne + "_" + currentDate);
                	CMRedisClient.getInstance().addOne(CM, CM_MONITOR_EDI_ERRORCOUNT);
                	CMRedisClient.getInstance().addOne(CM, CM_MONITOR_EDI_CURRENTDATE_FAILTOTAL);
                	CMRedisClient.getInstance().expire(CM, CM_MONITOR_EDI_CURRENTDATE_FAILTOTAL, expireTime);

                	//return resultMap;
        		}
        	}*/
        	JSONObject jsonObject = new JSONObject();
			if (params != null && !params.isEmpty()) {
				for (Map.Entry<String, String> entry : params.entrySet()) {
					String value = entry.getValue();
					if (value != null) {
						jsonObject.put(entry.getKey(), value);
					}
				}
			}
			result = HttpClientUtil.doPostJsonAsyncClientJson(targetUrl, jsonObject.toString(), "UTF-8");
		} catch (Exception e) {
	        LogUtil.info("====EDI任务通知接口：taskid=" + taskId + ",inscomcode=" + insComCode + ",taskType=" + taskType + ",调用地址:" + targetUrl + ",错误信息" + e.getMessage() + ",result:" + result + "====");
        	/*CMRedisClient.getInstance().addOne(CM, CM_MONITOR_EDI_ERRORCOUNT);
        	CMRedisClient.getInstance().addOne(CM, CM_MONITOR_EDI_CURRENTDATE_FAILTOTAL);
        	CMRedisClient.getInstance().expire(CM, CM_MONITOR_EDI_CURRENTDATE_FAILTOTAL, expireTime);*/
        	resultMap.put("flag", "false");
        	resultMap.put("msg", "错误信息:" + e.getMessage());
        	resultMap.put("monitorid", keyOne + "_" + currentDate);
	        return resultMap;
		}
        LogUtil.info("====EDI任务通知接口：taskid=" + taskId + ",inscomcode=" + insComCode + ",taskType=" + taskType + ",调用地址:" + targetUrl + ",result:" + result + "====");
        if(!StringUtil.isEmpty(result)){
			Map<String,Object> resultStringMap = JsonUtil.parseJSONToMap(result);
			if("true".equalsIgnoreCase(String.valueOf(resultStringMap.get("accept")))){
				/*CMRedisClient.getInstance().addOne(CM, CM_MONITOR_EDI_CURRENTDATE_ORDER_TOTAL);
	        	CMRedisClient.getInstance().expire(CM, CM_MONITOR_EDI_CURRENTDATE_ORDER_TOTAL, expireTime);
				CMRedisClient.getInstance().addOne(CM, CM_MONITOR_EDI_CURRENTDATE_ORDER_TYPE);
	        	CMRedisClient.getInstance().expire(CM, CM_MONITOR_EDI_CURRENTDATE_ORDER_TYPE, expireTime);*/
				resultMap.put("flag", "true");
				resultMap.put("monitorid", keyOne + "_" + currentDate);
		        return resultMap;
			}else{
				/*CMRedisClient.getInstance().addOne(CM, CM_MONITOR_EDI_ERRORCOUNT);
	        	CMRedisClient.getInstance().addOne(CM, CM_MONITOR_EDI_CURRENTDATE_FAILTOTAL);
            	CMRedisClient.getInstance().expire(CM, CM_MONITOR_EDI_CURRENTDATE_FAILTOTAL, expireTime);*/
	        	resultMap.put("flag", "false");
	        	resultMap.put("msg", "错误信息:" + resultStringMap.get("msg").toString());
	        	resultMap.put("monitorid", keyOne + "_" + currentDate);
		        return resultMap;
			}
		}else{
			/*CMRedisClient.getInstance().addOne(CM, CM_MONITOR_EDI_ERRORCOUNT);
        	CMRedisClient.getInstance().addOne(CM, CM_MONITOR_EDI_CURRENTDATE_FAILTOTAL);
        	CMRedisClient.getInstance().expire(CM, CM_MONITOR_EDI_CURRENTDATE_FAILTOTAL, expireTime);*/
        	resultMap.put("flag", "false");
        	resultMap.put("msg", "错误信息:EDI任务接收接口返回为空");
        	resultMap.put("monitorid", keyOne + "_" + currentDate);
	        return resultMap;
		}
	}
	
	public static Map<String,String> go2Robot(String taskId, String insComCode, String taskType, Map<String,String> params, List<Object> urls ) throws Exception{
		String currentDate =  DateUtil.getCurrentDate();
		Map<String,String> resultMap = new HashMap<String, String>();
		String keyOne = "";
		String targetUrl = "";
		String source = CM;
		long minCount = 99999999999l;
		String targetID = "";
		if(urls.size()==1){
			targetUrl = ((INSBElfconf) urls.get(0)).getElfpath();
			targetID = ((INSBElfconf) urls.get(0)).getId();
			keyOne = targetID + "_" +  ((INSBElfconf) urls.get(0)).getNoti();
		}else{
			for(Object tempData : urls){
				keyOne = targetID + "_" +   ((INSBElfconf) tempData).getNoti();
				String tempUrl = ((INSBElfconf) tempData).getElfpath();
				long tempCount = 0;
				String tempCountString = String.valueOf(CMRedisClient.getInstance().get(source, "newmonitor:edi:" + keyOne + ":" + "currentdate:" + currentDate + ":" + "inorder:" + "total"));
				if(StringUtil.isEmpty(tempCountString)){
				}else{
					tempCount = Long.parseLong(tempCountString);
				}
				if(minCount>=tempCount){
					targetUrl=tempUrl;
					minCount=tempCount;
					targetID = ((INSBElfconf) tempData).getId();
				}
			}
		}
		
		/*String CM_MONITOR_ROBOT = "newmonitor:robot:" + keyOne + ":";
		String CM_MONITOR_ROBOT_TOTAL = CM_MONITOR_ROBOT + "total";
		String CM_MONITOR_ROBOT_ERRORCOUNT = CM_MONITOR_ROBOT + "errorcount";
		String CM_MONITOR_ROBOT_CURRENTDATE = CM_MONITOR_ROBOT + "currentdate:" + currentDate + ":";
		String CM_MONITOR_ROBOT_CURRENTDATE_TOTAL = CM_MONITOR_ROBOT_CURRENTDATE + "total";
		String CM_MONITOR_ROBOT_CURRENTDATE_FAILTOTAL = CM_MONITOR_ROBOT_CURRENTDATE + "failtotal";
		String CM_MONITOR_ROBOT_CURRENTDATE_ORDER = CM_MONITOR_ROBOT_CURRENTDATE + "inorder:";
		String CM_MONITOR_ROBOT_CURRENTDATE_ORDER_TOTAL = CM_MONITOR_ROBOT_CURRENTDATE_ORDER + "total";
		String CM_MONITOR_ROBOT_CURRENTDATE_ORDER_TYPE = CM_MONITOR_ROBOT_CURRENTDATE_ORDER + taskType;*/
		
		LogUtil.info("====精灵任务通知接口：taskid=" + taskId + ",inscomcode=" + insComCode + ",taskType=" + taskType + ",调用地址:" + targetUrl + ",请求参数：params：" + params + "====");
        params.put("monitorid", keyOne + "_" + currentDate);
        resultMap.put("monitorid", keyOne + "_" + currentDate);
        String result = "";
        try {
        	/*CMRedisClient.getInstance().addOne(CM, CM_MONITOR_ROBOT_TOTAL);
        	CMRedisClient.getInstance().addOne(CM, CM_MONITOR_ROBOT_CURRENTDATE_TOTAL);
        	CMRedisClient.getInstance().expire(CM, CM_MONITOR_ROBOT_CURRENTDATE_TOTAL, expireTime);
        	if(!StringUtil.isEmpty(String.valueOf(CMRedisClient.getInstance().get(CM, CM_MONITOR_ROBOT_ERRORCOUNT)))){
        		int errcount = Integer.parseInt(String.valueOf(CMRedisClient.getInstance().get(CM, CM_MONITOR_ROBOT_ERRORCOUNT)));
        		if(errcount>=10){
        			resultMap.put("flag", "false");
                	resultMap.put("msg", "服务器：" + targetUrl + ",存在问题，请检查服务器");
                	resultMap.put("monitorid", keyOne + "_" + currentDate);
                	CMRedisClient.getInstance().addOne(CM, CM_MONITOR_ROBOT_ERRORCOUNT);
                	CMRedisClient.getInstance().addOne(CM, CM_MONITOR_ROBOT_CURRENTDATE_FAILTOTAL);
                	CMRedisClient.getInstance().expire(CM, CM_MONITOR_ROBOT_CURRENTDATE_FAILTOTAL, expireTime);
                	//return resultMap;
        		}
        	}*/
        	JSONObject jsonObject = new JSONObject();
			if (params != null && !params.isEmpty()) {
				for (Map.Entry<String, String> entry : params.entrySet()) {
					String value = entry.getValue();
					if (value != null) {
						jsonObject.put(entry.getKey(), value);
					}
				}
			}
			result = HttpClientUtil.doPostJsonAsyncClientJson(targetUrl, jsonObject.toString(), "UTF-8");
		} catch (Exception e) {
	        LogUtil.info("====精灵任务通知接口：taskid=" + taskId + ",inscomcode=" + insComCode + ",taskType=" + taskType + ",调用地址:" + targetUrl + ",错误信息" + e.getMessage() + ",result:" + result + "====");
        	/*CMRedisClient.getInstance().addOne(CM, CM_MONITOR_ROBOT_ERRORCOUNT);
        	CMRedisClient.getInstance().addOne(CM, CM_MONITOR_ROBOT_CURRENTDATE_FAILTOTAL);
        	CMRedisClient.getInstance().expire(CM, CM_MONITOR_ROBOT_CURRENTDATE_FAILTOTAL, expireTime);*/
        	resultMap.put("flag", "false");
        	resultMap.put("msg", "错误信息:" + e.getMessage());
        	resultMap.put("monitorid", keyOne + "_" + currentDate);
	        return resultMap;
		}
        LogUtil.info("====精灵任务通知接口返回：taskid=" + taskId + ",inscomcode=" + insComCode + ",taskType=" + taskType + ",调用地址:" + targetUrl + ",result:" + result + "====");
        if(!StringUtil.isEmpty(result)){
			Map<String,Object> resultStringMap = JsonUtil.parseJSONToMap(result);
			if("true".equalsIgnoreCase(String.valueOf(resultStringMap.get("result")))){
				/*CMRedisClient.getInstance().addOne(CM, CM_MONITOR_ROBOT_CURRENTDATE_ORDER_TOTAL);
	        	CMRedisClient.getInstance().expire(CM, CM_MONITOR_ROBOT_CURRENTDATE_ORDER_TOTAL, expireTime);
				CMRedisClient.getInstance().addOne(CM, CM_MONITOR_ROBOT_CURRENTDATE_ORDER_TYPE);
	        	CMRedisClient.getInstance().expire(CM, CM_MONITOR_ROBOT_CURRENTDATE_ORDER_TYPE, expireTime);*/
				resultMap.put("flag", "true");
				resultMap.put("monitorid", keyOne + "_" + currentDate);
		        return resultMap;
			}else{
				/*CMRedisClient.getInstance().addOne(CM, CM_MONITOR_ROBOT_ERRORCOUNT);
	        	CMRedisClient.getInstance().addOne(CM, CM_MONITOR_ROBOT_CURRENTDATE_FAILTOTAL);
            	CMRedisClient.getInstance().expire(CM, CM_MONITOR_ROBOT_CURRENTDATE_FAILTOTAL, expireTime);*/
	        	resultMap.put("flag", "false");
	        	resultMap.put("msg", "错误信息:" + resultStringMap.toString());
	        	resultMap.put("monitorid", keyOne + "_" + currentDate);
		        return resultMap;
			}
		}else{
			/*CMRedisClient.getInstance().addOne(CM, CM_MONITOR_ROBOT_ERRORCOUNT);
        	CMRedisClient.getInstance().addOne(CM, CM_MONITOR_ROBOT_CURRENTDATE_FAILTOTAL);
        	CMRedisClient.getInstance().expire(CM, CM_MONITOR_ROBOT_CURRENTDATE_FAILTOTAL, expireTime);*/
        	resultMap.put("flag", "false");
        	resultMap.put("msg", "错误信息:精灵任务接收接口返回为空");
        	resultMap.put("monitorid", keyOne + "_" + currentDate);
	        return resultMap;
		}
	}
	
	/**
	 * 主动发起精灵任务，params 是Map<String, Object>类型
	 * @param taskId
	 * @param insComCode
	 * @param taskType
	 * @param params
	 * @param urls
	 * @return
	 * @throws Exception
	 */
	public static Map<String,String> go2RobotTwo(String taskId, String insComCode, String taskType, Map<String,Object> params, List<Object> urls ) throws Exception{
		String currentDate =  DateUtil.getCurrentDate();
		Map<String,String> resultMap = new HashMap<String, String>();
		String keyOne = "";
		String targetUrl = "";
		String source = CM;
		long minCount = 99999999999l;
		String targetID = "";
		if(urls.size()==1){
			targetUrl = ((INSBElfconf) urls.get(0)).getElfpath();
			targetID = ((INSBElfconf) urls.get(0)).getId();
			keyOne = targetID + "_" +  ((INSBElfconf) urls.get(0)).getNoti();
		}else{
			for(Object tempData : urls){
				keyOne = targetID + "_" +   ((INSBElfconf) tempData).getNoti();
				String tempUrl = ((INSBElfconf) tempData).getElfpath();
				long tempCount = 0;
				String tempCountString = String.valueOf(CMRedisClient.getInstance().get(source, "newmonitor:edi:" + keyOne + ":" + "currentdate:" + currentDate + ":" + "inorder:" + "total"));
				if(StringUtil.isEmpty(tempCountString)){
				}else{
					tempCount = Long.parseLong(tempCountString);
				}
				if(minCount>=tempCount){
					targetUrl=tempUrl;
					minCount=tempCount;
					targetID = ((INSBElfconf) tempData).getId();
				}
			}
		}
		
		String CM_MONITOR_ROBOT = "newmonitor:robot:" + keyOne + ":";
		String CM_MONITOR_ROBOT_TOTAL = CM_MONITOR_ROBOT + "total";
		String CM_MONITOR_ROBOT_ERRORCOUNT = CM_MONITOR_ROBOT + "errorcount";
		String CM_MONITOR_ROBOT_CURRENTDATE = CM_MONITOR_ROBOT + "currentdate:" + currentDate + ":";
		String CM_MONITOR_ROBOT_CURRENTDATE_TOTAL = CM_MONITOR_ROBOT_CURRENTDATE + "total";
		String CM_MONITOR_ROBOT_CURRENTDATE_FAILTOTAL = CM_MONITOR_ROBOT_CURRENTDATE + "failtotal";
		String CM_MONITOR_ROBOT_CURRENTDATE_ORDER = CM_MONITOR_ROBOT_CURRENTDATE + "inorder:";
		String CM_MONITOR_ROBOT_CURRENTDATE_ORDER_TOTAL = CM_MONITOR_ROBOT_CURRENTDATE_ORDER + "total";
		String CM_MONITOR_ROBOT_CURRENTDATE_ORDER_TYPE = CM_MONITOR_ROBOT_CURRENTDATE_ORDER + taskType;
		
		LogUtil.info("====精灵任务通知接口：taskid=" + taskId + ",inscomcode=" + insComCode + ",taskType=" + taskType + ",调用地址:" + targetUrl + ",请求参数：params：" + params + "====");
        params.put("monitorid", keyOne + "_" + currentDate);
        resultMap.put("monitorid", keyOne + "_" + currentDate);
        String result = "";
        try {
        	CMRedisClient.getInstance().addOne(CM, CM_MONITOR_ROBOT_TOTAL);
        	CMRedisClient.getInstance().addOne(CM, CM_MONITOR_ROBOT_CURRENTDATE_TOTAL);
        	CMRedisClient.getInstance().expire(CM, CM_MONITOR_ROBOT_CURRENTDATE_TOTAL, expireTime);
        	if(!StringUtil.isEmpty(String.valueOf(CMRedisClient.getInstance().get(CM, CM_MONITOR_ROBOT_ERRORCOUNT)))){
        		int errcount = Integer.parseInt(String.valueOf(CMRedisClient.getInstance().get(CM, CM_MONITOR_ROBOT_ERRORCOUNT)));
        		if(errcount>=10){
        			resultMap.put("flag", "false");
                	resultMap.put("msg", "服务器：" + targetUrl + ",存在问题，请检查服务器");
                	resultMap.put("monitorid", keyOne + "_" + currentDate);
                	CMRedisClient.getInstance().addOne(CM, CM_MONITOR_ROBOT_ERRORCOUNT);
                	CMRedisClient.getInstance().addOne(CM, CM_MONITOR_ROBOT_CURRENTDATE_FAILTOTAL);
                	CMRedisClient.getInstance().expire(CM, CM_MONITOR_ROBOT_CURRENTDATE_FAILTOTAL, expireTime);
                	//return resultMap;
        		}
        	}
        	JSONObject jsonObject = new JSONObject();
			if (params != null && !params.isEmpty()) {
				for (Map.Entry<String, Object> entry : params.entrySet()) {
					Object value = entry.getValue();
					if (value != null) {
						jsonObject.put(entry.getKey(), value);
					}
				}
			}
			result = HttpClientUtil.doPostJsonAsyncClientJson(targetUrl, jsonObject.toString(), "UTF-8");
		} catch (Exception e) {
	        LogUtil.info("====精灵任务通知接口：taskid=" + taskId + ",inscomcode=" + insComCode + ",taskType=" + taskType + ",调用地址:" + targetUrl + ",错误信息" + e.getMessage() + ",result:" + result + "====");
        	CMRedisClient.getInstance().addOne(CM, CM_MONITOR_ROBOT_ERRORCOUNT);
        	CMRedisClient.getInstance().addOne(CM, CM_MONITOR_ROBOT_CURRENTDATE_FAILTOTAL);
        	CMRedisClient.getInstance().expire(CM, CM_MONITOR_ROBOT_CURRENTDATE_FAILTOTAL, expireTime);
        	resultMap.put("flag", "false");
        	resultMap.put("msg", "错误信息:" + e.getMessage());
        	resultMap.put("monitorid", keyOne + "_" + currentDate);
	        return resultMap;
		}
        LogUtil.info("====精灵任务通知接口返回：taskid=" + taskId + ",inscomcode=" + insComCode + ",taskType=" + taskType + ",调用地址:" + targetUrl + ",result:" + result + "====");
        if(!StringUtil.isEmpty(result)){
			Map<String,Object> resultStringMap = JsonUtil.parseJSONToMap(result);
			if("true".equalsIgnoreCase(String.valueOf(resultStringMap.get("result")))){
				CMRedisClient.getInstance().addOne(CM, CM_MONITOR_ROBOT_CURRENTDATE_ORDER_TOTAL);
	        	CMRedisClient.getInstance().expire(CM, CM_MONITOR_ROBOT_CURRENTDATE_ORDER_TOTAL, expireTime);
				CMRedisClient.getInstance().addOne(CM, CM_MONITOR_ROBOT_CURRENTDATE_ORDER_TYPE);
	        	CMRedisClient.getInstance().expire(CM, CM_MONITOR_ROBOT_CURRENTDATE_ORDER_TYPE, expireTime);
				resultMap.put("flag", "true");
				resultMap.put("monitorid", keyOne + "_" + currentDate);
		        return resultMap;
			}else{
				CMRedisClient.getInstance().addOne(CM, CM_MONITOR_ROBOT_ERRORCOUNT);
	        	CMRedisClient.getInstance().addOne(CM, CM_MONITOR_ROBOT_CURRENTDATE_FAILTOTAL);
            	CMRedisClient.getInstance().expire(CM, CM_MONITOR_ROBOT_CURRENTDATE_FAILTOTAL, expireTime);
	        	resultMap.put("flag", "false");
	        	resultMap.put("msg", "错误信息:" + resultStringMap.toString());
	        	resultMap.put("monitorid", keyOne + "_" + currentDate);
		        return resultMap;
			}
		}else{
			CMRedisClient.getInstance().addOne(CM, CM_MONITOR_ROBOT_ERRORCOUNT);
        	CMRedisClient.getInstance().addOne(CM, CM_MONITOR_ROBOT_CURRENTDATE_FAILTOTAL);
        	CMRedisClient.getInstance().expire(CM, CM_MONITOR_ROBOT_CURRENTDATE_FAILTOTAL, expireTime);
        	resultMap.put("flag", "false");
        	resultMap.put("msg", "错误信息:精灵任务接收接口返回为空");
        	resultMap.put("monitorid", keyOne + "_" + currentDate);
	        return resultMap;
		}
	}
	
	public static void main(String[] args) {
		CMRedisClient.getInstance().addOne("cm", "newmonitor:robot:10e8a3d0db7011e5dcb192f02895a7d7_02001:currentdate:2016-08-24:inorder:quote");
		System.out.println(CMRedisClient.getInstance().get("cm", "newmonitor:robot:10e8a3d0db7011e5dcb192f02895a7d7_02001:currentdate:2016-08-24:inorder:quote"));
	}
	
	public static Map<String,String> getPack(String taskId, String insComCode, String taskType, String monitorid,boolean flag,String processType){
		
		if("RulesQuote".equals(taskType)){
			return null;
		}
		
		String CM_MONITOR_TEMP = "newmonitor:" + processType.toLowerCase() + ":" + monitorid.substring(0, monitorid.lastIndexOf("_")) + ":";
		String CM_MONITOR_TEMP_ERRORCOUNT = CM_MONITOR_TEMP + "errorcount";
		String CM_MONITOR_TEMP_CURRENTDATE = CM_MONITOR_TEMP + "currentdate:" + monitorid.substring(monitorid.lastIndexOf("_") + 1, monitorid.length()) + ":";
		String CM_MONITOR_TEMP_CURRENTDATE_FAILTOTAL = CM_MONITOR_TEMP_CURRENTDATE + "failtotal";
		
		if(!flag){
			CMRedisClient.getInstance().addOne(CM, CM_MONITOR_TEMP_ERRORCOUNT);
			CMRedisClient.getInstance().addOne(CM, CM_MONITOR_TEMP_CURRENTDATE_FAILTOTAL);
			CMRedisClient.getInstance().expire(CM, CM_MONITOR_TEMP_CURRENTDATE_FAILTOTAL, expireTime);
		}
		
		
		return null;
	}
	
	/**
	 * 回写后调用任务完成接口
	 * @return
	 */
	public static Boolean feeBack(String taskId, String insComCode, String taskType, boolean flag, String processType,String monitorid,Long time){
        LogUtil.info("====数据回写接口：taskid=" + taskId + ",inscomcode=" + insComCode + ",taskType=" + taskType + ",flag=" + flag + ",processType=" + processType + ",time=" + time + ",monitorid=" + monitorid + "===");
		String CM_MONITOR_TEMP = "newmonitor:" + processType.toLowerCase() + ":" + monitorid.substring(0, monitorid.lastIndexOf("_")) + ":";
		String CM_MONITOR_TEMP_SUCTOTAL = CM_MONITOR_TEMP + "suctotal";
		String CM_MONITOR_TEMP_ERRORCOUNT = CM_MONITOR_TEMP + "errorcount";
		String CM_MONITOR_TEMP_CURRENTDATE = CM_MONITOR_TEMP + "currentdate:" + monitorid.substring(monitorid.lastIndexOf("_") + 1, monitorid.length()) + ":";
		String CM_MONITOR_TEMP_CURRENTDATE_SUCTOTAL = CM_MONITOR_TEMP_CURRENTDATE + "suctotal";
		String CM_MONITOR_TEMP_CURRENTDATE_FAILTOTAL = CM_MONITOR_TEMP_CURRENTDATE + "failtotal";
		String CM_MONITOR_TEMP_CURRENTDATE_ORDER = CM_MONITOR_TEMP_CURRENTDATE + "inorder:";
		String CM_MONITOR_TEMP_CURRENTDATE_ORDER_TOTAL = CM_MONITOR_TEMP_CURRENTDATE_ORDER + "total";
		String CM_MONITOR_TEMP_CURRENTDATE_ORDER_TYPE = CM_MONITOR_TEMP_CURRENTDATE_ORDER + taskType;
		String CURRENTDATE_ORDER_TOTAL = String.valueOf(CMRedisClient.getInstance().get(CM, CM_MONITOR_TEMP_CURRENTDATE_ORDER_TOTAL));
		if(!StringUtil.isEmpty(CURRENTDATE_ORDER_TOTAL)&&Integer.parseInt(CURRENTDATE_ORDER_TOTAL)>0){
			CMRedisClient.getInstance().minusOne(CM, CM_MONITOR_TEMP_CURRENTDATE_ORDER_TOTAL);
		}
		String CURRENTDATE_ORDER_TYPE = String.valueOf(CMRedisClient.getInstance().get(CM, CM_MONITOR_TEMP_CURRENTDATE_ORDER_TYPE));
		if(!StringUtil.isEmpty(CURRENTDATE_ORDER_TYPE)&&Integer.parseInt(CURRENTDATE_ORDER_TYPE)>0){
			CMRedisClient.getInstance().minusOne(CM, CM_MONITOR_TEMP_CURRENTDATE_ORDER_TYPE);
		}
		String TEMP_ERRORCOUNT = String.valueOf(CMRedisClient.getInstance().get(CM, CM_MONITOR_TEMP_ERRORCOUNT));
		if(!StringUtil.isEmpty(TEMP_ERRORCOUNT)&&Integer.parseInt(TEMP_ERRORCOUNT)>0){
			CMRedisClient.getInstance().minusOne(CM, CM_MONITOR_TEMP_ERRORCOUNT);
		}else{
			CMRedisClient.getInstance().set(CM,CM_MONITOR_TEMP_ERRORCOUNT,0);
		}
		if(flag){
			CMRedisClient.getInstance().addOne(CM, CM_MONITOR_TEMP_SUCTOTAL);
			CMRedisClient.getInstance().addOne(CM, CM_MONITOR_TEMP_CURRENTDATE_SUCTOTAL);
        	CMRedisClient.getInstance().expire(CM, CM_MONITOR_TEMP_CURRENTDATE_SUCTOTAL, expireTime);
		}else{
			CMRedisClient.getInstance().addOne(CM, CM_MONITOR_TEMP_CURRENTDATE_FAILTOTAL);
        	CMRedisClient.getInstance().expire(CM, CM_MONITOR_TEMP_CURRENTDATE_FAILTOTAL, expireTime);
		}
		return false;
	}
}
