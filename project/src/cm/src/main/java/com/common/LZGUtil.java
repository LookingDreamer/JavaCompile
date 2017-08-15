package com.common;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import net.sf.json.JSONObject;

import com.cninsure.core.utils.LogUtil;
import com.lzgapi.order.model.LzgOrderSaveModel;
import com.lzgapi.order.model.LzgOrderUpdateModel;
import com.lzgapi.yzt.model.UptomanagerModel;

/**
 * 调用蓝掌柜接口
 * 
 *
 */
public class LZGUtil {

	private static String LZG = "";

	static {
		// 读取相关的配置
		ResourceBundle resourceBundle = ResourceBundle
				.getBundle("config/config");
		LZG = resourceBundle.getString("lzg.api.service.url");
	}

	


	
	/**
	 * 通知蓝掌柜更新订单信息
	 * 
	 * @param taskId
	 * @param createby
	 * @param processInstanceId
	 */
	public static String noticeLZGUpdateOrder(LzgOrderUpdateModel lzgUpdateModel) {
		
		String result=null;
		String path = LZG + "/lm/requirement/status";
		JSONObject jsonObject = JSONObject.fromObject(lzgUpdateModel);
		LogUtil.info("订单向LZG更新参数："+jsonObject.toString());
		result =  HttpClientUtil.doPostJsonString(path, jsonObject.toString());
		return result;
	}
	
	/**
	 * cm用户升级成为懒掌柜
	 * 
	 * @param taskId
	 * @param createby
	 * @param processInstanceId
	 */
	public static String agentTurnToLZGManager(UptomanagerModel model) {
		
		String result=null;
		String path = LZG + "/lm/otherconfig/uptomanager";
		JSONObject jsonObject = JSONObject.fromObject(model);
		LogUtil.info("订单向LZG更新参数："+jsonObject.toString());
		result =  HttpClientUtil.doPostJsonString(path, jsonObject.toString());
		return result;
	}
	
	

	/**
	 * lzg请求方法：添加或更新订单信息
	 * @param model
	 * @return
	 */
	public static String saveOrupdateLZGorder(LzgOrderSaveModel model){
		String result=null;
		String path = LZG + "/lm/requirement/add";
		JSONObject jsonObject = JSONObject.fromObject(model);
		result =  HttpClientUtil.doPostJsonString(path, jsonObject.toString());
		return result;
	}
	
	public static void main(String[] args) throws Exception, IOException {
		// noticeWorkflowRenewalOrInsure("","","");
		// {"processinstanceid":138,"renewal":0,"userId":"zhangjc","incoids":
		// ["pingan","taipingyang"],"taskId":163}
		List<String> list = new ArrayList<String>();
		list.add("2022");
		list.add("2085");
		String res = noticeLZGUpdateOrder(null);
		System.out.println(res);
	}
}
