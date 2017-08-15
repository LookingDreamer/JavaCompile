package com.zzb.app.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zzb.app.service.AppOrderInfoService;
@Service
@Transactional
public class AppOrderInfoServiceImpl implements AppOrderInfoService {

	@Override
	public String getMultiQuoteInfo(String multiQuoteId) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, String> msm_header = new HashMap<String, String>();
		Map<String, Object> body = new HashMap<String, Object>();

		// 添加MSM_HEADER数据
		// TODO :Add MSM_HEADER Data
		msm_header.put("CmdId", "Get");
		msm_header.put("CmdModule", "MULTIQUOTES");
		msm_header.put("CmdType", "FHBX");
		msm_header.put("CmdVer", "100");
		msm_header.put("Token", "test");

		// 添加Body数据
		Map<String, Object> multiQuoteInfo = new HashMap<String, Object>();
		Map<String, Object> multiQuote = new HashMap<String, Object>();

		// TODO :Add MSM_HEADER Data

		multiQuote.put("accounteId", "B88C62ADF49D4343AA76829209CCD614");
		multiQuote.put("id", "ba21a1fd7cfd4e7d98fea983ce416f53");
		multiQuote.put("isLock", "true");
		multiQuote.put("isVerify", "false");
		// Object
		multiQuote.put("processStatus", "");
		// Object
		multiQuote.put("processType", "");
		multiQuote.put("remark", "");
		multiQuote.put("status", "FirstVerifying");

		Map<String, Object> vehicleEnquiryList = new HashMap<String, Object>();
		List<Map<String, String>> row = new ArrayList<Map<String, String>>();

		// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		Map<String, String> r0 = new HashMap<String, String>();
		Map<String, String> r1 = new HashMap<String, String>();
		r0.put("companyId", "12448");
		r0.put("companyName", "富德财险");
		r0.put("couldInsure", "false");
		r0.put("currentState", "Created");
		r0.put("id", "0001012448152531729530687");
		r0.put("supplerId", "1056");
		r0.put("way", "human");

		r1.put("companyId", "84");
		r1.put("companyName", "鼎和财险");
		r1.put("couldInsure", "false");
		r1.put("currentState", "Created");
		r1.put("id", "000150084152531729539741");
		r1.put("supplerId", "101");
		r1.put("way", "human");

		row.add(r0);
		row.add(r1);
		// +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

		vehicleEnquiryList.put("row", row);

		multiQuote.put("vehicleEnquiryList", vehicleEnquiryList);

		multiQuoteInfo.put("multiQuote", multiQuote);
		body.put("MultiQuoteInfo", multiQuoteInfo);

		String code = "0";
		String text = "OK";
		String type = "0";

		result.put("Code", code);
		result.put("Text", text);
		result.put("Type", type);
		result.put("MSM_HEADER", msm_header);
		result.put("Body", body);

		JSONObject jsonObject = JSONObject.fromObject(result);
		return jsonObject.toString();
	}

	@Override
	public String getMultiQuoteInstanceList(String uuid, String licensePlate,
			String max, String offset, String insuredName, String status,
			String dateCreatedStart) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, String> msm_header = new HashMap<String, String>();
		Map<String, Object> body = new HashMap<String, Object>();

		// 添加MSM_HEADER数据
		// TODO :Add MSM_HEADER Data
		msm_header.put("CmdId", "Search");
		msm_header.put("CmdModule", "MULTIQUOTES");
		msm_header.put("CmdType", "FHBX");
		msm_header.put("CmdVer", "1.0.0");

		Map<String, Object> success = new HashMap<String, Object>();
		Map<String, Object> multiQuoteInstanceList = new HashMap<String, Object>();
		multiQuoteInstanceList.put("max", max);
		List<Map<String, Object>> multiQuoteInstance = new ArrayList<Map<String, Object>>();
		
		Map<String, Object> multiQuoteInstance0 = new HashMap<String, Object>();
		multiQuoteInstance0.put("dateCreated", "2015-09-10 16:56:56");
		multiQuoteInstance0.put("insuredName", "吴先生");
		multiQuoteInstance0.put("isLock", "true");
		multiQuoteInstance0.put("isVerify", "false");
		multiQuoteInstance0.put("lastUpdated", "2015-09-10 17:54:49");
		multiQuoteInstance0.put("licensePlate", "暂未上牌");
		multiQuoteInstance0.put("multiQuoteId",
				"ba21a1fd7cfd4e7d98fea983ce416f53");
		multiQuoteInstance0.put("ownerName", "吴先生");
		multiQuoteInstance0.put("processStatus", "");
		multiQuoteInstance0.put("processType", "");
		multiQuoteInstance0.put("status", "FirstVerifying");

		List<Map<String, Object>> vehicleEnquiryInstance = new ArrayList<Map<String, Object>>();
		// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		Map<String, Object> vehicleEnquiryInstance0 = new HashMap<String, Object>();
		Map<String, Object> vehicleEnquiryInstance1 = new HashMap<String, Object>();

		Map<String, Object> attrs = new HashMap<String, Object>();
		List<Map<String, Object>> row = new ArrayList<Map<String, Object>>();
		Map<String, Object> row0 = new HashMap<String, Object>();
		Map<String, Object> row1 = new HashMap<String, Object>();
		row0.put("key", "ruleItem.ruleID");
		row0.put("value", "682");
		row1.put("key", "enquiry.limit.date");
		row1.put("value", "2015-09-10 23:59:59");
		row.add(row0);
		row.add(row1);
		attrs.put("row", row);
		vehicleEnquiryInstance0.put("attrs", attrs);

		Map<String, Object> calcResult = new HashMap<String, Object>();
		calcResult.put("totalPrice", "");
		vehicleEnquiryInstance0.put("calcResult", calcResult);

		Map<String, Object> company = new HashMap<String, Object>();
		company.put("companyId", "12448");
		company.put("fullName", "富德财产保险股份有限公司");
		company.put("shortName", "富德财险");
		vehicleEnquiryInstance0.put("company", company);

		vehicleEnquiryInstance0.put("currentState", "Created");
		vehicleEnquiryInstance0.put("insureWay", "human");
		vehicleEnquiryInstance0.put("message", "正在计算参考报价，请稍等");
		vehicleEnquiryInstance0.put("payResult", "false");

		Map<String, Object> quoteResult = new HashMap<String, Object>();
		quoteResult.put("statusCode", "");
		quoteResult.put("totalPrice", "");
		vehicleEnquiryInstance0.put("quoteResult", quoteResult);

		vehicleEnquiryInstance0.put("vehicleEnquiryId",
				"0001012448152531729530687");

		// repeat

		vehicleEnquiryInstance1.put("attrs", attrs);
		vehicleEnquiryInstance1.put("calcResult", calcResult);
		vehicleEnquiryInstance1.put("company", company);
		vehicleEnquiryInstance1.put("currentState", "Created");
		vehicleEnquiryInstance1.put("insureWay", "human");
		vehicleEnquiryInstance1.put("message", "正在计算参考报价，请稍等");
		vehicleEnquiryInstance1.put("payResult", "false");
		vehicleEnquiryInstance1.put("quoteResult", quoteResult);
		vehicleEnquiryInstance1.put("vehicleEnquiryId",
				"0001012448152531729530687");

		vehicleEnquiryInstance.add(vehicleEnquiryInstance0);
		vehicleEnquiryInstance.add(vehicleEnquiryInstance1);
		// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

		multiQuoteInstance0.put("vehicleEnquiryInstance",vehicleEnquiryInstance);

		
		multiQuoteInstance.add(multiQuoteInstance0);
		multiQuoteInstance.add(multiQuoteInstance0);
		multiQuoteInstance.add(multiQuoteInstance0);
		multiQuoteInstance.add(multiQuoteInstance0);
		multiQuoteInstance.add(multiQuoteInstance0);
		multiQuoteInstance.add(multiQuoteInstance0);
		multiQuoteInstance.add(multiQuoteInstance0);
		multiQuoteInstance.add(multiQuoteInstance0);
		multiQuoteInstance.add(multiQuoteInstance0);
		multiQuoteInstance.add(multiQuoteInstance0);
		multiQuoteInstance.add(multiQuoteInstance0);
		
		multiQuoteInstanceList.put("multiQuoteInstance", multiQuoteInstance);
		
		multiQuoteInstanceList.put("multiQuoteInstanceTotal","47");
		multiQuoteInstanceList.put("offset",offset);
		
		success.put("multiQuoteInstanceList", multiQuoteInstanceList);
		
		body.put("success", success);
		
		
		String code = "0";
		String text = "OK";
		String type = "0";

		result.put("Code", code);
		result.put("Text", text);
		result.put("Type", type);
		result.put("MSM_HEADER", msm_header);
		result.put("Body", body);

		JSONObject jsonObject = JSONObject.fromObject(result);
		return jsonObject.toString();
	}

	@Override
	public String getMultiQuoteInstanceList(String uuid, String multiQuoteId,
			String licensePlate) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, String> msm_header = new HashMap<String, String>();
		Map<String, Object> body = new HashMap<String, Object>();

		// 添加MSM_HEADER数据
		// TODO :Add MSM_HEADER Data
		msm_header.put("CmdId", "Search");
		msm_header.put("CmdModule", "MULTIQUOTES");
		msm_header.put("CmdType", "FHBX");
		msm_header.put("CmdVer", "1.0.0");

		Map<String, Object> success = new HashMap<String, Object>();
		Map<String, Object> multiQuoteInstanceList = new HashMap<String, Object>();
		multiQuoteInstanceList.put("max", "10");
		Map<String, Object> multiQuoteInstance = new HashMap<String, Object>();
		multiQuoteInstance.put("dateCreated", "2015-09-10 17:23:30");
		multiQuoteInstance.put("insuredName", "吴先生");
		multiQuoteInstance.put("isLock", "true");
		multiQuoteInstance.put("isVerify", "false");
		multiQuoteInstance.put("lastUpdated", "2015-09-10 17:54:49");
		multiQuoteInstance.put("licensePlate", "暂未上牌");
		multiQuoteInstance.put("multiQuoteId",
				"ba21a1fd7cfd4e7d98fea983ce416f53");
		multiQuoteInstance.put("ownerName", "吴先生");
		multiQuoteInstance.put("processStatus", "");
		multiQuoteInstance.put("processType", "");
		multiQuoteInstance.put("status", "FirstVerifying");

		List<Map<String, Object>> vehicleEnquiryInstance = new ArrayList<Map<String, Object>>();
		// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		Map<String, Object> vehicleEnquiryInstance0 = new HashMap<String, Object>();
		Map<String, Object> vehicleEnquiryInstance1 = new HashMap<String, Object>();

		Map<String, Object> attrs = new HashMap<String, Object>();
		List<Map<String, Object>> row = new ArrayList<Map<String, Object>>();
		Map<String, Object> row0 = new HashMap<String, Object>();
		Map<String, Object> row1 = new HashMap<String, Object>();
		row0.put("key", "ruleItem.ruleID");
		row0.put("value", "682");
		row1.put("key", "enquiry.limit.date");
		row1.put("value", "2015-09-10 23:59:59");
		row.add(row0);
		row.add(row1);
		attrs.put("row", row);
		vehicleEnquiryInstance0.put("attrs", attrs);

		Map<String, Object> calcResult = new HashMap<String, Object>();
		calcResult.put("totalPrice", "");
		vehicleEnquiryInstance0.put("calcResult", calcResult);

		Map<String, Object> company = new HashMap<String, Object>();
		company.put("companyId", "12448");
		company.put("fullName", "富德财产保险股份有限公司");
		company.put("shortName", "富德财险");
		vehicleEnquiryInstance0.put("company", company);

		vehicleEnquiryInstance0.put("currentState", "Created");
		vehicleEnquiryInstance0.put("insureWay", "human");
		vehicleEnquiryInstance0.put("message", "正在计算参考报价，请稍等");
		vehicleEnquiryInstance0.put("payResult", "false");

		Map<String, Object> quoteResult = new HashMap<String, Object>();
		quoteResult.put("statusCode", "");
		quoteResult.put("totalPrice", "");
		vehicleEnquiryInstance0.put("quoteResult", quoteResult);

		vehicleEnquiryInstance0.put("vehicleEnquiryId",
				"0001012448152531729530687");

		// repeat

		vehicleEnquiryInstance1.put("attrs", attrs);
		vehicleEnquiryInstance1.put("calcResult", calcResult);
		vehicleEnquiryInstance1.put("company", company);
		vehicleEnquiryInstance1.put("currentState", "Created");
		vehicleEnquiryInstance1.put("insureWay", "human");
		vehicleEnquiryInstance1.put("message", "正在计算参考报价，请稍等");
		vehicleEnquiryInstance1.put("payResult", "false");
		vehicleEnquiryInstance1.put("quoteResult", quoteResult);
		vehicleEnquiryInstance1.put("vehicleEnquiryId",
				"0001012448152531729530687");

		vehicleEnquiryInstance.add(vehicleEnquiryInstance0);
		vehicleEnquiryInstance.add(vehicleEnquiryInstance1);
		// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

		multiQuoteInstance
				.put("vehicleEnquiryInstance", vehicleEnquiryInstance);

		multiQuoteInstanceList.put("multiQuoteInstance", multiQuoteInstance);
		multiQuoteInstanceList.put("multiQuoteInstanceTotal", "1");
		multiQuoteInstanceList.put("offset", "0");

		success.put("multiQuoteInstanceList", multiQuoteInstanceList);

		body.put("Success", success);

		String code = "0";
		String text = "OK";
		String type = "0";

		result.put("Code", code);
		result.put("Text", text);
		result.put("Type", type);
		result.put("MSM_HEADER", msm_header);
		result.put("Body", body);

		JSONObject jsonObject = JSONObject.fromObject(result);
		return jsonObject.toString();
	}
	
	@Override
	public String updateDept(String enquiryId, String content){
		
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, String> msm_header = new HashMap<String, String>();
		Map<String, Object> body = new HashMap<String, Object>();

		// 添加MSM_HEADER数据
		// TODO :Add MSM_HEADER Data
		msm_header.put("CmdId","BusinessOffice");
		msm_header.put("CmdModule","QUOTE");
		msm_header.put("CmdType","FHBX");
		msm_header.put("CmdVer","100");
		msm_header.put("Token", "test");

		// 添加Body数据
		// TODO :Add Body Data
		body.put("Result","true");
		
		String code = "0";
		String text = "OK";
		String type = "0";

		result.put("Code", code);
		result.put("Text", text);
		result.put("Type", type);
		result.put("MSM_HEADER", msm_header);
		result.put("Body", body);

		JSONObject jsonObject = JSONObject.fromObject(result);
		return jsonObject.toString();
	}
	
	@Override
	public String saveApplicant(String multiQuoteId){
		
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, String> msm_header = new HashMap<String, String>();
		Map<String, Object> body = new HashMap<String, Object>();
		
		// 添加MSM_HEADER数据
		// TODO :Add MSM_HEADER Data
		msm_header.put("CmdId","QuoteInfo");
		msm_header.put("CmdModule","MULTIQUOTES");
		msm_header.put("CmdType","FHBX");
		msm_header.put("CmdVer","100");
		msm_header.put("Token", "test");
		
		// 添加Body数据
		// TODO :Add Body Data
		
		
		
		
		
		
		
		
		
		
		
		
		
		
		String code = "0";
		String text = "OK";
		String type = "0";
		
		result.put("Code", code);
		result.put("Text", text);
		result.put("Type", type);
		result.put("MSM_HEADER", msm_header);
		result.put("Body", body);
		
		JSONObject jsonObject = JSONObject.fromObject(result);
		return jsonObject.toString();
	}
	
	
	@Override
	public String getPaymentList(String payMethod, String bizTransactionId){
		
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, String> msm_header = new HashMap<String, String>();
		Map<String, Object> body = new HashMap<String, Object>();
		
		// 添加MSM_HEADER数据
		// TODO :Add MSM_HEADER Data
		msm_header.put("CmdId","PaymentService");
		msm_header.put("CmdModule","ORDER");
		msm_header.put("CmdType","FHBX");
		
		
		Map<String, Object> paymentList = new HashMap<String, Object>();
		Map<String, Object> paymentListCon = new HashMap<String, Object>();
		paymentListCon.put("bizTransactionId", bizTransactionId);
		
		Map<String, Object> payment = new HashMap<String, Object>();
		
		List<Map<String, String>> param = new ArrayList<Map<String, String>>();
		Map<String, String> param0 = new HashMap<String, String>();
		Map<String, String> param1 = new HashMap<String, String>();
		Map<String, String> param2 = new HashMap<String, String>();
		Map<String, String> param3 = new HashMap<String, String>();
		Map<String, String> param4 = new HashMap<String, String>();
		Map<String, String> param5 = new HashMap<String, String>();
		Map<String, String> param6 = new HashMap<String, String>();
		Map<String, String> param7 = new HashMap<String, String>();
		param0.put("paramName","paymentFlag");
		param0.put("paramValue","true");
		param1.put("paramName","bizTransactionId");
		param1.put("paramValue",bizTransactionId);
		param2.put("paramName","traceNumber");
		param2.put("paramValue",bizTransactionId);
		param3.put("paramName","paymentAmount");
		param3.put("paramValue","5433.06");
		param4.put("paramName","idCardNumber");
		param4.put("paramValue","440121196708030034");
		param5.put("paramName","idCardName");
		param5.put("paramValue","江泽锋");
		param6.put("paramName","userNameIdCardNumber");
		param6.put("paramValue","");
		param7.put("paramName","userName");
		param7.put("paramValue","渠道");
		
		param.add(param0);
		param.add(param1);
		param.add(param2);
		param.add(param3);
		param.add(param4);
		param.add(param5);
		param.add(param6);
		param.add(param7);
		
		
		Map<String, Object> paymentMethod = new HashMap<String, Object>();
		
		paymentMethod.put("code","Payeco");
		paymentMethod.put("desc","无卡支付");
		
		payment.put("param", param);
		payment.put("paymentMethod", paymentMethod);
		
		paymentListCon.put("payment",payment);
		
		paymentList.put("paymentListCon",paymentListCon);
		
		body.put("paymentList", paymentList);
		
		
		String code = "0";
		String text = "OK";
		String type = "0";
		
		result.put("Code", code);
		result.put("Text", text);
		result.put("Type", type);
		result.put("MSM_HEADER", msm_header);
		result.put("Body", body);
		
		JSONObject jsonObject = JSONObject.fromObject(result);
		return jsonObject.toString();
	}

}
