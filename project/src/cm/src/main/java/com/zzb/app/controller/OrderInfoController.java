package com.zzb.app.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cninsure.core.controller.BaseController;
import com.cninsure.core.exception.ControllerException;
import com.common.ModelUtil;
import com.zzb.app.service.AppOrderInfoService;

@Controller
@RequestMapping("/cpmap/*")
public class OrderInfoController extends BaseController {
	
	@Resource
	private AppOrderInfoService orderInfoService;

	/**
	 * 多方报价信息订单分页查询
	 * 请求方式 POST 请求地址/cpmap/access/
	 * @param MSM_PARAM 参数字符串 【uuid=B88C62ADF49D4343AA76829209CCD614;multiQuoteId=0592462981804cbfbd33ed751c055701;licensePlate=粤A2546】
	 * 						【uuid=B88C62ADF49D4343AA76829209CCD614;max=12;offset=0;licensePlate=;insuredName=;status=;dateCreatedStart=】
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "/access_king", method = RequestMethod.POST,params="MSM_HEADER=CmdType=FHBX;CmdModule=MULTIQUOTES;CmdId=Search;CmdVer=1.0.0;Token=")
	@ResponseBody
	public String queryMultiQuoteInstanceList(@RequestParam String MSM_PARAM) throws ControllerException {
		
		String uuid = ModelUtil.splitString(MSM_PARAM, "uuid");
		String licensePlate = ModelUtil.splitString(MSM_PARAM, "licensePlate");
		
		String multiQuoteId = ModelUtil.splitString(MSM_PARAM, "multiQuoteId");
		
		String max = ModelUtil.splitString(MSM_PARAM, "max");
		String offset = ModelUtil.splitString(MSM_PARAM, "offset");
		String insuredName = ModelUtil.splitString(MSM_PARAM, "insuredName");
		String status = ModelUtil.splitString(MSM_PARAM, "status");
		String dateCreatedStart = ModelUtil.splitString(MSM_PARAM, "dateCreatedStart");
		
		System.out.println("MultiQuoteInstanceList==>MSM_PARAM："+MSM_PARAM);
		
		if(multiQuoteId.equals("")&&!max.equals("")&&!offset.equals("")){
			return orderInfoService.getMultiQuoteInstanceList(uuid, licensePlate, max, offset, insuredName, status, dateCreatedStart);
		}else{
			return orderInfoService.getMultiQuoteInstanceList(uuid, multiQuoteId, licensePlate);
		}
		
	}

	
	
	/**
	 * 多方报价基本信息查询
	 * 请求方式 POST 请求地址/cpmap/access/
	 * @param MSM_PARAM 参数字符串 【multiQuoteId=0592462981804cbfbd33ed751c055701】
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "/access_king", method = RequestMethod.POST,params="MSM_HEADER=CmdType=FHBX;CmdModule=MULTIQUOTES;CmdId=Get;CmdVer=100;Token=test")
	@ResponseBody
	public String queryMultiQuoteInfo(@RequestParam String MSM_PARAM) throws ControllerException {
		String multiQuoteId = ModelUtil.splitString(MSM_PARAM, "multiQuoteId");
		System.out.println("queryMultiQuoteInfo=="+MSM_PARAM);
		return orderInfoService.getMultiQuoteInfo(multiQuoteId);
	}	
	
	
	/**
	 * 多方报价历史信息查询
	 * 请求方式 POST 请求地址/cpmap/access/
	 * @param MSM_PARAM 参数字符串 【bizTransactionId=2a93ee6985424a0bb7db79dfef235287】
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "/access_king", method = RequestMethod.POST,params="MSM_HEADER=CmdType=FHBX;CmdModule=MULTIQUOTES;CmdId=History;CmdVer=1.0.0;Token=")
	@ResponseBody
	public String queryMultiQuotes(@RequestParam String MSM_PARAM) throws ControllerException {
		
		String bizTransactionId = ModelUtil.splitString(MSM_PARAM, "bizTransactionId");
		System.out.println("queryMultiQuotes=="+MSM_PARAM);
		return "{test}";
	}	
	
	
	/**
	 * 保存投保人信息
	 * 请求方式 POST 请求地址/cpmap/access/
	 * @param MSM_PARAM 参数字符串 【multiQuoteId=63347e140ac44c8d94c843a560450d7a】
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "/access_king", method = RequestMethod.POST,params="MSM_HEADER=CmdType=FHBX;CmdModule=MULTIQUOTES;CmdId=QuoteInfo;CmdVer=100;Token=test")
	@ResponseBody
	public String queryQuoteInfo2(@RequestParam String MSM_PARAM) throws ControllerException {
		
		String multiQuoteId = ModelUtil.splitString(MSM_PARAM, "multiQuoteId");
		System.out.println("queryQuoteInfo2=="+MSM_PARAM);
		return "{test}";
	}	

	/**
	 * 
	 * 请求方式 POST 请求地址/cpmap/access/
	 * @param MSM_PARAM 参数字符串 【functional_id=B88C62ADF49D4343AA76829209CCD614】
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "/access_king", method = RequestMethod.POST,params="MSM_HEADER=CmdType=FHBX;CmdModule=ATM;CmdId=GetFunctionAPI")
	@ResponseBody
	public String queryATM_FunctionAPI(@RequestParam String MSM_PARAM) throws ControllerException {
		
		String functional_id = ModelUtil.splitString(MSM_PARAM, "functional_id");
		System.out.println("queryATM_FunctionAPI=="+MSM_PARAM);
		return "{test}";
	}
	
	/**
	 * 供应商查询接口
	 * 请求方式 POST 请求地址/cpmap/access/
	 * @param MSM_PARAM 参数字符串 【providerId=107】
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "/access_king", method = RequestMethod.POST,params="MSM_HEADER=CmdType=FHBX;CmdModule=PROVIDER;CmdId=Provider;CmdVer=100;Token=test")
	@ResponseBody
	public String queryProviderInfo(@RequestParam String MSM_PARAM) throws ControllerException {
		
		String providerId = ModelUtil.splitString(MSM_PARAM, "providerId");
		System.out.println("queryProviderInfo=="+MSM_PARAM);
		return "{test}";
	}	
	
	
	
	/**
	 * 更新出单网点信息
	 * 请求方式 POST 请求地址/cpmap/access/
	 * @param MSM_PARAM 参数字符串 【enquiryId=000110525152551618196631;content=<businessOffice><id>3000010759</id><name>广东南枫市区新市网点</name></businessOffice>】
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "/access_king", method = RequestMethod.POST,params="MSM_HEADER=CmdType=FHBX;CmdModule=QUOTE;CmdId=BusinessOffice;CmdVer=100;Token=test")
	@ResponseBody
	public String updateBusinessOffice(@RequestParam String MSM_PARAM) throws ControllerException {
		
		String enquiryId = ModelUtil.splitString(MSM_PARAM, "enquiryId");
		String content = ModelUtil.splitString(MSM_PARAM, "content");
		System.out.println("queryQuote_BusinessOffice=="+MSM_PARAM);
		
		return orderInfoService.updateDept(enquiryId, content);
	}	
	
	/**
	 * 
	 * 请求方式 POST 请求地址/cpmap/access/
	 * @param MSM_PARAM 参数字符串 【enquiryId=000110525152551618196631】
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "/access_king", method = RequestMethod.POST,params="MSM_HEADER=CmdType=FHBX;CmdModule=QUOTE;CmdId=RequestQuote;CmdVer=100;Token=test")
	@ResponseBody
	public String queryQuote_RequestQuote(@RequestParam String MSM_PARAM) throws ControllerException {
		
		String enquiryId = ModelUtil.splitString(MSM_PARAM, "enquiryId");
		System.out.println("queryQuote_RequestQuote=="+MSM_PARAM);
		return "{test}";
	}	
	
	/**
	 * 收货地址查询接口
	 * 请求方式 POST 请求地址/cpmap/access/
	 * @param MSM_PARAM 参数字符串 【method=list;uuid=B88C62ADF49D4343AA76829209CCD614;param=】
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "/access_king", method = RequestMethod.POST,params="MSM_HEADER=CmdType=FHBX;CmdModule=DELIVERY;CmdId=METHOD;CmdVer=1.0.0;Token=")
	@ResponseBody
	public String queryDeliveryAddress(@RequestParam String MSM_PARAM) throws ControllerException {
		
		String method = ModelUtil.splitString(MSM_PARAM, "method");
		String uuid = ModelUtil.splitString(MSM_PARAM, "uuid");
		String param = ModelUtil.splitString(MSM_PARAM, "param");
		System.out.println("queryDeliveryAddress=="+MSM_PARAM);
		
		
		return "{test}";
	}	
	
	/**
	 * unknown
	 * 请求方式 POST 请求地址/cpmap/access/
	 * @param MSM_PARAM 参数字符串 【payMethod=Payeco;bizTransactionId=000150333152591031064036】
	 * @return
	 * @throws ControllerException
	 */
	@RequestMapping(value = "/access_king", method = RequestMethod.POST,params="MSM_HEADER=CmdType=FHBX;CmdModule=ORDER;CmdId=PaymentService")
	@ResponseBody
	public String queryPaymentList(@RequestParam String MSM_PARAM) throws ControllerException {
		
		String payMethod = ModelUtil.splitString(MSM_PARAM, "payMethod");
		String bizTransactionId = ModelUtil.splitString(MSM_PARAM, "bizTransactionId");
		System.out.println("queryPaymentList=="+MSM_PARAM);
		
		
		return orderInfoService.getPaymentList(payMethod, bizTransactionId);
	}	
	
	
	
	
	
	/**
	 * 
	 * 请求方式 POST 请求地址/cpmap/access/
	 * @param MSM_PARAM 参数字符串 【zone=440100;channel=2;jobId=620005858 ;comCode=1244191733】
	 * @return
	 * @throws ControllerException
	 */
//	@RequestMapping(value = "/access_king", method = RequestMethod.POST,params="MSM_HEADER=CmdType=FHBX;CmdModule=PROVIDER;CmdId=search")
//	@ResponseBody
//	public String queryProvider(@RequestParam String MSM_PARAM) throws ControllerException {
//		
//		//MSM_HEADER=CmdType=FHBX;CmdModule=PROVIDER;CmdId=search&MSM_PARAM=zone=440100;channel=2;jobId=620005858 ;comCode=1244191733
//		//MSM_HEADER=CmdType=FHBX;CmdModule=PROVIDER;CmdId=search&MSM_PARAM=zone=440100;channel=2;jobId=620005858 ;comCode=1244191733
//		
//		String multiQuoteId = ModelUtil.splitString(MSM_PARAM, "multiQuoteId");
//		System.out.println("queryMultiQuoteInfo=="+MSM_PARAM);
//		return "{test}";
//	}	
	
}
